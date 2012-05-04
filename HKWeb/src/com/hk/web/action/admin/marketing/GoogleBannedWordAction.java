package com.hk.web.action.admin.marketing;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.marketing.GoogleBannedWordDto;
import com.hk.constants.core.PermissionConstants;
import com.hk.manager.EmailManager;
import com.hk.report.manager.ReportManager;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA. User: Rahul Date: Dec 31, 2011 Time: 5:02:17 PM To change this template use File | Settings |
 * File Templates.
 */

@Secure(hasAnyPermissions = { PermissionConstants.REPORT_ADMIN }, authActionBean = AdminPermissionAction.class)
@Component
public class GoogleBannedWordAction extends BaseAction {
    @Autowired
    ReportManager                     reportManager;
    @Autowired
    EmailManager                      emailManager;

    private List<GoogleBannedWordDto> googleBannedWordDtoList = new ArrayList<GoogleBannedWordDto>();

    @DefaultHandler
    @DontValidate
    public Resolution pre() {

        // googleBannedWordDtoList = reportManager.generateDailyGoogleAdsBannedWords();
        /*
         * if (!googleBannedWordDtoList.isEmpty()) {
         * emailManager.sendDailyGoogleAdsBannedWords(googleBannedWordDtoList); }
         */
        return new ForwardResolution("/pages/admin/googleBannedWordReport.jsp");
    }

    public List<GoogleBannedWordDto> getGoogleBannedWordDtoList() {
        return googleBannedWordDtoList;
    }

    public void setGoogleBannedWordDtoList(List<GoogleBannedWordDto> googleBannedWordDtoList) {
        this.googleBannedWordDtoList = googleBannedWordDtoList;
    }
}
