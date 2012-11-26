package com.hk.web.action.core.affiliate;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.Keys;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.coupon.EnumCouponType;
import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.exception.HealthKartCouponException;
import com.hk.impl.dao.CheckDetailsDaoImpl;
import com.hk.manager.AffiliateManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.discount.CouponService;
import com.hk.web.action.core.auth.LogoutAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Secure(hasAnyRoles = {RoleConstants.HK_AFFILIATE, RoleConstants.HK_AFFILIATE_UNVERIFIED, RoleConstants.ADMIN, RoleConstants.HK_AFFILIATE_MANAGER})
@Component
public class AffiliateAccountAction extends BaseAction {

	Affiliate affiliate;
	Offer offer;
	Double affiliateAccountAmount;
	Double affiliatePayableAmount;
	List<CheckDetails> checkDetailsList;
	private List<String> categories = new ArrayList<String>();
	List<Coupon> coupons;
	Address affiliateDefaultAddress;

	@Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
	private String adminDownloads;

	private static Logger logger = Logger.getLogger(AffiliateAccountAction.class);
	@Autowired
	AffiliateDao affiliateDao;
	@Autowired
	AffiliateManager affiliateManager;
	@Autowired
	CheckDetailsDaoImpl checkDetailsDao;
	@Autowired
	CouponService couponService;
	@Autowired
	AffilateService affilateService;
	@Autowired
	CategoryDao categoryDao;
	@Autowired
	private AddressDao addressDao;

	@DefaultHandler
	@DontValidate
	public Resolution affiliateAccount() {
		if (getPrincipal() != null) {
			logger.debug("principal" + getPrincipal().getId());
			User user = getUserService().getUserById(getPrincipal().getId());
			affiliate = affiliateDao.getAffilateByUser(user);
			if (user != null)
				logger.debug("user name : " + user.getName());
			if (affiliate != null) {
				logger.debug("affiliate id : " + affiliate.getId());
				affiliateAccountAmount = affiliateManager.getAmountInAccount(affiliate, null, null);
				affiliatePayableAmount = affiliateManager.getPayableAmount(affiliate);

				if (affiliate.getMainAddressId() != null) {
					affiliateDefaultAddress = getAddressDao().get(Address.class, affiliate.getMainAddressId());
				}
				if (affiliate.getCategories() != null) {
					for (Category category : affiliate.getCategories()) {
						categories.add(category.getName());
					}
				}
			} else {
				logger.debug("affiliate is null");
				addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
				return new ForwardResolution(LogoutAction.class);
			}
		} else {
			logger.debug("no principal.....");
		}
		return new ForwardResolution("/pages/affiliate/affiliateProfile.jsp");
	}

	public Resolution checksToAffiliate() {
		if (getPrincipal() != null) {
			User user = getUserService().getUserById(getPrincipal().getId());
			affiliate = affiliateDao.getAffilateByUser(user);
			checkDetailsList = checkDetailsDao.getCheckListByAffiliate(affiliate);
		}
		return new ForwardResolution("/pages/affiliate/checksToAffiliate.jsp");
	}

	public Resolution saveAffiliatePreferences() {
		if (affiliate != null) {
			Set<Category> categoryList = new HashSet<Category>();
			for (String category : categories) {
				if (category != null) {
					categoryList.add((Category) categoryDao.getCategoryByName(category));
				}
			}
			affiliate.setCategories(categoryList);
			affiliateDao.save(affiliate);
			addRedirectAlertMessage(new SimpleMessage("your preferences have been saved."));
		}
		return new RedirectResolution(AffiliateAccountAction.class);
	}

	public Resolution showCouponScreen() {
		if (getPrincipal() != null) {
			User user = getUserService().getUserById(getPrincipal().getId());
			affiliate = affiliateDao.getAffilateByUser(user);
			offer = affiliate.getOffer();
		}
		return new ForwardResolution("/pages/affiliate/affiliateCouponDownload.jsp");
	}

	public Resolution generateAffiliateCoupons() {
		String couponCode = "HK";
		String endPart = "AFF";
		if (getPrincipal() != null) {
			User user = getUserService().getUserById(getPrincipal().getId());
			affiliate = affiliateDao.getAffilateByUser(user);
		}
		if (affiliate != null) {
			Offer offer = affiliate.getOffer();
			if (offer != null) {
				Long numberOfCouponsToDownload = 0L;
				Long numberOfCoupons = affiliate.getWeeklyCouponLimit() - affilateService.getMaxCouponsLeft(affiliate);
				if (numberOfCoupons == 0L) {
					numberOfCouponsToDownload = affiliate.getWeeklyCouponLimit();
				} else {
					numberOfCouponsToDownload = numberOfCoupons;
				}
				if (numberOfCouponsToDownload > 0) {
					if (numberOfCoupons == 0L) {
						coupons = couponService.getAffiliateUnusedCoupons(affiliate);
					} else {
						try {
							coupons = couponService.generateCoupons("AFF", "HK", numberOfCouponsToDownload, false, new DateTime().plusMonths(1).toDate(), 1L, 0L, offer, EnumCouponType.AFFILIATE.asCouponType(), affiliate.getUser());
//				addRedirectAlertMessage(new SimpleMessage("your preferences have been saved."));
						} catch (HealthKartCouponException e) {
							addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
							return getContext().getSourcePageResolution();
						}
					}
						// save coupons file to admin dir
						File couponsDir = new File(getSavedCouponsDirPath());
						if (!couponsDir.exists()) {
							couponsDir.mkdirs();
						}

						String couponFileName = couponCode + "-" + endPart + "-" + coupons.size() + "-coupons-" + BaseUtils.getCurrentTimestamp().getTime() + ".txt";
						final File couponFile = new File(couponsDir.getAbsolutePath() + "/" + couponFileName);
						Writer output = null;
						try {
							couponFile.createNewFile();
							output = new BufferedWriter(new FileWriter(couponFile));
							if(numberOfCoupons == 0){
								output.write("You have currently exceeded your coupon limit, These are your current unused coupons.");
							}
							output.write("Offer: " + offer.getDescription() + BaseUtils.newline);
							output.write("Nuber of coupons: " + numberOfCouponsToDownload + BaseUtils.newline);
							output.write("Coupons: " + BaseUtils.newline);
							for (Coupon coupon : coupons) {
								output.write(coupon.getCode() + BaseUtils.newline);
							}
						} catch (IOException e) {
							logger.error("Error while making a coupons file:", e);
						} finally {
							if (output != null) {
								try {
									output.close();
								} catch (IOException e) {
									logger.error("error while closing the coupons file", e);
								}
							}
						}

						final int contentLength = (int) couponFile.length();
						if (numberOfCoupons == 0) {
							addRedirectAlertMessage(new SimpleMessage("You have already downloaded your this weeks coupons, Please find the downloaded list again"));
						} else {
							addRedirectAlertMessage(new LocalizableMessage("/CreateCoupon.action.coupon.created"));
						}
						// give option to download the coupons file
						return new Resolution() {
							public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
								OutputStream out = null;
								InputStream in = new BufferedInputStream(new FileInputStream(couponFile));
								res.setContentLength(contentLength);
								res.setHeader("Content-Disposition", "attachment; filename=\"" + couponFile.getName() + "\";");
								out = res.getOutputStream();

								// Copy the contents of the file to the output stream
								byte[] buf = new byte[4096];
								int count = 0;
								while ((count = in.read(buf)) >= 0) {
									out.write(buf, 0, count);
								}
							}
						};
				} else {
					addRedirectAlertMessage(new SimpleMessage("You have already exceeded your max weekly coupon limit, that being " + affiliate.getWeeklyCouponLimit()));
					return new RedirectResolution(AffiliateAccountAction.class);
				}
			}
		}
		addRedirectAlertMessage(new SimpleMessage("Sorry some error occurred!!"));
		return new RedirectResolution(AffiliateAccountAction.class);
	}

	public Affiliate getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(Affiliate affiliate) {
		this.affiliate = affiliate;
	}

	public Double getAffiliateAccountAmount() {
		return affiliateAccountAmount;
	}

	public void setAffiliateAccountAmount(Double affiliateAccountAmount) {
		this.affiliateAccountAmount = affiliateAccountAmount;
	}

	public List<CheckDetails> getCheckDetailsList() {
		return checkDetailsList;
	}

	public void setCheckDetailsList(List<CheckDetails> checkDetailsList) {
		this.checkDetailsList = checkDetailsList;
	}

	private String getSavedCouponsDirPath() {
		return adminDownloads + "/coupons";
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public Double getAffiliatePayableAmount() {
		return affiliatePayableAmount;
	}

	public void setAffiliatePayableAmount(Double affiliatePayableAmount) {
		this.affiliatePayableAmount = affiliatePayableAmount;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public Address getAffiliateDefaultAddress() {
		return affiliateDefaultAddress;
	}

	public void setAffiliateDefaultAddress(Address affiliateDefaultAddress) {
		this.affiliateDefaultAddress = affiliateDefaultAddress;
	}

	public AddressDao getAddressDao() {
		return addressDao;
	}
}
