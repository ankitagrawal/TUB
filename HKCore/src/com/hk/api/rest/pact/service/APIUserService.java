package com.hk.api.rest.pact.service;

import com.hk.api.rest.models.user.APIUser;
import com.hk.domain.user.User;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 8, 2012
 * Time: 11:19:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface APIUserService {
   public User getHKUser(APIUser apiUser);
}
