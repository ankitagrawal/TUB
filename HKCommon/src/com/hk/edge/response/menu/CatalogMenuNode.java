package com.hk.edge.response.menu;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.constants.DtoJsonConstants;
import com.hk.edge.helper.HKLinkManager;

/**
 * @author vaibhav.adlakha
 */
public class CatalogMenuNode {

  @JsonProperty(DtoJsonConstants.NAV_KEY)
  private String navKey;
  @JsonProperty(DtoJsonConstants.URL_FRAGMENT)
  private String urlFragment;
  @JsonProperty(DtoJsonConstants.NAME)
  private String name;
  @JsonProperty(DtoJsonConstants.DISPLAY_ORDER)
  private int displayOrder;
  @JsonProperty(DtoJsonConstants.HIGHLIGHTER)
  private String higlighter;
  @JsonProperty(DtoJsonConstants.CHILDREN)
  private List<CatalogMenuNode> childNodes = new ArrayList<CatalogMenuNode>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(int displayOrder) {
    this.displayOrder = displayOrder;
  }

  public String getHiglighter() {
    return higlighter;
  }

  public void setHiglighter(String higlighter) {
    this.higlighter = higlighter;
  }

  public List<CatalogMenuNode> getChildNodes() {
    return childNodes;
  }

  public void setChildNodes(List<CatalogMenuNode> childNodes) {
    this.childNodes = childNodes;
  }

  public String getNavKey() {
    return navKey;
  }

  public void setNavKey(String navKey) {
    this.navKey = navKey;
  }

  public String getUrlFragment() {
    return urlFragment;
  }

  public void setUrlFragment(String urlFragment) {
    this.urlFragment = urlFragment;
  }

  public String getUrl() {
    return HKLinkManager.getMenuNodeUrl(this);
  }
}
