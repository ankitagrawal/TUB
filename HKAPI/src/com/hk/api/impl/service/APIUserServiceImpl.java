package com.hk.api.impl.service;

import com.hk.api.constants.APIOperationStatus;
import com.hk.api.constants.EnumAPIErrorCode;
import com.hk.api.dto.HkAPIBaseDto;
import com.hk.api.dto.UserDetailDto;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.api.HkApiUser;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.exception.HealthkartSignupException;
import com.hk.manager.ReferrerProgramManager;
import com.hk.manager.UserManager;
import com.hk.pact.service.order.RewardPointService;
import com.hk.security.HkAuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.store.StoreService;
import com.hk.api.models.user.APIUser;
import com.hk.api.pact.service.APIUserService;


/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 1, 2012
 * Time: 1:25:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class APIUserServiceImpl implements APIUserService {

    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;
    @Autowired
    RewardPointService rewardPointService;
    @Autowired
    HkAuthService hkAuthService;
    @Autowired
    ReferrerProgramManager referrerProgramManager;
    @Autowired
    UserManager userManager;


    public User getHKUser(APIUser apiUser) {
        if (userExists(apiUser)) {
            User user=getUser(apiUser);
            if(StringUtils.isBlank(user.getName())){
                user.setName(apiUser.getName());
            }
            user=userService.save(user);
            return user;
        } else {
            return createNewHKUser(apiUser);
        }
    }

    public User getHKUser(User user){
        if (userExists(user)) {
            return getUser(user);
        } else {
            User hkUser = new User();
            hkUser.setName(user.getName());
            hkUser.setEmail(user.getEmail());
            hkUser.setPasswordChecksum(user.getPasswordChecksum());
            hkUser.setStore(user.getStore());
            hkUser.setLogin(user.getEmail()+"||"+user.getStore().getId());
            hkUser=userService.save(hkUser);
            return hkUser;
        }
    }

    public UserDetailDto getUserDetails(String userAccessToken){
        User user=hkAuthService.getUserFromAccessToken(userAccessToken);
        HkApiUser apiUser=hkAuthService.getApiUserFromUserAccessToken(userAccessToken);
        UserDetailDto userDetailDto=new UserDetailDto();
        userDetailDto.setEmail(user.getEmail());
        userDetailDto.setName(user.getName());
        return userDetailDto;
    }

    public HkAPIBaseDto awardRewardPoints(String userAccessToken, Double rewardPoints){
        User user=hkAuthService.getUserFromAccessToken(userAccessToken);
        HkApiUser hkApiUser=hkAuthService.getApiUserFromUserAccessToken(userAccessToken);
        //TODO allow this only for hkplus - but there is no way out here right now other than hardcoding the logic for "HealthkartPlus"
        // as name of HkApiUser - which I am not sure as of now
         rewardPointService.addRewardPoints(null,null,null,rewardPoints,hkApiUser.getName(), EnumRewardPointStatus.APPROVED, EnumRewardPointMode.HealthkartPlus.asRewardPointMode());

        HkAPIBaseDto hkAPIBaseDto=new HkAPIBaseDto();
        return hkAPIBaseDto;
    }

    public UserDetailDto getUserRewardPointDetails(String userAccessToken){
        User user=hkAuthService.getUserFromAccessToken(userAccessToken);
        UserDetailDto userDetailDto=this.getUserDetails(userAccessToken);
        userDetailDto.setRewardPoints(referrerProgramManager.getTotalRedeemablePoints(user));
        return userDetailDto;
    }

    public UserDetailDto createSSOUser(UserDetailDto userDetail){
        UserDetailDto userDetailDto=new UserDetailDto();
        userDetailDto.setEmail(userDetail.getEmail());
        userDetailDto.setName(userDetail.getName());
        User user=null;
        try{
            user=userManager.signup(userDetail.getEmail(),userDetail.getName(), userDetail.getPassword(), null);
        } catch (HealthkartSignupException e){
             userDetailDto.setErrorCode(EnumAPIErrorCode.UserAlreadyExists.getId());
             userDetailDto.setMessage(EnumAPIErrorCode.UserAlreadyExists.getMessage());
            userDetailDto.setStatus(APIOperationStatus.ERROR);
        }
        return userDetailDto;

    }

    private User createNewHKUser(APIUser apiUser) {
        User user = new User();
        user.setName(apiUser.getName());
        user.setEmail(apiUser.getEmail());
        user.setPasswordChecksum(apiUser.getPassword());
        user.setLogin(apiUser.getEmail()+"||"+apiUser.getStoreId());
        user.setStore(storeService.getStoreById(apiUser.getStoreId()));
        return userService.save(user);
    }

    private boolean userExists(APIUser apiUser) {
        if (apiUser != null) {
            if (userService.findByLoginAndStoreId(apiUser.getEmail(), apiUser.getStoreId()) != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean userExists(User user) {
        if (user != null) {
            if (userService.findByLoginAndStoreId(user.getEmail(), user.getStore().getId()) != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private User getUser(APIUser apiUser) {
        return userService.findByLoginAndStoreId(apiUser.getEmail(), apiUser.getStoreId());
    }

    private User getUser(User user) {
        return userService.findByLoginAndStoreId(user.getEmail(), user.getStore().getId());
    }
}
