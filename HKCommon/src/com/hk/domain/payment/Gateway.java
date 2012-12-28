package com.hk.domain.payment;

import com.akube.framework.gson.JsonSkip;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 11/21/12
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "gateway")
public class Gateway implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "priority", scale = 0)
    private Long priority;

    @Column(name = "active", scale = 0)
    private Boolean active;

    @Column(name = "image")
    private byte[] imageIcon;

   @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "downtime_start_date", length = 19)
    private Date downtimeStartDate;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "downtime_end_date", length = 19)
    private Date downtimeEndDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDowntimeStartDate() {
        return downtimeStartDate;
    }

    public void setDowntimeStartDate(Date downtimeStartDate) {
        this.downtimeStartDate = downtimeStartDate;
    }

    public Date getDowntimeEndDate() {
        return downtimeEndDate;
    }

    public void setDowntimeEndDate(Date downtimeEndDate) {
        this.downtimeEndDate = downtimeEndDate;
    }

    public byte[] getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(byte[] imageIcon) {
        this.imageIcon = imageIcon;
    }
}