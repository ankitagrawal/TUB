package com.hk.impl.dao;

import org.springframework.stereotype.Repository;

import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.pact.dao.ReconciliationStatusDao;

@Repository
public class ReconciliationStatusDaoImpl extends BaseDaoImpl implements ReconciliationStatusDao {

    public ReconciliationStatus getReconciliationStatusById(EnumReconciliationStatus enumReconciliationStatus) {
        return get(ReconciliationStatus.class, enumReconciliationStatus.getId());
    }

}
