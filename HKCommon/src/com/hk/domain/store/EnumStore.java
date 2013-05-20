package com.hk.domain.store;


public enum EnumStore {
	
	HEALTHKART(1L, "HK"),
	MADEINHEALTH(2L, "MIH"),
	FITNESSPRO(3L, "FP"),
	LOYALTYPG(4L, "LP");
	
	private Long id;
	private String prefix;
	
	private EnumStore(Long id, String prefix) {
		this.id = id;
		this.prefix = prefix;
	}

	public Long getId() {
		return id;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	 public Store asStore() {
		 Store store = new Store();
		 store.setId(this.id);
		 store.setPrefix(this.prefix);
		 return store;
	 }
}
