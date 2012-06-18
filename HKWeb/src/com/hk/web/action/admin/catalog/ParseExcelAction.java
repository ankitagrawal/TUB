package com.hk.web.action.admin.catalog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.task.dbmaster.ProductCatalogService;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.web.BatchProcessWorkManager;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.UPLOAD_PRODUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
@Component
public class ParseExcelAction extends BaseAction {
    // static File inFile = new File("./test/data/catalog/ProductCatalog-Diabetes.xls");
    //  
    // TestDatabaseConnectionHelper connectionHelper;

    private static Logger           logger = LoggerFactory.getLogger(ParseExcelAction.class);

    @Autowired
    private ProductCatalogService productCatalogService;

    @Autowired
    private BatchProcessWorkManager batchProcessWorkManager;

    @Autowired
    private XslParser               xslParser;

    @Autowired
    private UserService             userService;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                          adminUploadsPath;

    @Validate(required = true)
    FileBean                        fileBean;

    @Validate(required = true)
    String                          category;

  public void setCategory(String category) {
        this.category = category;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/catalogDump.jsp");
    }

    public Resolution parse() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/catalogFiles/" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        try {
            Set<Product> productSet = getXslParser().readProductList(excelFile, loggedOnUser);
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
            return new ForwardResolution("/pages/admin/catalogDump.jsp");
        }
        getProductManager().insertCatalogue(excelFile, null);
        // excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/catalogDump.jsp");
    }

    public String getCategory() {
        return category;
    }

    public ProductCatalogService getProductManager() {
        return productCatalogService;
    }

    public void setProductManager(ProductCatalogService productCatalogService) {
        this.productCatalogService = productCatalogService;
    }

    public XslParser getXslParser() {
        return xslParser;
    }

    public void setXslParser(XslParser xslParser) {
        this.xslParser = xslParser;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
