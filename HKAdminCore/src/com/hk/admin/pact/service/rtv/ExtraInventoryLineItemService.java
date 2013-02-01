package com.hk.admin.pact.service.rtv;

import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 19, 2012
 * Time: 4:12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExtraInventoryLineItemService {

 public ExtraInventoryLineItem getExtraInventoryLineItemById(Long extraInventoryLineItemId);

  public List<ExtraInventoryLineItem> getExtraInventoryLineItemsByExtraInventoryId(Long extraInventoryId);

  public ExtraInventoryLineItem save(ExtraInventoryLineItem extraInventoryLineItem);

  public void delete(ExtraInventoryLineItem extraInventoryLineItem);
}
