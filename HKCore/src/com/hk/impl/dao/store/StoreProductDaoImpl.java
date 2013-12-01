package com.hk.impl.dao.store;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.store.Store;
import com.hk.domain.store.StoreProduct;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.store.StoreProductDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/21/12
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class StoreProductDaoImpl extends BaseDaoImpl implements StoreProductDao {

    public StoreProduct save(StoreProduct storeProduct) {

        return (StoreProduct) super.save(storeProduct);
    }

    public StoreProduct getStoreProductByHKVariantAndStore(ProductVariant productVariant, Store store){
        return (StoreProduct) findUniqueByNamedParams(" from StoreProduct sp where sp.productVariant = :productVariant and sp.store = :store ", new String[]{"productVariant","store"}, new Object[]{productVariant,store});
    }

    public StoreProduct getStoreProductByHKVariantIdAndStoreId(String productVariantId, Long storeId){
        return (StoreProduct) findUniqueByNamedParams(" from StoreProduct sp where sp.productVariant.id = :productVariantId and sp.store.id = :storeId", new String[]{"productVariantId","storeId"}, new Object[]{productVariantId,storeId});
    }

    public List<StoreProduct> getProductListForStore(Store store){
        return (List<StoreProduct>) findByNamedParams("from StoreProduct sp where sp.store =:store", new String[]{"store"},new Object[]{store});
    }
}
