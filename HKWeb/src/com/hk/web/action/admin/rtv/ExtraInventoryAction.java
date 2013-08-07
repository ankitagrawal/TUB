package com.hk.web.action.admin.rtv;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.accounting.PurchaseInvoiceService;
import com.hk.admin.pact.service.courier.CourierPickupService;
import com.hk.admin.pact.service.inventory.PoLineItemService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.rtv.ExtraInventoryLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.constants.courier.EnumPickupStatus;
import com.hk.constants.courier.StateList;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.constants.rtv.EnumExtraInventoryLineItemType;
import com.hk.constants.rtv.EnumExtraInventoryStatus;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.inventory.rtv.ExtraInventoryStatus;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.TaxDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.pact.dao.MasterDataDao;
import com.hk.constants.rtv.EnumRtvNoteStatus;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.accounting.PoLineItem;
import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.admin.util.TaxUtil;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.TaxConstants;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.domain.sku.Sku;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
import com.hk.web.HealthkartResponse;
import com.hk.domain.core.Tax;
import com.hk.dto.TaxComponent;
import com.hk.web.action.admin.inventory.InventoryCheckinAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Shrey Date: Dec 19, 2012 Time: 1:50:37 PM To
 * change this template use File | Settings | File Templates.
 */
@Component
public class ExtraInventoryAction extends BasePaginatedAction {

	@Autowired
	ExtraInventoryService extraInventoryService;
	@Autowired
	ExtraInventoryLineItemService extraInventoryLineItemService;
	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	SkuService skuService;
	@Autowired
	WarehouseService wareHouseService;
	@Autowired
	PurchaseOrderService purchaseOrderService;
	@Autowired
	RtvNoteService rtvNoteService;
	@Autowired
	RtvNoteLineItemService rtvNoteLineItemService;
	@Autowired
	PoLineItemService poLineItemService;
	@Autowired
	MasterDataDao masterDataDao;
	@Autowired
	EmailManager emailManager;
	@Autowired
	CourierPickupService courierPickupService;
	@Autowired
	UserService userService;
	@Autowired
	PurchaseInvoiceService purchaseInvoiceService;
	@Autowired
	TaxDao taxDao;

	private List<ExtraInventoryLineItem> extraInventoryLineItems = new ArrayList<ExtraInventoryLineItem>();
	private List<ExtraInventoryLineItem> extraInventoryLineItemsSelected = new ArrayList<ExtraInventoryLineItem>();
	private List<ExtraInventoryLineItem> extraInventoryShortLineItemsSelected = new ArrayList<ExtraInventoryLineItem>();
	private List<RtvNoteLineItem> rtvNoteLineItems = new ArrayList<RtvNoteLineItem>();
	private List<Tax> taxList = new ArrayList<Tax>();
	private List<ExtraInventory> extraInventories = new ArrayList<ExtraInventory>();
	private Integer defaultPerPage = 20;
	Page purchaseOrderPage;
	private Long purchaseOrderId;
	private ExtraInventory extraInventory;
	private PurchaseOrder purchaseOrder;
	private Long wareHouseId;
	private String comments;
	private String productVariantId;
	private Long extraInventoryId;
	private User user;
	private RtvNote rtvNote;
	private Long rtvNoteId;
	private Long rtvStatusId;
	private Long extraInventoryStatusId;
	private Boolean isDebitToSupplier;
	private Boolean isReconciled;
	private Boolean isPiReconciled;
	private String reconciledStatus;
	private Long newPurchaseOrderId;
	private String destinationAddress;
	private CourierPickupDetail courierPickupDetail;
	private Long pickupStatusId;
	private Boolean sameState;
	private String supplierState;
	private String warehouseState;
	private Double shippingChargeHk;
	private Double shippingChargeVendor;
	private Boolean returnByHand;
	private String rtvReturnAddress;
	private String rtvTrackingNumber;
	private Date rtvReturnDate;

	@DefaultHandler
	public Resolution pre() {
		extraInventory = getExtraInventoryService().getExtraInventoryByPoId(purchaseOrderId);
		if (extraInventory != null) {
			extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
		}
		purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
		if (purchaseOrder != null) {
			newPurchaseOrderId = purchaseOrder.getId();
		}

		if (purchaseOrderService.getPurchaseOrderById(purchaseOrderId) != null) {
			supplierState = purchaseOrderService.getPurchaseOrderById(purchaseOrderId).getSupplier().getState().toString();
			warehouseState = purchaseOrderService.getPurchaseOrderById(purchaseOrderId).getWarehouse().getState().toString();
			if (supplierState != null && warehouseState != null && supplierState.equalsIgnoreCase(warehouseState)) {
				sameState = Boolean.TRUE;
			} else {
				sameState = Boolean.FALSE;
			}
		}

		if (extraInventory != null) {
			rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
			if (rtvNote != null) {
				if (rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId())
						|| rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.SentToSupplier.getId()) || rtvNote.isReconciled()) {
					reconciledStatus = "reconciled";
				}
			}
		}
		isPiReconciled = Boolean.FALSE;
		for (ExtraInventoryLineItem eili : extraInventoryLineItems) {
			if (eili.getPurchaseInvoices() != null && eili.getPurchaseInvoices().size() > 0) {
				for (PurchaseInvoice pi : eili.getPurchaseInvoices()) {
					if (pi.getReconciled() != null && pi.getReconciled()) {
						isPiReconciled = Boolean.TRUE;
					}
				}
			}
		}
		taxList = taxDao.getLocalTaxList();
		return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId",
				wareHouseId);
	}

	public Resolution save() {

		User user = null;
		Warehouse userWarehouse = null;
		if (getPrincipal() != null) {
			user = getUserService().getUserById(getPrincipal().getId());
		}
		if (getUserService().getWarehouseForLoggedInUser() != null) {
			userWarehouse = userService.getWarehouseForLoggedInUser();
		} else {
			addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
			return new RedirectResolution(ExtraInventoryAction.class).addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId", wareHouseId);
		}

		extraInventory = getExtraInventoryService().getExtraInventoryByPoId(purchaseOrderId);
		purchaseOrder = getPurchaseOrderService().getPurchaseOrderById(purchaseOrderId);
		List<Long> skus = new ArrayList<Long>();
		for (ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItems) {
			if (skus.size() == 0 && extraInventoryLineItem.getSku() != null) {
				skus.add(extraInventoryLineItem.getSku().getId());
			} else if (extraInventoryLineItem.getSku() != null && skus.contains(extraInventoryLineItem.getSku().getId())) {
				if (extraInventory != null) {
					extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
					rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
					if (rtvNote != null) {
						if (rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId())
								|| rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.SentToSupplier.getId()) || rtvNote.isReconciled()) {
							reconciledStatus = "reconciled";
						}
					}
					purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
					if (purchaseOrder != null) {
						newPurchaseOrderId = purchaseOrder.getId();
					}
				}
				noCache();
				addRedirectAlertMessage(new SimpleMessage("Same Sku is present more than once !!!! "));
				return new RedirectResolution(ExtraInventoryAction.class).addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId",
						wareHouseId);
			} else if (extraInventoryLineItem.getSku() != null) {
				skus.add(extraInventoryLineItem.getSku().getId());
			}
		}
		// creating Extra Inventory
		if (extraInventory == null) {
			ExtraInventory extraInventory1 = new ExtraInventory();
			if (purchaseOrder != null) {
				extraInventory1.setPurchaseOrder(purchaseOrder);
			}
			extraInventory1.setComments(comments);
			extraInventory1.setCreateDate(new Date());
			extraInventory1.setUpdateDate(new Date());
			if (getPrincipal() != null) {
				user = getUserService().getUserById(getPrincipal().getId());
			}
			extraInventory1.setCreatedBy(user);
			extraInventory1.setExtraInventoryStatus(EnumExtraInventoryStatus.Created.asEnumExtraInventoryStatus());
			extraInventory = getExtraInventoryService().save(extraInventory1);
			purchaseOrder.setExtraInventoryCreated(true);
			purchaseOrder = getPurchaseOrderService().save(purchaseOrder);
		} else {
			extraInventory.setUpdateDate(new Date());
			extraInventory.setComments(comments);
			ExtraInventoryStatus extraInventoryStatus = EnumExtraInventoryStatus.asEnumExtraInventoryStatusByID(extraInventoryStatusId);
			if (!(extraInventory.getExtraInventoryStatus().getName().equals(extraInventoryStatus.getName()))) {
				extraInventory.setExtraInventoryStatus(extraInventoryStatus);
				if (extraInventoryStatus.getName().equals(EnumExtraInventoryStatus.SentToCategory.getName()) && !extraInventory.isEmailSent()) {
					boolean isEmailSent = getEmailManager().sendExtraInventoryMail(extraInventory);
					if (isEmailSent)
						extraInventory.setEmailSent(true);
				}
			}
			extraInventory = getExtraInventoryService().save(extraInventory);
		}
		for (ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItems) {
			if (extraInventoryLineItem.getId() == null && extraInventoryLineItem.getReceivedQty() != 0) {
				extraInventoryLineItem.setExtraInventoryLineItemType(EnumExtraInventoryLineItemType.Normal.asEnumExtraInventoryLineItemType());
				extraInventoryLineItem.setExtraInventory(extraInventory);
				extraInventoryLineItem.setCreateDate(new Date());
				extraInventoryLineItem.setUpdateDate(new Date());
				extraInventoryLineItem.setRtvCreated(false);
				extraInventoryLineItem.setGrnCreated(false);
				getExtraInventoryLineItemService().save(extraInventoryLineItem);
			} else {
				if (extraInventoryLineItem.getExtraInventoryLineItemType() != null) {
					Long id = extraInventoryLineItem.getExtraInventoryLineItemType().getId();
					if (id != null) {
						EnumExtraInventoryLineItemType lineItemType = EnumExtraInventoryLineItemType.getById(id);
						extraInventoryLineItem.setExtraInventoryLineItemType(lineItemType.asEnumExtraInventoryLineItemType());
					}
				}
				if (extraInventoryLineItem.getReceivedQty() != 0) {
					ExtraInventoryLineItem extraInventoryLineItem1 = getExtraInventoryLineItemService().getExtraInventoryLineItemById(
							extraInventoryLineItem.getId());
					if (extraInventoryLineItem1 != null) {
						extraInventoryLineItem.setPurchaseInvoices(extraInventoryLineItem1.getPurchaseInvoices());
					}
					extraInventoryLineItem.setUpdateDate(new Date());
					extraInventoryLineItem.setExtraInventory(extraInventory);
					getExtraInventoryLineItemService().save(extraInventoryLineItem);

					if (extraInventoryLineItem.getPurchaseInvoices() != null && extraInventoryLineItem.getPurchaseInvoices().size() > 0) {
						for (PurchaseInvoice pi : extraInventoryLineItem.getPurchaseInvoices()) {
							List<ExtraInventoryLineItem> eiliList = pi.getEiLineItems();
							Double shortAmount = 0.0;
							if (eiliList != null && eiliList.get(0) != null) {
								for (ExtraInventoryLineItem eiLi : eiliList) {
									if (eiLi.getExtraInventoryLineItemType().getId().equals(EnumExtraInventoryLineItemType.Short.getId())) {
										shortAmount += eiLi.getPayableAmount();
									}
								}
							}
							pi.setShortAmount(shortAmount);
							pi.setPiRtvShortTotal(pi.getFinalPayableAmount() + pi.getRtvAmount() + shortAmount);
							purchaseInvoiceService.save(pi);
						}
					}

				} else {
					extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
					if (extraInventoryLineItem.getPurchaseInvoices() != null && extraInventoryLineItem.getPurchaseInvoices().size() > 0) {
						for (PurchaseInvoice pi : extraInventoryLineItem.getPurchaseInvoices()) {
							pi.setShortAmount(pi.getShortAmount() - extraInventoryLineItem.getPayableAmount());
							pi.setPiRtvShortTotal(pi.getPiRtvShortTotal() - extraInventoryLineItem.getPayableAmount());
							purchaseInvoiceService.save(pi);
						}
					}
					getExtraInventoryLineItemService().delete(extraInventoryLineItem);
				}
			}
		}
		noCache();
		addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully !!!! "));
		return new RedirectResolution(ExtraInventoryAction.class).addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId", wareHouseId);
	}

	@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
	public Resolution createRtv() {
		extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
		List<ExtraInventoryLineItem> extraLineItems = new ArrayList<ExtraInventoryLineItem>();
		Double rtvamount = 0.0;
		for (ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected) {
			if (extraInventoryLineItem != null) {
				extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
				if (extraInventoryLineItem.getExtraInventoryLineItemType() != null) {
					Long id = extraInventoryLineItem.getExtraInventoryLineItemType().getId();
					if (id != null) {
						EnumExtraInventoryLineItemType lineItemType = EnumExtraInventoryLineItemType.getById(id);
						extraInventoryLineItem.setExtraInventoryLineItemType(lineItemType.asEnumExtraInventoryLineItemType());
					}
				}
				extraInventoryLineItem.setRtvCreated(true);
				rtvamount += extraInventoryLineItem.getPayableAmount();
				extraInventoryLineItem.setExtraInventoryLineItemType(EnumExtraInventoryLineItemType.RTV.asEnumExtraInventoryLineItemType());
				extraInventoryLineItem = getExtraInventoryLineItemService().save(extraInventoryLineItem);
				extraLineItems.add(extraInventoryLineItem);
			}
		}

		extraInventoryLineItemsSelected = extraLineItems;
		List<RtvNoteLineItem> rtvNoteLineItems1 = new ArrayList<RtvNoteLineItem>();
		// creating Rtv Note
		rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
		if (rtvNote == null) {
			rtvNote = new RtvNote();
			rtvNote.setExtraInventory(extraInventory);
			rtvNote.setRtvNoteStatus(EnumRtvNoteStatus.Created.asRtvNoteStatus());
			rtvNote.setReconciled(false);
			rtvNote.setDebitToSupplier(false);
			rtvNote.setCreateDate(new Date());
			rtvNote.setUpdateDate(new Date());
			if (getPrincipal() != null) {
				user = getUserService().getUserById(getPrincipal().getId());
			}
			rtvNote.setCreatedBy(user);
			rtvNote = getRtvNoteService().save(rtvNote);
		} else {
			rtvNoteLineItems1 = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
		}
		// creating Rtv Note Line Items
		for (ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected) {
			if (extraInventoryLineItem != null) {
				RtvNoteLineItem rtvNoteLineItem = getRtvNoteLineItemService().getRtvNoteLineItemByExtraInventoryLineItem(extraInventoryLineItem.getId());
				if (rtvNoteLineItem == null) {
					rtvNoteLineItem = new RtvNoteLineItem();
					rtvNoteLineItem.setExtraInventoryLineItem(extraInventoryLineItem);
					rtvNoteLineItem.setRtvNote(rtvNote);
					rtvNoteLineItem = getRtvNoteLineItemService().save(rtvNoteLineItem);
					rtvNoteLineItems.add(rtvNoteLineItem);
				}
			}
		}
		if (rtvNoteLineItems1 != null && rtvNoteLineItems1.size() != 0) {
			rtvNoteLineItems.addAll(rtvNoteLineItems1);
		}
		/*
		 * if (rtvNote != null) { if (rtvNote.getPurchaseInvoices() != null &&
		 * rtvNote.getPurchaseInvoices().size() > 0) { for (PurchaseInvoice pi :
		 * rtvNote.getPurchaseInvoices()) {
		 * pi.setRtvAmount(pi.getRtvAmount()+rtvamount);
		 * pi.setPiRtvShortTotal(pi.getFinalPayableAmount() +
		 * pi.getShortAmount()+pi.getRtvAmount());
		 * purchaseInvoiceService.save(pi); } } }
		 */
		noCache();
		addRedirectAlertMessage(new SimpleMessage("Rtv Created !!!!"));
		return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId", purchaseOrderId)
				.addParameter("wareHouseId", wareHouseId);
	}

	@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
	public Resolution createShort() {
		extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
		for (ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected) {
			if (extraInventoryLineItem != null) {
				extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
				extraInventoryLineItem.setExtraInventoryLineItemType(EnumExtraInventoryLineItemType.Short.asEnumExtraInventoryLineItemType());
				extraInventoryLineItem = getExtraInventoryLineItemService().save(extraInventoryLineItem);
			}
		}

		addRedirectAlertMessage(new SimpleMessage("Short Created !!!!"));
		return new RedirectResolution(ExtraInventoryAction.class).addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId", wareHouseId);
	}

	@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
	public Resolution editRtvNote() {
		rtvNote = getRtvNoteService().getRtvNoteById(rtvNoteId);
		extraInventory = rtvNote.getExtraInventory();
		if (rtvNote != null) {
			rtvNote.setRemarks(comments);
			if (rtvStatusId != null && isReconciled != null) {
				if (EnumRtvNoteStatus.asRtvNoteStatusById(rtvStatusId).getName().equalsIgnoreCase("reconciled") || isReconciled) {
					rtvNote.setReconciled(true);
					rtvNote.setRtvNoteStatus(EnumRtvNoteStatus.Reconciled.asRtvNoteStatus());
				} else {
					rtvNote.setReconciled(isReconciled);
					rtvNote.setRtvNoteStatus(EnumRtvNoteStatus.asRtvNoteStatusById(rtvStatusId));
				}
			}
			rtvNote.setDebitToSupplier(isDebitToSupplier);
			if (courierPickupDetail != null && pickupStatusId != null && courierPickupDetail.getCourier() != null) {
				if (courierPickupDetail.getPickupDate() == null) {
					courierPickupDetail.setPickupDate(new Date());
				}
				courierPickupDetail.setPickupStatus(EnumPickupStatus.asPickupStatusById(pickupStatusId));
				courierPickupDetail = getCourierPickupService().save(courierPickupDetail);
				rtvNote.setCourierPickupDetail(courierPickupDetail);
			}
			rtvNote.setShippingChargeHk(shippingChargeHk);
			rtvNote.setShippingChargeVendor(shippingChargeVendor);
			rtvNote.setDestinationAddress(destinationAddress);
			rtvNote.setReturnByHand(returnByHand);
			rtvNote.setRtvReturnAddress(rtvReturnAddress);
			rtvNote.setRtvReturnDate(rtvReturnDate);
			rtvNote.setRtvTrackingNumber(rtvTrackingNumber);
			rtvNote = getRtvNoteService().save(rtvNote);
		}
		noCache();
		addRedirectAlertMessage(new SimpleMessage("Changes Saved successfully !!!!"));
		return new RedirectResolution(RTVAction.class, "pre");
	}

	@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
	public Resolution editRtv() {
		rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventoryId);
		if (rtvNote == null) {
			addRedirectAlertMessage(new SimpleMessage("No RTV Exist !!!! "));
			return new RedirectResolution(ExtraInventoryAction.class, "pre").addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId",
					wareHouseId);
		}
		rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
		extraInventory = rtvNote.getExtraInventory();
		courierPickupDetail = rtvNote.getCourierPickupDetail();
		return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId", purchaseOrderId)
				.addParameter("wareHouseId", wareHouseId);
	}

	@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
	public Resolution createPO() {
		extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
		extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
		if (extraInventory != null) {
			rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
			if (rtvNote != null) {
				if (rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId())
						|| rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.SentToSupplier.getId()) || rtvNote.isReconciled()) {
					reconciledStatus = "reconciled";
				}
			}
		}
		// checking if one of sku is null
		for (ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected) {
			if (extraInventoryLineItem != null) {
				extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
				if (extraInventoryLineItem.getSku() == null) {
					noCache();
					addRedirectAlertMessage(new SimpleMessage("One of the selected Line Item sku is null, please Enter Sku and then press create PO !!!"));
					return new RedirectResolution(ExtraInventoryAction.class).addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId",
							wareHouseId);
				}
			}
		}
		generatePO();
		return new RedirectResolution(ExtraInventoryAction.class).addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId", wareHouseId);
	}

	@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
	public Resolution generatePO() {

		purchaseOrder = getPurchaseOrderService().getPurchaseOrderById(purchaseOrderId);
		extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
		extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
		if (extraInventory != null) {
			rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
			if (rtvNote != null) {
				if (rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId())
						|| rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.SentToSupplier.getId()) || rtvNote.isReconciled()) {
					reconciledStatus = "reconciled";
				}
			}
		}
		PurchaseOrder purchaseOrder1 = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
		PurchaseOrder newPurchaseOrder = new PurchaseOrder();

		if (purchaseOrder1 == null) {
			// Creating New PurchaseOrder and set Extra Inventory in it

			newPurchaseOrder.setExtraInventory(extraInventory);
			newPurchaseOrder.setSupplier(purchaseOrder.getSupplier());
			newPurchaseOrder.setCreateDate(new Date());
			newPurchaseOrder.setUpdateDate(new Date());
			newPurchaseOrder.setAdvPayment(0.0D);
			newPurchaseOrder.setApprovalDate(new Date());
			newPurchaseOrder.setDiscount(0.0D);
			newPurchaseOrder.setPoPlaceDate(new Date());
			newPurchaseOrder.setWarehouse(purchaseOrder.getWarehouse());
			newPurchaseOrder.setSurchargeAmount(purchaseOrder.getSurchargeAmount());
			newPurchaseOrder.setTaxAmount(purchaseOrder.getTaxAmount());
			newPurchaseOrder.setTaxableAmount(purchaseOrder.getTaxableAmount());
			if (getPrincipal() != null) {
				user = getUserService().getUserById(getPrincipal().getId());
			}
			newPurchaseOrder.setCreatedBy(user);
			newPurchaseOrder.setPurchaseOrderStatus(getBaseDao().get(PurchaseOrderStatus.class, EnumPurchaseOrderStatus.Generated.getId()));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getLeadTime());
			newPurchaseOrder.setEstDelDate(calendar.getTime());
			if (purchaseOrder.getSupplier().getCreditDays() != null && purchaseOrder.getSupplier().getCreditDays() >= 0) {
				calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getCreditDays());
				newPurchaseOrder.setEstPaymentDate(calendar.getTime());
			} else {
				newPurchaseOrder.setEstPaymentDate(new Date());
			}
			newPurchaseOrder = getPurchaseOrderService().save(newPurchaseOrder);
		} else {
			newPurchaseOrder = purchaseOrder1;
			if (!newPurchaseOrder.getPurchaseOrderStatus().getId().equals(EnumPurchaseOrderStatus.Generated.getId())) {
				purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
				if (purchaseOrder != null) {
					newPurchaseOrderId = purchaseOrder.getId();
				}
				noCache();
				addRedirectAlertMessage(new SimpleMessage("PO is not in Generated State"));
				return new RedirectResolution(ExtraInventoryAction.class).addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId",
						wareHouseId);
			}
		}

		// Creating new POLine Items and set Extra inventory Line Items Id in it

		for (ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected) {

			if (extraInventoryLineItem != null) {
				extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
				extraInventoryLineItem.setGrnCreated(true);
				if (extraInventoryLineItem.getExtraInventoryLineItemType() != null) {
					Long id = extraInventoryLineItem.getExtraInventoryLineItemType().getId();
					if (id != null) {
						EnumExtraInventoryLineItemType lineItemType = EnumExtraInventoryLineItemType.getById(id);
						extraInventoryLineItem.setExtraInventoryLineItemType(lineItemType.asEnumExtraInventoryLineItemType());
					}
				}
				extraInventoryLineItem = getExtraInventoryLineItemService().save(extraInventoryLineItem);
				PoLineItem poLineItem = new PoLineItem();
				poLineItem.setExtraInventoryLineItem(extraInventoryLineItem);
				poLineItem.setCostPrice(extraInventoryLineItem.getCostPrice());
				poLineItem.setMrp(extraInventoryLineItem.getMrp());
				poLineItem.setQty(extraInventoryLineItem.getReceivedQty());
				poLineItem.setReceivedQty(extraInventoryLineItem.getReceivedQty());
				poLineItem.setSku(extraInventoryLineItem.getSku());
				poLineItem.setPurchaseOrder(newPurchaseOrder);
				poLineItem.setDiscountPercent(0.0D);
				poLineItem.setTaxAmount(extraInventoryLineItem.getSku().getTax().getValue());
				poLineItem.setTaxableAmount(0.0D);
				poLineItem.setSurchargeAmount(0.0D);
				poLineItem = getPoLineItemService().save(poLineItem);
			}
		}
		purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
		if (purchaseOrder != null) {
			newPurchaseOrderId = purchaseOrder.getId();
		}
		taxList = taxDao.getLocalTaxList();
		noCache();
		addRedirectAlertMessage(new SimpleMessage("PO and PoLine Item has been created !!! with New PO ID - " + newPurchaseOrder.getId()));
		return new RedirectResolution(ExtraInventoryAction.class).addParameter("purchaseOrderId", purchaseOrderId).addParameter("wareHouseId", wareHouseId);
	}

	@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
	public Resolution editRtvNoteLineItems() {
		rtvNote = getRtvNoteService().getRtvNoteById(rtvNoteId);
		extraInventory = rtvNote.getExtraInventory();
		rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
		courierPickupDetail = rtvNote.getCourierPickupDetail();
		return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId", purchaseOrderId);
	}

	@SuppressWarnings("unchecked")
	public Resolution searchExtraInventory() {
		if (purchaseOrderId != null) {
			purchaseOrder = getPurchaseOrderService().getPurchaseOrderById(purchaseOrderId);
		}
		ExtraInventoryStatus extraInventoryStatus = EnumExtraInventoryStatus.asEnumExtraInventoryStatusByID(extraInventoryStatusId);
		purchaseOrderPage = getExtraInventoryService().searchExtraInventory(extraInventoryId, purchaseOrder, extraInventoryStatus, getPageNo(), getPerPage());
		extraInventories = purchaseOrderPage.getList();
		return new ForwardResolution("/pages/admin/extraInventoryList.jsp");
	}

	public Resolution rtvNotePrintPreview() {
		rtvNote = getRtvNoteService().getRtvNoteById(rtvNoteId);
		purchaseOrder = rtvNote.getExtraInventory().getPurchaseOrder();
		rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
		return new ForwardResolution("/pages/admin/rtvNotePrintPreview.jsp");
	}

	@SuppressWarnings("unchecked")
	public Resolution getSku() {

		HealthkartResponse healthkartResponse = null;
		Warehouse wareHouse = null;
		Sku sku = null;
		Map dataMap = new HashMap();
		ProductVariant pv = getProductVariantService().getVariantById(productVariantId);
		if (wareHouseId != null) {
			wareHouse = getWareHouseService().getWarehouseById(wareHouseId);
		} else {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "There Came an Error, please try again later");
			noCache();
		}
		if (pv != null) {
			sku = getSkuService().findSKU(pv, wareHouse);
			if (sku != null) {
				dataMap.put("sku", sku);
				dataMap.put("productName", sku.getProductVariant().getProduct().getName());
				dataMap.put("taxId", sku.getTax().getId());
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product Variant", dataMap);
			} else {
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Sku Not present for this Variant Id");
			}
		} else {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Variant Id");
			noCache();
		}
		return new JsonResolution(healthkartResponse);
	}

	public int getPageCount() {
		return purchaseOrderPage == null ? 0 : purchaseOrderPage.getTotalPages();
	}

	public int getResultCount() {
		return purchaseOrderPage == null ? 0 : purchaseOrderPage.getTotalResults();
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("extraInventoryId");
		params.add("purchaseOrderId");
		return params;
	}

	public ExtraInventoryService getExtraInventoryService() {
		return extraInventoryService;
	}

	public ExtraInventoryLineItemService getExtraInventoryLineItemService() {
		return extraInventoryLineItemService;
	}

	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public List<ExtraInventoryLineItem> getExtraInventoryLineItems() {
		return extraInventoryLineItems;
	}

	public void setExtraInventoryLineItems(List<ExtraInventoryLineItem> extraInventoryLineItems) {
		this.extraInventoryLineItems = extraInventoryLineItems;
	}

	public List<ExtraInventoryLineItem> getExtraInventoryLineItemsSelected() {
		return extraInventoryLineItemsSelected;
	}

	public void setExtraInventoryLineItemsSelected(List<ExtraInventoryLineItem> extraInventoryLineItemsSelected) {
		this.extraInventoryLineItemsSelected = extraInventoryLineItemsSelected;
	}

	public ExtraInventory getExtraInventory() {
		return extraInventory;
	}

	public void setExtraInventory(ExtraInventory extraInventory) {
		this.extraInventory = extraInventory;
	}

	public Long getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Long wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public ProductVariantService getProductVariantService() {
		return productVariantService;
	}

	public SkuService getSkuService() {
		return skuService;
	}

	public WarehouseService getWareHouseService() {
		return wareHouseService;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public PurchaseOrderService getPurchaseOrderService() {
		return purchaseOrderService;
	}

	public Long getExtraInventoryId() {
		return extraInventoryId;
	}

	public void setExtraInventoryId(Long extraInventoryId) {
		this.extraInventoryId = extraInventoryId;
	}

	public RtvNote getRtvNote() {
		return rtvNote;
	}

	public void setRtvNote(RtvNote rtvNote) {
		this.rtvNote = rtvNote;
	}

	public RtvNoteService getRtvNoteService() {
		return rtvNoteService;
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

	public Long getRtvNoteId() {
		return rtvNoteId;
	}

	public void setRtvNoteId(Long rtvNoteId) {
		this.rtvNoteId = rtvNoteId;
	}

	public Long getRtvStatusId() {
		return rtvStatusId;
	}

	public void setRtvStatusId(Long rtvStatusId) {
		this.rtvStatusId = rtvStatusId;
	}

	public Boolean isReconciled() {
		return isReconciled;
	}

	public void setReconciled(Boolean reconciled) {
		isReconciled = reconciled;
	}

	public Boolean getIsPiReconciled() {
		return isPiReconciled;
	}

	public void setIsPiReconciled(Boolean isPiReconciled) {
		this.isPiReconciled = isPiReconciled;
	}

	public Boolean isDebitToSupplier() {
		return isDebitToSupplier;
	}

	public void setDebitToSupplier(Boolean debitToSupplier) {
		isDebitToSupplier = debitToSupplier;
	}

	public String getReconciledStatus() {
		return reconciledStatus;
	}

	public void setReconciledStatus(String reconciledStatus) {
		this.reconciledStatus = reconciledStatus;
	}

	public PoLineItemService getPoLineItemService() {
		return poLineItemService;
	}

	public Long getNewPurchaseOrderId() {
		return newPurchaseOrderId;
	}

	public void setNewPurchaseOrderId(Long newPurchaseOrderId) {
		this.newPurchaseOrderId = newPurchaseOrderId;
	}

	public List<Tax> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<Tax> taxList) {
		this.taxList = taxList;
	}

	public MasterDataDao getMasterDataDao() {
		return masterDataDao;
	}

	public List<ExtraInventory> getExtraInventories() {
		return extraInventories;
	}

	public void setExtraInventories(List<ExtraInventory> extraInventories) {
		this.extraInventories = extraInventories;
	}

	public Long getExtraInventoryStatusId() {
		return extraInventoryStatusId;
	}

	public void setExtraInventoryStatusId(Long extraInventoryStatusId) {
		this.extraInventoryStatusId = extraInventoryStatusId;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public EmailManager getEmailManager() {
		return emailManager;
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

	public CourierPickupService getCourierPickupService() {
		return courierPickupService;
	}

	public Boolean getSameState() {
		return sameState;
	}

	public void setSameState(Boolean sameState) {
		this.sameState = sameState;
	}

	public String getSupplierState() {
		return supplierState;
	}

	public void setSupplierState(String supplierState) {
		this.supplierState = supplierState;
	}

	public String getWarehouseState() {
		return warehouseState;
	}

	public void setWarehouseState(String warehouseState) {
		this.warehouseState = warehouseState;
	}

	public List<ExtraInventoryLineItem> getExtraInventoryShortLineItemsSelected() {
		return extraInventoryShortLineItemsSelected;
	}

	public void setExtraInventoryShortLineItemsSelected(List<ExtraInventoryLineItem> extraInventoryShortLineItemsSelected) {
		this.extraInventoryShortLineItemsSelected = extraInventoryShortLineItemsSelected;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Double getShippingChargeHk() {
		return shippingChargeHk;
	}

	public void setShippingChargeHk(Double shippingChargeHk) {
		this.shippingChargeHk = shippingChargeHk;
	}

	public Double getShippingChargeVendor() {
		return shippingChargeVendor;
	}

	public void setShippingChargeVendor(Double shippingChargeVendor) {
		this.shippingChargeVendor = shippingChargeVendor;
	}

	public Boolean getReturnByHand() {
		return returnByHand;
	}

	public void setReturnByHand(Boolean returnByHand) {
		this.returnByHand = returnByHand;
	}

	public String getRtvReturnAddress() {
		return rtvReturnAddress;
	}

	public void setRtvReturnAddress(String rtvReturnAddress) {
		this.rtvReturnAddress = rtvReturnAddress;
	}

	public Date getRtvReturnDate() {
		return rtvReturnDate;
	}

	public void setRtvReturnDate(Date rtvReturnDate) {
		this.rtvReturnDate = rtvReturnDate;
	}

	public String getRtvTrackingNumber() {
		return rtvTrackingNumber;
	}

	public void setRtvTrackingNumber(String rtvTrackingNumber) {
		this.rtvTrackingNumber = rtvTrackingNumber;
	}

}
