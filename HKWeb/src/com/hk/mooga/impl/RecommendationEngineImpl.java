package com.hk.mooga.impl;

import com.hk.mooga.MoogaWebServicesLocator;
import com.hk.mooga.MoogaWebServicesSoap12Stub;
import com.hk.mooga.MoogaWebServicesSoap_PortType;
import com.hk.mooga.pact.RecommendationEngine;
import org.springframework.stereotype.Service;

import java.rmi.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Marut
 * Date: 6/23/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RecommendationEngineImpl implements RecommendationEngine{
    final int key = 11213141;   //provided by MOOGA

    public void dummy(){
        String s = "";
    }
    public void pushAddToCart(long userId, long dealId){

        try{
            int i;
           String params = String.format("%s, %s, %s, %d, %d, %s, %s, %s, %s, %s, %s, %s, %s, %s",
                                            "1","0","Consumer/Add to cart",userId, dealId, "IT",
                                    "0","0","0","0","0","0","0","0");

            /*StringBuilder sb = new StringBuilder();
            ArrayList<String> params = new ArrayList<String>(14);
            params.add("1");
            params.add("0");
            params.add("Consumer/Add to cart");
            params.add(Long.toString(userId));
            params.add(Long.toString(dealId));
            params.add("IT");
            
            for (String param : params){
                 sb.append(param);
                 sb.append(",");
            }*/
            MoogaWebServicesLocator servicesLocator = new MoogaWebServicesLocator();


            MoogaWebServicesSoap_PortType moogaStub = servicesLocator.getMoogaWebServicesSoap12();
            String result = moogaStub.trans_NotifyTrans(key,"OFBiz.NotifyTransaction",params);
            result = "";
        }catch(Exception ex){
            String err = ex.getMessage();
            err = "";
        }
    }
}
