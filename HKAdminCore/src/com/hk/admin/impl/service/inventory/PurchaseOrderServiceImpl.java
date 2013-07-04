package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.service.inventory.PoLineItemService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.domain.order.ShippingOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	@Autowired
	PoLineItemService poLineItemService;

	public void updatePOFillRate(PurchaseOrder purchaseOrder) {
		long totalAskedQty = 0;
		long totalReceivedQty = 0;
		double fillRate = 0.0;

		for(GoodsReceivedNote grn : purchaseOrder.getGoodsReceivedNotes()){
			for(GrnLineItem grnLineItem:grn.getGrnLineItems()){
			getPoLineItemService().updatePoLineItemFillRate(grn, grnLineItem, grnLineItem.getCheckedInQty());
			}
		}
		
		for(PoLineItem poLineItem : purchaseOrder.getPoLineItems()) {
			totalAskedQty += poLineItem.getQty();
			if(poLineItem.getReceivedQty() != null) {
				totalReceivedQty += poLineItem.getReceivedQty();
			}
		}

		if(totalAskedQty > 0) {
			fillRate = (totalReceivedQty * 100.0)/ totalAskedQty;
		}
		purchaseOrder.setFillRate(fillRate);
		getPurchaseOrderDao().saveOrUpdate(purchaseOrder);

	}

    public PurchaseOrder save(PurchaseOrder purchaseOrder){
      return (PurchaseOrder)getPurchaseOrderDao().save(purchaseOrder);
    }

  public PurchaseOrder getPurchaseOrderById(Long purchaseOrderId){
     return (PurchaseOrder)getPurchaseOrderDao().findUniqueByNamedQueryAndNamedParam("getPurchaseOrderById", new String[]{"purchaseOrderId"} , new Object[]{purchaseOrderId});
  }
  
   public PurchaseOrder getPurchaseOrderByExtraInventory(ExtraInventory extraInventory){
    return (PurchaseOrder) getPurchaseOrderDao().findUniqueByNamedQueryAndNamedParam("getPurchaseOrderByExtraInventory", new String[]{"extraInventory"}, new Object[]{extraInventory});
   }

  @SuppressWarnings("unchecked")
  public List<PurchaseOrder> getAllPurchaseOrderByExtraInventory(){
    return (List<PurchaseOrder>) getPurchaseOrderDao().findByNamedQuery("getAllPurchaseOrderByExtraInventory");
  }
  
  public List<ProductVariant> getAllProductVariantFromPO(PurchaseOrder po){
	  return (List<ProductVariant>) getPurchaseOrderDao().getAllProductVariantFromPO(po);
  }
  
  public void deleteSoForPo(PurchaseOrder po, ShippingOrder so){
	  getPurchaseOrderDao().deleteSoForPo(po,so);
  }

	public PurchaseOrderDao getPurchaseOrderDao() {
		return purchaseOrderDao;
	}

	public void setPurchaseOrderDao(PurchaseOrderDao purchaseOrderDao) {
		this.purchaseOrderDao = purchaseOrderDao;
	}

	public PoLineItemService getPoLineItemService() {
		return poLineItemService;
	}

	public void setPoLineItemService(PoLineItemService poLineItemService) {
		this.poLineItemService = poLineItemService;
	}
	
}
