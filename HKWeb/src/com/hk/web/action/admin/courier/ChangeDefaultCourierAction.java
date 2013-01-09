package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.util.helper.XslPincodeParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.courier.Courier;
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
import java.util.List;
import java.util.Set;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS})
@Component
public class ChangeDefaultCourierAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(ChangeDefaultCourierAction.class);

    @Autowired
    private PincodeService pincodeService;
    @Autowired
    XslPincodeParser xslPincodeParser;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    CourierService courierService;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminDownloadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    FileBean fileBean;

    private String pincodeString;
    private Pincode pincode;
    private List<PincodeDefaultCourier> pincodeDefaultCouriers;
    private List<PincodeCourierMapping> pincodeCourierMappings;
    private List<Warehouse> allWarehouse;
    private List<Courier> allCourier;

    Warehouse warehouse;
    boolean isCod;
    boolean isGround;

    @Autowired
    PincodeCourierService pincodeCourierService;
    /*
    search by pincode --> get 8 entries  --> plus show all available PDC mapping (related to
    update either of those 8 entries
    add a new entry
    @validation method, that new/update entry should be in pincode courier mapping
    download/upload pincode default courier excel
    now when i add/update PDC, i have isCod/isGround, i see in PCM, if for pin, for respective isCod && isGround, entry exists or not
     */

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
    }

    public Resolution search() {
        pincode = pincodeService.getByPincode(pincodeString);
        if (pincode == null) {
            addRedirectAlertMessage(new SimpleMessage("No such pincode in system"));
            return new RedirectResolution(ChangeDefaultCourierAction.class);
        } else {
            pincodeDefaultCouriers = pincodeService.searchPincodeDefaultCourierList(pincode, warehouse, isCod, isGround);
            pincodeCourierMappings = pincodeCourierService.getApplicablePincodeCourierMappingList(pincode, isCod, isGround, true);
        }
        allWarehouse = getWarehouseService().getAllWarehouses();
        allCourier = getCourierService().getAllCouriers();
        return new ForwardResolution("/pages/courier/changeDefaultCourierAction.jsp");
    }

    public Resolution save() {
        for (PincodeDefaultCourier defaultCourier : pincodeDefaultCouriers) {
            if (!pincodeCourierService.isDefaultCourierApplicable(pincode, defaultCourier.getCourier(), defaultCourier.isGroundShipping(), defaultCourier.isCod())) {
                 addRedirectAlertMessage(new SimpleMessage("One of the mappings is currently not serviceable"));
                 break;
            }
        }
        for (PincodeDefaultCourier defaultCourier : pincodeDefaultCouriers) {
            getBaseDao().save(defaultCourier);
        }
        allWarehouse = getWarehouseService().getAllWarehouses();
        allCourier = getCourierService().getAllCouriers();
       addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
    }

    public Resolution generatePincodeExcel() throws Exception {
        pincodeDefaultCouriers = pincodeService.searchPincodeDefaultCourierList(pincode, warehouse, isCod, isGround);
        String excelFilePath = adminDownloadsPath + "/pincodeExcelFiles/pincodesDefaultCouriers_" + System.currentTimeMillis() + ".xls";
        final File excelFile = new File(excelFilePath);

        xslPincodeParser.generatePincodeDefaultCourierXsl(pincodeDefaultCouriers, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Download complete"));
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
        allWarehouse = getWarehouseService().getAllWarehouses();
        allCourier = getCourierService().getAllCouriers();
        if (fileBean == null) {
            addRedirectAlertMessage(new SimpleMessage("Please chose a file"));
            return new ForwardResolution("/pages/courier/changeDefaultCourierAction.jsp");
        }
        String excelFilePath = adminUploadsPath + "/pincodeExcelFiles/defaultpincodes" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        try {
            Set<PincodeDefaultCourier> pincodeDefaultCourierSet = xslPincodeParser.readDefaultPincodeList(excelFile);
            for (PincodeDefaultCourier defaultCourier : pincodeDefaultCourierSet) {
                getBaseDao().save(defaultCourier);
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
            return new ForwardResolution("/pages/courier/changeDefaultCourierAction.jsp");
        }
        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/courier/changeDefaultCourierAction.jsp");
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

    public List<PincodeDefaultCourier> getPincodeDefaultCouriers() {
        return pincodeDefaultCouriers;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public boolean isCod() {
        return isCod;
    }

    public void setCod(boolean cod) {
        isCod = cod;
    }

    public boolean isGround() {
        return isGround;
    }

    public void setGround(boolean ground) {
        isGround = ground;
    }

    public List<PincodeCourierMapping> getPincodeCourierMappings() {
        return pincodeCourierMappings;
    }

    public void setPincodeCourierMappings(List<PincodeCourierMapping> pincodeCourierMappings) {
        this.pincodeCourierMappings = pincodeCourierMappings;
    }

  public List<Warehouse> getAllWarehouse() {
    return allWarehouse;
  }

  public void setAllWarehouse(List<Warehouse> allWarehouse) {
    this.allWarehouse = allWarehouse;
  }

  public List<Courier> getAllCourier() {
    return allCourier;
  }

  public void setAllCourier(List<Courier> allCourier) {
    this.allCourier = allCourier;
  }

  public CourierService getCourierService() {
    return courierService;
  }

  public WarehouseService getWarehouseService() {
    return warehouseService;
  }
}