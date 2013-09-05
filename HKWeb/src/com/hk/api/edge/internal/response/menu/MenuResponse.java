package com.hk.api.edge.internal.response.menu;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.api.edge.constants.DtoJsonConstants;
import com.hk.api.edge.ext.response.AbstractApiBaseResponse;


public class MenuResponse extends AbstractApiBaseResponse {

    @JsonProperty(DtoJsonConstants.TOP_LEVEL_MENU_NODES)
    private List<CatalogMenuNode> menuNodes;

    public List<CatalogMenuNode> getMenuNodes() {
      return menuNodes;
    }

    public void setMenuNodes(List<CatalogMenuNode> menuNodes) {
      this.menuNodes = menuNodes;
    }
  }
