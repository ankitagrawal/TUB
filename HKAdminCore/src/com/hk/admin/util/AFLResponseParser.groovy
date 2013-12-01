package com.hk.admin.util;

import java.util.HashMap;
import org.springframework.stereotype.Component


public class AFLResponseParser{

  public static Map<String,String> parseResponse(String response, String trackingId){

    def SHIPMENTTRACK,awb,delivery_date,num_check_points,delivery_checkpoint;
    def digitsInGatewayId = 5;
    String current_status,order_gateway_id;

    SHIPMENTTRACK = new XmlParser().parseText(response);
    Map<String,String> responseAFL = new HashMap();
    awb = SHIPMENTTRACK.SHIPMENTREPORT.AWB.text();
    if( awb.equals(trackingId)){
      responseAFL.put("awb",awb);
      current_status=(SHIPMENTTRACK.SHIPMENTREPORT.CURRENTSTATUS.text());
      order_gateway_id = SHIPMENTTRACK.SHIPMENTREPORT.SHIPMENTREFERENCENUMBER.text();

      //in case order gateway id in xml does not have a -  in order gateway id
      if(order_gateway_id.indexOf('-') == -1){
        if (order_gateway_id.length() > digitsInGatewayId-1) {
          order_gateway_id = (order_gateway_id.substring(0, order_gateway_id.length() - digitsInGatewayId) +
              "-" + order_gateway_id.substring(order_gateway_id.length() - digitsInGatewayId, order_gateway_id.length()));
        }
      }

      if (null != current_status && null != order_gateway_id && current_status.equalsIgnoreCase("DELIVERED")){
        responseAFL.put("current_status",current_status);
        responseAFL.put("order_gateway_id", order_gateway_id)
        num_check_points = SHIPMENTTRACK.SHIPMENTREPORT.CHECKPOINTDETAILS.CHECKPOINTS.size();
        delivery_checkpoint = SHIPMENTTRACK.SHIPMENTREPORT.CHECKPOINTDETAILS.CHECKPOINTS[num_check_points - 1];
        delivery_date = delivery_checkpoint.CHECKDATE.text();
      }
      if(null != delivery_date){
        responseAFL.put("delivery_date",delivery_date);
      }
    }
    return responseAFL;
  }

}