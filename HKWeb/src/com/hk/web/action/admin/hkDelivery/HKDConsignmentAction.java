package com.hk.web.action.admin.hkDelivery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.util.HKDeliveryUtil;
import com.hk.constants.core.EnumPermission;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.ConsignmentLifecycleStatus;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;

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
    private             Boolean               reconciled;
    private             Runsheet              runsheet;
    private             User                loggedOnUser;


    @Autowired
    private              ConsignmentService          consignmentService;
    @Autowired
    private              HubService                  hubService;
    @Autowired
    private              AwbService                  awbService;
    @Autowired
    private              ShipmentService             shipmentService;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;




    @DefaultHandler
    public Resolution pre(){
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");        
    }

    @SuppressWarnings("unchecked")
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
        /*Consignment  consignment                      = null;*/
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
                        shipmentObj.getShippingOrder().getBaseOrder().getAddress().getCity()+"-"+shipmentObj.getShippingOrder().getBaseOrder().getAddress().getPincode().getPincode();
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
            consignmentTrackingList = consignmentService.createConsignmentTracking(healthkartHub,hub,loggedOnUser,consignmentList ,consignmentLifecycleStatus, null);
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

    @SuppressWarnings("unchecked")
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

    public Resolution trackConsignment(){

        if(doTracking){
            consignment = consignmentService.getConsignmentByAwbNumber(consignmentNumber);
            if(consignment != null) {
            consignmentTrackingList = consignmentService.getConsignmentTracking(consignment);
            logger.info(consignment + "" + consignmentTrackingList.size());
            } else {
                addRedirectAlertMessage(new SimpleMessage("Consignment doesn't exist."));
            }

            return new ForwardResolution("/pages/admin/trackConsignment.jsp"); 
        }
        return new ForwardResolution("/pages/admin/trackConsignment.jsp");
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
