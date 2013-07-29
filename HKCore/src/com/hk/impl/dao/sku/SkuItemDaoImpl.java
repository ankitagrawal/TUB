package com.hk.impl.dao.sku;

import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.warehouse.EnumWarehouseType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.*;

import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.warehouse.WarehouseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class SkuItemDaoImpl extends BaseDaoImpl implements SkuItemDao {
    @Autowired
    SkuGroupDao skuGroupDao;
    @Autowired
    WarehouseDao warehouseDao;

    /*public List<SkuGroup> getInStockSkuGroups(Sku sku) {
         List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
         String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem is not null " +
                 "and pvi.sku = :sku group by pvi.skuItem.id having sum(pvi.qty) > 0";
         List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery)
                 .setParameter("sku", sku)
                 .list();
         if (skuItemIdList != null && skuItemIdList.size() > 0) {
             String query = "select distinct si.skuGroup from SkuItem si where si.id in (:skuItemIdList) " +
                     "and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc ";

             skuGroupList = (List<SkuGroup>) getSession().createQuery(query)
                     .setParameterList("skuItemIdList", skuItemIdList)
                     .setParameter("sku", sku)
                     .list();
         }
         return skuGroupList;
     }*/

    public List<SkuGroup> getInStockSkuGroups(Sku sku) {
        String query = "select distinct si.skuGroup from SkuItem si where si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId() +
                " and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc ";
        List<SkuGroup> skuGroupList = findByNamedParams(query, new String[]{"sku"}, new Object[]{sku});
        //List<SkuGroup> skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameter("sku", sku).list();

        if (skuGroupList == null) {
            skuGroupList = new ArrayList<SkuGroup>(0);
        }
        return skuGroupList;
    }

    public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty) {
        List<Warehouse> warehouseList =  warehouseDao.getAllWarehouses(EnumWarehouseType.Online_B2B.getId(), Boolean.TRUE, Boolean.TRUE);
        SkuGroup minMRPUnbookedSkuGroup = null;
        String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi " +
            "where pvi.skuItem is not null and pvi.skuItem.skuGroup.mrp is not null and pvi.sku.warehouse in (:warehouseList) and pvi.sku.productVariant = :productVariant " +
            "group by pvi.skuItem.id having sum(pvi.qty) > 0 order by pvi.skuItem.skuGroup.mrp asc";
        List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery)
            .setParameter("productVariant", productVariant)
            .setParameterList("warehouseList", warehouseList).list();
        if (skuItemIdList != null && skuItemIdList.size() > bookedQty) {
            List<Long> firstUnBookedSkuItem = skuItemIdList.subList(bookedQty.intValue(), bookedQty.intValue() + 1);
            String query = "select distinct si.skuGroup from SkuItem si where si.id = :skuItemId order by si.skuGroup.mrp asc";
            minMRPUnbookedSkuGroup = (SkuGroup) getSession().createQuery(query).setParameter("skuItemId", firstUnBookedSkuItem.get(0)).uniqueResult();
        }
        return minMRPUnbookedSkuGroup;
    }


    private DetachedCriteria getSkuItemCriteria(SkuGroup skuGroup, SkuItemStatus skuItemStatus) {
        DetachedCriteria skuItemCriteria = DetachedCriteria.forClass(SkuItem.class);
        if (skuGroup != null) {
            skuItemCriteria.add(Restrictions.eq("skuGroup", skuGroup));
        }
        if (skuItemStatus != null) {
            skuItemCriteria.add(Restrictions.eq("skuItemStatus", skuItemStatus));
        }
        return skuItemCriteria;
    }


    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
        if (skuGroup == null) {
            return new ArrayList<SkuItem>();
        }
        DetachedCriteria skuItemCriteria = getSkuItemCriteria(skuGroup, EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        return findByCriteria(skuItemCriteria);
    }

    public SkuItem getSkuItem(SkuGroup skuGroup, SkuItemStatus skuItemStatus) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SkuItem.class);
        criteria.add(Restrictions.eq("skuGroup", skuGroup));
        criteria.add(Restrictions.eq("skuItemStatus", skuItemStatus));
        List<SkuItem> skuItems = (List<SkuItem>) findByCriteria(criteria);
        return skuItems == null || skuItems.isEmpty() ? null : skuItems.get(0);
    }

    public SkuItem getSkuItem(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatusList) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SkuItem.class);
        criteria.add(Restrictions.eq("skuGroup", skuGroup));
        criteria.add(Restrictions.in("skuItemStatus", skuItemStatusList));
        List<SkuItem> skuItems = (List<SkuItem>) findByCriteria(criteria);
        return skuItems == null || skuItems.isEmpty() ? null : skuItems.get(0);
    }


    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId) {
        String sql = "select si from SkuItem si where si.barcode = :barcode and si.skuGroup.sku.warehouse.id = :warehouseId ";
        if (statusId != null) {
            sql = sql + "and si.skuItemStatus.id = :statusId ";
        }
        Query query = getSession().createQuery(sql).setParameter("barcode", barcode).setParameter("warehouseId", warehouseId);
        if (statusId != null) {
            query.setParameter("statusId", statusId);
        }
        List<SkuItem> skuItems = query.list();
	      if(skuItems != null && skuItems.size() > 1){
		      logger.error(" barcode -> " + barcode + " resulting in more than on sku_item in warehouse id " + warehouseId);
	      }
        return skuItems != null && !skuItems.isEmpty() ? skuItems.get(0) : null;
    }

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusList, List<SkuItemOwner> skuItemOwners) {
        String sql = "select si from SkuItem si where si.barcode = :barcode and si.skuGroup.sku.warehouse.id = :warehouseId ";
        if (skuItemStatusList != null && skuItemStatusList.size() > 0) {
            sql = sql + "and si.skuItemStatus  in (:skuItemStatusList) ";
        }
        if(skuItemOwners!=null &&skuItemOwners.size()>0){
        	sql = sql+ "and si.skuItemOwner in (:skuItemOwners)";
        }
        Query query = getSession().createQuery(sql).setParameter("barcode", barcode).setParameter("warehouseId", warehouseId);
        if (skuItemStatusList != null && skuItemStatusList.size() > 0) {
            query.setParameterList("skuItemStatusList", skuItemStatusList);
        }
        if(skuItemOwners!=null &&skuItemOwners.size()>0){
        	query.setParameterList("skuItemOwners", skuItemOwners);
        }
       
        List<SkuItem> skuItems = query.list();
        if(skuItems != null && skuItems.size() > 1){
            logger.error(" barcode -> " + barcode + " resulting in more than on sku_item in warehouse id " + warehouseId);
        }
        return skuItems != null && !skuItems.isEmpty() ? skuItems.get(0) : null;
    }

    public List<SkuItem> getCheckedInSkuItems(Sku sku) {
        String sql = "from SkuItem si where  si.skuItemStatus.id =  :checkedInStatusId  and  si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc";
        Query query = getSession().createQuery(sql).setParameter("sku", sku).setParameter("checkedInStatusId", EnumSkuItemStatus.Checked_IN.getId());
        return query.list();
    }
    
    public List<SkuItem> getSkuItem(Sku sku, Long id){
        String sql = "from SkuItem si where  si.skuItemStatus.id =  :checkedInStatusId  and  si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc";
        Query query = getSession().createQuery(sql).setParameter("sku", sku).setParameter("checkedInStatusId", id);
        return query.list();
    }
    public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> statusIds, List<SkuItemOwner> skuItemOwners, Double mrp){
        String sql = "from SkuItem si where si.skuGroup.sku in (:skuList)";
        
        if(statusIds!=null && statusIds.size()>0){
        	sql+="and si.skuItemStatus.id in (:statusIds)";
        }
        if(skuItemOwners!=null && skuItemOwners.size()>0){
        	sql+="and si.skuItemOwner in (:skuItemOwners)";
        }
        if(mrp != null){
        	sql += " and si.skuGroup.mrp = :mrp " ;
        }
        String orderByClause = " order by si.skuGroup.expiryDate asc";
        sql+=orderByClause;
        Query query = getSession().createQuery(sql).setParameterList("skuList", skuList);
        if(statusIds!=null && statusIds.size()>0){
        	query.setParameterList("statusIds", statusIds);
        }
        if(skuItemOwners!=null && skuItemOwners.size()>0){
        	query.setParameterList("skuItemOwners", skuItemOwners);
        }
        if(mrp!=null){
        	query.setParameter("mrp", mrp);
        }
        return query.list();
    }

    public SkuItem getSkuItemWithStatusAndOwner(SkuGroup skuGroup, SkuItemStatus skuItemStatus, SkuItemOwner skuItemOwner){
        String sql;
        Query query = null;
        if(skuItemOwner!=null){
            sql = "from SkuItem si where si.skuGroup =:skuGroup and si.skuItemStatus = :skuItemStatus and si.skuItemOwner = :skuItemOwner";
            query = getSession().createQuery(sql).setParameter("skuGroup", skuGroup).setParameter("skuItemStatus", skuItemStatus).setParameter("skuItemOwner", skuItemOwner);
        }
        else{
            sql = "from SkuItem si where si.skuGroup =:skuGroup and si.skuItemStatus = :skuItemStatus";
            query = getSession().createQuery(sql).setParameter("skuGroup", skuGroup).setParameter("skuItemStatus", skuItemStatus);
        }
        List<SkuItem> skuItems = query.list();
        return skuItems != null && !skuItems.isEmpty() ? skuItems.get(0) : null;
    }

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId, SkuItemOwner skuItemOwner){
        String sql = "select si from SkuItem si where si.barcode = :barcode and si.skuGroup.sku.warehouse.id = :warehouseId ";
        if (statusId != null) {
            sql = sql + "and si.skuItemStatus.id = :statusId ";
        }
        if(skuItemOwner != null){
            sql = sql + "and si.skuItemOwner = :skuItemOwner";
        }
        Query query = getSession().createQuery(sql).setParameter("barcode", barcode).setParameter("warehouseId", warehouseId);
        if (statusId != null) {
            query.setParameter("statusId", statusId);
        }
        if(skuItemOwner != null){
            query.setParameter("skuItemOwner",skuItemOwner);
        }
        List<SkuItem> skuItems = query.list();
        if(skuItems != null && skuItems.size() > 1){
            logger.error(" barcode -> " + barcode + " resulting in more than on sku_item in warehouse id " + warehouseId);
        }
        return skuItems != null && !skuItems.isEmpty() ? skuItems.get(0) : null;
    }
    
}











