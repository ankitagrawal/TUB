package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.constants.core.EnumPermission;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.collections.CollectionUtils;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.UserService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.manager.HKDRunsheetManager;
import com.hk.admin.util.HKDeliveryUtil;
import com.hk.admin.dto.ConsignmentDto;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;
import com.hk.manager.SMSManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class HKDRunsheetAction extends BasePaginatedAction {

    private static  Logger                logger                             = LoggerFactory.getLogger(HKDRunsheetAction.class);
    private         File                  xlsFile;
    private         String                assignedTo;
    private         String                awbIdsWithoutConsignmntString      = null;
    private         List<String>          trackingIdList                     = new ArrayList<String>();
    private         Hub                   hub;
    private         List<Runsheet>        runsheetList                       = new ArrayList<Runsheet>();
    private         Boolean               runsheetDownloadFunctionality;
    private         Boolean               runsheetPreview;
    private         SimpleDateFormat      sdf                                = new SimpleDateFormat("dd-MM-yyyy hh:mm");
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
    private         List<Consignment> changedConsignmentList = new ArrayList<Consignment>();
    private         List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();

    private         List<ConsignmentStatus> consignmentStatuses;

    private         User                loggedOnUser;
    private         List<ConsignmentDto>  consignmentDtoList;

	private         Map<Consignment, String> consignmentOnHoldReason;

//	private         String
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
    @Autowired
    SMSManager smsManager;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloads;

    @SuppressWarnings("unchecked")
    @DefaultHandler
    public Resolution pre() {
     /*   loggedOnUser = getUserService().getUserById(getPrincipal().getId());*/
        loggedOnUser = getPrincipalUser();
        if(loggedOnUser != null && !loggedOnUser.hasPermission(EnumPermission.SELECT_HUB)){
            hub = hubService.getHubForUser(loggedOnUser);
        }
        runsheetPage = runsheetService.searchRunsheet(runsheet, startDate, endDate, runsheetStatus, agent, hub, getPageNo(), getPerPage());
        runsheetList = runsheetPage.getList();
        return new ForwardResolution("/pages/admin/hkRunsheetList.jsp");
    }

    public Resolution editRunsheet(){
        if(runsheet == null){
            return new ForwardResolution("/pages/admin/hkRunsheetList.jsp");
        }
        consignmentStatuses = consignmentService.getConsignmentStatusList();
        runsheetConsignments = new ArrayList<Consignment>(runsheet.getConsignments());
	    consignmentOnHoldReason = runsheetService.getOnHoldCustomerReasonForRunsheetConsignments(runsheet);
        return new ForwardResolution("/pages/admin/hkRunsheet.jsp");
    }

    public Resolution saveRunsheet(){        
        if(runsheet != null){
            consignments = new TreeSet<Consignment>(runsheetConsignments);
            runsheet.setConsignments(consignments);
            if(runsheet.getRunsheetStatus().getId().equals(EnumRunsheetStatus.Close.getId()) && runsheetService.isRunsheetClosable(runsheet) == false){
                addRedirectAlertMessage(new SimpleMessage("Cannot close runsheet with a consignment status out for delivery"));
                return new ForwardResolution(HKDRunsheetAction.class,"editRunsheet").addParameter("runsheet", runsheet.getId());
            }
            if((runsheet.getActualCollection() != null) &&
                ((runsheet.getActualCollection().doubleValue() - runsheet.getExpectedCollection().doubleValue()) > 10.0)){
                getContext().getValidationErrors().add("1", new SimpleError("Actual collected amount cannot be greater than expected amount "));
                return new ForwardResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
            }
            runsheetService.saveRunSheet(runsheet, changedConsignmentList, consignmentOnHoldReason);
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
            runsheetService.saveRunSheet(runsheet, changedConsignmentList, null);
        }
        return new RedirectResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
    }

    public Resolution closeRunsheet(){
        if(runsheet != null){
            if(runsheet.getActualCollection() == null){
                getContext().getValidationErrors().add("1", new SimpleError("Please enter actual collection"));
                return new ForwardResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
            }
            if(runsheetService.isRunsheetClosable(runsheet)){
	            if ((runsheet.getActualCollection() != null) &&
			            ((runsheet.getActualCollection().doubleValue() - runsheet.getExpectedCollection().doubleValue()) > 10.0)) {
		            getContext().getValidationErrors().add("1", new SimpleError("Actual collected amount cannot be greater than expected amount "));
		            return new ForwardResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
	            }
	            runsheet = runsheetService.closeRunsheet(runsheet);                
            }
            else{
                addRedirectAlertMessage(new SimpleMessage("cannot close runsheet with consignment status out for delivery or receieved at hub."));
                return new ForwardResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());
            }
            runsheetService.saveRunSheet(runsheet, changedConsignmentList, consignmentOnHoldReason);

        }
        return new RedirectResolution(HKDRunsheetAction.class, "editRunsheet").addParameter("runsheet", runsheet.getId());

    }

    //Method to create Runsheet preview and do necessary validation for runsheet creation.
    public Resolution previewRunsheet() {
        //checking url-parameter to see if only jsp has to be displayed or runsheet preview has to be created.
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        if (!runsheetPreview) {
            trackingIdList = null;
            return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");

        } else {
            Long prePaidBoxCount = null;
            int totalPackets = 0;
            int totalCODPackets = 0;
            double totalCODAmount = 0.0;
            List<String> trackingIdsWithoutConsignment = new ArrayList<String>();
            List<String> invalidConsignmentsForRunsheet = new ArrayList<String>();
            consignments = new TreeSet<Consignment>();
            String awbWithoutConsignmentString = "";
            String invalidConsignmentsForRunsheetString = "";
            //Getting HK-Delivery Courier Object.
            Courier hkDeliveryCourier = EnumCourier.HK_Delivery.asCourier();
            List<String> duplicatedAwbNumbers = null;
            String duplicateAwbString = "";
            Map<Object, Object> runsheetCODParams = null;

            //Checking if agent selected has any open runsheet or not.
            if (runsheetService.agentHasOpenRunsheet(agent) == true) {
                addRedirectAlertMessage(new SimpleMessage(agent.getName() + " already has an open runsheet"));
                return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
            }

            if (trackingIdList != null && trackingIdList.size() > 0) {
                //Fetching those awbNumbers which are present in open runsheets.(such consignments wont't be added to any new runsheet)
                duplicatedAwbNumbers = consignmentService.getDuplicateAwbNumbersinRunsheet(trackingIdList);
                if (duplicatedAwbNumbers.size() > 0) {
                    // creating a string for user-display.
                    duplicateAwbString = HKDeliveryUtil.convertListToString(duplicatedAwbNumbers);
                    duplicateAwbString = HKDeliveryConstants.OPEN_RUNSHEET_MESSAGE + duplicateAwbString;
                    addRedirectAlertMessage(new SimpleMessage(duplicateAwbString));
                    return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
                }
                //Iterating over trackingIdList(entered by the user) to get coressponding shippingOrderList,consignmentList.
                for (String trackingNum : trackingIdList) {
                    //Fetching consignment from trackingId(or awbNumber)
                    Consignment consignment = consignmentService.getConsignmentByAwbNumber(trackingNum);
                    //This is a check to ensure that runsheetObj wud be created for those trackingIds for which Consignment exists in DB.
                    if (consignment != null) {
                        if (consignmentService.isConsignmentValidForRunsheet(consignment)) {
                            consignments.add(consignment);
                        } else {
                            invalidConsignmentsForRunsheet.add(consignment.getAwbNumber());
                        }
                    } else {
                        //adding the trackingId without a consignment to a list.
                        trackingIdsWithoutConsignment.add(trackingNum);
                        continue;
                    }
                }

                awbWithoutConsignmentString = "Consignments are not created for following Awb Numbers:" + HKDeliveryUtil.convertListToString(trackingIdsWithoutConsignment);
                if (invalidConsignmentsForRunsheet.size() > 0) {
                    invalidConsignmentsForRunsheetString = "Consignments with following awb numbers cannot be added to runsheet as their statuses are Out For Delivery/Delivered/Damaged/Lost/Returned to Hub" + HKDeliveryUtil.convertListToString(invalidConsignmentsForRunsheet);

                }
                //logic for creating runsheet,downloading sheet
                // Calculating no. of total packets,no of prepaid boxes in runsheetObj.
                runsheetCODParams = consignmentService.getRunsheetCODParams(consignments);
                totalPackets = consignments.size();
                totalCODAmount = (Double) runsheetCODParams.get(HKDeliveryConstants.TOTAL_COD_AMT);
                totalCODPackets = (Integer) runsheetCODParams.get(HKDeliveryConstants.TOTAL_COD_PKTS);
                prePaidBoxCount = Long.parseLong((totalPackets - totalCODPackets) + "");
                if (consignments.size() > 0) {
                    // Creating Runsheet object.
                    runsheet = runsheetService.createRunsheet(hub, consignments, getRunSheetDao().get(RunsheetStatus.class, EnumRunsheetStatus.Open.getId()), agent, prePaidBoxCount, Long.parseLong(totalCODPackets + ""), totalCODAmount);
                    consignmentDtoList = consignmentService.getConsignmentDtoList(consignments);
                } else {
                    addRedirectAlertMessage(new SimpleMessage("Runsheet Cannot be created." + awbWithoutConsignmentString));
                    return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
                }
                addRedirectAlertMessage(new SimpleMessage(invalidConsignmentsForRunsheetString));
                return new ForwardResolution("/pages/admin/hkRunsheetPreview.jsp");
            }
            addRedirectAlertMessage(new SimpleMessage("Please add consignments to runsheet."));
            return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
        }
    }

    // Method to save runsheet obj in DB.
    public Resolution downloadDeliveryWorkSheet() {

        Runsheet runsheetObj = null;
        Long prePaidBoxCount = null;
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        Hub deliveryHub = hubService.findHubByName(HKDeliveryConstants.DELIVERY_HUB);
        List<Consignment> transferredConsignments = new ArrayList<Consignment>();
        int totalPackets = 0;
        int totalCODPackets = 0;
        double totalCODAmount = 0.0;
        Map<Object, Object> runsheetCODParams = null;
        ConsignmentStatus outForDelivery = getBaseDao().get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentOutForDelivery.getId());
        ConsignmentLifecycleStatus consignmentLifecycleStatus = getBaseDao().get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.Dispatched.getId());
        if (consignmentDtoList != null && consignmentDtoList.size() > 0) {
            if (getPrincipal() != null) {
                loggedOnUser = getUserService().getUserById(getPrincipal().getId());
            }

            transferredConsignments = consignmentService.updateTransferredConsignments(consignmentDtoList, agent, loggedOnUser);
            if(transferredConsignments != null && transferredConsignments.size()>0) {
            consignments = new TreeSet(CollectionUtils.subtract(consignmentService.getConsignmentsFromConsignmentDtos(consignmentDtoList),transferredConsignments));
            } else {
                consignments = new TreeSet(consignmentService.getConsignmentsFromConsignmentDtos(consignmentDtoList));
            }
            runsheetCODParams = consignmentService.getRunsheetCODParams(consignments);
            totalPackets = consignments.size();
            totalCODAmount = (Double) runsheetCODParams.get(HKDeliveryConstants.TOTAL_COD_AMT);
            totalCODPackets = (Integer) runsheetCODParams.get(HKDeliveryConstants.TOTAL_COD_PKTS);
            prePaidBoxCount = Long.parseLong((totalPackets - totalCODPackets) + "");
            for (Consignment consignment : consignments) {
              //Send SMS
              if (!consignment.getConsignmentStatus().getStatus().equals(EnumConsignmentStatus.ShipmentOutForDelivery.toString())) {
                try {
                  Awb awb = awbService.findByCourierAwbNumber(EnumCourier.HK_Delivery.asCourier(), consignment.getAwbNumber());
                  Shipment shipment = shipmentService.findByAwb(awb);
                  smsManager.sendHKReachOutForDeliverySMS(shipment, agent);
                } catch (Exception e) {
                  logger.error("Exception while sending sms: ", e);
                  e.printStackTrace();
                }
              }
              consignment.setConsignmentStatus(outForDelivery);

            }
              if(consignments.size()>0) {
            try {
                //shippingOrderList = consignmentService.getShippingOrderFromConsignments(new ArrayList(consignments));
                xlsFile = new File(adminDownloads + "/" + CourierConstants.HKDELIVERY_WORKSHEET_FOLDER + "/" + agent.getName()+ "_" + sdf.format(new Date()) + ".xls");
                // Creating Runsheet object.
                runsheetObj = runsheetService.createRunsheet(hub, consignments, getRunSheetDao().get(RunsheetStatus.class, EnumRunsheetStatus.Open.getId()), agent, prePaidBoxCount, Long.parseLong(totalCODPackets + ""), totalCODAmount);
                // Saving Runsheet in db.
                runsheetObj = runsheetService.saveRunSheet(runsheetObj);
                //making corresponding entry in consignment tracking.
                consignmentService.saveConsignmentTracking(consignmentService.createConsignmentTracking(hub, deliveryHub, loggedOnUser, new ArrayList<Consignment>(runsheetObj.getConsignments()), consignmentLifecycleStatus, runsheetObj));
                // generating Xls file.
                xlsFile = hkdRunsheetManager.generateWorkSheetXls(xlsFile.getPath(), runsheetObj.getConsignments(), agent.getName(), totalCODAmount, totalPackets, totalCODPackets, runsheetObj.getHub());
            } catch (IOException ioe) {
                logger.info("IOException Occurred" + ioe.getMessage());
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_IOEXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
            } catch (NullPointerException npe) {
                logger.debug("NullPointerException Occurred" + npe.getMessage());
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_NULLEXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
            } catch (Exception ex) {
                logger.debug("Exception Occurred" + ex.getMessage());
                addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_EXCEPTION));
                return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
            }
            return new HTTPResponseResolution();
        } else {
                  addRedirectAlertMessage(new SimpleMessage("Sorry no consignment left for making a runsheet."));
                  return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
              }
        }
        return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
    }


    //Method to just download the runsheet.
    public Resolution downloadRunsheetAgain() {
        logger.info("Inside download runsheet again");
        consignments = runsheet.getConsignments();
        //List<ShippingOrder> shippingOrderList = consignmentService.getShippingOrderFromConsignments(new ArrayList<Consignment>(consignments));
        Map<Object, Object> runsheetCODParams = consignmentService.getRunsheetCODParams(consignments);
        try {
            xlsFile = new File(adminDownloads + "/" + CourierConstants.HKDELIVERY_WORKSHEET_FOLDER + "/" + runsheet.getAgent().getName()+ "_" + sdf.format(new Date()) + ".xls");
            // generating Xls file.
            xlsFile = hkdRunsheetManager.generateWorkSheetXls(xlsFile.getPath(), consignments, runsheet.getAgent().getName(), (Double) runsheetCODParams.get(HKDeliveryConstants.TOTAL_COD_AMT), consignments.size(), (Integer) runsheetCODParams.get(HKDeliveryConstants.TOTAL_COD_PKTS), runsheet.getHub());
        } catch (IOException ioe) {
            logger.debug("IOException Occurred:" + ioe.getMessage());
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_IOEXCEPTION));
            return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
        } catch (NullPointerException npe) {
            logger.debug("NullPointerException Occurred:" + npe.getMessage());
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_NULLEXCEPTION));
            return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
        } catch (Exception ex) {
            logger.debug("Exception Occurred:" + ex.getMessage());
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_EXCEPTION));
            return new ForwardResolution(HKDRunsheetAction.class, HKDeliveryConstants.PREVIEW_RUNSHEET).addParameter(HKDeliveryConstants.RUNSHEET_PREVIEW_PARAM, false);
        }
        return new HTTPResponseResolution();
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

    public List<Consignment> getChangedConsignmentList() {
        return changedConsignmentList;
    }

    public void setChangedConsignmentList(List<Consignment> changedConsignmentList) {
        this.changedConsignmentList = changedConsignmentList;
    }

    public Boolean isRunsheetPreview() {
        return runsheetPreview;
    }

    public void setRunsheetPreview(Boolean runsheetPreview) {
        this.runsheetPreview = runsheetPreview;
    }

    public List<ShippingOrder> getShippingOrderList() {
        return shippingOrderList;
    }

    public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
        this.shippingOrderList = shippingOrderList;
    }

    public User getLoggedOnUser() {
        return loggedOnUser;
    }

    public void setLoggedOnUser(User loggedOnUser) {
        this.loggedOnUser = loggedOnUser;
    }

    public List<ConsignmentStatus> getConsignmentStatuses() {
        return consignmentStatuses;
    }

    public void setConsignmentStatuses(List<ConsignmentStatus> consignmentStatuses) {
        this.consignmentStatuses = consignmentStatuses;
    }

    public List<ConsignmentDto> getConsignmentDtoList() {
        return consignmentDtoList;
    }

    public void setConsignmentDtoList(List<ConsignmentDto> consignmentDtoList) {
        this.consignmentDtoList = consignmentDtoList;
    }

	public Map<Consignment, String> getConsignmentOnHoldReason() {
		return consignmentOnHoldReason;
	}

	public void setConsignmentOnHoldReason(Map<Consignment, String> consignmentOnHoldReason) {
		this.consignmentOnHoldReason = consignmentOnHoldReason;
	}
}
