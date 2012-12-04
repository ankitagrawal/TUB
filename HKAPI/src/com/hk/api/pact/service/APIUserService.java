package com.hk.api.pact.service;

import com.hk.api.dto.HkAPIBaseDto;
import com.hk.api.dto.UserDetailDto;
import com.hk.domain.user.User;
import com.hk.api.models.user.APIUser;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 8, 2012
 * Time: 11:19:56 AM
 */
public interface APIUserService {

    public User getHKUser(APIUser apiUser);

    public User getHKUser(User user);

    public UserDetailDto getUserDetails(String userAccessToken);

    public HkAPIBaseDto awardRewardPoints(String userAccessToken, Double rewardPoints);

    public UserDetailDto getUserRewardPointDetails(String userAccessToken);

    public UserDetailDto createSSOUser(UserDetailDto userDetailDto);
}
