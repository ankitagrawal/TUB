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
		
		Map<Double, List<InventoryInfo>> availableInvMap = getAvailableInventory(variant, warehouseService.getServiceableWarehouses());
		long netInventory = 0l;
		for(List<InventoryInfo> list : availableInvMap.values()) {
			for (InventoryInfo inventoryInfo : list) {
				netInventory+=inventoryInfo.getQty();
			}
		}
		
		List<InventoryInfo> availableInvList = availableInvMap.remove(variant.getMarkedPrice());
		
		InventoryInfo selectedInfo = null;
		if(availableInvList != null && !availableInvList.isEmpty()) {
			selectedInfo = availableInvList.get(0);
			for (InventoryInfo info : availableInvList) {
				if(selectedInfo.getQty() < info.getQty()) {
					if(selectedInfo.getQty() <= 0) {
						selectedInfo = info;
					} 
				}
			}
		}
		
		if(selectedInfo == null || selectedInfo.getQty() <= 0) {
			for(Map.Entry<Double, List<InventoryInfo>>  entry : availableInvMap.entrySet()) {
				availableInvList = entry.getValue();
				selectedInfo = availableInvList.get(0);
				for (InventoryInfo info : availableInvList) {
					if(selectedInfo.getQty() < info.getQty()) {
						selectedInfo = info;
						break;
					}
				}
			}
		}
		
		if(selectedInfo != null && selectedInfo.getQty() > 0 && netInventory > 0) {
			updateVariant(variant, selectedInfo.getQty(), netInventory, selectedInfo.getMrp(), false);
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
		
		if(!variant.getMarkedPrice().equals(Double.valueOf(mrpQty))) {
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
		
		if(outOfStock) {
			Product product = variant.getProduct();
			List<ProductVariant> inStockVariants = product.getInStockVariants();
			if (inStockVariants != null && inStockVariants.isEmpty()) {
				product.setOutOfStock(true);
				productService.save(product);
			}
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
		query.setParameterList("statusIds", Arrays.asList(EnumOrderStatus.Placed.getId(), EnumOrderStatus.OnHold.getId()));
		
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
			" and c.order_status_id not in (:statusIds)" +
			" and b.shipping_order_status_id in (:sosIds)" +
			" group by a.marked_price, a.sku_id";
	
	private Map<Double, List<InventoryInfo>> getInProcessInventory(ProductVariant productVariant, List<Warehouse> whs) {
		String sql = inProcessInventorySql;
		SQLQuery query = baseDao.createSqlQuery(sql);
		query.addScalar("mrp", Hibernate.DOUBLE);
		query.addScalar("qty", Hibernate.LONG);
		query.addScalar("skuId", Hibernate.LONG);
		
		query.setParameter("pvId", productVariant.getId());
		query.setParameterList("whIds", toWarehouseIds(whs));
		query.setParameterList("statusIds", Arrays.asList(EnumOrderStatus.Placed.getId(), EnumOrderStatus.OnHold.getId()));
		query.setParameterList("sosIds", EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory()));
		
		query.setResultTransformer(Transformers.aliasToBean(InventoryInfo.class));
		
		Map<Double, List<InventoryInfo>> map = new LinkedHashMap<Double, List<InventoryInfo>>();
		
		@SuppressWarnings("unchecked")
		List<InventoryInfo> list = query.list();
		for (InventoryInfo info : list) {
			List<InventoryInfo> infos = map.get(info.getMrp());
			if(infos == null) {
				infos = new ArrayList<InventoryInfo>();
				map.put(info.getMrp(), infos);
			}
			infos.add(info);
		}
		return map;
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
	
	private Collection<InventoryInfo> getCheckedInInventory(ProductVariant productVariant, List<Warehouse> whs) {
		String sql = checkedInInvSql;
		SQLQuery query = baseDao.createSqlQuery(sql);
		
		query.addScalar("skuId", Hibernate.LONG);
		query.addScalar("mrp", Hibernate.DOUBLE);
		query.addScalar("qty", Hibernate.LONG);
		query.addScalar("checkinDate", Hibernate.DATE);
		
		query.setParameter("pvId", productVariant.getId());
		query.setParameterList("whIds", toWarehouseIds(whs));
		query.setParameter("itemStatus", EnumSkuItemStatus.Checked_IN.getId());
		
		query.setResultTransformer(Transformers.aliasToBean(InventoryInfo.class));
		
		@SuppressWarnings("unchecked")
		List<InventoryInfo> list = query.list();
		
		Queue<InventoryInfo> queue = new LinkedList<InventoryInfo>();
		for (InventoryInfo inventoryInfo : list) {
			InventoryInfo info = queue.peek();
			if(info != null && inventoryInfo.getSkuId() == info.getSkuId() && inventoryInfo.getMrp() == info.getMrp()) {
				info.setQty(info.getQty() + inventoryInfo.getQty());
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
	
	private Map<Double,List<InventoryInfo>> getAvailableInventory(ProductVariant productVariant, List<Warehouse> whs) {

		Map<Double, List<InventoryInfo>> availableInvMap = new LinkedHashMap<Double, List<InventoryInfo>>();
		
		Collection<InventoryInfo> availableInvList = getCheckedInInventory(productVariant, whs);
		for (InventoryInfo inventoryInfo : availableInvList) {
			List<InventoryInfo> infos = availableInvMap.get(inventoryInfo.getMrp());
			if(infos == null) {
				infos = new ArrayList<InventoryInfo>();
				availableInvMap.put(inventoryInfo.getMrp(), infos);
			}
			infos.add(inventoryInfo);
		}
		
		Map<Double, List<InventoryInfo>> inProcessMap = getInProcessInventory(productVariant, whs);
		Map<Double, Long> bookedQtyMap = getBookedInventoryQty(productVariant);
		
		for(Map.Entry<Double, List<InventoryInfo>> entry : availableInvMap.entrySet()) {
			Double mrp = entry.getKey();
			
			List<InventoryInfo> inProcessList = inProcessMap.get(mrp);
			if(inProcessList != null) {
				for (InventoryInfo inProcessInfo : inProcessList) {
					for (InventoryInfo info : availableInvList) {
						if(info.getSkuId() == inProcessInfo.getSkuId() && info.getMrp() == inProcessInfo.getMrp()) {
							info.setQty(info.getQty() - inProcessInfo.getQty());
							break;
						}
					}
				}
			}
			
			if(bookedQtyMap.get(mrp) != null) {
				long bookedQty = bookedQtyMap.get(mrp);
				long leftQty = bookedQty;
				
				for (InventoryInfo info : availableInvList) {
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
			}
		}
		return availableInvMap;
	}
	
	@Override
	public List<InventoryInfo> getAvailableInventory(ProductVariant productVariant, Double preferredMrp) {
		return getAvailableInventory(productVariant, warehouseService.getServiceableWarehouses()).get(preferredMrp);
	}

}
