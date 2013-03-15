package com.hk.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "b2bOrder_checkList")
public class B2BOrderCheckList {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long                      id;
	
	private Long base_order_id;
	
	private boolean cForm;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBase_order_id() {
		return base_order_id;
	}

	public void setBase_order_id(Long base_order_id) {
		this.base_order_id = base_order_id;
	}

	public boolean iscForm() {
		return cForm;
	}

	public void setcForm(boolean cForm) {
		this.cForm = cForm;
	}
	

}
