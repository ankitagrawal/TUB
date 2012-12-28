package com.hk.web.action.admin.rtv;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.admin.pact.service.rtv.RtvNoteLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.rtv.EnumRtvNoteStatus;
import com.hk.domain.inventory.rtv.RtvNoteStatus;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 27, 2012
 * Time: 9:17:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)

@Component
public class RTVAction extends BasePaginatedAction{

  @Autowired
  private RtvNoteService rtvNoteService;
  @Autowired
  private ExtraInventoryService extraInventoryService;
  @Autowired
  private RtvNoteLineItemService rtvNoteLineItemService;

  private List<RtvNoteLineItem> rtvNoteLineItems = new ArrayList<RtvNoteLineItem>();
  private Integer defaultPerPage = 20;
  Page rtvNotePage;
  private List<RtvNote> rtvNotes = new ArrayList<RtvNote>();
  private Long rtvNoteId;
  private RtvNote rtvNote;
  private ExtraInventory extraInventory;
  private Long extraInventoryId;
  private EnumRtvNoteStatus enumRtvNoteStatus;

  @DefaultHandler
  public Resolution pre(){
      RtvNoteStatus rtvNoteStatus = null;
      extraInventory =  getExtraInventoryService().getExtraInventoryById(extraInventoryId);
      if(enumRtvNoteStatus!=null){
      rtvNoteStatus = enumRtvNoteStatus.asRtvNoteStatus();
      }
      rtvNotePage = getRtvNoteService().searchRtvNote(rtvNoteId,extraInventory,rtvNoteStatus, getPageNo(), getPerPage());
      rtvNotes = rtvNotePage.getList();
      return new ForwardResolution("/pages/admin/rtvList.jsp");
  }

  public int getPageCount() {
    return rtvNotePage == null ? 0 : rtvNotePage.getTotalPages();
  }

  public int getResultCount() {
    return rtvNotePage == null ? 0 : rtvNotePage.getTotalResults();
  }

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("rtvNoteId");
    params.add("extraInventoryId");
    params.add("enumRtvNoteStatus");
    return params;
  }

  public Long getExtraInventoryId() {
    return extraInventoryId;
  }

  public void setExtraInventoryId(Long extraInventoryId) {
    this.extraInventoryId = extraInventoryId;
  }

  public Long getRtvNoteId() {
    return rtvNoteId;
  }

  public void setRtvNoteId(Long rtvNoteId) {
    this.rtvNoteId = rtvNoteId;
  }

  public List<RtvNote> getRtvNotes() {
    return rtvNotes;
  }

  public void setRtvNotes(List<RtvNote> rtvNotes) {
    this.rtvNotes = rtvNotes;
  }


  public EnumRtvNoteStatus getEnumRtvNoteStatus() {
    return enumRtvNoteStatus;
  }

  public void setEnumRtvNoteStatus(EnumRtvNoteStatus enumRtvNoteStatus) {
    this.enumRtvNoteStatus = enumRtvNoteStatus;
  }

  public RtvNoteService getRtvNoteService() {
    return rtvNoteService;
  }

  public ExtraInventoryService getExtraInventoryService() {
    return extraInventoryService;
  }

  public ExtraInventory getExtraInventory() {
    return extraInventory;
  }

  public void setExtraInventory(ExtraInventory extraInventory) {
    this.extraInventory = extraInventory;
  }

  public RtvNote getRtvNote() {
    return rtvNote;
  }

  public void setRtvNote(RtvNote rtvNote) {
    this.rtvNote = rtvNote;
  }

  public RtvNoteLineItemService getRtvNoteLineItemService() {
    return rtvNoteLineItemService;
  }

  public List<RtvNoteLineItem> getRtvNoteLineItems() {
    return rtvNoteLineItems;
  }

  public void setRtvNoteLineItems(List<RtvNoteLineItem> rtvNoteLineItems) {
    this.rtvNoteLineItems = rtvNoteLineItems;
  }

}
