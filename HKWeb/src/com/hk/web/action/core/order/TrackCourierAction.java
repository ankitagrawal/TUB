	package com.hk.web.action.core.order;

import java.util.List;
import java.util.Map;

import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.google.gson.JsonObject;
import com.hk.admin.util.ChhotuCourierDelivery;
import com.hk.admin.util.CourierStatusUpdateHelper;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.courier.EnumCourier;
import com.hk.domain.order.ShippingOrder;
import com.hk.exception.HealthkartCheckedException;

/**
 * User: rahul
 * Time: 15 Feb, 2010 5:38:57 PM
 */
public class TrackCourierAction extends BaseAction {

    private static            Logger                      logger                 = LoggerFactory.getLogger(TrackCourierAction.class);

    private                   String                      trackingId;
    private                   ChhotuCourierDelivery       chhotuCourierDelivery;

    @Validate(required = true)
    private                   Long                        courierId;
    private                   ShippingOrder               shippingOrder;
    private                   String                      status;
    private                   String                      awb;
    private                   String                      paymentType;
    private                   String                      courierName;
	Consignment consignment;
	List<ConsignmentTracking> consignmentTrackingList;

    @Autowired
    CourierStatusUpdateHelper courierStatusUpdateHelper;

	@Autowired
	ConsignmentService consignmentService;


    @SuppressWarnings("unchecked")
    @DefaultHandler
    public Resolution pre() {
        Resolution resolution = null;
        EnumCourier enumCourier = EnumCourier.getEnumCourierFromCourierId(courierId);
	    if(enumCourier == null){
		 return new RedirectResolution("/pages/error/courierTrackError.jsp");  
	    }
        switch (enumCourier) {
            case Aramex:
                resolution = new RedirectResolution("http://www.aramex.com/track_results_multiple.aspx", false).addParameter("ShipmentNumber", trackingId);
                break;
            case AFLWiz:
                resolution = new RedirectResolution("http://trackntrace.aflwiz.com/aflwizhtmltrack", false).addParameter("shpntnum", trackingId);
                break;
            case Speedpost:
                resolution = new RedirectResolution("/pages/indiaPostCourier.jsp");
                break;
            case FirstFLight:
                resolution = new RedirectResolution("http://www.firstflight.net/n_contrac_new.asp", false).addParameter("tracking1", trackingId);
                break;
            case Chhotu:
                try {
                    chhotuCourierDelivery = courierStatusUpdateHelper.updateDeliveryStatusChhotu(trackingId);
                } catch (HealthkartCheckedException hce) {
                    logger.debug("Exception occurred in TrackCourierAction");
                }
                if (chhotuCourierDelivery != null) {
                    resolution = new ForwardResolution("/pages/chhotuCourier.jsp");
                } else {
                    resolution = new RedirectResolution("/pages/error/courierTrackError.jsp");
                }
                break;

            case Delhivery:
                courierName = CourierConstants.DELHIVERY;
                JsonObject jsonObject = null;
                try {
                    jsonObject = courierStatusUpdateHelper.updateDeliveryStatusDelhivery(trackingId);
                } catch (HealthkartCheckedException hce) {
                    logger.debug("Exception occurred in TrackCourierAction");
                }
                if (jsonObject != null) {
                    if (!jsonObject.has("Error")) {
                        status = jsonObject.getAsJsonObject(CourierConstants.DELHIVERY_STATUS).get(CourierConstants.DELHIVERY_STATUS).getAsString();
                        awb = jsonObject.get(CourierConstants.DELHIVERY_AWB).getAsString();
                        paymentType = jsonObject.get(CourierConstants.DELHIVERY_ORDER_TYPE).getAsString();
                    }
                    resolution = new ForwardResolution("/pages/courierDetails.jsp");
                } else {
                    resolution = new RedirectResolution("/pages/error/courierTrackError.jsp");
                }
                break;
            case BlueDart:
            case BlueDart_COD:
                courierName = CourierConstants.BLUEDART;
                Element ele = null;
                try {
                    ele = courierStatusUpdateHelper.updateDeliveryStatusBlueDart(trackingId);
                } catch (HealthkartCheckedException hce) {
                    logger.debug("Exception occurred in TrackCourierAction");
                }
                if (ele != null) {
                    String responseStatus = ele.getChildText(CourierConstants.DELHIVERY_STATUS);
                    if (!responseStatus.equals(CourierConstants.DELHIVERY_ERROR_MSG)) {
                        status = ele.getChildText(CourierConstants.DELHIVERY_STATUS);
                    }
                    resolution = new ForwardResolution("/pages/courierDetails.jsp");
                } else {
                    resolution = new RedirectResolution("/pages/error/courierTrackError.jsp");
                }
                break;
            case DTDC_COD:
            case DTDC_Lite:
            case DTDC_Plus:
            case DTDC_Surface:
                courierName = CourierConstants.DTDC;
                Map<String, String> responseMap = null;
                try {
                    responseMap = courierStatusUpdateHelper.updateDeliveryStatusDTDC(trackingId);
                } catch (HealthkartCheckedException hce) {
                    logger.debug("Exception occurred in TrackCourierAction");
                }
                if (responseMap != null) {
                    for (Map.Entry entryObj : responseMap.entrySet()) {
                        if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUS)) {
                            status = entryObj.getValue().toString();
                        }
                    }
                    resolution = new ForwardResolution("/pages/courierDetails.jsp");
                } else {
                    resolution = new RedirectResolution("/pages/error/courierTrackError.jsp");
                }
                break;

	        case HK_Delivery:
		        if (trackingId != null) {
			        consignment = consignmentService.getConsignmentByAwbNumber(trackingId);
			        if (consignment != null) {
				        consignmentTrackingList = consignmentService.getConsignmentTracking(consignment);
				        resolution = new ForwardResolution("/pages/hkDeliveryTracking.jsp");
			        } else {
				        resolution = new RedirectResolution("/pages/error/courierTrackError.jsp");
			        }
		        }
		        else{
			        resolution = new RedirectResolution("/pages/error/courierTrackError.jsp");
		        }
	            
	            break;
            default:
                resolution = new RedirectResolution("/pages/error/courierTrackError.jsp");

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

	public Consignment getConsignment() {
		return consignment;
	}

	public void setConsignment(Consignment consignment) {
		this.consignment = consignment;
	}

	public List<ConsignmentTracking> getConsignmentTrackingList() {
		return consignmentTrackingList;
	}

	public void setConsignmentTrackingList(List<ConsignmentTracking> consignmentTrackingList) {
		this.consignmentTrackingList = consignmentTrackingList;
	}
}
