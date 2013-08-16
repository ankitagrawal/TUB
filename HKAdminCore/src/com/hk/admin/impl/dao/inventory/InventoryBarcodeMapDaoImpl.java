package com.hk.admin.impl.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.domain.user.User;
import com.hk.domain.inventory.crossDomain.InventoryBarcodeMap;
import com.hk.domain.inventory.crossDomain.InventoryBarcodeMapItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.inventory.InventoryBarcodeMapDao;
import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 22 May, 2013
 * Time: 4:56:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class InventoryBarcodeMapDaoImpl extends BaseDaoImpl implements InventoryBarcodeMapDao {
  public Page searchInventoryBarcodeMap(String status, String login, int pageNo, int perPage) {
    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(InventoryBarcodeMap.class);
    if (StringUtils.isNotBlank(login)) {
      DetachedCriteria userCriteria = detachedCriteria.createCriteria("user");
      userCriteria.add(Restrictions.like("login", login));
    }

    if (status != null) {
      detachedCriteria.add(Restrictions.eq("status", status));
    }
    detachedCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
    return list(detachedCriteria, pageNo, perPage);

  }

  public InventoryBarcodeMap getInventoryBarcodeMap(PurchaseOrder purchaseOrder) {
    String query = "from InventoryBarcodeMap ibm where ibm.purchaseOrder = :purchaseOrder";
    return (InventoryBarcodeMap) findUniqueByNamedParams(query, new String[]{"purchaseOrder"}, new Object[]{purchaseOrder});
  }

  public InventoryBarcodeMapItem getInventoryBarcodeMapItem(String barcode, PurchaseOrder purchaseOrder) {
    String query = "select ibmi from InventoryBarcodeMapItem ibmi, InventoryBarcodeMap ibm " +
        "where ibmi.inventoryBarcodeMap=ibm and ibm.purchaseOrder=:purchaseOrder and ibmi.skuItemBarcode = :barcode";
    List<InventoryBarcodeMapItem> barcodeMapItemList = (List<InventoryBarcodeMapItem>)
        findByNamedParams(query, new String[]{"purchaseOrder", "barcode"}, new Object[]{purchaseOrder, barcode});
    if (barcodeMapItemList == null || barcodeMapItemList.isEmpty()) {
      query = "select ibmi from InventoryBarcodeMapItem ibmi, InventoryBarcodeMap ibm " +
          "where ibmi.inventoryBarcodeMap=ibm and ibm.purchaseOrder=:purchaseOrder and ibmi.skuGroupBarcode = :barcode";
      barcodeMapItemList = (List<InventoryBarcodeMapItem>)
          findByNamedParams(query, new String[]{"purchaseOrder", "barcode"}, new Object[]{purchaseOrder, barcode});
    }
    return barcodeMapItemList != null && !barcodeMapItemList.isEmpty() ? barcodeMapItemList.get(0) : null;
  }

  public void deleteRecords(InventoryBarcodeMap inventoryBarcodeMap) {
    super.delete(inventoryBarcodeMap);
  }
}
