package com.hk.domain.marketing;

// Generated Nov 18, 2011 4:38:38 PM by Hibernate Tools 3.2.4.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.akube.framework.gson.JsonSkip;

@SuppressWarnings("serial")
@Entity
@Table(name = "campaign_tracking")
public class CampaignTracking implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long   id;

    @Column(name = "utm_source", length = 120)
    private String utmSource;

    @Column(name = "utm_medium", length = 120)
    private String utmMedium;

    @Column(name = "utm_campaign", length = 120)
    private String utmCampaign;

    @Column(name = "referral_url", length = 450)
    private String referralUrl;

    @Column(name = "target_url", length = 450)
    private String targetUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_stamp", length = 19)
    private Date   timeStamp;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date   createDate = new Date();

    @Column(name = "params_string", length = 120)
    private String paramsString;

    @Column(name = "user_id")
    private Long   userId;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUtmSource() {
        return this.utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getUtmMedium() {
        return this.utmMedium;
    }

    public void setUtmMedium(String utmMedium) {
        this.utmMedium = utmMedium;
    }

    public String getUtmCampaign() {
        return this.utmCampaign;
    }

    public void setUtmCampaign(String utmCampaign) {
        this.utmCampaign = utmCampaign;
    }

    public String getReferralUrl() {
        return this.referralUrl;
    }

    public void setReferralUrl(String referralUrl) {
        this.referralUrl = referralUrl;
    }

    public String getTargetUrl() {
        return this.targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Date getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getParamsString() {
        return this.paramsString;
    }

    public void setParamsString(String paramsString) {
        this.paramsString = paramsString;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
