package com.hk.web.action.admin.pos;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.pos.POSReportService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.Order;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.store.Store;
import com.hk.dto.pos.POSSaleItemDto;
import com.hk.dto.pos.POSSummaryDto;
import com.hk.pact.service.UserService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.Date;
import java.util.List;

@Secure(hasAnyPermissions = {PermissionConstants.REPORT_ADMIN}, authActionBean = AdminPermissionAction.class)
@Component
public class POSReportAction extends BaseAction {
  private List<Order> saleList;
  private List<ReverseOrder> returnItemList;
  private List<POSSaleItemDto> posSaleItems;
  private Date startDate;
  private Date endDate;
  private POSSummaryDto posSummaryDto;
  @Autowired
  private UserService userService;
  @Autowired
  private POSReportService posReportService;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/pos/posReportResult.jsp");
  }

  public Resolution generateSalesReportByDate() {
    Store store = userService.getWarehouseForLoggedInUser().getStore();
    if (store == null) {
      addRedirectAlertMessage(new SimpleMessage("Please select a Store"));
      return new ForwardResolution("/pages/pos/pos.jsp");
    }
    saleList = posReportService.storeSalesReport(store.getId(), startDate, endDate);
    returnItemList = posReportService.storeReturnReport(store.getId(), startDate, endDate);
    posSummaryDto = posReportService.storeDailySalesSummaryReport(saleList, returnItemList);
    posSaleItems =  posReportService.storeSalesReportWithDiscount(saleList);
    return new ForwardResolution("/pages/pos/posReportResult.jsp");
  }

  public Resolution generateDailySalesReport() {
    startDate = null;
    endDate = null;
    return generateSalesReportByDate();
  }

  public Resolution generateReturnReportByDate() {
    Long warehouseId = userService.getWarehouseForLoggedInUser().getId();
    if (warehouseId == null) {
      addRedirectAlertMessage(new SimpleMessage("Please select a Store"));
      return new ForwardResolution("/pages/pos/pos.jsp");
    }
    returnItemList = posReportService.storeReturnReport(warehouseId, startDate, endDate);
    return new ForwardResolution("/pages/pos/posReturnItemReportResult.jsp");
  }


  public Resolution generateDailyReturnReport() {
    startDate = null;
    endDate = null;
    return generateReturnReportByDate();
  }

  public List<Order> getSaleList() {
    return saleList;
  }

  public void setSaleList(List<Order> saleList) {
    this.saleList = saleList;
  }

  public List<ReverseOrder> getReturnItemList() {
    return returnItemList;
  }

  public void setReturnItemList(List<ReverseOrder> returnItemList) {
    this.returnItemList = returnItemList;
  }

  public POSSummaryDto getPosSummaryDto() {
    return posSummaryDto;
  }

  public void setPosSummaryDto(POSSummaryDto posSummaryDto) {
    this.posSummaryDto = posSummaryDto;
  }


  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public List<POSSaleItemDto> getPosSaleItems() {
    return posSaleItems;
  }

  public void setPosSaleItems(List<POSSaleItemDto> posSaleItems) {
    this.posSaleItems = posSaleItems;
  }
}
