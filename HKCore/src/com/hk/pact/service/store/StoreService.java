package com.hk.pact.service.store;

import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.store.Store;
import com.hk.domain.store.StoreProduct;

public interface StoreService {

    public static final Long DEFAULT_STORE_ID = 1L;
    public static final Long MIH_STORE_ID     = 2L;
    public static final Long LOYALTYPG_ID     = 4L;
	public static final Long PUNJABI_BAGH     = 101L;

    public List<Store> getAllStores();

    /**
     * Default store will be healthkart
     * 
     * @return Store
     */
    public Store getDefaultStore();

    public Store getStoreById(Long storeId);

    public StoreProduct saveStoreProduct(StoreProduct storeProduct);

    public StoreProduct getStoreProductByHKVariantAndStore(ProductVariant productVariant, Store store);

    public StoreProduct getStoreProductByHKVariantIDAndStoreId(String productVariantId, Long storeId);

    public List<StoreProduct> getProductListForStore(Store store);

}
