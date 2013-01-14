package com.hk.domain.cycleCount;

import com.hk.domain.inventory.BrandsToAudit;
import com.hk.domain.user.User;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 11, 2013
 * Time: 6:04:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "cycle_count")
public class CycleCount implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@OneToOne
	@JoinColumn(name = "brands_to_audit_id", unique = true, nullable = false)
	private BrandsToAudit brandsToAudit;


	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 19)
	private Date createDate = new Date();


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = false, length = 19)
	private Date endDate = new Date();

	@Column(name = "audit_status", nullable = false)
	private Long auditStatus;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BrandsToAudit getBrandsToAudit() {
		return brandsToAudit;
	}

	public void setBrandsToAudit(BrandsToAudit brandsToAudit) {
		this.brandsToAudit = brandsToAudit;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Long auditStatus) {
		this.auditStatus = auditStatus;
	}
}
