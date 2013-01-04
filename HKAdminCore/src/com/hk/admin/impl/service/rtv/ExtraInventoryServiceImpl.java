package com.hk.admin.impl.service.rtv;

import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 19, 2012
 * Time: 4:30:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ExtraInventoryServiceImpl implements ExtraInventoryService{

  @Autowired
  BaseDao baseDao;

  public ExtraInventory getExtraInventoryById(Long extraInventoryId){
    return (ExtraInventory) getBaseDao().findUniqueByNamedQueryAndNamedParam("getExtraInventoryById", new String[]{"extraInventoryId"}, new Object[]{extraInventoryId});
  }

  public ExtraInventory getExtraInventoryByPoId(Long purchaseOrderId){
    return (ExtraInventory)getBaseDao().findUniqueByNamedQueryAndNamedParam("getExtraInventoryByPoId", new String[]{"purchaseOrderId"}, new Object[]{purchaseOrderId});
  }

  public ExtraInventory save(ExtraInventory extraInventory){
   return (ExtraInventory) getBaseDao().save(extraInventory);
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }
}
