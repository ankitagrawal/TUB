package com.hk.api;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface UserAPI extends HkAPI{
    
    
    /**
     * Get details for the user idnetified by login.
     * @param email
     * @return
     */
    public String getUserDetails(String login);
    
    /**
     * get eligible reward points for user, these are the reward points which user could redeem.
     * @param login
     * @return
     */
    public String getEligibleRewardPointsForUser(String login);

}
