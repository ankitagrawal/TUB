package com.hk.domain.user;

import com.hk.domain.order.Order;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Feb 7, 2013
 * Time: 5:13:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "user_cod_call")
public class UserCodCall {

	@Id
	@JoinColumn(name = "order_id", nullable = false)
	Order orderId;

	@Column(name = "call_status", nullable = false)
	String callStatus;

	@Column(name = "remark")
	String remark;

	public Order getOrderId() {
		return orderId;
	}

	public void setOrderId(Order orderId) {
		this.orderId = orderId;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
