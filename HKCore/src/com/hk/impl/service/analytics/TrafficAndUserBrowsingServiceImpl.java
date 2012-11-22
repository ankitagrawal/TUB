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

	public TrafficTracking saveTrafficTracking(HttpServletRequest httpRequest, User user) {
		Map<String, String> trafficInfoMap = TrafficSourceFinder.getTrafficDetails(httpRequest);
		TrafficTracking trafficTracking = new TrafficTracking();

		trafficTracking.setSrcUrl(httpRequest.getHeader(HttpRequestAndSessionConstants.REFERER));
		trafficTracking.setTrafficSrc(trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC));
		trafficTracking.setTrafficSrcDetails(trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC_DETAILS));
		trafficTracking.setTrafficSrcPaid(Boolean.getBoolean(trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC_PAID)));

		String pageUrl = httpRequest.getRequestURL().toString();
		trafficTracking.setLandingUrl(pageUrl);
		Category category = null;
		String categoryName = trafficInfoMap.get(TrafficSourceFinder.CATEGORY);
		if (StringUtils.isNotBlank(categoryName))
			category = getBaseDao().get(Category.class, categoryName);
		Product product = null;
		String productId = trafficInfoMap.get(TrafficSourceFinder.PRODUCT);
		if (StringUtils.isNotBlank(productId)){
			product = getBaseDao().get(Product.class, productId);
			if(product != null)
				category = product.getPrimaryCategory();
		}
		trafficTracking.setProduct(product);
		trafficTracking.setCategory(category);
		trafficTracking.setUser(user);
		trafficTracking.setIp(httpRequest.getRemoteAddr());
		trafficTracking.setSessionId(httpRequest.getSession().getId());
		trafficTracking.setCreateDt(new Date());
		trafficTracking.setUpdateDt(new Date());

		trafficTracking = (TrafficTracking) getBaseDao().save(trafficTracking);
		
		return trafficTracking;
	}

	public void saveBrowsingHistory(Product product, HttpServletRequest httpServletRequest) {
		TrafficTracking trafficTracking = (TrafficTracking) httpServletRequest.getSession().getAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING);
		UserBrowsingHistory userBrowsingHistory = new UserBrowsingHistory();
		userBrowsingHistory.setCategory(product.getPrimaryCategory());
		userBrowsingHistory.setProduct(product);
		userBrowsingHistory.setPageUrl(httpServletRequest.getRequestURL().toString());
		userBrowsingHistory.setTrafficTracking(trafficTracking);
		userBrowsingHistory.setCreateDt(new Date());
		userBrowsingHistory.setUpdateDt(new Date());

		try {
			getBaseDao().save(userBrowsingHistory);
		} catch (Exception e) {
			logger.error("Exception while saving browsing history:" + e.getMessage());
		}
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}
}
