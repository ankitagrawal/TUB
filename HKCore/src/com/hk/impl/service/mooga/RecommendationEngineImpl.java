package com.hk.impl.service.mooga;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.mooga.MoogaWebServicesLocator;
import com.hk.mooga.MoogaWebServicesSoap_PortType;
import com.hk.pact.service.mooga.RecommendationEngine;

/**
 * Created by IntelliJ IDEA. User: Marut Date: 6/23/12 Time: 11:18 AM To change this template use File | Settings | File
 * Templates.
 */
@Service
public class RecommendationEngineImpl implements RecommendationEngine {
    private final int             HK_MOOGA_KEY    = 11213141;                                               // Key
                                                                                                            // provided
                                                                                                            // by MOOGA
    private final String          MOOGA_SEPARATOR = "=";
    private final String          LEFT_OPERATOR   = "[";
    private final String          RIGHT_OPERATOR  = "]";

    MoogaWebServicesLocator       servicesLocator;                                                          // Service
                                                                                                            // locator
                                                                                                            // wrapper
                                                                                                            // for
                                                                                                            // mooga..generated
                                                                                                            // by Apache
                                                                                                            // Axis
    MoogaWebServicesSoap_PortType moogaStub;                                                                // Mooga
                                                                                                            // wrapper
                                                                                                            // generated
                                                                                                            // by Apache
                                                                                                            // Axis
    static Logger                 logger          = LoggerFactory.getLogger(RecommendationEngineImpl.class);

    @Value("#{hkEnvProps['" + Keys.Env.moogaUrl + "']}")
    String                        moogaUrl;

    @Value("#{hkEnvProps['" + Keys.Env.moogaEnabled + "']}")
    boolean                       moogaOn;

    /**
     * Mooga Actions as defined by MOOGA service. Has to be in sync with Mooga
     */
    static final class MoogaActions {
        public static final String CONSUMER_BUY                  = "Consumer/Buy";
        public static final String COSUMER_BROWSE                = "Consumer/Browse";
        public static final String CONSUMER_ADD_TO_CART          = "Consumer/Add to cart";
        public static final String CONSUMER_REMOVE_FROM_CART     = "Consumer/Remove from cart";
        public static final String CONSUMER_ANONYMOUS_BROWSE     = "Consumer/Anonymous Browse";
        public static final String CONSUMER_RATE_PRODUCT         = "Consumer/Rate";
        public static final String CONSUMER_WRITE_REVIEW         = "Consumer/Review";
        public static final String CONSUMER_SHOW_PRODUCT_DETAILS = "Consumer/Browse Details";
    }

    /**
     * This inner class does not access any instance member sp according to Joshua Bloch it is better to make it static
     */
    static class ProductResult implements Comparable<RecommendationEngineImpl.ProductResult> {
        public String name;
        public double rank;

        public ProductResult(String name, double rank) {
            this.name = name;
            this.rank = rank;
        }

        // Must to define otherwise its parent class can not be autowired
        public ProductResult() {

        }

        @Override
        public int compareTo(ProductResult other) {
            return Double.valueOf(other.rank).compareTo(this.rank);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ProductResult) {
                return ((ProductResult) o).equals(this.name);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    public RecommendationEngineImpl() {
        servicesLocator = new MoogaWebServicesLocator();
    }

    private MoogaWebServicesSoap_PortType getMoogaStub() {
        return moogaStub;
    }

    @PostConstruct
    public void initMooga() {
        try {
            servicesLocator.setMoogaWebServicesSoap12EndpointAddress(moogaUrl);
            servicesLocator.setMoogaWebServicesSoapEndpointAddress(moogaUrl);
            moogaStub = servicesLocator.getMoogaWebServicesSoap12();
        } catch (ServiceException ex) {

        }
    }

    public void notifyPurchase(long userId, String pvId) {
        notifyUserTransaction(userId, pvId, MoogaActions.CONSUMER_BUY);
    }

    public void notifyRemoveFromCart(long userId, String pvId) {
        notifyUserTransaction(userId, pvId, MoogaActions.CONSUMER_REMOVE_FROM_CART);
    }

    public void notifyWriteReview(long userId, String pvId) {
        notifyUserTransaction(userId, pvId, MoogaActions.CONSUMER_WRITE_REVIEW);
    }

    /**
     * pushes the add to cart action to MOOGA store
     * 
     * @param userId
     * @param productVariantId
     */
    public void notifyAddToCart(long userId, String productVariantId) {
        notifyUserTransaction(userId, productVariantId, MoogaActions.CONSUMER_ADD_TO_CART);
    }

    public void notifyAddToCart(long userId, List<ProductVariant> productVariant) {
        for (ProductVariant pv : productVariant) {
            if (pv != null) {
                notifyAddToCart(userId, pv.getId());
            }
        }
    }

    /**
     * returns the recommended products for given product Id
     * 
     * @param pId
     * @return
     */
    public List<String> getRecommendedProducts(String pId) {
        List<String> recommendedItems = new ArrayList<String>();
        try {
            String itemResponse = getMoogaStub().item_GetRecommendedItemsToItem(HK_MOOGA_KEY, pId, "0", "0", "PRODUCT");
            recommendedItems = getProducts(itemResponse);
        } catch (Exception ex) { // swallow the exception

        }
        return recommendedItems;
    }

    public List<String> getRelatedProducts(String pId, List<String> categories) {
        List<String> recommendedItems = new ArrayList<String>();
        try {
            StringBuffer categoryId = new StringBuffer();
            int maxLevel = Math.min(CategoryPriority.getRecommendationLevel(categories.get(0)), categories.size());
            int count = 0;
            for (String category : categories) {
                if (StringUtils.isNotBlank(category)) {
                    count++;
                    categoryId.append(category);
                    categoryId.append("/");
                    // Send only the required level Beauty if level is 2
                    // Parenting/Toddler if level is 2
                    // Services/Health Checkups/Thyroid if level is 3
                    if (count == maxLevel) {
                        break;
                    }
                }
            }
            String catId = categoryId.toString();
            catId = catId.substring(0, catId.length() - 1);// remove the last "/" character
            String itemResponse = getMoogaStub().item_GetRelatedItemsToItem(HK_MOOGA_KEY, pId, catId, "0", "PRODUCT");
            recommendedItems = getProducts(itemResponse);
        } catch (Exception ex) { // swallow the exception

        }
        return recommendedItems;
    }

    /**
     * returns the recommended product variants for given product variant id
     * 
     * @param pvId
     * @return
     */
    public List<String> getRecommendedProductVariants(String pvId) {
        List<String> recommendedItems = new ArrayList<String>();
        try {
            String itemResponse = getMoogaStub().item_GetRecommendedItemsToItem(HK_MOOGA_KEY, pvId, "0", "0", "");
            recommendedItems = getProducts(itemResponse);
        } catch (RemoteException ex) {

        }
        return recommendedItems;
    }

    private void notifyUserTransaction(long userId, String productVariantId, String moogaAction) {
        if (moogaOn) {
            try {
                String params = String.format("%s, %s, %s, %d, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s", "1", "0", moogaAction, userId, productVariantId, "IT", "0", "0", "0", "0",
                        "0", "0", "0", "0");

                String result = moogaStub.trans_NotifyTrans(HK_MOOGA_KEY, "OFBiz.NotifyTransaction", LEFT_OPERATOR + params + RIGHT_OPERATOR);
                result = "";
            } catch (RemoteException ex) {
                String err = ex.getMessage();
                err = "";
            } catch (Exception ex) {
                String err = ex.getMessage();
                err = "";
            }
        }
    }

    private List<String> getProducts(String itemResponse) {

        List<String> recommendedItemsList = new ArrayList<String>();
        try {
            // MOOGA sends duplicate products sometimes..OOPS
            Set<String> productResults = new HashSet<String>();
            List<ProductResult> productResultsList = new ArrayList<ProductResult>();
            // Go Ahead only if there is no error from MOOGA
            if (!itemResponse.contains("ERR")) {
                String[] moogaResult = itemResponse.split(",");

                for (String result : moogaResult) {
                    String[] splitResults = result.split(MOOGA_SEPARATOR);
                    String productId = splitResults[0].replace(LEFT_OPERATOR, "").replace(RIGHT_OPERATOR, "").trim();
                    if (productId.contains(":")) {
                        productId = productId.split(":")[0];
                    }
                    Double rank = Double.parseDouble(splitResults[1].replace(LEFT_OPERATOR, "").replace(RIGHT_OPERATOR, "").trim());
                    if ((splitResults.length > 0) && StringUtils.isNotBlank(productId)) { // Just in case MOOGA
                                                                                            // misbehaves
                        if (!productResults.contains(productId)) {
                            productResultsList.add(new ProductResult(productId, rank));
                            productResults.add(productId);
                        }
                    }
                }
            }
            Collections.sort(productResultsList);
            for (ProductResult pr : productResultsList) {
                recommendedItemsList.add(pr.name);
            }
        } catch (Exception ex) { // WALL : Suppress any exception coming from MOOGA API just in case

        }
        return recommendedItemsList;
    }
}
