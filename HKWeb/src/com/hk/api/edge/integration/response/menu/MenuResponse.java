package com.hk.api.edge.integration.response.menu;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.constants.DtoJsonConstants;
import com.hk.edge.response.AbstractBaseResponseFromEdge;
import com.hk.edge.response.menu.CatalogMenuNode;


@SuppressWarnings("serial")
public class MenuResponse extends AbstractBaseResponseFromEdge {

    @JsonProperty(DtoJsonConstants.TOP_LEVEL_MENU_NODES)
    private List<CatalogMenuNode> menuNodes;

    public List<CatalogMenuNode> getMenuNodes() {
      return menuNodes;
    }

    public void setMenuNodes(List<CatalogMenuNode> menuNodes) {
      this.menuNodes = menuNodes;
    }


   
}
