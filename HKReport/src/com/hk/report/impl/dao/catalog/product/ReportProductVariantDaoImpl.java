
package com.hk.report.impl.dao.catalog.product;

import java.util.Date;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.pact.dao.catalog.product.ReportProductVariantDao;

@Repository
public class ReportProductVariantDaoImpl extends BaseDaoImpl implements ReportProductVariantDao {
    

    
    @SuppressWarnings("unchecked")
    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate) {
        return (List<InventorySoldDto>) getSession().createQuery(
                "select count(li.id) as countSold,li.sku.productVariant.product.id as productId,"
                        + " li.sku.productVariant.product.name as productName"
                        + " from LineItem li"
                        + " where li.shippingOrder.shippingOrderStatus.id in (180,190) and li.shippingOrder.shipment.shipDate  > :startDate and li.shippingOrder.shipment.shipDate  < :endDate "
                        + " group by li.sku.productVariant.product.id  order by count(li.id) desc").setParameter("startDate", startDate).setParameter("endDate", endDate).setResultTransformer(
                Transformers.aliasToBean(InventorySoldDto.class)).list();
    }

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId) {
        return (InventorySoldDto) getSession().createQuery(
                "select count(li.id) as countSold,li.sku.productVariant.product.id as productId," + " li.sku.productVariant.product.name as productName" + " from LineItem li"
                        + " where  li.shippingOrder.shippingOrderStatus.id in (180,190) and li.shippingOrder.shipment.shipDate > :startDate "
                        + " and li.shippingOrder .shipment.shipDate < :endDate " + " and li.sku.productVariant.product.id = :productId order by count(li.id) desc ").setParameter(
                "startDate", startDate).setParameter("endDate", endDate).setParameter("productId", productId).setResultTransformer(Transformers.aliasToBean(InventorySoldDto.class)).uniqueResult();
    }


}
