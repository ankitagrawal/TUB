package com.hk.api.pact.service;

import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.domain.user.User;
import com.hk.api.dto.user.HKAPIUserDTO;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 8, 2012
 * Time: 11:19:56 AM
 */
public interface HKAPIUserService {

    public User getHKUser(HKAPIUserDTO HKAPIUserDTO, Long storeId);

    public User getHKUser(User user);

    public HKAPIBaseDTO authenticate(String loginEmail, String password);

    public HKAPIBaseDTO getUserDetails(String userAccessToken);

    public HKAPIBaseDTO awardRewardPoints(String userAccessToken, Double rewardPoints);

    public HKAPIBaseDTO getUserRewardPointDetails(String userAccessToken);

    public HKAPIBaseDTO createSSOUser(HKAPIUserDTO userDetailDto);

    public String getResetPasswordLink(User user);

}
