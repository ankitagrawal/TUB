package com.hk.admin.impl.service.email;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.service.email.ProductVariantNotifyMeEmailService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.SimilarProduct;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        Map<String, List<NotifyMe>> finalUserListForNotificationMap = sendNotifyMeEmail(notifyConversionRate, bufferRate, true, notifyMeList);
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
                        /* product is OOS for similar product Notify me */
                        if (unbookedInventory == 0) {
                            /* for  OOS  Product Send according sum of Invn of 3 similar products*/
                            int similarProductInventory = getSumOFSimilarProductInventory(productVariant);
                            if (similarProductInventory > 0) {
                                unbookedInventory = similarProductInventory;
                            }
                        }
                        if (unbookedInventory > 0) {
                            allowedUserNumber = (int) (unbookedInventory / (notifyConversionRate * bufferRate));
                            allowedUserPerVariantMap.put(productVariantId, allowedUserNumber);
                        }

                    } else {
                        /*For Notify me */
                        allowedUserNumber = (int) (unbookedInventory / (notifyConversionRate * bufferRate));
                        allowedUserPerVariantMap.put(productVariantId, allowedUserNumber);
                    }

                } else {
                    allowedUserNumber = allowedUserPerVariantMap.get(productVariantId);
                }
                if (!(userPerVariantAlreadyNotifiedMap.containsKey(productVariantId))) {
                    userPerVariantAlreadyNotifiedMap.put(productVariantId, 0);
                }
                Integer alreadyNotified = userPerVariantAlreadyNotifiedMap.get(productVariantId);
                if (alreadyNotified != null && alreadyNotified < allowedUserNumber) {
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
                        boolean productAlreadyPresent = notifyListAlreadyContainsProduct(notifyMeListPerUser, notifyMe.getProductVariant());
                        if (!productAlreadyPresent) {
                            notifyMeListPerUser.add(notifyMe);
                            finalUserListForNotificationMap.put(email, notifyMeListPerUser);
                            userPerVariantAlreadyNotifiedMap.put(productVariantId, (alreadyNotified + 1));
                        }
                    }

                }
            }

        } catch (Exception ex) {
            logger.error("Unable to send bulk notify me emails " + ex.getMessage());
        }
        return finalUserListForNotificationMap;
    }

    private int getSumOFSimilarProductInventory(ProductVariant productVariant) {
        int sumOfUnbookedInvn = 0;
        List<ProductInventoryDto> productInventoryDtos = getProductVariantOfSimilarProductWithNthMaxAvailableInventory(productVariant, 3);

        if (productInventoryDtos != null) {
            for (ProductInventoryDto productInventoryDto : productInventoryDtos) {
                sumOfUnbookedInvn = sumOfUnbookedInvn + productInventoryDto.getInventory();
            }
        }
        return sumOfUnbookedInvn;
    }


    public List<ProductInventoryDto> getProductVariantOfSimilarProductWithNthMaxAvailableInventory(ProductVariant productVariant, int numberOfSimilarProduct) {
        List<ProductInventoryDto> similarProductWithInvnInDescOrder = getProductVariantOfSimilarProductWithAvailableInventory(productVariant);
        List<ProductInventoryDto> finalSimilarProductWithInvnInDescOrderList = new ArrayList<ProductInventoryDto>();

        int count = 0;
        if (similarProductWithInvnInDescOrder != null) {
            for (ProductInventoryDto productInventoryDto : similarProductWithInvnInDescOrder) {
                if (count >= numberOfSimilarProduct) {
                    return finalSimilarProductWithInvnInDescOrderList;
                }
                finalSimilarProductWithInvnInDescOrderList.add(productInventoryDto);
                count++;

            }
        }
        return finalSimilarProductWithInvnInDescOrderList;
    }


    public List<Product> getInStockSimilarProductsWithMaxInvn(ProductVariant productVariant, int noOfSimilarProduct) {
        List<ProductInventoryDto> similarProductListInvn = getProductVariantOfSimilarProductWithAvailableInventory(productVariant);
        List<Product> similarProductList = new ArrayList<Product>();
        int count = 0;
        if (similarProductListInvn != null) {
            for (ProductInventoryDto productInventoryDto : similarProductListInvn) {
                if (count >= noOfSimilarProduct) {
                    return similarProductList;
                }
                similarProductList.add(productInventoryDto.getProduct());
                count++;

            }
        }
        return similarProductList;
    }


    public List<ProductInventoryDto> getProductVariantOfSimilarProductWithAvailableInventory(ProductVariant productVariant) {
        List<ProductInventoryDto> similarProductWithInvnInDescOrder = new ArrayList<ProductInventoryDto>();
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
                    ProductInventoryDto productInventoryDto = new ProductInventoryDto(similarProduct.getSimilarProduct(), availableInventoryForProduct);
                    similarProductWithInvnInDescOrder.add(productInventoryDto);
                }
            }
            Comparator<ProductInventoryDto> InventoryComparator = new ProductInventoryDto.InventoryComparator();
            Collections.sort(similarProductWithInvnInDescOrder, Collections.reverseOrder(InventoryComparator));
        }
        return similarProductWithInvnInDescOrder;
    }


    private boolean notifyListAlreadyContainsProduct(List<NotifyMe> notifyMeList, ProductVariant productVariant) {
        if (notifyMeList != null) {
            for (NotifyMe notifyMe : notifyMeList) {
                if (notifyMe.getProductVariant().getProduct().getId().equals(productVariant.getProduct().getId())) {
                    return true;
                }
            }
        }

        return false;
    }

}
