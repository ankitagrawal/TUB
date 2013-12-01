package com.akube.framework.service;

import java.util.Map;

/**
 * Author: Kani
 * Date: Dec 30, 2008
 */
public interface PaymentGatewayWrapper {
  public String getGatewayUrl();
//  public boolean isPaypal();
    
  public void setGatewayUrl(String url);
  public Map<String, Object> getParameters();

}
