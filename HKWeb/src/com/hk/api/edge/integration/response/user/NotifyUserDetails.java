package com.hk.api.edge.integration.response.user;


import com.hk.api.edge.integration.response.AbstractResponseFromHKR;
import com.hk.edge.constants.DtoJsonConstants;

public class NotifyUserDetails extends AbstractResponseFromHKR{

  private String name;
  private String email;
  private boolean isSubscribedForNotify;

  private String contactNumber;
  private String accountLink;

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

  public String getAccountLink() {
    return accountLink;
  }

  public void setAccountLink(String accountLink) {
    this.accountLink = accountLink;
  }

  @Override
  protected String[] getKeys() {
    return new String[]{
        DtoJsonConstants.NAME,
        DtoJsonConstants.EMAIL,
        DtoJsonConstants.CONTACT_NUMBER,
        "isSubscribedForNotify",
        DtoJsonConstants.MESSAGES,
        DtoJsonConstants.EXCEPTION,
        DtoJsonConstants.STORE_ID,
        "accntLnk"
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
      this.getStoreId(),
      this.accountLink
    };
  }
}
