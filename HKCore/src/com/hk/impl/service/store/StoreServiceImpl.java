package com.hk.impl.service.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.store.Store;
import com.hk.domain.store.StoreProduct;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.store.StoreDao;
import com.hk.pact.dao.store.StoreProductDao;
import com.hk.pact.service.store.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private StoreProductDao storeProductDao;


    public List<Store> getAllStores() {
        return getStoreDao().getAllStores();
    }

    public Store getDefaultStore() {
        return getStoreDao().getStoreById(DEFAULT_STORE_ID);
    }

    public StoreDao getStoreDao() {
        return storeDao;
    }

    public void setStoreDao(StoreDao storeDao) {
        this.storeDao = storeDao;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public Store getStoreById(Long storeId) {
        return getStoreDao().getStoreById(storeId);
    }

    public StoreProduct saveStoreProduct(StoreProduct storeProduct){
        return storeProductDao.save(storeProduct);
    }

    public StoreProduct getStoreProductByHKVariantAndStore(ProductVariant productVariant, Store store){
        return storeProductDao.getStoreProductByHKVariantAndStore(productVariant, store);
    }

    public StoreProduct getStoreProductByHKVariantIDAndStoreId(String productVariantId, Long storeId){
        return storeProductDao.getStoreProductByHKVariantIdAndStoreId(productVariantId, storeId);
    }

    public List<StoreProduct> getProductListForStore(Store store){
        return storeProductDao.getProductListForStore(store);
    }

}
