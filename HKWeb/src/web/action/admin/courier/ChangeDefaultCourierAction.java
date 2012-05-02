package web.action.admin.courier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.courier.CourierServiceInfoDao;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.courier.PincodeDao;
import com.hk.dao.warehouse.WarehouseDao;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.service.PincodeService;
import com.hk.util.XslGenerator;

@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_ORDERS })
@Component
public class ChangeDefaultCourierAction extends BaseAction {

    PincodeDao                          pincodeDao;

    private PincodeService              pincodeService;

    CourierServiceInfoDao               courierServiceInfoDao;

    WarehouseDao                        warehouseDao;

    XslGenerator                        xslGenerator;

    // @Named(Keys.Env.adminDownloads)
    String                              adminDownloadsPath;

    // @Named(Keys.Env.adminUploads)
    String                              adminUploadsPath;

    XslParser                           xslParser;

    FileBean                            fileBean;

    private static Logger               logger                 = LoggerFactory.getLogger(ChangeDefaultCourierAction.class);
    private Long                        pincodesInSystem       = 0L;
    private String                      pincodeString;
    private PincodeDefaultCourier       pincodeDefaultCourier;
    private Pincode                     pincode;
    private List<CourierServiceInfo>    courierServiceList     = new ArrayList<CourierServiceInfo>();
    private List<PincodeDefaultCourier> pincodeDefaultCouriers = new ArrayList<PincodeDefaultCourier>();
    private Warehouse                   warehouse;
    private Double                      estimatedShippingCostCod;
    private Double                      estimatedShippingCostNonCod;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
    }

    public Resolution search() {
        try {
            pincode = pincodeDao.getByPincode(pincodeString);
            pincodeDefaultCouriers = getPincodeService().getByPincode(pincode);
            if (!pincodeDefaultCouriers.isEmpty()) {

                courierServiceList = courierServiceInfoDao.getCourierServicesForPinCode(pincodeString);
                return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
            } else {
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
        pincodeDao.save(pincodeDefaultCourier);

        addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
    }

    public Resolution add_pincode() {
        PincodeDefaultCourier pincodeDefaultCourierNew;
        pincode = pincodeDao.getByPincode(pincodeString);
        warehouse = pincodeDefaultCourier.getWarehouse();
        Courier codCourier = pincodeDefaultCourier.getCodCourier();
        Courier nonCodCourier = pincodeDefaultCourier.getNonCodCourier();
        pincodeDefaultCourierNew = getPincodeService().getByPincodeWarehouse(pincode, warehouse);
        if (pincodeDefaultCourierNew != null) {
            addRedirectAlertMessage(new SimpleMessage("Default courier for destination pincode already exists for given warehouse"));
            return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
        } else {
            if (pincode != null) {
                pincodeDefaultCourierNew = new PincodeDefaultCourier();
                pincodeDefaultCourierNew.setWarehouse(warehouse);
                pincodeDefaultCourierNew.setPincode(pincode);
                pincodeDefaultCourierNew.setCodCourier(codCourier);
                pincodeDefaultCourierNew.setNonCodCourier(nonCodCourier);
                pincodeDefaultCourierNew.setEstimatedShippingCostCod(pincodeDefaultCourier.getEstimatedShippingCostCod());
                pincodeDefaultCourierNew.setEstimatedShippingCostNonCod(pincodeDefaultCourier.getEstimatedShippingCostNonCod());
                pincodeDao.save(pincodeDefaultCourierNew);
            } else {
                addRedirectAlertMessage(new SimpleMessage("Pincode does not exists in the system."));
                return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        return new ForwardResolution("/pages/admin/changeDefaultCourier.jsp");
    }

    /*
     * public Resolution generatePincodeExcel() throws Exception { List<Pincode> pincodeList = new ArrayList<Pincode>();
     * pincodeList = pincodeDao.listAll(); String excelFilePath = adminDownloadsPath + "/pincodeExcelFiles/pincodes" +
     * System.currentTimeMillis() + ".xls"; final File excelFile = new File(excelFilePath);
     * xslGenerator.generatePincodeXsl(pincodeList, excelFilePath); addRedirectAlertMessage(new SimpleMessage("Downlaod
     * complete")); return new Resolution() { public void execute(HttpServletRequest req, HttpServletResponse res)
     * throws Exception { OutputStream out = null; InputStream in = new BufferedInputStream(new
     * FileInputStream(excelFile)); res.setContentLength((int) excelFile.length()); res.setHeader("Content-Disposition",
     * "attachment; filename=\"" + excelFile.getName() + "\";"); out = res.getOutputStream(); // Copy the contents of
     * the file to the output stream byte[] buf = new byte[4096]; int count = 0; while ((count = in.read(buf)) >= 0) {
     * out.write(buf, 0, count); } } }; }
     */

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
                PincodeDefaultCourier existingDefaultCourierObject = getPincodeService().getByPincodeWarehouse(defaultPincode.getPincode(), defaultPincode.getWarehouse());
                if (defaultPincode != null) {
                    if (existingDefaultCourierObject == null) {
                        pincodeDao.save(defaultPincode);
                        logger.info("inserting:" + defaultPincode.getPincode().getPincode());
                    } else {
                        existingDefaultCourierObject.setNonCodCourier(defaultPincode.getNonCodCourier());
                        existingDefaultCourierObject.setCodCourier(defaultPincode.getCodCourier());
                        existingDefaultCourierObject.setEstimatedShippingCostCod(defaultPincode.getEstimatedShippingCostCod());
                        existingDefaultCourierObject.setEstimatedShippingCostNonCod(defaultPincode.getEstimatedShippingCostNonCod());
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

    public void setPincodeDefaultCouriers(List<PincodeDefaultCourier> addresses) {
        this.pincodeDefaultCouriers = pincodeDefaultCouriers;
    }

    public PincodeDefaultCourier getPincodeDefaultCourier() {
        return pincodeDefaultCourier;
    }

    public void setPincodeDefaultCourier(PincodeDefaultCourier pincodeDefaultCourier) {
        this.pincodeDefaultCourier = pincodeDefaultCourier;
    }

    public Double getEstimatedShippingCostCod() {
        return estimatedShippingCostCod;
    }

    public void setEstimatedShippingCostCod(Double estimatedShippingCostCod) {
        this.estimatedShippingCostCod = estimatedShippingCostCod;
    }

    public Double getEstimatedShippingCostNonCod() {
        return estimatedShippingCostNonCod;
    }

    public void setEstimatedShippingCostNonCod(Double estimatedShippingCostNonCod) {
        this.estimatedShippingCostNonCod = estimatedShippingCostNonCod;
    }

    public PincodeService getPincodeService() {
        return pincodeService;
    }

    public void setPincodeService(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }
    
    
}