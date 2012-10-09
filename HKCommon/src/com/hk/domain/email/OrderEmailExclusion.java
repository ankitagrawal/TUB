package com.hk.domain.email;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/14/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "order_email_exclusion")
@NamedQueries( {@NamedQuery(name = "orderExclusionfindByEmail", query = "from OrderEmailExclusion o where o.email = :email") } )
public class OrderEmailExclusion implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "email", nullable = false, length = 100, unique = true)
	private String email;

	@Column(name = "is_delivery_mail_excluded", nullable = false)
	private Boolean deliveryMailExcluded = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getDeliveryMailExcluded() {
		return deliveryMailExcluded;
	}

	public void setDeliveryMailExcluded(Boolean deliveryMailExcluded) {
		this.deliveryMailExcluded = deliveryMailExcluded;
	}

	public Boolean isDeliveryMailExcluded() {
			return deliveryMailExcluded;
	}

}
