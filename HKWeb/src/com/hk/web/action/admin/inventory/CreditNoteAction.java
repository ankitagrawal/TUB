package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.dto.inventory.CreditNoteDto;
import com.hk.admin.manager.GRNManager;
import com.hk.admin.pact.dao.inventory.CreditNoteDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.creditNote.CreditNoteLineItem;
import com.hk.domain.inventory.creditNote.CreditNoteStatus;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class CreditNoteAction extends BasePaginatedAction {

  private static Logger logger = Logger.getLogger(CreditNoteAction.class);

  @Autowired
  private CreditNoteDao creditNoteDao;

  @Autowired
  private BaseDao baseDao;
  @Autowired
  GRNManager grnManager;
  @Autowired
  SkuService skuService;
  @Autowired
  B2bUserDetailsDao b2bUserDetailsDao;

  Page creditNotePage;
  private List<CreditNote> creditNoteList = new ArrayList<CreditNote>();
  private List<CreditNoteLineItem> creditNoteLineItems = new ArrayList<CreditNoteLineItem>();
  private User user;
  private CreditNote creditNote;
  private CreditNoteDto creditNoteDto;
  private CreditNoteStatus creditNoteStatus;

  private String customerLogin;
  private Warehouse warehouse;

  private Integer defaultPerPage = 20;

  @DefaultHandler
  public Resolution pre() {
    creditNotePage = creditNoteDao.searchCreditNote(creditNoteStatus, customerLogin, warehouse, getPageNo(), getPerPage());
    creditNoteList = creditNotePage.getList();
    return new ForwardResolution("/pages/admin/creditNoteList.jsp");
  }

  public Resolution view() {
    if (creditNote != null) {
      logger.debug("creditNote@view: " + creditNote.getId());
      creditNoteDto = grnManager.generateCreditNoteDto(creditNote);
    } else {
      creditNote = new CreditNote();
      creditNote.setUser(user);
      creditNote.setCreateDt(new Date());
    }
    return new ForwardResolution("/pages/admin/creditNote.jsp");
  }

  public Resolution save() {
    User loggedOnUser = getUserService().getLoggedInUser();
    logger.debug("creditNoteLineItems@Save: " + creditNoteLineItems.size());
    if (creditNote == null || creditNote.getId() == null) {
      creditNote.setCreateDt(new Date());
      creditNote.setCreatedBy(loggedOnUser);
    }
    creditNote.setUpdateDt(new Date());
    creditNote = (CreditNote) getCreditNoteDao().save(creditNote);
    for (CreditNoteLineItem creditNoteLineItem : creditNoteLineItems) {
      if (creditNoteLineItem.getCreditNote() == null) {
        creditNoteLineItem.setCreditNote(creditNote);
      }
      if (creditNoteLineItem.getQty() != null && creditNoteLineItem.getQty() <= 0) {
        getBaseDao().delete(creditNoteLineItem);
      } else {
        getCreditNoteDao().save(creditNoteLineItem);
      }
    }
    addRedirectAlertMessage(new SimpleMessage("Changes saved."));
    return new RedirectResolution(CreditNoteAction.class);
  }

  public String getCustomerLogin() {
    return customerLogin;
  }

  public void setCustomerLogin(String customerLogin) {
    this.customerLogin = customerLogin;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<CreditNote> getCreditNoteList() {
    return creditNoteList;
  }

  public void setCreditNoteList(List<CreditNote> creditNoteList) {
    this.creditNoteList = creditNoteList;
  }

  public CreditNoteStatus getCreditNoteStatus() {
    return creditNoteStatus;
  }

  public void setCreditNoteStatus(CreditNoteStatus creditNoteStatus) {
    this.creditNoteStatus = creditNoteStatus;
  }

  public CreditNote getCreditNote() {
    return creditNote;
  }

  public void setCreditNote(CreditNote creditNote) {
    this.creditNote = creditNote;
  }

  public CreditNoteDto getCreditNoteDto() {
    return creditNoteDto;
  }

  public void setCreditNoteDto(CreditNoteDto creditNoteDto) {
    this.creditNoteDto = creditNoteDto;
  }

  public List<CreditNoteLineItem> getCreditNoteLineItems() {
    return creditNoteLineItems;
  }

  public void setCreditNoteLineItems(List<CreditNoteLineItem> creditNoteLineItems) {
    this.creditNoteLineItems = creditNoteLineItems;
  }


  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public int getPageCount() {
    return creditNotePage == null ? 0 : creditNotePage.getTotalPages();
  }

  public int getResultCount() {
    return creditNotePage == null ? 0 : creditNotePage.getTotalResults();
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("tinNumber");
    params.add("customerLogin");
    params.add("creditNoteStatus");
    params.add("warehouse");
    return params;
  }

  public CreditNoteDao getCreditNoteDao() {
    return creditNoteDao;
  }

  public void setCreditNoteDao(CreditNoteDao creditNoteDao) {
    this.creditNoteDao = creditNoteDao;
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

  public SkuService getSkuService() {
    return skuService;
  }

  public void setSkuService(SkuService skuService) {
    this.skuService = skuService;
  }
}