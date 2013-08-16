package com.hk.admin.pact.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.domain.inventory.creditNote.CreditNoteStatus;
import com.hk.domain.inventory.crossDomain.InventoryBarcodeMap;
import com.hk.domain.inventory.crossDomain.InventoryBarcodeMapItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface InventoryBarcodeMapDao extends BaseDao {

  public Page searchInventoryBarcodeMap(String status, String login, int pageNo, int perPage);

  public InventoryBarcodeMap getInventoryBarcodeMap(PurchaseOrder purchaseOrder);

  public InventoryBarcodeMapItem getInventoryBarcodeMapItem(String barcode, PurchaseOrder purchaseOrder);

  public void deleteRecords(InventoryBarcodeMap inventoryBarcodeMap);

}