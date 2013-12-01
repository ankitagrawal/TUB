package com.hk.pact.service.clm;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.clm.CategoryKarmaProfile;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 29, 2012
 * Time: 3:56:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KarmaProfileService {

     public KarmaProfile save(KarmaProfile karmaProfile);

     public  CategoryKarmaProfile save(CategoryKarmaProfile categoryKarmaProfile);

     public KarmaProfile findByUser(User user);

     public CategoryKarmaProfile findByUserAndCategory(User user, Category category);

     public KarmaProfile updateKarmaAfterOrder(Order order);

     public KarmaProfile updateKarmaAfterOrderCancellation(Order order);

}
