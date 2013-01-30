package com.hk.loyaltypg.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.TransactionType;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.karmaPointStatus;
import com.hk.domain.loyaltypg.UserOrderKey;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.exception.HealthkartRuntimeException;
import com.hk.loyaltypg.dao.LoyaltyProductDao;
import com.hk.loyaltypg.dao.UserOrderKarmaProfileDao;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.order.OrderDao;

@Service
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {
	
	double oneKarmaPoint = 100d;

	@Autowired private LoyaltyProductDao loyaltyProductDao;
	@Autowired private OrderDao orderDao;
	@Autowired private UserOrderKarmaProfileDao userOrderKarmaProfileDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LoyaltyProduct> listProucts(Long userId, int startRow, int maxRows) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "pv");
		criteria.createAlias("pv.product", "p");
		criteria.add(Restrictions.eq("p.outOfStock", Boolean.FALSE));
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
	public double calculateKarmaPoints(Long userId) {
		Double credits = calculatePoints(userId, TransactionType.CREDIT, karmaPointStatus.APPROVED);
		Double debits = calculatePoints(userId, TransactionType.DEBIT);
		if(credits == null) {
			return 0d;
		} else if(debits == null) {
			return credits;
		} else {
			return credits - debits;
		}
	}
	
	private Double calculatePoints(Long userId, TransactionType transactionType, karmaPointStatus... status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserOrderKarmaProfile.class);
		criteria.setProjection(Projections.sum("karmaPints"));
		
		if(status != null && status.length > 0) {
			if(status.length == 1) {
				criteria.add(Restrictions.eq("status", status[0]));
			} else {
				Disjunction disjunction = Restrictions.disjunction();
				for (karmaPointStatus karmaPointStatus : status) {
					disjunction.add(Restrictions.eq("status", karmaPointStatus));
				}
				criteria.add(disjunction);
			}
		}
		criteria.add(Restrictions.eq("transactionType", transactionType));
		Double karmaPoints = (Double) orderDao.findByCriteria(criteria).iterator().next();
		return karmaPoints;
	}

	@Override
	@Transactional
	public void creditKarmaPoints(Long orderId) {
		Order order = orderDao.get(Order.class, orderId);
		Double amount = order.getPayment().getAmount();
		double karmaPoints = amount / oneKarmaPoint;
		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(karmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.CREDIT);
		profile.setKarmaPints(karmaPoints);
		UserOrderKey userOrderKey = new UserOrderKey(order, order.getUser());
		profile.setUserOrderKey(userOrderKey);
		userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	@Transactional
	public void debitKarmaPoints(Long orderId) {
		Order order = orderDao.get(Order.class, orderId);
		double existingKarmaPoints = calculateKarmaPoints(order.getUser().getId());
		double karmaPoints = aggregatePoints(orderId);
		if (existingKarmaPoints < karmaPoints) {
			throw new HealthkartRuntimeException("Not sufficient karma points") {
				private static final long serialVersionUID = 1L;
			};
		}
		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(karmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.DEBIT);
		profile.setKarmaPints(karmaPoints);
		UserOrderKey userOrderKey = new UserOrderKey(order, order.getUser());
		profile.setUserOrderKey(userOrderKey);
		userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	@Transactional
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
			return products.iterator().next();
		}
		return null;
	}

	@Override
	public double aggregatePoints(Long orderId) {
		Order order = orderDao.get(Order.class, orderId);
		Set<CartLineItem> cartLineItems = order.getCartLineItems();
		return aggregatePoints(cartLineItems);
	}
	
	@Override
	public double aggregatePoints(Collection<CartLineItem> cartLineItems) {
		double points = 0d;
		for (CartLineItem cartLineItem : cartLineItems) {
			LoyaltyProduct loyaltyProduct = getProductByVariantId(cartLineItem.getProductVariant().getId());
			points += (loyaltyProduct.getPoints()*cartLineItem.getQty());
		}
		return points;
	}
}
