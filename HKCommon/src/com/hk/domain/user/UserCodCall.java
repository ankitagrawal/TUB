package com.hk.domain.user;

import com.hk.domain.order.Order;
import com.akube.framework.gson.JsonSkip;

import javax.persistence.*;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 7, 2013
 * Time: 5:13:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "user_cod_call")
public class UserCodCall {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false , unique = true)
    private Order baseOrder;

    @Column(name = "call_status", nullable = false)
    Integer callStatus;

    @Column(name = "remark")
    String remark;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 19)
    private Date createDate;

    @Column(name = "source", length = 45)
    private String source;

    public Order getBaseOrder() {
        return baseOrder;
    }

    public void setBaseOrder(Order baseOrder) {
        this.baseOrder = baseOrder;
    }

    public Integer getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Integer callStatus) {
        this.callStatus = callStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return this.id != null ? this.id.toString() : "";
    }
}
