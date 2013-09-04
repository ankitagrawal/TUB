package com.hk.api.edge.internal.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.api.edge.constants.DtoJsonConstants;
import com.hk.api.edge.ext.response.AbstractApiBaseResponse;
import com.hk.api.edge.internal.response.menu.MenuResponse;

public class MenuNodeResponseApiWrapper extends AbstractApiBaseResponse {

    @JsonProperty(DtoJsonConstants.RESULTS)
    private MenuResponse menuResponse;

    public MenuResponse getMenuResponse() {
      return menuResponse;
    }

    public void setMenuResponse(MenuResponse menuResponse) {
      this.menuResponse = menuResponse;
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