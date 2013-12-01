package com.hk.web.action.admin.marketing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.google.gson.Gson;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA. User: PRATHAM Date: 2/28/12 Time: 5:16 PM To change this template use File | Settings |
 * File Templates.
 */
@Secure(hasAnyPermissions = { PermissionConstants.SEND_MARKETING_MAILS }, authActionBean = AdminPermissionAction.class)
@Component
public class SendWeMissYouEmailer extends BaseAction {

    Integer             noOfDays;
    String              couponCode;
    int                 userCount;
    EmailCampaign       emailCampaign;
    List<EmailCampaign> emailCampaigns;
    @Autowired
    EmailCampaignDao    emailCampaignDao;
    @Autowired
    UserDao             userDao;
    @Autowired
    CouponDao           couponDao;
    @Autowired
    EmailManager        emailManager;
    @Autowired
    OrderDao            orderDao;

    Logger              logger = LoggerFactory.getLogger(SendWeMissYouEmailer.class);

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        emailCampaigns = emailCampaignDao.listAllMissYouCampaigns();
        return new ForwardResolution("/pages/admin/marketing/sendWeMissYouEmail.jsp");
    }

    public Resolution confirmCampaign() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String emailCampaignName = "WeMissYou" + "_" + sdf.format(new Date());
        emailCampaign = emailCampaignDao.getOrCreateEmailCampaign(emailCampaignName, 0l, EmailTemplateConstants.weMissYouEmail);
        List<User> potentialUsers = userDao.getAllMissingUsersLastOrderId(noOfDays);
        userCount = potentialUsers != null ? potentialUsers.size() : 0;
        return new ForwardResolution("/pages/admin/marketing/confirmSendingMissYouCampaign.jsp");
    }

    public Resolution sendWeMissYouEmailer() {
        Coupon coupon = couponDao.findByCode(couponCode);
        if (coupon == null) {
            addRedirectAlertMessage(new SimpleMessage("Coupon Code doesn't exist!!"));
            return new RedirectResolution(SendWeMissYouEmailer.class);
        }
        List<User> potentialUsers = userDao.getAllMissingUsersLastOrderId(noOfDays);
        userCount = potentialUsers != null ? potentialUsers.size() : 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String emailCampaignName = "WeMissYou" + "_" + sdf.format(new Date());
        emailCampaign = emailCampaignDao.getOrCreateEmailCampaign(emailCampaignName, 0l, EmailTemplateConstants.weMissYouEmail);
        String xsmtpapi = getSendGridHeaderJson(emailCampaign);
        if (potentialUsers != null) {
            logger.info("user list size " + potentialUsers.size());
        }
        emailManager.sendWeMissYouEmail(potentialUsers, coupon, emailCampaign, xsmtpapi);

        addRedirectAlertMessage(new SimpleMessage("Sending mail to Missing users  "));
        return new RedirectResolution(SendWeMissYouEmailer.class);
    }

    @SuppressWarnings("unchecked")
    public static String getSendGridHeaderJson(EmailCampaign emailCampaign) {
        List<String> categories = new ArrayList<String>();
        categories.add("MissingUsers");
        categories.add("campaign_" + emailCampaign.getName());

        Map sendGridHeaderMap = new HashMap();
        sendGridHeaderMap.put("category", categories);
        Gson gson = JsonUtils.getGsonDefault();
        return gson.toJson(sendGridHeaderMap);
    }

    public Integer getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public List<EmailCampaign> getEmailCampaigns() {
        return emailCampaigns;
    }

    public void setEmailCampaigns(List<EmailCampaign> emailCampaigns) {
        this.emailCampaigns = emailCampaigns;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public EmailCampaign getEmailCampaign() {
        return emailCampaign;
    }

    public void setEmailCampaign(EmailCampaign emailCampaign) {
        this.emailCampaign = emailCampaign;
    }
}
