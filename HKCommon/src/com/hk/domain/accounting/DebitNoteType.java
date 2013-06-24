package com.hk.domain.accounting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "debit_note_type")
public class DebitNoteType {
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

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

	@Override
	public String toString() {
		return id == null ? "" : id.toString();
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
