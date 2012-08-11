package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.user.User;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.UserService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.manager.HKDRunsheetManager;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class HKDRunsheetAction extends BasePaginatedAction {

    private         File                  xlsFile;
    private         String                assignedTo;
    private         String                awbIdsWithoutConsignmntString      = null;
    private         List<String>          trackingIdList                     = new ArrayList<String>();
    private         Hub                   hub;
    private         List<Runsheet>        runsheetList                       = new ArrayList<Runsheet>();
    private         Boolean               runsheetDownloadFunctionality;
    private         Runsheet              runsheet;
    private         Page                  runsheetPage;
    //search filters for runsheet list

    private         Date                  startDate;
    private         Date                  endDate;
    private         RunsheetStatus        runsheetStatus;
    private         User                  agent;
    private         Integer               defaultPerPage                     = 20;
    @Autowired
    ShippingOrderService                  shippingOrderService;
    @Autowired
    AwbService                            awbService;
    @Autowired
    UserService                           userService;
    @Autowired
    ShipmentService                       shipmentService;
    @Autowired
    ConsignmentService                    consignmentService;
    @Autowired
    HKDRunsheetManager                    hkdRunsheetManager;
    @Autowired
    RunSheetService                       runsheetService;
    @Autowired
    HubService                            hubService;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloads;


    @DefaultHandler
    public Resolution pre() {
        runsheetPage = runsheetService.searchRunsheet(runsheet, startDate, endDate, runsheetStatus, agent, hub, getPageNo(), getPerPage());
        runsheetList = runsheetPage.getList();
        return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");
    }

    // Method to create and download runsheet.It also makes an entry in consignment-tracking.
    public Resolution downloadDeliveryWorkSheet() {
        
        //checking url-parameter to see if only jsp has to be displayed or runsheet has to be created.
        if (!runsheetDownloadFunctionality) {

            return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");

        } else {
            Runsheet              runsheetObj                     = null;
            Long                  prePaidBoxCount                 = null;
            User                  loggedOnUser                    = null;
            Hub                   deliveryHub                     = hubService.findHubByName(HKDeliveryConstants.DELIVERY_HUB);
            SimpleDateFormat      sdf                             = new SimpleDateFormat("yyyyMMdd");
            List<ShippingOrder>   shippingOrderList               = new ArrayList<ShippingOrder>();
            int                   totalPackets                    = 0;
            int                   totalCODPackets                 = 0;
            double                totalCODAmount                  = 0.0;
            List<String>          trackingIdsWithoutConsignment   = new ArrayList<String>();
            List<Consignment>     consignmentList                 = new ArrayList<Consignment>();
            //todo fetch userId from agent.
            Long                  userId                          = 1l;
            //Getting HK-Delivery Courier Object.
            Courier               hkDeliveryCourier               = EnumCourier.HK_Delivery.asCourier();

            //Iterating over trackingIdList(entered by the user) to get coreesponding shippingOrderList,consignmentList.
            for (String trackingNum : trackingIdList) {

                //fetching awb object from trackingId (or awbNumber).
                Awb awb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(hkDeliveryCourier, trackingNum.trim(), userService.getWarehouseForLoggedInUser(), null, EnumAwbStatus.Used.getAsAwbStatus());
                // Fetching consignment from Awb.
                Consignment consignment = consignmentService.getConsignmentByAwbId(awb.getId());
                //Getting loggedIn user,needed for consignment-tracking
                if (getPrincipal() != null) {
                    loggedOnUser = getUserService().getUserById(getPrincipal().getId());
                }
                //This is a check to ensure that runsheetObj wud be created for those trackingIds for which Consignment exists in DB.
                if (consignment != null) {
                    //Fetching shippingOrder for corresponding trackingID in order to calculate totalCodAmount and totalCodPackets
                    Shipment shipment = shipmentService.findByAwb(awb);
                    if (shipment != null) {
                        ShippingOrder shippingOrder = shipment.getShippingOrder();
                        if (shippingOrder != null) {
                            if (shippingOrder.isCOD()) {
                                ++totalCODPackets;
                                totalCODAmount = totalCODAmount + shippingOrder.getAmount();
                                totalCODAmount = Math.round(totalCODAmount);
                            }
                            shippingOrderList.add(shippingOrder);
                        }
                    }

                    //Changing consignment-status from ShipmntRcvdAtHub(10) to ShipmntOutForDelivry(20).
                    consignment.setConsignmentStatus(EnumConsignmentStatus.ShipmntOutForDelivry.asConsignmentStatus());
                    consignmentList.add(consignment);
                } else {
                    //adding the trackingId without a consignment to a list.
                    trackingIdsWithoutConsignment.add(trackingNum);
                    continue;
                }
            }
            // Converting list to comma seperated string(to be displayed on UI)
            awbIdsWithoutConsignmntString = hkdRunsheetManager.getAwbWithoutConsignmntString(trackingIdsWithoutConsignment);
            // Calculating no. of total packets,no of prepaid boxes in runsheetObj.
            totalPackets = shippingOrderList.size();
            prePaidBoxCount = Long.parseLong((totalPackets - totalCODPackets) + "");

            try {
                xlsFile = new File(adminDownloads + "/" + CourierConstants.HKDELIVERY_WORKSHEET_FOLDER + "/" + CourierConstants.HKDELIVERY_WORKSHEET + "_" + sdf.format(new Date()) + ".xls");
                // Creating Runsheet object.
                runsheetObj = runsheetService.createRunsheet(hub, consignmentList, EnumRunsheetStatus.Open.asRunsheetStatus(), userId, prePaidBoxCount, Long.parseLong(totalCODPackets + ""), totalCODAmount);
                // Saving Runsheet in db.
                runsheetService.saveRunSheet(runsheetObj);
                //making corresponding entry in consignment tracking.
                consignmentService.updateConsignmentTracking(hub.getId(),deliveryHub.getId(),loggedOnUser.getId(),consignmentList);
                // generating Xls file.
                xlsFile = hkdRunsheetManager.generateWorkSheetXls(xlsFile.getPath(), shippingOrderList, assignedTo, totalCODAmount, totalPackets, totalCODPackets);
            } catch (IOException ioe) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_IOEXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD,false);
            } catch (NullPointerException npe) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_NULLEXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD,false);
            } catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_EXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD,false);
            }
            return new HTTPResponseResolution();
        }
    }

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            OutputStream out = null;
            InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
            res.setContentLength((int) xlsFile.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
            out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
        }

    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<String> getTrackingIdList() {
        return trackingIdList;
    }

    public void setTrackingIdList(List<String> trackingIdList) {
        this.trackingIdList = trackingIdList;
    }

    public String getAwbIdsWithoutConsignmntString() {
        return awbIdsWithoutConsignmntString;
    }

    public void setAwbIdsWithoutConsignmntString(String awbIdsWithoutConsignmntString) {
        this.awbIdsWithoutConsignmntString = awbIdsWithoutConsignmntString;
    }

    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }
    public List<Runsheet> getRunsheetList() {
        return runsheetList;
    }

    public void setRunsheetList(List<Runsheet> runsheetList) {
        this.runsheetList = runsheetList;
    }

    public Boolean isRunsheetDownloadFunctionality() {
        return runsheetDownloadFunctionality;
    }

    public void setRunsheetDownloadFunctionality(Boolean runsheetDownloadFunctionality) {
        this.runsheetDownloadFunctionality = runsheetDownloadFunctionality;
    }
    
    public int getPerPageDefault() {
        return defaultPerPage; // To change body of implemented methods use File | Settings | File Templates.
    }

    public int getPageCount() {
        return runsheetPage == null ? 0 : runsheetPage.getTotalPages();
    }

    public int getResultCount() {
        return runsheetPage == null ? 0 : runsheetPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("runsheet");
        params.add("agent");
        params.add("hub");
        params.add("runsheetStatus");
        params.add("startDate");
        params.add("endDate");        
        return params;
    }
}
