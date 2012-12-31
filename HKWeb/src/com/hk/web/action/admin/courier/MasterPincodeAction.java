package com.hk.web.action.admin.courier;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeRegionZoneService;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.core.City;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.RegionType;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.core.PincodeService;
import com.hk.util.XslGenerator;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS})
@Component
public class MasterPincodeAction extends BaseAction {
	@Autowired
	PincodeDao pincodeDao;
	@Autowired
	CourierServiceInfoDao courierServiceInfoDao;
	@Autowired
	CourierService courierService;
	@Autowired
	XslGenerator xslGenerator;
	@Autowired
	PincodeRegionZoneService pincodeRegionZoneService;
	@Autowired
	PincodeService pincodeService;

	@Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminDownloadsPath;

	// @Named(Keys.Env.adminUploads)
	@Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminUploadsPath;

	@Autowired
	XslParser xslParser;

	FileBean fileBean;

	private static Logger logger = LoggerFactory.getLogger(MasterPincodeAction.class);
	private Long pincodesInSystem = 0L;
	private String pincodeString;
	private Pincode pincode;
	private List<CourierServiceInfo> courierServiceList = new ArrayList<CourierServiceInfo>();
	private PincodeRegionZone pincodeRegionZone;
	private List<PincodeRegionZone> pincodeRegionZoneList = null;
	private List<Pincode> pincodeList;



	@DefaultHandler
	public Resolution pre() {
		try {
			pincodesInSystem = Long.valueOf(pincodeDao.getAll(Pincode.class).size());
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}
		return new ForwardResolution("/pages/admin/searchAndAddPincodes.jsp");
	}

	public Resolution search() {
		try {
			pincode = pincodeDao.getByPincode(pincodeString);
			if (pincode != null) {
				courierServiceList = courierService.getCourierServiceInfoList(null, pincodeString, false, false, false, null);
				return new ForwardResolution("/pages/admin/searchAndAddPincodes.jsp");
			}
			// return new RedirectResolution(MasterPincodeAction.class).addParameter("pincode", pincode.getId());
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}
		addRedirectAlertMessage(new SimpleMessage("No such pincode in system"));
		return new RedirectResolution(MasterPincodeAction.class);
	}

	public Resolution save() {
		int numberOfPRZSavedForPinocde  = 0;
		if (pincode == null || StringUtils.isBlank(pincode.getPincode()) || pincode.getCity() == null || pincode.getState() == null
				|| pincode.getZone() == null || pincode.getPincode().length() < 6 || (!StringUtils.isNumeric(pincode.getPincode()))) {
			addRedirectAlertMessage(new SimpleMessage("Enter values correctly."));
			return new RedirectResolution(MasterPincodeAction.class);
		}
		Pincode pincodeByCode = pincodeDao.getByPincode(pincode.getPincode());
		Pincode pincodeDb = null;
		if (pincodeByCode != null && pincode.getId() == null) {
			City previousCity = pincodeByCode.getCity();
			pincodeByCode.setLocality(pincode.getLocality());
			pincodeByCode.setCity(pincode.getCity());
			pincodeByCode.setState(pincode.getState());
			pincodeByCode.setRegion(pincode.getRegion());
			pincodeByCode.setDefaultCourier(pincode.getDefaultCourier());
			pincodeDb = (Pincode) pincodeDao.save(pincodeByCode);
			if (!(previousCity.equals(pincode.getCity()))) {
				numberOfPRZSavedForPinocde = pincodeRegionZoneService.assignPincodeRegionZoneToPincode(pincodeDb);
			}
		} else {
			pincodeDb = (Pincode) pincodeDao.save(pincode);
			numberOfPRZSavedForPinocde = pincodeRegionZoneService.assignPincodeRegionZoneToPincode(pincodeDb);
		}
		if(numberOfPRZSavedForPinocde > 0){
		addRedirectAlertMessage(new SimpleMessage(" Pincode Saved and  Total ::::::::  " + numberOfPRZSavedForPinocde + "    ------- New Pincode Region Zone also saved For Pincode :: " + pincodeDb.getPincode()));
		return new RedirectResolution(MasterPincodeAction.class, "searchPincodeRegion").addParameter("pincodeRegionZone.pincode.pincode", pincodeDb.getPincode());
		}
		else {
		addRedirectAlertMessage(new SimpleMessage("Pincode changes saved and PRZs are not changed"));	
		return new RedirectResolution(CourierServiceInfoAction.class).addParameter("pincode", pincode.getPincode());
		}
	}

	public Resolution generatePincodeExcel() throws Exception {
		List<Pincode> pincodeList = new ArrayList<Pincode>();

		pincodeList = pincodeDao.getAll(Pincode.class);

		String excelFilePath = adminDownloadsPath + "/pincodeExcelFiles/pincodes" + System.currentTimeMillis() + ".xls";
		final File excelFile = new File(excelFilePath);

		xslGenerator.generatePincodeXsl(pincodeList, excelFilePath);
		addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));
		return new Resolution() {

			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				OutputStream out = null;
				InputStream in = new BufferedInputStream(new FileInputStream(excelFile));
				res.setContentLength((int) excelFile.length());
				res.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getName() + "\";");
				out = res.getOutputStream();

				// Copy the contents of the file to the output stream
				byte[] buf = new byte[4096];
				int count = 0;
				while ((count = in.read(buf)) >= 0) {
					out.write(buf, 0, count);
				}
			}
		};
	}

	public Resolution uploadPincodeExcel() throws Exception {
		if (fileBean == null) {
			addRedirectAlertMessage(new SimpleMessage("Choose File to Upload "));
			return new ForwardResolution("/pages/admin/searchAndAddPincodes.jsp");
		}
		String excelFilePath = adminUploadsPath + "/pincodeExcelFiles/pincodes" + System.currentTimeMillis() + ".xls";
		File excelFile = new File(excelFilePath);
		excelFile.getParentFile().mkdirs();
		fileBean.save(excelFile);

		try {
			Set<Pincode> pincodeSet = xslParser.readPincodeList(excelFile);
			for (Pincode pincode : pincodeSet) {
				if (pincode != null)
					pincodeDao.save(pincode);
				logger.info("inserting or updating:" + pincode.getPincode());
			}
		} catch (Exception e) {
			logger.error("Exception while reading excel sheet.", e);
			addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
			return new ForwardResolution("/pages/admin/searchAndAddPincodes.jsp");
		}

		excelFile.delete();
		addRedirectAlertMessage(new SimpleMessage("Database Updated"));
		return new ForwardResolution("/pages/admin/searchAndAddPincodes.jsp");
	}

	public Resolution directToPincodeRegionZone() {
		return new RedirectResolution("/pages/admin/addPincodeRegionZone.jsp");
	}

	public Resolution savePincodeRegionList() {
		for (PincodeRegionZone pincodeRegionZone : pincodeRegionZoneList) {
			pincodeRegionZoneService.saveOrUpdate(pincodeRegionZone);
		}
		addRedirectAlertMessage(new SimpleMessage("Pincode Region saved"));
		return new RedirectResolution("/pages/admin/addPincodeRegionZone.jsp");

	}

	public Resolution savePincodeRegion() {
		Pincode pincodeObj = pincodeService.getByPincode(pincodeRegionZone.getPincode().getPincode());
		if (pincodeObj == null) {
			addRedirectAlertMessage(new SimpleMessage("Pincode does not exist in System"));
		} else {
			try {
				PincodeRegionZone pincodeRegionZoneDb = pincodeRegionZoneService.getPincodeRegionZone(pincodeRegionZone.getCourierGroup(), pincodeObj, pincodeRegionZone.getWarehouse());
				if (pincodeRegionZoneDb != null) {
					pincodeRegionZoneDb.setRegionType(pincodeRegionZone.getRegionType());
				} else {
					pincodeRegionZone.setPincode(pincodeObj);
					pincodeRegionZoneDb = pincodeRegionZone;
				}
				pincodeRegionZoneService.save(pincodeRegionZoneDb);
			} catch (Exception ex) {
				addRedirectAlertMessage(new SimpleMessage("EXCEPTION IN SAVING" + ex.getMessage()));
				logger.debug("Exception In Saving Pincode Region" + ex.getStackTrace());
				return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
			}
			addRedirectAlertMessage(new SimpleMessage("Pincode region saved"));
		}
		return new RedirectResolution("/pages/admin/addPincodeRegionZone.jsp");
	}

	public Resolution searchPincodeRegion() {
		Pincode pincode = pincodeService.getByPincode(pincodeRegionZone.getPincode().getPincode());
		if (pincode == null) {
			addRedirectAlertMessage(new SimpleMessage("Pincode does not exist in System"));
		} else {
			pincodeRegionZoneList = pincodeRegionZoneService.getPincodeRegionZoneList(pincodeRegionZone.getCourierGroup(), pincode, pincodeRegionZone.getWarehouse());
			if (pincodeRegionZoneList == null) {
				addRedirectAlertMessage(new SimpleMessage("Pincode Region zone does not exist for Pincode"));
			}
		}
		return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
	}

	public Resolution showRemainingPrz() {
		pincodeList = pincodeService.getPincodeNotInPincodeRegionZone();
		return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
	}

	public Long getPincodesInSystem() {
		return pincodesInSystem;
	}

	public String getPincodeString() {
		return pincodeString;
	}

	public void setPincodeString(String pincodeString) {
		this.pincodeString = pincodeString;
	}

	public Pincode getPincode() {
		return pincode;
	}

	public void setPincode(Pincode pincode) {
		this.pincode = pincode;
	}

	public List<CourierServiceInfo> getCourierServiceList() {
		return courierServiceList;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public CourierService getCourierService() {
		return courierService;
	}

	public void setCourierService(CourierService courierService) {
		this.courierService = courierService;
	}

	public PincodeRegionZone getPincodeRegionZone() {
		return pincodeRegionZone;
	}

	public void setPincodeRegionZone(PincodeRegionZone pincodeRegionZone) {
		this.pincodeRegionZone = pincodeRegionZone;
	}

	public List<PincodeRegionZone> getPincodeRegionZoneList() {
		return pincodeRegionZoneList;
	}

	public void setPincodeRegionZoneList(List<PincodeRegionZone> pincodeRegionZoneList) {
		this.pincodeRegionZoneList = pincodeRegionZoneList;
	}

	public List<Pincode> getPincodeList() {
		return pincodeList;
	}

}