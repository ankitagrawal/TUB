package com.hk.impl.dao.inventoryManagement;

import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.InventoryManagement.ProductVariantInventoryDao;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 2:41:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductVariantInventoryDaoImpl extends BaseDaoImpl implements ProductVariantInventoryDao {


    public Long getNetInventory(Sku sku) {
        return getNetInventory(Arrays.asList(sku));
    }

    // checked in as always with  owner self
    public Long getNetInventory(List<Sku> skuList) {
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


    public Long getTempOrBookedQtyOfProductVariantInQueue(ProductVariant productVariant, Long skuItemStatusId, Long skuItemOwnerStatusId) {
        String query = "select count(*) from SkuItem si where si.skugroup.productVariant = :productVariant and si.skuItemStatus.id= :skuItemStatusId and  si.skuItemOwner.id= :skuItemOwnerStatusId";
        return (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("skuItemStatusId", skuItemStatusId).setParameter("skuItemOwnerStatusId", skuItemOwnerStatusId).uniqueResult();

    }


    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory) {
        Long netInventory = getNetInventory(skuList);
        logger.debug("net inventory " + netInventory);

        Long bookedInventory = 0L;
        if (!skuList.isEmpty()) {
            ProductVariant productVariant = skuList.get(0).getProductVariant();
            bookedInventory = getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemOwner.SELF.getId()) + getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.BOOKED.getId(), EnumSkuItemOwner.SELF.getId());
            logger.debug("booked inventory " + bookedInventory);
        }

        return (netInventory - bookedInventory);

    }


    public List<SkuItem> getSkuItems(Sku sku, Double mrp) {
        String sql = " from SkuItem si where si.skuGroup.sku =:sku and si.skuGroup.sku.mrp =:mrp and si.skuItemStatus.id = :skuItemStatusId and (si.skuGropu.status != :reviewStatus or si.skGroup.status is null)";
        Query query = getSession().createQuery(sql).setParameter("sku", sku).setParameter("mrp", mrp).setParameter("skuItemStatusId", EnumSkuItemStatus.Checked_IN.getId()).setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW);
        return query.list();

    }


}
