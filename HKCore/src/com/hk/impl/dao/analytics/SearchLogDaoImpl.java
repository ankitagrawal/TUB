package com.hk.impl.dao.analytics;

import com.hk.domain.search.SearchLog;
import com.hk.domain.search.Synonym;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.analytics.SearchLogDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 18 May, 2013
 * Time: 12:43:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SearchLogDaoImpl extends BaseDaoImpl implements SearchLogDao {
    public SearchLog getLatestSearchLog(Long trafficTrackingId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SearchLog.class);
        detachedCriteria.add(Restrictions.eq("trafficTrackingId", trafficTrackingId));
        detachedCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        List<SearchLog> list = (List<SearchLog>) findByCriteria(detachedCriteria, 0, 1);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    @Override
    public Synonym getCategoryBrandSynonym(String searchTerm, boolean categorySearch, boolean brandSearch) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Synonym.class);
        detachedCriteria.add(Restrictions.eq("searchSynonym", searchTerm));
        detachedCriteria.add(Restrictions.eq("deleted", false));
        List<Synonym> synonyms = findByCriteria(detachedCriteria);
        return synonyms != null && !synonyms.isEmpty() ? synonyms.get(0) : null;
    }
}
