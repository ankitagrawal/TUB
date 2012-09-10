package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.EnumPermission;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.Keys;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.user.User;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.util.HKDeliveryUtil;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.util.XslGenerator;
import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

@Component
public class HKDConsignmentAction extends BasePaginatedAction {

    private static       Logger                      logger                    = LoggerFactory.getLogger(HKDConsignmentAction.class);
    private              Hub                         hub;
    private              List<String>                trackingIdList            = new ArrayList<String>();
    private              Courier                     hkDelivery                = EnumCourier.HK_Delivery.asCourier();
    private              String                      consignmentNumber;
    private              Boolean                     doTracking;
    private              List<ConsignmentTracking>   consignmentTrackingList   = new ArrayList<ConsignmentTracking>();


    private             Consignment           consignment;
    private             Page                  consignmentPage;

    private             Date                  startDate;
    private             Date                  endDate;
    private             ConsignmentStatus     consignmentStatus;
    private             Integer               defaultPerPage           = 20;
    private             List<Consignment>     consignmentList          = new ArrayList<Consignment>();
    private             List<Consignment>     consignmentListForPaymentReconciliation = new ArrayList<Consignment>();
    private             HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation;
    private             Boolean               reconciled;
    private             Runsheet              runsheet;
    private             File                  xlsFile;
    private             SimpleDateFormat      sdf                      = new SimpleDateFormat("dd-MM-yyyy");

    User                loggedOnUser;                     


    @Autowired
    private              ConsignmentService          consignmentService;
    @Autowired
    private              HubService                  hubService;
    @Autowired
    private              AwbService                  awbService;
    @Autowired
    private              ShipmentService             shipmentService;
    @Autowired
    private              XslGenerator                xslGenerator;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;




    @DefaultHandler
    public Resolution pre(){
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");        
    }

    @Transactional
    public Resolution markShipmentsReceived() {
        Set<String>  awbNumberSet ;
        List<String> existingAwbNumbers;
        Hub          healthkartHub;
        String       duplicateAwbString               = "";
        Shipment     shipmentObj                      = null;
        String       cnnNumber                        = null;
        String       paymentMode                      = null;
        Double       amount                           = null;
        String       address                          = null;
        Consignment  consignment                      = null;
        List<String> newAwbNumbers                    = null;
        List<Consignment> consignmentList             = new ArrayList<Consignment>();
        List<ConsignmentTracking> consignmentTrackingList = null;
        ConsignmentLifecycleStatus consignmentLifecycleStatus = getBaseDao().get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.ReceivedAtHub.getId());

        if (trackingIdList != null && trackingIdList.size() > 0) {
            healthkartHub = hubService.findHubByName(HKDeliveryConstants.HEALTHKART_HUB);
            if (getPrincipal() != null) {
                loggedOnUser = getUserService().getUserById(getPrincipal().getId());
            }
            //Checking if consignment is already created for the enterd awbNumber and fetching the awb for the same .
            existingAwbNumbers = consignmentService.getDuplicateAwbNumbersinConsignment(trackingIdList);
            if (existingAwbNumbers.size() > 0) {
                // removing duplicated awbs from the list.
                trackingIdList.removeAll(existingAwbNumbers);
                newAwbNumbers = (List<String>) CollectionUtils.subtract(trackingIdList,existingAwbNumbers);
                // creating a string for user-display.
                duplicateAwbString = HKDeliveryUtil.convertListToString(existingAwbNumbers);
                duplicateAwbString =HKDeliveryConstants.CONSIGNMNT_DUPLICATION_MSG + duplicateAwbString;
            }
            // convertig list to set to delete/remove duplicate elements from the list.
            newAwbNumbers = trackingIdList;
            awbNumberSet = new HashSet<String>(newAwbNumbers);

            if(awbNumberSet.size()>0) {
            // Creating consignments.
            for (String awbNumber : awbNumberSet) {
            try {
                shipmentObj = shipmentService.findByAwb(awbService.findByCourierAwbNumber(hkDelivery,awbNumber));
                amount = shipmentObj.getShippingOrder().getAmount();
                cnnNumber = shipmentObj.getShippingOrder().getGatewayOrderId();
                address = shipmentObj.getShippingOrder().getBaseOrder().getAddress().getLine1()+","+shipmentObj.getShippingOrder().getBaseOrder().getAddress().getLine2()+","+
                        shipmentObj.getShippingOrder().getBaseOrder().getAddress().getCity()+"-"+shipmentObj.getShippingOrder().getBaseOrder().getAddress().getPin();
                paymentMode = consignmentService.getConsignmentPaymentMode(shipmentObj.getShippingOrder());
                // Creating consignment object and adding to consignmentList.
                consignmentList.add(consignmentService.createConsignment(awbNumber,cnnNumber,amount,paymentMode,address ,hub));
            } catch (Exception ex) {
                logger.info("Exception occurred"+ex.getMessage());
                continue;
            }
        }
            try{
            //saving consignmentList and consignmentTrackingList.
            consignmentService.saveConsignments(consignmentList);
            //fetching the consignments just created above.
            consignmentList = consignmentService.getConsignmentListByAwbNumbers(new ArrayList<String>(awbNumberSet));
            //creating consignmentTrackingList
            consignmentTrackingList = consignmentService.createConsignmentTracking(healthkartHub,hub,loggedOnUser,consignmentList ,consignmentLifecycleStatus);
            //saving it.
            consignmentService.saveConsignmentTracking(consignmentTrackingList);
            addRedirectAlertMessage(new SimpleMessage(consignmentTrackingList.size() + HKDeliveryConstants.CONSIGNMNT_CREATION_SUCCESS + duplicateAwbString));
            } catch (Exception ex){
              addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.CONSIGNMENT_FAILURE));
            }

            } else {
                addRedirectAlertMessage(new SimpleMessage(duplicateAwbString));
                return new RedirectResolution(HKDConsignmentAction.class);
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.CONSIGNMNT_CREATION_FAILURE));
        }
        return new RedirectResolution(HKDConsignmentAction.class);
    }

    public Resolution searchConsignments(){
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        if(!loggedOnUser.hasPermission(EnumPermission.SELECT_HUB)){
            hub = hubService.getHubForUser(loggedOnUser);
        }
        consignmentPage = consignmentService.searchConsignment(consignment, consignmentNumber, startDate, endDate, consignmentStatus, hub, runsheet, reconciled, getPageNo(), getPerPage());
        if(consignmentPage != null){
            consignmentList = consignmentPage.getList();
        }
        return new ForwardResolution("/pages/admin/hkConsignmentList.jsp");
    }

    public Resolution generatePaymentReconciliation(){
        for(Consignment consignment : consignmentListForPaymentReconciliation){
            if(!consignment.getConsignmentStatus().getStatus().equals(EnumConsignmentStatus.ShipmentDelivered.getStatus())){
                addRedirectAlertMessage(new SimpleMessage("Status of consignment "+ consignment.getAwbNumber() + " is not delivered."));
                return new ForwardResolution(HKDConsignmentAction.class, "searchConsignments");
            }
        }
        hkdeliveryPaymentReconciliation = consignmentService.createPaymentReconciliationForConsignmentList(consignmentListForPaymentReconciliation, getUserService().getUserById(getPrincipal().getId()));
        return new ForwardResolution("/pages/admin/hkdeliveryPaymentReconciliation.jsp");
    }

    public Resolution savePaymentReconciliation(){
        hkdeliveryPaymentReconciliation.setConsignments(new HashSet<Consignment>(consignmentListForPaymentReconciliation));
        if(hkdeliveryPaymentReconciliation.getActualAmount() == null){
            getContext().getValidationErrors().add("1", new SimpleError("Please Enter actual collected amount"));
            return new ForwardResolution(HKDConsignmentAction.class, "savePaymentReconciliation");
        }
        if((hkdeliveryPaymentReconciliation.getActualAmount().doubleValue() - hkdeliveryPaymentReconciliation.getExpectedAmount().doubleValue()) > 10){
            getContext().getValidationErrors().add("1", new SimpleError("Actual collected amount cannot be blank or greater than expected amount"));
            return new ForwardResolution(HKDConsignmentAction.class, "savePaymentReconciliation");
        }
        hkdeliveryPaymentReconciliation = consignmentService.saveHkdeliveryPaymentReconciliation(hkdeliveryPaymentReconciliation);
        addRedirectAlertMessage(new SimpleMessage("Payment Reconciliation saved."));        
        return new ForwardResolution(HKDConsignmentAction.class, "editPaymentReconciliation").addParameter("hkdeliveryPaymentReconciliation", hkdeliveryPaymentReconciliation.getId());
    }

    public Resolution editPaymentReconciliation(){
        if(hkdeliveryPaymentReconciliation != null){
            consignmentListForPaymentReconciliation = new ArrayList<Consignment>(hkdeliveryPaymentReconciliation.getConsignments());
            return new ForwardResolution("/pages/admin/hkdeliveryPaymentReconciliation.jsp");
        }
        addRedirectAlertMessage(new SimpleMessage("Payment Reconciliation saved."));
        return new ForwardResolution(HKDConsignmentAction.class, "editPaymentReconciliation").addParameter("hkdeliveryPaymentReconciliation", hkdeliveryPaymentReconciliation.getId());
    }

    public Resolution trackConsignment(){
        Consignment consignment = null;
        if(doTracking){
            consignment = consignmentService.getConsignmentByAwbNumber(consignmentNumber);
            consignmentTrackingList = consignmentService.getConsignmentTracking(consignment);
            logger.info(consignment + "" + consignmentTrackingList.size());

            return new ForwardResolution("/pages/admin/trackConsignment.jsp"); 
        }
        return new ForwardResolution("/pages/admin/trackConsignment.jsp");
    }

    public Resolution downloadPaymentReconciliation() {
           logger.info("Inside downloadPaymentReconciliation");
           try {
               xlsFile = new File(adminDownloadsPath + "/" + "PaymentReconciliation" + "_" + sdf.format(new Date()) + ".xls");
               // generating Xls file.
               xlsFile = xslGenerator.generateHKDPaymentReconciliationXls(xlsFile.getPath(), hkdeliveryPaymentReconciliation);
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

    
    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public List<String> getTrackingIdList() {
        return trackingIdList;
    }

    public void setTrackingIdList(List<String> trackingIdList) {
        this.trackingIdList = trackingIdList;
    }

    public int getPerPageDefault() {
        return defaultPerPage; // To change body of implemented methods use File | Settings | File Templates.
    }

    public int getPageCount() {
        return consignmentPage == null ? 0 : consignmentPage.getTotalPages();
    }

    public int getResultCount() {
        return consignmentPage == null ? 0 : consignmentPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("hub");
        params.add("consignmentStatus");
        params.add("startDate");
        params.add("endDate");
        params.add("reconciled");
        return params;
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

    public Consignment getConsignment() {
        return consignment;
    }

    public void setConsignment(Consignment consignment) {
        this.consignment = consignment;
    }

    public ConsignmentStatus getConsignmentStatus() {
        return consignmentStatus;
    }

    public void setConsignmentStatus(ConsignmentStatus consignmentStatus) {
        this.consignmentStatus = consignmentStatus;
    }

    public List<Consignment> getConsignmentList() {
        return consignmentList;
    }

    public void setConsignmentList(List<Consignment> consignmentList) {
        this.consignmentList = consignmentList;
    }

    public List<Consignment> getConsignmentListForPaymentReconciliation() {
        return consignmentListForPaymentReconciliation;
    }

    public void setConsignmentListForPaymentReconciliation(List<Consignment> consignmentListForPaymentReconciliation) {
        this.consignmentListForPaymentReconciliation = consignmentListForPaymentReconciliation;
    }

    public String getConsignmentNumber() {
        return consignmentNumber;
    }

    public void setConsignmentNumber(String consignmentNumber) {
        this.consignmentNumber = consignmentNumber;
    }

    public Boolean isDoTracking() {
        return doTracking;
    }

    public void setDoTracking(Boolean doTracking) {
        this.doTracking = doTracking;
    }

    public HkdeliveryPaymentReconciliation getHkdeliveryPaymentReconciliation() {
        return hkdeliveryPaymentReconciliation;
    }

    public void setHkdeliveryPaymentReconciliation(HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation) {
        this.hkdeliveryPaymentReconciliation = hkdeliveryPaymentReconciliation;
    }
    
    public List<ConsignmentTracking> getConsignmentTrackingList() {
        return consignmentTrackingList;
    }

    public void setConsignmentTrackingList(List<ConsignmentTracking> consignmentTrackingList) {
        this.consignmentTrackingList = consignmentTrackingList;
    }

    public Boolean getReconciled() {
        return reconciled;
    }

    public void setReconciled(Boolean reconciled) {
        this.reconciled = reconciled;
    }

    public Runsheet getRunsheet() {
        return runsheet;
    }

    public void setRunsheet(Runsheet runsheet) {
        this.runsheet = runsheet;
    }

    public User getLoggedOnUser() {
        return loggedOnUser;
    }

    public void setLoggedOnUser(User loggedOnUser) {
        this.loggedOnUser = loggedOnUser;
    }
}
