package com.hk.report.dto;


import java.util.ArrayList;
import java.util.List;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductFeature;

public class Table {

  List<Row> rows = new ArrayList<Row>();

  //      List<Row> rows;
  public List<Row> getRows() {
    return rows;
  }

  public void setRows(List<Row> rows) {
    this.rows = rows;
  }

  public boolean find(String rowName) {
    /*   if(rows==null)
   return false;*/
    if (rows.isEmpty())
      return false;
    for (Row row : rows) {
      if (row.getFeature().equals(rowName))
        return true;
    }
    return false;
  }

  /*public void addToRow(Product product, ProductFeature productFeature) {
    String rowName = productFeature.getName();
    for (Row row : rows) {
      if (row.getFeature().equals(rowName)) {
        row.cells.add(product.getName() +":"+ productFeature.getName());
      }
    }
  }*/
  public void addToTable(Product product, int productNo) {

//      if (rows != null) {
    for (Row row : rows) {
      row.cells.add("-");
    }
//      }
    List<ProductFeature> productFeatures = product.getProductFeatures();
    for (ProductFeature productFeature : productFeatures) {
      if (!(find(productFeature.getName()))) {
        Row row = new Row();
        List<String> temp = new ArrayList<String>();
        for (int i = 1; i < productNo; i++) {
          temp.add("-");
        }
        temp.add(productFeature.getValue());
        row.setFeature(productFeature.getName());
        row.setCells(temp);
        rows.add(row);
      } else {
        for (Row row : rows) {
          if (row.getFeature().equals(productFeature.getName())) {
//              row.cells.remove(productNo);
//              row.cells.add(productFeature.getValue());
            row.cells.set(productNo - 1, productFeature.getValue());
            break;
          }
        }
      }
    }
  }

}
