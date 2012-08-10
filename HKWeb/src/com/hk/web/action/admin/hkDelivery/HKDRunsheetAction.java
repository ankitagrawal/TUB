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
import com.akube.framework.stripes.action.BaseAction;
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
import com.hk.admin.manager.HKDRunsheetManager;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class HKDRunsheetAction extends BasePaginatedAction {

    private         File                  xlsFile;
    private         String                assignedTo;
    private         SimpleDateFormat      sdf;
    private         List<ShippingOrder>   shippingOrderList;
    private         int                   totalPackets;
    private         int                   totalCODPackets                 = 0;
    private         double                totalCODAmount                  = 0.0;
    private         String                awbIdsWithoutConsignmntString      = null;

    private         List<String>          trackingIdList                  = new ArrayList<String>();
    private         Hub                   hub;
    private         List<String>          trackingIdsWithoutConsignment   = new ArrayList<String>();
    private         List<Consignment>     consignmentList                 = new ArrayList<Consignment>();
    private         List<Runsheet>        runsheetList                    = new ArrayList<Runsheet>();
    private         Boolean               runsheetDownloadFunctionality;
    private         Runsheet              runsheet;
    private         Page                  runsheetPage;
    //search filters for runsheet list

    private         Date                  startDate;
    private         Date                  endDate;
    private         RunsheetStatus        runsheetStatus;
    private         User                  agent;
    private         Integer               defaultPerPage            = 20;

    private
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


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloads;


    @DefaultHandler
    public Resolution pre() {
        runsheetPage = runsheetService.searchRunsheet(runsheet, startDate, endDate, runsheetStatus, agent, hub, getPageNo(), getPerPage());
        runsheetList = runsheetPage.getList();
        return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");
    }

    public Resolution downloadDeliveryWorkSheet() {

        if(!runsheetDownloadFunctionality){
           return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");
        } else {
        Runsheet runsheetObj;
        shippingOrderList = new ArrayList<ShippingOrder>();
        //Getting HK-Delivery Courier Object.
        Courier hkDeliveryCourier = EnumCourier.HK_Delivery.asCourier();

        //Iterating over list to get shippingOrders,consignmentList for the corresponding trackingIdList entered by user.
        for (String trackingNum : trackingIdList) {
            Awb awb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(hkDeliveryCourier, trackingNum.trim(), userService.getWarehouseForLoggedInUser(), null, EnumAwbStatus.Used.getAsAwbStatus());
            Consignment consignment = consignmentService.getConsignmentByAwbId(awb.getId());

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
        // Calculating no. of total packets in runsheetObj.
        totalPackets = shippingOrderList.size();

        try {
            sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/" + CourierConstants.HKDELIVERY_WORKSHEET_FOLDER + "/" + CourierConstants.HKDELIVERY_WORKSHEET + "_" + sdf.format(new Date()) + ".xls");
            runsheetObj = createRunsheet();
            runsheetService.createRunSheet(runsheetObj);
            xlsFile = hkdRunsheetManager.generateWorkSheetXls(xlsFile.getPath(), shippingOrderList, assignedTo, totalCODAmount, totalPackets, totalCODPackets);
        } catch (IOException ioe) {
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_IOEXCEPTION));
            return new ForwardResolution(HKDRunsheetAction.class);
        } catch (NullPointerException npe) {
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_NULLEXCEPTION));
            return new ForwardResolution(HKDRunsheetAction.class);
        } catch (Exception ex) {
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_EXCEPTION));
            return new ForwardResolution(HKDRunsheetAction.class);
        }
        return new HTTPResponseResolution();
        }
    }

    private Runsheet createRunsheet(){
        Long prePaidBoxCount = Long.parseLong((totalPackets - totalCODPackets)+"");
        Runsheet runsheetObj = new Runsheet();
        runsheetObj.setCodBoxCount(Long.parseLong(totalCODPackets+""));
        runsheetObj.setCreateDate(new Date());
        runsheetObj.setExpectedCollection(totalCODAmount);
        runsheetObj.setPrePaidBoxCount(prePaidBoxCount);
        runsheetObj.setUserId(1l);
        runsheetObj.setHub(hub);
        runsheetObj.setConsignments(consignmentList);
        runsheetObj.setRunsheetStatus(EnumRunsheetStatus.Open.asRunsheetStatus());
       return runsheetObj;
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
