package com.hk.api.edge.integration.response.user;


import com.hk.api.edge.integration.response.AbstractResponseFromHKR;

public class NotifyUserDetails extends AbstractResponseFromHKR{

  private String name;
  private String email;
  private boolean isSubscribedForNotify;

  private String contactNumber;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isSubscribedForNotify() {
    return isSubscribedForNotify;
  }

  public void setSubscribedForNotify(boolean subscribedForNotify) {
    isSubscribedForNotify = subscribedForNotify;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  @Override
  protected String[] getKeys() {
    return new String[]{
        "nm",
        "email",
        "cntNum",
        "isSubscribedForNotify",
        "msgs",
        "exception",
        "stId"
    };
  }

  @Override
  protected Object[] getValues() {
    return new Object[]{
      this.name,
      this.email,
      this.contactNumber,
      this.isSubscribedForNotify,
      this.getMsgs(),
      this.isException(),
      this.getStoreId()
    };
  }
}
