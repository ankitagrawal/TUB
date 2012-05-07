package com.hk.pact.dao;

import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;

public interface ReconciliationStatusDao extends BaseDao {

    public ReconciliationStatus getReconciliationStatusById(EnumReconciliationStatus enumReconciliationStatus);

}
