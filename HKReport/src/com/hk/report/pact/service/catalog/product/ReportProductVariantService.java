package com.hk.report.pact.service.catalog.product;

import java.util.Date;
import java.util.List;

import com.hk.report.dto.inventory.InventorySoldDto;

public interface ReportProductVariantService {
    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);

}
