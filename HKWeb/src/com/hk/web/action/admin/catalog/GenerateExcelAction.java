package com.hk.web.action.admin.catalog;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.util.AmazonXslGenerator;
import com.hk.util.XslGenerator;
import com.hk.web.BatchProcessWorkManager;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Secure(hasAnyPermissions = { PermissionConstants.DOWNLOAD_PRDOUCT_CATALOG })
@Component
public class GenerateExcelAction extends BaseAction {
    @Autowired
    XslGenerator             xslGenerator;
    @Autowired
    AmazonXslGenerator       amazonXslGenerator;
    @Autowired
    ProductDao               productDao;
    @Autowired
    BatchProcessWorkManager  workManager;
    @Autowired
    CategoryDao categoryDao;

    // @Named(Keys.Env.adminDownloads)
    @Value("#{hkEnvProps['adminDownloads']}")
    String                   adminDownloadsPath;

    // @Validate(required = true)
    String                   category;
    String                   brand;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/catalogDownload.jsp");
    }

    @ValidationMethod(on = { "generateCatalogByCategory", "generateCatalogBySubCategory" })
    public void validateCategory() {
        if (categoryDao.getCategoryByName(category) == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Category not found"));
        }
    }

    public Resolution generateCatalogByCategory() throws Exception {
        List<Product> products = productDao.getAllProductByCategory(category);
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Category_" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        final File excelFile = new File(excelFilePath);

        xslGenerator.generateCatalogExcel(products, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

        return new Resolution() {

            public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                OutputStream out = null;
                InputStream in = new BufferedInputStream(new FileInputStream(excelFile));
                res.setContentLength((int) excelFile.length());
                res.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getName() + "\";");
                out = res.getOutputStream();

                // Copy the contents of the file to the output stream
                byte[] buf = new byte[8192];
                int count = 0;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
            }
        };

    }

    public Resolution generateCatalogBySubCategory() throws Exception {
        List<Product> products = productDao.getAllProductBySubCategory(category);
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Category_" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        final File excelFile = new File(excelFilePath);

        xslGenerator.generateCatalogExcel(products, excelFilePath);
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

    public Resolution generateCatalogByBrand() throws Exception {
        List<Product> products = productDao.getAllProductByBrand(brand);
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Brand_" + sdf.format(new Date()) + "/" + brand + "_" + sdf.format(new Date()) + ".xls";
        final File excelFile = new File(excelFilePath);

        xslGenerator.generateCatalogExcel(products, excelFilePath);
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

    public Resolution generateAmazonCatalogByCategory() throws Exception {
        List<Product> products = productDao.getAllProductByCategory(category);
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Amazon_" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        final File excelFile = new File(excelFilePath);

        amazonXslGenerator.generateCatalogExcel(products, excelFilePath);
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
