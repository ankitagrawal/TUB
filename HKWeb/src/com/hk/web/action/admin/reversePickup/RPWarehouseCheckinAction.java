package com.hk.web.action.admin.reversePickup;//package com.hk.web.action.admin.reversePickup;
//
//import com.akube.framework.stripes.action.BaseAction;
//
//import com.hk.admin.pact.service.inventory.AdminInventoryService;
//import com.hk.admin.util.BarcodeUtil;
//import com.hk.constants.core.Keys;
//import com.hk.constants.courier.StateList;
//import com.hk.constants.reversePickup.EnumCourierConstant;
//import com.hk.constants.reversePickup.EnumReversePickupStatus;
//import com.hk.constants.sku.EnumSkuItemStatus;
//
//import com.hk.domain.reversePickupOrder.ReversePickupOrder;
//import com.hk.domain.reversePickupOrder.RpLineItem;
//import com.hk.domain.shippingOrder.LineItem;
//import com.hk.domain.sku.SkuItem;
//import com.hk.domain.warehouse.Warehouse;
//import com.hk.pact.service.UserService;
//import com.hk.pact.service.inventory.SkuGroupService;
//import com.hk.pact.service.reversePickup.ReversePickupService;
//import com.hk.web.HealthkartResponse;
//import net.sourceforge.stripes.action.*;
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///*User: Seema
//* Date: 7/27/13
//*/
//public class RPWarehouseCheckinAction extends BaseAction {
//    private static Logger logger = Logger.getLogger(RPWarehouseCheckinAction.class);
//    private String reversePickupId;
//    private String errorMessage = null;
//    private ReversePickupOrder reversePickupOrder;
//    private List<RpLineItem> rpLineItems;
//    private String scannedBarcode;
//    private Boolean showSearchBox = false;
//    private Boolean alreadyCheckedIn = false;
//    private List<String> scannedBarcodeList = new ArrayList<String>();
//    private LineItem lineItem;
//    File barcodeFile;
//    @Value("#{hkEnvProps['" + Keys.Env.barcodeGurgaon + "']}")
//    String barcodeGurgaon;
//    @Value("#{hkEnvProps['" + Keys.Env.barcodeMumbai + "']}")
//    String barcodeMumbai;
//    @Autowired
//    private ReversePickupService reversePickupService;
//    @Autowired
//    AdminInventoryService adminInventoryService;
//    @Autowired
//    SkuGroupService skuGroupService;
//
//    @Autowired
//    UserService userService;
//
//    @DefaultHandler
//    public Resolution pre() {
//        showSearchBox = true;
//        return new ForwardResolution("/pages/admin/reversePickup/reversePickupWarehouseCheckin.jsp");
//    }
//
//
//    public Resolution search() {
//        reversePickupOrder = reversePickupService.getByReversePickupId(reversePickupId);
//        if (reversePickupOrder == null) {
//            showSearchBox = true;
//            errorMessage = "Invalid Reverse Pickup Id";
//        } else {
//            if (reversePickupOrder.getReversePickupStatus().getId().equals(EnumReversePickupStatus.RPU_QC_Checked_In.getId()) ||
//                    reversePickupOrder.getReversePickupStatus().getId().equals(EnumReversePickupStatus.Return_QC_Checkin.getId())) {
//                alreadyCheckedIn = true;
//                errorMessage = "Reverse Pickup is already checkedIn ";
//                reversePickupOrder = null;
//            }
//        }
//        return new ForwardResolution("/pages/admin/reversePickup/reversePickupWarehouseCheckin.jsp");
//    }
//
//
//    public Resolution warehouseCheckIn() {
//        if (rpLineItems != null) {
//            reversePickupService.checkedInRpLineItems(rpLineItems);
//            ReversePickupOrder reversePickupOrderFromDb = reversePickupService.getReversePickupOrderById(reversePickupOrder.getId());
//            if (reversePickupOrder.getCourierManagedBy().equals(EnumCourierConstant.HealthKart_Managed.getId())) {
//                reversePickupOrderFromDb.setReversePickupStatus(EnumReversePickupStatus.RPU_QC_Checked_In.asReversePickupStatus());
//            } else {
//                reversePickupOrderFromDb.setReversePickupStatus(EnumReversePickupStatus.Return_QC_Checkin.asReversePickupStatus());
//            }
//            reversePickupService.saveReversePickupOrder(reversePickupOrderFromDb);
//            /** delete rplineItems which are not checked in**/
//
//        }
//        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId());
//    }
//
//    public Resolution verifyScannedBarcode() {
//        String errorMsg = "";
//        boolean error = false;
//        HealthkartResponse healthkartResponse = null;
//        if (scannedBarcode != null) {
//            Warehouse warehouse = userService.getWarehouseForLoggedInUser();
//            Map dataMap = new HashMap();
//            try {
//                SkuItem skuItem = skuGroupService.getSkuItemByBarcode(scannedBarcode, warehouse.getId(), null);
//                if (skuItem != null) {
//                    if (skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.Checked_IN.getId())) {
//                        error = true;
//                        String productName = skuItem.getSkuGroup().getSku().getProductVariant().getProduct().getName();
//                        errorMsg = "Invalid barcode : Inventory for product :::  " + productName + " ::: is in Checked Status";
//                    } else {
//                        Long checkout = -1l;
//                        LineItem lineItem = adminInventoryService.getCheckedOutLineItem(skuItem, checkout);
//                        if (lineItem != null) {
//                            boolean validLineItem = isRPLineItemsListContainsLineItem(reversePickupOrder.getRpLineItems(), lineItem);
//                            if (validLineItem) {
//                                dataMap.put("lineItemId", lineItem.getId());
//                                dataMap.put("scannedBarcode", scannedBarcode);
//                                healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", dataMap);
//                            } else {
//                                error = true;
//                                errorMsg = "Invalid barcode : Reverse pickup does not created for :::  " + scannedBarcode + " ::: Barcode";
//                            }
//                        } else {
//                            error = true;
//                            errorMsg = "Invalid barcode : Inventory is never checked out with  " + scannedBarcode + "  Barcode";
//                        }
//                    }
//                } else {
//                    error = true;
//                    errorMsg = "Invalid Barcode : No Such Barcode Found in the System";
//                }
//            } catch (Exception e) {
//                healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getLocalizedMessage(), "");
//                return new JsonResolution(healthkartResponse);
//            }
//            if (error) {
//                healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, errorMsg, "");
//            }
//        } else {
//            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Empty Barcode", "");
//        }
//        return new JsonResolution(healthkartResponse);
//    }
//
//
//    public Resolution downloadBarcode() {
//        if (lineItem != null) {
//            List<SkuItem> skuItemList = adminInventoryService.getCheckedOutSkuItems(lineItem, scannedBarcodeList, -1l);
//            List<SkuItem> skuItemListForBarcode = new ArrayList<SkuItem>();
//            if (skuItemList != null && skuItemList.size() > 0) {
//                skuItemListForBarcode.add(skuItemList.get(0));
//            }
//            Map<Long, String> skuItemDataMap = adminInventoryService.skuItemBarcodeMap(skuItemListForBarcode);
//            String barcodeFilePath = null;
//            Warehouse userWarehouse = userService.getWarehouseForLoggedInUser();
//            if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
//                barcodeFilePath = barcodeGurgaon;
//            } else {
//                barcodeFilePath = barcodeMumbai;
//            }
//            barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + "RP_" + lineItem.getSku().getProductVariant().getProduct().getName() +  ".txt";
//            try {
//                barcodeFile = BarcodeUtil.createBarcodeFileForSkuItem(barcodeFilePath, skuItemDataMap);
//            } catch (IOException e) {
//                logger.error("Exception while appending on barcode file", e);
//            }
//            addRedirectAlertMessage(new SimpleMessage("Print Barcodes downloaded Successfully."));
//        }
//        return new HTTPResponseResolution();
//    }
//
////    private List<Long> getListOfLineItems(List<RpLineItem> rpLineItemList) {
////        List<Long> lineItemIdsList = new ArrayList<Long>();
////        for (RpLineItem rpLineItem : rpLineItemList) {
////
////            if (!(lineItemIdsList.contains(rpLineItem.getLineItem().getId()))) {
////                lineItemIdsList.add(rpLineItem.getLineItem().getId());
////            }
////        }
////        return lineItemIdsList;
////    }
//
//    class HTTPResponseResolution implements Resolution {
//        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//            InputStream in = new BufferedInputStream(new FileInputStream(barcodeFile));
//            res.setContentType("text/plain");
//            res.setCharacterEncoding("UTF-8");
//            res.setContentLength((int) barcodeFile.length());
//            res.setHeader("Content-Disposition", "attachment; filename=\"" + barcodeFile.getName() + "\";");
//            OutputStream out = res.getOutputStream();
//
//            // Copy the contents of the file to the output stream
//            byte[] buf = new byte[4096];
//            int count = 0;
//            while ((count = in.read(buf)) >= 0) {
//                out.write(buf, 0, count);
//            }
//            in.close();
//            out.flush();
//            out.close();
//        }
//
//    }
//
//
//    private boolean isRPLineItemsListContainsLineItem(List<RpLineItem> rpLineItemList, LineItem lineItem) {
//        if (rpLineItemList != null) {
//            for (RpLineItem rpLineItem : rpLineItemList) {
//                if (rpLineItem.getLineItem().getId().equals(lineItem.getId())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public String getReversePickupId() {
//        return reversePickupId;
//    }
//
//    public void setReversePickupId(String reversePickupId) {
//        this.reversePickupId = reversePickupId;
//    }
//
//    public String getErrorMessage() {
//        return errorMessage;
//    }
//
//    public void setErrorMessage(String errorMessage) {
//        this.errorMessage = errorMessage;
//    }
//
//    public ReversePickupOrder getReversePickupOrder() {
//        return reversePickupOrder;
//    }
//
//    public void setReversePickupOrder(ReversePickupOrder reversePickupOrder) {
//        this.reversePickupOrder = reversePickupOrder;
//    }
//
//    public List<RpLineItem> getRpLineItems() {
//        return rpLineItems;
//    }
//
//    public void setRpLineItems(List<RpLineItem> rpLineItems) {
//        this.rpLineItems = rpLineItems;
//    }
//
//    public String getScannedBarcode() {
//        return scannedBarcode;
//    }
//
//    public void setScannedBarcode(String scannedBarcode) {
//        this.scannedBarcode = scannedBarcode;
//    }
//
//    public Boolean getShowSearchBox() {
//        return showSearchBox;
//    }
//
//    public void setShowSearchBox(Boolean showSearchBox) {
//        this.showSearchBox = showSearchBox;
//    }
//
//    public Boolean getAlreadyCheckedIn() {
//        return alreadyCheckedIn;
//    }
//
//    public void setAlreadyCheckedIn(Boolean alreadyCheckedIn) {
//        this.alreadyCheckedIn = alreadyCheckedIn;
//    }
//
//    public List<String> getScannedBarcodeList() {
//        return scannedBarcodeList;
//    }
//
//    public void setScannedBarcodeList(List<String> scannedBarcodeList) {
//        this.scannedBarcodeList = scannedBarcodeList;
//    }
//
//    public LineItem getLineItem() {
//        return lineItem;
//    }
//
//    public void setLineItem(LineItem lineItem) {
//        this.lineItem = lineItem;
//    }
//}
