package com.hk.web.action.core.order;

import com.akube.framework.stripes.action.BaseAction;
import com.google.gson.JsonObject;
import com.hk.admin.util.ChhotuCourierDelivery;
import com.hk.admin.util.CourierStatusUpdateHelper;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.CourierConstants;
import com.hk.domain.order.ShippingOrder;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * User: rahul
 * Time: 15 Feb, 2010 5:38:57 PM
 */
public class TrackCourierAction extends BaseAction {

    private static            Logger                      logger                         = LoggerFactory.getLogger(TrackCourierAction.class);

    private                   String                      trackingId;
    private                   ChhotuCourierDelivery       chhotuCourierDelivery;

    @Validate(required = true)
    private                   Long                        courierId;
    private                   ShippingOrder               shippingOrder;
    private                   String                      status;
    private                   String                      awb;
    private                   String                      paymentType;
    private                   String                      courierName;

    @Autowired
    CourierStatusUpdateHelper courierStatusUpdateHelper;


    @DefaultHandler
    public Resolution pre() {

        Resolution resolution = null;

        if (courierId.equals(EnumCourier.Aramex.getId())) {
            resolution = new RedirectResolution("http://www.aramex.com/track_results_multiple.aspx", false).addParameter("ShipmentNumber", trackingId);

        } /*else if (courierId.equals(EnumCourier.DTDC_Plus.getId()) || courierId.equals(EnumCourier.DTDC_Lite.getId()) || courierId.equals(EnumCourier.DTDC_COD.getId())) {
            resolution = new RedirectResolution("http://www.dtdc.in/dtdcTrack/Tracking/consignInfo.asp", false)
                    .addParameter("action", "track")
                    .addParameter("sec", "tr")
                    .addParameter("strCnno", trackingId)
                    .addParameter("TType", "cnno");

        } */
        else if (courierId.equals(EnumCourier.AFLWiz.getId())) {
            resolution = new RedirectResolution("http://trackntrace.aflwiz.com/aflwizhtmltrack", false).addParameter("shpntnum", trackingId);

        } else if (courierId.equals(EnumCourier.Speedpost.getId())) {
            resolution = new RedirectResolution("/pages/indiaPostCourier.jsp");

        } else if (courierId.equals(EnumCourier.FirstFLight.getId()) || courierId.equals(EnumCourier.FirstFLight_COD.getId())) {
            resolution = new RedirectResolution("http://www.firstflight.net/n_contrac_new.asp", false).addParameter("tracking1", trackingId);

        } else if (EnumCourier.getDelhiveryCouriers().contains(courierId)) {
            courierName = CourierConstants.DELHIVERY;
            JsonObject jsonObject = courierStatusUpdateHelper.updateDeliveryStatusDelhivery(trackingId);
            if (!jsonObject.has("Error")) {
                status = jsonObject.getAsJsonObject("Status").get("Status").getAsString();
                awb = jsonObject.get("AWB").getAsString();
                paymentType = jsonObject.get("OrderType").getAsString();
            }
            resolution = new ForwardResolution("/pages/courierDetails.jsp");
        } else if (courierId.equals(EnumCourier.Chhotu.getId())) {
            chhotuCourierDelivery = courierStatusUpdateHelper.updateDeliveryStatusChhotu(trackingId);
            resolution = new ForwardResolution("/pages/chhotuCourier.jsp");

        } else if (EnumCourier.getBlueDartCouriers().contains(courierId)) {
            courierName=CourierConstants.BLUEDART;
            Element ele = courierStatusUpdateHelper.updateDeliveryStatusBlueDart(trackingId);
            String responseStatus = ele.getChildText("Status");
            if (!responseStatus.equals("Incorrect Waybill number or No Information")) {
                status = ele.getChildText("Status");
            }
            resolution = new ForwardResolution("/pages/courierDetails.jsp");

        } else if (EnumCourier.getDTDCCouriers().contains(courierId)) {
            courierName=CourierConstants.DTDC;
            Map<String, String> responseMap = courierStatusUpdateHelper.updateDeliveryStatusDTDC(trackingId);
            for (Map.Entry entryObj : responseMap.entrySet()) {
                if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUS)) {
                    status = entryObj.getValue().toString();
                }
            }
            resolution = new ForwardResolution("/pages/courierDetails.jsp");
        } else {
            resolution = new RedirectResolution("/pages/error/invalidCourier.jsp");
        }
        return resolution;
    }


    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public ChhotuCourierDelivery getChhotuCourierDelivery() {
        return chhotuCourierDelivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }
}
