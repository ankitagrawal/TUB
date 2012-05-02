package com.hk.dao.affiliate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.constants.EnumAffiliateTxnType;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTxn;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.order.Order;


@Repository
public class AffiliateTxnDao extends BaseDaoImpl {

    @SuppressWarnings("unchecked")
    public List<AffiliateTxn> getTxnListByAffiliate(Affiliate affiliate) {
        String queryString = "from AffiliateTxn a where a.affiliate=:affiliate";
        return findByNamedParams(queryString, new String[] { "affiliate" }, new Object[] { affiliate });
    }

    public AffiliateTxn getTxnByOrder(Order order) {
        String queryString = "from AffiliateTxn aT where aT.order =:order";
        return (AffiliateTxn) findUniqueByNamedQueryAndNamedParam(queryString, new String[] { "order" }, new Object[] { order });
    }

    public AffiliateTxn saveTxn(Affiliate affiliate, Double amountToAdd, AffiliateTxnType affiliateTxnType, Order order) {
        AffiliateTxn affiliateTxn = new AffiliateTxn();
        affiliateTxn.setAffiliate(affiliate);
        if (affiliateTxnType.getId() == 10L) {
            affiliateTxn.setAmount(amountToAdd);
        } else if (affiliateTxnType.getId() == 20L) {
            affiliateTxn.setAmount(amountToAdd * -1);
        }
        affiliateTxn.setAffiliateTxnType(affiliateTxnType);
        affiliateTxn.setOrder(order);
        affiliateTxn.setDate(new Date());
        return (AffiliateTxn) save(affiliateTxn);
    }

    @SuppressWarnings("unchecked")
    public Page getReferredOrderListByAffiliate(Affiliate affiliate, Date startDate, Date endDate, int pageNo, int perPage) {

        List<Long> applicableTxnTypes = new ArrayList<Long>();
        applicableTxnTypes.add(EnumAffiliateTxnType.ADD.getId());
        applicableTxnTypes.add(EnumAffiliateTxnType.ORDER_CANCELLED.getId());

        DetachedCriteria applicableTxnCriteria = DetachedCriteria.forClass(AffiliateTxnType.class);
        applicableTxnCriteria.add(Restrictions.in("id", applicableTxnTypes));
        List<AffiliateTxnType> applicableTxns = findByCriteria(applicableTxnCriteria);

        String hqlQuery = "select id from AffiliateTxn at where at.affiliate=:affiliate and "
                + "at.affiliateTxnType in (:applicableTxns)and at.date >= :startDate and at.date <= :endDate";

        List<Long> affiliateTxns = findByNamedParams(hqlQuery, new String[] { "affiliate", "applicableTxns", "startDate", "endDate" }, new Object[] {
                affiliate,
                applicableTxns,
                startDate,
                endDate });

        DetachedCriteria criteria = DetachedCriteria.forClass(AffiliateTxn.class);
        criteria.add(Restrictions.in("id", affiliateTxns));

        return list(criteria, pageNo, perPage);
    }

    public long getReferredOrdersCountByAffiliate(Affiliate affiliate, Date startDate, Date endDate) {
        List<AffiliateTxnType> applicableTxns = new ArrayList<AffiliateTxnType>();
        applicableTxns.add(get(AffiliateTxnType.class, EnumAffiliateTxnType.ADD.getId()));

        String queryString = "select count(at.id) from AffiliateTxn at where at.affiliate=:affiliate and at.affiliateTxnType in (:applicableTxns)and at.date >= :startDate and at.date <= :endDate";

        return countByNamedParams(queryString, new String[] { "affiliate", "applicableTxns", "startDate", "endDate" },
                new Object[] { affiliate, applicableTxns, startDate, endDate });
    }

    @SuppressWarnings("unchecked")
    public Double getAmountInAccount(Affiliate affiliate) {
        List<Long> applicableTxnTypes = new ArrayList<Long>();
        applicableTxnTypes.add(EnumAffiliateTxnType.ADD.getId());
        applicableTxnTypes.add(EnumAffiliateTxnType.SENT.getId());

        DetachedCriteria applicableTxnCriteria = DetachedCriteria.forClass(AffiliateTxnType.class);
        applicableTxnCriteria.add(Restrictions.in("id", applicableTxnTypes));
        List<AffiliateTxnType> applicableTxns = findByCriteria(applicableTxnCriteria);

        String queryString = "select sum(at.amount) from AffiliateTxn at where at.affiliate =:affiliate and at.affiliateTxnType in (:applicableTxns)";
        Double ammountInAccount = (Double)findUniqueByNamedQueryAndNamedParam(queryString, new String[]{"affiliate","applicableTxns"}, new Object[]{affiliate,applicableTxns});
        
        /*ammountInAccount = (Double) getSession().createQuery(
                "").setParameterList("",
                ).setParameter("affiliate", affiliate).uniqueResult();*/
        if (ammountInAccount == null) {
            ammountInAccount = 0.0;
        }
        return ammountInAccount;
    }
}
