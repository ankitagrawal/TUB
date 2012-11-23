package com.hk.admin.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vaibhav.adlakha
 */
public class SendCampaignResult {

    private boolean      isCampaignSentSuccess = true;

    private List<String> errorEmails           = new ArrayList<String>();

    public boolean isCampaignSentSuccess() {
        return isCampaignSentSuccess;
    }

    public SendCampaignResult setCampaignSentSuccess(boolean isCampaignSentSuccess) {
        this.isCampaignSentSuccess = isCampaignSentSuccess;
        return this;
    }

    public List<String> getErrorEmails() {
        return errorEmails;
    }

    public void setErrorEmails(List<String> errorEmails) {
        this.errorEmails = errorEmails;
    }

    public SendCampaignResult addErrorEmail(String email) {
        errorEmails.add(email);
        return this;

    }

    public SendCampaignResult addErrorEmaiList(List<String> errorEmailList) {
        errorEmails.addAll(errorEmailList);
        return this;
    }
}
