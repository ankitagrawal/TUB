package com.hk.api.loyaltypg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.api.loyaltypg.dao.LoyaltyProductDao;
import com.hk.api.loyaltypg.dao.UserOrderKarmaProfileDao;
import com.hk.api.loyaltypg.domain.LoyaltyProduct;
import com.hk.api.loyaltypg.domain.UserOrderKarmaProfile;
import com.hk.api.loyaltypg.domain.UserOrderKarmaProfile.TransactionType;
import com.hk.api.loyaltypg.domain.UserOrderKarmaProfile.karmaPointStatus;
import com.hk.api.loyaltypg.service.LoyaltyProgramService;
import com.hk.domain.order.Order;
import com.hk.exception.HealthkartRuntimeException;
import com.hk.pact.dao.order.OrderDao;

public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {
	
	int oneKarmaPoint = 100;

	@Autowired private LoyaltyProductDao loyaltyProductDao;
	@Autowired private OrderDao orderDao;
	@Autowired private UserOrderKarmaProfileDao userOrderKarmaProfileDao;
	
	@Override
	public List<LoyaltyProduct> listProucts(Long userId, int startRow, int maxRows) {
		int karmaPoints = calculateKarmaPoints(userId);
		return loyaltyProductDao.listProductsUnderKarmaPints(karmaPoints, startRow, maxRows);
	}

	@Override
	public void reconcileHistoryPurchase(Long userId) {
		//Logic to insert history order points in userOrderKarmaProfile.
	}

	@Override
	public int calculateKarmaPoints(Long userId) {
		String queryString = "select sum(u.karmaPints) from UserOrderKarmaProfile u where u.status:=status";
		Object[] params = {karmaPointStatus.APPROVED};
		Integer karmaPoints = (Integer) orderDao.findByQuery(queryString,params).get(0);
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
}
