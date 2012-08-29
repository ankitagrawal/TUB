package com.hk.admin.impl.dao.marketing;

import java.util.Date;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.marketing.MarketingExpenseDao;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.marketing.AdNetworks;
import com.hk.domain.marketing.MarketingExpense;
import com.hk.impl.dao.BaseDaoImpl;


@Repository
public class MarketingExpenseDaoImpl extends BaseDaoImpl implements MarketingExpenseDao {

    @SuppressWarnings("unchecked")
    public Page searchMarketingExpense(Category category, Long adNetworksId, Date startDate, Date endDate, int pageNo, int perPage) {
        DetachedCriteria marketingExpenseCriteria = DetachedCriteria.forClass(MarketingExpense.class);
        // Criteria marketingExpenseCriteria = getSession().createCriteria(MarketingExpense.class);
        if (startDate != null) {
            marketingExpenseCriteria.add(Restrictions.ge("date", startDate));
        }
        if (endDate != null) {
            marketingExpenseCriteria.add(Restrictions.le("date", endDate));
        }
        if (category != null) {
            marketingExpenseCriteria.add(Restrictions.eq("category", category));
        }
        if(adNetworksId != null)
        {
            DetachedCriteria adnetworksCriteria=marketingExpenseCriteria.createCriteria("adNetworks");
           adnetworksCriteria.add(Restrictions.eq("id", adNetworksId));
        }
        marketingExpenseCriteria.addOrder(org.hibernate.criterion.Order.desc("date"));
        return list(marketingExpenseCriteria, pageNo, perPage);
    }

   public MarketingExpense checkUniqueMarketingExpense(Category category, Date date, AdNetworks adNetworks) {
        MarketingExpense marketingExpense = null;
        marketingExpense = (MarketingExpense) getSession().createQuery(
                "select m from MarketingExpense m where m.category = :category and m.date =:date and m.adNetworks =:adNetworks").setParameter("category", category).setParameter(
                "date", date).setParameter("adNetworks", adNetworks).uniqueResult();
        return marketingExpense;
    }

}
