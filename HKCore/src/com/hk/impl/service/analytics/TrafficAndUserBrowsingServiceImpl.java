package com.hk.impl.service.analytics;

import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.domain.analytics.TrafficTracking;
import com.hk.domain.analytics.UserBrowsingHistory;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.analytics.UserBrowsingHistoryDao;
import com.hk.pact.service.analytics.TrafficAndUserBrowsingService;
import com.hk.util.TrafficSourceFinder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

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
	@Autowired
	UserBrowsingHistoryDao userBrowsingHistoryDao;

	@Transactional
	public TrafficTracking saveTrafficTracking(HttpServletRequest httpRequest, User user) {
		String pageUrl = httpRequest.getRequestURL().toString();
		if (StringUtils.isNotBlank(pageUrl) && StringUtils.equals(pageUrl, "http:///")) {
			return null;
		}

		Map<String, String> trafficInfoMap = TrafficSourceFinder.getTrafficDetails(httpRequest);
		TrafficTracking trafficTracking = new TrafficTracking();

		String srcUrl = httpRequest.getHeader(HttpRequestAndSessionConstants.REFERER);
		if (StringUtils.isNotBlank(srcUrl) && srcUrl.length() > 190) {
			srcUrl = srcUrl.substring(0, 180);
		}
		trafficTracking.setSrcUrl(srcUrl);
		String userAgent = httpRequest.getHeader(HttpRequestAndSessionConstants.USER_AGENT);
		if (StringUtils.isNotBlank(userAgent) && userAgent.length() > 190) {
			userAgent = userAgent.substring(0, 180);
		}
		trafficTracking.setUserAgent(userAgent);
		trafficTracking.setTrafficSrc(trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC));
		String trafficSrcDetails = trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC_DETAILS);
		if (StringUtils.isNotBlank(trafficSrcDetails) && trafficSrcDetails.length() > 190) {
			trafficSrcDetails = trafficSrcDetails.substring(0, 180);
		}
		trafficTracking.setTrafficSrcDetails(trafficSrcDetails);
		trafficTracking.setTrafficSrcPaid(Boolean.valueOf(trafficInfoMap.get(TrafficSourceFinder.TRAFFIC_SRC_PAID)));

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
			trafficTracking.setReturningUser(1L);
		}
		trafficTracking.setIp(getClientIPAddress(httpRequest));
		trafficTracking.setSessionId(httpRequest.getSession().getId());
		trafficTracking.setCreateDt(new Date());
		trafficTracking.setUpdateDt(new Date());

		trafficTracking = (TrafficTracking) getBaseDao().save(trafficTracking);


		return trafficTracking;
	}

	public void saveBrowsingHistory(Product product, HttpServletRequest httpServletRequest, Long productReferrerId, String productPosition) {
		if (product != null) {
			TrafficTracking trafficTracking = (TrafficTracking) httpServletRequest.getSession().getAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING);
			if (trafficTracking != null) {
				String pageUrl = httpServletRequest.getRequestURL().toString();
				UserBrowsingHistory userBrowsingHistory = getUserBrowsingHistoryDao().getUserBrowsingHistory(trafficTracking.getId(), pageUrl);
				if (userBrowsingHistory == null) {
					userBrowsingHistory = new UserBrowsingHistory();
					if (product.getPrimaryCategory() != null) {
						userBrowsingHistory.setPrimaryCategory(product.getPrimaryCategory().getName());
					}
					userBrowsingHistory.setProductId(product.getId());
					userBrowsingHistory.setPageUrl(pageUrl);
					userBrowsingHistory.setTrafficTrackingId(trafficTracking.getId());
					userBrowsingHistory.setProductReferrerId(productReferrerId);
					userBrowsingHistory.setProductPosition(productPosition);
					userBrowsingHistory.setCreateDt(new Date());
					userBrowsingHistory.setUpdateDt(new Date());
					try {
						getBaseDao().save(userBrowsingHistory);
					} catch (Exception e) {
						logger.error("Exception while saving browsing history - " + e.getMessage() + " stringified object -> " + userBrowsingHistory);
					}
				} else {
					//Do nothing					
					//logger.warn("Entry for the pageUrl and trackingId already exists - so skipping entry");
				}
			}
		}
	}

	public String getClientIPAddress(HttpServletRequest request) {
		// Apache redirection
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		if (ipAddress.contains(",")) {
			ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		}
		return ipAddress;
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public UserBrowsingHistoryDao getUserBrowsingHistoryDao() {
		return userBrowsingHistoryDao;
	}
}
