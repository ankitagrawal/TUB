package com.hk.impl.service.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.store.Store;
import com.hk.pact.dao.store.StoreDao;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.store.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

  @Autowired
  private StoreDao storeDao;

  @Autowired
  private BaseDao baseDao;


  @Override
  public List<Store> getAllStores() {
    return getStoreDao().getAllStores();
  }

  @Override
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

  @Override
  public Store getStoreById(Long storeId) {
    return getBaseDao().load(Store.class, storeId);
  }

}
