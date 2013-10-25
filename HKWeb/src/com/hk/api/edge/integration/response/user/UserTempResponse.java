package com.hk.api.edge.integration.response.user;


import com.hk.api.edge.integration.response.AbstractResponseFromHKR;

public class UserTempResponse extends AbstractResponseFromHKR {

  private boolean isTempUser;

  public boolean isTempUser() {
    return isTempUser;
  }

  public void setTempUser(boolean tempUser) {
    isTempUser = tempUser;
  }

  @Override
  protected String[] getKeys() {
    return new String[]{"isTempUser","msgs","exception"};
  }

  @Override
  protected Object[] getValues() {
    return new Object[]{this.isTempUser,this.msgs, this.exception};
  }
}
