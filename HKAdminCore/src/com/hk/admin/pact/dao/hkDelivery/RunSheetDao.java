package com.hk.admin.pact.dao.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

import java.util.Date;


public interface RunSheetDao extends BaseDao{
    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, String runsheetStatus, User agent );
}
