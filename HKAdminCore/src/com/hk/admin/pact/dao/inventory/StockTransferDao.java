package com.hk.admin.pact.dao.inventory;

import java.util.Date;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface StockTransferDao extends BaseDao {

    public Page searchStockTransfer(Date createDate, String userLogin, Warehouse fromWarehouse, Warehouse toWarehouse, int pageNo, int perPage) ;
       
    public StockTransferLineItem getStockTransferLineItem(StockTransfer stockTransfer, ProductVariant productVariant, String batchNumber) ;

	public StockTransferLineItem getStockTransferLineItem(StockTransfer stockTransfer, Sku sku, SkuGroup skuGroup);

    public StockTransferLineItem getStockTransferLineItemForCheckedOutSkuGrp(SkuGroup checkedOutSkuGroup, StockTransfer stockTransfer );

    public StockTransferLineItem checkinSkuGroupExists(StockTransferLineItem stockTransferLineItem );
}
