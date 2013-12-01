package com.akube.framework.shiro.manager;

import java.util.Set;

import com.akube.framework.shiro.Principal;

/**
 * Author: Kani
 * Date: Sep 6, 2008
 */
public interface UserSecurityManager {

  public String getPasswordForUser(String userName);

  public Set<String> getRoleNamesForUser(Principal principal);

  public Set<String> getPermissions(Principal principal, Set<String> roles);

  Principal getPrincipal(String loginName);
}
