package com.hk.admin.impl.dao.inventory;

import java.util.Date;
import java.util.List;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.BaseDao;

@SuppressWarnings("unchecked")
@Repository
public class PurchaseOrderDaoImpl extends BaseDaoImpl implements PurchaseOrderDao {
	
	@Autowired
	BaseDao baseDao;

    public List<PurchaseOrder> listPurchaseOrdersExcludingStatus(List<Long> purchaseOrderStatusList) {
        return (List<PurchaseOrder>) getSession().createQuery("from PurchaseOrder o where o.purchaseOrderStatus.id not in (:purchaseOrderStatusList)").setParameterList(
                "purchaseOrderStatusList", purchaseOrderStatusList).list();
    }

    public List<PurchaseOrder> listPurchaseOrdersWithProductVariant(ProductVariant productVariant) {
        return (List<PurchaseOrder>) getSession().createQuery("select distinct poli.purchaseOrder from PoLineItem poli where poli.sku.productVariant = (:productVariant)").setParameter(
                "productVariant", productVariant).list();
    }

    public List<PurchaseOrder> searchPO(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber,
                                        String tinNumber, String supplierName, Warehouse warehouse, Boolean extraInventoryCreated, Date startDate, Date endDate, String poType) {
        return findByCriteria(getPurchaseOrderCriteria(purchaseOrder, purchaseOrderStatus, approvedBy, createdBy, invoiceNumber,
                tinNumber, supplierName, warehouse, extraInventoryCreated, startDate, endDate, poType));
    }

    public Page searchPO(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber,
                         String tinNumber, String supplierName, Warehouse warehouse, Boolean extraInventoryCreated, int pageNo, int perPage, Date startDate, Date endDate, String poType) {
        return list(getPurchaseOrderCriteria(purchaseOrder, purchaseOrderStatus, approvedBy, createdBy, invoiceNumber,
                tinNumber, supplierName, warehouse, extraInventoryCreated, startDate, endDate, poType), pageNo, perPage);
    }

    private DetachedCriteria getPurchaseOrderCriteria(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber,
                                                      String tinNumber, String supplierName, Warehouse warehouse, Boolean extraInventoryCreated, Date startDate, Date endDate, String poType) {
        DetachedCriteria purchaseOrderCriteria = DetachedCriteria.forClass(PurchaseOrder.class);
        DetachedCriteria supplierCriteria = null;
        if (purchaseOrder != null) {
            purchaseOrderCriteria.add(Restrictions.eq("id",purchaseOrder.getId()));
        }
        if (purchaseOrderStatus != null) {
            purchaseOrderCriteria.add(Restrictions.eq("purchaseOrderStatus", purchaseOrderStatus));
        }
        if (approvedBy != null) {
            purchaseOrderCriteria.add(Restrictions.eq("approvedBy", approvedBy));
        }
        if (createdBy != null) {
            purchaseOrderCriteria.add(Restrictions.eq("createdBy", createdBy));
        }
        if (StringUtils.isNotBlank(invoiceNumber)) {
            purchaseOrderCriteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        }
        if (warehouse != null) {
            purchaseOrderCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        if (StringUtils.isNotBlank(tinNumber)) {
            if (supplierCriteria == null) {
                supplierCriteria = purchaseOrderCriteria.createCriteria("supplier");
            }
            supplierCriteria.add(Restrictions.eq("tinNumber", tinNumber));
        }
        if (StringUtils.isNotBlank(supplierName)) {
            if (supplierCriteria == null) {
                supplierCriteria = purchaseOrderCriteria.createCriteria("supplier");
            }
            supplierCriteria.add(Restrictions.like("name", "%" + supplierName + "%"));
        }
      if(extraInventoryCreated!=null){
        purchaseOrderCriteria.add(Restrictions.eq("isExtraInventoryCreated",extraInventoryCreated));
      }
      if(StringUtils.isNotBlank(poType)){
            purchaseOrderCriteria.add(Restrictions.eq("purchaseOrder.purchaseOrderType.name",poType));
      }
        if(startDate != null && endDate != null){
            purchaseOrderCriteria.add(Restrictions.between("createDate",startDate, endDate));
        }
        purchaseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));

        return purchaseOrderCriteria;
    }


    public List<PurchaseOrder> listPurchaseOrdersIncludingStatus(List<Long> purchaseOrderStatusList) {
        return (List<PurchaseOrder>) getSession().createQuery("from PurchaseOrder o where o.purchaseOrderStatus.id in (:purchaseOrderStatusList)").setParameterList(
                "purchaseOrderStatusList", purchaseOrderStatusList).list();
    }

    public List<PurchaseOrder> listPurchaseOrdersExcludingStatusInTimeFrame(List<Long> purchaseOrderStatusList, Date startDate, Date endDate) {
        return (List<PurchaseOrder>) getSession().createQuery(
                "from PurchaseOrder o where o.purchaseOrderStatus.id not in (:purchaseOrderStatusList) " + " and o.poDate >= (:startDate) and o.poDate <= (:endDate) ").setParameterList(
                "purchaseOrderStatusList", purchaseOrderStatusList).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
    }
    public boolean isPiCreated(PurchaseOrder purchaseOrder){
        List<GoodsReceivedNote> goodsReceivedNotes= purchaseOrder.getGoodsReceivedNotes();
        for (GoodsReceivedNote grn :goodsReceivedNotes )    {
                if(grn.getPurchaseInvoices()!=null && grn.getPurchaseInvoices().size()>0 ) {
                    return true;
                }
             }
        return false;
    }
    
    public List<ProductVariant> getAllProductVariantFromPO(PurchaseOrder po){
    	List<PoLineItem> poLineItems = po.getPoLineItems();
    	return (List<ProductVariant>) getSession().createQuery( "Select p.sku.productVariant from PoLineItem p where p in (:poLineItems)").setParameterList("poLineItems", poLineItems).list();
    }
    
    public List<ShippingOrder> getCancelledShippingOrderFromSoPo() {
    	Long id = EnumShippingOrderStatus.SO_Cancelled.getId();
    	return (List<ShippingOrder>) getSession().createQuery("from ShippingOrder so where so.shippingOrderStatus.id = :statusId and so.purchaseOrders.size>0").setLong("statusId", id).list();
    }
}
