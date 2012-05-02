package web.action.core.order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akube.framework.stripes.action.BaseAction;
import com.google.gson.Gson;
import com.hk.admin.util.ChhotuCourierDelivery;
import com.hk.constants.shipment.EnumCourier;

/**
 * User: rahul
 * Time: 15 Feb, 2010 5:38:57 PM
 */
public class TrackCourierAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(TrackCourierAction.class);

	private String trackingId;
	private ChhotuCourierDelivery chhotuCourierDelivery;

	@Validate(required = true)
	private Long courierId;

	@DefaultHandler
	public Resolution pre() {

		Resolution resolution = null;

		if (courierId.equals(EnumCourier.Aramex.getId())) {
			resolution = new RedirectResolution("http://www.aramex.com/track_results_multiple.aspx", false).addParameter("ShipmentNumber", trackingId);
		} else if (courierId.equals(EnumCourier.DTDC_Plus.getId()) || courierId.equals(EnumCourier.DTDC_Lite.getId()) || courierId.equals(EnumCourier.DTDC_COD.getId())) {
			resolution = new RedirectResolution("http://www.dtdc.in/dtdcTrack/Tracking/consignInfo.asp", false)
					.addParameter("action", "track")
					.addParameter("sec", "tr")
					.addParameter("strCnno", trackingId)
					.addParameter("TType", "cnno");
		} else if (courierId.equals(EnumCourier.AFLWiz.getId())) {
			//resolution = new RedirectResolution("http://trackntrace.aflwiz.com/Wiz_Summary.jsp", false).addParameter("shpntnum", trackingId);
			resolution = new RedirectResolution("http://trackntrace.aflwiz.com/aflwizhtmltrack", false).addParameter("shpntnum", trackingId);
		} else if (courierId.equals(EnumCourier.Speedpost.getId())) {
			resolution = new RedirectResolution("/pages/indiaPostCourier.jsp");
		} else if (courierId.equals(EnumCourier.Delhivery.getId())) {
			resolution = new RedirectResolution("/pages/delhiveryCourier.jsp");
		} else if (courierId.equals(EnumCourier.Chhotu.getId())) {
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
			}
			catch (MalformedURLException mue) {
				logger.error("malformed url for tracking id " + trackingId);
				mue.printStackTrace();
			}
			catch (IOException ioe) {
				logger.error("ioexception encounter for tracking id " + trackingId);
				ioe.printStackTrace();
			}
			catch (NullPointerException npe) {
				logger.error("Null pointer Exception for tracking id " + trackingId);
				npe.printStackTrace();
			}
			resolution = new ForwardResolution("/pages/chhotuCourier.jsp");
		} else if (courierId.equals(EnumCourier.BlueDart.getId()) || courierId.equals(EnumCourier.BlueDart_COD.getId())) {
			resolution = new RedirectResolution("http://www.bluedart.com/servlet/RoutingServlet", false).addParameter("action", "awbquery")
					.addParameter("awb", "awb").addParameter("handler", "tnt").addParameter("numbers", trackingId);
		} else if (courierId.equals(EnumCourier.FirstFLight.getId())|| courierId.equals(EnumCourier.FirstFLight_COD.getId())) {
			resolution = new RedirectResolution("http://www.firstflight.net/n_contrac_new.asp", false).addParameter("tracking1", trackingId);
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

}
