package com.hk.impl.dao.affiliate;

import com.akube.framework.dao.Page;
import com.hk.constants.affiliate.EnumAffiliateTxnType;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTxn;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.affiliate.AffiliateTxnDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class AffiliateTxnDaoImpl extends BaseDaoImpl implements AffiliateTxnDao {

	@SuppressWarnings("unchecked")
	public List<AffiliateTxn> getTxnListByAffiliate(Affiliate affiliate) {
		String queryString = "from AffiliateTxn a where a.affiliate=:affiliate";
		return findByNamedParams(queryString, new String[]{"affiliate"}, new Object[]{affiliate});
	}

	public AffiliateTxn getTxnByOrder(Order order) {
		String queryString = "from AffiliateTxn aT where aT.order =:order";
		return (AffiliateTxn) findUniqueByNamedParams(queryString, new String[]{"order"}, new Object[]{order});
	}

	public AffiliateTxn saveTxn(Affiliate affiliate, Double amountToAdd, AffiliateTxnType affiliateTxnType, Order order) {
		AffiliateTxn affiliateTxn = new AffiliateTxn();
		affiliateTxn.setAffiliate(affiliate);
		if (affiliateTxnType.getId().equals(EnumAffiliateTxnType.PENDING.getId())) {
			affiliateTxn.setAmount(amountToAdd);
		} else if (affiliateTxnType.getId().equals(EnumAffiliateTxnType.SENT.getId())) {
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

		List<Long> affiliateTxns = findByNamedParams(hqlQuery, new String[]{"affiliate", "applicableTxns", "startDate", "endDate"}, new Object[]{
				affiliate,
				applicableTxns,
				startDate,
				endDate});

		DetachedCriteria criteria = DetachedCriteria.forClass(AffiliateTxn.class);
		criteria.add(Restrictions.in("id", affiliateTxns));

		return list(criteria, pageNo, perPage);
	}

	public long getReferredOrdersCountByAffiliate(Affiliate affiliate, Date startDate, Date endDate) {
		List<AffiliateTxnType> applicableTxns = new ArrayList<AffiliateTxnType>();
		applicableTxns.add(get(AffiliateTxnType.class, EnumAffiliateTxnType.ADD.getId()));

		String queryString = "select count(at.id) from AffiliateTxn at where at.affiliate=:affiliate and at.affiliateTxnType in (:applicableTxns)and at.date >= :startDate and at.date <= :endDate";

		return countByNamedParams(queryString, new String[]{"affiliate", "applicableTxns", "startDate", "endDate"},
				new Object[]{affiliate, applicableTxns, startDate, endDate});
	}

	@SuppressWarnings("unchecked")
	public Double getAmountInAccount(Affiliate affiliate, Date startDate, Date endDate) {
		List<Long> applicableTxnTypes = new ArrayList<Long>();
		applicableTxnTypes.add(EnumAffiliateTxnType.ADD.getId());
		applicableTxnTypes.add(EnumAffiliateTxnType.SENT.getId());

		Double amountInAccount = 0D;
		if (startDate == null || endDate == null) {
			String queryString = "select sum(at.amount) from AffiliateTxn at where at.affiliate =:affiliate and at.affiliateTxnType.id in (:applicableTxnTypes)";
			amountInAccount = (Double) findUniqueByNamedParams(queryString, new String[]{"affiliate", "applicableTxnTypes"}, new Object[]{
					affiliate,
					applicableTxnTypes});
		} else {
			String queryString = "select sum(at.amount) from AffiliateTxn at where at.affiliate =:affiliate and at.affiliateTxnType.id in (:applicableTxnTypes) and at.date >= :startDate and at.date <= :endDate";
			amountInAccount = (Double) findUniqueByNamedParams(queryString, new String[]{"affiliate", "applicableTxnTypes", "startDate", "endDate"}, new Object[]{
					affiliate,
					applicableTxnTypes, startDate, endDate});
		}
		return amountInAccount != null ? amountInAccount : 0D;
	}

	@Transactional
	public void approvePendingAffiliateTxn(Affiliate affiliate, Order order) {
		String queryString = "from AffiliateTxn aT where aT.order =:order and aT.affiliate = :affiliate and aT.affiliateTxnTypeId =:affiliateTxnTypeId ";
		AffiliateTxn affiliateTxn = (AffiliateTxn) findUniqueByNamedParams(queryString, new String[]{"order", "affiliate", "affiliateTxnTypeId"}, new Object[]{order, affiliate, EnumAffiliateTxnType.PENDING.getId()});
		if (affiliateTxn != null) {
			affiliateTxn.setAffiliateTxnType(EnumAffiliateTxnType.ADD.asAffiliateTxnType());
			save(affiliateTxn);
		}
	}
}
