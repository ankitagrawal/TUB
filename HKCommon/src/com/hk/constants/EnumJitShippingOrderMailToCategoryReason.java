package com.hk.constants;

public enum EnumJitShippingOrderMailToCategoryReason {
	
	SO_CANCELLED(10L, "so canclted"),
	SO_WAREHOUSE_FLIPPED(30L, "so warehouse flipped"),
	SO_SPLITTED(30L, "so splitted");
	
	
	private String name;
	  private Long id;

	  EnumJitShippingOrderMailToCategoryReason(Long id, String name) {
	    this.name = name;
	    this.id = id;
	  }

	  public String getName() {
	    return name;
	  }

	  public Long getId() {
	    return id;
	  }

}
