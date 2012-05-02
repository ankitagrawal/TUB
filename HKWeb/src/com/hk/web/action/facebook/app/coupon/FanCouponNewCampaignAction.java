package com.hk.web.action.facebook.app.coupon;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.FanCouponManager;

@Component
public class FanCouponNewCampaignAction extends BaseAction {

    @Validate(required = true)
    private String       name;

    @Validate(required = true)
    private String       code;

    @Validate(required = false)
    private String       url;

    @Validate(required = true)
    private String       coupons;

    private List<String> couponCodes      = new ArrayList<String>();
    private List<String> bonusCouponCodes = new ArrayList<String>();

    @ValidationMethod
    public void validate() {
        /*
         * validate coupons here can be in the following formats 1) every code in a new line : couponcode1 couponcode2
         * 2) two codes in every line (one bonus code for sharing) couponcode1, couponcode2 couponcode3, couponcode4
         */

        String[] couponCodeLines = StringUtils.split(coupons, '\n');
        for (String couponCodeLine : couponCodeLines) {
            couponCodeLine = StringUtils.strip(couponCodeLine, "\n\r ");
            String[] coupons = StringUtils.split(couponCodeLine, ',');
            couponCodes.add(coupons[0]);
            if (coupons.length > 1) {
                bonusCouponCodes.add(coupons[1]);
            }
        }
    }

    FanCouponManager fanCouponManager;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/facebook/app/coupon/fanCouponNewCampaign.jsp");
    }

    public Resolution create() {
        fanCouponManager.createCampaign(name, code, url, couponCodes, bonusCouponCodes);
        addRedirectAlertMessage(new SimpleMessage("New Campaign created"));
        return new RedirectResolution(FanCouponAdminAction.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }
}
