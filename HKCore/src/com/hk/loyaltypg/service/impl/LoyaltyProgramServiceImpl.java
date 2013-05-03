package com.hk.loyaltypg.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.loyaltypg.UserBadgeInfo;
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
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.loyaltypg.service.NextLevelInfo;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.order.RewardPointService;
import com.hk.store.CategoryDto;
import com.hk.store.SearchCriteria;

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

	@Autowired
	private RewardPointService rewardPointService;

	private enum LoyaltyProductAlias {
		VARIANT("pv"), PRODUCT("p"), CATEGORY("c");

		private String alias;

		private LoyaltyProductAlias(String alias) {
			this.alias = alias;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LoyaltyProduct> listProucts(SearchCriteria searchCriteria) {
		DetachedCriteria criteria = this.prepareLoyaltyProductCriteria(searchCriteria);

		criteria.setProjection(Projections.distinct(Projections.id()));
		List<Long> ids = this.loyaltyProductDao.findByCriteria(criteria);

		DetachedCriteria distinctCriteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		int fromIndex = searchCriteria.getStartRow();
		int toIndex = searchCriteria.getStartRow() + searchCriteria.getMaxRows();
		toIndex = ids.size() < toIndex ? ids.size() : toIndex;
		if (searchCriteria.getMaxRows() == 0) {
			distinctCriteria.add(Restrictions.in("id", ids));
		} else {
			distinctCriteria.add(Restrictions.in("id", ids.subList(fromIndex, toIndex)));
		}
		return this.loyaltyProductDao.findByCriteria(distinctCriteria);
	}

	@Override
	public int countProucts(SearchCriteria criteria) {

		DetachedCriteria crit = this.prepareLoyaltyProductCriteria(criteria);
		crit.setProjection(Projections.distinct(Projections.id()));

		@SuppressWarnings("unchecked")
		List<Long> ids = this.loyaltyProductDao.findByCriteria(crit);
		if (ids == null) {
			return 0;
		}
		return ids.size();
	}

	@Override
	public List<CategoryDto> listCategories() {
		DetachedCriteria criteria = this.prepareLoyaltyProductCriteria(null);

		ProjectionList projectionsList = Projections.projectionList();
		projectionsList.add(Projections.alias(Projections.property(LoyaltyProductAlias.CATEGORY.alias + ".name"),
				"name"));
		projectionsList.add(Projections.alias(
				Projections.property(LoyaltyProductAlias.CATEGORY.alias + ".displayName"), "displayName"));
		projectionsList
				.add(Projections.alias(Projections.count(LoyaltyProductAlias.PRODUCT.alias + ".id"), "prodCount"));
		projectionsList.add(Projections.groupProperty(LoyaltyProductAlias.CATEGORY.alias + ".name"));
		criteria.setProjection(projectionsList);
		criteria.addOrder(org.hibernate.criterion.Order.asc(LoyaltyProductAlias.CATEGORY.alias + ".name"));

		criteria.setResultTransformer(Transformers.aliasToBean(CategoryDto.class));

		@SuppressWarnings("unchecked")
		List<CategoryDto> list = this.loyaltyProductDao.findByCriteria(criteria);
		return list;
	}

	@Override
	@Transactional
	public void creditKarmaPoints(Order order) {
		UserBadgeInfo badgeInfo = this.getUserBadgeInfo(order.getUser());
		double loyaltyMultiplier = badgeInfo.getBadge().getLoyaltyMultiplier();
		Double amount = order.getPayment().getAmount();

		double karmaPoints = (amount * loyaltyMultiplier);
		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(KarmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.CREDIT);
		profile.setKarmaPoints(karmaPoints);
		UserOrderKey userOrderKey = new UserOrderKey(order, order.getUser());
		profile.setUserOrderKey(userOrderKey);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	@Transactional
	public UserBadgeInfo getUserBadgeInfo(User user) {
		Long normalId = (long) 1;
		Badge normalBadge = this.baseDao.load(Badge.class, normalId);

		List<UserBadgeInfo> infos = null;
		if (user != null) {
			DetachedCriteria criteria = DetachedCriteria.forClass(UserBadgeInfo.class);
			criteria.add(Restrictions.eq("user.id", user.getId()));

			infos = this.baseDao.findByCriteria(criteria);
		}
		UserBadgeInfo info = null;
		if (infos == null || infos.isEmpty()) {
			info = new UserBadgeInfo();
			info.setBadge(normalBadge);
			return info;
		}
		info = infos.iterator().next();
		Period period = new Period(new Date().getTime(), info.getUpdationTime().getTime());
		if (period.getDays() > 365) {
			info.setBadge(normalBadge);
			info.setUpdationTime(Calendar.getInstance().getTime());
			this.baseDao.save(info);
		}
		return info;
	}

	@Override
	public Collection<Badge> getAllBadges() {
		return new java.util.TreeSet<Badge>(this.baseDao.getAll(Badge.class));
	}

	@Override
	@Transactional
	public void debitKarmaPoints(Order order) {
		double existingKarmaPoints = this.calculateLoyaltyPoints(order.getUser());
		double karmaPoints = this.calculateLoyaltyPoints(order);
		if (existingKarmaPoints < karmaPoints) {
			throw new HealthkartRuntimeException("Not sufficient karma points") {
				private static final long serialVersionUID = 1L;
			};
		}
		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(KarmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.DEBIT);
		profile.setKarmaPoints(-(karmaPoints));
		UserOrderKey userOrderKey = new UserOrderKey(order, order.getUser());
		profile.setUserOrderKey(userOrderKey);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	@Transactional
	public void approveKarmaPoints(Order order) {
		UserOrderKarmaProfile profile = this.getUserOrderKarmaProfile(order.getId());
		profile.setStatus(KarmaPointStatus.APPROVED);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
		this.updateUserBadgeInfo(profile.getUserOrderKey().getUser());
	}

	@Transactional
	private void updateUserBadgeInfo(User user) {
		double anualSpend = this.calculateAnualSpend(user);

		List<Badge> badges = this.baseDao.getAll(Badge.class);
		Badge calculatedBadge = this.baseDao.get(Badge.class, 1l);
		for (Badge badge : badges) {
			if (anualSpend >= badge.getMinScore() && (anualSpend <= badge.getMaxScore() || badge.getMaxScore() == -1)) {
				calculatedBadge = badge;
				break;
			}
		}

		UserBadgeInfo userBadgeInfo = this.getUserBadgeInfo(user);
		if (calculatedBadge.compareTo(userBadgeInfo.getBadge()) > 0) {
			userBadgeInfo.setBadge(calculatedBadge);
			userBadgeInfo.setUpdationTime(Calendar.getInstance().getTime());
			this.baseDao.save(userBadgeInfo);
		}
	}

	@Override
	public double calculateLoyaltyPoints(Order order) {
		Set<CartLineItem> cartLineItems = order.getCartLineItems();
		return this.calculateLoyaltyPoints(cartLineItems);
	}

	@Override
	public double calculateLoyaltyPoints(Collection<CartLineItem> cartLineItems) {
		double points = 0d;
		for (CartLineItem cartLineItem : cartLineItems) {
			LoyaltyProduct loyaltyProduct = this.getProductByVariantId(cartLineItem.getProductVariant().getId());
			points += (loyaltyProduct.getPoints() * cartLineItem.getQty());
		}
		return points;
	}

	@Override
	@Transactional
	public void cancelLoyaltyPoints(Order order) {
		UserOrderKarmaProfile profile = this.getUserOrderKarmaProfile(order.getId());
		profile.setStatus(KarmaPointStatus.CANCELED);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
		this.updateUserBadgeInfo(profile.getUserOrderKey().getUser());
	}

	@Override
	public Page getUserLoyaltyProfileHistory(User user, int page, int perPage) {
		return this.userOrderKarmaProfileDao.listKarmaPointsForUser(user, page, perPage);
	}

	@Override
	public double calculateLoyaltyPoints(User user) {
		Double credits = this.calculatePoints(user.getId(), TransactionType.CREDIT, KarmaPointStatus.APPROVED);
		Double debits = this.calculatePoints(user.getId(), TransactionType.DEBIT);
		if (credits == null) {
			return 0d;
		} else if (debits == null) {
			return credits;
		} else {
			return credits - Math.abs(debits);
		}
	}

	@Override
	public LoyaltyProduct getProductByVariantId(String variantId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.add(Restrictions.eq("variant.id", variantId));

		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> products = this.loyaltyProductDao.findByCriteria(criteria);
		if (products != null && products.size() > 0) {
			return products.iterator().next();
		}
		return null;
	}

	@Override
	public double calculateAnualSpend(User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.add(Restrictions.eq("user", user));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		criteria.add(Restrictions.ge("createDate", calendar.getTime()));
		criteria.createAlias("payment", "pmt");
		criteria.setProjection(Projections.sum("pmt.amount"));
		criteria.add(Restrictions.eq("orderStatus.id", EnumOrderStatus.Delivered.asOrderStatus().getId()));

		@SuppressWarnings("unchecked")
		List<Double> list = this.baseDao.findByCriteria(criteria);
		if (list == null || list.isEmpty() || list.iterator().next() == null) {
			return 0d;
		}
		return list.iterator().next();
	}

	@Override
	@Transactional
	public NextLevelInfo fetchNextLevelInfo(User user) {
		UserBadgeInfo userBadgeInfo = this.getUserBadgeInfo(user);
		Badge currentBadge = userBadgeInfo.getBadge();
		double anualSpend = this.calculateAnualSpend(user);

		Badge nextBadge = currentBadge;
		Collection<Badge> badges = this.getAllBadges();
		for (Badge badge : badges) {
			if (anualSpend < badge.getMinScore()) {
				nextBadge = badge;
				break;
			}
		}

		NextLevelInfo nextLevelInfo = new NextLevelInfo();
		nextLevelInfo.setCurrentSpend(anualSpend);
		nextLevelInfo.setExistingBadge(currentBadge);
		nextLevelInfo.setNextBadge(nextBadge);
		if (currentBadge.compareTo(nextBadge) == 0) {
			nextLevelInfo.setSpendRequired(0d);
		} else {
			nextLevelInfo.setSpendRequired(nextBadge.getMinScore() - anualSpend);
		}
		return nextLevelInfo;
	}

	private DetachedCriteria prepareLoyaltyProductCriteria(SearchCriteria search) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", LoyaltyProductAlias.VARIANT.alias, CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq(LoyaltyProductAlias.VARIANT.alias + ".outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq(LoyaltyProductAlias.VARIANT.alias + ".deleted", Boolean.FALSE));
		criteria.createAlias(LoyaltyProductAlias.VARIANT.alias + ".product", LoyaltyProductAlias.PRODUCT.alias,
				CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq(LoyaltyProductAlias.PRODUCT.alias + ".outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq(LoyaltyProductAlias.PRODUCT.alias + ".deleted", Boolean.FALSE));
		criteria.createAlias(LoyaltyProductAlias.PRODUCT.alias + ".categories", LoyaltyProductAlias.CATEGORY.alias,
				CriteriaSpecification.INNER_JOIN);
		if (search != null) {
			if (search.getCategoryName() != null) {
				criteria.add(Restrictions.eq(LoyaltyProductAlias.CATEGORY.alias + ".name", search.getCategoryName()));
			}
			if (search.getRange() != null) {
				criteria.add(Restrictions.ge("points", search.getRange().getStart()));
				criteria.add(Restrictions.le("points", search.getRange().getEnd()));
				criteria.addOrder(org.hibernate.criterion.Order.asc("points"));
			}
		}
		return criteria;
	}

	private double calculatePoints(Long userId, TransactionType transactionType, KarmaPointStatus... status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserOrderKarmaProfile.class);
		criteria.setProjection(Projections.sum("karmaPoints"));

		// Check for 2 years
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		criteria.add(Restrictions.ge("creationTime", cal.getTime()));

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

		@SuppressWarnings("rawtypes")
		List vList = this.loyaltyProductDao.findByCriteria(criteria);
		if (vList == null || vList.isEmpty() || vList.get(0) == null) {
			return 0d;
		}
		return (Double) vList.get(0);
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
	public double convertLoyaltyToRewardPoints(User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserOrderKarmaProfile.class);
		criteria.add(Restrictions.eq("userOrderKey.user", user));
		criteria.add(Restrictions.eq("transactionType", TransactionType.CREDIT));
		criteria.add(Restrictions.eq("status", KarmaPointStatus.APPROVED));
		criteria.addOrder(org.hibernate.criterion.Order.asc("creationTime"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		criteria.add(Restrictions.ge("creationTime", cal.getTime()));

		@SuppressWarnings("unchecked")
		List<UserOrderKarmaProfile> profileList = this.userOrderKarmaProfileDao.findByCriteria(criteria);

		double loyaltyPoints = this.calculateLoyaltyPoints(user);
		double totalPointsConverted = 0;
		// To iterate over profile list without using another loop.
		int counter = 0;
		int size = profileList.size();
		UserOrderKarmaProfile currentProfile;
		String comment = "Reward Points converted from Loyalty points";
		while (loyaltyPoints > 0 && counter < size) {
			currentProfile = profileList.get(counter);

			// when karma points for an order are less than total points for
			// conversion
			if (currentProfile.getKarmaPoints() <= loyaltyPoints) {
				this.rewardPointService.addRewardPoints(user, null, currentProfile.getUserOrderKey().getOrder(),
						currentProfile.getKarmaPoints(), comment, EnumRewardPointStatus.APPROVED,
						EnumRewardPointMode.HKLOYALTY_POINTS.asRewardPointMode());
				currentProfile.setStatus(KarmaPointStatus.CONVERTED);
				currentProfile.setUpdateTime(Calendar.getInstance().getTime());
				loyaltyPoints = loyaltyPoints - currentProfile.getKarmaPoints();
				totalPointsConverted += currentProfile.getKarmaPoints();
				this.userOrderKarmaProfileDao.saveOrUpdate(currentProfile);
				counter++;
			} else {
				// No processing to be done
				counter++;
				continue;
			}

		}

		return totalPointsConverted;

	}

	/**
	 * 
	 * Setters and getters start from here.
	 */

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
