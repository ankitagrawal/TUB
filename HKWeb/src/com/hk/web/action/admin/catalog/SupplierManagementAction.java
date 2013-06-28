package com.hk.web.action.admin.catalog;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.accounting.SupplierTransactionService;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.courier.StateList;
import com.hk.constants.inventory.EnumSupplierTransactionType;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.util.XslGenerator;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.Date;

@Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class SupplierManagementAction extends BasePaginatedAction {

    private static Logger logger = Logger.getLogger(SupplierManagementAction.class);

    File xlsFile;
    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloads;

    @Autowired
    SupplierDao supplierDao;

    @Autowired
    XslGenerator xslGenerator;

    @Autowired
    SupplierTransactionService supplierTransactionService;

    private List<Supplier> supplierList = new ArrayList<Supplier>();
    private Supplier supplier;
    public static final int LenghtOfTIN = 11;
    private String supplierTin;
    private String supplierName;
    private Boolean status;

    Page supplierPage;
    private Integer defaultPerPage = 30;

    @ValidationMethod(on = "pre")
    public void validateSupplierTinNumber() {
        if (!StringUtils.isBlank(supplierTin)) {
            if (!supplierDao.doesTinNumberExist(supplierTin)) {
                getContext().getValidationErrors().add("1", new SimpleError("No supplier exists with the entered Tin Number!"));
            }
        }
    }

    @DefaultHandler
    public Resolution pre() {
	    supplierPage = supplierDao.getSupplierByTinAndName(supplierTin, supplierName, status, getPageNo(), getPerPage());
        supplierList = supplierPage.getList();
        return new ForwardResolution("/pages/admin/supplierList.jsp");
    }

    public Resolution createOrEdit() {
        logger.debug("supplier: " + supplier);
        return new ForwardResolution("/pages/admin/supplier.jsp");
    }

    @ValidationMethod(on = "save")
    public void validateSaveSupplier() {
        String regex = "^0-9$";
        if (supplier != null) {
            if (supplier.getState() != null && supplier.getTinNumber() != null) {

                if (supplier.getTinNumber().length() != LenghtOfTIN) {
                    getContext().getValidationErrors().add("e1", new SimpleError("TIN should be of 11 digits"));
                }
                if (!(supplier.getTinNumber().substring(0, 2).equals(StateList.stateMapTIN.get(supplier.getState())))) {
                    getContext().getValidationErrors().add("e1", new SimpleError("check the first two digits of TIN"));
                }
            }

        //validation for the margins
        /*if(supplier.getMargins() !=null){
            //Validating for entering only valid double values
            Pattern pattern;


            String margin = supplier.getMargins();
            final String DOUBLE_PATTERN = "^\\d+\\.?\\d*$";
            pattern = Pattern.compile(DOUBLE_PATTERN);
            boolean bool=pattern.matcher(margin).matches();
            if(!bool)  getContext().getValidationErrors().add("e1", new SimpleError("Please Enter the Margins in percent"));
                //To check the value in range of percentage
            else{
                double d = Double.valueOf(supplier.getMargins().trim()).doubleValue();
                if(d < 0 || d > 100)
                    getContext().getValidationErrors().add("e1", new SimpleError("Margins is in percentage and must be Enter within the Range(0 to 100)"));
                else if(bool){
                    int x = margin.indexOf(".");
                    if(x==(margin.length()-1))
                        getContext().getValidationErrors().add("e1", new SimpleError("Please Enter the Margins in percent"));
                }
            }
        }*/


            // Validation for the Email Id
            if (supplier.getEmail_id() != null) {
                Pattern pattern;
                String email_id = supplier.getEmail_id();
                final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                pattern = Pattern.compile(EMAIL_PATTERN);
                boolean bool = pattern.matcher(email_id).matches();
                if (!bool)
                    getContext().getValidationErrors().add("e1", new SimpleError("Please enter the valid Email-Id"));
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please enter values."));
        }
    }

	@Secure(hasAnyRoles = { RoleConstants.PO_APPROVER }, authActionBean = AdminPermissionAction.class)
    public Resolution save() {
        if (supplier != null) {
            Supplier oldSupplier = supplierDao.findByTIN(supplier.getTinNumber());
            if (oldSupplier == null) {
                if (checkForMandatoryFields(supplier)) {
                    addRedirectAlertMessage(new SimpleMessage("Supplier added successfully."));
                    supplier = supplierDao.save(supplier);
                    supplierTransactionService.createOpeningBalanceAccountForSupplier(supplier, EnumSupplierTransactionType.Create.asSupplierTransactionType(), 0.0, new Date());
                } else {
                    addRedirectAlertMessage(new SimpleMessage("* marked fields are mandatory."));
                    //return new RedirectResolution(SupplierManagementAction.class).addParameter("createOrEdit").addParameter("supplier", supplier.getId());
                    return new ForwardResolution("/pages/admin/supplier.jsp");
				}
            } else if (supplier.getId() != null) {
                if (checkForMandatoryFields(supplier)) {
                    addRedirectAlertMessage(new SimpleMessage("Supplier edited successfully."));
                    supplierDao.save(supplier);
                } else {
                    addRedirectAlertMessage(new SimpleMessage("* marked fields are mandatory."));
                    return new RedirectResolution(SupplierManagementAction.class).addParameter("createOrEdit").addParameter("supplier", supplier.getId());
                }
            } else {
                addRedirectAlertMessage(new SimpleMessage("Supplier with provided TIN already exists."));
            }

            return new RedirectResolution(SupplierManagementAction.class);
        } else {
            return new RedirectResolution("/pages/admin/supplier.jsp");
        }
    }

    public Resolution generateExcelReport() {
        supplierList = supplierDao.getSupplierByTinAndName(supplierTin, supplierName, status);

        xlsFile = new File(adminDownloads + "/reports/SupplierList.xls");


        if (supplierList != null) {
            xslGenerator.generateSupplierListExcel(xlsFile, supplierList);


            return new HTTPResponseResolution();
        }
        return new RedirectResolution(SupplierManagementAction.class);
    }

    private class HTTPResponseResolution implements Resolution {
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

	private boolean checkForMandatoryFields(Supplier supplier){
		return (StringUtils.isNotBlank(supplier.getTinNumber()) && StringUtils.isNotBlank(supplier.getName()) &&
				StringUtils.isNotBlank(supplier.getState()) && supplier.getCreditDays() != null &&
				supplier.getTargetCreditDays() != null && supplier.getLeadTime() != null &&
				supplier.getActive() != null);
	}

    public List<Supplier> getSupplierList() {
        return supplierList;

    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierTin() {
        return supplierTin;
    }

    public void setSupplierTin(String supplierTin) {
        this.supplierTin = supplierTin;
    }

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return supplierPage == null ? 0 : supplierPage.getTotalPages();
    }

    public int getResultCount() {
        return supplierPage == null ? 0 : supplierPage.getTotalResults();
    }

    public File getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(File xlsFile) {
        this.xlsFile = xlsFile;
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("supplierTin");
        params.add("supplierName");
        params.add("status");
        return params;
    }

    public Boolean getStatus() {
        return status;
    }
    
    
}