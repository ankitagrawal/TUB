package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.courier.PincodeRegionZoneService;
import com.hk.admin.util.helper.XslPincodeParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.PincodeService;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS})
@Component
public class MasterPincodeAction extends BaseAction {
    @Autowired
    CourierService courierService;
    @Autowired
    PincodeRegionZoneService pincodeRegionZoneService;
    @Autowired
    PincodeService pincodeService;
    @Autowired
    BaseDao baseDao;
    @Autowired
    XslPincodeParser xslPincodeParser;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminDownloadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    FileBean fileBean;

    private static Logger logger = LoggerFactory.getLogger(MasterPincodeAction.class);
    private String pincodeString;
    private Pincode pincode;
    private PincodeRegionZone pincodeRegionZone;
    private List<PincodeRegionZone> pincodeRegionZoneList = null;
    private List<Pincode> pincodeList;


    List<PincodeCourierMapping> pincodeCourierMappings;
    Map<String, Boolean> applicableShipmentServices = new HashMap<String, Boolean>();

    @Autowired
    PincodeCourierService pincodeCourierService;


    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchAndAddPincodes.jsp");
    }

    @DontValidate
    public Resolution search() {
        pincode = pincodeService.getByPincode(pincodeString);
        if (pincode != null) {
            pincodeCourierMappings = pincodeCourierService.getApplicablePincodeCourierMappingList(pincode, null, null, true);
            applicableShipmentServices = pincodeCourierService.generateDetailedAnalysis(pincodeCourierMappings);
        } else {
            addRedirectAlertMessage(new SimpleMessage("No such pincode in system"));
        }
        return new ForwardResolution("/pages/admin/searchAndAddPincodes.jsp");
    }

    public Resolution save() {
        Pincode dbPincode = pincodeService.getByPincode(pincodeString);
        if (dbPincode != null && !dbPincode.getCity().equals(pincode.getCity())) {
            pincode = pincodeService.save(pincode);
            pincodeRegionZoneService.assignPincodeRegionZoneToPincode(pincode);
            addRedirectAlertMessage(new SimpleMessage("Assigned Region Zone !!!!"));
            return new RedirectResolution(MasterPincodeAction.class, "searchPincodeRegion").addParameter("pincodeString", pincode.getPincode());
        } else if(dbPincode==null){
          pincode = pincodeService.save(pincode);
          addRedirectAlertMessage(new SimpleMessage("Pincode changes saved Successfully"));
          return new RedirectResolution(MasterPincodeAction.class, "search").addParameter("pincodeString", pincode.getPincode());
          }
          else {
        }
            addRedirectAlertMessage(new SimpleMessage("Pincode already exist in the Database!!!"));
            return new RedirectResolution(MasterPincodeAction.class, "search").addParameter("pincodeString", pincode.getPincode());
    }

    public Resolution generatePincodeExcel() throws Exception {
        List<Pincode> pincodeList = baseDao.getAll(Pincode.class);
        String excelFilePath = adminDownloadsPath + "/pincodeExcelFiles/pincodes" + System.currentTimeMillis() + ".xls";
        final File excelFile = new File(excelFilePath);

        xslPincodeParser.generatePincodeXsl(pincodeList, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Download Complete"));
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
            Set<Pincode> pincodeSet = xslPincodeParser.readPincodeList(excelFile);
            for (Pincode pincode : pincodeSet) {
                pincodeService.save(pincode);
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

  public Resolution directToPincodeRegionZone(){
    return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
  }
    public Resolution savePincodeRegionList() {
        for (PincodeRegionZone pincodeRegionZone : pincodeRegionZoneList) {
            pincodeRegionZoneService.saveOrUpdate(pincodeRegionZone);
        }
        addRedirectAlertMessage(new SimpleMessage("Pincode Region saved"));
        return new RedirectResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    public Resolution savePincodeRegion() {
       if(pincodeRegionZone!=null){
        if (pincodeRegionZone.getPincode() == null) {
            addRedirectAlertMessage(new SimpleMessage("Pincode does not exist in System"));
        } else {
            try {
                PincodeRegionZone pincodeRegionZoneDb = pincodeRegionZoneService.getPincodeRegionZone(pincodeRegionZone.getCourierGroup(), pincode, pincodeRegionZone.getWarehouse());
                if (pincodeRegionZoneDb != null) {
                    pincodeRegionZoneDb.setRegionType(pincodeRegionZone.getRegionType());
                } else {
                    pincodeRegionZone.setPincode(pincode);
                    pincodeRegionZoneDb = pincodeRegionZone;
                }
                pincodeRegionZoneService.save(pincodeRegionZoneDb);
            } catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage("EXCEPTION IN SAVING" + ex.getMessage()));
                return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
            }
            addRedirectAlertMessage(new SimpleMessage("Pincode region saved"));
        }
    }
        return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    public Resolution searchPincodeRegion() {
        Pincode pincode = pincodeRegionZone.getPincode();
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

  public List<PincodeCourierMapping> getPincodeCourierMappings() {
    return pincodeCourierMappings;
  }

  public void setPincodeCourierMappings(List<PincodeCourierMapping> pincodeCourierMappings) {
    this.pincodeCourierMappings = pincodeCourierMappings;
  }

  public Map<String, Boolean> getApplicableShipmentServices() {
    return applicableShipmentServices;
  }

  public void setApplicableShipmentServices(Map<String, Boolean> applicableShipmentServices) {
    this.applicableShipmentServices = applicableShipmentServices;
  }
}