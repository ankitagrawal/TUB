package com.hk.api.edge.internal.response;

public class AbstractBaseResponseApiWrapper {
    private int statusCode;


    public int getStatusCode() {
      return statusCode;
    }

    public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
    }
}
