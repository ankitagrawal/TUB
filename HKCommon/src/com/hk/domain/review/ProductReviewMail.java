package com.hk.domain.review;
// Generated Jan 7, 2013 2:53:10 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.User;

import java.util.Date;
import javax.persistence.*;


/**
 * ProductReviewMail generated by hbm2java
 */
@Entity
@Table(name="product_review_mail")
public class ProductReviewMail  implements java.io.Serializable {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", unique=true, nullable=false)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="created_by", nullable=false)
    private User createdBy;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mail_id", nullable=false)
    private Mail mail;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="last_updated_by", nullable=false)
    private User lastUpdatedBy;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id", nullable=false)
    private Product product;


    @Column(name="time_window_days")
    private Long timeWindowDays;


    @Column(name="days_to_send_review_mail_again")
    private Long daysToSendReviewMailAgain;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_dt", length=19)
    private Date createDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_dt", nullable=false, length=19)
    private Date updateDt;


    @Column(name="is_enabled", nullable=false)
    private boolean isEnabled;


    @Column(name="test_email_id", length=50)
    private String testEmailId;

    @Column(name="deleted", nullable=false)
    private boolean deleted;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mail getMail() {
        return this.mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public Long getTimeWindowDays() {
        return this.timeWindowDays;
    }

    public void setTimeWindowDays(Long timeWindowDays) {
        this.timeWindowDays = timeWindowDays;
    }
    public Long getDaysToSendReviewMailAgain() {
        return this.daysToSendReviewMailAgain;
    }

    public void setDaysToSendReviewMailAgain(Long daysToReviewAgain) {
        this.daysToSendReviewMailAgain = daysToReviewAgain;
    }
    public Date getCreateDt() {
        return this.createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
    public Date getUpdateDt() {
        return this.updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(boolean enabled){
        this.isEnabled = enabled;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public String getTestEmailId() {
        return this.testEmailId;
    }


    public void setTestEmailId(String testEmailId) {
        this.testEmailId = testEmailId;
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }



}


