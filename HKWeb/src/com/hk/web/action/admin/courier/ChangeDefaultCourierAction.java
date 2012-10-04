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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.warehouse.WarehouseDaoImpl;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.core.PincodeService;
import com.hk.util.XslGenerator;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS})
@Component
public class ChangeDefaultCourierAction extends BaseAction {
    @Autowired
    PincodeDao pincodeDao;
    @Autowired
    private PincodeService pincodeService;
    @Autowired
    CourierServiceInfoDao courierServiceInfoDao;
    @Autowired
    CourierService courierService;
    @Autowired
    WarehouseDaoImpl warehouseDao;
    @Autowired
    XslGenerator xslGenerator;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminDownloadsPath;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    @Autowired
    XslParser xslParser;

    FileBean fileBean;

    private static Logger logger = LoggerFactory.getLogger(ChangeDefaultCourierAction.class);
    private Long pincodesInSystem = 0L;
    private String pincodeString;
    private PincodeDefaultCourier pincodeDefaultCourier;
    private Pincode pincode;
    private List<CourierServiceInfo> courierServiceList = new ArrayList<CourierServiceInfo>();
    private List<PincodeDefaultCourier> pincodeDefaultCouriers = new ArrayList<PincodeDefaultCourier>();
    private Warehouse warehouse;    


    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
    }

    public Resolution search() {
        try {
            pincode = pincodeDao.getByPincode(pincodeString);
            pincodeDefaultCouriers = getPincodeService().searchPincodeDefaultCourierList(pincode,null, pincodeDefaultCourier.isCod(), pincodeDefaultCourier.isGroundShipping());
//             pincodeDefaultCourier = getPincodeService().searchPincodeDefaultCourier(pincode,null, pincodeDefaultCourier.isCod(), pincodeDefaultCourier.isGroundShipping());
            if (pincodeDefaultCourier != null) {
                courierServiceList = courierService.getCourierServiceInfoList(null,pincodeString, false, false, false);
                return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
            } else {
                addRedirectAlertMessage(new SimpleMessage("Pincode does not exist for selected combination"));
                return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
            }

            // return new RedirectResolution(MasterPincodeAction.class).addParameter("pincode", pincode.getId());
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
        }
        addRedirectAlertMessage(new SimpleMessage("No such pincode in system"));
        return new RedirectResolution(ChangeDefaultCourierAction.class);
    }

    public Resolution save() {
//        PincodeDefaultCourier pincodeDefaultCourierNew = getPincodeService().getByPincodeWarehouse(pincodeDefaultCourier.getPincode(), pincodeDefaultCourier.getWarehouse(), pincodeDefaultCourier.isCod(), pincodeDefaultCourier.isGroundShipping());
       PincodeDefaultCourier pincodeDefaultCourierNew = getPincodeService().searchPincodeDefaultCourier(pincodeDefaultCourier.getPincode(), pincodeDefaultCourier.getWarehouse(), pincodeDefaultCourier.isCod(), pincodeDefaultCourier.isGroundShipping());
        if (pincodeDefaultCourierNew != null) {
            pincodeDefaultCourierNew.setCourier(pincodeDefaultCourier.getCourier());
            pincodeDefaultCourierNew.setEstimatedShippingCost(pincodeDefaultCourier.getEstimatedShippingCost());
            pincodeDao.save(pincodeDefaultCourierNew);
            addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        }

        //Todo:
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");

    }


    public Resolution add_pincode() {
        PincodeDefaultCourier pincodeDefaultCourierNew;
        pincode = pincodeDao.getByPincode(pincodeString);
        warehouse = pincodeDefaultCourier.getWarehouse();
        Courier courier = pincodeDefaultCourier.getCourier();
//        Courier nonCodCourier = pincodeDefaultCourier.getNonCodCourier();
         pincodeDefaultCourierNew = getPincodeService().searchPincodeDefaultCourier(pincode, warehouse, pincodeDefaultCourier.isCod(), pincodeDefaultCourier.isGroundShipping());
        if (pincodeDefaultCourierNew != null) {
            addRedirectAlertMessage(new SimpleMessage("Default courier for destination pincode already exists for given warehouse"));
            return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
        } else {
            if (pincode != null) {
                pincodeDefaultCourierNew = new PincodeDefaultCourier();
                pincodeDefaultCourierNew.setWarehouse(warehouse);
                pincodeDefaultCourierNew.setPincode(pincode);
                pincodeDefaultCourierNew.setCourier(courier);
                pincodeDefaultCourierNew.setGroundShipping(pincodeDefaultCourier.isGroundShipping());
                pincodeDefaultCourierNew.setCod(pincodeDefaultCourier.isCod());
                pincodeDefaultCourierNew.setEstimatedShippingCost(pincodeDefaultCourier.getEstimatedShippingCost());
                pincodeDao.save(pincodeDefaultCourierNew);
            } else {
                addRedirectAlertMessage(new SimpleMessage("Pincode does not exists in the system."));
                return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
    }

    public Resolution generatePincodeExcel() throws Exception {
        List<PincodeDefaultCourier> pincodeDefaultCourierList = new ArrayList<PincodeDefaultCourier>();
        warehouse = pincodeDefaultCourier.getWarehouse();
//        get(PincodeDefaultCourier.class, variantId);

//        pincodeDefaultCourierList = pincodeDao.getAll(PincodeDefaultCourier.class);
          pincodeDefaultCourierList= pincodeDao.searchPincodeDefaultCourierList(null,warehouse,null,null) ;

        String excelFilePath = adminDownloadsPath + "/pincodeExcelFiles/pincodesDefaultCouriers_" + System.currentTimeMillis() + ".xls";
        final File excelFile = new File(excelFilePath);

        xslGenerator.generatePincodeDefaultCourierXsl(pincodeDefaultCourierList, excelFilePath);
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
            addRedirectAlertMessage(new SimpleMessage("Please chose a file"));
            return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
        }
        String excelFilePath = adminUploadsPath + "/pincodeExcelFiles/defaultpincodes" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            Set<PincodeDefaultCourier> defaultPincodes = xslParser.readDefaultPincodeList(excelFile);
            for (PincodeDefaultCourier defaultPincode : defaultPincodes) {
//                PincodeDefaultCourier existingDefaultCourierObject = getPincodeService().getByPincodeWarehouse(defaultPincode.getPincode(), defaultPincode.getWarehouse());
                PincodeDefaultCourier existingDefaultCourierObject = getPincodeService().searchPincodeDefaultCourier(defaultPincode.getPincode(), defaultPincode.getWarehouse(), defaultPincode.isCod(), defaultPincode.isGroundShipping());
                if (defaultPincode != null) {
                    if (existingDefaultCourierObject == null) {
                        pincodeDao.save(defaultPincode);
                        logger.info("inserting:" + defaultPincode.getPincode().getPincode());
                    } else {
                        existingDefaultCourierObject.setCourier(defaultPincode.getCourier());
                        existingDefaultCourierObject.setCod(defaultPincode.isCod());
                        existingDefaultCourierObject.setGroundShipping(defaultPincode.isGroundShipping());
                        existingDefaultCourierObject.setEstimatedShippingCost(defaultPincode.getEstimatedShippingCost());
                        pincodeDao.save(existingDefaultCourierObject);
                        logger.info("updating:" + defaultPincode.getPincode().getPincode());
                    }

                }
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
            return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
        }

        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
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

    public List<PincodeDefaultCourier> getPincodeDefaultCouriers() {
        return pincodeDefaultCouriers;
    }

    //TODO: please explain what is this
    public void setPincodeDefaultCouriers(List<PincodeDefaultCourier> addresses) {
        this.pincodeDefaultCouriers = pincodeDefaultCouriers;
    }

    public PincodeDefaultCourier getPincodeDefaultCourier() {
        return pincodeDefaultCourier;
    }

    public void setPincodeDefaultCourier(PincodeDefaultCourier pincodeDefaultCourier) {
        this.pincodeDefaultCourier = pincodeDefaultCourier;
    }

    public PincodeService getPincodeService() {
        return pincodeService;
    }

    public void setPincodeService(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    public CourierService getCourierService() {
        return courierService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }
}