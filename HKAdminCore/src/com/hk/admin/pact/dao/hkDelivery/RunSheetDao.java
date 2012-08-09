package com.hk.admin.pact.dao.hkDelivery;

import com.hk.pact.dao.BaseDao;
import com.hk.domain.hkDelivery.Runsheet;


public interface RunSheetDao extends BaseDao {

    public void createRunSheet(Runsheet runsheet);
}
