package com.hk.admin.impl.dao.hkDelivery;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.domain.hkDelivery.Runsheet;
import org.springframework.stereotype.Repository;

@Repository
public class RunSheetDaoImpl extends BaseDaoImpl implements RunSheetDao {

    @Override
    public void createRunSheet(Runsheet runsheet) {
        save(runsheet);
    }
}
