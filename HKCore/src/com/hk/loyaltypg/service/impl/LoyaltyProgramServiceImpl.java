package com.hk.loyaltypg.service.impl;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.KarmaPointStatus;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.TransactionType;
import com.hk.domain.loyaltypg.UserOrderKey;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartRuntimeException;
import com.hk.loyaltypg.dao.LoyaltyProductDao;
import com.hk.loyaltypg.dao.UserOrderKarmaProfileDao;
import com.hk.loyaltypg.dto.CategoryLoyaltyDto;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.loyaltypg.service.UserBadgeInfo;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.order.OrderDao;

@Service
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {

	@Autowired
	private LoyaltyProductDao loyaltyProductDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private UserOrderKarmaProfileDao userOrderKarmaProfileDao;
	@Autowired
	private BaseDao baseDao;

	private static Set<Badge> BADGES = null;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Badge.class);

		BADGES = new TreeSet<Badge>(new Comparator<Badge>() {
			@Override
			public int compare(Badge o1, Badge o2) {
				Double o1Min = o1.getMinScore();
				Double o2Min = o2.getMinScore();
				return o1Min.compareTo(o2Min);
			}
		});

		BADGES.addAll(this.baseDao.findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LoyaltyProduct> listProucts(int startRow, int maxRows) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "pv");
		criteria.add(Restrictions.eq("pv.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("pv.deleted", Boolean.FALSE));
		criteria.createAlias("pv.product", "p");
		criteria.add(Restrictions.eq("p.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("p.deleted", Boolean.FALSE));
		if (maxRows == 0) {
			return this.loyaltyProductDao.findByCriteria(criteria);
		}
		return this.loyaltyProductDao.findByCriteria(criteria, startRow, maxRows);
	}

	@Override
	public double calculateKarmaPoints(Long userId) {
		Double credits = this.calculatePoints(userId, TransactionType.CREDIT, KarmaPointStatus.APPROVED);
		Double debits = this.calculatePoints(userId, TransactionType.DEBIT);
		if (credits == null) {
			return 0d;
		} else if (debits == null) {
			return credits;
		} else {
			return credits - debits;
		}
	}

	private Double calculatePoints(Long userId, TransactionType transactionType, KarmaPointStatus... status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserOrderKarmaProfile.class);
		criteria.setProjection(Projections.sum("karmaPoints"));

		if (status != null && status.length > 0) {
			if (status.length == 1) {
				criteria.add(Restrictions.eq("status", status[0]));
			} else {
				Disjunction disjunction = Restrictions.disjunction();
				for (KarmaPointStatus karmaPointStatus : status) {
					disjunction.add(Restrictions.eq("status", karmaPointStatus));
				}
				criteria.add(disjunction);
			}
		}
		criteria.add(Restrictions.ne("status", KarmaPointStatus.CANCELED));
		criteria.add(Restrictions.eq("userOrderKey.user.id", userId));
		criteria.add(Restrictions.ne("userOrderKey.order.id", -1l));
		criteria.add(Restrictions.eq("transactionType", transactionType));
		Double karmaPoints = (Double) this.orderDao.findByCriteria(criteria).iterator().next();
		return karmaPoints;
	}

	@Override
	@Transactional
	public void creditKarmaPoints(Long orderId) {
		Order order = this.orderDao.get(Order.class, orderId);

		UserBadgeInfo badgeInfo = this.getUserBadgeInfo(order.getUser().getId());
		double loyaltyPercentage = badgeInfo.getBadge().getLoyaltyPercentage();
		Double amount = order.getPayment().getAmount();
		double karmaPoints = (amount * (loyaltyPercentage / 100));

		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(KarmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.CREDIT);
		profile.setKarmaPoints(karmaPoints);
		UserOrderKey userOrderKey = new UserOrderKey(order, order.getUser());
		profile.setUserOrderKey(userOrderKey);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	public UserBadgeInfo getUserBadgeInfo(Long userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.createAlias("payment", "pmt");
		criteria.setProjection(Projections.sum("pmt.amount"));
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("orderStatus.id", EnumOrderStatus.Delivered.asOrderStatus().getId()));

		Double completePurchase = (Double) this.baseDao.findByCriteria(criteria).iterator().next();
		Badge userBadge = BADGES.iterator().next();
		for (Badge badge : BADGES) {
			if (completePurchase != null
					&& (completePurchase >= badge.getMinScore() && completePurchase < badge.getMaxScore())) {
				userBadge = badge;
				break;
			}
		}
		Double loyaltyPoints = this.calculateKarmaPoints(userId);
		UserBadgeInfo badgeInfo = new UserBadgeInfo();
		badgeInfo.setBadge(userBadge);
		badgeInfo.setLoyaltyPoints(loyaltyPoints);
		badgeInfo.setUserId(userId);
		return badgeInfo;
	}

	@Override
	public List<Badge> getAllBadges() {
		return this.baseDao.getAll(Badge.class);
	}

	@Override
	@Transactional
	public void debitKarmaPoints(Long orderId) {
		Order order = this.orderDao.get(Order.class, orderId);
		double existingKarmaPoints = this.calculateKarmaPoints(order.getUser().getId());
		double karmaPoints = this.aggregatePoints(orderId);
		if (existingKarmaPoints < karmaPoints) {
			throw new HealthkartRuntimeException("Not sufficient karma points") {
				private static final long serialVersionUID = 1L;
			};
		}
		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(KarmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.DEBIT);
		profile.setKarmaPoints(karmaPoints);
		UserOrderKey userOrderKey = new UserOrderKey(order, order.getUser());
		profile.setUserOrderKey(userOrderKey);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	@Transactional
	public void approveKarmaPoints(Long orderId) {
		UserOrderKarmaProfile profile = this.getUserOrderKarmaProfile(orderId);
		profile.setStatus(KarmaPointStatus.APPROVED);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	private UserOrderKarmaProfile getUserOrderKarmaProfile(Long orderId) {
		Order order = this.orderDao.get(Order.class, orderId);
		UserOrderKey uOKey = new UserOrderKey();
		uOKey.setOrder(order);
		uOKey.setUser(order.getUser());
		UserOrderKarmaProfile profile = this.userOrderKarmaProfileDao.get(UserOrderKarmaProfile.class, uOKey);
		return profile;
	}

	@Override
	public LoyaltyProduct getProductByVariantId(String variantId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "pv");
		criteria.add(Restrictions.eq("pv.id", variantId));

		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> products = this.loyaltyProductDao.findByCriteria(criteria);
		if (products != null && products.size() > 0) {
			return products.iterator().next();
		}
		return null;
	}

	@Override
	public double aggregatePoints(Long orderId) {
		Order order = this.orderDao.get(Order.class, orderId);
		Set<CartLineItem> cartLineItems = order.getCartLineItems();
		return this.aggregatePoints(cartLineItems);
	}

	@Override
	public double aggregatePoints(Collection<CartLineItem> cartLineItems) {
		double points = 0d;
		for (CartLineItem cartLineItem : cartLineItems) {
			LoyaltyProduct loyaltyProduct = this.getProductByVariantId(cartLineItem.getProductVariant().getId());
			points += (loyaltyProduct.getPoints() * cartLineItem.getQty());
		}
		return points;
	}

	@Override
	public int countProucts() {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.setProjection(Projections.count("pv.id"));
		criteria.createAlias("variant", "pv");
		criteria.add(Restrictions.eq("pv.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("pv.deleted", Boolean.FALSE));
		criteria.createAlias("pv.product", "p");
		criteria.add(Restrictions.eq("p.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("p.deleted", Boolean.FALSE));

		Integer count = (Integer) this.baseDao.findByCriteria(criteria).iterator().next();
		if (count == null) {
			return 0;
		}
		return count;
	}

	@Override
	public void cancelKarmaPoints(Long orderId) {
		UserOrderKarmaProfile profile = this.getUserOrderKarmaProfile(orderId);
		profile.setStatus(KarmaPointStatus.CANCELED);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}


	@Override
	public List<CategoryLoyaltyDto> getLoyaltyCatalog() {

		return this.loyaltyProductDao.getCategoryDtoForLoyaltyProducts();
				//return this.loyaltyProductDao.getCategoryForLoyaltyProducts();
	}

	public List<Category> getCategoryForLoyaltyProducts () {
		
		return this.loyaltyProductDao.getCategoryForLoyaltyProducts();
	}
	@Override
	public List<LoyaltyProduct> getProductsByCategoryName(String categoryName) {
		return this.loyaltyProductDao.getProductsByCategoryName(categoryName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.loyaltypg.service.LoyaltyProgramService#getProfileHistory()
	 */
	@Override
	public Page getProfileHistory(User user, int page, int perPage) {

		return this.userOrderKarmaProfileDao.listKarmaPointsForUser(user, page, perPage);
	}
	
	/* (non-Javadoc)
	 * @see com.hk.loyaltypg.service.LoyaltyProgramService#getProductsByPoints(double, double)
	 */
	@Override
	public List<LoyaltyProduct> getProductsByPoints(double minPoints, double maxPoints) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.add(Restrictions.ge("points", minPoints));
		criteria.add(Restrictions.le("points", maxPoints));
		criteria.addOrder(org.hibernate.criterion.Order.asc("points"));
		
		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> prodList = this.loyaltyProductDao.findByCriteria(criteria);
		
		return prodList;
	}

	public LoyaltyProductDao getLoyaltyProductDao() {
		return this.loyaltyProductDao;
	}

	public void setLoyaltyProductDao(LoyaltyProductDao loyaltyProductDao) {
		this.loyaltyProductDao = loyaltyProductDao;
	}

	public OrderDao getOrderDao() {
		return this.orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public UserOrderKarmaProfileDao getUserOrderKarmaProfileDao() {
		return this.userOrderKarmaProfileDao;
	}

	public void setUserOrderKarmaProfileDao(UserOrderKarmaProfileDao userOrderKarmaProfileDao) {
		this.userOrderKarmaProfileDao = userOrderKarmaProfileDao;
	}

	public BaseDao getBaseDao() {
		return this.baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

}
