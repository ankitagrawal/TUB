package com.hk.domain.user;

import com.hk.domain.order.Order;

import javax.persistence.*;

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
	@JoinColumn(name = "order_id", nullable = false)
	private Order basOrder;

	@Column(name = "call_status", nullable = false)
	Long callStatus;

	@Column(name = "remark")
	String remark;

	public Order getBasOrder() {
		return basOrder;
	}

	public void setBasOrder(Order basOrder) {
		this.basOrder = basOrder;
	}

	public Long getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(Long callStatus) {
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
}
