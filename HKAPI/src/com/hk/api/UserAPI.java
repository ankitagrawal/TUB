package com.hk.api;

import com.hk.api.dto.user.UserDTO;
import com.hk.constants.discount.EnumRewardPointMode;

/**
 * @author vaibhav.adlakha
 */
public interface UserAPI extends HkAPI {

    /**
     * Get details for the user identified by login.
     * 
     * @param email
     * @return
     */
    public UserDTO getUserDetails(String login);

    /**
     * get eligible reward points for user, these are the reward points which user could redeem.
     * 
     * @param login
     * @return
     */
    public Double getEligibleRewardPointsForUser(String login);

    /**
     * Add Reward points for user identified by login
     * 
     * @param login
     * @param rewardPoints
     * @param rewardPointMode
     * @return
     */
    public boolean addRewardPointsForUser(String login, double rewardPoints, String comment, EnumRewardPointMode rewardPointMode);

}
