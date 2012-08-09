package com.hk.web.action.admin.hkDelivery;

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
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.UserService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.admin.manager.HKDRunsheetManager;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.CourierConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Component
public class HKDRunsheetAction extends BaseAction {

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
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    AwbService awbService;
    @Autowired
    UserService userService;
    @Autowired
    ShipmentService shipmentService;
    @Autowired
    ConsignmentService consignmentService;
    @Autowired
    HKDRunsheetManager hkdRunsheetManager;
    @Autowired
    RunSheetService  runsheetService;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloads;


    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");
    }

    public Resolution downloadDeliveryWorkSheet() {
        Runsheet runsheet = new Runsheet();
        shippingOrderList = new ArrayList<ShippingOrder>();
        //Getting HK-Delivery Courier Object.
        Courier hkDeliveryCourier = EnumCourier.HK_Delivery.asCourier();

        //Iterating over list to get shippingOrders,consignmentList for the corresponding trackingIdList entered by user.
        for (String trackingNum : trackingIdList) {
            Awb awb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(hkDeliveryCourier, trackingNum.trim(), userService.getWarehouseForLoggedInUser(), null, EnumAwbStatus.Used.getAsAwbStatus());
            Consignment consignment = consignmentService.getConsignmentByAwbId(awb.getId());

            //This is a check to ensure that runsheet wud be created for those trackingIds for which Consignment exists in DB.
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
                consignmentList.add(consignment);
            } else {
                //adding the trackingId without a consignment to a list.
                trackingIdsWithoutConsignment.add(trackingNum);
                continue;
            }
        }
        // Converting list to comma seperated string(to be displayed on UI)
        awbIdsWithoutConsignmntString = hkdRunsheetManager.getAwbWithoutConsignmntString(trackingIdsWithoutConsignment);
        // Calculating no. of total packets in runsheet.
        totalPackets = shippingOrderList.size();

        try {
            sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/" + CourierConstants.HKDELIVERY_WORKSHEET_FOLDER + "/" + CourierConstants.HKDELIVERY_WORKSHEET + "_" + sdf.format(new Date()) + ".xls");
            runsheet = createRunsheet();
            runsheetService.createRunSheet(runsheet);
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
}
