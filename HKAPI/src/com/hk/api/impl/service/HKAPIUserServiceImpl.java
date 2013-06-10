package com.hk.api.impl.service;

import com.hk.api.constants.EnumHKAPIErrorCode;
import com.hk.api.constants.HKAPIOperationStatus;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.api.HkApiUser;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.exception.HealthkartSignupException;
import com.hk.manager.UserManager;
import com.hk.manager.LinkManager;
import com.hk.pact.service.order.RewardPointService;
import com.hk.security.HkAuthService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.user.User;
import com.hk.domain.TempToken;
import com.hk.pact.service.UserService;
import com.hk.pact.service.store.StoreService;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.api.dto.user.HKAPIUserDTO;
import com.hk.api.pact.service.HKAPIUserService;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 1, 2012
 * Time: 1:25:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class HKAPIUserServiceImpl implements HKAPIUserService {

    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;
    @Autowired
    HkAuthService hkAuthService;
    @Autowired
    RewardPointService rewardPointService;
    @Autowired
    UserManager userManager;
    @Autowired
    private TempTokenDao tempTokenDao;
    @Autowired
    private LinkManager linkManager;


    public User getHKUser(HKAPIUserDTO HKAPIUserDTO, Long storeId) {
        if (userExists(HKAPIUserDTO, storeId)) {
            User user=getUser(HKAPIUserDTO, storeId);
            if(StringUtils.isBlank(user.getName())){
                user.setName(HKAPIUserDTO.getName());
            }
            user=userService.save(user);
            return user;
        } else {
            return createNewHKUser(HKAPIUserDTO, storeId);
        }
    }

    public HKAPIBaseDTO authenticate(String loginEmail, String password){
        try{
            UserLoginDto userLoginDto= userManager.login(loginEmail, password, false);
            HKAPIUserDTO userDTO=new HKAPIUserDTO();
            userDTO.setEmail(userLoginDto.getLoggedUser().getEmail());
            userDTO.setName(userLoginDto.getLoggedUser().getName());
            HKAPIBaseDTO responseDTO=new HKAPIBaseDTO();
            responseDTO.setData(userDTO);
            return responseDTO;
        }catch (HealthkartLoginException e){
            return new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidUserCredentials);
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

    public HKAPIBaseDTO getUserDetails(String userAccessToken){
        HKAPIBaseDTO baseDTO=new HKAPIBaseDTO();
        User user=hkAuthService.getUserFromAccessToken(userAccessToken);
        HkApiUser apiUser=hkAuthService.getApiUserFromUserAccessToken(userAccessToken);
        HKAPIUserDTO userDto=new HKAPIUserDTO();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        baseDTO.setData(userDto);
        return baseDTO;
    }

    public HKAPIBaseDTO awardRewardPoints(String userAccessToken, Double rewardPoints){
        User user=hkAuthService.getUserFromAccessToken(userAccessToken);
        HkApiUser hkApiUser=hkAuthService.getApiUserFromUserAccessToken(userAccessToken);
        //TODO allow this only for hkplus - hardcoded here - probably add an is allowed to add reward points at  the db level- which inturn needs a different reward point mode
        // probably add a column in the hk_api_user table for reward point allocation
        if(hkApiUser.getApiKey().equals("healthkartplus")){

            RewardPoint rewardPoint=rewardPointService.addRewardPoints(user,null,null,rewardPoints,hkApiUser.getName(), EnumRewardPointStatus.APPROVED, EnumRewardPointMode.HKPLUS_POINTS.asRewardPointMode());
            rewardPointService.createRewardPointTxnForApprovedRewardPoints(rewardPoint, new DateTime().plusMonths(3).toDate());

            return new HKAPIBaseDTO();
        }
        else{
            return new HKAPIBaseDTO(EnumHKAPIErrorCode.Unauthorized);
        }
    }

    public HKAPIBaseDTO getUserRewardPointDetails(String userAccessToken){
        HKAPIBaseDTO hkapiBaseDTO=new HKAPIBaseDTO();
        User user=hkAuthService.getUserFromAccessToken(userAccessToken);
        double rewardPoints=rewardPointService.getEligibleRewardPointsForUser(user.getLogin());
        hkapiBaseDTO.setData(rewardPoints);
        return hkapiBaseDTO;
    }

    public HKAPIBaseDTO createSSOUser(HKAPIUserDTO userDetail){
        HKAPIBaseDTO baseDTO =new HKAPIBaseDTO();
        HKAPIUserDTO userDto=new HKAPIUserDTO();
        userDto.setEmail(userDetail.getEmail());
        userDto.setName(userDetail.getName());
        baseDTO.setData(userDto);
        User user=null;
        try{
            user=userManager.signup(userDetail.getEmail(),userDetail.getName(), userDetail.getPassword(), null);
        } catch (HealthkartSignupException e){
            baseDTO.setErrorCode(EnumHKAPIErrorCode.UserAlreadyExists.getId());
            baseDTO.setMessage(EnumHKAPIErrorCode.UserAlreadyExists.getMessage());
            baseDTO.setStatus(HKAPIOperationStatus.ERROR);
        }
        return baseDTO;

    }

    private User createNewHKUser(HKAPIUserDTO HKAPIUserDTO, Long storeId) {
        User user = new User();
        user.setName(HKAPIUserDTO.getName());
        user.setEmail(HKAPIUserDTO.getEmail());
        user.setPasswordChecksum(HKAPIUserDTO.getPassword());
        user.setLogin(HKAPIUserDTO.getEmail()+"||"+ storeId);
        user.setStore(storeService.getStoreById(storeId));
        return userService.save(user);
    }

   @Override
    public String getResetPasswordLink(User user) {
      TempToken tempToken = tempTokenDao.createNew(user, 1); // 1Day
      return linkManager.getSSOResetPasswordLink(tempToken);
    }


    private boolean userExists(HKAPIUserDTO HKAPIUserDTO, Long storeId) {
        if (HKAPIUserDTO != null) {
            if (userService.findByLoginAndStoreId(HKAPIUserDTO.getEmail(), storeId) != null) {
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

    private User getUser(HKAPIUserDTO HKAPIUserDTO, Long storeId) {
        return userService.findByLoginAndStoreId(HKAPIUserDTO.getEmail(), storeId);
    }

    private User getUser(User user) {
        return userService.findByLoginAndStoreId(user.getEmail(), user.getStore().getId());
    }
}
