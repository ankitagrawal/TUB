package com.hk.api.base.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.api.UserAPI;
import com.hk.api.dto.user.UserDTO;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.util.json.JSONResponseBuilder;

/**
 * @author vaibhav.adlakha
 */
public class AbstractUserAPIImpl implements UserAPI {

    @Autowired
    private UserService userService;

    @Override
    public String getUserDetails(String login) {
        User user = getUserService().findByLogin(login);
        UserDTO userDTO = new UserDTO(user);

        return new JSONResponseBuilder().addField("userDetails", userDTO).build();
    }

    public UserService getUserService() {
        return userService;
    }

}
