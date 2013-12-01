package com.hk.web.action.admin.catalog.product;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class BulkEditOptions {

  Map<String, Boolean> options = new HashMap<String, Boolean>();

  public Map<String, Boolean> getOptions() {
    return options;
  }

  public void setOptions(Map<String, Boolean> options) {
    this.options = options;
  }

}