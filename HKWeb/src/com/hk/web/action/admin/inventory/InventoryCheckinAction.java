package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.dto.inventory.CycleCountDto;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.*;
import com.hk.admin.pact.service.catalog.product.ProductVariantSupplierInfoService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.PoLineItemService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.admin.util.BarcodeUtil;
import com.hk.admin.util.CycleCountDtoUtil;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.constants.inventory.EnumGrnStatus;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.inventory.EnumStockTransferStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.*;
import com.hk.domain.inventory.crossDomain.InventoryBarcodeMapItem;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.dao.warehouse.WarehouseDao;
import com.hk.util.XslGenerator;
import com.hk.util.HKDateUtil;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.jsoup.helper.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.INVENTORY_CHECKIN}, authActionBean = AdminPermissionAction.class)
@Component
public class InventoryCheckinAction extends BaseAction {

    private static Logger logger = Logger.getLogger(InventoryCheckinAction.class);
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private AdminInventoryService adminInventoryService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private UserService userService;
    @Autowired
    private GrnLineItemDao grnLineItemDao;
    @Autowired
    private GoodsReceivedNoteDao goodsReceivedNoteDao;
    // private LowInventoryDao lowInventoryDao;
    @Autowired
    private XslGenerator xslGenerator;
    @Autowired
    private XslParser xslParser;
    @Autowired
    private StockTransferDao stockTransferDao;
    @Autowired
    private ProductVariantSupplierInfoService productVariantSupplierInfoService;
    @Autowired
	  PoLineItemDao poLineItemDao;
    @Autowired
    PurchaseOrderService purchaseOrderService;

    @Autowired
    private SkuGroupService skuGroupService;
    @Autowired
    BaseDao baseDao;
    @Autowired
    private AdminEmailManager adminEmailManager;
    @Autowired
    private ExtraInventoryService extraInventoryService;
    @Autowired
    PoLineItemService poLineItemService;
    private List<SkuGroup> skuGroupList;
    @Autowired
    CycleCountService cycleCountService;
    @Autowired
	  private PurchaseInvoiceDao purchaseInvoiceDao;
    @Autowired
    InventoryBarcodeMapDao inventoryBarcodeMapDao;
    @Autowired
    WarehouseDao warehouseDao;
    @Autowired
    AdminProductVariantInventoryDao adminProductVariantInventoryDao;
    @Autowired
    SkuItemLineItemDao skuItemLineItemDao;

    @Validate(required = true, on = "save")
    private String upc;
    @Validate(required = true, minvalue = 1.0, on = "save")
    private Long qty;
    @Validate(required = true, on = "save")
    private Double costPrice;
    private Double mrp;
    private String batch;
    private Date mfgDate;
    private Date expiryDate;
    private GoodsReceivedNote grn;
    private String invoiceNumber;
    private Date invoiceDate;
    private StockTransfer stockTransfer;
    int strLength = 20;
    File printBarcode;
    private Sku sku;
    private SkuGroup checkinSkuGroup;
    private GrnLineItem grnLineItem;
    private String productVariantBarcode;
    private String invBarcode;
    private Boolean isBrightSupplier = Boolean.FALSE;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;

    @Value("#{hkEnvProps['" + Keys.Env.barcodeGurgaon + "']}")
    String barcodeGurgaon;

    @Value("#{hkEnvProps['" + Keys.Env.barcodeMumbai + "']}")
    String barcodeMumbai;

    @Validate(required = true, on = "parse")
    private FileBean fileBean;

    private final double TOLERANCE_LEVEL_PERCENTAGE = 10; // Max allowed percentage value for CP, MRP etc to be higher or lower than
    // the corresponding product variant value.

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
    	Supplier supplier = grn.getPurchaseOrder().getSupplier();
    	Warehouse warehouse = warehouseDao.findWarehouseByTin(supplier.getTinNumber());
    	//TODO: Warehouse is Bright
    	isBrightSupplier = Boolean.TRUE;
        return new ForwardResolution("/pages/admin/inventoryCheckin.jsp");
    }

    @SuppressWarnings("unchecked")
    @JsonHandler
    public Resolution validateFields() {
        Map dataMap = new HashMap();
        HealthkartResponse healthkartResponse = null;
        try {
            ProductVariant productVariant = getProductVariantService().findVariantFromUPC(upc);
            if (productVariant == null) {
                productVariant = getProductVariantService().getVariantById(upc);
            }
            if (productVariant != null) {
                if (costPrice != null && (costPrice > productVariant.getCostPrice() + TOLERANCE_LEVEL_PERCENTAGE * productVariant.getCostPrice() / 100)) {
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                            /*"Cost price is higher than the maximum permissible limit of " + TOLERANCE_LEVEL_PERCENTAGE + " %. \n" +*/
                            "Cost price of the variant in the system is Rs. " + productVariant.getCostPrice() + "\n Do you want to continue?", dataMap);
                } else if (costPrice != null && (costPrice < productVariant.getCostPrice() - TOLERANCE_LEVEL_PERCENTAGE * productVariant.getCostPrice() / 100)) {
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                            /*"Cost price is lesser than the maximum permissible limit of " + TOLERANCE_LEVEL_PERCENTAGE + " %. \n" +*/
                            "Cost price of the variant in the system is Rs. " + productVariant.getCostPrice() + "\n Do you want to continue?", dataMap);
                } else if (mrp != null && (mrp > productVariant.getMarkedPrice() + TOLERANCE_LEVEL_PERCENTAGE * productVariant.getMarkedPrice() / 100)) {
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                            /*"MRP is higher than the maximum permissible limit of " + TOLERANCE_LEVEL_PERCENTAGE +" %. \n" +*/
                            "MRP of the variant in the system is Rs. " + productVariant.getMarkedPrice() + "\n Do you want to continue?", dataMap);
                } else if (mrp != null && (mrp < productVariant.getMarkedPrice() - TOLERANCE_LEVEL_PERCENTAGE * productVariant.getMarkedPrice() / 100)) {
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                            /*"MRP is lesser than the maximum permissible limit of " + TOLERANCE_LEVEL_PERCENTAGE +" %. \n" +*/
                            "MRP of the variant in the system is Rs. " + productVariant.getMarkedPrice() + "\n Do you want to continue?", dataMap);
                } else {
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Cost price and MRP are within the permissible limit", dataMap);
                }
            } else {
                healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "No such UPC or Variant Id. Do you want to continue?", dataMap);
            }

        } catch (Exception e) {
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(), dataMap);
        }
        noCache();
        return new JsonResolution(healthkartResponse);
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
			return new RedirectResolution(InventoryCheckinAction.class);
		}
		logger.debug("upc: " + upc);
		try {
			ProductVariant productVariant = getProductVariantService().findVariantFromUPC(upc);
			if (productVariant == null) {
				productVariant = getProductVariantService().getVariantById(upc);
			}
			if (productVariant != null) {
				if (productVariant.getProduct().isDeleted()) {
					addRedirectAlertMessage(new SimpleMessage("Product is marked Deleted for Variant:" + productVariant.getId() + " - Plz get it fixed by Category Team"));
					return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
				}
				Sku sku = getSkuService().getSKU(productVariant, grn.getWarehouse());
				List<CycleCountDto> cycleCountInProgressForVariantList = cycleCountService.inProgressCycleCountForVariant(productVariant, grn.getWarehouse());
				if (cycleCountInProgressForVariantList != null && cycleCountInProgressForVariantList.size() > 0) {
					String closeAuditMsg = CycleCountDtoUtil.getCycleCountInProgress(cycleCountInProgressForVariantList);
					closeAuditMsg = closeAuditMsg + "  For  :  " + grn.getWarehouse().getCity();
					addRedirectAlertMessage(new SimpleMessage(closeAuditMsg));
					return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
				}

				Long askedQty = 0L;
				GrnLineItem grnLineItem = getGrnLineItemDao().getGrnLineItem(grn, productVariant);
				if (grnLineItem != null && sku != null) {
					askedQty = grnLineItem.getQty();
					Long alreadyCheckedInQty = getAdminInventoryService().countOfCheckedInUnitsForGrnLineItem(grnLineItem);
					if (qty > (askedQty - alreadyCheckedInQty)) {
						addRedirectAlertMessage(new SimpleMessage("Qty mentioned - " + qty + " is exceeding required checked in qty. Plz check."));
						return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
					}
					if (StringUtils.isBlank(batch) || StringUtils.isEmpty(batch)) {
						addRedirectAlertMessage(new SimpleMessage("Batch is required. Plz check."));
						return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
					}
					if (mrp != null && mrp.equals(0.0D)) {
						addRedirectAlertMessage(new SimpleMessage("MRP is required. Plz check."));
						return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
					}
					SkuGroup skuGroup = getAdminInventoryService().createSkuGroupWithoutBarcode(batch, mfgDate, expiryDate, costPrice, mrp, grn, null, null, sku);
					getAdminInventoryService().createSkuItemsAndCheckinInventory(null, skuGroup, qty, null, grnLineItem, null, null,
							getInventoryService().getInventoryTxnType(EnumInvTxnType.INV_CHECKIN), user);
					getInventoryService().checkInventoryHealth(productVariant);

					if (getInventoryService().allInventoryCheckedIn(grn)) {
						// TODO: Escalate all the shipping orders corresponding to this GRN
						getPurchaseOrderService().updatePOFillRate(grn.getPurchaseOrder());
						for (PoLineItem poLineItem : grn.getPurchaseOrder().getPoLineItems()) {
							if (poLineItemDao.getPoLineItemCountBySku(poLineItem.getSku()) <= 1) {
								poLineItem.setFirstTimePurchased(true);
							}
						}
						if (grn.getPurchaseOrder().isExtraInventoryCreated()) {
							PurchaseOrder po = grn.getPurchaseOrder();
							Long id = getExtraInventoryService().getExtraInventoryByPoId(po.getId()).getId();
							po.setExtraInventoryId(id);
						}

						List<GrnLineItem> grnLineItems = grn.getGrnLineItems();
						if (grnLineItems != null && grnLineItems.size() > 0) {
							for (GrnLineItem item : grnLineItems) {
								if (item.getQty() != null && item.getQty() == 0) {
									if (grn.getPurchaseInvoices() != null && grn.getPurchaseInvoices().size() > 0) {
										for (PurchaseInvoice invoice : grn.getPurchaseInvoices()) {
											List<PurchaseInvoiceLineItem> items = invoice.getPurchaseInvoiceLineItems();
											if (items != null && items.size() > 0) {
												for (PurchaseInvoiceLineItem pili : items) {
													if (pili.getGrnLineItem().equals(item)) {
														pili.setGrnLineItem(null);
														purchaseInvoiceDao.save(pili);
													}
												}
											}
										}
									}
									grnLineItemDao.delete(item);
								}

							}
						}
						grn.setGrnStatus(EnumGrnStatus.Closed.asGrnStatus());
						getGoodsReceivedNoteDao().save(grn);
						getAdminEmailManager().sendGRNEmail(grn);
						editPVFillRate(grn);
					} else {
						grn.setGrnStatus(EnumGrnStatus.InventoryCheckinInProcess.asGrnStatus());
						getGoodsReceivedNoteDao().save(grn);
						editPVFillRate(grn);
					}
				} else {
					addRedirectAlertMessage(new SimpleMessage("Error with either GrnLineItem->" + grnLineItem + " or Sku ->" + sku));
					return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
				}
			} else {
				addRedirectAlertMessage(new SimpleMessage("No such UPC or Variant Id"));
				return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
			}
		} catch (Exception e) {
			logger.error("Error while checkin : ", e);
			addRedirectAlertMessage(new SimpleMessage("Duplicate UPC or Variants in PO - Please fix the same."));
			return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
		}

		addRedirectAlertMessage(new SimpleMessage("Inventory Checked in successfully. Qty = " + qty));
		return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
	}
	
	private void editPVFillRate(GoodsReceivedNote grn) {
		if (grn != null) {
			Supplier supplier = grn.getPurchaseOrder().getSupplier();
			for (GrnLineItem grnLineItem : grn.getGrnLineItems()) {
				ProductVariantSupplierInfo productVariantSupplierInfo = productVariantSupplierInfoService.getOrCreatePVSupplierInfo(grnLineItem.getSku()
						.getProductVariant(), supplier);
				productVariantSupplierInfoService.updatePVSupplierInfo(productVariantSupplierInfo, null, grnLineItem.getQty());
			}
		}
	}

	public Resolution checkinInvBarcode() {
		if (StringUtil.isBlank(invBarcode)) {
			addRedirectAlertMessage(new SimpleMessage("Barcode cannot be blank"));
			return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
		}
		User loggedOnUser = null;
		if (getPrincipal() != null) {
			loggedOnUser = getUserService().getUserById(getPrincipal().getId());
		}
		InventoryBarcodeMapItem item = inventoryBarcodeMapDao.getInventoryBarcodeMapItem(invBarcode, grn.getPurchaseOrder());
		if (item != null) {
			ProductVariant productVariant = getProductVariantService().getVariantById(item.getVariantId());
			if (productVariant != null) {
				Sku sku = getSkuService().getSKU(productVariant, grn.getWarehouse());
				if (sku != null) {
					GrnLineItem grnLineItem = getGrnLineItemDao().getGrnLineItem(grn, productVariant);
					if (grnLineItem != null) {
						Long askedQty = grnLineItem.getQty();
						Long alreadyCheckedInQty = getAdminInventoryService().countOfCheckedInUnitsForGrnLineItem(grnLineItem);
						if (alreadyCheckedInQty >= askedQty) {
							addRedirectAlertMessage(new SimpleMessage("Asked qty is already checked in."));
							return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
						}
						try {
							SkuItem skuItem = skuGroupService.getSkuItemByBarcode(invBarcode);
							if (skuItem != null) {
								adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, grnLineItem, null, null, EnumSkuItemStatus.Checked_IN,
										EnumSkuItemOwner.SELF, getInventoryService().getInventoryTxnType(EnumInvTxnType.INV_CHECKIN), 1l, loggedOnUser);
								getInventoryService().checkInventoryHealth(sku.getProductVariant());
								skuGroupService.updateBookingAfterCheckin(skuItem, skuItem.getSkuGroup());
								addRedirectAlertMessage(new SimpleMessage("Inventory Checked in successfully. Qty = " + qty));
								return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
							} else {
								SkuGroup skuGroup = skuGroupService.getForeignSkuGroup(grn.getWarehouse().getId(), item.getSkuGroupId());
								if (skuGroup == null)
									skuGroup = getAdminInventoryService().createSkuGroup(null, item.getBatchNumber(), item.getMfgDate(), item.getExpDate(), item.getCostPrice(),
											item.getMrp(), grn, null, null, sku, item.getSkuGroupId());

								String siBarcode = item.getSkuItemBarcode();
								if (StringUtils.isNumeric(siBarcode)) {
									siBarcode = new String(skuGroup.getId() + "-" + (skuGroup.getSkuItems().size() + 1));
								}
								getAdminInventoryService().createSkuItemsAndCheckinInventory(siBarcode, skuGroup, 1L, null, grnLineItem, null, null,
										getInventoryService().getInventoryTxnType(EnumInvTxnType.INV_CHECKIN), loggedOnUser);
							}
							getInventoryService().checkInventoryHealth(productVariant);
							if (getInventoryService().allInventoryCheckedIn(grn)) {
								String message = "All inventory checked in. GRN is marked closed";
								Set<ShippingOrder> shippingOrders = adminInventoryService.manuallyEscalateShippingOrdersForThisCheckin(grn);
								if(shippingOrders!=null&&shippingOrders.size()>0){
									String escalated = "";
									for (ShippingOrder shippingOrder : shippingOrders) {
										escalated +=shippingOrder.getId().toString()+", ";
									}
									message.concat("<br>Shipping Order(s) - "+escalated+" were escalated.");
								}
								
								getPurchaseOrderService().updatePOFillRate(grn.getPurchaseOrder());
								grn.setGrnStatus(EnumGrnStatus.Closed.asGrnStatus());
								getGoodsReceivedNoteDao().save(grn);
								editPVFillRate(grn);
								addRedirectAlertMessage(new SimpleMessage(message));
								return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
							} else {
								grn.setGrnStatus(EnumGrnStatus.InventoryCheckinInProcess.asGrnStatus());
								getGoodsReceivedNoteDao().save(grn);
								editPVFillRate(grn);
							}
						} catch (Exception e) {
							addRedirectAlertMessage(new SimpleMessage("Oops!! The Barcode is unique and it is already checked in."));
							return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
						}
					} else {
						addRedirectAlertMessage(new SimpleMessage("No GRN LineIten for SKU for " + productVariant.getId() + " in " + grn.getWarehouse().getIdentifier()
								+ " in the GRN"));
						return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
					}
				} else {
					addRedirectAlertMessage(new SimpleMessage("No SKU for " + productVariant.getId() + " in " + grn.getWarehouse().getIdentifier()));
					return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
				}
			} else {
				addRedirectAlertMessage(new SimpleMessage("No Variant " + item.getVariantId() + " for barcode mapping# " + invBarcode));
				return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
			}
		} else {
			addRedirectAlertMessage(new SimpleMessage("No barcode mapping availabe for barcode# " + invBarcode + " for PO#" + grn.getPurchaseOrder().getId()));
			return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
		}
		addRedirectAlertMessage(new SimpleMessage("Checkin Successful"));
		return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
	}


    public Resolution saveStockTransfer() {
        SkuItem skuItem = null;

        if (stockTransfer == null) {
            addRedirectAlertMessage(new SimpleMessage("Invalid Stock Transfer"));
            return new ForwardResolution("/pages/admin/stockTransfer.jsp");
        }
        if (StringUtil.isBlank(productVariantBarcode)) {
            addRedirectAlertMessage(new SimpleMessage("Barcode cannot be blank"));
            return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId()).addParameter(
                    "checkinInventoryAgainstStockTransfer", stockTransfer.getId());
        }

        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }

        SkuGroup skuGroup = null;
        SkuItem skuItemBarcode = skuGroupService.getSkuItemByBarcode(productVariantBarcode, stockTransfer.getFromWarehouse().getId(), EnumSkuItemStatus.Stock_Transfer_Out.getId());
        if (skuItemBarcode != null) {
            skuGroup = skuItemBarcode.getSkuGroup();
        } else {
            skuGroupList = skuGroupService.getSkuGroupsByBarcodeForStockTransfer(productVariantBarcode, stockTransfer.getFromWarehouse().getId());
            if (skuGroupList == null || skuGroupList.size() <= 0) {
                addRedirectAlertMessage(new SimpleMessage("No SKU Group found for Barcode"));
                return new RedirectResolution(StockTransferAction.class, "checkinInventoryAgainstStockTransfer").addParameter("stockTransfer", stockTransfer.getId());
            }
            skuGroup = skuGroupList.get(0);
        }
        StockTransferLineItem stockTransferLineItem = stockTransferDao.getStockTransferLineItemForCheckedOutSkuGrp(skuGroup, stockTransfer);
        if (stockTransferLineItem == null) {
            addRedirectAlertMessage(new SimpleMessage("Wrong Barcode for this stock Transfer"));
            return new RedirectResolution(StockTransferAction.class, "checkinInventoryAgainstStockTransfer").addParameter("stockTransfer", stockTransfer.getId());
        }

        if (stockTransferLineItem.getCheckedoutQty() <= 0) {
            addRedirectAlertMessage(new SimpleMessage("Please do transfer some Item to check in "));
            return new RedirectResolution(StockTransferAction.class, "checkinInventoryAgainstStockTransfer").addParameter("stockTransfer", stockTransfer.getId());
        }

        StockTransferLineItem stockTransferLineItemAgainstCheckInSkuGrp = stockTransferDao.checkinSkuGroupExists(stockTransferLineItem);
        ProductVariant productVariant = skuGroup.getSku().getProductVariant();
        Warehouse toWarehouse = stockTransfer.getToWarehouse();
        sku = skuService.getSKU(productVariant, toWarehouse);
        if (sku == null) {
            addRedirectAlertMessage(new SimpleMessage("No SKU Found for ProductVariantId:-" + (productVariant == null ? "" : productVariant.getId())));
            return new RedirectResolution(StockTransferAction.class, "checkinInventoryAgainstStockTransfer").addParameter("stockTransfer", stockTransfer.getId());
        }

        if (stockTransferLineItemAgainstCheckInSkuGrp == null) {
            checkinSkuGroup = getAdminInventoryService().createSkuGroupWithoutBarcode(skuGroup.getBatchNumber(), skuGroup.getMfgDate(), skuGroup.getExpiryDate(), skuGroup.getCostPrice(), skuGroup.getMrp(), null, null, stockTransfer, sku);
            checkinSkuGroup.setBarcode(skuGroup.getBarcode());
            checkinSkuGroup = (SkuGroup) getBaseDao().save(checkinSkuGroup);
        } else {
            checkinSkuGroup = stockTransferLineItemAgainstCheckInSkuGrp.getCheckedInSkuGroup();
        }

        if (skuItemBarcode != null) {
            skuItem = skuItemBarcode;
        } else {
            skuItem = skuGroupService.getSkuItem(skuGroup,Arrays.asList( EnumSkuItemStatus.Stock_Transfer_Out.getSkuItemStatus()));
        }

        if (skuItem != null) {

            if (stockTransferLineItem.getCheckedinQty() == null || (!stockTransferLineItem.getCheckedinQty().equals(stockTransferLineItem.getCheckedoutQty()))) {
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                skuItem.setSkuGroup(checkinSkuGroup);
                stockTransfer.setCheckinDate(HKDateUtil.getNow());
                stockTransfer.setReceivedBy(loggedOnUser);
                stockTransfer.setStockTransferStatus(EnumStockTransferStatus.Stock_Transfer_CheckIn_In_Process.getStockTransferStatus());
                stockTransferLineItem.setStockTransfer(stockTransfer);
                stockTransferLineItem.setCheckedInSkuGroup(checkinSkuGroup);
                if (stockTransferLineItem.getCheckedinQty() != null) {
                    stockTransferLineItem.setCheckedinQty(stockTransferLineItem.getCheckedinQty() + 1L);
                } else {
                    stockTransferLineItem.setCheckedinQty(1L);
                }
                baseDao.update(stockTransferLineItem);
//           adminInventoryService.inventoryCheckoutForStockTransfer(sku, skuItem, stockTransferLineItem, 1L, loggedOnUser, getInventoryService().getInventoryTxnType(EnumInvTxnType.STOCK_TRANSFER_CHECKIN), EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, null, stockTransferLineItem,EnumSkuItemStatus.Checked_IN,EnumSkuItemOwner.SELF, getInventoryService().getInventoryTxnType(EnumInvTxnType.STOCK_TRANSFER_CHECKIN), 1L, loggedOnUser);
                getInventoryService().checkInventoryHealth(sku.getProductVariant());

            } else {
                addRedirectAlertMessage(new SimpleMessage("All Sku Item has already been checked in against this stock transfer "));
                return new RedirectResolution(StockTransferAction.class, "checkinInventoryAgainstStockTransfer").addParameter("stockTransfer", stockTransfer.getId());
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("All Sku Item has already been checked in agianst sku group :" + skuGroup.getId()));
            return new RedirectResolution(StockTransferAction.class, "checkinInventoryAgainstStockTransfer").addParameter("stockTransfer", stockTransfer.getId());
        }

        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        return new RedirectResolution(StockTransferAction.class, "checkinInventoryAgainstStockTransfer").addParameter("stockTransfer", stockTransfer.getId()).addParameter("messageColor", "green");
    }


    public Resolution generateGRNExcel() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminDownloadsPath + "/grnFiles/" + sdf.format(new Date()) + "/GRN-ID-" + grn.getId() + "-" + sdf.format(new Date()) + ".xls";
        final File excelFile = new File(excelFilePath);

        getXslGenerator().generateGRNXsl(grn, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

        return new Resolution() {

            public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                OutputStream out = null;
                InputStream in = new BufferedInputStream(new FileInputStream(excelFile));
                res.setContentLength((int) excelFile.length());
                res.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getName() + "\";");
                out = res.getOutputStream();

                // Copy the contents of the file to the output stream
                byte[] buf = new byte[4096];
                int count = 0;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
            }
        };
    }

    public Resolution parse() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/poFiles/" + sdf.format(new Date()) + "/POID-" + grn.getId() + "-" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            Set<SkuGroup> skuGroupSet = getXslParser().readAndBulkCheckinInventory(grn, excelFile);
            addRedirectAlertMessage(new SimpleMessage(skuGroupSet.size() + " SkuGroups Created Successfully."));
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.GRN_CREATION}, authActionBean = AdminPermissionAction.class)
    public Resolution clearPrintBarcodeFile() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        Warehouse userWarehouse = null;
        if (getUserService().getWarehouseForLoggedInUser() != null) {
            userWarehouse = getUserService().getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        String barcodeFilePath = null;
        if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
            barcodeFilePath = barcodeGurgaon;
        } else {
            barcodeFilePath = barcodeMumbai;
        }
        barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + user.getId() + "_" + user.getName() + "_" + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
        File printBarcode = new File(barcodeFilePath);
        if (printBarcode.exists()) {
            printBarcode.delete();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no barcode file generated with your name."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        try {
            File newPrintBarcode = new File(barcodeFilePath);
            newPrintBarcode.createNewFile();
        } catch (IOException e) {
            logger.error("Exception while deleting " + barcodeFilePath, e);
        }
        addRedirectAlertMessage(new SimpleMessage("Print Barcode deleted Successfully."));
        return new RedirectResolution(AdminHomeAction.class);
    }

    @Secure(hasAnyPermissions = {PermissionConstants.GRN_CREATION}, authActionBean = AdminPermissionAction.class)
    public Resolution downloadPrintBarcodeFile() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        Warehouse userWarehouse = null;
        if (getUserService().getWarehouseForLoggedInUser() != null) {
            userWarehouse = getUserService().getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        String barcodeFilePath = null;
        if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
            barcodeFilePath = barcodeGurgaon;
        } else {
            barcodeFilePath = barcodeMumbai;
        }
        barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + user.getId() + "_" + user.getName() + "_" + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
        printBarcode = new File(barcodeFilePath);
        if (!printBarcode.exists()) {
            addRedirectAlertMessage(new SimpleMessage("There is no barcode file generated with your name."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        addRedirectAlertMessage(new SimpleMessage("Print Barcodes downloaded Successfully."));
        return new HTTPResponseResolution();
    }


    public Resolution downloadBarcode() {
        List<SkuItem> checkedInSkuItems = adminInventoryService.getCheckedInOrOutSkuItems(null, null, grnLineItem, null, 1L);
        if (checkedInSkuItems == null || checkedInSkuItems.size() < 1) {
            addRedirectAlertMessage(new SimpleMessage(" Please do checkin some items for Downlaoding Barcode "));
            return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
        }

        ProductVariant productVariant = grnLineItem.getSku().getProductVariant();
//        SkuGroup skuGroup = checkedInSkuItems.get(0).getSkuGroup();
        Map<Long, String> skuItemDataMap = adminInventoryService.skuItemBarcodeMap(checkedInSkuItems);

        String barcodeFilePath = null;
        Warehouse userWarehouse = null;
        if (getUserService().getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(InventoryCheckinAction.class);
        }
        if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
            barcodeFilePath = barcodeGurgaon;
        } else {
            barcodeFilePath = barcodeMumbai;
        }
        barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + "grn_" + grn.getId() + "_" + productVariant.getId() + "_"
                + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";

        try {
            printBarcode = BarcodeUtil.createBarcodeFileForSkuItem(barcodeFilePath, skuItemDataMap);
        } catch (IOException e) {
            logger.error("Exception while appending on barcode file", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Print Barcodes downloaded Successfully."));
        return new HTTPResponseResolution();

    }

    public Resolution downloadAllBarcode() {
        String barcodeFilePath = null;
        Map<Long, String> skuItemDataMap = new HashMap<Long, String>();
        List<GrnLineItem> grnLineItems = grn.getGrnLineItems();

        for (GrnLineItem grnLineItem : grnLineItems) {
            List<SkuItem> checkedInSkuItems = adminInventoryService.getCheckedInOrOutSkuItems(null, null, grnLineItem, null, 1L);
            if (checkedInSkuItems != null && checkedInSkuItems.size() > 0) {
//                SkuGroup skuGroup = checkedInSkuItems.get(0).getSkuGroup();
                Map<Long, String> skuItemBarcodeMap = adminInventoryService.skuItemBarcodeMap(checkedInSkuItems);
                skuItemDataMap.putAll(skuItemBarcodeMap);
                Warehouse userWarehouse = null;
                if (getUserService().getWarehouseForLoggedInUser() != null) {
                    userWarehouse = userService.getWarehouseForLoggedInUser();
                } else {
                    addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
                    return new RedirectResolution(InventoryCheckinAction.class);
                }
                if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
                    barcodeFilePath = barcodeGurgaon;
                } else {
                    barcodeFilePath = barcodeMumbai;
                }
                barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + "grn_" + grn.getId() + "_All_"
                        + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
            }
        }
        try {
            if (skuItemDataMap == null || skuItemDataMap.size() < 1) {
                addRedirectAlertMessage(new SimpleMessage(" Please do checkin some items for Downlaoding Barcode "));
                return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
            }
            printBarcode = BarcodeUtil.createBarcodeFileForSkuItem(barcodeFilePath, skuItemDataMap);
        } catch (IOException e) {
            logger.error("Exception while appending on barcode file", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Print Barcode downloaded Successfully."));
        return new HTTPResponseResolution();
    }

	public Resolution freezeCheckin() {
		Set<ShippingOrder> shippingOrders = adminInventoryService.manuallyEscalateShippingOrdersForThisCheckin(grn);
		if(shippingOrders!=null&&shippingOrders.size()>0){
			String escalated = "";
			for (ShippingOrder shippingOrder : shippingOrders) {
				escalated +=shippingOrder.getId().toString()+", ";
			}
			addRedirectAlertMessage(new SimpleMessage("Shipping Order(s) - "+escalated+" were escalated."));
		}
		addRedirectAlertMessage(new SimpleMessage("No Shipping Orders were escalated"));
    return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
	}

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            InputStream in = new BufferedInputStream(new FileInputStream(printBarcode));
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            res.setContentLength((int) printBarcode.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + printBarcode.getName() + "\";");
            OutputStream out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
        }

    }
    
    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Date getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(Date mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public GoodsReceivedNote getGrn() {
        return grn;
    }

    public void setGrn(GoodsReceivedNote grn) {
        this.grn = grn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public GrnLineItemDao getGrnLineItemDao() {
        return grnLineItemDao;
    }

    public void setGrnLineItemDao(GrnLineItemDao grnLineItemDao) {
        this.grnLineItemDao = grnLineItemDao;
    }

    public GoodsReceivedNoteDao getGoodsReceivedNoteDao() {
        return goodsReceivedNoteDao;
    }

    public void setGoodsReceivedNoteDao(GoodsReceivedNoteDao goodsReceivedNoteDao) {
        this.goodsReceivedNoteDao = goodsReceivedNoteDao;
    }

    public XslGenerator getXslGenerator() {
        return xslGenerator;
    }

    public void setXslGenerator(XslGenerator xslGenerator) {
        this.xslGenerator = xslGenerator;
    }

    public XslParser getXslParser() {
        return xslParser;
    }

    public void setXslParser(XslParser xslParser) {
        this.xslParser = xslParser;
    }

    public AdminInventoryService getAdminInventoryService() {
        return adminInventoryService;
    }

    public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
        this.adminInventoryService = adminInventoryService;
    }

    public StockTransfer getStockTransfer() {
        return stockTransfer;
    }

    public void setStockTransfer(StockTransfer stockTransfer) {
        this.stockTransfer = stockTransfer;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getProductVariantBarcode() {
        return productVariantBarcode;
    }

    public void setProductVariantBarcode(String productVariantBarcode) {
        this.productVariantBarcode = productVariantBarcode;
    }

  public String getInvBarcode() {
    return invBarcode;
  }

  public void setInvBarcode(String invBarcode) {
    this.invBarcode = invBarcode;
  }

  public GrnLineItem getGrnLineItem() {
        return grnLineItem;
    }

    public void setGrnLineItem(GrnLineItem grnLineItem) {
        this.grnLineItem = grnLineItem;
    }

    public AdminEmailManager getAdminEmailManager() {
        return adminEmailManager;
    }

    public void setAdminEmailManager(AdminEmailManager adminEmailManager) {
        this.adminEmailManager = adminEmailManager;
    }

    public ExtraInventoryService getExtraInventoryService() {
        return extraInventoryService;
    }

    public void setExtraInventoryService(ExtraInventoryService extraInventoryService) {
        this.extraInventoryService = extraInventoryService;
    }

	public PoLineItemService getPoLineItemService() {
		return poLineItemService;
	}

	public void setPoLineItemService(PoLineItemService poLineItemService) {
		this.poLineItemService = poLineItemService;
	}

	public PurchaseOrderService getPurchaseOrderService() {
		return purchaseOrderService;
	}

	public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
		this.purchaseOrderService = purchaseOrderService;
	}

	public Boolean getIsBrightSupplier() {
		return isBrightSupplier;
	}

	public void setIsBrightSupplier(Boolean isBrightSupplier) {
		this.isBrightSupplier = isBrightSupplier;
	}

}