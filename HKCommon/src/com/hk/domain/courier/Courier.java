package com.hk.domain.courier;
// Generated Dec 26, 2011 1:31:41 PM by Hibernate Tools 3.2.4.CR1


import com.akube.framework.gson.JsonSkip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**s
 * Courier generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "courier")
public class Courier implements java.io.Serializable {

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 45)
	private String name;

	@Column(name = "disabled", nullable = false, length = 10)
	private Boolean  disabled;

	@JsonSkip
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "courier")
	private Set<CourierServiceInfo> courierServiceInfos = new HashSet<CourierServiceInfo>(0);

	@JsonSkip
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "couriers")
	private List<CourierGroup> courierGroup = new ArrayList<CourierGroup>(0);

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name; 
	}

	public Set<CourierServiceInfo> getCourierServiceInfos() {
		return this.courierServiceInfos;
	}

	public void setCourierServiceInfos(Set<CourierServiceInfo> courierServiceInfos) {
		this.courierServiceInfos = courierServiceInfos;
	}

	@Override
	public String toString() {
		return id != null ? id.toString() : "";
	}

	public String getShortForm() {
		String shortCut = "";
		String[] strArr = name.split(" ");
		for (String s : strArr) {
			shortCut += s.substring(0, 1);
		}
		return shortCut;
	}

	public CourierGroup getCourierGroup() {
		return courierGroup != null && (courierGroup.size() > 0) ? courierGroup.get(0) : null;
	}

	public void setCourierGroup(List<CourierGroup> courierGroup) {
		this.courierGroup = courierGroup;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof Courier))
			return false;

		Courier courier = (Courier) o;
		if (this.id != null && courier.getId() != null) {
			return id.equals(courier.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
	return this.id != null ? id.hashCode() :0 ;
}

}


