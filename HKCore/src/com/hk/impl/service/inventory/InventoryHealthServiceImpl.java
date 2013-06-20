package com.hk.impl.service.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.hk.domain.sku.Sku;
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
		
		Collection<InventoryInfo> infos = getAvailableInventory(variant, warehouseService.getServiceableWarehouses());
		
		InventoryInfo selectedInfo = null;
		long netInventory = 0l;
		if(infos != null && infos.size() != 0) {
			selectedInfo = removeFirst((List<InventoryInfo>)infos);
		}
		
		if(selectedInfo != null) {
			netInventory = selectedInfo.getQty();
			for (InventoryInfo inventoryInfo : infos) {
				netInventory+=inventoryInfo.getQty();
				if(selectedInfo.getQty() <= 0) {
					selectedInfo = inventoryInfo;
				}
			}
		}
		
		VariantUpdateInfo vInfo = new VariantUpdateInfo();
		vInfo.variant = variant;
		vInfo.netQty = netInventory;

		if(selectedInfo != null && selectedInfo.getQty() > 0 && netInventory > 0) {
			long mxQty = selectedInfo.getMaxQtySkuInfo().getQty();
			if(selectedInfo.getQty() < mxQty) {
				mxQty = selectedInfo.getQty();
			}
			
			SkuInfo mxQtyInfo = selectedInfo.getMaxQtySkuInfo();
			vInfo.mrp = selectedInfo.getMrp();
			vInfo.mrpQty = mxQtyInfo.getQty();
			vInfo.costPrice = mxQtyInfo.getCostPrice();
			vInfo.inStock = true;
		}
		updateVariant(vInfo);
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

	private static class VariantUpdateInfo {
		ProductVariant variant;
		long mrpQty;
		long netQty;
		double mrp;
		double costPrice;
		boolean inStock;
		
	}
	
	private void updateVariant(VariantUpdateInfo vInfo) {
		if(vInfo.mrp != 0l && !vInfo.variant.getMarkedPrice().equals(Double.valueOf(vInfo.mrp))) {
			UpdatePvPrice updatePvPrice = updatePvPriceDao.getPVForPriceUpdate(vInfo.variant, EnumUpdatePVPriceStatus.Pending.getId());
            if (updatePvPrice == null) {
                updatePvPrice = new UpdatePvPrice();
            }
            updatePvPrice.setProductVariant(vInfo.variant);
            updatePvPrice.setOldCostPrice(vInfo.variant.getCostPrice());
            updatePvPrice.setNewCostPrice(vInfo.costPrice);
            updatePvPrice.setOldMrp(vInfo.variant.getMarkedPrice());
            updatePvPrice.setNewMrp(vInfo.mrp);
            updatePvPrice.setOldHkprice(vInfo.variant.getHkPrice());
            Double newHkPrice = vInfo.mrp * (1 - vInfo.variant.getDiscountPercent());
            updatePvPrice.setNewHkprice(newHkPrice);
            updatePvPrice.setTxnDate(new Date());
            updatePvPrice.setStatus(EnumUpdatePVPriceStatus.Pending.getId());
            baseDao.save(updatePvPrice);
		}
		
		if(vInfo.inStock) {
			vInfo.variant.setMarkedPrice(vInfo.mrp);
		}
		vInfo.variant.setNetQty(vInfo.netQty);
		vInfo.variant.setMrpQty(vInfo.mrpQty);
		vInfo.variant.setOutOfStock(!vInfo.inStock);
		vInfo.variant.setCostPrice(vInfo.costPrice);
		
		productVariantService.save(vInfo.variant);
		
		Product product = productService.getProductById(vInfo.variant.getProduct().getId());
		if(!vInfo.inStock) {
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


	private static final String checkedInInvSql = "select c.id as skuId, b.mrp as mrp, b.cost_price as costPrice, " +
			" count(a.id) as qty, b.create_date as checkinDate" +
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
		query.addScalar("costPrice", Hibernate.DOUBLE);
		query.addScalar("checkinDate", Hibernate.DATE);
		
		query.setParameter("pvId", productVariant.getId());
		query.setParameterList("whIds", toWarehouseIds(whs));
		query.setParameter("itemStatus", EnumSkuItemStatus.Checked_IN.getId());
		
		query.setResultTransformer(Transformers.aliasToBean(SkuInfo.class));
		
		@SuppressWarnings("unchecked")
		List<SkuInfo> list = query.list();
		
		LinkedList<SkuInfo> skuList = new LinkedList<SkuInfo>();
		for (SkuInfo inventoryInfo : list) {
			SkuInfo info = getLast(skuList);
			if(info != null && inventoryInfo.getSkuId() == info.getSkuId() && inventoryInfo.getMrp() == info.getMrp()) {
				info.setQty(info.getQty() + inventoryInfo.getQty());
				info.setUnbookedQty(info.getQty());
			} else {
				inventoryInfo.setUnbookedQty(inventoryInfo.getQty());
				skuList.add(inventoryInfo);
			}
		}
		return skuList;
	}
	
	private List<Long> toWarehouseIds(List<Warehouse> whs) {
		List<Long> list = new ArrayList<Long>();
		for (Warehouse wh : whs) {
			list.add(wh.getId());
		}
		
		return list;
	}
	
	private Collection<InventoryInfo> getAvailableInventory(ProductVariant productVariant, List<Warehouse> whs) {
		Collection<SkuInfo> checkedInInvList = getCheckedInInventory(productVariant, whs);
		
		Map<Double, Long> bookedQtyMap = getBookedInventoryQty(productVariant);
		
		List<SkuInfo> inProcessList = getInProcessInventory(productVariant, whs);
		if(inProcessList !=null) {
			for (SkuInfo inProcessInfo : inProcessList) {
				List<SkuInfo> infos = searchBySkuIdAndMrp(checkedInInvList, inProcessInfo.getSkuId(), inProcessInfo.getMrp());
				long leftQty = inProcessInfo.getQty();
				for (SkuInfo skuInfo : infos) {
					long qty = skuInfo.getQty() - leftQty;
					if(qty < 0) {
						leftQty = -qty;
						skuInfo.setQty(0);
						skuInfo.setUnbookedQty(0);
					} else {
						leftQty = 0;
						skuInfo.setQty(qty);
						skuInfo.setUnbookedQty(qty);
					}
				}
			}
		}
		
		List<InventoryInfo> invList = new LinkedList<InventoryInfo>();
		for (SkuInfo skuInfo : checkedInInvList) {
			InventoryInfo info = getLast(invList);
			if(info != null && skuInfo.getMrp() == info.getMrp()) {
				info.setQty(info.getQty() + skuInfo.getQty());
			} else {
				info = new InventoryInfo();
				info.setMrp(skuInfo.getMrp());
				info.setQty(skuInfo.getQty());
				invList.add(info);
			}
			info.addSkuInfo(skuInfo);
		}
		
		for (InventoryInfo inventoryInfo : invList) {
			Long bookedQty = bookedQtyMap.get(inventoryInfo.getMrp());
			if(bookedQty != null) {
				long leftQty = bookedQty;
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
		return invList;
	}
	
	private List<SkuInfo> searchBySkuIdAndMrp(Collection<SkuInfo> list,  long skuId, double mrp) {
		List<SkuInfo> infos = new ArrayList<SkuInfo>();
		for (SkuInfo info : list) {
			if(info.getSkuId() == skuId && info.getMrp() == mrp) {
				infos.add(info);
			}
		}
		return infos;
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
	
	@Override
	public Collection<InventoryInfo> getAvailableInventory(List<Sku> skus) {
		List<Warehouse> whs = new ArrayList<Warehouse>();
		for (Sku sku : skus) {
			whs.add(sku.getWarehouse());
		}
		return getAvailableInventory(skus.get(0).getProductVariant(), whs);
	}
	
	private static <T> T removeFirst(List<T> list) {
		if(list == null || list.size() == 0) return null;
		if(list instanceof LinkedList) {
			try {
				return ((LinkedList<T>) list).removeFirst();
			} catch (Exception e) {
				
			}
		}
		return list.remove(0);
	}
	
	private static <T> T getLast(List<T> list) {
		if(list == null || list.size() == 0) return null;
		if(list instanceof LinkedList) {
			try {
				return ((LinkedList<T>) list).getLast();
			} catch (Exception e) {
				
			}
		}
		return list.get(list.size() -1);
	}
}
