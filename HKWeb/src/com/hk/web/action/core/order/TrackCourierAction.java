package com.hk.web.action.core.order;

import com.akube.framework.stripes.action.BaseAction;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hk.admin.util.ChhotuCourierDelivery;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.CourierConstants;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    @Autowired
    private                   ShippingOrderDao            shippingOrderDao;

    private                   ShippingOrder               shippingOrder;

    private                   String                      status;
    private                   String                      awb;
    private                   String                      paymentType;
    private static final      String                      authenticationIdForDelhivery   = "9aaa943a0c74e29b340074d859b2690e07c7fb25";
    private static final      String                      loginIdForBlueDart             = "GGN37392";
    private static final      String                      licenceKeyForBlueDart          = "3c6867277b7a2c8cd78c8c4cb320f401";
    private                   String                      courierName;


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

        } */else if (courierId.equals(EnumCourier.AFLWiz.getId())) {
            resolution = new RedirectResolution("http://trackntrace.aflwiz.com/aflwizhtmltrack", false).addParameter("shpntnum", trackingId);

        } else if (courierId.equals(EnumCourier.Speedpost.getId())) {
            resolution = new RedirectResolution("/pages/indiaPostCourier.jsp");

        } else if (courierId.equals(EnumCourier.FirstFLight.getId()) || courierId.equals(EnumCourier.FirstFLight_COD.getId())) {
            resolution = new RedirectResolution("http://www.firstflight.net/n_contrac_new.asp", false).addParameter("tracking1", trackingId);

        } else if (courierId.equals(EnumCourier.Delhivery.getId())) {
            courierName = "Delhivery";
            shippingOrder = shippingOrderDao.findByTrackingId(trackingId);
            String gatewayOrderId = "";
            if (shippingOrder != null) {
                gatewayOrderId = shippingOrder.getGatewayOrderId();
            }
            try {
                URL url = new URL("http://track.delhivery.com/api/packages/json/?token=" + authenticationIdForDelhivery + "&ref_nos=" + gatewayOrderId);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
                String inputLine;
                String jsonFormattedResponse = "";

                while ((inputLine = in.readLine()) != null) {
                    if (inputLine != null) {
                        jsonFormattedResponse += inputLine;
                    }
                }
                in.close();
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(jsonFormattedResponse).getAsJsonObject();
                if (!jsonObject.has("Error")) {
                    JsonObject shipmentObj = jsonObject.getAsJsonArray("ShipmentData").get(0)
                            .getAsJsonObject().getAsJsonObject("Shipment");

                    status = shipmentObj.getAsJsonObject("Status").get("Status").getAsString();
                    awb = shipmentObj.get("AWB").getAsString();
                    paymentType = shipmentObj.get("OrderType").getAsString();
                }
            } catch (MalformedURLException mue) {
                logger.error("malformed url for gateway id " + gatewayOrderId);
                mue.printStackTrace();
            } catch (IOException ioe) {
                logger.error("ioexception encounter for gateway id " + gatewayOrderId);
                ioe.printStackTrace();
            } catch (NullPointerException npe) {
                logger.error("Null pointer Exception for gateway id " + gatewayOrderId);
                npe.printStackTrace();
            }
            resolution = new ForwardResolution("/pages/courierDetails.jsp");

        } else if (courierId.equals(EnumCourier.Chhotu.getId())) {
            courierName = "Chhotu";
            try {
                URL url = new URL("http://api.chhotu.in/shipmenttracking?tracking_number=" + trackingId);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
                String inputLine;
                String response = "";
                String jsonFormattedResponse = "";

                while ((inputLine = in.readLine()) != null) {
                    if (inputLine != null) {
                        response += inputLine;
                    }
                }
                in.close();
                jsonFormattedResponse = response.substring(14, (response.length() - 1));
                chhotuCourierDelivery = new Gson().fromJson(jsonFormattedResponse, ChhotuCourierDelivery.class);
            } catch (MalformedURLException mue) {
                logger.error("malformed url for tracking id " + trackingId);
                mue.printStackTrace();
            } catch (IOException ioe) {
                logger.error("ioexception encounter for tracking id " + trackingId);
                ioe.printStackTrace();
            } catch (NullPointerException npe) {
                logger.error("Null pointer Exception for tracking id " + trackingId);
                npe.printStackTrace();
            }
            resolution = new ForwardResolution("/pages/chhotuCourier.jsp");

        } else if (courierId.equals(EnumCourier.BlueDart.getId()) || courierId.equals(EnumCourier.BlueDart_COD.getId())) {
            courierName = "BlueDart";
            try {
                URL url = new URL("http://www.bluedart.com/servlet/RoutingServlet?handler=tnt&action=custawbquery&loginid=" + loginIdForBlueDart + "&awb=awb&numbers=" + trackingId + "&format=xml&lickey=" + licenceKeyForBlueDart + "&verno=1.3&scan=1");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
                String inputLine;
                String response = "";

                while ((inputLine = in.readLine()) != null) {
                    if (inputLine != null) {
                        response += inputLine;
                    }
                }
                in.close();
                Document doc = new SAXBuilder().build(new StringReader(response));
                XPath xPath = XPath.newInstance("/*/Shipment");
                Element ele = (Element) xPath.selectSingleNode(doc);
                String responseStatus = ele.getChildText("Status");
                if (!responseStatus.equals("Incorrect Waybill number or No Information")) {
                    status = ele.getChildText("Status");
                }
            } catch (MalformedURLException mue) {
                logger.error("malformed url for gateway id " + trackingId);
                mue.printStackTrace();
            } catch (IOException ioe) {
                logger.error("ioexception encounter for gateway id " + trackingId);
                ioe.printStackTrace();
            } catch (NullPointerException npe) {
                logger.error("Null pointer Exception for gateway id " + trackingId);
                npe.printStackTrace();
            } catch (Exception e) {
                logger.error("Null pointer Exception for gateway id " + trackingId);
                e.printStackTrace();
            }
            resolution = new ForwardResolution("/pages/courierDetails.jsp");

        } else if (courierId.equals(EnumCourier.DTDC_COD.getId()) || courierId.equals(EnumCourier.DTDC_Lite.getId()) ||
                courierId.equals(EnumCourier.DTDC_Plus.getId()) || courierId.equals(EnumCourier.DTDC_Surface.getId())) {
            courierName = "DTDC";
            BufferedReader bufferedReader = null;

            try {
                //http://cust.dtdc.co.in/DPTrack/XMLCnTrk.asp?strcnno=B65303941&TrkType=cnno&addtnlDtl=Y&strCustType=DP&strCustDtl=BL8068
                URL url = new URL("http://cust.dtdc.co.in/DPTrack/XMLCnTrk.asp?strcnno=" + trackingId + "&TrkType=" + trackingId + "&addtnlDtl=Y&strCustType=DP&strCustDtl=DP");
                bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                String response = "";
                //added for debugging
                response = "<DTDCREPLY>" +
                        "<CONSIGNMENT>" +
                        "<CNHEADER>" +
                        "<CNTRACK>TRUE</CNTRACK>" +
                        "<FIELD NAME=\"strShipmentNo\" VALUE=\"A15082271\" />" +
                        "<FIELD NAME=\"strRefNo\" VALUE=\"N/A\" />" +
                        "<FIELD NAME=\"strMode\" VALUE=\"AIR\" />\n" +
                        "<FIELD NAME=\"strOrigin\" VALUE=\"LEAK-PROOF ENGINEERING PVT.LTD, AHMEDABAD\" />" +
                        "<FIELD NAME=\"strOriginRemarks\" VALUE=\"Received from\" />" +
                        "<FIELD NAME=\"strBookedOn\" VALUE=\"08072009\" />" +
                        "<FIELD NAME=\"strPieces\" VALUE=\"1\" />" +
                        "<FIELD NAME=\"strWeightUnit\" VALUE=\"Kg\" />" +
                        "<FIELD NAME=\"strWeight\" VALUE=\"0.020\" />" +
                        "<FIELD NAME=\"strDestination\" VALUE=\"PUNE\" />" +
                        "<FIELD NAME=\"strStatus\" VALUE=\"DELIVERED\" />" +
                        "<FIELD NAME=\"strStatusTransOn\" VALUE=\"12062012\" />" +
                        "<FIELD NAME=\"strStatusTransTime\" VALUE=\"1210\" />" +
                        "<FIELD NAME=\"strRemarks\" VALUE=\"CO SEAL\" />" +
                        "<FIELD NAME=\"strNoOfAttempts\" VALUE=\"\" />" +
                        "</CNHEADER>" +
                        "<CNBODY>" +
                        "<CNACTIONTRACK>TRUE</CNACTIONTRACK>" +
                        "<CNACTION>" +
                        "<FIELD NAME=\"strAction\" VALUE=\"DISPATCHED\" />" +
                        "<FIELD NAME=\"strManifestNo\" VALUE=\"A1534068\" />" +
                        "<FIELD NAME=\"strOrigin\" VALUE=\"AHMEDABAD APEX, AHMEDABAD\" />" +
                        "<FIELD NAME=\"strDestination\" VALUE=\"PUNE APEX, PUNE\" />" +
                        "<FIELD NAME=\"strActionDate\" VALUE=\"08072009\" />" +
                        "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                        "</CNACTION>" +
                        "<CNACTION>" +
                        "<FIELD NAME=\"strAction\" VALUE=\"RECEIVED\" />" +
                        "<FIELD NAME=\"strManifestNo\" VALUE=\"A1534068\" />" +
                        "<FIELD NAME=\"strOrigin\" VALUE=\"AHMEDABAD, AHMEDABAD\" />" +
                        "<FIELD NAME=\"strDestination\" VALUE=\"PUNE APEX, PUNE\" />" +
                        "<FIELD NAME=\"strActionDate\" VALUE=\"09072009\" />" +
                        "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                        "</CNACTION>" +
                        "<CNACTION>" +
                        "<FIELD NAME=\"strAction\" VALUE=\"DISPATCHED\" />" +
                        "<FIELD NAME=\"strManifestNo\" VALUE=\"90033365\" />" +
                        "<FIELD NAME=\"strOrigin\" VALUE=\"PUNE, PUNE\" />" +
                        "<FIELD NAME=\"strDestination\" VALUE=\"PUNE SHIVAJI NAGAR BRANCH, PUNE\" />" +
                        "<FIELD NAME=\"strActionDate\" VALUE=\"09072009\" />" +
                        "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                        "</CNACTION>" +
                        "<CNACTION>" +
                        "<FIELD NAME=\"strAction\" VALUE=\"RECEIVED\" />" +
                        "<FIELD NAME=\"strManifestNo\" VALUE=\"90033365\" />" +
                        "<FIELD NAME=\"strOrigin\" VALUE=\"PUNE, PUNE\" />" +
                        "<FIELD NAME=\"strDestination\" VALUE=\"PUNE SHIVAJI NAGAR BRANCH, PUNE\" />" +
                        "<FIELD NAME=\"strActionDate\" VALUE=\"09072009\" />" +
                        "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                        "</CNACTION>" +
                        "<CNACTION>" +
                        "<FIELD NAME=\"strAction\" VALUE=\"OUT FOR DELIVERY\" />" +
                        "<FIELD NAME=\"strManifestNo\" VALUE=\"91108846\" />" +
                        "<FIELD NAME=\"strOrigin\" VALUE=\"PUNE SHIVAJI NAGAR BRANCH, PUNE\" />" +
                        "<FIELD NAME=\"strDestination\" VALUE=\"KOTHRUD (MAIN ROAD), PUNE\" />" +
                        "<FIELD NAME=\"strActionDate\" VALUE=\"09072009\" />" +
                        "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                        "</CNACTION>" +
                        "</CNBODY>" +
                        "</CONSIGNMENT>" +
                        "</DTDCREPLY>";
                /*while ((inputLine = bufferedReader.readLine()) != null) {
                  if (inputLine != null) {
                    response += inputLine;
                  }
                }*/
                Document doc = new SAXBuilder().build(new StringReader(response));

                //XPath xPath = XPath.newInstance("/*/Shipment");
                //Element ele = (Element) xPath.selectSingleNode(doc);
                Element element = doc.getRootElement();
                Element consig = element.getChild(CourierConstants.DTDC_INPUT_CONSIGNMENT);
                Element header = consig.getChild(CourierConstants.DTDC_INPUT_CNHEADER);
                Element cnTrack = header.getChild(CourierConstants.DTDC_INPUT_CNTRACK);
                String trackStatus = header.getChildText(CourierConstants.DTDC_INPUT_CNTRACK);
                List fields = header.getChildren();
                if (trackStatus.equalsIgnoreCase("TRUE")) {
                    Map<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < fields.size(); i++) {
                        Element elementObj = (Element) fields.get(i);
                        if (elementObj != cnTrack) {
                            if (elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue().equals(CourierConstants.DTDC_INPUT_STR_STATUS)) {
                                map.put(elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue(), elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_VALUE).getValue());
                            }
                        }
                    }

                    for (Map.Entry entryObj : map.entrySet()) {
                        if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUS)) {
                            status = entryObj.getValue().toString();
                        }
                    }
                }
            } catch (MalformedURLException mue) {
                logger.error("Malformed URL for tracking id: "+ trackingId);
                mue.printStackTrace();

            } catch (IOException ioe) {
                logger.error("IOException for tracking id:" + trackingId);
                ioe.printStackTrace();

            } catch (NullPointerException npe) {
                logger.error("NullPointerException for tracking id:"+ trackingId);
                npe.printStackTrace();

            } catch (Exception e) {
                logger.error("Exception occurred for tracking id:" + trackingId);
                e.printStackTrace();

            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error(CourierConstants.IO_EXCEPTION + trackingId);
                    e.printStackTrace();
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
