package com.hk.admin.impl.service.email;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.service.email.ProductVariantNotifyMeEmailService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.SimilarProduct;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.impl.dao.email.NotifyMeDto;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/12/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ProductVariantNotifyMeEmailServiceImpl implements ProductVariantNotifyMeEmailService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProductVariantNotifyMeEmailServiceImpl.class);
    @Autowired

    NotifyMeDao notifyMeDao;
    @Autowired
    EmailCampaignDao emailCampaignDao;
    @Autowired
    AdminEmailManager adminEmailManager;
    @Autowired
    AdminInventoryService adminInventoryService;
    @Autowired
    UserService userService;
    @Autowired
    EmailRecepientDao emailRecepientDao;


    public void sendNotifyMeEmailForInStockProducts(final float notifyConversionRate, final int bufferRate) {
        Map<String, List<NotifyMe>> finalUserListForNotificationMap = sendNotifyMeEmail(notifyConversionRate, bufferRate, false, null);
        //send mails
        adminEmailManager.sendNotifyUsersMails(finalUserListForNotificationMap);
    }

    public int sendNotifyMeEmailForDeletedOOSHidden(final float notifyConversionRate, final int bufferRate, List<NotifyMe> notifyMeList) {
        Map<String, List<NotifyMe>> finalUserListForNotificationMap = sendNotifyMeEmail(notifyConversionRate, bufferRate, false, notifyMeList);
        //send mails
        return adminEmailManager.sendNotifyUserMailsForDeletedOOSHiddenProducts(finalUserListForNotificationMap);
    }


    public Map<String, List<NotifyMe>> sendNotifyMeEmail(final float notifyConversionRate, final int bufferRate, boolean isSimilarProduct, List<NotifyMe> notifyMeList) {

        Map<String, Integer> allowedUserPerVariantMap = new HashMap<String, Integer>();
        Map<String, List<NotifyMe>> finalUserListForNotificationMap = new HashMap<String, List<NotifyMe>>();
        Map<String, Integer> userPerVariantAlreadyNotifiedMap = new HashMap<String, Integer>();
        List<NotifyMe> finalNotifyList;
        if (isSimilarProduct) {
            finalNotifyList = notifyMeList;
        } else {
            finalNotifyList = notifyMeDao.getNotifyMeListForProductVariantInStock();
        }
        try {

            for (NotifyMe notifyMe : finalNotifyList) {
                ProductVariant productVariant = notifyMe.getProductVariant();
                String productVariantId = notifyMe.getProductVariant().getId();
                String email = notifyMe.getEmail();
                int allowedUserNumber;
                // get number of eligible user  to be notified for variant by formula
                if (!(allowedUserPerVariantMap.containsKey(productVariantId))) {
                    /*Check for inventory*/
                    int availableInventory = adminInventoryService.getNetInventory(productVariant).intValue();
                    int bookedInventory = adminInventoryService.getBookedInventory(productVariant).intValue();
                    int unbookedInventory = availableInventory - bookedInventory;
                    allowedUserNumber = availableInventory;
                    /* For Similar Products*/
                    if (isSimilarProduct) {
                        /* product is OOS */
                        if (unbookedInventory == 0) {
                            /* for  OOS  Product Send according to 1st similar product with Max inventory*/
                            int similarProductInventory = getSimilarProductInventory(productVariant);
                            if (similarProductInventory == 0) {
                                /*don't send mails for this case where products is OOS and all similarProducts are not present or OOS*/
                                allowedUserPerVariantMap.put(productVariantId, -1);
                            } else {
                                /*similar product inventory*/
                                allowedUserPerVariantMap.put(productVariantId, similarProductInventory);
                            }

                        } else {
                            /*deleted-hidden  product is in stock*/
                            allowedUserNumber = (int) (unbookedInventory / (notifyConversionRate * bufferRate));
                            allowedUserPerVariantMap.put(productVariantId, allowedUserNumber);

                        }
                    } else {
                        /*For Product In Stock*/
                        allowedUserNumber = (int) (unbookedInventory / (notifyConversionRate * bufferRate));
                        allowedUserPerVariantMap.put(productVariantId, allowedUserNumber);
                    }

                } else {
                    allowedUserNumber = allowedUserPerVariantMap.get(productVariantId);
                }
                if (!(userPerVariantAlreadyNotifiedMap.containsKey(productVariantId))) {
                    userPerVariantAlreadyNotifiedMap.put(productVariantId, 0);
                }
                int alreadyNotified = userPerVariantAlreadyNotifiedMap.get(productVariantId);
                if (alreadyNotified < allowedUserNumber && allowedUserNumber != -1) {
                    /* get List of user to be informed */
                    List<NotifyMe> notifyMeListPerUser = null;
                    boolean isEligible = false;
                    if ((finalUserListForNotificationMap.containsKey(email))) {
                        notifyMeListPerUser = finalUserListForNotificationMap.get(email);
                    } else {
                        notifyMeListPerUser = new ArrayList<NotifyMe>();
                    }

                    User user = userService.findByLogin(email);
                    if (user != null) {
                        if (user.isSubscribedForNotify()) {
                            isEligible = true;
                        }
                    } else {
                        // find existing recipients and check for subscribed
                        EmailRecepient emailRecepient = emailRecepientDao.findByRecepient(email);
                        if (emailRecepient != null) {
                            if (emailRecepient.isSubscribed()) {
                                isEligible = true;
                            }
                        } else {
                            isEligible = true;
                        }

                    }
                    if (isEligible) {
                        notifyMeListPerUser.add(notifyMe);
                        finalUserListForNotificationMap.put(email, notifyMeListPerUser);
                        userPerVariantAlreadyNotifiedMap.put(productVariantId, (alreadyNotified + 1));
                    }

                }
            }


        } catch (Exception ex) {
            logger.error("Unable to send bulk notify me emails " + ex.getMessage());
        }
        return finalUserListForNotificationMap;
    }

    private int getSimilarProductInventory(ProductVariant productVariant) {
        int unbookedInventory = 0;
        List<ProductInventoryDomain> productInventoryDomains = getProductVariantOfSimilarProductWithNthMaxAvailableInventory(productVariant, 1);
        if (productInventoryDomains != null && productInventoryDomains.size() > 0) {
            unbookedInventory = productInventoryDomains.get(0).getInventory();
        }
        return unbookedInventory;
    }


    public List<ProductInventoryDomain> getProductVariantOfSimilarProductWithNthMaxAvailableInventory(ProductVariant productVariant, int numberOfSimilarProduct) {
        List<ProductInventoryDomain> similarProductWithInvnInDescOrder = getProductVariantOfSimilarProductWithAvailableInventory(productVariant);
        List<ProductInventoryDomain> finalSimilarProductWithInvnInDescOrderList = new ArrayList<ProductInventoryDomain>();

        int count = 0;
        if (similarProductWithInvnInDescOrder != null) {
            for (ProductInventoryDomain productInventoryDomain : similarProductWithInvnInDescOrder) {
                if (count >= numberOfSimilarProduct) {
                    return finalSimilarProductWithInvnInDescOrderList;
                }
                finalSimilarProductWithInvnInDescOrderList.add(productInventoryDomain);
                count++;

            }
        }
        return finalSimilarProductWithInvnInDescOrderList;
    }


    public List<Product> getInStockSimilarProductsWithMaxInvn(ProductVariant productVariant, int noOfSimilarProduct) {
        List<ProductInventoryDomain> similarProductListInvn = getProductVariantOfSimilarProductWithAvailableInventory(productVariant);
        List<Product> similarProductList = new ArrayList<Product>();
        int count = 0;
        if (similarProductListInvn != null) {
            for (ProductInventoryDomain productInventoryDomain : similarProductListInvn) {
                if (count >= noOfSimilarProduct) {
                    return similarProductList;
                }
                similarProductList.add(productInventoryDomain.getProduct());
                count++;

            }
        }

        return similarProductList;
    }


    public List<ProductInventoryDomain> getProductVariantOfSimilarProductWithAvailableInventory(ProductVariant productVariant) {
        List<ProductInventoryDomain> similarProductWithInvnInDescOrder = new ArrayList<ProductInventoryDomain>();
        List<SimilarProduct> similarProductList = productVariant.getProduct().getSimilarProducts();


        if (similarProductList != null) {
            for (SimilarProduct similarProduct : similarProductList) {
                List<ProductVariant> similarProductVariantList = similarProduct.getSimilarProduct().getProductVariants();
                int availableInventoryForProduct = 0;
                for (ProductVariant similarProductVariant : similarProductVariantList) {
                    int bookedInvn = adminInventoryService.getBookedInventory(similarProductVariant).intValue();
                    int netInvn = adminInventoryService.getNetInventory(similarProductVariant).intValue();
                    availableInventoryForProduct = availableInventoryForProduct + (netInvn - bookedInvn);
                }
                if (availableInventoryForProduct > 0) {
                    ProductInventoryDomain productInventoryDomain = new ProductInventoryDomain(similarProduct.getSimilarProduct(), availableInventoryForProduct);
                    similarProductWithInvnInDescOrder.add(productInventoryDomain);
                }
            }
            Collections.sort(similarProductWithInvnInDescOrder);
        }

        return similarProductWithInvnInDescOrder;
    }

}
