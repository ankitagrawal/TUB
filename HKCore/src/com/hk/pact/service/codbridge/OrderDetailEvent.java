package com.hk.pact.service.codbridge;


import com.akube.framework.gson.JsonUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 2/19/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderDetailEvent {


    public static void main(String arg[]) {
        EffortBpoOrderJson ef = new EffortBpoOrderJson();
        EffortBpoOrderJson.User user = ef.getUser();
        List<EffortBpoOrderJson.ProductVariant>  productVariantList = new ArrayList<EffortBpoOrderJson.ProductVariant>();

        EffortBpoOrderJson.ProductVariant productVariant1 = ef.new ProductVariant();

        EffortBpoOrderJson.ProductVariant productVariant2 = ef.new ProductVariant();

         ef.setOrderID("ORDER123");
        user.setUserName("seema");
        user.setPhoneNumber("1234567890");

        productVariant1.setPrice("676");
        productVariant1.setProductName("protein");
        productVariant1.setProductVariantId("NUT301-01");

        productVariant2.setPrice("898");
        productVariant2.setProductName("yuyu");
        productVariant2.setProductVariantId("NUT301-054");

        productVariantList.add(productVariant1);
        productVariantList.add(productVariant2);
        ef.setProductList(productVariantList);

        Gson gson = JsonUtils.getGsonDefault();
       String jsonString =  gson.toJson(ef);

         System.out.println(jsonString);




    }



}
