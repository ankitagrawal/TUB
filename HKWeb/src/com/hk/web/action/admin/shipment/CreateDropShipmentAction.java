package com.hk.web.action.admin.shipment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.web.action.admin.queue.DropShippingAwaitingQueueAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;

import java.util.List;
import java.util.ArrayList;

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
    private String trackingId;
    @Autowired
    AwbService awbService;

    @Autowired
    ShipmentService shipmentService;
    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    ShippingOrderDao shippingOrderDao;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    AdminShippingOrderService adminShippingOrderService;

   List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
    Double boxWeight;


    @DefaultHandler
    public Resolution pre() {
        if(shippingOrder == null){
          addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("You have not selected the Drop shipped order"));
           return new RedirectResolution(DropShippingAwaitingQueueAction.class);
        } else {
               shippingOrderList.add(shippingOrder);
               return new ForwardResolution("/pages/admin/createDropShipment.jsp");
        }
    }

    public Resolution saveDropShipmentDetails() {
        Shipment shipment = new Shipment();
        shipment.setEmailSent(false);
        Double weightInKg = boxWeight;
        Awb finalAwb = null;
        if (trackingId == null) {
		 addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("You have not entered the tracking id"));
           return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder",shippingOrder);
        }
        if (shippingOrder == null){
           addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("You have not selected the Drop shipped order"));
           return new RedirectResolution(DropShippingAwaitingQueueAction.class);
        }
        Awb awb = awbService.createAwb(selectedCourier, trackingId.trim(), shippingOrder.getWarehouse(), shippingOrder.isCOD());
        if (awb != null) {

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
                    return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder",shippingOrder);
                }
                finalAwb = updateAttachStatus(awbFromDb);
            } else {
                awb = (Awb) awbService.save(awb, null);
                awbService.save(awb, EnumAwbStatus.Attach.getId().intValue());
                awbService.refresh(awb);
                finalAwb = awb;
            }
            shipment.setAwb(finalAwb);
            shipment.setShippingOrder(shippingOrder);
            shipment.setBoxWeight(weightInKg);
            shipment.setBoxSize(EnumBoxSize.MIGRATE.asBoxSize());
            shippingOrder.setShipment(shipment);
            shipment.setShippingOrder(shippingOrder);
            shipmentService.save(shipment);
            shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Shipped));
            shippingOrderDao.save(shippingOrder);
            String comment = "";
            if (shipment != null) {
                String trackingId = shipment.getAwb().getAwbNumber();
                comment = "Shipment Details: " + shipment.getAwb().getCourier().getName() + "/" + trackingId;
            }
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipped, comment);
        } else {
            addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage(" OPERATION FAILED *********You have not entered all the mandatory details"));
            return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder",shippingOrder);
        }
        addRedirectAlertMessage(new net.sourceforge.stripes.action.SimpleMessage("Shipmet has been created for your order"));         
            return new RedirectResolution(CreateDropShipmentAction.class).addParameter("shippingOrder",shippingOrder);
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
}
