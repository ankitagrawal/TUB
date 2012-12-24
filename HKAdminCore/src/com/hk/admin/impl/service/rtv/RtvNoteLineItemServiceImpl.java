package com.hk.admin.impl.service.rtv;


import com.hk.admin.pact.service.rtv.RtvNoteLineItemService;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 23, 2012
 * Time: 11:13:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RtvNoteLineItemServiceImpl implements RtvNoteLineItemService {

  @Autowired
  BaseDao baseDao;

  public RtvNoteLineItem getRtvNoteLineItemById(Long rtvNoteLineItemId){
    return (RtvNoteLineItem) getBaseDao().findUniqueByNamedQueryAndNamedParam("getRtvNoteLineItemById", new String[]{"rtvNoteLineItemId"}, new Object[]{rtvNoteLineItemId});
  }

    public RtvNoteLineItem getRtvNoteLineItemByExtraInventoryLineItem(Long extraInventoryLineItemId){
      return (RtvNoteLineItem) getBaseDao().findUniqueByNamedQueryAndNamedParam("getRtvNoteLineItemByExtraInventoryLineItem", new String[]{"extraInventoryLineItemId"}, new Object[]{extraInventoryLineItemId});
    }

  public RtvNoteLineItem save(RtvNoteLineItem rtvNoteLineItem){
      return (RtvNoteLineItem)getBaseDao().save(rtvNoteLineItem);
  }

  @SuppressWarnings("unchecked")
  public List<RtvNoteLineItem> getRtvNoteLineItemsByRtvNote(RtvNote rtvNote){
    return (List<RtvNoteLineItem>)  getBaseDao().findByNamedQueryAndNamedParam("getRtvNoteLineItemsByRtvNote", new String[]{"rtvNote"}, new Object[]{rtvNote});
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }
}
