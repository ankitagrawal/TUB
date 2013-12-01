package com.hk.admin.pact.service.rtv;


import com.hk.domain.inventory.rtv.ExtraInventory;
import com.akube.framework.dao.Page;
import com.hk.domain.inventory.rtv.ExtraInventoryStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 19, 2012
 * Time: 4:31:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExtraInventoryService {

  public ExtraInventory getExtraInventoryById(Long extraInventoryId);

  public ExtraInventory getExtraInventoryByPoId(Long purchaseOrderId);

  public ExtraInventory save(ExtraInventory extraInventory);

  public Page searchExtraInventory(Long extraInventoryId, PurchaseOrder purchaseOrder, ExtraInventoryStatus extraInventoryStatus, int pageNo, int perPage);
}
