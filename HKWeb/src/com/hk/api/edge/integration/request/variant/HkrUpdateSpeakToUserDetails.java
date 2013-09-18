package com.hk.api.edge.integration.request.variant;


import com.hk.edge.constants.DtoJsonConstants;
import org.codehaus.jackson.annotate.JsonProperty;

public class HkrUpdateSpeakToUserDetails {

  @JsonProperty(DtoJsonConstants.NAME)
  private String name;
  @JsonProperty(DtoJsonConstants.EMAIL)
  private String email;
  @JsonProperty(DtoJsonConstants.CONTACT_NUMBER)
  private String contactNumber;
  @JsonProperty(DtoJsonConstants.SUBSCRIBE)
  private boolean subscribe;
  @JsonProperty(DtoJsonConstants.CATEGORY_NAME)
  private String categoryName;

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

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public boolean isSubscribe() {
    return subscribe;
  }

  public void setSubscribe(boolean subscribe) {
    this.subscribe = subscribe;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
}
