package com.hk.admin.impl.service.pos;

import com.hk.admin.pact.dao.order.AdminOrderDao;
import com.hk.admin.pact.service.pos.POSReportService;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.dto.pos.POSSaleItemDto;
import com.hk.dto.pos.POSSummaryDto;
import com.hk.dto.pos.PosSkuGroupSearchDto;
import com.hk.pact.dao.courier.ReverseOrderDao;
import com.hk.util.io.HkXlsWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class POSReportServiceImpl implements POSReportService {

  @Autowired
  private AdminOrderDao adminOrderDao;
  @Autowired
  private ReverseOrderDao reverseOrderDao;

  public List<Order> storeSalesReport(Long storeId, Date startDate, Date endDate) {
    if (startDate == null) {
      startDate = getStartDate();
    }
    if (endDate == null) {
      endDate = new Date();
    }
    OrderStatus orderStatusReturned = EnumOrderStatus.Delivered.asOrderStatus();
    List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
    orderStatusList.add(orderStatusReturned);
    return adminOrderDao.findSaleForTimeFrame(storeId, startDate, endDate, orderStatusList);
  }

  public List<POSSaleItemDto> storeSalesReportWithDiscount(List<Order> orders) {
    List<POSSaleItemDto> posSaleItems = new ArrayList<POSSaleItemDto>();
    for (Order order : orders) {
      double discount = 0.0;
      if (order.getCartLineItems() != null) {
        for (CartLineItem lineItem : order.getCartLineItems()) {
          discount = discount + lineItem.getDiscountOnHkPrice();
        }
        posSaleItems.add(new POSSaleItemDto(discount, order));
      }
    }
    return posSaleItems;
  }

  public POSSummaryDto storeDailySalesSummaryReport(List<Order> saleList, List<ReverseOrder> returnList) {
    double creditCardAmtCollected = 0.0;
    double creditCardAmtRefunded = 0.0;
    double cashAmtCollected = 0.0;
    double cashAmtRefunded = 0.0;
    double totalAmountCollected = 0.0;
    double avgAmtPerInvoice = 0.0;
    Long itemsSold = 0L;
    Long itemReturned = 0L;
    Long noOfBills = 0L;
    Double rewardPointsRedeemed = 0.0;
    for (Order order : saleList) {
      if (order.getShippingOrders() != null) {
        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
          if (shippingOrder.getLineItems() != null) {
            for (LineItem lineItem : shippingOrder.getLineItems()) {
              itemsSold = itemsSold + lineItem.getQty();
            }
          }
        }
      }
      for (Payment payment : order.getPayments()) {
        if (payment.getPaymentMode().getId().equals(EnumPaymentMode.COUNTER_CASH.getId())) {
          cashAmtCollected = cashAmtCollected + payment.getAmount();
        } else if (payment.getPaymentMode().getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId())) {
          creditCardAmtCollected = creditCardAmtCollected + payment.getAmount();
        }
      }
      if(order.getRewardPointsUsed() != null && order.getRewardPointsUsed().doubleValue() != 0.0){
        rewardPointsRedeemed += order.getRewardPointsUsed();
      }
    }
    for (ReverseOrder reverseOrder : returnList) {
      if (reverseOrder.getAmount() != null) {
        cashAmtRefunded = cashAmtRefunded + reverseOrder.getAmount();
      }
      if (reverseOrder.getReverseLineItems() != null) {
        itemReturned = itemReturned + reverseOrder.getReverseLineItems().size();
      }
    }
    totalAmountCollected = cashAmtCollected + creditCardAmtCollected - cashAmtRefunded;

    if (saleList != null && saleList.size() > 0) {
      avgAmtPerInvoice = totalAmountCollected / saleList.size();
      noOfBills = Long.valueOf(saleList.size());
    }
    return (new POSSummaryDto(cashAmtCollected, cashAmtRefunded, creditCardAmtCollected, creditCardAmtRefunded,
        itemsSold, itemReturned, totalAmountCollected, avgAmtPerInvoice, noOfBills, rewardPointsRedeemed));
  }

  public List<ReverseOrder> storeReturnReport(Long storeId, Date startDate, Date endDate) {
    if (startDate == null) {
      startDate = getStartDate();
    }
    if (endDate == null) {
      endDate = new Date();
    }
    return reverseOrderDao.findReverseOrderForTimeFrame(storeId, startDate, endDate);
  }

  public Date getStartDate() {
    Date date = new Date();
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  public File generatePosStockReport(File xlsFile, List<PosSkuGroupSearchDto> posSkuGroupSearchDtoList) {
    HkXlsWriter xlsWriter = new HkXlsWriter();
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    if (posSkuGroupSearchDtoList != null) {
      int xlsRow = 1;
      xlsWriter.addHeader("PRODUCT VARIANT ID", "PRODUCT VARIANT ID");
      xlsWriter.addHeader("PRODUCT NAME", "PRODUCT NAME");
      xlsWriter.addHeader("SIZE", "SIZE");
      xlsWriter.addHeader("FLAVOR", "FLAVOR");
      xlsWriter.addHeader("COLOR", "COLOR");
      xlsWriter.addHeader("FORM", "FORM");
      xlsWriter.addHeader("BATCH NUMBER", "BATCH NUMBER");
      xlsWriter.addHeader("COST PRICE", "COST PRICE");
      xlsWriter.addHeader("MRP", "MRP");
      xlsWriter.addHeader("MFG DATE", "MFG DATE");

      xlsWriter.addHeader("EXPIRY DATE", "EXPIRY DATE");
      xlsWriter.addHeader("AVAILABLE INVENTORY", "AVAILABLE INVENTORY");

      for (PosSkuGroupSearchDto posSkuGroupSearchDto : posSkuGroupSearchDtoList) {
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getProductVariantId());
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getProductName());
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getSize());
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getFlavor());
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getColor());
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getForm());
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getBatchNumber());
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getCostPrice());
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getMrp());
        if (posSkuGroupSearchDto.getMfgDate() == null) {
          xlsWriter.addCell(xlsRow, "");
        } else {
          xlsWriter.addCell(xlsRow, dateFormat.format(posSkuGroupSearchDto.getMfgDate()));
        }
        if (posSkuGroupSearchDto.getExpiryDate() == null) {
          xlsWriter.addCell(xlsRow, "");
        } else {
          xlsWriter.addCell(xlsRow, dateFormat.format(posSkuGroupSearchDto.getExpiryDate()));
        }
        xlsWriter.addCell(xlsRow, posSkuGroupSearchDto.getAvailableInventory());
        xlsRow++;
      }
      xlsWriter.writeData(xlsFile, "Pos_Stock_Report");
    }
    return xlsFile;
  }
}
