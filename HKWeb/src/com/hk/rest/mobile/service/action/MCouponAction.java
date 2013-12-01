package com.hk.rest.mobile.service.action;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.akube.framework.gson.JsonUtils;
import com.hk.admin.manager.EmployeeManager;
import com.hk.admin.manager.IHOManager;
import com.hk.constants.coupon.EnumCouponType;
import com.hk.constants.discount.OfferConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.coupon.CouponType;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferEmailDomain;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.service.discount.CouponService;
import com.hk.pact.service.order.OrderService;
import com.hk.rest.mobile.service.model.MCouponResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;

@Path("/mCoupon")
@Component
public class MCouponAction extends MBaseAction {

	@Autowired
	private CouponService couponService;
	@Autowired
	private OfferInstanceDao offerInstanceDao;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private OfferManager offerManager;
	@Autowired
	private IHOManager ihoManager;
	@Autowired
	private EmployeeManager employeeManager;

	@Autowired
	OrderService orderService;

	// private String couponCode;

	private Coupon coupon;
	// private String message;
	// private boolean success = false;

	private String error;

	public static final String error_role = "error_role";
	public static final String error_alreadyUsed = "error_alreadyUsed";
	public static final String error_alreadyApplied = "error_alreadyApplied";
	public static final String error_alreadyReferrer = "error_alreadyReferrer";
	public static final String error_referralNotAllowed = "error_referralNotAllowed";

	private OfferInstance offerInstance;

	@POST
	@Path("/applyCoupon/")
	@Produces("application/json")
	public String applyCoupon(@FormParam("coupon") String couponCode,
			@Context HttpServletResponse response) {

		String status = MHKConstants.STATUS_OK;
		String message = MHKConstants.STATUS_DONE;
		try {
			if (StringUtils.isBlank(couponCode)) {
				status = MHKConstants.STATUS_ERROR;
				message = MHKConstants.COUPON_CODE_RQD;
				return JsonUtils.getGsonDefault().toJson(
						new HealthkartResponse(status, message, message));
			}

			coupon = couponService.findByCode(couponCode);
			User user = getUserService().getUserById(getPrincipal().getId());
			Order order = orderManager.getOrCreateOrder(user);

			/*
			 * If coupon is NULL Check if the user has applied an IHO Coupon for
			 * the first time and create the coupon - it will validate and then
			 * create coupon
			 */
			if (coupon == null) {
				coupon = ihoManager.createIHOCoupon(user, couponCode);
			}
			if (coupon == null) {
				coupon = employeeManager.createEmpCoupon(user, couponCode);
			}

			if (coupon == null) {
				status = MHKConstants.STATUS_ERROR;
				message = MHKConstants.INVALID_COUPON;
				return JsonUtils.getGsonDefault().toJson(
						new HealthkartResponse(status, message, message));
			} else {
				List<OfferInstance> offerInstances = offerInstanceDao
						.findByUserAndCoupon(user, coupon);
				if (offerInstances != null && !offerInstances.isEmpty()) {
					offerInstance = offerInstances.get(0);
				}
				if (offerInstance != null && !coupon.getRepetitiveUsage()) {
					if (!offerInstance.isActive()) {
						error = error_alreadyUsed;
						message = MHKConstants.USED_OFFER;
								
						return JsonUtils.getGsonDefault().toJson(
								new HealthkartResponse(
										MHKConstants.STATUS_ERROR, message,
										MHKConstants.ERROR_ALREADYUSED));
					} else {
						error = error_alreadyApplied;
						order.setOfferInstance(offerInstance);
						message = "You have already added this coupon.";
						return JsonUtils.getGsonDefault().toJson(
								new HealthkartResponse(
										MHKConstants.STATUS_ERROR, message,
										MHKConstants.ERROR_ALREADYAPPLIED));
					}
				} else if (!coupon.isValid()) {
					message = "Coupon code has expired.";
					return JsonUtils.getGsonDefault().toJson(
							new HealthkartResponse(MHKConstants.STATUS_ERROR,
									message, message));
				} else if (!offerManager.isOfferValidForUser(coupon.getOffer(),
						user)) {
					error = error_role;
					Offer offer = coupon.getOffer();
					if (offer.getOfferEmailDomains().size() > 0) {
						message = "The offer is valid for the following domains only:";
						for (OfferEmailDomain offerEmailDomain : offer
								.getOfferEmailDomains()) {
							message += "<br/>"
									+ offerEmailDomain.getEmailDomain();
						}
					} else {
						message = "This offer is not activated for you yet";
					}
					return JsonUtils.getGsonDefault().toJson(
							new HealthkartResponse(MHKConstants.STATUS_ERROR,
									message, message));

				} else if (user.equals(coupon.getReferrerUser())) {
					message = "You are not allowed to use your own referrer code.";
					return JsonUtils.getGsonDefault().toJson(
							new HealthkartResponse(MHKConstants.STATUS_ERROR,
									message, message));
				} else if (coupon.getReferrerUser() != null
						&& user.getReferredBy() != null) {
					error = error_alreadyReferrer;
					message = "You have already mentioned your referrer.";
					return JsonUtils.getGsonDefault()
							.toJson(new HealthkartResponse(
									MHKConstants.STATUS_ERROR, message,
									MHKConstants.ERROR_ALREADYREFERRER));
				} else if (coupon.getReferrerUser() != null
						&& coupon.getCouponType() != null
						&& !coupon.getCouponType().getId()
								.equals(EnumCouponType.AFFILIATE.getId())
						&& user.getCreateDate().before(coupon.getCreateDate())) {
					error = error_referralNotAllowed;
					message = "You are not allowed to use this referrer coupon.";
					return JsonUtils.getGsonDefault().toJson(
							new HealthkartResponse(MHKConstants.STATUS_ERROR,
									message,
									MHKConstants.ERROR_REFERRALNOTALLOWED));
				} else if (coupon.getReferrerUser() != null
						&& orderManager.getOrderService()
								.getLatestOrderForUser(user) != null) {
					message = "Coupon can not be applied. This is a referral discount coupon and it is only valid before you place your first order.";
					return JsonUtils.getGsonDefault().toJson(
							new HealthkartResponse(MHKConstants.STATUS_ERROR,
									message, message));
				} else {
					Date offerInstanceEndDate = null;

					// add referredBy to the user if coupon contains the
					// referrerUser
					if (coupon.getReferrerUser() != null) {
						// add affiliate_to to the user if its an affiliate
						// coupon
						CouponType couponType = coupon.getCouponType();
						if (couponType != null
								&& couponType.getId().equals(
										EnumCouponType.AFFILIATE.getId())) {
							Assert.assertNull(user.getAffiliateTo());
							user.setAffiliateTo(coupon.getReferrerUser());
							user = (User) getBaseDao().save(user);
						} else { // its a referral coupon
							Assert.assertNull(user.getReferredBy());
							user.setReferredBy(coupon.getReferrerUser());
							user = (User) getBaseDao().save(user);
							offerInstanceEndDate = new DateTime()
									.plusDays(
											OfferConstants.MAX_ALLOWED_DAYS_FOR_15_PERCENT_REFERREL_DISCOUNT)
									.toDate();
						}
					}
					if (coupon.getRepetitiveUsage()) {
						List<OfferInstance> activeOfferInstances = offerInstanceDao
								.findActiveOfferInstances(user,
										coupon.getOffer());
						if (activeOfferInstances == null
								|| activeOfferInstances.isEmpty()) {
							offerInstance = offerInstanceDao
									.createOfferInstance(coupon.getOffer(),
											coupon, user, offerInstanceEndDate);
						}
					} else {
						offerInstance = offerInstanceDao.createOfferInstance(
								coupon.getOffer(), coupon, user,
								offerInstanceEndDate);
					}
					order.setOfferInstance(offerInstance);
					coupon.setAlreadyUsed(coupon.getAlreadyUsed() + 1);
					couponService.save(coupon);

					message = "Coupon applied successfully.";
					Map<String, Object> couponMap = new HashMap<String, Object>();
					Offer offer = offerInstance.getOffer();
					couponMap.put("terms", offer.getTerms());
					String endDate = offer.getEndDate().toString();
					if(null!=offer.getEndDate()&&!"".equals(offer.getEndDate()))
						endDate = DateFormat.getDateInstance().format(offer.getEndDate());
					couponMap.put("valid_till", endDate);
					couponMap.put("coupon",
							null != coupon.getCode() ? coupon.getCode()
									: MHKConstants.NO_COUPON_CODE);
					return JsonUtils.getGsonDefault().toJson(
							new HealthkartResponse(MHKConstants.STATUS_DONE,
									message, couponMap));
				}
			}
		} catch (Exception e) {
			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(MHKConstants.STATUS_ERROR,
							MHKConstants.STATUS_ERROR,
							MHKConstants.STATUS_ERROR));
		}

	}

	@GET
	@Path("/getOffers/")
	@Produces("application/json")
	public String getOffers() {
		List<OfferInstance> offerInstanceList=null;

		OfferInstance selectedOffer=null;
		try {
			User user = getUserService().getUserById(getPrincipal().getId());
			Order order = orderManager.getOrCreateOrder(user);

			selectedOffer = order.getOfferInstance();
			offerInstanceList = offerInstanceDao.getActiveOffers(user);
			for (OfferInstance offerInstance : offerInstanceList) {
				if (offerInstance.equals(selectedOffer)) {
					order.setOfferInstance(offerInstance);
					orderService.save(order);
					break;
				}
			}
		} catch (Exception e) {
			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(MHKConstants.STATUS_ERROR,
							MHKConstants.STATUS_ERROR, null));
		}
		List<MCouponResponse> couponList = new ArrayList<MCouponResponse>();
		MCouponResponse couponResponse = null;
		for(OfferInstance offer:offerInstanceList){
			couponResponse = new MCouponResponse();
			couponResponse.setCouponCode(offer.getCoupon().getCode());

			String endDate = "";
			if (null != offer.getEndDate() && !"".equals(offer.getEndDate()))
				endDate = DateFormat.getDateInstance().format(offer.getEndDate());
			
			couponResponse.setEndDate(endDate);
			couponResponse.setId(offer.getId().toString());
			couponResponse.setOfferDescription(offer.getOffer().getDescription());
			String offerTerms = "";
			if(offer.getOffer().getTerms() != null){
				offerTerms = offer.getOffer().getTerms();
			}
			couponResponse.setOfferTerms(offerTerms);
			if(null!=selectedOffer)
			if(offer.getId().equals(selectedOffer.getId()))
				couponResponse.setSelectedOffer(true);
			couponList.add(couponResponse);
		}
		return JsonUtils.getGsonDefault().toJson(
				new HealthkartResponse(MHKConstants.STATUS_OK,
						MHKConstants.STATUS_DONE, couponList));
	}

	@POST
	@Path("/applyOffer/")
	@Produces("application/json")
	public String applyOffer(@FormParam("offer") long couponId,
	@Context HttpServletResponse response) {
		List<OfferInstance> offerInstanceList= null;
		OfferInstance selectedOffer = null;

		try {
			User user = getUserService().getUserById(getPrincipal().getId());
			Order order = orderManager.getOrCreateOrder(user);

			offerInstanceList = offerInstanceDao.getActiveOffers(user);
			for (OfferInstance offerInstance : offerInstanceList) {
				if (offerInstance.getId().equals(couponId)) {
					order.setOfferInstance(offerInstance);
					//orderService.save(order);
					selectedOffer = offerInstance;
				}
			}
			//selectedOffer = offerInstanceDao.findByUserAndOffer(user, offer);
			if(null!=selectedOffer){
			if (!selectedOffer.getUser().equals(user)) {
				HealthkartResponse healthkartResponse = new HealthkartResponse(
						MHKConstants.STATUS_ERROR, "Invalid offer selected.");
				return JsonUtils.getGsonDefault().toJson(healthkartResponse);
			}

			order.setOfferInstance(selectedOffer);
			orderService.save(order);

			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(MHKConstants.STATUS_OK,
							MHKConstants.OFFER_APPLIED));
			}else{
				return JsonUtils.getGsonDefault().toJson(
						new HealthkartResponse(MHKConstants.STATUS_OK,
								MHKConstants.NO_COUPON_OFFER_SELECTED));
			}
		} catch (Exception e) {
			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(MHKConstants.STATUS_ERROR,
							MHKConstants.STATUS_ERROR,
							MHKConstants.STATUS_ERROR));
		}

	}

	@POST
	@Path("/removeOffer/")
	@Produces("application/json")
	public String removeAppliedOffer() {
		try {
			User user = getUserService().getUserById(getPrincipal().getId());
			Order order = orderManager.getOrCreateOrder(user);
			order.setOfferInstance(null);
			orderService.save(order);

			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(HealthkartResponse.STATUS_OK,
							MHKConstants.OFFER_REMOVED));
		} catch (Exception e) {
			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(MHKConstants.STATUS_ERROR,
							MHKConstants.STATUS_ERROR,
							MHKConstants.STATUS_ERROR));
		}

	}

	public Coupon getCoupon() {
		return coupon;
	}

	public String getError() {
		return error;
	}

	public OfferInstance getOfferInstance() {
		return offerInstance;
	}

	public OfferInstanceDao getOfferInstanceDao() {
		return offerInstanceDao;
	}

	public void setOfferInstanceDao(OfferInstanceDao offerInstanceDao) {
		this.offerInstanceDao = offerInstanceDao;
	}

	public OrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public OfferManager getOfferManager() {
		return offerManager;
	}

	public void setOfferManager(OfferManager offerManager) {
		this.offerManager = offerManager;
	}

	public IHOManager getIhoManager() {
		return ihoManager;
	}

	public void setIhoManager(IHOManager ihoManager) {
		this.ihoManager = ihoManager;
	}

	public EmployeeManager getEmployeeManager() {
		return employeeManager;
	}

	public void setEmployeeManager(EmployeeManager employeeManager) {
		this.employeeManager = employeeManager;
	}
}