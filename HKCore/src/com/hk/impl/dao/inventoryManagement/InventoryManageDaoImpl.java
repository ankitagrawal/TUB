package com.hk.impl.dao.inventoryManagement;

import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.InventoryManagement.InventoryManageDao;


import java.util.*;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 2:41:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class InventoryManageDaoImpl extends BaseDaoImpl implements InventoryManageDao{


    public Long getNetInventory(Sku sku) {
        return getAvailableUnBookedInventory(Arrays.asList(sku));
    }

    // checked in as always with  owner self
    public Long getAvailableUnBookedInventory(List<Sku> skuList) {
        Long netInv = 0L;
        if (skuList != null && !skuList.isEmpty()) {
            //String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku in (:skuList)";
            String query = "select count(si) from SkuItem si where si.skuGroup.status != :skuStatus and si.skuGroup.sku in (:skuList) and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId();
            netInv = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameter("skuStatus", EnumSkuGroupStatus.UNDER_REVIEW).uniqueResult();
            if (netInv == null) {
                netInv = 0L;
            }
        }
        return netInv;
    }


    public Long getNetInventory(List<Sku> skuList, List<Long> skuItemStatuses){
          Long netInv = 0L;
        if (skuList != null && !skuList.isEmpty()) {
            //String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku in (:skuList)";
            String query = "select count(si) from SkuItem si where si.skuGroup.status != :skuStatus and si.skuGroup.sku in (:skuList) and si.skuItemStatus.id in (:skuItemStatuses) " ;
            netInv = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameterList("skuItemStatuses", skuItemStatuses).setParameter("skuStatus", EnumSkuGroupStatus.UNDER_REVIEW).uniqueResult();
            if (netInv == null) {
                netInv = 0L;
            }
        }
        return netInv;
     }



    public Long getNetInventory(List<Sku> skuList, Double mrp) {
        Long netInv = 0L;
        if (skuList != null && !skuList.isEmpty()) {
            //String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku in (:skuList)";
            String query = "select count(si) from SkuItem si where si.skuGroup.status != :skuStatus and si.skuGroup.sku in (:skuList) " +
                    "and si.skuGroup.mrp = :mrp and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId();
            netInv = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameter("skuStatus", EnumSkuGroupStatus.UNDER_REVIEW).setParameter("mrp", mrp).uniqueResult();
            if (netInv == null) {
                netInv = 0L;
            }
        }
        return netInv;
    }




    public Long getTempOrBookedQtyOfProductVariantInQueue(ProductVariant productVariant, List<Long> skuItemStatusId, List <Long> skuItemOwnerStatusId) {
        String query = "select count(si) from SkuItem si where si.skugroup.productVariant = :productVariant and si.skuItemStatus.id= :skuItemStatusId and  si.skuItemOwner.id= :skuItemOwnerStatusId";
        return (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("skuItemStatusId", skuItemStatusId).setParameter("skuItemOwnerStatusId", skuItemOwnerStatusId).uniqueResult();

    }


    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory) {
        Long netInventory = getAvailableUnBookedInventory(skuList);
        logger.debug("net inventory " + netInventory);

        Long bookedInventory = 0L;
        if (!skuList.isEmpty()) {
            ProductVariant productVariant = skuList.get(0).getProductVariant();
//            bookedInventory = getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemOwner.SELF.getId()) + getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.BOOKED.getId(), EnumSkuItemOwner.SELF.getId());
            logger.debug("booked inventory " + bookedInventory);
        }

        return (netInventory - bookedInventory);

    }


    public List<SkuItem> getCheckedInSkuItems(Sku sku, Double mrp) {
        String sql = " from SkuItem si where si.skuGroup.sku =:sku and si.skuGroup.mrp =:mrp and si.skuItemStatus.id = :skuItemStatusId and (si.skuGroup.status != :reviewStatus or si.skuGroup.status is null)";
        Query query = getSession().createQuery(sql).setParameter("sku", sku).setParameter("mrp", mrp).setParameter("skuItemStatusId", EnumSkuItemStatus.Checked_IN.getId()).setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW);
        return query.list();

    }



    public Long getAvailableUnbookedInventory(List<Sku> skuList,Double mrp ,boolean addBrightInventory) {
           Long netInventory = getNetInventory(skuList, mrp);
           logger.debug("net inventory " + netInventory);

           Long bookedInventory = 0L;
           if (!skuList.isEmpty()) {
               ProductVariant productVariant = skuList.get(0).getProductVariant();
//               bookedInventory = getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemOwner.SELF.getId()) + getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.BOOKED.getId(), EnumSkuItemOwner.SELF.getId());
               logger.debug("booked inventory " + bookedInventory);
           }

           return (netInventory - bookedInventory);

       }


     public Double getFirstcheckedInBatchMRP(ProductVariant productVariant) {
        String sql = "Select si.skuGroup.mrp from SkuItem si where si.skuGroup.sku.productVariant =:productVariant and  si.skuItemStatus.id = :skuItemStatusId and ( si.skuGroup.status != :reviewStatus or si.skuGroup.status is null ) order by si.skuGroup.createDate asc  ";
        Query query = getSession().createQuery(sql).setParameter("productVariant", productVariant).setParameter("skuItemStatusId", EnumSkuItemStatus.Checked_IN.getId()).setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW);
        return (Double)query.list().get(0);

    }





}
