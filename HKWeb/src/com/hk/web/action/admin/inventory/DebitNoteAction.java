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
import com.hk.admin.manager.DebitNoteManager;
import com.hk.admin.manager.GRNManager;
import com.hk.admin.pact.dao.inventory.DebitNoteDao;
import com.hk.admin.pact.service.rtv.RtvNoteLineItemService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumDebitNoteStatus;
import com.hk.constants.inventory.EnumDebitNoteType;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteLineItem;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
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
    @Autowired
    RtvNoteLineItemService rtvNoteLineItemService;
    @Autowired
    DebitNoteManager debitNoteManager;

    Page                            debitNotePage;
    private List<DebitNote>         debitNoteList      = new ArrayList<DebitNote>();
    private List<DebitNoteLineItem> debitNoteLineItems = new ArrayList<DebitNoteLineItem>();
    private List<RtvNoteLineItem> rtvNoteLineItems = new ArrayList<RtvNoteLineItem>();
    private Supplier                supplier;
    private GoodsReceivedNote       grn;
    private DebitNote               debitNote;
    private DebitNoteDto            debitNoteDto;
    private DebitNoteStatus         debitNoteStatus;

    private String                  tinNumber;
    private String                  supplierName;
    private Warehouse               warehouse;

    private Integer                 defaultPerPage     = 20;
    public PurchaseInvoice purchaseInvoice; 
    public List<RtvNote> rtvList = new ArrayList<RtvNote>();
    public List<ExtraInventoryLineItem> eiLineItem = new ArrayList<ExtraInventoryLineItem>();

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
            debitNoteDto = debitNoteManager.generateDebitNoteDto(debitNote);
        } else {
            debitNote = new DebitNote();
            debitNote.setDebitNoteStatus(EnumDebitNoteStatus.Created.asDebitNoteStatus());
            debitNote.setDebitNoteType(EnumDebitNoteType.PreCheckin.asEnumDebitNoteType());
            debitNote.setSupplier(supplier);
            debitNote.setGoodsReceivedNote(grn);
        }
        return new ForwardResolution("/pages/admin/debitNote.jsp");
    }

    public Resolution debitNoteFromPi(){
    	if (purchaseInvoice != null) {
    		rtvList = purchaseInvoice.getRtvNotes();
    		eiLineItem = purchaseInvoice.getEiLineItems();
    		for(RtvNote rtv:rtvList){
    		List<RtvNoteLineItem> rtvNoteLineItemsList = rtvNoteLineItemService.getRtvNoteLineItemsByRtvNote(rtv);
    		if(rtvNoteLineItemsList!=null && rtvNoteLineItemsList.size()>0){
    			rtvNoteLineItems.addAll(rtvNoteLineItemsList);
    			}
    		}
    		debitNote = new DebitNote();
    		
    		
    		debitNote.setPurchaseInvoice(purchaseInvoice);
    		debitNote = debitNoteManager.createDebitNoteLineItem(debitNote, rtvNoteLineItems, eiLineItem);
    		
    	}
    	//return new ForwardResolution("/pages/admin/debitNote.jsp");
    	return new RedirectResolution(DebitNoteAction.class).addParameter("editDebitNote").addParameter("debitNote", debitNote.getId());
    }
    
    public Resolution editDebitNote(){
    	debitNoteDto = new DebitNoteDto();
    	debitNoteDto = debitNoteManager.generateDebitNoteDto(debitNote);
    	return new ForwardResolution("/pages/admin/debitNote.jsp");
    }
    
    public Resolution save() {

		logger.debug("debitNoteLineItems@Save: " + debitNoteLineItems.size());
		if (debitNote == null || debitNote.getId() == null) {
			debitNote.setCreateDate(new Date());
		}
		if (debitNote.getDebitNoteType() != null) {
			Long id = debitNote.getDebitNoteType().getId();
			EnumDebitNoteType debitNoteType = EnumDebitNoteType.getById(id);
			debitNote.setDebitNoteType(debitNoteType.asEnumDebitNoteType());
		} else {
			debitNote.setDebitNoteType(EnumDebitNoteType.PreCheckin.asEnumDebitNoteType());
		}
		debitNote = (DebitNote) getDebitNoteDao().save(debitNote);
		for (DebitNoteLineItem debitNoteLineItem : debitNoteLineItems) {
			if (debitNoteLineItem.getDebitNote() == null) {
				debitNoteLineItem.setDebitNote(debitNote);
			}
			if (debitNoteLineItem.getQty() != null && debitNoteLineItem.getQty() <= 0) {
				getBaseDao().delete(debitNoteLineItem);
			} else {
				getDebitNoteDao().save(debitNoteLineItem);
			}
		}
		addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		return new RedirectResolution(DebitNoteAction.class);
    }
    
    public Resolution delete(){
    	getBaseDao().delete(debitNote);
    	addRedirectAlertMessage(new SimpleMessage("Debit Note Deleted."));
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

	public PurchaseInvoice getPurchaseInvoice() {
		return purchaseInvoice;
	}

	public void setPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

	public List<RtvNote> getRtvList() {
		return rtvList;
	}

	public void setRtvList(List<RtvNote> rtvList) {
		this.rtvList = rtvList;
	}

	public List<ExtraInventoryLineItem> getEiLineItem() {
		return eiLineItem;
	}

	public void setEiLineItem(List<ExtraInventoryLineItem> eiLineItem) {
		this.eiLineItem = eiLineItem;
	}
}