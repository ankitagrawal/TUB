package com.hk.web.action.admin.catalog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.util.AmazonXslGenerator;
import com.hk.util.XslGenerator;
import com.hk.web.BatchProcessWorkManager;

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
    CategoryDao              categoryDao;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                   adminDownloadsPath;

    // @Validate(required = true)
    String                   category;
    String                   brand;
    File                     excelFile;

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

    private class HTTPResponseResolution implements Resolution {
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

    }

    public Resolution generateCatalogByCategory() throws Exception {
        List<Product> products = productDao.getAllProductByCategory(category);
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Category_" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        excelFile = new File(excelFilePath);

        xslGenerator.generateCatalogExcel(products, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

        return new HTTPResponseResolution();
    }

    public Resolution generateCatalogForAllCategories() throws Exception {
        List<Product> products = productDao.getAllProductsForCatalog();
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Category_ALL_" + sdf.format(new Date()) + ".xls";
        excelFile = new File(excelFilePath);

        xslGenerator.generateCatalogExcel(products, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Download complete"));

        return new HTTPResponseResolution();
    }

    public Resolution generateCatalogBySubCategory() throws Exception {
        List<Product> products = productDao.getAllProductBySubCategory(category);
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Category_" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        excelFile = new File(excelFilePath);

        xslGenerator.generateCatalogExcel(products, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

        return new HTTPResponseResolution();
    }

    public Resolution generateCatalogByBrand() throws Exception {
        List<Product> products = productDao.getAllProductByBrand(brand);
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Brand_" + sdf.format(new Date()) + "/" + brand + "_" + sdf.format(new Date()) + ".xls";
        excelFile = new File(excelFilePath);

        xslGenerator.generateCatalogExcel(products, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

        return new HTTPResponseResolution();
    }

    public Resolution generateAmazonCatalogByCategory() throws Exception {
        List<Product> products = productDao.getAllProductByCategory(category);
        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/Amazon_" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        excelFile = new File(excelFilePath);

      // non-deleted

      for (Iterator<Product> productIterator = products.iterator(); productIterator.hasNext();) {
        Product product = productIterator.next();
        if (product.isDeleted()) {
          productIterator.remove();
        }
      }

        amazonXslGenerator.generateCatalogExcel(products, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

        return new HTTPResponseResolution();
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
