package com.hk.impl.service.mooga;

import com.hk.pact.service.mooga.RecommendationEngine;
import org.apache.commons.lang.StringUtils;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.service.mooga.MoogaWebServicesLocator;
import com.hk.pact.service.mooga.MoogaWebServicesSoap_PortType;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.rmi.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Marut
 * Date: 6/23/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RecommendationEngineImpl implements RecommendationEngine {
    private final int HK_MOOGA_KEY = 11213141;   //provided by MOOGA
    private final int PRODUCT_MAX_LIMIT = 10;    //Max number of recommended products just in case MOOGA misbehaves

    MoogaWebServicesLocator servicesLocator = new MoogaWebServicesLocator();
    MoogaWebServicesSoap_PortType moogaStub;

    public RecommendationEngineImpl(){
        try{
            moogaStub = servicesLocator.getMoogaWebServicesSoap12();
        }catch (ServiceException ex)  {

        }
    }
    public void dummy(){
        String s = "";
    }

    public void pushAddToCart(long userId, List<ProductVariant> productVariants){
        for (ProductVariant pv : productVariants){
            pushAddToCart(userId, pv.getId());
        }
    }

    public void pushAddToCart(long userId, String productVariantId){

        try{
            String params = String.format("%s, %s, %s, %d, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
                    "1","0","Consumer/Add to cart",userId, productVariantId, "IT",
                    "0","0","0","0","0","0","0","0");

            String result = moogaStub.trans_NotifyTrans(HK_MOOGA_KEY,"OFBiz.NotifyTransaction",params);
            result = "";
        }catch(RemoteException ex){
            String err = ex.getMessage();
            err = "";
        }
    }

    public List<String> getRecommendedProducts(String pvID){
        MoogaWebServicesLocator servicesLocator = new MoogaWebServicesLocator();
        List<String> recommendedItems = new ArrayList<String>();
        try{
            String itemResponse = moogaStub.item_GetRecommendedItemsToItem(HK_MOOGA_KEY, pvID, "0","0","PRODUCT");
            String[] moogaResult = itemResponse.split("," , PRODUCT_MAX_LIMIT);
            for (String result : moogaResult){
                String[] splitResults = result.split("=");
                if ((splitResults.length > 0) && StringUtils.isNotBlank(splitResults[0])){ //Just in case MOOGA misbehaves
                    recommendedItems.add(splitResults[0].trim());
                }
            }
        }catch(RemoteException ex){

        }
        return  recommendedItems;
    }
    public List<String> getRecommendedProductVariants(String pvID){
        MoogaWebServicesLocator servicesLocator = new MoogaWebServicesLocator();
        List<String> recommendedItems = new ArrayList<String>();
        try{
            String itemResponse = moogaStub.item_GetRecommendedItemsToItem(HK_MOOGA_KEY, pvID, "","","");
            String[] moogaResult = itemResponse.split("," , PRODUCT_MAX_LIMIT);
            for (String result : moogaResult){
                String[] splitResults = result.split("=");
                if ((splitResults.length > 0) && StringUtils.isNotBlank(splitResults[0])){ //Just in case MOOGA misbehaves
                    recommendedItems.add(splitResults[0].trim());
                }
            }
        }catch(RemoteException ex){

        }
        return  recommendedItems;
    }
}
