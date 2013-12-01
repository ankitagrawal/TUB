package com.hk.domain.courier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 13, 2012
 * Time: 9:11:40 AM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "awb_status")
public class AwbStatus {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "status", nullable = false, length = 40)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof AwbStatus) {
			AwbStatus awbStatus = (AwbStatus) obj;
			if (this.id != null && awbStatus.getId() != null) {
				return this.id.equals(awbStatus.getId());
			}

		}
		return false;
	}

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
