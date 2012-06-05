package com.hk.pact.service.clm;

import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 29, 2012
 * Time: 3:56:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KarmaProfileService {

     public KarmaProfile save(KarmaProfile karmaProfile);

     public KarmaProfile findByUser(User user);

     public KarmaProfile updateKarmaAfterOrder(Order order);

     public boolean isPriorityUser(User user);

}
