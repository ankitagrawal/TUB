package com.hk.api.base.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hk.api.UserAPI;
import com.hk.api.dto.user.UserDTO;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.user.User;
import com.hk.exception.InvalidRewardPointsException;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.RewardPointService;

/**
 * @author vaibhav.adlakha
 */
public abstract class AbstractUserAPIImpl implements UserAPI {

    private static Logger      logger = LoggerFactory.getLogger(AbstractUserAPIImpl.class);

    @Autowired
    private UserService        userService;
    @Autowired
    private RewardPointService rewardPointService;

    @Override
    public UserDTO getUserDetails(String login) {
        User user = getUserService().findByLogin(login);
        // User user = UserCache.getInstance().getUserByLogin(login).getUser();
        return new UserDTO(user);
    }

    @Override
    public Double getEligibleRewardPointsForUser(String login) {
        return getRewardPointService().getEligibleRewardPointsForUser(login);
    }

    @Override
    public boolean addRewardPointsForUser(String login, double rewardPoints, String comment, EnumRewardPointMode enumRewardPointMode) {
        boolean rewardPointsAdded = true;
        RewardPoint rewardPoint = null;
        User user = getUserService().findByLogin(login);
        // User user = UserCache.getInstance().getUserByLogin(login).getUser();

        if (user != null && enumRewardPointMode != null) {

            try {
                rewardPoint = getRewardPointService().addRewardPoints(user, null, null, rewardPoints, comment, EnumRewardPointStatus.APPROVED,
                        enumRewardPointMode.asRewardPointMode());
            } catch (InvalidRewardPointsException e) {
                logger.error("Reward point cannot be added", e);
                rewardPointsAdded = false;
            }
        } else {
            rewardPointsAdded = false;
        }
        if (rewardPointsAdded && rewardPoint != null) {
            getRewardPointService().approveRewardPoints(Arrays.asList(rewardPoint), null);
        }

        return rewardPointsAdded;
    }

    public UserService getUserService() {
        return userService;
    }

    public RewardPointService getRewardPointService() {
        return rewardPointService;
    }

}
