package com.hk.admin.impl.service.rtv;

import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 23, 2012
 * Time: 11:12:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RtvNoteServiceImpl implements RtvNoteService {

  @Autowired
  BaseDao baseDao;

  public RtvNote getRtvNoteById(Long rtvNoteId){
    return (RtvNote) getBaseDao().findUniqueByNamedQueryAndNamedParam("getRtvNoteById", new String[]{"rtvNoteId"}, new Object[]{rtvNoteId});
  }

   public RtvNote getRtvNoteByExtraInventory(Long extraInventoryId){
     return (RtvNote) getBaseDao().findUniqueByNamedQueryAndNamedParam("getRtvNoteByExtraInventory", new String[]{"extraInventoryId"}, new Object[]{extraInventoryId});
   }

  public RtvNote save(RtvNote rtvNote){
    return (RtvNote)getBaseDao().save(rtvNote);
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }
}
