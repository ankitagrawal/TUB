package com.hk.web.action.admin.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.dto.inventory.DebitNoteDto;
import com.hk.admin.manager.GRNManager;
import com.hk.admin.pact.dao.inventory.DebitNoteDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteLineItem;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class DebitNoteAction extends BasePaginatedAction {

    private static Logger           logger             = Logger.getLogger(DebitNoteAction.class);

    @Autowired
    private DebitNoteDao            debitNoteDao;

    @Autowired
    private BaseDao                 baseDao;
    @Autowired
    GRNManager                      grnManager;
    @Autowired
    SkuService                      skuService;

    Page                            debitNotePage;
    private List<DebitNote>         debitNoteList      = new ArrayList<DebitNote>();
    private List<DebitNoteLineItem> debitNoteLineItems = new ArrayList<DebitNoteLineItem>();
    private Supplier                supplier;
    private GoodsReceivedNote       grn;
    private DebitNote               debitNote;
    private DebitNoteDto            debitNoteDto;
    private DebitNoteStatus         debitNoteStatus;

    private String                  tinNumber;
    private String                  supplierName;
    private Warehouse               warehouse;

    private Integer                 defaultPerPage     = 20;

    @DefaultHandler
    public Resolution pre() {
        debitNotePage = debitNoteDao.searchDebitNote(grn, debitNoteStatus, tinNumber, supplierName, warehouse, getPageNo(), getPerPage());
        debitNoteList = debitNotePage.getList();
        return new ForwardResolution("/pages/admin/debitNoteList.jsp");
    }

    public Resolution print() {
        if (debitNote != null) {
            debitNoteDto = grnManager.generateDebitNoteDto(debitNote);
            return new ForwardResolution("/pages/admin/debitNotePrintView.jsp");
        } else
            return new ForwardResolution("/pages/admin/debitNoteList.jsp");
    }

    public Resolution view() {
        if (debitNote != null) {
            logger.debug("debitNote@view: " + debitNote.getId());
            debitNoteDto = grnManager.generateDebitNoteDto(debitNote);
        } else {
            debitNote = new DebitNote();
            debitNote.setSupplier(supplier);
            debitNote.setGoodsReceivedNote(grn);
        }
        return new ForwardResolution("/pages/admin/debitNote.jsp");
    }

    public Resolution save() {

        logger.debug("debitNoteLineItems@Save: " + debitNoteLineItems.size());
        if (debitNote == null || debitNote.getId() == null) {
            debitNote.setCreateDate(new Date());
        }
        debitNote = (DebitNote) getDebitNoteDao().save(debitNote);
        for (DebitNoteLineItem debitNoteLineItem : debitNoteLineItems) {
             if(debitNoteLineItem.getDebitNote() == null){
                 debitNoteLineItem.setDebitNote(debitNote);
             }
            if (debitNoteLineItem.getQty() != null && debitNoteLineItem.getQty()<= 0 ) {
                getBaseDao().delete(debitNoteLineItem);
            } else {
                getDebitNoteDao().save(debitNoteLineItem);
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        return new RedirectResolution(DebitNoteAction.class);
    }

    public List<DebitNote> getDebitNoteList() {
        return debitNoteList;
    }

    public void setDebitNoteList(List<DebitNote> debitNoteList) {
        this.debitNoteList = debitNoteList;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public GoodsReceivedNote getGrn() {
        return grn;
    }

    public void setGrn(GoodsReceivedNote grn) {
        this.grn = grn;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public DebitNoteStatus getDebitNoteStatus() {
        return debitNoteStatus;
    }

    public void setDebitNoteStatus(DebitNoteStatus debitNoteStatus) {
        this.debitNoteStatus = debitNoteStatus;
    }

    public DebitNote getDebitNote() {
        return debitNote;
    }

    public void setDebitNote(DebitNote debitNote) {
        this.debitNote = debitNote;
    }

    public DebitNoteDto getDebitNoteDto() {
        return debitNoteDto;
    }

    public void setDebitNoteDto(DebitNoteDto debitNoteDto) {
        this.debitNoteDto = debitNoteDto;
    }

    public List<DebitNoteLineItem> getDebitNoteLineItems() {
        return debitNoteLineItems;
    }

    public void setDebitNoteLineItems(List<DebitNoteLineItem> debitNoteLineItems) {
        this.debitNoteLineItems = debitNoteLineItems;
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
        return debitNotePage == null ? 0 : debitNotePage.getTotalPages();
    }

    public int getResultCount() {
        return debitNotePage == null ? 0 : debitNotePage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("grn");
        params.add("tinNumber");
        params.add("supplierName");
        params.add("debitNoteStatus");
        params.add("warehouse");
        return params;
    }

    public DebitNoteDao getDebitNoteDao() {
        return debitNoteDao;
    }

    public void setDebitNoteDao(DebitNoteDao debitNoteDao) {
        this.debitNoteDao = debitNoteDao;
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