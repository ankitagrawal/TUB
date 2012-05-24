package com.hk.pact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.core.Pincode;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.TestTxnService;

@Service
public class TestTxnServiceImpl implements TestTxnService {

    @Autowired
    private BaseDao baseDao;

    @Transactional
    public void runTest() {
        addTestBadge();
        deleteTestBadge();
    }

    @Override
    public void addTestBadge() {

        // TestTxnService testService = ServiceLocatorFactory.getService(TestTxnService.class);

        Pincode pincode = new Pincode();
        pincode.setCity("test2");
        String code = Double.valueOf(Math.random()).toString().substring(0, 4);
        pincode.setPincode(code);
        pincode.setState("testState2");

        getBaseDao().save(pincode);

        /*
         * pincode = new Pincode(); pincode.setCity("test2"); pincode.setPincode(code); pincode.setState("testState2");
         * getBaseDao().save(pincode);
         */
    }

    @Override
    public void deleteTestBadge() {

        throw new UnsupportedOperationException();
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
