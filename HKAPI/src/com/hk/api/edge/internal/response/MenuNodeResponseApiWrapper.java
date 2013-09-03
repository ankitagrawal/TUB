package com.hk.api.edge.internal.response;

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

