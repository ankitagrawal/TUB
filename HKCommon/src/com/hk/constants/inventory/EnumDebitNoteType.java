package com.hk.constants.inventory;

import com.hk.domain.accounting.DebitNoteType;

public enum EnumDebitNoteType {
	
	PreCheckin(10L, "PreCheckin"), PostCheckin(20L, "PostCheckin");
	
	private String name;
	private Long id;

	EnumDebitNoteType(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}
	
	public DebitNoteType asEnumDebitNoteType(){
		DebitNoteType debitNoteType = new DebitNoteType();
		debitNoteType.setId(this.id);
		debitNoteType.setName(this.name);
		return debitNoteType;
	}
	
	public static EnumDebitNoteType getById(Long id) {
	    for(EnumDebitNoteType e : values()) {
	        if(e.id.equals(id)) return e;
	    }
	    return null;
	 }

}
