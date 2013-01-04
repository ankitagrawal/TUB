package com.hk.admin.impl.service.rtv;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.domain.inventory.rtv.RtvNoteStatus;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.pact.dao.BaseDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

  public Page searchRtvNote(Long rtvNoteId, ExtraInventory extraInventory, RtvNoteStatus rtvNoteStatus, int pageNo, int perPage){
    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RtvNote.class);
      if(rtvNoteId!=null){
        detachedCriteria.add(Restrictions.eq("id",rtvNoteId));
      }
    if(extraInventory!=null){
        detachedCriteria.add(Restrictions.eq("extraInventory",extraInventory));
    }
    if(rtvNoteStatus!=null){
      detachedCriteria.add(Restrictions.eq("rtvNoteStatus",rtvNoteStatus));
    }
    detachedCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
      return getBaseDao().list(detachedCriteria, pageNo, perPage);
  }

  public RtvNote save(RtvNote rtvNote){
    return (RtvNote)getBaseDao().save(rtvNote);
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }
}
