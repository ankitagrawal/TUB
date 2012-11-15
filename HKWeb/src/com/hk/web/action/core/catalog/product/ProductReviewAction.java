package com.hk.web.action.core.catalog.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.review.EnumReviewStatus;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.SeoData;
import com.hk.domain.review.UserReview;
import com.hk.pact.dao.review.ReviewDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.review.ReviewService;
import com.hk.util.SeoManager;

@Component
public class ProductReviewAction extends BasePaginatedAction {
	
	private Product product;
	private Page productReviewPage;
	private List<UserReview> productReviews = new ArrayList<UserReview>();
	private Integer defaultPerPage = 10;
	private SeoData seoData;
	private UserReview review;
	private boolean captchaMatch;

	
	@Autowired
	private SeoManager seoManager;
	@Autowired
	private UserService userService;
	@Autowired
	private ReviewDao userReviewDao;
	@Autowired
	private ReviewService reviewService;


	@SuppressWarnings("unchecked")
    @DefaultHandler
	public Resolution pre() {
		productReviewPage = reviewService.getProductReviews(product, Arrays.asList(EnumReviewStatus.Published.getId()), getPageNo(), getPerPage());
		if (productReviewPage != null) {
			productReviews = productReviewPage.getList();
		}
		if(product != null) {
			seoData = seoManager.generateSeo(product.getId());
		} 

		return new ForwardResolution("/pages/productReviews.jsp");
	}

	@Secure
	public Resolution writeNewReview() {
		review = new UserReview();
		review.setPostedBy(userService.getLoggedInUser());
		return new ForwardResolution("/pages/postReview.jsp");
	}

	public Resolution postReview() {
		review.setReviewDate(new Date());
		review.setReviewStatus(reviewService.getReviewStatus(EnumReviewStatus.Pending.getId()));
		userReviewDao.save(review);
		captchaMatch = true;
		return new ForwardResolution("/pages/postReview.jsp");

	}

	@ValidationMethod(on = "postReview")
	public void captchaValidation() {
		String challengeField = getContext().getRequest().getParameter("recaptcha_challenge_field");
		String responseField = getContext().getRequest().getParameter("recaptcha_response_field");
		if (StringUtils.isBlank(responseField))
			responseField = "null";

		ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(HealthkartConstants.recaptchaPublicKey, HealthkartConstants.recaptchaPrivateKey, false);
		ReCaptchaResponse response = captcha.checkAnswer(getContext().getRequest().getRemoteAddr(), challengeField, responseField);
		if (!response.isValid()) {
			product = review.getProduct();
			getContext().getValidationErrors().add("Captcha", new LocalizableError("/Signup.action.captcha.invalid"));
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<UserReview> getProductReviews() {
		return productReviews;
	}

	public SeoData getSeoData() {
		return seoData;
	}

	public UserReview getReview() {
		return review;
	}

	public void setReview(UserReview review) {
		this.review = review;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return productReviewPage == null ? 0 : productReviewPage.getTotalPages();
	}

	public int getResultCount() {
		return productReviewPage == null ? 0 : productReviewPage.getTotalResults();
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("product");
		return params;
	}

	

	public void setSeoManager(SeoManager seoManager) {
		this.seoManager = seoManager;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setUserReviewDao(ReviewDao userReviewDao) {
		this.userReviewDao = userReviewDao;
	}

	public void setReviewStatusDao(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	public boolean isCaptchaMatch() {
		return captchaMatch;
	}

	public void setCaptchaMatch(boolean captchaMatch) {
		this.captchaMatch = captchaMatch;
	}
}
