package com.hk.report.dto;

import java.util.List;

public class Row {

  String feature;
  public List<String> cells;

  public List<String> getCells() {
    return cells;
  }

  public String getFeature() {
    return feature;
  }

  public void setFeature(String feature) {
    this.feature = feature;
  }

  public void setCells(List<String> cells) {
    this.cells = cells;
  }
}
