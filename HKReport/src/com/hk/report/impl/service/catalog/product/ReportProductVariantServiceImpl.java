package com.hk.report.impl.service.catalog.product;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.pact.dao.catalog.product.ReportProductVariantDao;
import com.hk.report.pact.service.catalog.product.ReportProductVariantService;

@Service
public class ReportProductVariantServiceImpl implements ReportProductVariantService {

    private ReportProductVariantDao reportProductVariantDao;

    @Override
    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate) {
       return getReportProductVariantDao().findInventorySoldByDate(startDate, endDate);
    }

    @Override
    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId) {
        return getReportProductVariantDao().findInventorySoldByDateAndProduct(startDate, endDate,productId);
    }

    public ReportProductVariantDao getReportProductVariantDao() {
        return reportProductVariantDao;
    }

    public void setReportProductVariantDao(ReportProductVariantDao reportProductVariantDao) {
        this.reportProductVariantDao = reportProductVariantDao;
    }

}
