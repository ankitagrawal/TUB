package com.hk.admin.pact.service.rtv;

import com.hk.domain.inventory.rtv.RtvNoteLineItem;
import com.hk.domain.inventory.rtv.RtvNote;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 23, 2012
 * Time: 11:12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RtvNoteLineItemService {

  public RtvNoteLineItem save(RtvNoteLineItem rtvNoteLineItem);

  public RtvNoteLineItem getRtvNoteLineItemById(Long rtvNoteLineItemId);

  public RtvNoteLineItem getRtvNoteLineItemByExtraInventoryLineItem(Long extraInventoryLineItemId);

  public List<RtvNoteLineItem> getRtvNoteLineItemsByRtvNote(RtvNote rtvNote);
}
