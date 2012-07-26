package com.hk.report.impl.service.catalog.product;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.report.dto.inventory.*;
import com.hk.report.pact.dao.catalog.product.ReportProductVariantDao;
import com.hk.report.pact.service.catalog.product.ReportProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class ReportProductVariantServiceImpl implements ReportProductVariantService {
    @Autowired
    private ReportProductVariantDao reportProductVariantDao;

    @Autowired
    private ProductVariantDao productVariantDao;

    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate) {
        return getReportProductVariantDao().findInventorySoldByDate(startDate, endDate);
    }

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId) {
        return getReportProductVariantDao().findInventorySoldByDateAndProduct(startDate, endDate,productId);
    }

    public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse) {
        List<ExpiryAlertReportDto> expiryAlertReportDtoList = reportProductVariantDao.getToBeExpiredProductDetails(startDate, endDate, warehouse);
        List<ExpiryAlertReportDto> expiryAlertReturnList = new ArrayList<ExpiryAlertReportDto>();

        for(ExpiryAlertReportDto expiryAlertReport : expiryAlertReportDtoList) {
            SkuGroup skuGroup= expiryAlertReport.getSkuGroup();
            ProductVariant productVariant = skuGroup.getSku().getProductVariant();
            expiryAlertReport.setProductName(productVariant.getProduct().getName());
            expiryAlertReport.setBatchNumber(skuGroup.getBatchNumber());
            expiryAlertReport.setProductVariantId(productVariant.getId());
            expiryAlertReport.setProductOption(productVariant.getOptionsSlashSeparated());
            expiryAlertReport.setBatchQty(expiryAlertReport.getBatchQty());
            expiryAlertReturnList.add(expiryAlertReport);
        }
        return expiryAlertReturnList;
    }

    public StockReportDto getStockDetailsByProductVariant(String productVariantId, Warehouse warehouse, Date startDate, Date endDate) {

        ProductVariant productVariant = productVariantDao.getVariantById(productVariantId);

        StockReportDto stockReportDtoStore = new StockReportDto();
        stockReportDtoStore.setProductVariant(productVariantId);
        stockReportDtoStore.setProductName( productVariant.getProduct().getName() );
        stockReportDtoStore.setProductOption( productVariant.getOptionsSlashSeparated() );

        Long openingStock = reportProductVariantDao.getOpeningStockOfProductVariantOnDate(productVariantId, startDate, warehouse);
        stockReportDtoStore.setOpeningStock(openingStock);
        stockReportDtoStore.setStockLeft(reportProductVariantDao.getStockLeftQty(productVariantId, warehouse));
        List<StockReportDto> stockReportDtoList = reportProductVariantDao.getProductVariantStockBetweenDates(productVariantId, startDate, endDate, warehouse);
        for(StockReportDto stockReportDto : stockReportDtoList) {
            if(stockReportDto.getInventoryTxnType() != null ){
                if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.INV_CHECKIN.getId().longValue()) {
                    stockReportDtoStore.setGrnCheckin(stockReportDto.getInventoryQty());
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.INV_CHECKOUT.getId().longValue()) {
                    stockReportDtoStore.setLineItemCheckout(stockReportDto.getInventoryQty() * -1L);
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.RTO_CHECKIN.getId().longValue()) {
                    stockReportDtoStore.setRtoCheckin(stockReportDto.getInventoryQty());
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.RV_DAMAGED.getId().longValue()) {
                    stockReportDtoStore.setReconcileCheckout(stockReportDto.getInventoryQty() * -1L);
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.RV_CHECKIN.getId().longValue()) {
                    stockReportDtoStore.setReconcileCheckin(stockReportDto.getInventoryQty());
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.CANCEL_CHECKIN.getId().longValue()) {
                    stockReportDtoStore.setCancelCheckin(stockReportDto.getInventoryQty());
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.INV_REPEAT_CHECKOUT.getId().longValue()) {
                    stockReportDtoStore.setInventoryRepeatCheckout(stockReportDto.getInventoryQty() * -1L);
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.RV_EXPIRED.getId().longValue()) {
                    stockReportDtoStore.setRvExpired(stockReportDto.getInventoryQty() * -1L);
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.RV_LOST_PILFERAGE.getId().longValue()) {
                    stockReportDtoStore.setRvLostPilferage(stockReportDto.getInventoryQty() * -1L);
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.STOCK_TRANSFER_CHECKIN.getId().longValue()) {
                    stockReportDtoStore.setStockTransferCheckin(stockReportDto.getInventoryQty());
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.STOCK_TRANSFER_CHECKOUT.getId().longValue()) {
                    stockReportDtoStore.setStockTransferCheckout(stockReportDto.getInventoryQty() * -1L);
                }else if(stockReportDto.getInventoryTxnType().longValue() == EnumInvTxnType.TRANSIT_LOST.getId().longValue()) {
                    stockReportDtoStore.setTransitLost(stockReportDto.getInventoryQty() * -1L);
                }
            }
        }
        Long damageRtoCheckin = reportProductVariantDao.getDamageRtoCheckedInQty(productVariantId, startDate, endDate, warehouse);
        stockReportDtoStore.setDamageCheckin(damageRtoCheckin);
        return stockReportDtoStore;
    }

    public List<RTOReportDto> getRTOProductsDetail(Date startDate, Date endDate) {
        List<ShippingOrder> shippingOrderList = reportProductVariantDao.getShippingOrdersByReturnDate(startDate, endDate, EnumShippingOrderStatus.SO_Returned);
        Iterator<ShippingOrder> iteratorShippingOrder = shippingOrderList.iterator();
        List<RTOReportDto> rtoReportDtoList = new ArrayList<RTOReportDto>();
        RTOReportDto rtoReportDto;

        while(iteratorShippingOrder.hasNext()) {
            ShippingOrder shippingOrder = iteratorShippingOrder.next();
            List<RTOFineReportDto> rtoFineReportDtoList = reportProductVariantDao.getRTOFineProductVariantDetails(shippingOrder);
            List<RTODamageReportDto> rtoDamageReportDtoList = reportProductVariantDao.getRTODamageProductVariantDetails(shippingOrder);

            for(RTOFineReportDto rtoFineReportDto : rtoFineReportDtoList) {
                rtoReportDto = new RTOReportDto();
                Long rtoCheckInQty = rtoFineReportDto.getRtoCheckinCount();
                ProductVariant productVariant = rtoFineReportDto.getProductVariant();
                rtoReportDto.setProductName(productVariant.getProduct().getName());
                rtoReportDto.setProductOptions(productVariant.getOptionsSlashSeparated());
                rtoReportDto.setProductVariantId(productVariant.getId());
                rtoReportDto.setRtoCheckinQty(rtoCheckInQty);
                rtoReportDto.setRtoDamageCheckinQty(0L);
                rtoReportDto.setRtoDate(shippingOrder.getShipment().getReturnDate());
                rtoReportDto.setShippingOrderNumber(shippingOrder.getId());

                rtoReportDtoList.add(rtoReportDto);
            }

            for(RTODamageReportDto rtoFineReportDto : rtoDamageReportDtoList) {
                rtoReportDto = new RTOReportDto();
                Long rtoDamageCheckInQty = rtoFineReportDto.getRtoDamageCheckinCount();
                ProductVariant productVariant = rtoFineReportDto.getProductVariant();
                rtoReportDto.setProductName(productVariant.getProduct().getName());
                rtoReportDto.setProductOptions(productVariant.getOptionsSlashSeparated());
                rtoReportDto.setProductVariantId(productVariant.getId());
                rtoReportDto.setRtoCheckinQty(0L);
                rtoReportDto.setRtoDamageCheckinQty(rtoDamageCheckInQty);
                rtoReportDto.setRtoDate(shippingOrder.getShipment().getReturnDate());
                rtoReportDto.setShippingOrderNumber(shippingOrder.getId());

                rtoReportDtoList.add(rtoReportDto);
            }
        }
        return rtoReportDtoList;
    }

    public List<RVReportDto> getReconciliationVoucherDetail(String productVariantId, Warehouse warehouse, Date startDate, Date endDate) {
        List<RVReportDto> rvReportDtoList = reportProductVariantDao.getReconciliationVoucherDetail(productVariantId, warehouse, startDate, endDate);
        return rvReportDtoList;
    }

    public List<GrnLineItem> getPurchaseOrderByProductVariant(ProductVariant productVariant, Warehouse warehouse, Date startDate, Date endDate){
        return getReportProductVariantDao().getPurchaseOrderByProductVariant(productVariant, warehouse, startDate, endDate);
    }

    public ReportProductVariantDao getReportProductVariantDao() {
        return reportProductVariantDao;
    }

    public void setReportProductVariantDao(ReportProductVariantDao reportProductVariantDao) {
        this.reportProductVariantDao = reportProductVariantDao;
    }

    public ProductVariantDao getProductVariantDao() {
        return productVariantDao;
    }

    public void setProductVariantDao(ProductVariantDao productVariantDao) {
        this.productVariantDao = productVariantDao;
    }
}
