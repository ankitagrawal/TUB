package com.hk.admin.impl.dao.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RunSheetDaoImpl extends BaseDaoImpl implements RunSheetDao {

    @Override
    public Runsheet saveRunSheet(Runsheet runsheet) {
        return (Runsheet)save(runsheet);
    }

    @Override
    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage) {
        DetachedCriteria runsheetCriteria = DetachedCriteria.forClass(Runsheet.class);

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
            runsheetCriteria.add(Restrictions.eq("agent", agent));
        }
        if (hub != null) {
            runsheetCriteria.add(Restrictions.eq("hub", hub));
        }

        runsheetCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(runsheetCriteria, pageNo, perPage);
    }

    @Override
    public List<User> getAgentList(RunsheetStatus runsheetStatus, Hub hub) {

        if(hub != null){
            String query = "select rs.agent from Runsheet rs where rs.runsheetStatus = :runsheetStatus and rs.hub = :hub ";
            return (List<User>) findByNamedParams(query, new String[]{"runsheetStatus", "hub"}, new Object[]{runsheetStatus, hub});
        }
        else{
            String query = "select rs.agent from Runsheet rs where rs.runsheetStatus = :runsheetStatus ";
            return (List<User>) findByNamedParams(query, new String[]{"runsheetStatus"}, new Object[]{runsheetStatus});
        }
    }

    @Override
    public List<Runsheet> getRunsheetForAgent(User agent) {
        String query = "from Runsheet rs where rs.agent = :agent";
        return (List<Runsheet>) findByNamedParams(query, new String[]{"agent"}, new Object[]{agent});
    }

    @Override
    public Runsheet getOpenRunsheetForAgent(User agent) {
        Long openRunsheetStatusId = EnumRunsheetStatus.Open.getId();
        String query = "from Runsheet rs where rs.agent = :agent and rs.runsheetStatus.id = :openRunsheetStatusId";
        return (Runsheet) findUniqueByNamedParams(query, new String[]{"agent","openRunsheetStatusId"}, new Object[]{agent,openRunsheetStatusId});
    }
}
