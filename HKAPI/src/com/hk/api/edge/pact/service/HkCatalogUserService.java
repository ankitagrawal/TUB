package com.hk.api.edge.pact.service;

import com.hk.api.edge.response.user.UserApiResponse;

public interface HKCatalogUserService {

  /*public UserApiBaseResponse getLoggedInUser();*/

  public boolean isTempUser(Long userId);

  public UserApiResponse getUserResponseById(Long userId);

  public UserApiResponse getUserResponseByLogin(String loginName);
}
