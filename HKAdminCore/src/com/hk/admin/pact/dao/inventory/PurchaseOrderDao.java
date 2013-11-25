package com.hk.admin.pact.dao.inventory;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface PurchaseOrderDao extends BaseDao {

    public List<PurchaseOrder> listPurchaseOrdersExcludingStatus(List<Long> purchaseOrderStatusList);

    public List<PurchaseOrder> listPurchaseOrdersWithProductVariant(ProductVariant productVariant);

    public List<PurchaseOrder> searchPO(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber, String tinNumber,
                                        String supplierName, Warehouse warehouse, Boolean extraInventoryCreated, Long brightSoId);

    public Page searchPO(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber,
                         String tinNumber, String supplierName, Warehouse warehouse, Boolean extraInventoryCreated, Long brightSoId, int pageNo, int perPage);

    public List<PurchaseOrder> listPurchaseOrdersIncludingStatus(List<Long> purchaseOrderStatusList);

    public List<PurchaseOrder> listPurchaseOrdersExcludingStatusInTimeFrame(List<Long> purchaseOrderStatusList, Date startDate, Date endDate);

    public boolean isPiCreated(PurchaseOrder purchaseOrder);
    
    public List<ProductVariant> getAllProductVariantFromPO(PurchaseOrder po);
    
    public List<ShippingOrder> getCancelledShippingOrderFromSoPo();
}
