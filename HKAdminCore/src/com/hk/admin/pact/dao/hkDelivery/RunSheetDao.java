package com.hk.admin.pact.dao.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.domain.hkDelivery.Runsheet;

import java.util.Date;
import java.util.List;


public interface RunSheetDao extends BaseDao {

    public Runsheet saveRunSheet(Runsheet runsheet);

    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage);

    public List<User> getAgentList(RunsheetStatus runsheetStatus, Hub hub);

    public List<Runsheet> getRunsheetForAgent(User agent);

    public Runsheet getOpenRunsheetForAgent(User agent);
}
