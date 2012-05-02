package web.action.admin.courier;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.courier.CourierServiceInfoDao;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.courier.PincodeDao;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.util.XslGenerator;

@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_ORDERS })
@Component
public class MasterPincodeAction extends BaseAction {

    PincodeDao                       pincodeDao;

    CourierServiceInfoDao            courierServiceInfoDao;

    XslGenerator                     xslGenerator;

    // @Named(Keys.Env.adminDownloads)
    @Value("#{hkEnvProps['adminDownloads']}")
    String                           adminDownloadsPath;

    // @Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['adminUploads']}")
    String                           adminUploadsPath;

    XslParser                        xslParser;

    FileBean                         fileBean;

    private static Logger            logger             = LoggerFactory.getLogger(MasterPincodeAction.class);
    private Long                     pincodesInSystem   = 0L;
    private String                   pincodeString;
    private Pincode                  pincode;
    private List<CourierServiceInfo> courierServiceList = new ArrayList<CourierServiceInfo>();

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

                courierServiceList = courierServiceInfoDao.getCourierServicesForPinCode(pincodeString);
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
        if (pincode == null || StringUtils.isBlank(pincode.getPincode()) || StringUtils.isBlank(pincode.getCity()) || StringUtils.isBlank(pincode.getState())
                || pincode.getPincode().length() < 6 || (!StringUtils.isNumeric(pincode.getPincode()))) {
            addRedirectAlertMessage(new SimpleMessage("Enter values correctly."));
            return new RedirectResolution(MasterPincodeAction.class);
        }
        Pincode pincodeByCode = pincodeDao.getByPincode(pincode.getPincode());
        if (pincodeByCode != null && pincode.getId() == null) {
            pincodeByCode.setLocality(pincode.getLocality());
            pincodeByCode.setCity(pincode.getCity());
            pincodeByCode.setState(pincode.getState());
            pincodeByCode.setDefaultCourier(pincode.getDefaultCourier());
            pincodeDao.save(pincodeByCode);
        } else {
            pincodeDao.save(pincode);
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        return new RedirectResolution(CourierServiceInfoAction.class).addParameter("pincode", pincode.getPincode());
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
}