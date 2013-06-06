package com.hk.web.action.admin.marketing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.admin.pact.service.email.ProductVariantNotifyMeEmailService;
import com.hk.impl.dao.email.NotifyMeDto;
import com.hk.web.action.core.user.NotifyMeAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.report.manager.ReportManager;
import com.hk.util.SendGridUtil;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA. User: USER Date: Nov 24, 2011 Time: 11:00:42 AM To change this template use File | Settings |
 * File Templates.
 */

@Secure(hasAnyPermissions = {PermissionConstants.VIEW_NOTIFY_LIST}, authActionBean = AdminPermissionAction.class)
@Component
public class NotifyMeListAction extends BasePaginatedAction implements ValidationErrorHandler {

    @Autowired
    private NotifyMeDao notifyMeDao;
    @Autowired
    private ReportManager reportManager;
    @Autowired
    private AdminEmailManager adminEmailManager;
    @Autowired
    private EmailCampaignDao emailCampaignDao;
    @Autowired
    ProductVariantNotifyMeEmailService productVariantNotifyMeEmailService;


    File xlsFile;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloads;

    private Date startDate;
    private Date endDate;
    Page notifyMePage;
    private Integer defaultPerPage = 30;
    private List<NotifyMe> notifyMeList = new ArrayList<NotifyMe>();
    private List<NotifyMeDto> notifyMeDtoList = new ArrayList<NotifyMeDto>();
    private List<NotifyMe> notifyMeListForProductVariantInStock = new ArrayList<NotifyMe>();
    private ProductVariant productVariant;
    private Product product;
    private Category primaryCategory;
    private Boolean productInStock;
    private Boolean productDeleted;
    private Boolean productHidden;
    private Float conversionRate = 0.1f;
    private int bufferRate = 2;
    private int totalProductVariant;


    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        notifyMePage = notifyMeDao.searchNotifyMe(startDate, endDate, getPageNo(), getPerPage(), product, productVariant, primaryCategory, productInStock, productDeleted);
        notifyMeList = notifyMePage.getList();
        return new ForwardResolution("/pages/admin/notifyMeList.jsp");
    }

    @ValidationMethod(on = "sendMailToNotifiedUsers")
    public void checkIfProductOrVariantIsOutOfStock() {
        if (product == null && productVariant == null) {
            addValidationError("Either product or product variant should be mentioned", new SimpleError("Either product or product variant should be mentioned"));
        }

        if (productVariant != null && Boolean.TRUE.equals(productVariant.isOutOfStock())) {
            addValidationError("The variant is still out of stock, damn U!!", new SimpleError("The variant is still out of stock, damn U!!"));
        }
        if (product != null) {
            Integer outOfStockOrDeletedCtr = 0;
            for (ProductVariant variant : product.getProductVariants()) {
                if (variant.isOutOfStock() || variant.isDeleted()) {
                    outOfStockOrDeletedCtr++;
                }
            }
            if (product.getProductVariants().size() == outOfStockOrDeletedCtr) {
                product.setOutOfStock(true);
                addValidationError("Hell man, Product is still outta stock!!", new SimpleError("Hell man, Product is still outta stock!!"));
            }
        }
    }

    public Resolution generateNotifyMeList() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/notify-me-list" + sdf.format(new Date()) + ".xls");
            xlsFile = reportManager.generateNotifyMeList(xlsFile.getPath(), startDate, endDate, product, productVariant, primaryCategory, productInStock, productDeleted);
            addRedirectAlertMessage(new SimpleMessage("Notify me list successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Notify me list generation failed"));
        }
        return new HTTPResponseResolution();
    }

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
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

    public Resolution sendMailToNotifiedUsers() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        notifyMePage = notifyMeDao.searchNotifyMe(startDate, endDate, 0, 0, product, productVariant, null, null, null);
        notifyMeList = notifyMePage.getList();

        User user = getPrincipalUser();

        String emailCampaignName = product != null ? product.getId() : productVariant != null ? productVariant.getProduct().getId() : "";
        emailCampaignName += "_" + sdf.format(new Date());

        // get or create emailCampaign
        EmailCampaign emailCampaign = emailCampaignDao.getOrCreateEmailCampaign(emailCampaignName, 0l, EmailTemplateConstants.notifyUserEmail);

        String xsmtpapi = SendGridUtil.getNotifyMeSendGridHeaderJson(product, productVariant, emailCampaign);
        // send email to users, one per unique email id and unique email campaign
        getAdminEmailManager().sendNotifyUsersMails(notifyMeList, emailCampaign, xsmtpapi, product, productVariant, user);

        addRedirectAlertMessage(new SimpleMessage("Sending mail to Notified users  "));
        return new RedirectResolution(NotifyMeListAction.class);
    }

    public Resolution getNotifyMeProductVariantListInStock() {
        notifyMePage = notifyMeDao.getNotifyMeListForProductVariantInStock(getPageNo(), getPerPage());
        if (notifyMePage == null) {
            notifyMeList = null;
            addRedirectAlertMessage(new SimpleMessage("There are no more notifications for PV in stock."));
            return new ForwardResolution("/pages/admin/notifyMeList.jsp");
        } else {
            notifyMeList = notifyMePage.getList();
            return new ForwardResolution("/pages/admin/notifyMeList.jsp").addParameter("productVariantInStock", "true");
        }
    }

    public Resolution sendEmailToNotifiedUsersForProductVariantInStock() {
        notifyMeListForProductVariantInStock = notifyMeDao.getNotifyMeListForProductVariantInStock();
        if (notifyMeListForProductVariantInStock == null || notifyMeListForProductVariantInStock.isEmpty()) {
            addRedirectAlertMessage(new SimpleMessage("All emails have been sent to the notified users.There are no more notifications for PV instock."));
            notifyMeList = null;
            return new ForwardResolution("/pages/admin/notifyMeList.jsp");
        }
        User user = getPrincipalUser();
        getAdminEmailManager().sendNotifyUserMailsForPVInStock(notifyMeListForProductVariantInStock, user);
        addRedirectAlertMessage(new SimpleMessage("Sending Emails to  In Stock PV  Notified Users"));
        return new RedirectResolution(NotifyMeListAction.class);
    }

    /*According to conversion logic*/
    public Resolution sendAllNotifyMailsForAvailableProductVariant() {
        if (conversionRate > 1) {
            addRedirectAlertMessage(new SimpleMessage("enter conversion rate less than 1"));
            return new RedirectResolution(NotifyMeListAction.class);
        }
        productVariantNotifyMeEmailService.sendNotifyMeEmailForInStockProducts(conversionRate, bufferRate);
        return new RedirectResolution(NotifyMeListAction.class);
    }

    /*pre method for similar product screen*/
    public Resolution notifyMeListForDeletedHiddenOOSProduct() {
        if(productDeleted == null){
            productDeleted = true;
        }
        if (!productDeleted && (productInStock == null || !productInStock) && (productHidden == null || !productHidden)) {
            addRedirectAlertMessage(new SimpleMessage("Please mark product as deleted/OOS/Hidden value , to qualify for similar product mails"));
            return new ForwardResolution("/pages/admin/notifyMeSimilarProduct.jsp");
        }
        notifyMePage = notifyMeDao.getNotifyMeListForDeletedHiddenOOSProduct(startDate, endDate, getPageNo(), getPerPage(), product, productVariant, primaryCategory, productInStock, productDeleted, productHidden);
        notifyMeDtoList = notifyMePage.getList();

        totalProductVariant = notifyMeDtoList.size();
        return new ForwardResolution("/pages/admin/notifyMeSimilarProduct.jsp");
    }


    /*For Similar Products*/
    public Resolution sendAllMailsForDeletedProducts() {
        if (!productDeleted && (productInStock == null || !productInStock) && (productHidden == null || !productHidden)) {
            addRedirectAlertMessage(new SimpleMessage("Please mark product as deleted/OOS/Hidden value , to qualify for similar product mails"));
            return new ForwardResolution("/pages/admin/notifyMeSimilarProduct.jsp");
        }
        notifyMeList = notifyMeDao.searchNotifyMe(startDate, endDate, product, productVariant, primaryCategory, productInStock, productDeleted, productHidden);
        int countOfSentMail = productVariantNotifyMeEmailService.sendNotifyMeEmailForDeletedOOSHidden(conversionRate, bufferRate, notifyMeList);
        addRedirectAlertMessage(new SimpleMessage("Total Emails Sent" + countOfSentMail));
        return new ForwardResolution("/pages/admin/notifyMeSimilarProduct.jsp");
    }

    public List<NotifyMe> getNotifyMeList() {
        return notifyMeList;
    }

    public void setNotifyMeList(List<NotifyMe> notifyMeList) {
        this.notifyMeList = notifyMeList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public File getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(File xlsFile) {
        this.xlsFile = xlsFile;
    }

    public String getAdminDownloads() {
        return adminDownloads;
    }

    public void setAdminDownloads(String adminDownloads) {
        this.adminDownloads = adminDownloads;
    }

    public Set<String> getParamSet() {

        HashSet<String> params = new HashSet<String>();
        params.add("productVariant");
        params.add("product");
        params.add("startDate");
        params.add("endDate");
        params.add("primaryCategory");
        params.add("productInStock");
        params.add("productDeleted");
        params.add("productHidden");
        return params;
    }

    public int getResultCount() {
        return notifyMePage == null ? 0 : notifyMePage.getTotalResults();
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return notifyMePage == null ? 0 : notifyMePage.getTotalPages();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Category getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(Category primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public NotifyMeDao getNotifyMeDao() {
        return notifyMeDao;
    }

    public void setNotifyMeDao(NotifyMeDao notifyMeDao) {
        this.notifyMeDao = notifyMeDao;
    }

    public AdminEmailManager getAdminEmailManager() {
        return adminEmailManager;
    }

    public void setAdminEmailManager(AdminEmailManager adminEmailManager) {
        this.adminEmailManager = adminEmailManager;
    }

    public EmailCampaignDao getEmailCampaignDao() {
        return emailCampaignDao;
    }

    public void setEmailCampaignDao(EmailCampaignDao emailCampaignDao) {
        this.emailCampaignDao = emailCampaignDao;
    }

    public Boolean getProductInStock() {
        return productInStock;
    }

    public void setProductInStock(Boolean productInStock) {
        this.productInStock = productInStock;
    }

    public Boolean getProductDeleted() {
        return productDeleted;
    }

    public void setProductDeleted(Boolean productDeleted) {
        this.productDeleted = productDeleted;
    }

    public Float getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Float conversionRate) {
        this.conversionRate = conversionRate;
    }

    public int getBufferRate() {
        return bufferRate;
    }

    public void setBufferRate(int bufferRate) {
        this.bufferRate = bufferRate;
    }

    public List<NotifyMeDto> getNotifyMeDtoList() {
        return notifyMeDtoList;
    }

    public void setNotifyMeDtoList(List<NotifyMeDto> notifyMeDtoList) {
        this.notifyMeDtoList = notifyMeDtoList;
    }

    public Boolean getProductHidden() {
        return productHidden;
    }

    public void setProductHidden(Boolean productHidden) {
        this.productHidden = productHidden;
    }

    public int getTotalProductVariant() {
        return totalProductVariant;
    }

    public void setTotalProductVariant(int totalProductVariant) {
        this.totalProductVariant = totalProductVariant;
    }
}
