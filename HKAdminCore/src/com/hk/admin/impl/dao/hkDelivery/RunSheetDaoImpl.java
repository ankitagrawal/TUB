package com.hk.admin.impl.dao.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class RunSheetDaoImpl extends BaseDaoImpl implements RunSheetDao {

    @Override
    public void saveRunSheet(Runsheet runsheet) {
        save(runsheet);
    }

    @Override
    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage) {
        DetachedCriteria runsheetCriteria = DetachedCriteria.forClass(Runsheet.class);

        DetachedCriteria supplierCriteria = null;

        if (runsheet != null) {
            runsheetCriteria.add(Restrictions.eq("id", runsheet.getId()));
        }
        if (startDate != null) {
            runsheetCriteria.add(Restrictions.ge("createDate", startDate));
        }
        if (endDate != null) {
            runsheetCriteria.add(Restrictions.le("createDate", endDate));
        }
        if (runsheetStatus != null) {
            runsheetCriteria.add(Restrictions.eq("runsheetStatus", runsheetStatus));
        }
        if (agent != null) {
            runsheetCriteria.add(Restrictions.eq("userId", agent));
        }
        if (hub != null) {
            runsheetCriteria.add(Restrictions.eq("hub", hub));
        }

        runsheetCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(runsheetCriteria, pageNo, perPage);
    }
}
