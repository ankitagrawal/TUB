package com.hk.api.edge.ext.pact.service.user;

import com.hk.api.edge.ext.response.user.UserApiResponse;

public interface HKCatalogUserService {

  public boolean isTempUser(Long userId);

  public UserApiResponse getUserResponseById(Long userId);

  public UserApiResponse getUserResponseByLogin(String loginName);
}
