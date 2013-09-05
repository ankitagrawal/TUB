package com.hk.api.edge.internal.response.menu;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.api.edge.ext.response.AbstractApiBaseResponse;
import com.hk.edge.constants.DtoJsonConstants;
import com.hk.edge.response.menu.CatalogMenuNode;


public class MenuResponse extends AbstractApiBaseResponse {

    @JsonProperty(DtoJsonConstants.TOP_LEVEL_MENU_NODES)
    private List<CatalogMenuNode> menuNodes;

    public List<CatalogMenuNode> getMenuNodes() {
      return menuNodes;
    }

    public void setMenuNodes(List<CatalogMenuNode> menuNodes) {
      this.menuNodes = menuNodes;
    }


    @Override
    protected String[] getKeys() {
        return new String[0];
    }

    @Override
    protected Object[] getValues() {
        return new Object[0];
    }
}
