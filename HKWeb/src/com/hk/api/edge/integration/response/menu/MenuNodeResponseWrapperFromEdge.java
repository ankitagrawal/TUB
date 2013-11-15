package com.hk.api.edge.integration.response.menu;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.constants.DtoJsonConstants;
import com.hk.edge.response.AbstractBaseResponseWrapperFromEdge;


public class MenuNodeResponseWrapperFromEdge extends AbstractBaseResponseWrapperFromEdge{

    @JsonProperty(DtoJsonConstants.RESULTS)
    private MenuResponse menuResponse;

    public MenuResponse getMenuResponse() {
      return menuResponse;
    }

    public void setMenuResponse(MenuResponse menuResponse) {
      this.menuResponse = menuResponse;
    }


   
}

