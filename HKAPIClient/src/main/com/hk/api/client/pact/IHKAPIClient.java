package com.hk.api.client.pact;

import com.hk.api.client.dto.HKAPIBaseDto;
import com.hk.api.client.dto.HKUserDetailDto;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 1:52 PM
 *
 * we need to segregate functions into different classes once we scale up our API
 */
public interface IHKAPIClient {

    public HKUserDetailDto getUserDetails(String userEmail);

    public HKUserDetailDto createUserInHK(HKUserDetailDto userDetails);

    public Double getRewardPoints(String userEmail);

    public HKAPIBaseDto awardRewardPoints(String userEmail, Double rewardPoints);

}
