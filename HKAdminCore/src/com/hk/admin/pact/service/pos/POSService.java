package com.hk.admin.pact.service.pos;

import com.hk.domain.order.Order;
import com.hk.domain.user.User;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/22/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface POSService {

	public Order createOrderForStore(User user);

	public User createUserForStore(String email, String name, String password, String roleName);

}
