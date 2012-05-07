package com.hk.pact.dao.store;

import java.util.List;

import com.hk.domain.store.Store;
import com.hk.pact.dao.BaseDao;

public interface StoreDao extends BaseDao {

    public List<Store> getAllStores();

    public Store getStoreById(Long storeId);

}
