package com.hk.impl.service.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.catalog.product.EnumUpdatePVPriceStatus;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.product.UpdatePvPriceDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryHealthService;

@Service
public class InventoryHealthServiceImpl implements InventoryHealthService {

	@Autowired ProductVariantService productVariantService;
	@Autowired ProductService productService;
	@Autowired WarehouseService warehouseService;
	@Autowired BaseDao baseDao;
	@Autowired UpdatePvPriceDao updatePvPriceDao;

	@Override
	@Transactional
	public void checkInventoryHealth(ProductVariant variant) {
		Product product = variant.getProduct();
		boolean updateHealth = (product.isJit() != null && !product.isJit())
				&& (product.isService() != null && !product.isService()) && !product.getDropShipping();

		if (!updateHealth) {
			return;
		}
		
		Queue<InventoryInfo> infos = getAvailableInventory(variant, warehouseService.getServiceableWarehouses());
		InventoryInfo selectedInfo = infos.poll();
		long netInventory = selectedInfo.getQty();
		for (InventoryInfo inventoryInfo : infos) {
			netInventory+=inventoryInfo.getQty();
			if(selectedInfo.getQty() <= 0) {
				selectedInfo = inventoryInfo;
			}
		}
		
		if(selectedInfo != null && selectedInfo.getQty() > 0 && netInventory > 0) {
			updateVariant(variant, selectedInfo.getMaxQtySkuInfo().getQty(), netInventory, selectedInfo.getMrp(), false);
		} else {
			updateVariant(variant, 0l, netInventory, null, true);
		}
		
	}
	
	@Override
	public long getAvailableUnbookedInventory(ProductVariant productVariant) {
		if(productVariant.getMrpQty() == null) {
			checkInventoryHealth(productVariant);
		}
		productVariant = productVariantService.getVariantById(productVariant.getId());
		if(productVariant.getMrpQty() == null) {
			return 0l;
		}
		return productVariant.getMrpQty();
	}

	private void updateVariant(ProductVariant variant, Long mrpQty, Long netQty, Double mrp, boolean outOfStock) {
		
		if(mrp != null && !variant.getMarkedPrice().equals(Double.valueOf(mrp))) {
			UpdatePvPrice updatePvPrice = updatePvPriceDao.getPVForPriceUpdate(variant, EnumUpdatePVPriceStatus.Pending.getId());
            if (updatePvPrice == null) {
                updatePvPrice = new UpdatePvPrice();
            }
            updatePvPrice.setProductVariant(variant);
            updatePvPrice.setOldCostPrice(variant.getCostPrice());
            updatePvPrice.setNewCostPrice(variant.getCostPrice());
            updatePvPrice.setOldMrp(variant.getMarkedPrice());
            updatePvPrice.setNewMrp(mrp);
            updatePvPrice.setOldHkprice(variant.getHkPrice());
            Double newHkPrice = mrp * (1 - variant.getDiscountPercent());
            updatePvPrice.setNewHkprice(newHkPrice);
            updatePvPrice.setTxnDate(new Date());
            updatePvPrice.setStatus(EnumUpdatePVPriceStatus.Pending.getId());
            baseDao.save(updatePvPrice);
		}
		
		if(mrp != null) {
			variant.setMarkedPrice(mrp);
		}
		variant.setNetQty(netQty);
		variant.setMrpQty(mrpQty);
		
		variant.setOutOfStock(outOfStock);
		productVariantService.save(variant);
		
		Product product = variant.getProduct();
		if(outOfStock) {
			List<ProductVariant> inStockVariants = product.getInStockVariants();
			if (inStockVariants != null && inStockVariants.isEmpty()) {
				product.setOutOfStock(true);
				productService.save(product);
			}
		} else {
			product.setOutOfStock(false);
			productService.save(product);
		}
	}

	private static final String bookedInventorySql = "select a.marked_price as mrp, sum(a.qty) as qty" +
			  " from cart_line_item as a inner join base_order as b on a.order_id = b.id" +
			  " where a.product_variant_id = :pvId " +
			  " and b.order_status_id in (:statusIds) " +
			  " group by a.marked_price";
	
	private Map<Double, Long> getBookedInventoryQty(ProductVariant productVariant) {
		String sql = bookedInventorySql;
		SQLQuery query = baseDao.createSqlQuery(sql);
		query.addScalar("mrp", Hibernate.DOUBLE);
		query.addScalar("qty", Hibernate.LONG);
		
		query.setParameter("pvId", productVariant.getId());
		query.setParameterList("statusIds", Arrays.asList(EnumOrderStatus.Placed.getId()));
		
		Map<Double, Long> map = new LinkedHashMap<Double, Long>();
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.list();
		if(list != null) {
			for (Object[] data : list) {
				map.put((Double) data[0], (Long)data[1]);
			}
		}
		return map;
	}
	
	
	private static final String inProcessInventorySql = "select a.marked_price as mrp, sum(a.qty) as qty, a.sku_id as skuId" +
			" from line_item as a inner join shipping_order as b on a.shipping_order_id = b.id" +
			" inner join base_order as c on b.base_order_id = c.id" +
			" inner join sku as e on a.sku_id = e.id" +
			" where e.product_variant_id = :pvId" +
			" and e.warehouse_id in (:whIds)" +
			" and c.order_status_id in (:statusIds)" +
			" and b.shipping_order_status_id in (:sosIds)" +
			" group by a.marked_price, a.sku_id";
	
	private List<SkuInfo> getInProcessInventory(ProductVariant productVariant, List<Warehouse> whs) {
		String sql = inProcessInventorySql;
		SQLQuery query = baseDao.createSqlQuery(sql);
		query.addScalar("mrp", Hibernate.DOUBLE);
		query.addScalar("qty", Hibernate.LONG);
		query.addScalar("skuId", Hibernate.LONG);
		
		query.setParameter("pvId", productVariant.getId());
		query.setParameterList("whIds", toWarehouseIds(whs));
		query.setParameterList("statusIds", Arrays.asList(EnumOrderStatus.InProcess.getId(), EnumOrderStatus.OnHold.getId()));
		query.setParameterList("sosIds", EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory()));
		
		query.setResultTransformer(Transformers.aliasToBean(SkuInfo.class));
		
		@SuppressWarnings("unchecked")
		List<SkuInfo> list = query.list();
		return list;
	}


	private static final String checkedInInvSql = "select c.id as skuId, b.mrp as mrp, count(a.id) as qty, b.create_date as checkinDate" +
			" from sku_item as a" +
			" inner join sku_group as b on a.sku_group_id = b.id" +
			" inner join sku as c on b.sku_id = c.id" +
			" where c.product_variant_id = :pvId" +
			" and c.warehouse_id in (:whIds)" +
			" and a.sku_item_status_id = :itemStatus" +
			" and b.mrp is not null" +
			" group by b.id" +
			" order by checkinDate asc";
	
	private Collection<SkuInfo> getCheckedInInventory(ProductVariant productVariant, List<Warehouse> whs) {
		String sql = checkedInInvSql;
		SQLQuery query = baseDao.createSqlQuery(sql);
		
		query.addScalar("skuId", Hibernate.LONG);
		query.addScalar("mrp", Hibernate.DOUBLE);
		query.addScalar("qty", Hibernate.LONG);
		query.addScalar("checkinDate", Hibernate.DATE);
		
		query.setParameter("pvId", productVariant.getId());
		query.setParameterList("whIds", toWarehouseIds(whs));
		query.setParameter("itemStatus", EnumSkuItemStatus.Checked_IN.getId());
		
		query.setResultTransformer(Transformers.aliasToBean(SkuInfo.class));
		
		@SuppressWarnings("unchecked")
		List<SkuInfo> list = query.list();
		
		Queue<SkuInfo> queue = new LinkedList<SkuInfo>();
		for (SkuInfo inventoryInfo : list) {
			SkuInfo info = queue.peek();
			if(info != null && inventoryInfo.getSkuId() == info.getSkuId() && inventoryInfo.getMrp() == info.getMrp()) {
				info.setQty(info.getQty() + inventoryInfo.getQty());
				info.setUnbookedQty(info.getQty());
			} else {
				queue.add(inventoryInfo);
			}
		}
		
		return queue;
	}
	
	private List<Long> toWarehouseIds(List<Warehouse> whs) {
		List<Long> list = new ArrayList<Long>();
		for (Warehouse wh : whs) {
			list.add(wh.getId());
		}
		
		return list;
	}
	
	private Queue<InventoryInfo> getAvailableInventory(ProductVariant productVariant, List<Warehouse> whs) {
		Collection<SkuInfo> checkedInInvList = getCheckedInInventory(productVariant, whs);
		
		Map<Double, Long> bookedQtyMap = getBookedInventoryQty(productVariant);
		
		List<SkuInfo> inProcessList = getInProcessInventory(productVariant, whs);
		if(inProcessList !=null) {
			for (SkuInfo inProcessInfo : inProcessList) {
				SkuInfo info = searchBySkuIdAndMrp(checkedInInvList, inProcessInfo.getSkuId(), inProcessInfo.getMrp());
				if(info != null) {
					info.setQty(info.getQty() - inProcessInfo.getQty());
					info.setUnbookedQty(info.getQty());
				}
			}
		}
		
		Queue<InventoryInfo> queue = new LinkedList<InventoryInfo>();
		for (SkuInfo skuInfo : checkedInInvList) {
			InventoryInfo info = queue.peek();
			if(info != null && skuInfo.getMrp() == info.getMrp()) {
				info.setQty(info.getQty() + skuInfo.getQty());
			} else {
				info = new InventoryInfo();
				info.setMrp(skuInfo.getMrp());
				info.setQty(skuInfo.getQty());
				queue.add(info);
			}
			info.addSkuInfo(skuInfo);
		}
		
		for (InventoryInfo inventoryInfo : queue) {
			Long bookedQty = bookedQtyMap.get(inventoryInfo.getMrp());
			long leftQty = bookedQty;
			if(bookedQty != null) {
				for (SkuInfo info : inventoryInfo.getSkuInfoList()) {
					long netQty = 0l;
					if(leftQty == bookedQty) {
						netQty = info.getQty() - bookedQty;
						leftQty = netQty;
					} else if(leftQty < 0) {
						netQty = info.getQty() + leftQty;
						leftQty = netQty;
					} else if (leftQty >= 0) {
						netQty = info.getQty();
						leftQty = netQty;
					}
					info.setQty(netQty);
				}
				inventoryInfo.setQty(inventoryInfo.getQty() - bookedQty);
				bookedQtyMap.remove(inventoryInfo.getMrp());
			}
		}
		return queue;
	}
	
	private SkuInfo searchBySkuIdAndMrp(Collection<SkuInfo> list,  long skuId, double mrp) {
		for (SkuInfo info : list) {
			if(info.getSkuId() == skuId && info.getMrp() == mrp) {
				return info;
			}
		}
		return null;
	}
	
	@Override
	public InventoryInfo getAvailableInventory(ProductVariant productVariant, Double preferredMrp) {
		Collection<InventoryInfo> infos = getAvailableInventory(productVariant, warehouseService.getServiceableWarehouses());
		for (InventoryInfo inventoryInfo : infos) {
			if(inventoryInfo.getMrp() == preferredMrp) {
				return inventoryInfo;
			}
		}
		return null;
	}
}
