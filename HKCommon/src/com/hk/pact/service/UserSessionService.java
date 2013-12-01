package com.hk.pact.service;

/**
 * @author vaibhav.adlakha
 */
public interface UserSessionService {

    public void onLoginUser();

    public void onLogoutUser();

    public boolean isUserAuthenticated(Long userId);

}
