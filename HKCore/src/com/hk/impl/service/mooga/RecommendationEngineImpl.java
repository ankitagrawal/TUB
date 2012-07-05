package com.hk.impl.service.mooga;

import com.hk.constants.core.Keys;
import com.hk.pact.service.mooga.MoogaCacheService;
import com.hk.pact.service.mooga.RecommendationEngine;
import org.apache.commons.lang.StringUtils;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.service.mooga.MoogaWebServicesLocator;
import com.hk.pact.service.mooga.MoogaWebServicesSoap_PortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.rmi.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Marut
 * Date: 6/23/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RecommendationEngineImpl implements RecommendationEngine {
    private final int HK_MOOGA_KEY = 11213141;   //Key provided by MOOGA
    private final String MOOGA_SEPARATOR = "=";
    private final String LEFT_OPERATOR = "[";
    private final String RIGHT_OPERATOR = "]";

    MoogaWebServicesLocator servicesLocator;    //Service locator wrapper for mooga..generated by Apache Axis
    MoogaWebServicesSoap_PortType moogaStub;   //Mooga wrapper generated by Apache Axis
    static Logger logger = LoggerFactory.getLogger(RecommendationEngineImpl.class);
    @Value("#{hkEnvProps['" + Keys.Env.moogaCache + "']}")
    boolean moogaCache;

    @Autowired
    MoogaCacheService moogaCacheService;

    /**
     * Mooga Actions as defined by MOOGA service. Has to be in sync with Mooga
     */
    static final class MoogaActions{
        public static final String CONSUMER_BUY = "Consumer/Buy";
        public static final String COSUMER_BROWSE = "Consumer/Browse";
        public static final String CONSUMER_ADD_TO_CART = "Consumer/Add to cart";
        public static final String CONSUMER_REMOVE_FROM_CART = "Consumer/Anonymous Browse";
        public static final String CONSUMER_ANONYMOUS_BROWSE = "Consumer/Anonymous Browse";
        public static final String CONSUMER_RATE_PRODUCT = "Consumer/Rate";
        public static final String CONSUMER_WRITE_REVIEW = "Consumer/Review";
        public static final String CONSUMER_SHOW_PRODUCT_DETAILS = "Consumer/Browse Details";
    }

    /**
     * This inner class does not access any instance member sp according to Joshua Bloch it is better to make it static
     */
    static class ProductResult implements Comparable<RecommendationEngineImpl.ProductResult>{
        public String name;
        public double rank;

        public ProductResult(String name, double  rank){
            this.name = name;
            this.rank = rank;
        }
        //Must to define otherwise its parent class can not be autowired
        public ProductResult(){

        }

        @Override
        public int compareTo(ProductResult other){
            return Double.valueOf(other.rank).compareTo(this.rank);
        }

        @Override
        public boolean equals(Object o){
            if (o instanceof ProductResult){
                return ((ProductResult)o).equals(this.name);
            }
            return  false;
        }

        @Override
        public int hashCode(){
            return name.hashCode();
        }
    }

    public RecommendationEngineImpl(){
        try{
            servicesLocator = new MoogaWebServicesLocator();
            moogaStub = servicesLocator.getMoogaWebServicesSoap12();
        }catch (ServiceException ex)  {

        }
    }

    public void notifyPurchase(long userId,  String pvId){
        notifyUserTransaction(userId, pvId, MoogaActions.CONSUMER_ADD_TO_CART);
    }

    public void notifyRemoveFromCart(long userId,  String pvId){
        notifyUserTransaction(userId, pvId, MoogaActions.CONSUMER_ADD_TO_CART);
    }

    public void notifyWriteReview(long userId,  String pvId){
        notifyUserTransaction(userId, pvId, MoogaActions.CONSUMER_ADD_TO_CART);
    }

    /**
     * pushes the add to cart action to MOOGA store
     * @param userId
     * @param productVariantId
     */
    public void notifyAddToCart(long userId, String productVariantId){
        notifyUserTransaction(userId, productVariantId, MoogaActions.CONSUMER_ADD_TO_CART);
    }

    public void notifyAddToCart(long userId, List<ProductVariant> productVariant) {
        for (ProductVariant pv : productVariant){
            notifyAddToCart(userId, pv.getId());
        }
    }

    /**
     * returns the recommended products for given product Id
     * @param pId
     * @return
     */
    public List<String> getRecommendedProducts(String pId){
        List<String> recommendedItems = new ArrayList<String>();
        try{
            if (moogaCache) {
                if(!moogaCacheService.hasProduct(pId)){
                    String itemResponse = moogaStub.item_GetRecommendedItemsToItem(HK_MOOGA_KEY, pId, "0","0","");
                    recommendedItems = getProducts(itemResponse);
                    //push results into caching layer
                    moogaCacheService.pushReconmmededItems(pId, recommendedItems);
                }else{
                    recommendedItems = moogaCacheService.getRecommendedItems(pId);
                }
            }else{
                String itemResponse = moogaStub.item_GetRecommendedItemsToItem(HK_MOOGA_KEY, pId, "0","0","PRODUCT");
                recommendedItems = getProducts(itemResponse);
            }
        }catch(RemoteException ex){

        }catch (RedisSystemException ex){

        }
        return  recommendedItems;
    }

    /**
     * returns the recommended product variants for given product variant id
     * @param pvId
     * @return
     */
    public List<String> getRecommendedProductVariants(String pvId){
        List<String> recommendedItems = new ArrayList<String>();
        try{
            if (moogaCache) {
                if(!moogaCacheService.hasProduct(pvId)){
                    String itemResponse = moogaStub.item_GetRecommendedItemsToItem(HK_MOOGA_KEY, pvId, "0","0","");
                    recommendedItems = getProducts(itemResponse);
                        //push results into caching layer
                        moogaCacheService.pushReconmmededItems(pvId, recommendedItems);
                    }else{
                        recommendedItems = moogaCacheService.getRecommendedItems(pvId);
                 }
            }else{
                String itemResponse = moogaStub.item_GetRecommendedItemsToItem(HK_MOOGA_KEY, pvId, "0","0","");
                recommendedItems = getProducts(itemResponse);
            }
        }catch(RemoteException ex){

        }catch (RedisSystemException ex){

        }
        return  recommendedItems;
    }

    private void notifyUserTransaction(long userId, String productVariantId, String moogaAction){
        try{
            String params = String.format("%s, %s, %s, %d, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
                    "1","0",moogaAction,userId, productVariantId, "IT",
                    "0","0","0","0","0","0","0","0");

            String result = moogaStub.trans_NotifyTrans(HK_MOOGA_KEY,"OFBiz.NotifyTransaction",params);
            result = "";
        }catch(RemoteException ex){
            String err = ex.getMessage();
            err = "";
        }
    }
    private List<String> getProducts(String itemResponse){

        List<String> recommendedItemsList = new ArrayList<String>();
        try{
        //MOOGA sends duplicate products sometimes..OOPS
        Set<String> productResults = new HashSet<String>();
        List<ProductResult> productResultsList = new ArrayList<ProductResult>();
        //Go Ahead only if there is no error from MOOGA
        if (!itemResponse.contains("ERR")){
            String[] moogaResult = itemResponse.split(",");

            for (String result : moogaResult){
                String[] splitResults = result.split(MOOGA_SEPARATOR);
                String productId = splitResults[0].replace(LEFT_OPERATOR,"").replace(RIGHT_OPERATOR, "").trim();
                Double rank =  Double.parseDouble(splitResults[1].replace(LEFT_OPERATOR, "").replace(RIGHT_OPERATOR, "").trim());
                if ((splitResults.length > 0) && StringUtils.isNotBlank(productId)){ //Just in case MOOGA misbehaves
                    if (!productResults.contains(productId)){
                        productResultsList.add(new ProductResult(productId,rank));
                        productResults.add(productId);
                    }
                }
            }
        }
        Collections.sort(productResultsList);
        for (ProductResult pr : productResultsList){
            recommendedItemsList.add(pr.name);
        }
        }catch (Exception ex){  //WALL : Suppress any exception coming from MOOGA API just in case

        }
        return  recommendedItemsList;
    }
}
