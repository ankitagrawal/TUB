package com.hk.admin.impl.dao.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class RunSheetDaoImpl extends BaseDaoImpl implements RunSheetDao{
    
    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, String runsheetStatus, User agent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
