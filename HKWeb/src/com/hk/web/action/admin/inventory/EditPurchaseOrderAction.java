package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.admin.manager.PurchaseOrderManager;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.payment.PaymentHistory;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.taglibs.Functions;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class EditPurchaseOrderAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(EditPurchaseOrderAction.class);
	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	@Autowired
	PoLineItemDao poLineItemDao;
	@Autowired
	PurchaseOrderManager purchaseOrderManager;

	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	XslParser xslParser;
	@Autowired
	EmailManager emailManager;
	@Autowired
	AdminEmailManager adminEmailManager;
	@Autowired
	SkuService skuService;
    @Autowired
    private SupplierDao supplierDao;

	// @Named(Keys.Env.adminUploads)
	@Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminUploadsPath;

	@Validate(required = true, on = "parse")
	private FileBean fileBean;

    private  Supplier supplier;

	private PurchaseOrder purchaseOrder;
	private List<PoLineItem> poLineItems = new ArrayList<PoLineItem>();
	public PurchaseOrderDto purchaseOrderDto;
	public String productVariantId;
	public Warehouse warehouse;
	private PurchaseOrderStatus previousPurchaseOrderStatus;
	private List<Long> newSkuIdList = new ArrayList<Long>();
    private List<Supplier> suppliers = new ArrayList<Supplier>();


    private boolean isPiCreated;
	@DefaultHandler
	public Resolution pre() {
		if(purchaseOrder != null){
			logger.info("purchaseOrder@Pre: " + purchaseOrder.getId());
		}
        suppliers = supplierDao.getListOrderedByName();
		purchaseOrderDto = purchaseOrderManager.generatePurchaseOrderDto(purchaseOrder);
		for(PoLineItem poLineItem : purchaseOrder.getPoLineItems()) {
			if(poLineItemDao.getPoLineItemCountBySku(poLineItem.getSku()) <= 1) {
				newSkuIdList.add(poLineItem.getSku().getId());
			}
		}

        isPiCreated=purchaseOrderDao.isPiCreated(purchaseOrder);
         return new ForwardResolution("/pages/admin/editPurchaseOrder.jsp");
	}

	@SuppressWarnings("unchecked")
	public Resolution getPVDetails() {
		Map dataMap = new HashMap();
		if (StringUtils.isNotBlank(productVariantId)) {
			ProductVariant pv = getProductVariantService().getVariantById(productVariantId);
			if (pv != null) {
				try {
					dataMap.put("variant", pv);
					if (warehouse != null) {
						Sku sku = skuService.getSKU(pv, warehouse);
						if (sku != null) {
							dataMap.put("sku", sku);
							dataMap.put("last30DaysSales", Functions.findInventorySoldInGivenNoOfDays(sku, 30));
							if (sku.getTax() != null) {
								dataMap.put("tax", sku.getTax().getValue());
								dataMap.put("tax_id", sku.getTax().getId());
							}
							if(poLineItemDao.getPoLineItemCountBySku(sku) == 0) {
								dataMap.put("newSku", true);
							} else {
								dataMap.put("newSku", false);
							}
						}
					}
					if(purchaseOrder != null) {
						ProductVariantSupplierInfo productVariantSupplierInfo = Functions.getPVSupplierInfo(purchaseOrder.getSupplier(), pv);
						if(productVariantSupplierInfo != null) {
							dataMap.put("historicalFillRate", productVariantSupplierInfo.getFillRate());
						}
					}
					dataMap.put("product", pv.getProduct().getName());
					dataMap.put("options", pv.getOptionsCommaSeparated());
					HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product Variant", dataMap);
					noCache();
					return new JsonResolution(healthkartResponse);
				} catch (Exception e) {
					HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(), dataMap);
					noCache();
					return new JsonResolution(healthkartResponse);
				}
			}
		} else {
			logger.error("null or empty product variant id passed to load pv details in getPvDetails method of EditPurchaseOrderAction");
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product VariantID", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);
	}

	public Resolution save() {
		if (purchaseOrder != null && purchaseOrder.getId() != null) {
			logger.debug("poLineItems@Save: " + poLineItems.size());

			if (purchaseOrder.getPurchaseOrderStatus() == null) {
				addRedirectAlertMessage(new SimpleMessage("Please Select status"));
				return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
			}

			if(previousPurchaseOrderStatus.equals(EnumPurchaseOrderStatus.SentToSupplier.getPurchaseOrderStatus())
					&& purchaseOrder.getPurchaseOrderStatus().equals(EnumPurchaseOrderStatus.Cancelled.getPurchaseOrderStatus())
					&& purchaseOrder.getGoodsReceivedNotes() != null && purchaseOrder.getGoodsReceivedNotes().size() > 0) {
				addRedirectAlertMessage(new SimpleMessage("GRN has already been created, cannot cancel PO now."));
				return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
			}

			List<PurchaseOrderStatus> allowedNewPOStatusList = EnumPurchaseOrderStatus.getAllowedPOStatusToChange(previousPurchaseOrderStatus);
			if(! allowedNewPOStatusList.contains(purchaseOrder.getPurchaseOrderStatus())) {
				addRedirectAlertMessage(new SimpleMessage("Invalid Status chosen."));
				return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
			}

			double discountRatio = 0;
			if (purchaseOrder.getPayable() != null && purchaseOrder.getPayable() > 0 && purchaseOrder.getDiscount() != null) {
				discountRatio = purchaseOrder.getDiscount() / purchaseOrder.getPayable();
			}
			for (PoLineItem poLineItem : poLineItems) {
					if (poLineItem.getQty() != null) {
					if(poLineItem.getMrp() < poLineItem.getCostPrice()){
						addRedirectAlertMessage(new SimpleMessage("MRP cannot be less than cost price for variant " + poLineItem.getProductVariant().getId()));
						return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
					}
					if (poLineItem.getQty() == 0 && poLineItem.getId() != null) {
						getBaseDao().delete(poLineItem);
					} else if (poLineItem.getQty() > 0) {
						if (poLineItem.getPayableAmount() != null) {
							poLineItem.setProcurementPrice((poLineItem.getPayableAmount() / poLineItem.getQty()) - (poLineItem.getPayableAmount() / poLineItem.getQty() * discountRatio));
						}
						Sku sku = null;
						try {
							sku = skuService.getSKU(poLineItem.getProductVariant(), purchaseOrder.getWarehouse());
						} catch (Exception e) {
							addRedirectAlertMessage(new SimpleMessage("SKU doesn't exist for " + poLineItem.getProductVariant().getId()));
							return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
						}
						poLineItem.setSku(sku);
						poLineItem.setPurchaseOrder(purchaseOrder);
						try {
							poLineItemDao.save(poLineItem);
						} catch (Exception e) {
							e.printStackTrace();
							addRedirectAlertMessage(new SimpleMessage("Duplicate variant - " + poLineItem.getSku().getProductVariant().getId()));
							return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
						}
					}
				}
			}
			purchaseOrder.setUpdateDate(new Date());
			purchaseOrderDto = purchaseOrderManager.generatePurchaseOrderDto(purchaseOrder);
			purchaseOrder.setPayable(purchaseOrderDto.getTotalPayable());
			double overallDiscount = purchaseOrder.getDiscount() != null ? purchaseOrder.getDiscount() : 0;
			purchaseOrder.setFinalPayableAmount(purchaseOrder.getPayable() - overallDiscount);
			purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);


			if (purchaseOrder.getPurchaseOrderStatus().getId().equals(EnumPurchaseOrderStatus.SentForApproval.getId())) {
				emailManager.sendPOSentForApprovalEmail(purchaseOrder);
			} else if (purchaseOrder.getPurchaseOrderStatus().getId().equals(EnumPurchaseOrderStatus.Approved.getId())) {
				adminEmailManager.sendPOApprovedEmail(purchaseOrder);
			} else if (purchaseOrder.getPurchaseOrderStatus().getId().equals(EnumPurchaseOrderStatus.SentToSupplier.getId())) {
				purchaseOrder.setPoPlaceDate(new Date());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getLeadTime());
				purchaseOrder.setEstDelDate(calendar.getTime());
				if (purchaseOrder.getSupplier().getCreditDays() != null && purchaseOrder.getSupplier().getCreditDays() >= 0) {
					calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getCreditDays());
					purchaseOrder.setEstPaymentDate(calendar.getTime());
				} else {
					purchaseOrder.setEstPaymentDate(new Date());
				}
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);

				emailManager.sendPOPlacedEmail(purchaseOrder);
				if (purchaseOrder.getSupplier().getCreditDays() < 0 && purchaseOrder.getAdvPayment() > 0) {
					try {
						PaymentHistory paymentHistoryNew = new PaymentHistory();
						paymentHistoryNew.setPurchaseOrder(purchaseOrder);
						paymentHistoryNew.setAmount(purchaseOrder.getAdvPayment());
						paymentHistoryNew.setScheduledPaymentDate(new Date());
						paymentHistoryNew.setActualPaymentDate(new Date());
						paymentHistoryNew.setModeOfPayment("NEFT");
						getBaseDao().save(paymentHistoryNew);
					} catch (Exception e) {
						logger.error("Could not insert new payment detail: ", e);
						addRedirectAlertMessage(new SimpleMessage("Couldn't create payment history"));
						return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
					}
				}
			}
		}
		addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		return new RedirectResolution(POAction.class);
	}

	public Resolution closePurchaseOrder() {
		if (purchaseOrder != null) {
			purchaseOrder.setPurchaseOrderStatus(EnumPurchaseOrderStatus.Closed.getPurchaseOrderStatus());
			purchaseOrderDao.save(purchaseOrder);
			addRedirectAlertMessage(new SimpleMessage("PO closed."));
		} else {
			addRedirectAlertMessage(new SimpleMessage("PO not found"));
		}
		return new RedirectResolution(POAction.class);

	}

	public Resolution parse() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String excelFilePath = adminUploadsPath + "/poFiles/" + sdf.format(new Date()) + "/POID-" + purchaseOrder + "-" + sdf.format(new Date()) + ".xls";
		File excelFile = new File(excelFilePath);
		excelFile.getParentFile().mkdirs();
		fileBean.save(excelFile);

		try {
			Set<PoLineItem> poLineItems = xslParser.readAndCreatePOLineItems(excelFile, purchaseOrder);
			addRedirectAlertMessage(new SimpleMessage(poLineItems.size() + " PO Line Items Created Successfully."));
		} catch (Exception e) {
			logger.error("Exception while reading excel sheet.", e);
			addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
		}
		return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
	}

    public Resolution saveSupplier(){
        purchaseOrder.setSupplier(supplier);
        getBaseDao().save(purchaseOrder);
        return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
    }

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public List<PoLineItem> getPoLineItems() {
		return poLineItems;
	}

	public void setPoLineItems(List<PoLineItem> poLineItems) {
		this.poLineItems = poLineItems;
	}

	public PurchaseOrderDto getPurchaseOrderDto() {
		return purchaseOrderDto;
	}

	public void setPurchaseOrderDto(PurchaseOrderDto purchaseOrderDto) {
		this.purchaseOrderDto = purchaseOrderDto;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public ProductVariantService getProductVariantService() {
		return productVariantService;
	}

	public void setProductVariantService(ProductVariantService productVariantService) {
		this.productVariantService = productVariantService;
	}

	public PurchaseOrderManager getPurchaseOrderManager() {
		return purchaseOrderManager;
	}

	public void setPurchaseOrderManager(PurchaseOrderManager purchaseOrderManager) {
		this.purchaseOrderManager = purchaseOrderManager;
	}

	public PurchaseOrderStatus getPreviousPurchaseOrderStatus() {
		return previousPurchaseOrderStatus;
	}

	public void setPreviousPurchaseOrderStatus(PurchaseOrderStatus previousPurchaseOrderStatus) {
		this.previousPurchaseOrderStatus = previousPurchaseOrderStatus;
	}

	public List<Long> getNewSkuIdList() {
		return newSkuIdList;
	}

	public void setNewSkuIdList(List<Long> newSkuIdList) {
		this.newSkuIdList = newSkuIdList;
	}

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public boolean isPiCreated() {
        return isPiCreated;
    }

    public void setPiCreated(boolean piCreated) {
        isPiCreated = piCreated;
    }
}