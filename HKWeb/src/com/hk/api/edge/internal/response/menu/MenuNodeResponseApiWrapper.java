package com.hk.api.edge.internal.response.menu;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.api.edge.constants.DtoJsonConstants;
import com.hk.api.edge.ext.response.AbstractApiBaseResponse;


public class MenuNodeResponseApiWrapper extends AbstractApiBaseResponse {

    @JsonProperty(DtoJsonConstants.RESULTS)
    private MenuResponse menuResponse;

    public MenuResponse getMenuResponse() {
      return menuResponse;
    }

    public void setMenuResponse(MenuResponse menuResponse) {
      this.menuResponse = menuResponse;
    }
  }

