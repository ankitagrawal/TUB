package com.hk.web.action.admin.inventory;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.impl.dao.ReconciliationStatusDaoImpl;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.SALES_REPORT }, authActionBean = AdminPermissionAction.class)
@Component
public class ReconciliationAction extends BaseAction {

    @Autowired
    XslParser                   xslParser;
    @Autowired
    ReconciliationStatusDaoImpl reconciliationStatusDao;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                      adminUploadsPath;

    @Validate(required = true)
    FileBean                    fileBean;

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/updateReconciliation.jsp");
    }

    public Resolution parse() throws Exception {
        /*
         * String excelFilePath = adminUploadsPath + "/reconciledFiles/" + System.currentTimeMillis() + ".xls"; File
         * excelFile = new File(excelFilePath); excelFile.getParentFile().mkdirs(); fileBean.save(excelFile); try {
         * HashMap<ReconciliationStatus, List<String>> map = xslParser.readAndUpdateReconciliationStatus(excelFile);
         * ReconciliationStatus done = reconciliationStatusDao.find(EnumReconciliationStatus.DONE.getId()); if
         * (!map.get(done).isEmpty()) orderDao.updateReconciliationStatus(done, map.get(done)); ReconciliationStatus
         * pending = reconciliationStatusDao.find(EnumReconciliationStatus.PENDING.getId()); if
         * (!map.get(pending).isEmpty()) orderDao.updateReconciliationStatus(pending, map.get(pending));
         * addRedirectAlertMessage(new SimpleMessage("Reconciliation Status Updated Successfully.")); } catch (Exception
         * e) { logger.error("Exception while reading excel sheet.", e); addRedirectAlertMessage(new
         * SimpleMessage("Upload failed - " + e.getMessage())); return new
         * RedirectResolution(ReconciliationAction.class); } //excelFile.delete(); return new
         * RedirectResolution(ReconciliationAction.class);
         */

        // TODO: # warehouse fix this.
        return null;
    }

}