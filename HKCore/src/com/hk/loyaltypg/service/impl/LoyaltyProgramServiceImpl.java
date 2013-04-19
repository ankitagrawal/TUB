package com.hk.loyaltypg.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
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
import com.hk.loyaltypg.dto.CategoryLoyaltyDto;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.store.SearchCriteria;

@Service
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {

	@Autowired private LoyaltyProductDao loyaltyProductDao;
	@Autowired private OrderDao orderDao;
	@Autowired private UserOrderKarmaProfileDao userOrderKarmaProfileDao;
	@Autowired private BaseDao baseDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<LoyaltyProduct> listProucts(SearchCriteria searchCriteria) {
		DetachedCriteria criteria = this.prepareLoyaltyProductCriteria(searchCriteria);
		if (searchCriteria.getMaxRows() == 0) {
			return this.loyaltyProductDao.findByCriteria(criteria);
		}
		return this.loyaltyProductDao.findByCriteria(criteria, searchCriteria.getStartRow(), searchCriteria.getMaxRows());
	}
	
	private DetachedCriteria prepareLoyaltyProductCriteria(SearchCriteria search) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "pv", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("pv.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("pv.deleted", Boolean.FALSE));
		criteria.createAlias("pv.product", "p", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("p.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("p.deleted", Boolean.FALSE));
		if(search !=null && search.getCategoryName() != null) {
			criteria.createAlias("p.categories", "category", CriteriaSpecification.INNER_JOIN);
			criteria.add(Restrictions.eq("category.name", search.getCategoryName()));
		}
		return criteria;
	}
	
	@Override
	public int countProucts(SearchCriteria criteria) {
		DetachedCriteria crit = this.prepareLoyaltyProductCriteria(criteria);
		crit.setProjection(Projections.count("pv.id"));
		Integer count = (Integer) this.baseDao.findByCriteria(crit).iterator().next();
		if (count == null) {
			return 0;
		}
		return count;
	}

	@Override
	public List<CategoryLoyaltyDto> getLoyaltyCatalog() {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "prodVariant", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("prodVariant.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("prodVariant.deleted", Boolean.FALSE));
		criteria.createAlias("prodVariant.product", "prod", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("prod.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("prod.deleted", Boolean.FALSE));
		criteria.createAlias("prod.categories", "category", CriteriaSpecification.INNER_JOIN);
		
		ProjectionList projectionsList = Projections.projectionList();
		projectionsList.add(Projections.alias(Projections.property("category.name"), "name"));
		projectionsList.add(Projections.alias(Projections.property("category.displayName"), "displayName"));
		projectionsList.add(Projections.alias(Projections.count("prod.id"), "prodCount"));
		projectionsList.add(Projections.groupProperty("category.name"));
		criteria.setProjection(projectionsList);
		criteria.addOrder(org.hibernate.criterion.Order.asc("category.name"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(CategoryLoyaltyDto.class));
		
		@SuppressWarnings("unchecked")
		List<CategoryLoyaltyDto> list = this.loyaltyProductDao.findByCriteria(criteria);
		return list;
	}
	
	@Override
	@Transactional
	public void creditKarmaPoints(Long orderId) {
		Order order = this.orderDao.get(Order.class, orderId);

		UserBadgeInfo badgeInfo = this.getUserBadgeInfo(order.getUser().getId());
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
	public UserBadgeInfo getUserBadgeInfo(Long userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserBadgeInfo.class);
		criteria.add(Restrictions.eq("user.id", userId));
		
		@SuppressWarnings("rawtypes")
		List infos = this.loyaltyProductDao.findByCriteria(criteria);
		UserBadgeInfo info = null;
		if(infos == null || infos.isEmpty()) {
			info  = new UserBadgeInfo();
			info.setBadge(this.baseDao.load(Badge.class, 1l));
			info.setValidPoints(0d);
			return info;
		}
		return (UserBadgeInfo) infos.get(0);
	}

	@Override
	public List<Badge> getAllBadges() {
		return this.baseDao.getAll(Badge.class);
	}

	@Override
	@Transactional
	public void debitKarmaPoints(Long orderId) {
		Order order = this.orderDao.get(Order.class, orderId);
		double existingKarmaPoints = this.calculateValidPoints(order.getUser().getId());
		double karmaPoints = this.calculateLoyaltyPointsForOrder(orderId);
		if (existingKarmaPoints < karmaPoints) {
			throw new HealthkartRuntimeException("Not sufficient karma points") {
				private static final long serialVersionUID = 1L;
			};
		}
		UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
		profile.setStatus(KarmaPointStatus.PENDING);
		profile.setTransactionType(TransactionType.DEBIT);
		// add negative values for karma points
		profile.setKarmaPoints(-(karmaPoints));
		UserOrderKey userOrderKey = new UserOrderKey(order, order.getUser());
		profile.setUserOrderKey(userOrderKey);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	@Transactional
	public void approveKarmaPoints(Long orderId) {
		UserOrderKarmaProfile profile = this.getUserOrderKarmaProfile(orderId);
		profile.setStatus(KarmaPointStatus.APPROVED);
		//TODO recalculate the user badge
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
		DetachedCriteria criteria = this.prepareLoyaltyProductCriteria(null);
		criteria.add(Restrictions.eq("pv.id", variantId));

		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> products = this.loyaltyProductDao.findByCriteria(criteria);
		if (products != null && products.size() > 0) {
			return products.iterator().next();
		}
		return null;
	}

	@Override
	public double calculateLoyaltyPointsForOrder(Long orderId) {
		Order order = this.orderDao.get(Order.class, orderId);
		Set<CartLineItem> cartLineItems = order.getCartLineItems();
		return this.calculateLoyaltyPointsForCart(cartLineItems);
	}

	@Override
	public double calculateLoyaltyPointsForCart(Collection<CartLineItem> cartLineItems) {
		double points = 0d;
		for (CartLineItem cartLineItem : cartLineItems) {
			LoyaltyProduct loyaltyProduct = this.getProductByVariantId(cartLineItem.getProductVariant().getId());
			points += (loyaltyProduct.getPoints() * cartLineItem.getQty());
		}
		return points;
	}

	@Override
	public void cancelLoyaltyPoints(Long orderId) {
		UserOrderKarmaProfile profile = this.getUserOrderKarmaProfile(orderId);
		profile.setStatus(KarmaPointStatus.CANCELED);
		//TODO recalculate the badge.
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}


	@Override
	public Page getUserLoyaltyProfileHistory(User user, int page, int perPage) {
		return this.userOrderKarmaProfileDao.listKarmaPointsForUser(user, page, perPage);
	}
	
	@Override
	public List<LoyaltyProduct> getProductsByPoints(double minPoints, double maxPoints) {
		DetachedCriteria criteria = this.prepareLoyaltyProductCriteria(null);
		//DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.add(Restrictions.ge("points", minPoints));
		criteria.add(Restrictions.le("points", maxPoints));
		criteria.addOrder(org.hibernate.criterion.Order.asc("points"));
		
		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> prodList = this.loyaltyProductDao.findByCriteria(criteria);
		
		return prodList;
	}

	@Override
	public double calculateValidPoints(Long userId) {
		DetachedCriteria crit = DetachedCriteria.forClass(UserOrderKarmaProfile.class);
		crit.add(Restrictions.eq("userOrderKey.user.id", userId));
		crit.add(Restrictions.eq("status", KarmaPointStatus.APPROVED));
		
		// Check for 2 years
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		crit.add(Restrictions.ge("updateTime", cal.getTime()));
	
		crit.setProjection(Projections.sum("karmaPoints"));
		
		@SuppressWarnings("rawtypes")
		List vList = this.loyaltyProductDao.findByCriteria(crit);
		if(vList == null || vList.isEmpty() || vList.get(0) == null) {
			return 0d;
		}
		return  (Double)vList.get(0);
	}
	

	@Override
	public double calculateAnnualSpend(User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.add(Restrictions.eq("user", user));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		criteria.add(Restrictions.ge("createDate", calendar.getTime()));
		criteria.createAlias("payment", "pmt");
		criteria.setProjection(Projections.sum("pmt.amount"));
		criteria.add(Restrictions.eq("orderStatus.id", EnumOrderStatus.Delivered.asOrderStatus().getId()));

		Double annualSpend = (Double) this.loyaltyProductDao.findByCriteria(criteria).iterator().next();
		return annualSpend;
	}

	
	@Override
	public void reviseBadgeInfoForUser (User user, Double transactionAmount) {
		DetachedCriteria crit = DetachedCriteria.forClass(UserBadgeInfo.class);
		crit.add(Restrictions.eq("user", user));
//		crit.setResultTransformer(Transformers.aliasToBean(UserBadgeInfo.class));
		UserBadgeInfo info = (UserBadgeInfo) this.loyaltyProductDao.findByCriteria(crit).get(0);

		if (info!= null) {
			// An old user
			Badge oldBadge = info.getBadge();
			//info.setCreditedPoints(info.getCreditedPoints() + 
				//	(transactionAmount * oldBadge.getLoyaltyMultiplier()) );
			info.setValidPoints(this.calculateValidPoints(user.getId()));
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);
			Double spend = this.calculateAnnualSpend(user);
			if (spend > info.getBadge().getMaxScore() || 
					info.getUpdationTime().before(cal.getTime())) {
				// revise Badge
				
				for (Badge badge : this.getAllBadges()) {
					if (spend > badge.getMinScore() && spend < badge.getMaxScore()) {
						info.setBadge(badge);
						info.setUpdationTime(Calendar.getInstance().getTime());
						break;
					}
							
				}
			} else {
				// No revision required
				info.setBadge(oldBadge);
			}
			
		} else {
			
			// A new user for Loyalty
			info = new UserBadgeInfo();
			info.setUser(user);
			
			// Fetch lowest Badge
			DetachedCriteria criteriaForBadge = DetachedCriteria.forClass(Badge.class);
			criteriaForBadge.add(Restrictions.eq("minScore", 0));
			Badge newBadge = (Badge)this.loyaltyProductDao.findByCriteria(criteriaForBadge).get(0);
			info.setBadge(newBadge);
			
			// Set all the points
/*			info.setCreditedPoints(transactionAmount * newBadge.getLoyaltyMultiplier());
			info.setDebitedPoints(0.0);
			info.setValidPoints(transactionAmount * newBadge.getLoyaltyMultiplier());
*/			
			info.setValidPoints(transactionAmount * newBadge.getLoyaltyMultiplier());
			Calendar cal = Calendar.getInstance();
			Date currentTime = cal.getTime();
			
			// set all the dates
			info.setCreationTime(currentTime);
			info.setUpdationTime(currentTime);
			
			// next points expire date 2 years from now
			cal.add(Calendar.YEAR, 2);
//			info.setPointsRevisionDate(cal.getTime());
			
		}
	}
	
	public void convertLoyaltyToRewardPoints () {
		
	}
	
	
	@Override
	public double calculateUpgradePoints (UserBadgeInfo info) {
		Double creditedPoints = this.calculateAnnualSpend(info.getUser());
		//info.getCreditedPoints();
		for (Badge badge : this.getAllBadges()) {
			if (creditedPoints > badge.getMinScore() && creditedPoints < badge.getMaxScore()) {
				return (badge.getMaxScore() - creditedPoints) + 1;
			}
		}
		
		// In case no upgrade possible
		return -1.0;
		
	}
	
	@Override
	public void reviseUserBadgeInfo(User user) {
		// TODO Auto-generated method stub
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
