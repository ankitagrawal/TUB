package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.util.helper.XslAwbParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.DuplicateAwbexception;
import com.hk.pact.service.UserService;
import com.hk.util.XslGenerator;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CourierAWBAction extends BaseAction {
    @Autowired
    XslGenerator xslGenerator;
    @Autowired
    AwbService awbService;
    @Autowired
    UserService userService;
    @Autowired
    XslAwbParser xslAwbParser;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    private Courier courier;
    private AwbStatus awbStatus;


    FileBean fileBean;

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @DefaultHandler
    @DontValidate
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/updateCourierAWB.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
    public Resolution generateCourierAWBExcel() throws Exception {
        if (courier == null) {
            addRedirectAlertMessage(new SimpleMessage("Select Courier"));
            return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
        }
        String selectedStatus = awbStatus.getStatus();
        Warehouse warehouse = userService.getWarehouseForLoggedInUser();
        List<Awb> unusedAwbListFromDb = awbService.getAvailableAwbListForCourierByWarehouseCodStatus(courier, null, warehouse, null, awbStatus);
        String excelFilePath = adminDownloadsPath + "/courierExcelFiles/Courier_" + selectedStatus + ".xls";
        final File excelFile = new File(excelFilePath);
        xslAwbParser.generateAwbExcel(unusedAwbListFromDb, excelFile);
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

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
    public Resolution uploadCourierAWBExcel() {
        if ((courier == null) || (fileBean == null)) {
            addRedirectAlertMessage(new SimpleMessage("Select Courier  and choose file to upload"));
            return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
        }
        String excelFilePath = adminUploadsPath + "/courierFiles/" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        List<Awb> awbListFromExcel = null;
        Map<Courier, List<String>> courierIdAwbnumberMap;
        try {
            fileBean.save(excelFile);
            awbListFromExcel = xslAwbParser.readAwbExcel(excelFile);

            if (null != awbListFromExcel && awbListFromExcel.size() > 0) {
                courierIdAwbnumberMap = xslAwbParser.getCourierWithAllAwbsInExcel();
                List<Awb> alreadyExstingAwbInDbList = new ArrayList<Awb>();
                // Hit DB for every pair in Map <courier,list of AWBs for this courier in excel>
                for (Courier couriern : courierIdAwbnumberMap.keySet()) {
                    List<Awb> courierAwbList = awbService.getAlreadyPresentAwb(couriern, courierIdAwbnumberMap.get(couriern));
                    if (courierAwbList != null && courierAwbList.size() > 0) {
                        alreadyExstingAwbInDbList.addAll(courierAwbList);
                    }
                }

                if (alreadyExstingAwbInDbList.size() > 0) {
                    for (Awb anAlreadyExstingAwbInDbList : alreadyExstingAwbInDbList) {
                        awbListFromExcel.remove(anAlreadyExstingAwbInDbList);
                    }

                }
                for (Awb awb : awbListFromExcel) {
                    awbService.save(awb, null);

                }
                addRedirectAlertMessage(new SimpleMessage("database updated"));
                if (alreadyExstingAwbInDbList.size() > 0) {
                    addRedirectAlertMessage(new SimpleMessage("Upload Failed   for below listed  " + alreadyExstingAwbInDbList.size() + " Awb records. They are already present in database"));
                    for (Awb awb : alreadyExstingAwbInDbList) {
                        addRedirectAlertMessage(new SimpleMessage("Awb Number :: " + awb.getAwbNumber() + " ,  Courier Id  ::  " + awb.getCourier().getId()));
                    }

                }
                return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
            } else {

                addRedirectAlertMessage(new SimpleMessage("Empty Excel Sheet"));
                return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");

            }

        }
        catch (DuplicateAwbexception dup) {
            addRedirectAlertMessage(new SimpleMessage("ERROR IN UPLOADING ::::::::  "+dup.getMessage() + " AWB_Number  : " + dup.getAwbNumber() + "  is present in Excel twice for Courier ::   " + dup.getCourier().getId()));
            return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
        }
        catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage("ERROR IN UPLOADING"+ex.getMessage()));
            addRedirectAlertMessage(new SimpleMessage("ERROR IN UPLOADING file"));
            return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
        }

        finally {
            excelFile.delete();
            courierIdAwbnumberMap = null;
        }
    }


    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
    public Resolution uploadCourierAWBExcelForDelete() {
        if (fileBean == null) {
            addRedirectAlertMessage(new SimpleMessage("Select choose file to upload"));
            return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
        }

        String excelFilePath = adminUploadsPath + "/courierFiles/" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        List<Awb> awbListFromExcel = null;
        List<Awb> awbListToBeDeleted = new ArrayList<Awb>();
        List<Awb> wrongEntriesInExcel = new ArrayList<Awb>();

        try {
            fileBean.save(excelFile);
            awbListFromExcel = xslAwbParser.readAwbExcel(excelFile);
            if (awbListFromExcel != null && awbListFromExcel.size() > 0) {
                for (Awb awb : awbListFromExcel) {
                   Awb awbFromDb = awbService.isAwbEligibleForDeletion(awb.getCourier(), awb.getAwbNumber(), awb.getWarehouse(), awb.getCod());
                    if (awbFromDb != null) {
                        awbListToBeDeleted.add(awbFromDb);
                    } else {
                        wrongEntriesInExcel.add(awb);
                    }
                }

                for (Awb awbDelete : awbListToBeDeleted) {
                    awbService.delete(awbDelete);
                }
                addRedirectAlertMessage(new SimpleMessage("database updated"));

                //Awb which cannot be deleted
                if (wrongEntriesInExcel.size() > 0) {
                    addRedirectAlertMessage(new SimpleMessage("Deletion Failed :::  Below wrong can not be deleted"));
                    for (Awb awb : wrongEntriesInExcel) {
                        addRedirectAlertMessage(new SimpleMessage("Awb Number :: " + awb.getAwbNumber() + " ,  Courier Id  ::  " + awb.getCourier().getId()));
                    }
                }

            } else {
                addRedirectAlertMessage(new SimpleMessage("Empty Excel Sheet"));
            }
        } catch (DuplicateAwbexception dup) {
            addRedirectAlertMessage(new SimpleMessage(dup.getMessage() + " AWB_Number  : " + dup.getAwbNumber() + "  is present in Excel twice for Courier ::   " + dup.getCourier().getId()));

        }
        catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage("Either Awb is attached with shipment or AWB doesnot exists"));
        }
        return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
    }


    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public void setAwbStatus(AwbStatus awbStatus) {
        this.awbStatus = awbStatus;
    }
}
