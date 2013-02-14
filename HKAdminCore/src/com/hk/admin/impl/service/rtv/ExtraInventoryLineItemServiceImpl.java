package com.hk.admin.impl.service.rtv;

import com.hk.admin.pact.service.rtv.ExtraInventoryLineItemService;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 19, 2012
 * Time: 4:14:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ExtraInventoryLineItemServiceImpl implements ExtraInventoryLineItemService {

  @Autowired
  BaseDao baseDao;

  public ExtraInventoryLineItem getExtraInventoryLineItemById(Long extraInventoryLineItemId) {
    return (ExtraInventoryLineItem) getBaseDao().findUniqueByNamedQueryAndNamedParam("getExtraInventoryLineItemById", new String[]{"extraInventoryLineItemId"}, new Object[]{extraInventoryLineItemId});
  }

  @SuppressWarnings("unchecked")
  public List<ExtraInventoryLineItem> getExtraInventoryLineItemsByExtraInventoryId(Long extraInventoryId) {
    return (List<ExtraInventoryLineItem>) getBaseDao().findByNamedQueryAndNamedParam("getExtraInventoryLineItemsByExtraInventoryId", new String[]{"extraInventoryId"}, new Object[]{extraInventoryId});
  }

  public ExtraInventoryLineItem save(ExtraInventoryLineItem extraInventoryLineItem) {
    return (ExtraInventoryLineItem) getBaseDao().save(extraInventoryLineItem);
  }

  public void delete(ExtraInventoryLineItem extraInventoryLineItem) {
    getBaseDao().delete(extraInventoryLineItem);
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }
}
