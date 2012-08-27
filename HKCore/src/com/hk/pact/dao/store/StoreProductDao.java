package com.hk.pact.dao.store;

import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.store.Store;
import com.hk.domain.store.StoreProduct;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/21/12
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StoreProductDao extends BaseDao{

    public StoreProduct save(StoreProduct storeProduct);

    public StoreProduct getStoreProductByHKVariantAndStore(ProductVariant productVariant,Store store);

    public StoreProduct getStoreProductByHKVariantIdAndStoreId(String productVariantId, Long storeId);

    public List<StoreProduct> getProductListForStore(Store store);
}
