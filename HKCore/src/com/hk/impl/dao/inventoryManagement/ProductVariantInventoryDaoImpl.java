package com.hk.impl.dao.inventoryManagement;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.InventoryManagement.ProductVariantInventoryDao;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.order.EnumOrderStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

   


//////////

    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant) {
        String query = "select count(*) from SkuItemCLI sici where sici.productVariant = :productVariant and sici.skuItem.skuItemStatus.id=" + EnumSkuItemStatus.TEMP_BOOKED.getId();
        Long qtyInQueue = (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).uniqueResult();
        return qtyInQueue;
    }


    public Long getActualBookedQtyOfProductVariant(ProductVariant productVariant) {
        String query = "select count(*) from SkuItemLineItem  siLi where siLi.productVariant = :productVariant and siLi.skuItem.skuItemStatus.id=" + EnumSkuItemStatus.BOOKED.getId();
        Long qtyActualBooked = (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).uniqueResult();
        return qtyActualBooked;
    }


    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory) {

        Long netInventory = getNetInventory(skuList);
        logger.debug("net inventory " + netInventory);

        Long bookedInventory = 0L;
        if (!skuList.isEmpty()) {
            ProductVariant productVariant = skuList.get(0).getProductVariant();
            bookedInventory = getBookedQtyOfProductVariantInQueue(productVariant) + getActualBookedQtyOfProductVariant(productVariant);
            logger.debug("booked inventory " + bookedInventory);
        }

        return (netInventory - bookedInventory);

    }


    ////////


    public List<SkuItemCLI> getListSkuItemCLI(CartLineItem cartLineItem) {
        String query = "from SkuItemCLI sici where sici.cartLineItem = :cartLineItem";
        return (Lis<SkuItemCLI>) getSession().createQuery(query).setParameter("cartLineItem", cartLineItem).list();

    }


}
