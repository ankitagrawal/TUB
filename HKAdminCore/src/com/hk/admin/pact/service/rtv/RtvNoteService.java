package com.hk.admin.pact.service.rtv;

import com.akube.framework.dao.Page;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.RtvNoteStatus;
import com.hk.domain.inventory.rtv.ExtraInventory;

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

  public Page searchRtvNote(Long rtvNoteId, ExtraInventory extraInventory, RtvNoteStatus rtvNoteStatus, int pageNo, int perPage);

  public RtvNote save(RtvNote rtvNote);
}
