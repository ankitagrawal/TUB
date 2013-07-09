package com.hk.web.action.admin.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.admin.pact.service.accounting.DebitNoteService;
import com.hk.admin.pact.service.courier.CourierPickupService;

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
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.manager.GRNManager;
import com.hk.admin.pact.dao.inventory.DebitNoteDao;
import com.hk.admin.pact.service.rtv.RtvNoteLineItemService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.courier.EnumPickupStatus;
import com.hk.constants.inventory.EnumDebitNoteStatus;
import com.hk.constants.inventory.EnumDebitNoteType;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteLineItem;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.WarehouseService;
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
    DebitNoteService             debitNoteService;
    @Autowired
	AdminEmailManager adminEmailManager;
    @Autowired
	CourierPickupService courierPickupService;
    @Autowired
    WarehouseService warehouseService;

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
    public List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();
    private ReconciliationVoucher reconciliationVoucher;
    private String destinationAddress;
    private CourierPickupDetail courierPickupDetail;
	private Long pickupStatusId;
	private boolean returnByHand;
	private boolean printAsRtv;
	

    @DefaultHandler
    public Resolution pre() {
        debitNotePage = debitNoteDao.searchDebitNote(grn, debitNoteStatus, tinNumber, supplierName, warehouse, getPageNo(), getPerPage());
        debitNoteList = debitNotePage.getList();
        return new ForwardResolution("/pages/admin/debitNoteList.jsp");
    }

    public Resolution print() {
        if (debitNote != null) {
            debitNoteDto = debitNoteService.generateDebitNoteDto(debitNote);
            return new ForwardResolution("/pages/admin/debitNotePrintView.jsp");
        } else
            return new ForwardResolution("/pages/admin/debitNoteList.jsp");
    }
    
    public Resolution printAsRtv() {
        if (debitNote != null) {
        	printAsRtv = true;
            debitNoteDto = debitNoteService.generateDebitNoteDto(debitNote);
            return new ForwardResolution("/pages/admin/debitNotePrintView.jsp");
        } else
            return new ForwardResolution("/pages/admin/debitNoteList.jsp");
    } 

    public Resolution view() {
        if (debitNote != null) {
            logger.debug("debitNote@view: " + debitNote.getId());
            debitNoteDto = debitNoteService.generateDebitNoteDto(debitNote);
        } else {
            debitNote = new DebitNote();
            debitNote.setDebitNoteStatus(EnumDebitNoteStatus.Created.asDebitNoteStatus());
            debitNote.setDebitNoteType(EnumDebitNoteType.PostCheckin.asEnumDebitNoteType());
            debitNote.setSupplier(supplier);
            debitNote.setGoodsReceivedNote(grn);
        }
        return new ForwardResolution("/pages/admin/debitNote.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.FINANCE_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
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
		debitNote = debitNoteService.createDebitNoteLineItem(debitNote, rtvNoteLineItems, eiLineItem);
    		
    	}
    	//return new ForwardResolution("/pages/admin/debitNote.jsp");
    	return new RedirectResolution(DebitNoteAction.class).addParameter("editDebitNote").addParameter("debitNote", debitNote.getId());
    }
    
    
    @Secure(hasAnyPermissions = { PermissionConstants.FINANCE_MANAGEMENT, PermissionConstants.RECON_VOUCHER_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
    public Resolution debitNoteFromRV(){
    	User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
    	if(reconciliationVoucher!=null){
    		rvLineItems = reconciliationVoucher.getRvLineItems();
    		debitNote = new DebitNote();
        	debitNote.setReconciliationVoucher(reconciliationVoucher);
        	debitNote.setSupplier(reconciliationVoucher.getSupplier());
        	/*Long id = loggedOnUser.getSelectedWarehouse().getId();
        	warehouse = warehouseService.getWarehouseById(id);*/
        	if(warehouse!=null){
        		debitNote.setWarehouse(warehouse);
        	}
    		debitNote = debitNoteService.createDebitNoteLineItemWithRVLineItems(debitNote, rvLineItems);
    	}
		
		return new RedirectResolution(DebitNoteAction.class).addParameter("editDebitNote").addParameter("debitNote", debitNote.getId());
    }
    
    public Resolution editDebitNote(){
    	debitNoteDto = new DebitNoteDto();
    	debitNoteDto = debitNoteService.generateDebitNoteDto(debitNote);
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
			debitNote.setDebitNoteType(EnumDebitNoteType.PostCheckin.asEnumDebitNoteType());
		}
		if(debitNote.getDebitNoteStatus().getId().equals(EnumDebitNoteStatus.CLosed.getId())){
			debitNote.setCloseDate(new Date());
		}
		if (courierPickupDetail != null && pickupStatusId != null && courierPickupDetail.getCourier() != null) {
			if (courierPickupDetail.getPickupDate() == null) {
				courierPickupDetail.setPickupDate(new Date());
			}
			courierPickupDetail.setPickupStatus(EnumPickupStatus.asPickupStatusById(pickupStatusId));
			courierPickupDetail = courierPickupService.save(courierPickupDetail);
			debitNote.setCourierPickupDetail(courierPickupDetail);
		}
		debitNote.setDestinationAddress(destinationAddress);
		debitNote = (DebitNote) debitNoteService.save(debitNote);
		debitNote = (DebitNote) debitNoteService.save(debitNote, debitNoteLineItems);

		addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		return new RedirectResolution(DebitNoteAction.class);
    }
    
    public Resolution delete(){
    	if(debitNote.getDebitNoteStatus().getId()>=EnumDebitNoteStatus.CLosed.getId()){
    		addRedirectAlertMessage(new SimpleMessage("Cannot delete a Debit Note once it is closed"));
    		return new RedirectResolution(DebitNoteAction.class);
    	}
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

	public ReconciliationVoucher getReconciliationVoucher() {
		return reconciliationVoucher;
	}

	public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
		this.reconciliationVoucher = reconciliationVoucher;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public CourierPickupDetail getCourierPickupDetail() {
		return courierPickupDetail;
	}

	public void setCourierPickupDetail(CourierPickupDetail courierPickupDetail) {
		this.courierPickupDetail = courierPickupDetail;
	}

	public Long getPickupStatusId() {
		return pickupStatusId;
	}

	public void setPickupStatusId(Long pickupStatusId) {
		this.pickupStatusId = pickupStatusId;
	}

	public boolean isReturnByHand() {
		return returnByHand;
	}

	public void setReturnByHand(boolean returnByHand) {
		this.returnByHand = returnByHand;
	}
	
	public boolean getReturnByHand(){
		return returnByHand;
	}

	public boolean isPrintAsRtv() {
		return printAsRtv;
	}

	public void setPrintAsRtv(boolean printAsRtv) {
		this.printAsRtv = printAsRtv;
	}
	
	public boolean getPrintAsRtv() {
		return printAsRtv;
	}
	
}