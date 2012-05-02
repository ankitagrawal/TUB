package com.hk.report.pact.dao.catalog.product;

import java.util.Date;
import java.util.List;

import com.hk.report.dto.inventory.InventorySoldDto;

public interface ReportProductVariantDao{
	
	public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);


}
