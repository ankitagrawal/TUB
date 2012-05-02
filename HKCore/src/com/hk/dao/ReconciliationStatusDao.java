package com.hk.dao;

import org.springframework.stereotype.Repository;

import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.inventory.rv.ReconciliationStatus;

@Repository
public class ReconciliationStatusDao extends BaseDaoImpl {

    
    public ReconciliationStatus getReconciliationStatusById(EnumReconciliationStatus enumReconciliationStatus){
        return get(ReconciliationStatus.class, enumReconciliationStatus.getId());
    }

}

