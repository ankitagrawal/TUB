package com.hk.web.action.admin.catalog;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.constants.core.Keys;
import com.hk.report.dto.inventory.StockReportDto;
import com.hk.util.io.HkXlsWriter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.web.action.error.AdminPermissionAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Secure(hasAnyPermissions = {PermissionConstants.SUPPLIER_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class SupplierManagementAction extends BasePaginatedAction {

    private static Logger logger = Logger.getLogger(SupplierManagementAction.class);

    File xlsFile;
    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
        String adminDownloads;
    @Autowired
    SupplierDao supplierDao;

    private List<Supplier> supplierList = new ArrayList<Supplier>();
    private Supplier supplier;
    public static final int LenghtOfTIN = 11;
    private String supplierTin;
    private String supplierName;
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
        supplierPage = supplierDao.getSupplierByTinAndName(supplierTin, supplierName, getPageNo(), getPerPage());
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
        if (supplier.getState() != null && supplier.getTinNumber() != null) {

            if (supplier.getTinNumber().length() != LenghtOfTIN) {
                getContext().getValidationErrors().add("e1", new SimpleError("TIN should be of 11 digits"));
            }
            if (!(supplier.getTinNumber().substring(0, 2).equals(StateList.stateMapTIN.get(supplier.getState())))) {
                getContext().getValidationErrors().add("e1", new SimpleError("check the first two digits of TIN"));
            }
        }
    }

    public Resolution save() {
        Supplier oldSupplier = supplierDao.findByTIN(supplier.getTinNumber());
        if (oldSupplier == null) {
            if (StringUtils.isNotBlank(supplier.getTinNumber()) && StringUtils.isNotBlank(supplier.getName()) && StringUtils.isNotBlank(supplier.getState())) {
                addRedirectAlertMessage(new SimpleMessage("Supplier added successfully."));
                supplierDao.save(supplier);
            } else {
                addRedirectAlertMessage(new SimpleMessage("* marked fields are mandatory."));
                return new RedirectResolution(SupplierManagementAction.class).addParameter("createOrEdit").addParameter("supplier", supplier.getId());
            }
        } else if (supplier.getId() != null) {
            if (StringUtils.isNotBlank(supplier.getTinNumber()) && StringUtils.isNotBlank(supplier.getName()) && StringUtils.isNotBlank(supplier.getState())) {
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
    }

    public Resolution generateExcelReport() {
        supplierList = supplierDao.getAllSupplierListByTinAndName(supplierTin, supplierName);

        xlsFile = new File(adminDownloads + "/reports/SupplierList.xls");
        HkXlsWriter xlsWriter = new HkXlsWriter();

        if (supplierList != null) {
            int xlsRow = 1;
            xlsWriter.addHeader("NAME", "NAME");
            xlsWriter.addHeader("TIN", "TIN");
            xlsWriter.addHeader("ADDRESS", "ADDRESS");
            xlsWriter.addHeader("CONTACT PERSON", "CONTACT PERSON");
            xlsWriter.addHeader("CONTACT NUMBER", "CONTACT NUMBER");

            for (Supplier supplier : supplierList) {
                xlsWriter.addCell(xlsRow, supplier.getName());
                xlsWriter.addCell(xlsRow, supplier.getTinNumber());
                xlsWriter.addCell(xlsRow, supplier.getLine1() + "\n" + supplier.getLine2() + "\n" + supplier.getCity() + "\n" + supplier.getPincode() + "\n" + supplier.getState());
                xlsWriter.addCell(xlsRow, supplier.getContactPerson());
                xlsWriter.addCell(xlsRow, supplier.getContactNumber());
                xlsWriter.writeData(xlsFile, "SupplierList");
                xlsRow++;
            }
            addRedirectAlertMessage(new SimpleMessage("Download complete"));

            return new HTTPResponseResolution();
        }
        return new RedirectResolution(SupplierManagementAction.class);
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

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return supplierPage == null ? 0 : supplierPage.getTotalPages();
    }

    public int getResultCount() {
        return supplierPage == null ? 0 : supplierPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("supplierTin");
        params.add("supplierName");
        return params;
    }
}