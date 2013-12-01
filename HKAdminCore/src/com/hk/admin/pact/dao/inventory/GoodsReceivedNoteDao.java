package com.hk.admin.pact.dao.inventory;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface GoodsReceivedNoteDao extends BaseDao {

    public List<GoodsReceivedNote> getGRNByPO(PurchaseOrder purchaseOrder);

    public List<GoodsReceivedNote> listGRNsExcludingStatus(List<Long> grnStatusList);

    public List<GoodsReceivedNote> listGRNsWithProductVariant(ProductVariant productVariant);

    public Page searchGRN(GoodsReceivedNote grn, GrnStatus grnStatus, String invoiceNumber, String tinNumber, String supplierName, Boolean isReconciled, Warehouse warehouse,
                          int pageNo, int perPage);

    public List<GoodsReceivedNote> searchGRN(GoodsReceivedNote grn, GrnStatus grnStatus, String invoiceNumber, String tinNumber, String supplierName, Boolean isReconciled,
                                             Warehouse warehouse);

    public List<GoodsReceivedNote> listGRNsIncludingStatus(List<Long> grnStatusList);

    public List<GoodsReceivedNote> listGRNsExcludingStatusInTimeFrame(Long grnStatusValue, Date startDate, Date endDate, Warehouse warehouse, Boolean reconciled);

    public List<GoodsReceivedNote> checkinCompletedGrns(Date startDate);
    
    public List<GoodsReceivedNote> checkinCompleteAndClosedGrns(Date startDate);


}
