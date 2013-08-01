package com.hk.web.action.admin.reversePickup;

import com.akube.framework.stripes.action.BaseAction;

import com.hk.admin.pact.service.courier.reversePickup.ReversePickupService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.util.BarcodeUtil;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.StateList;
import com.hk.constants.reversePickup.EnumCourierConstant;
import com.hk.constants.reversePickup.EnumReversePickupStatus;
import com.hk.constants.sku.EnumSkuItemStatus;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.ReversePickupException;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuGroupService;

import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/*User: Seema
* Date: 7/27/13
*/
public class RPWarehouseCheckinAction extends BaseAction {
    private static Logger logger = Logger.getLogger(RPWarehouseCheckinAction.class);
    private String reversePickupId;
    private String errorMessage = null;
    private ReversePickupOrder reversePickupOrder;
    private List<RpLineItem> rpLineItems;
    private String scannedBarcode;
    private Boolean showSearchBox = false;
    private Boolean alreadyCheckedIn = false;
    private List<String> scannedBarcodeList = new ArrayList<String>();
    private LineItem lineItem;

    File barcodeFile;
    @Value("#{hkEnvProps['" + Keys.Env.barcodeGurgaon + "']}")
    String barcodeGurgaon;
    @Value("#{hkEnvProps['" + Keys.Env.barcodeMumbai + "']}")
    String barcodeMumbai;
    @Autowired
    private ReversePickupService reversePickupService;
    @Autowired
    AdminInventoryService adminInventoryService;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    UserService userService;

    @DefaultHandler
    public Resolution pre() {
        showSearchBox = true;
        return new ForwardResolution("/pages/admin/reversePickup/reversePickupWarehouseCheckin.jsp");
    }


    public Resolution search() {
        reversePickupOrder = reversePickupService.getByReversePickupId(reversePickupId);
        if (reversePickupOrder == null) {
            showSearchBox = true;
            errorMessage = "Invalid Reverse Pickup Id";
        } else {
            if (reversePickupOrder.getReversePickupStatus().getId().equals(EnumReversePickupStatus.RPU_QC_Checked_In.getId()) ||
                    reversePickupOrder.getReversePickupStatus().getId().equals(EnumReversePickupStatus.Return_QC_Checkin.getId())) {
                alreadyCheckedIn = true;
                errorMessage = "Reverse Pickup is already checkedIn ";
                reversePickupOrder = null;
            }
        }
        return new ForwardResolution("/pages/admin/reversePickup/reversePickupWarehouseCheckin.jsp");
    }


    public Resolution checkInRPLineItem() {
        HealthkartResponse healthkartResponse = null;
        String message = "";
        if (rpLineItems != null) {
            Map dataMap = new HashMap();
            RpLineItem itemToBeSaved = null;
            for (RpLineItem rpLineItem : rpLineItems) {
                if (rpLineItem != null) {
                    itemToBeSaved = rpLineItem;
                    break;
                }
            }
            if (itemToBeSaved == null || itemToBeSaved.getItemBarcode() == null || StringUtils.isEmpty(itemToBeSaved.getItemBarcode())) {
                message = "Plz Scan Barcode";
                healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, message, dataMap);
            } else if (itemToBeSaved.getWarehouseReceivedCondition() == null) {
                message = "Plz Select Warehouse Condition Also";
                healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, message, dataMap);
            } else {
                try {
                    reversePickupService.checkInRpLineItem(itemToBeSaved);
                    message = "Item Saved";
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, message, dataMap);
                } catch (ReversePickupException rpe) {
                    String errMsg = rpe.getMessage();
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, errMsg, dataMap);
                }

            }
        }
        return new JsonResolution(healthkartResponse);
    }

    public Resolution closeWarehouseCheckIn() {
        ReversePickupOrder reversePickupOrderFromDb = reversePickupService.getByReversePickupId(reversePickupId);
        /*check at least one of rp is checkedIn*/
        boolean validCheckin = false;
        for (RpLineItem rpLineItem : reversePickupOrderFromDb.getRpLineItems()) {
            if (rpLineItem.getWarehouseReceivedCondition() != null) {
                validCheckin = true;
                break;
            }
        }
        if (!validCheckin) {
            return new RedirectResolution(RPWarehouseCheckinAction.class, "search").addParameter("reversePickupId", reversePickupOrder.getReversePickupId())
                    .addParameter("errorMessage", "Select and checkIn at least one Item");
        }
        Long pickedStatus = EnumReversePickupStatus.RPU_Picked.getId();
        Long returnInitiated = EnumReversePickupStatus.Return_Initiated.getId();
        Long currentStatus = reversePickupOrder.getReversePickupStatus().getId();
        if (currentStatus.equals(returnInitiated) || currentStatus.equals(pickedStatus)) {
            if (currentStatus.equals(returnInitiated)) {
                reversePickupOrderFromDb.setReversePickupStatus(EnumReversePickupStatus.Return_QC_Checkin.asReversePickupStatus());
            } else {
                reversePickupOrderFromDb.setReversePickupStatus(EnumReversePickupStatus.RPU_QC_Checked_In.asReversePickupStatus());
            }

        } else {
            return new RedirectResolution(RPWarehouseCheckinAction.class).addParameter("reversePickupId", reversePickupOrder.getReversePickupId())
                    .addParameter("errorMessage", "Reverse Order is not in RPU_Picked or Return_Initiated status");
        }

        reversePickupService.saveReversePickupOrder(reversePickupOrderFromDb);
        return new RedirectResolution(ReversePickupListAction.class).addParameter("reversePickupId", reversePickupOrder.getReversePickupId());
    }

    public Resolution downloadBarcode() {
        Warehouse userWarehouse = null;
        if (getUserService().getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(RPWarehouseCheckinAction.class).addParameter("reversePickupId", reversePickupOrder.getReversePickupId());
        }
        ProductVariant productVariant = lineItem.getSku().getProductVariant();
        List<SkuItem> checkedOutSkuItems = adminInventoryService.getCheckedInOrOutSkuItems(null, null, null, lineItem, -1L);
        List<SkuItem> checkedInSkuItems = adminInventoryService.getCheckedInOrOutSkuItems(null, null, null, lineItem, 1L);
        checkedOutSkuItems.removeAll(checkedInSkuItems);
        List<SkuItem> singleBarcodeList = new ArrayList<SkuItem>();
        if (checkedOutSkuItems.size() > 0) {
            for (SkuItem skuItem : checkedOutSkuItems) {
                if (skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.Checked_OUT.getId())) {
                    singleBarcodeList.add(skuItem);
                    break;
                }
            }
            if (singleBarcodeList.size() > 0) {
                Map<Long, String> skuItemDataMap = adminInventoryService.skuItemBarcodeMap(singleBarcodeList);
                String barcodeFilePath = null;
                if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
                    barcodeFilePath = barcodeGurgaon;
                } else {
                    barcodeFilePath = barcodeMumbai;
                }
                barcodeFilePath = barcodeFilePath + "/" + "print_" + "RP" + productVariant + "_" + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
                try {
                    barcodeFile = BarcodeUtil.createBarcodeFileForSkuItem(barcodeFilePath, skuItemDataMap);
                } catch (IOException e) {
                    logger.error("Exception while appending on barcode file", e);
                }
                addRedirectAlertMessage(new SimpleMessage("Print Barcode downloaded Successfully."));
                return new HTTPResponseResolution();
            } else {
                return new RedirectResolution(RPWarehouseCheckinAction.class).addParameter("reversePickupId", reversePickupId)
                        .addParameter("errorMessage", "The line item is in wrong state , it has no barcode to delete");
            }
        } else {
            return new RedirectResolution(RPWarehouseCheckinAction.class).addParameter("reversePickupId", reversePickupId)
                    .addParameter("errorMessage", "The line item is in wrong state , it has no barcode to delete");
        }
    }


    class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            InputStream in = new BufferedInputStream(new FileInputStream(barcodeFile));
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            res.setContentLength((int) barcodeFile.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + barcodeFile.getName() + "\";");
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


    private boolean isRPLineItemsListContainsLineItem(List<RpLineItem> rpLineItemList, LineItem lineItem) {
        if (rpLineItemList != null) {
            for (RpLineItem rpLineItem : rpLineItemList) {
                if (rpLineItem.getLineItem().getId().equals(lineItem.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getReversePickupId() {
        return reversePickupId;
    }

    public void setReversePickupId(String reversePickupId) {
        this.reversePickupId = reversePickupId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ReversePickupOrder getReversePickupOrder() {
        return reversePickupOrder;
    }

    public void setReversePickupOrder(ReversePickupOrder reversePickupOrder) {
        this.reversePickupOrder = reversePickupOrder;
    }

    public List<RpLineItem> getRpLineItems() {
        return rpLineItems;
    }

    public void setRpLineItems(List<RpLineItem> rpLineItems) {
        this.rpLineItems = rpLineItems;
    }

    public String getScannedBarcode() {
        return scannedBarcode;
    }

    public void setScannedBarcode(String scannedBarcode) {
        this.scannedBarcode = scannedBarcode;
    }

    public Boolean getShowSearchBox() {
        return showSearchBox;
    }

    public void setShowSearchBox(Boolean showSearchBox) {
        this.showSearchBox = showSearchBox;
    }

    public Boolean getAlreadyCheckedIn() {
        return alreadyCheckedIn;
    }

    public void setAlreadyCheckedIn(Boolean alreadyCheckedIn) {
        this.alreadyCheckedIn = alreadyCheckedIn;
    }

    public List<String> getScannedBarcodeList() {
        return scannedBarcodeList;
    }

    public void setScannedBarcodeList(List<String> scannedBarcodeList) {
        this.scannedBarcodeList = scannedBarcodeList;
    }

    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
    }

}
