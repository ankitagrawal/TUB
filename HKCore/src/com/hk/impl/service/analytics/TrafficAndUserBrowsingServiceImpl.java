package com.hk.impl.service.analytics;

import com.hk.pact.service.analytics.TrafficAndUserBrowsingService;
import com.hk.pact.dao.BaseDao;
import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.util.TrafficSourceFinder;
import com.hk.domain.analytics.TrafficTracking;
import com.hk.domain.analytics.UserBrowsingHistory;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 22 Nov, 2012
 * Time: 3:21:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TrafficAndUserBrowsingServiceImpl extends BaseDaoImpl implements TrafficAndUserBrowsingService {

	private static Logger logger = LoggerFactory.getLogger(TrafficAndUserBrowsingServiceImpl.class);

	@Autowired
	BaseDao baseDao;

	@Transactional
	public TrafficTracking saveTrafficTracking(HttpServletRequest httpRequest, User user) {
		Map<String, String> trafficInfoMap = TrafficSourceFinder.getTrafficDetails(httpRequest);
		TrafficTracking trafficTracking = new TrafficTracking();

		String srcUrl = httpRequest.getHeader(HttpRequestAndSessionConstants.REFERER);
		if (StringUtils.isNotBlank(srcUrl) && srcUrl.length() > 190) {
			//logger.info("srcUrl=" + srcUrl);
			srcUrl = srcUrl.substring(0, 180);
		}
		trafficTracking.setSrcUrl(srcUrl);
		trafficTracking.setTrafficSrc(trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC));
		String trafficSrcDetails = trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC_DETAILS);
		if (StringUtils.isNotBlank(trafficSrcDetails) && trafficSrcDetails.length() > 190) {
			//logger.info("trafficSrcDetails=" + trafficSrcDetails);
			trafficSrcDetails = trafficSrcDetails.substring(0, 180);
		}
		trafficTracking.setTrafficSrcDetails(trafficSrcDetails);
		trafficTracking.setTrafficSrcPaid(Boolean.valueOf(trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC_PAID)));

		String pageUrl = httpRequest.getRequestURL().toString();
		trafficTracking.setLandingUrl(pageUrl);
		Category category = null;
		String categoryName = trafficInfoMap.get(TrafficSourceFinder.CATEGORY);
		if (StringUtils.isNotBlank(categoryName))
			category = getBaseDao().get(Category.class, categoryName);
		Product product = null;
		String productId = trafficInfoMap.get(TrafficSourceFinder.PRODUCT);
		if (StringUtils.isNotBlank(productId)) {
			product = getBaseDao().get(Product.class, productId);
			if (product != null) {
				trafficTracking.setProductId(product.getId());
				category = product.getPrimaryCategory();
			}
		}

		if (category != null) {
			trafficTracking.setPrimaryCategory(category.getName());
		}
		if (user != null) {
			trafficTracking.setUserId(user.getId());
		}
		trafficTracking.setIp(httpRequest.getRemoteAddr());
		trafficTracking.setSessionId(httpRequest.getSession().getId());
		trafficTracking.setCreateDt(new Date());
		trafficTracking.setUpdateDt(new Date());

		trafficTracking = (TrafficTracking) getBaseDao().save(trafficTracking);

		return trafficTracking;
	}

	@Transactional
	public void saveBrowsingHistory(Product product, HttpServletRequest httpServletRequest) {
	    UserBrowsingHistory userBrowsingHistory = new UserBrowsingHistory();
	    if (product != null) {
	        logger.error("1");
			TrafficTracking trafficTracking = (TrafficTracking) httpServletRequest.getSession().getAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING);
			 userBrowsingHistory = new UserBrowsingHistory();
			if (product.getPrimaryCategory() != null) {
				userBrowsingHistory.setPrimaryCategory(product.getPrimaryCategory().getName());
			}
			logger.error("2");
			userBrowsingHistory.setProductId(product.getId());
			userBrowsingHistory.setPageUrl(httpServletRequest.getRequestURL().toString());
			if (trafficTracking != null) {
				userBrowsingHistory.setTrafficTrackingId(trafficTracking.getId());
			}
			logger.error("3");
			userBrowsingHistory.setCreateDt(new Date());
			userBrowsingHistory.setUpdateDt(new Date());
			logger.error("4");
			try {
				getBaseDao().save(userBrowsingHistory);
			} catch (Throwable e) {
				logger.error("Exception while saving browsing history - " + e.getMessage() + "object was: " + userBrowsingHistory);
			}
			logger.error("5");
		}
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}
}
