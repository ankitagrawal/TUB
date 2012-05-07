package com.hk.pact.service.store;

import java.util.List;

import com.hk.domain.store.Store;

public interface StoreService {

    public static final Long DEFAULT_STORE_ID = 1L;


    public List<Store> getAllStores() ;
    /**
     * Default store will be healthkart
     * 
     * @return Store
     */
    public Store getDefaultStore() ;

}
