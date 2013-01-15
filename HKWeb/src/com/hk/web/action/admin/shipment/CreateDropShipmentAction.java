package com.hk.web.action.admin.shipment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.accounting.SeekInvoiceNumService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.*;
import com.hk.domain.order.ShippingOrder;
import com.hk.helper.InvoiceNumHelper;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.admin.queue.DropShippingAwaitingQueueAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Dec 5, 2012
 * Time: 4:44:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CreateDropShipmentAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(CreateDropShipmentAction.class);

    private ShippingOrder shippingOrder;
    Courier selectedCourier;
    Courier vendorCourier;
    private String trackingId;
    @Autowired
    AwbService awbService;

    @Autowired
    ShipmentService shipmentService;
    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    AdminShippingOrderService adminShippingOrderService;
    @Autowired
    private SeekInvoiceNumService seekInvoiceNumService;
    @Autowired
    PincodeService pincodeService;

    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
    Double boxWeight;

    private BoxSize boxSize;

    Courier suggestedCourier;

    Shipment shipment = null;


    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_DROP_SHIPPING_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        if (shippingOrder == null) {
            addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("You have not selected the Drop shipped order"));
            return new RedirectResolution(DropShippingAwaitingQueueAction.class);
        } else {
            shippingOrderList.add(shippingOrder);
            return new ForwardResolution("/pages/admin/createDropShipment.jsp");
        }
    }


    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_DROP_SHIPPING_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution saveDropShipmentDetails() {
        if (shippingOrder.getShipment() == null) {
            shipment = new Shipment();
        } else {
            shipment = shippingOrder.getShipment();
        }

        if (vendorCourier != null && !vendorCourier.equals("")){
              selectedCourier = vendorCourier;
        }

        shipment.setEmailSent(false);
        Double weightInKg = boxWeight;
        if (trackingId == null) {
            addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("You have not entered the tracking id"));
            return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder", shippingOrder);
        }
        Awb finalAwb = null;
        Awb suggestedAwb = null;
        if (shippingOrder.getShipment() != null) {
            suggestedAwb = shippingOrder.getShipment().getAwb();
            suggestedCourier = shippingOrder.getShipment().getAwb().getCourier();
        }
        finalAwb = suggestedAwb;
        if ((suggestedAwb == null) || (!(suggestedAwb.getAwbNumber().equalsIgnoreCase(trackingId.trim()))) ||
                (suggestedCourier != null && (!(selectedCourier.equals(suggestedCourier))))) {
            //User has not used suggested one and  has enetered  AWB manually
            if ((suggestedAwb != null) && (suggestedCourier != null) && (ThirdPartyAwbService.integratedCouriers.contains(suggestedCourier.getId()))) {
                // To delete the tracking no. generated previously
                awbService.deleteAwbForThirdPartyCourier(suggestedCourier, suggestedAwb.getAwbNumber());
            }

            if (ThirdPartyAwbService.integratedCouriers.contains(selectedCourier.getId())) {
                weightInKg = shipment.getBoxWeight();
                if (weightInKg == null || weightInKg == 0D) {
//					weightInKg = shipmentService.getEstimatedWeightOfShipment(shippingOrder);
                    weightInKg = boxWeight;
                }
                Awb thirdPartyAwb = awbService.getAwbForThirdPartyCourier(selectedCourier, shippingOrder, weightInKg);
                if (thirdPartyAwb == null) {
                    addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage(" The tracking number could not be generated"));
                    return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder", shippingOrder);
                } else {
                    finalAwb = (Awb) awbService.save(thirdPartyAwb, null);
                    awbService.save(finalAwb, EnumAwbStatus.Attach.getId().intValue());
                }
            } else {
                // For Non Fedex Couriers
                Awb awbFromDb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(selectedCourier, trackingId.trim(), null, null, null);
                if (awbFromDb != null && awbFromDb.getAwbNumber() != null) {
                    //User has eneterd AWB manually which is present in database Already
                    boolean error = false;
                    AwbStatus awbStatus = awbFromDb.getAwbStatus();
                    if (EnumAwbStatus.getAllStatusExceptUnused().contains(awbStatus)) {
                        error = true;
                    } else if ((!awbFromDb.getWarehouse().getId().equals(shippingOrder.getWarehouse().getId())) || (awbFromDb.getCod() != shippingOrder.isCOD())) {
                        error = true;
                    }
                    if (error) {
                        addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage(" OPERATION FAILED *********  Tracking Id : " + trackingId + "       is already Used with other  shipping Order  OR  already Present in another warehouse with same courier"));
                        return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder", shippingOrder);
                    }
                    finalAwb = updateAttachStatus(awbFromDb);

                } else {
                    //Create New AWb (Authorization_Pending shows it might not a valid  Awb , since person has added it manually.
                    Awb awb = awbService.createAwb(selectedCourier, trackingId.trim(), shippingOrder.getWarehouse(), shippingOrder.isCOD());
                    awb = (Awb) awbService.save(awb, null);
                    awbService.save(awb, EnumAwbStatus.Authorization_Pending.getId().intValue());
                    awbService.refresh(awb);
                    finalAwb = awb;
                }
            }
        } else {
            //user has used suggested one
            finalAwb = updateAttachStatus(finalAwb);
        }
        shipment.setAwb(finalAwb);
        shipment.setShippingOrder(shippingOrder);
        shipment.setBoxWeight(weightInKg);
//          shipment.setBoxSize(EnumBoxSize.MIGRATE.asBoxSize());
        shipment.setBoxSize(boxSize);
        shippingOrder.setShipment(shipment);
        shipment.setShippingOrder(shippingOrder);
        if(shipment.getZone() == null){
			Pincode pinCode = shippingOrder.getBaseOrder().getAddress().getPincode();
			shipment.setZone(pinCode.getZone());
		}
        shipmentService.save(shipment);
        shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ReadyForDropShipping));
        shippingOrderService.save(shippingOrder);
        String comment = "";
        if (shipment != null) {
            String trackingId = shipment.getAwb().getAwbNumber();
            comment = "Shipment Details has been saved: " + shipment.getAwb().getCourier().getName() + "/" + trackingId;
        }
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ShipmentDetailSaved, comment);
        addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("Shipment has been created for your order"));
        return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder", shippingOrder);
    }


  
    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_DROP_SHIPPING_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution markShippingOrdersAsShipped() {
        logger.info("Drop shipment Item mark as shipped");
        if (shippingOrder != null) {
            // shippingOrder.setAccountingInvoiceNumber();
            if (shippingOrder.getShipment()== null){
              addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("Please Enter the shipment details"));
              return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder", shippingOrder);
            }
            
           trackingId = shippingOrder.getShipment().getAwb().getAwbNumber();
           selectedCourier = shippingOrder.getShipment().getAwb().getCourier(); 

            if (trackingId == null || trackingId.isEmpty()){
              addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("Please Enter the Tracking Id"));
              return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder", shippingOrder);
            }
            if (selectedCourier == null){
              addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("Please select the courier"));
              return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder", shippingOrder);
            }
            String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().isB2bOrder());
            shippingOrder.setAccountingInvoiceNumber(seekInvoiceNumService.getInvoiceNum(invoiceType, shippingOrder.getWarehouse()));
            adminShippingOrderService.markShippingOrderAsShipped(shippingOrder);

            addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("Orders have been marked as shipped"));
        } else {
            addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("Please select order to be marked as shipped"));
        }
        return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder", shippingOrder);
    }


    private Awb updateAttachStatus(Awb finalAwb) {
        int rowsUpdate = (Integer) awbService.save(finalAwb, EnumAwbStatus.Attach.getId().intValue());
        if (rowsUpdate == 0) {
            addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage(" OPERATION FAILED *********  Tracking Id : " + trackingId + "       is Already Used with Another User Order   ,     Try again With New Tracking ID"));
            pre();
        }
        awbService.refresh(finalAwb);
        return finalAwb;
    }


    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public Courier getSelectedCourier() {
        return selectedCourier;
    }

    public void setSelectedCourier(Courier selectedCourier) {
        this.selectedCourier = selectedCourier;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        CreateDropShipmentAction.logger = logger;
    }

    public Double getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(Double boxWeight) {
        this.boxWeight = boxWeight;
    }

    public AwbService getAwbService() {
        return awbService;
    }

    public void setAwbService(AwbService awbService) {
        this.awbService = awbService;
    }

    public List<ShippingOrder> getShippingOrderList() {
        return shippingOrderList;
    }

    public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
        this.shippingOrderList = shippingOrderList;
    }

    public ShipmentService getShipmentService() {
        return shipmentService;
    }

    public void setShipmentService(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    public BoxSize getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(BoxSize boxSize) {
        this.boxSize = boxSize;
    }

    public Courier getSuggestedCourier() {
        return suggestedCourier;
    }

    public void setSuggestedCourier(Courier suggestedCourier) {
        this.suggestedCourier = suggestedCourier;
    }

    public Courier getVendorCourier() {
        return vendorCourier;
    }

    public void setVendorCourier(Courier vendorCourier) {
        this.vendorCourier = vendorCourier;
    }
}
