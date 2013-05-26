package com.hk.pact.service.analytics;

import com.hk.domain.user.User;
import com.hk.domain.analytics.TrafficTracking;
import com.hk.domain.catalog.product.Product;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 22 Nov, 2012
 * Time: 3:18:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TrafficAndUserBrowsingService {

	public TrafficTracking saveTrafficTracking(HttpServletRequest httpRequest, User user);

	public void saveBrowsingHistory(Product product, HttpServletRequest httpServletRequest, Long productReferrerId, String productPosition);

}
