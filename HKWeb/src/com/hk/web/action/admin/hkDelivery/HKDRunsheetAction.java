package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.UserService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.manager.HKDRunsheetManager;
import com.hk.admin.util.HKDeliveryUtil;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;
import com.hk.constants.payment.EnumPaymentMode;

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
    private         SimpleDateFormat      sdf                                = new SimpleDateFormat("yyyyMMdd");
    private         Runsheet              runsheet;
    private         Page                  runsheetPage;
    //search filters for runsheet list

    private         Date                  startDate;
    private         Date                  endDate;
    private         RunsheetStatus        runsheetStatus;
    private         User                  agent;
    private         Integer               defaultPerPage                     = 20;
    private         Set<Consignment>      consignments;
    private         List<Consignment>     runsheetConsignments;
    private         List<Long>            changedConsignmentIdsList        = new ArrayList<Long>();
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
    @Autowired
    RunSheetDao                           runSheetDao;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloads;


    @DefaultHandler
    public Resolution pre() {
        runsheetPage = runsheetService.searchRunsheet(runsheet, startDate, endDate, runsheetStatus, agent, hub, getPageNo(), getPerPage());
        runsheetList = runsheetPage.getList();
        return new ForwardResolution("/pages/admin/hkRunsheetList.jsp");
    }

    public Resolution editRunsheet(){
        if(runsheet == null){
            return new ForwardResolution("/pages/admin/hkRunsheetList.jsp");
        }
        runsheetConsignments = new ArrayList<Consignment>(runsheet.getConsignments());
        return new ForwardResolution("/pages/admin/hkRunsheet.jsp");
    }

    public Resolution saveRunsheet(){        
        if(runsheet != null){
            consignments = new HashSet<Consignment>(runsheetConsignments);
            runsheet.setConsignments(consignments);
            if(runsheet.getRunsheetStatus().getId().equals(EnumRunsheetStatus.Close.getId()) && runsheetService.isRunsheetClosable(runsheet) == false){
                addRedirectAlertMessage(new SimpleMessage("Cannot close runsheet with a consignment status out for delivery"));
                return new ForwardResolution(HKDRunsheetAction.class,"editRunsheet").addParameter("runsheet", runsheet.getId());
            }

            runsheetService.saveRunSheet(runsheet);
            addRedirectAlertMessage(new SimpleMessage("Runsheet saved"));
            return new RedirectResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
        }
        else{
            addRedirectAlertMessage(new SimpleMessage("Runsheet not found."));
        }
        return new RedirectResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
    }

    public Resolution markAllDelivered(){
        if(runsheet != null){
            runsheetService.markAllConsignmentsAsDelivered(runsheet);
            runsheetService.saveRunSheet(runsheet);
        }
        return new RedirectResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
    }

    public Resolution closeRunsheet(){
        if(runsheet != null){
            if(runsheetService.isRunsheetClosable(runsheet)){
                runsheet.setRunsheetStatus(getRunSheetDao().get(RunsheetStatus.class, EnumRunsheetStatus.Close.getId()));
            }
            else{
                addRedirectAlertMessage(new SimpleMessage("cannot close runsheet with consignment status out for delivery"));
                return new ForwardResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
            }
            runsheetService.saveRunSheet(runsheet);

        }
        return new RedirectResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());

    }
   
    // Method to create and download runsheet.It also makes an entry in consignment-tracking.
    public Resolution downloadDeliveryWorkSheet() {
        
        //checking url-parameter to see if only jsp has to be displayed or runsheet has to be created.
        if (!runsheetDownloadFunctionality) {
            trackingIdList = null;
            return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");

        } else {
            Runsheet              runsheetObj                     = null;
            Long                  prePaidBoxCount                 = null;
            User                  loggedOnUser                    = null;
            Hub                   deliveryHub                     = hubService.findHubByName(HKDeliveryConstants.DELIVERY_HUB);
            List<ShippingOrder>   shippingOrderList               = new ArrayList<ShippingOrder>();
            int                   totalPackets                    = 0;
            int                   totalCODPackets                 = 0;
            double                totalCODAmount                  = 0.0;
            List<String>          trackingIdsWithoutConsignment   = new ArrayList<String>();
            consignments                                          = new HashSet<Consignment>();
            //Getting HK-Delivery Courier Object.
            Courier               hkDeliveryCourier               = EnumCourier.HK_Delivery.asCourier();
            List<String>          duplicatedAwbNumbers            = null ;
            String                duplicateAwbString              = "";
            ConsignmentStatus     outForDelivery = getBaseDao().get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentOutForDelivery.getId());
            ConsignmentLifecycleStatus consignmentLifecycleStatus = getBaseDao().get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.Dispatched.getId());
            //Checking if agent selected has any open runsheet or not.
            if (runsheetService.agentHasOpenRunsheet(agent) == true) {
                addRedirectAlertMessage(new SimpleMessage(agent.getName() + " already has an open runsheet"));
                return new ForwardResolution(HKDRunsheetAction.class,"downloadDeliveryWorkSheet").addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
            }

            if (trackingIdList != null && trackingIdList.size() > 0) {
                //Fetching those awbNumbers which are present in open runsheets.(such consignments wont't be added to any new runsheet)
                duplicatedAwbNumbers = consignmentService.getDuplicateAwbNumbersinRunsheet(trackingIdList);
                if (duplicatedAwbNumbers.size() > 0) {
                    // creating a string for user-display.
                    duplicateAwbString = HKDeliveryUtil.convertListToString(duplicatedAwbNumbers);
                    duplicateAwbString = HKDeliveryConstants.OPEN_RUNSHEET_MESSAGE + duplicateAwbString;
                    addRedirectAlertMessage(new SimpleMessage(duplicateAwbString));
                    return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
                }
                    //Iterating over trackingIdList(entered by the user) to get coressponding shippingOrderList,consignmentList.
                    for (String trackingNum : trackingIdList) {
                        //Fetching consignment from trackingId(or awbNumber)
                        Consignment consignment = consignmentService.getConsignmentByAwbNumber(trackingNum);
                        //Getting loggedIn user,needed for consignment-tracking
                        if (getPrincipal() != null) {
                            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
                        }
                        //This is a check to ensure that runsheetObj wud be created for those trackingIds for which Consignment exists in DB.
                        if (consignment != null) {
                            if (HKDeliveryConstants.COD.equals(consignment.getPaymentMode())) {
                                ++totalCODPackets;
                                totalCODAmount = totalCODAmount + consignment.getAmount();
                                totalCODAmount = Math.round(totalCODAmount);
                            }
                            shippingOrderList.add(shipmentService.findByAwb(awbService.findByCourierAwbNumber(hkDeliveryCourier, trackingNum)).getShippingOrder());

                            //Changing consignment-status from ShipmntRcvdAtHub(10) to ShipmntOutForDelivry(20).
                            consignment.setConsignmentStatus(outForDelivery);
                            consignments.add(consignment);
                        } else {
                            //adding the trackingId without a consignment to a list.
                            trackingIdsWithoutConsignment.add(trackingNum);
                            continue;
                        }
                    }

                //logic for creating runsheet,downloading sheet
            // Calculating no. of total packets,no of prepaid boxes in runsheetObj.
            totalPackets = shippingOrderList.size();
            prePaidBoxCount = Long.parseLong((totalPackets - totalCODPackets) + "");

            try {
                xlsFile = new File(adminDownloads + "/" + CourierConstants.HKDELIVERY_WORKSHEET_FOLDER + "/" + CourierConstants.HKDELIVERY_WORKSHEET + "_" + sdf.format(new Date()) + ".xls");
                // Creating Runsheet object.
                runsheetObj = runsheetService.createRunsheet(hub, consignments, getRunSheetDao().get(RunsheetStatus.class, EnumRunsheetStatus.Open.getId()), agent, prePaidBoxCount, Long.parseLong(totalCODPackets + ""), totalCODAmount);
                // Saving Runsheet in db.
                runsheetService.saveRunSheet(runsheetObj);
                //making corresponding entry in consignment tracking.
                consignmentService.saveConsignmentTracking(consignmentService.createConsignmentTracking(hub,deliveryHub,loggedOnUser,new ArrayList<Consignment>(consignments),consignmentLifecycleStatus));
                // generating Xls file.
                xlsFile = hkdRunsheetManager.generateWorkSheetXls(xlsFile.getPath(), shippingOrderList, agent.getName(), totalCODAmount, totalPackets, totalCODPackets);
            } catch (IOException ioe) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_IOEXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
            } catch (NullPointerException npe) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_NULLEXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
            } catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_EXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
            }
            return new HTTPResponseResolution();
            } 
            return new ForwardResolution(HKDRunsheetAction.class,"downloadDeliveryWorkSheet").addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
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

    public Resolution downloadRunsheetAgain(){
        consignments = runsheet.getConsignments();
        List<ShippingOrder> shippingOrderList = consignmentService.getShippingOrderFromConsignments(new ArrayList<Consignment>(consignments));
        Map<Object,Object> runsheetCODParams = consignmentService.getRunsheetCODParams(consignments);
        try {
                xlsFile = new File(adminDownloads + "/" + CourierConstants.HKDELIVERY_WORKSHEET_FOLDER + "/" + CourierConstants.HKDELIVERY_WORKSHEET + "_" + sdf.format(new Date()) + ".xls");

                // generating Xls file.
                xlsFile = hkdRunsheetManager.generateWorkSheetXls(xlsFile.getPath(),shippingOrderList,runsheet.getAgent().getName(), (Double)runsheetCODParams.get(HKDeliveryConstants.TOTAL_COD_AMT), consignments.size(), (Integer)runsheetCODParams.get(HKDeliveryConstants.TOTAL_COD_PKTS));
            } catch (IOException ioe) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_IOEXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
            } catch (NullPointerException npe) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_NULLEXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
            } catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_EXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class).addParameter(HKDeliveryConstants.RUNSHEET_DOWNLOAD, false);
            }
            return new HTTPResponseResolution(); 
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

    public Runsheet getRunsheet() {
        return runsheet;
    }

    public void setRunsheet(Runsheet runsheet) {
        this.runsheet = runsheet;
    }

    public Boolean isRunsheetDownloadFunctionality() {
        return runsheetDownloadFunctionality;
    }

    public void setRunsheetDownloadFunctionality(Boolean runsheetDownloadFunctionality) {
        this.runsheetDownloadFunctionality = runsheetDownloadFunctionality;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public RunsheetStatus getRunsheetStatus() {
        return runsheetStatus;
    }

    public void setRunsheetStatus(RunsheetStatus runsheetStatus) {
        this.runsheetStatus = runsheetStatus;
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

    public List<Consignment> getRunsheetConsignments() {
        return runsheetConsignments;
    }

    public void setRunsheetConsignments(List<Consignment> runsheetConsignments) {
        this.runsheetConsignments = runsheetConsignments;
    }

    public RunSheetDao getRunSheetDao() {
        return runSheetDao;
    }

    public void setRunSheetDao(RunSheetDao runSheetDao) {
        this.runSheetDao = runSheetDao;
    }

    public List<Long> getChangedConsignmentIdsList() {
        return changedConsignmentIdsList;
    }

    public void setChangedConsignmentIdsList(List<Long> changedConsignmentIdsList) {
        this.changedConsignmentIdsList = changedConsignmentIdsList;
    }
}
