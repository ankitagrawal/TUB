package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.dao.Page;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.constants.core.Keys;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.util.XslGenerator;
import com.hk.domain.hkDelivery.HkdeliveryPaymentReconciliation;
import net.sourceforge.stripes.validation.SimpleError;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.validation.Validate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

@Component
public class HKDPaymentReconciliationAction extends BasePaginatedAction {

    private static Logger logger = LoggerFactory.getLogger(HKDPaymentReconciliationAction.class);
    private Page hkPaymentReconciliationPage;
    private Integer defaultPerPage = 20;
    private Date startDate;
    private Date endDate;
    private File xlsFile;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private List<HkdeliveryPaymentReconciliation> paymentReconciliationList = new ArrayList<HkdeliveryPaymentReconciliation>();
    private HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation;
	private List<Consignment> consignmentListForPaymentReconciliation = new ArrayList<Consignment>();
	private User loggedOnUser;
	private Hub hub;


    @Autowired
    private ConsignmentService consignmentService;
    @Autowired
    private XslGenerator xslGenerator;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;


    @DefaultHandler
    public Resolution searchPaymentReconciliation() {
        hkPaymentReconciliationPage = consignmentService.getPaymentReconciliationListByDates(startDate, endDate, getPageNo(), getPerPage());
        if (hkPaymentReconciliationPage != null) {
            paymentReconciliationList = hkPaymentReconciliationPage.getList();
        }
        return new ForwardResolution("/pages/admin/hkPaymentReconciliationList.jsp");
    }

	public Resolution hkDeliveryreports(){
		loggedOnUser = getUserService().getUserById(getPrincipal().getId());
		return new ForwardResolution("/pages/admin/hkdeliveryReports.jsp");
	}

	public Resolution generatePaymentReconciliation(){
		loggedOnUser = getUserService().getUserById(getPrincipal().getId());
		if(consignmentListForPaymentReconciliation.size() ==0 && (startDate != null || endDate != null)){
			consignmentListForPaymentReconciliation = consignmentService.getConsignmentsForPaymentReconciliation(startDate, endDate, hub);
			if(consignmentListForPaymentReconciliation.size() == 0){
				addRedirectAlertMessage(new SimpleMessage("No delivered consignment found for given date"));
                return new ForwardResolution("/pages/admin/hkdeliveryReports.jsp");
			}
		}
		if (consignmentListForPaymentReconciliation.size() == 0) {
			addRedirectAlertMessage(new SimpleMessage("No delivered consignment selected"));
			return new ForwardResolution(HKDConsignmentAction.class, "searchConsignments");
		}
        for(Consignment consignment : consignmentListForPaymentReconciliation){
            if(!consignment.getConsignmentStatus().getStatus().equals(EnumConsignmentStatus.ShipmentDelivered.getStatus())){
                addRedirectAlertMessage(new SimpleMessage("Status of consignment "+ consignment.getAwbNumber() + " is not delivered."));
                return new ForwardResolution(HKDConsignmentAction.class, "searchConsignments");
            }
        }
        hkdeliveryPaymentReconciliation = consignmentService.createPaymentReconciliationForConsignmentList(consignmentListForPaymentReconciliation, loggedOnUser);
        return new ForwardResolution("/pages/admin/hkdeliveryPaymentReconciliation.jsp");
    }

	public Resolution savePaymentReconciliation(){
		loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        hkdeliveryPaymentReconciliation.setConsignments(new TreeSet<Consignment>(consignmentListForPaymentReconciliation));
        if(hkdeliveryPaymentReconciliation.getActualAmount() == null){
            getContext().getValidationErrors().add("1", new SimpleError("Please Enter actual collected amount"));
            return new ForwardResolution(HKDPaymentReconciliationAction.class, "savePaymentReconciliation");
        }
        if((hkdeliveryPaymentReconciliation.getActualAmount().doubleValue() - hkdeliveryPaymentReconciliation.getExpectedAmount().doubleValue()) > 10){
            getContext().getValidationErrors().add("1", new SimpleError("Actual collected amount cannot be blank or greater than expected amount"));
            return new ForwardResolution(HKDPaymentReconciliationAction.class, "savePaymentReconciliation");
        }
        hkdeliveryPaymentReconciliation = consignmentService.saveHkdeliveryPaymentReconciliation(hkdeliveryPaymentReconciliation, loggedOnUser);
        addRedirectAlertMessage(new SimpleMessage("Payment Reconciliation saved."));
        return new ForwardResolution(HKDPaymentReconciliationAction.class, "editPaymentReconciliation").addParameter("hkdeliveryPaymentReconciliation", hkdeliveryPaymentReconciliation.getId());
    }

	public Resolution editPaymentReconciliation(){
        if(hkdeliveryPaymentReconciliation != null){
            consignmentListForPaymentReconciliation = new ArrayList<Consignment>(hkdeliveryPaymentReconciliation.getConsignments());
            return new ForwardResolution("/pages/admin/hkdeliveryPaymentReconciliation.jsp");
        }
        addRedirectAlertMessage(new SimpleMessage("Payment Reconciliation saved."));
        return new ForwardResolution(HKDPaymentReconciliationAction.class, "editPaymentReconciliation").addParameter("hkdeliveryPaymentReconciliation", hkdeliveryPaymentReconciliation.getId());
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

    public int getPerPageDefault() {
        return defaultPerPage; // To change body of implemented methods use File | Settings | File Templates.
    }

    public int getPageCount() {
        return hkPaymentReconciliationPage == null ? 0 : hkPaymentReconciliationPage.getTotalPages();
    }

    public int getResultCount() {
        return hkPaymentReconciliationPage == null ? 0 : hkPaymentReconciliationPage.getTotalResults();
    }


    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("startDate");
        params.add("endDate");
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

    public List<HkdeliveryPaymentReconciliation> getPaymentReconciliationList() {
        return paymentReconciliationList;
    }

    public void setPaymentReconciliationList(List<HkdeliveryPaymentReconciliation> paymentReconciliationList) {
        this.paymentReconciliationList = paymentReconciliationList;
    }

    public HkdeliveryPaymentReconciliation getHkdeliveryPaymentReconciliation() {
        return hkdeliveryPaymentReconciliation;
    }

    public void setHkdeliveryPaymentReconciliation(HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation) {
        this.hkdeliveryPaymentReconciliation = hkdeliveryPaymentReconciliation;
    }

	public List<Consignment> getConsignmentListForPaymentReconciliation() {
		return consignmentListForPaymentReconciliation;
	}

	public void setConsignmentListForPaymentReconciliation(List<Consignment> consignmentListForPaymentReconciliation) {
		this.consignmentListForPaymentReconciliation = consignmentListForPaymentReconciliation;
	}

	public Hub getHub() {
		return hub;
	}

	public void setHub(Hub hub) {
		this.hub = hub;
	}

	public User getLoggedOnUser() {
		return loggedOnUser;
	}

	public void setLoggedOnUser(User loggedOnUser) {
		this.loggedOnUser = loggedOnUser;
	}
}
