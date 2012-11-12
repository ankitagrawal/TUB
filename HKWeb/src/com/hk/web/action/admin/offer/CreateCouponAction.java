package com.hk.web.action.admin.offer;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
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
import com.akube.framework.util.BaseUtils;
import com.hk.admin.manager.FanCouponManager;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.exception.HealthKartCouponException;
import com.hk.pact.service.discount.CouponService;
import com.hk.util.ParseCsvFile;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.CREATE_COUPON }, authActionBean = AdminPermissionAction.class)
@Component
public class CreateCouponAction extends BaseAction {

    private static int       MAX_COUPON_LENGTH = 20;

    private static Logger    logger            = LoggerFactory.getLogger(CreateCouponAction.class);
    @Autowired
    private CouponService    couponService;
    @Autowired
    private FanCouponManager fanCouponManager;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    private String           adminDownloads;

    @Validate(required = true)
    private String           couponCode;

    private Date             endDate;
    private Long             allowedTimes;
    private Long             alreadyUsed;
    private Boolean          repetitiveUsage;

    @Validate(required = true)
    private Offer            offer;

    @Validate(required = true)
    private Long             numberOfCoupons;

    @Validate(required = true, on = "generateMultiFanCoupon")
    String                   campaignCode;

    private String           endPart;
    // @Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                   adminUploadsPath;
    FileBean                 fileBean;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/offer/createCoupon.jsp");
    }

    public Resolution generateSingle() {
        if (couponCode.length() > MAX_COUPON_LENGTH) {
            addRedirectAlertMessage(new LocalizableMessage("/CreateCoupon.action.coupon.length.exceeded"));
            return getContext().getSourcePageResolution();
        }

        if (getCouponService().findByCode(couponCode) != null) {
            addRedirectAlertMessage(new LocalizableMessage("/CreateCoupon.action.coupon.code.already.exists"));
            return getContext().getSourcePageResolution();
        }
        if (repetitiveUsage == null)
            repetitiveUsage = false;
        // Coupon coupon =
        getCouponService().createCoupon(couponCode, endDate, allowedTimes, alreadyUsed, offer, null, repetitiveUsage, null);

        addRedirectAlertMessage(new LocalizableMessage("/CreateCoupon.action.coupon.created"));
        return new RedirectResolution(CreateCouponAction.class);
    }

    public Resolution generateMulti() {

        List<Coupon> coupons = null;
        try {
            coupons = getCouponService().generateCoupons(endPart, couponCode, numberOfCoupons, repetitiveUsage, endDate, allowedTimes, alreadyUsed, offer, null, null);
        } catch (HealthKartCouponException e) {
            addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
            return getContext().getSourcePageResolution();
        }

        // save coupons file to admin dir
        File couponsDir = new File(getSavedCouponsDirPath());
        if (!couponsDir.exists()) {
            couponsDir.mkdirs();
        }

        String couponFileName = couponCode + "-" + endPart + "-" + coupons.size() + "-coupons-" + BaseUtils.getCurrentTimestamp().getTime() + ".txt";
        final File couponFile = new File(couponsDir.getAbsolutePath() + "/" + couponFileName);
        Writer output = null;
        try {
            couponFile.createNewFile();
            output = new BufferedWriter(new FileWriter(couponFile));
            output.write("Offer: " + offer.getDescription() + BaseUtils.newline);
            output.write("Nuber of coupons: " + numberOfCoupons + BaseUtils.newline);
            output.write("Coupons: " + BaseUtils.newline);
            for (Coupon coupon : coupons) {
                output.write(coupon.getCode() + BaseUtils.newline);
            }
        } catch (IOException e) {
            logger.error("Error while making a coupons file:", e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error("error while closing the coupons file", e);
                }
            }
        }

        final int contentLength = (int) couponFile.length();

        addRedirectAlertMessage(new LocalizableMessage("/CreateCoupon.action.coupon.created"));
        // give option to download the coupons file
        return new Resolution() {
            public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                OutputStream out = null;
                InputStream in = new BufferedInputStream(new FileInputStream(couponFile));
                res.setContentLength(contentLength);
                res.setHeader("Content-Disposition", "attachment; filename=\"" + couponFile.getName() + "\";");
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

    public Resolution generateMultiFanCoupon() {

        List<Coupon> coupons = null;
        try {
            coupons = getCouponService().generateCoupons(endPart, couponCode, numberOfCoupons, repetitiveUsage, endDate, allowedTimes, alreadyUsed, offer, null, null);
        } catch (HealthKartCouponException e) {
            addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
            return getContext().getSourcePageResolution();
        }

        // save coupons file to admin dir and also insert them into fb fan coupon table
        File couponsDir = new File(getSavedCouponsDirPath());
        if (!couponsDir.exists()) {
            couponsDir.mkdirs();
        }

        List<String> couponCodes = new ArrayList<String>();
        String couponFileName = couponCode + "-" + endPart + "-" + coupons.size() + "-coupons-" + BaseUtils.getCurrentTimestamp().getTime() + ".txt";
        final File couponFile = new File(couponsDir.getAbsolutePath() + "/" + couponFileName);
        Writer output = null;
        try {
            couponFile.createNewFile();
            output = new BufferedWriter(new FileWriter(couponFile));
            output.write("Offer: " + offer.getDescription() + BaseUtils.newline);
            output.write("Nuber of coupons: " + numberOfCoupons + BaseUtils.newline);
            output.write("Coupons: " + BaseUtils.newline);
            for (Coupon coupon : coupons) {
                output.write(coupon.getCode() + BaseUtils.newline);
                couponCodes.add(coupon.getCode());
            }
        } catch (IOException e) {
            logger.error("Error while making a coupons file:", e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error("error while closing the coupons file", e);
                }
            }
        }

        getFanCouponManager().createCampaign(campaignCode, campaignCode, "", couponCodes, new ArrayList<String>(), true);

        addRedirectAlertMessage(new LocalizableMessage("/CreateCoupon.action.coupon.created"));
        return new RedirectResolution(CreateCouponAction.class);
    }

    public Resolution uploadCouponsAndSaveIntoDB() throws IOException {
        String excelFilePath = adminUploadsPath + "/emailList/" + System.currentTimeMillis() + ".txt";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        List<String> couponList = new ArrayList<String>();
        couponList.addAll(ParseCsvFile.getStringListFromCsv(excelFilePath));

        for (String couponCode : couponList) {
            if (couponCode.length() > MAX_COUPON_LENGTH) {
                addRedirectAlertMessage(new LocalizableMessage("/CreateCoupon.action.coupon.length.exceeded"));
                return getContext().getSourcePageResolution();
            }

            // Coupon coupon =
            getCouponService().createCoupon(couponCode, endDate, allowedTimes, alreadyUsed, offer, null, false, null);

        }

        addRedirectAlertMessage(new LocalizableMessage("coupons read and inserted into db"));
        return new RedirectResolution(CreateCouponAction.class);
    }

    private String getSavedCouponsDirPath() {
        return adminDownloads + "/coupons";
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getAllowedTimes() {
        return allowedTimes;
    }

    public void setAllowedTimes(Long allowedTimes) {
        this.allowedTimes = allowedTimes;
    }

    public Long getAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(Long alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Long getNumberOfCoupons() {
        return numberOfCoupons;
    }

    public void setNumberOfCoupons(Long numberOfCoupons) {
        this.numberOfCoupons = numberOfCoupons;
    }

    public String getEndPart() {
        return endPart;
    }

    public void setEndPart(String endPart) {
        this.endPart = endPart;
    }

    public Boolean isRepetitiveUsage() {
        return repetitiveUsage;
    }

    public void setRepetitiveUsage(Boolean repetitiveUsage) {
        this.repetitiveUsage = repetitiveUsage;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public CouponService getCouponService() {
        return couponService;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }

    public FanCouponManager getFanCouponManager() {
        return fanCouponManager;
    }

    public void setFanCouponManager(FanCouponManager fanCouponManager) {
        this.fanCouponManager = fanCouponManager;
    }

    public Boolean getRepetitiveUsage() {
        return repetitiveUsage;
    }

}
