package com.hk.report.dto.order;

import java.util.List;

import com.hk.report.dto.catalog.CategoryPerformanceDto;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Nov 7, 2011
 * Time: 12:46:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoriesOrderReportDto {

  private List<CategoryPerformanceDto> categoryPerformanceDtoList;
  private Integer totalOrders = 0;
  private Integer totalDistinctOrders = 0;
  private Integer totalMixedOrder = 0;
  private Long totalSumOfMrp = 0L;
  private Long totalSumOfProjectedMrp = 0L;
  private Long totalSumOfTargetMrp = 0L;

  public List<CategoryPerformanceDto> getCategoryPerformanceDtoList() {
    return categoryPerformanceDtoList;
  }

  public void setCategoryPerformanceDtoList(List<CategoryPerformanceDto> categoryPerformanceDtoList) {
    this.categoryPerformanceDtoList = categoryPerformanceDtoList;
  }

  public Integer getTotalOrders() {
    return totalOrders;
  }

  public void setTotalOrders(Integer totalOrders) {
    this.totalOrders = totalOrders;
  }

  public Integer getTotalDistinctOrders() {
    return totalDistinctOrders;
  }

  public void setTotalDistinctOrders(Integer totalDistinctOrders) {
    this.totalDistinctOrders = totalDistinctOrders;
  }

  public Integer getTotalMixedOrder() {
    return totalMixedOrder;
  }

  public void setTotalMixedOrder(Integer totalMixedOrder) {
    this.totalMixedOrder = totalMixedOrder;
  }

  public Long getTotalSumOfMrp() {
    return totalSumOfMrp;
  }

  public void setTotalSumOfMrp(Long totalSumOfMrp) {
    this.totalSumOfMrp = totalSumOfMrp;
  }

  public Long getTotalSumOfProjectedMrp() {
    return totalSumOfProjectedMrp;
  }

  public void setTotalSumOfProjectedMrp(Long totalSumOfProjectedMrp) {
    this.totalSumOfProjectedMrp = totalSumOfProjectedMrp;
  }

  public Long getTotalSumOfTargetMrp() {
    return totalSumOfTargetMrp;
  }

  public void setTotalSumOfTargetMrp(Long totalSumOfTargetMrp) {
    this.totalSumOfTargetMrp = totalSumOfTargetMrp;
  }
}
