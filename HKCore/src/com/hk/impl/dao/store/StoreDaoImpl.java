package com.hk.impl.dao.store;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.store.Store;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.store.StoreDao;

@Repository
public class StoreDaoImpl extends BaseDaoImpl implements StoreDao {

    @Override
    public List<Store> getAllStores() {
        return getAll(Store.class);
    }

    @Override
    public Store getStoreById(Long storeId) {
        return get(Store.class, storeId);
    }

}
