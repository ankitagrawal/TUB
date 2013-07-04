package com.hk.admin.pact.service.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.domain.order.ShippingOrder;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PurchaseOrderService {

	public void updatePOFillRate(PurchaseOrder purchaseOrder);

  public PurchaseOrder getPurchaseOrderById(Long purchaseOrderId);

  public PurchaseOrder save(PurchaseOrder purchaseOrder);

  public PurchaseOrder getPurchaseOrderByExtraInventory(ExtraInventory extraInventory);

  public List<PurchaseOrder> getAllPurchaseOrderByExtraInventory();
  
  public List<ProductVariant> getAllProductVariantFromPO(PurchaseOrder po);
  
  public void deleteSoForPo(PurchaseOrder po, ShippingOrder so);

}
