package com.hk.db.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.pact.dao.BaseDao;

@Component
public abstract class BaseSeedData {

    @Autowired
    private BaseDao baseDao;

    protected Object save(Object entity) {
        return getBaseDao().save(entity);
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
