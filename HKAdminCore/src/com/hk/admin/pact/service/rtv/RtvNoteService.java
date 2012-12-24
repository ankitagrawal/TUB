package com.hk.admin.pact.service.rtv;

import com.hk.domain.inventory.rtv.RtvNote;
/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 23, 2012
 * Time: 11:11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RtvNoteService {

  public RtvNote getRtvNoteById(Long rtvNoteId);

  public RtvNote getRtvNoteByExtraInventory(Long extraInventoryId);

  public RtvNote save(RtvNote rtvNote);
}
