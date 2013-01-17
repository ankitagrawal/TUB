package com.hk.loyaltypg.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.TransactionType;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.karmaPointStatus;
import com.hk.domain.order.Order;
import com.hk.exception.HealthkartRuntimeException;
import com.hk.loyaltypg.dao.LoyaltyProductDao;
import com.hk.loyaltypg.dao.UserOrderKarmaProfileDao;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.order.OrderDao;

@Service
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {
	
	int oneKarmaPoint = 100;

	@Autowired private LoyaltyProductDao loyaltyProductDao;
	@Autowired private OrderDao orderDao;
	@Autowired private UserOrderKarmaProfileDao userOrderKarmaProfileDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LoyaltyProduct> listProucts(Long userId, int startRow, int maxRows) {
		int karmaPoints = calculateKarmaPoints(userId);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "pv");
		criteria.createAlias("pv.product", "p");
		criteria.add(Restrictions.le("points", karmaPoints));
		criteria.add(Restrictions.le("p.outOfStock", Boolean.FALSE));
		if(maxRows == 0) {
			return loyaltyProductDao.findByCriteria(criteria);
		}
		return loyaltyProductDao.findByCriteria(criteria, startRow, maxRows);
	}

	@Override
	public void reconcileHistoryPurchase(Long userId) {
		//Logic to insert history order points in userOrderKarmaProfile.
	}

	@Override
	public int calculateKarmaPoints(Long userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserOrderKarmaProfile.class);
		criteria.setProjection(Projections.sum("karmaPints")).add(Restrictions.eq("status", karmaPointStatus.APPROVED));
		Integer karmaPoints = (Integer) orderDao.findByCriteria(criteria).iterator().next();
		return karmaPoints;
	}

	@Override
	public void creditKarmaPoints(Long orderId) {
		Order order = orderDao.get(Order.class, orderId);
		Double amount = order.getPayment().getAmount();
		int karmaPoints = amount.intValue() / oneKarmaPoint;
		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(karmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.CREDIT);
		profile.setKarmaPints(karmaPoints);
		profile.setUser(order.getUser());
		userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	public void debitKarmaPoints(Long orderId) {
		Order order = orderDao.get(Order.class, orderId);
		int existingKarmaPoints = calculateKarmaPoints(order.getUser().getId());
		Double amount = order.getPayment().getAmount();
		int karmaPoints = amount.intValue() / oneKarmaPoint;
		if (existingKarmaPoints < karmaPoints) {
			throw new HealthkartRuntimeException("Not sufficient karma points") {
				private static final long serialVersionUID = 1L;
			};
		}
		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(karmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.DEBIT);
		profile.setKarmaPints(karmaPoints);
		profile.setUser(order.getUser());
		userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	public void approveKarmaPoints(Long orderId) {
		String queryStr = "from UserOrderKarmaProfile u where u.orderId=:orderId";
		Object[] params = {orderId};
		UserOrderKarmaProfile profile = (UserOrderKarmaProfile) userOrderKarmaProfileDao.findByQuery(queryStr, params).get(0);
		profile.setStatus(karmaPointStatus.APPROVED);
		userOrderKarmaProfileDao.saveOrUpdate(profile);
	}
	
	@Override
	public LoyaltyProduct getProductByVariantId(String variantId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "pv");
		criteria.add(Restrictions.eq("pv.id", variantId));
		
		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> products = loyaltyProductDao.findByCriteria(criteria);
		if(products != null && products.size() > 0) {
			products.iterator().next();
		}
		return null;
	}
}
