package com.hk.web.action.admin.inventory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hk.domain.core.PurchaseOrderStatus;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.admin.manager.PurchaseOrderManager;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.manager.EmailManager;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class EditPurchaseOrderAction extends BaseAction {

    private static Logger    logger      = LoggerFactory.getLogger(EditPurchaseOrderAction.class);
    @Autowired
    PurchaseOrderDao         purchaseOrderDao;
    @Autowired
    PoLineItemDao            poLineItemDao;
    @Autowired
    PurchaseOrderManager     purchaseOrderManager;

    @Autowired
    private ProductVariantService    productVariantService;
    @Autowired
    XslParser                xslParser;
    @Autowired
    EmailManager             emailManager;
    @Autowired
    SkuService               skuService;

    // @Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                   adminUploadsPath;

    @Validate(required = true, on = "parse")
    private FileBean         fileBean;

    private PurchaseOrder    purchaseOrder;
    private List<PoLineItem> poLineItems = new ArrayList<PoLineItem>();
    public PurchaseOrderDto  purchaseOrderDto;
    public String            productVariantId;
    public Warehouse         warehouse;
	private PurchaseOrderStatus       previousPurchaseOrderStatus ;

    @DefaultHandler
    public Resolution pre() {
        logger.debug("purchaseOrder@Pre: " + purchaseOrder.getId());
        purchaseOrderDto = purchaseOrderManager.generatePurchaseOrderDto(purchaseOrder);
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
	                    if(sku != null) {
		                    dataMap.put("sku", sku);
		                    if(sku.getTax() != null) {
			                    dataMap.put("tax", sku.getTax().getValue());
		                    }
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
		    boolean errorReturn = false;
		    if (purchaseOrder.getPurchaseOrderStatus() == null) {
			    addRedirectAlertMessage(new SimpleMessage("Please Select status"));
		    }
		    long newStatus = purchaseOrder.getPurchaseOrderStatus().getId().longValue();
		    long oldStatus = getPreviousPurchaseOrderStatus().getId().longValue();
		    if (newStatus < oldStatus) {
			    if ((newStatus == EnumPurchaseOrderStatus.Generated.getId().longValue()) && (oldStatus == EnumPurchaseOrderStatus.Cancelled.getId().longValue())) {
				    errorReturn = false;
			    } else {
				    addRedirectAlertMessage(new SimpleMessage("The Selected Status Should Be After  : " + getPreviousPurchaseOrderStatus().getName()));
				    errorReturn = true;
			    }
		    } else if (newStatus > oldStatus) {
			    PurchaseOrderStatus expectPurchaseStatus = EnumPurchaseOrderStatus.getNextPurchaseOrderStatus(previousPurchaseOrderStatus);
			    if (purchaseOrder.getPurchaseOrderStatus().getId().equals(EnumPurchaseOrderStatus.Cancelled.getId())) {
				    errorReturn = false;
			    } else if (!(expectPurchaseStatus.equals(purchaseOrder.getPurchaseOrderStatus()))) {
				    addRedirectAlertMessage(new SimpleMessage("Please Choose Correct Next  Status : " + expectPurchaseStatus.getName()));
				    errorReturn = true;
			    }
		    } else if (newStatus == oldStatus && (oldStatus != EnumPurchaseOrderStatus.Generated.getId().longValue())) {
			    addRedirectAlertMessage(new SimpleMessage("PO is Already : " + purchaseOrder.getPurchaseOrderStatus().getName()));
			    errorReturn = true;
		    }
		    if (errorReturn) {
			    return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
		    }
		    double discountRatio = 0;
		    if (purchaseOrder.getPayable() != null && purchaseOrder.getPayable() > 0 && purchaseOrder.getDiscount() != null) {
			    discountRatio = purchaseOrder.getDiscount() / purchaseOrder.getPayable();
		    }
            for (PoLineItem poLineItem : poLineItems) {
                if (poLineItem.getQty() != null) {
                    if (poLineItem.getQty() == 0 && poLineItem.getId() != null) {
                        getBaseDao().delete(poLineItem);
                    } else if (poLineItem.getQty() > 0) {
	                    if(poLineItem.getPayableAmount() != null) {
		                    poLineItem.setProcurementPrice((poLineItem.getPayableAmount() / poLineItem.getQty()) - (poLineItem.getPayableAmount() / poLineItem.getQty() * discountRatio ));
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
            purchaseOrderDao.save(purchaseOrder);

            if (purchaseOrder.getPurchaseOrderStatus().getId() == EnumPurchaseOrderStatus.SentForApproval.getId()) {
                emailManager.sendPOSentForApprovalEmail(purchaseOrder);
            } else if (purchaseOrder.getPurchaseOrderStatus().getId() == EnumPurchaseOrderStatus.SentToSupplier.getId()) {
                emailManager.sendPOPlacedEmail(purchaseOrder);
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
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
}