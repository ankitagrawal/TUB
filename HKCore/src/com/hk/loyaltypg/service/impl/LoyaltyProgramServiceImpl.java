package com.hk.loyaltypg.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.FileBean;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVReader;

import com.akube.framework.dao.Page;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.discount.OfferConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.loyaltypg.UserBadgeInfo;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.KarmaPointStatus;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.TransactionType;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.offer.rewardPoint.RewardPoint;
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

	private final double LOYALTY_JOINING_BONUS= 15.0;
	
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
		if (ids.size()==0) {
			return new ArrayList<LoyaltyProduct>();
		}
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
	public double creditKarmaPoints(Order order) {
		boolean creditLoyaltyPoints = true;
		double karmaPoints =0;
		UserOrderKarmaProfile karmaProfile = this.getUserOrderKarmaProfile(order.getId());
		if(karmaProfile != null && karmaProfile.getTransactionType() == TransactionType.CREDIT) {
			return karmaPoints=karmaProfile.getKarmaPoints();
		}
		
		UserBadgeInfo badgeInfo = this.getUserBadgeInfo(order.getUser());
		double loyaltyPercentage = badgeInfo.getBadge().getLoyaltyPercentage();
		Double amount = order.getPayment().getAmount();

		 OfferInstance offerInstance = order.getOfferInstance();
         if (offerInstance != null) {
             Coupon coupon = offerInstance.getCoupon();
             if (coupon != null && coupon.getCode().equalsIgnoreCase(OfferConstants.HK_EMPLOYEE_CODE)) {
             		creditLoyaltyPoints=false;
             	}
             }
         
         if (creditLoyaltyPoints || !(badgeInfo.getBadge().getBadgeName().equalsIgnoreCase("NORMAL"))) {
        	karmaPoints = (amount * loyaltyPercentage)/100;
        	if (karmaPoints ==0) {
        		return 0;
        	}
        	UserOrderKarmaProfile profile = new UserOrderKarmaProfile();
        	profile.setStatus(KarmaPointStatus.PENDING);
        	profile.setTransactionType(TransactionType.CREDIT);
        	profile.setKarmaPoints(karmaPoints);
        	profile.setUser(order.getUser());
        	profile.setOrder(order);
        	this.userOrderKarmaProfileDao.saveOrUpdate(profile);
        }
         return karmaPoints;
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
			info.setUser(user);
			info.setCreationTime(Calendar.getInstance().getTime());
			info.setUpdationTime(Calendar.getInstance().getTime());
			return info;
		}
		info = infos.iterator().next();
		return info;
	}

	@Override
	public Collection<Badge> getAllBadges() {
		return new java.util.TreeSet<Badge>(this.baseDao.getAll(Badge.class));
	}

	@Override
	@Transactional
	public void debitKarmaPoints(Order order) {
		UserOrderKarmaProfile karmaProfile = this.getUserOrderKarmaProfile(order.getId());
		if(karmaProfile != null && karmaProfile.getTransactionType() == TransactionType.DEBIT) {
			throw new RuntimeException("User order karma profile already exist for given orer and user");
		}
		
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
		profile.setUser(order.getUser());
		profile.setOrder(order);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
	}

	@Override
	@Transactional
	public void approveKarmaPoints(Order order) {
		UserOrderKarmaProfile profile = this.getUserOrderKarmaProfile(order.getId());
		profile.setStatus(KarmaPointStatus.APPROVED);
		this.userOrderKarmaProfileDao.saveOrUpdate(profile);
		this.updateUserBadgeInfo(profile.getUser());
	}

	@Override
	@Transactional
	public void updateUserBadgeInfo(User user) {
		double annualSpend = this.calculateAnualSpend(user);

		List<Badge> badges = this.baseDao.getAll(Badge.class);
		Badge calculatedBadge = this.baseDao.get(Badge.class, 1l);
		for (Badge badge : badges) {
			if (annualSpend >= badge.getMinScore() && (annualSpend <= badge.getMaxScore() || badge.getMaxScore() == -1)) {
				calculatedBadge = badge;
				break;
			}
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		UserBadgeInfo userBadgeInfo = this.getUserBadgeInfo(user);
		if (calculatedBadge.compareTo(userBadgeInfo.getBadge()) > 0
				|| userBadgeInfo.getUpdationTime().before(cal.getTime())) {
			userBadgeInfo.setBadge(calculatedBadge);
			userBadgeInfo.setUpdationTime(Calendar.getInstance().getTime());
			this.baseDao.save(userBadgeInfo);
		}

	}

	@Override
	@Transactional
	public void createNewUserBadgeInfo(User user) {
		double annualSpend = this.calculateAnualSpend(user);
		Order bonusOrder = this.baseDao.get(Order.class, -1l);
		List<Badge> badges = this.baseDao.getAll(Badge.class);
		Badge calculatedBadge = this.baseDao.get(Badge.class, 1l);
		Date currentTime = Calendar.getInstance().getTime();
		for (Badge badge : badges) {
			if (annualSpend >= badge.getMinScore() && (annualSpend <= badge.getMaxScore() || badge.getMaxScore() == -1)) {
				calculatedBadge = badge;
				break;
			}
		}
		UserBadgeInfo userBadgeInfo = this.getUserBadgeInfo(user);
		if (calculatedBadge.compareTo(userBadgeInfo.getBadge()) > 0) {
			userBadgeInfo.setBadge(calculatedBadge);
			userBadgeInfo.setUpdationTime(currentTime);
			this.baseDao.save(userBadgeInfo);
		} else {
			this.baseDao.save(userBadgeInfo);
		}
		UserOrderKarmaProfile bonusProfile = new UserOrderKarmaProfile();
		bonusProfile.setUser(user);
		bonusProfile.setOrder(bonusOrder);
		bonusProfile.setKarmaPoints(this.LOYALTY_JOINING_BONUS);
		bonusProfile.setCreationTime(currentTime);
		bonusProfile.setUpdateTime(currentTime);
		bonusProfile.setTransactionType(TransactionType.CREDIT);
		bonusProfile.setStatus(KarmaPointStatus.BONUS);
		this.loyaltyProductDao.saveOrUpdate(bonusProfile);
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
		this.updateUserBadgeInfo(profile.getUser());
	}

	@Override
	public Page getUserLoyaltyProfileHistory(User user, int page, int perPage) {
		return this.userOrderKarmaProfileDao.listKarmaPointsForUser(user, page, perPage);
	}

	@Override
	public double calculateLoyaltyPoints(User user) {
		Double credits = this.calculatePoints(user.getId(), TransactionType.CREDIT, KarmaPointStatus.APPROVED, KarmaPointStatus.BONUS);
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
		criteria.add(Restrictions.eq(LoyaltyProductAlias.PRODUCT.alias + ".hidden", Boolean.FALSE));
		criteria.createAlias(LoyaltyProductAlias.PRODUCT.alias + ".primaryCategory", LoyaltyProductAlias.CATEGORY.alias,
				CriteriaSpecification.INNER_JOIN);
		criteria.addOrder(org.hibernate.criterion.Order.asc("points"));
		if (search != null) {
			if (search.getCategoryName() != null) {
				criteria.add(Restrictions.eq(LoyaltyProductAlias.CATEGORY.alias + ".name", search.getCategoryName()));
			}
			if (search.getRange() != null) {
				criteria.add(Restrictions.ge("points", search.getRange().getStart()));
				criteria.add(Restrictions.le("points", search.getRange().getEnd()));
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
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("transactionType", transactionType));

		@SuppressWarnings("rawtypes")
		List vList = this.loyaltyProductDao.findByCriteria(criteria);
		if (vList == null || vList.isEmpty() || vList.get(0) == null) {
			return 0d;
		}
		return (Double) vList.get(0);
	}

	private UserOrderKarmaProfile getUserOrderKarmaProfile(Long orderId) {
		if(orderId == -1l) {
			throw new RuntimeException("This API is not supposed to query order id with -1");
		}
		Order order = this.orderDao.get(Order.class, orderId);
		DetachedCriteria criteria = DetachedCriteria.forClass(UserOrderKarmaProfile.class);
		criteria.add(Restrictions.eq("user.id", order.getUser().getId()));
		criteria.add(Restrictions.eq("order.id", order.getId()));
		
		List<UserOrderKarmaProfile> list = this.userOrderKarmaProfileDao.findByCriteria(criteria);
		if(list == null || list.size() == 0) {
			return null;
		}
		return list.iterator().next();
	}

	@Override
	public double convertLoyaltyToRewardPoints(User user) {
	
		double loyaltyPoints = this.calculateLoyaltyPoints(user);
		double totalPointsConverted = -1;	
		if (loyaltyPoints > 0) {
			String comment = "Reward Points converted from Loyalty points";
			Order orderReward = this.orderDao.get(Order.class, -1l);
			UserOrderKarmaProfile rewardProfile = new UserOrderKarmaProfile();
			rewardProfile.setUser(user);
			rewardProfile.setKarmaPoints(-loyaltyPoints);
			rewardProfile.setCreationTime(Calendar.getInstance().getTime());
			rewardProfile.setUpdateTime(Calendar.getInstance().getTime());
			rewardProfile.setOrder(orderReward);
			rewardProfile.setTransactionType(TransactionType.DEBIT);
			rewardProfile.setStatus(KarmaPointStatus.REWARDED);
			
			// add reward points
			RewardPoint loyaltyRewardPoints = this.rewardPointService.addRewardPoints(user, null, orderReward, loyaltyPoints, comment,
					EnumRewardPointStatus.APPROVED, EnumRewardPointMode.HKLOYALTY_POINTS.asRewardPointMode());
			 
	        this.rewardPointService.approveRewardPoints(Arrays.asList(loyaltyRewardPoints), new DateTime().plusDays(0).toDate());

	        // save reward profile
			this.userOrderKarmaProfileDao.saveOrUpdate(rewardProfile);
			totalPointsConverted = loyaltyPoints;
 		} 
		return totalPointsConverted;
	}

	@Override
	public void uploadLoyaltyProductsCSV(FileBean csvFileReader, List<String> errorMessages) {
		Set<LoyaltyProduct> uploadedProducts = new HashSet<LoyaltyProduct>();
		if (this.validateLoyaltyCsvFile(csvFileReader, uploadedProducts, errorMessages)) {
			this.loyaltyProductDao.saveOrUpdate(uploadedProducts);
		}
	}
	
	private boolean validateLoyaltyCsvFile(FileBean csvFileReader, Set<LoyaltyProduct> uploadedProducts, List<String> errorMessages) {
		boolean flag = false;
		List<LoyaltyProduct> products;
		List<ProductVariant> variants;
		DetachedCriteria criteria;
		DetachedCriteria variantCriteria;

		try {
			CSVReader reader = new CSVReader(csvFileReader.getReader());
			List<String[]> loyaltyProductList = reader.readAll();
			
			for(String[] productRow: loyaltyProductList) {
				if(productRow.equals(loyaltyProductList.get(0))) {
					continue;
				}
				criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
				criteria.add(Restrictions.eq("variant.id", productRow[0]));
				products = this.loyaltyProductDao.findByCriteria(criteria);
				LoyaltyProduct prod;
				try {
					if (products!=null && !products.isEmpty()) {
						prod = products.iterator().next();
						prod.setPoints(Double.parseDouble(productRow[1]));
					} else {
						// For a new product, check if the variant exists
						variantCriteria = DetachedCriteria.forClass(ProductVariant.class);
						variantCriteria.add(Restrictions.eq("id", productRow[0]));
						variants = this.loyaltyProductDao.findByCriteria(variantCriteria);
						if (variants!=null && !variants.isEmpty()) {
							prod = new LoyaltyProduct();
							prod.setVariant(variants.iterator().next());
							prod.setPoints(Double.parseDouble(productRow[1]));
						} else {
							errorMessages.add("No Product Variant found for the variant Id " + productRow[0]);
							continue;
						}
					}
					if (!uploadedProducts.add(prod)) {
						errorMessages.add("Duplicate entries found in the file for the variant Id " + productRow[0]);
					}
				}catch (NumberFormatException nfe) {
					errorMessages.add(productRow[1]+ " is not suitable value as points for product variant " + productRow[0]);
				}
				reader.close();
			}
			
		} catch (FileNotFoundException e) {
			errorMessages.add("File not found for the given path");
		} catch (IOException ioe) {
			errorMessages.add("Failed to read the uploaded file");	
			ioe.printStackTrace();
		} catch (Exception e) {
			errorMessages.add("Bad file format or corrupt file");
		}
		
		if (!(errorMessages.size()>0)) {
			flag = true;
		}
		return flag;
	}

	@Override
	public void uploadBadgeInfoCSV(FileBean csvFileReader, List<String> errorMessages) {
		Set<UserBadgeInfo> uploadedBadges = new HashSet<UserBadgeInfo>();
		if (this.validateBadgeCsvFile(csvFileReader, uploadedBadges, errorMessages)) {
			this.loyaltyProductDao.saveOrUpdate(uploadedBadges);
		}
	}

	private boolean validateBadgeCsvFile(FileBean csvFileReader,Set<UserBadgeInfo> uploadedBadges, List<String> errorMessages) {
		boolean flag = false;
		List<UserBadgeInfo> badges;
		DetachedCriteria criteria;
		
		try {
			CSVReader reader = new CSVReader(csvFileReader.getReader());
			List<String[]> badgeList = reader.readAll();

			for(String[] badgeRow: badgeList) {
				if(badgeRow.equals(badgeList.get(0))) {
					continue;
				}
				criteria = DetachedCriteria.forClass(UserBadgeInfo.class);
				try {
				criteria.add(Restrictions.eq("user.id", Long.parseLong(badgeRow[0])));
				} catch(NumberFormatException nfe) {
					errorMessages.add("User Id: " + badgeRow[0] + " is not a valid user id.");
				}
				badges = this.loyaltyProductDao.findByCriteria(criteria);
				UserBadgeInfo localBadge;
				if (badges!=null && !badges.isEmpty()) {
					localBadge = badges.iterator().next();
					localBadge.setCardNumber(badgeRow[1]);
				} else {
					// For a new user, automatic badge creation not allowed
					errorMessages.add("User Id: "+ badgeRow[0] +" is not a loyalty member" );
					continue;
				}

				if (!uploadedBadges.add(localBadge)) {
					errorMessages.add("Duplicate entries found in the file for the User Id " + badgeRow[0]);
				}
				reader.close();
			}

		} catch (FileNotFoundException e) {
			errorMessages.add("File not found for the given path");
		} catch (IOException ioe) {
			errorMessages.add("Failed to read the uploaded file");	
			ioe.printStackTrace();
		} catch (Exception e) {
			errorMessages.add("Bad file format or corrupt file");
		}

		if (!(errorMessages.size()>0)) {
			flag = true;
		}
		return flag;
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
