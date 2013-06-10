package com.hk.admin.impl.service.email;

import com.akube.framework.util.DateUtils;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.service.email.ProductVariantNotifyMeEmailService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.domain.catalog.category.Category;
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
import org.joda.time.DateTime;
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

    /*send mail for in stock products*/
    public void sendNotifyMeEmailForInStockProducts(final float notifyConversionRate, final int bufferRate) {
        List<NotifyMe> notifyMeListForInStockProduct = notifyMeDao.getNotifyMeListForProductVariantInStock();
        Map<String, List<NotifyMe>> finalUserListForNotificationMap = evaluateNotifyMeRequest(notifyConversionRate, bufferRate, false, notifyMeListForInStockProduct);
        adminEmailManager.sendNotifyUsersMails(finalUserListForNotificationMap);
    }


    /*send mails for product which are deleted or hidden or out of stock*/
    public int sendNotifyMeEmailForDeletedOOSHidden(final float notifyConversionRate, final int bufferRate) {
        Date monthBackDate = DateUtils.startOfMonthBack(new DateTime()).toDate();
        List<NotifyMe> notifyMeList = new ArrayList<NotifyMe>();
        List<NotifyMe> nonDeletedOOSList = notifyMeDao.searchNotifyMe(null, monthBackDate, null, null, null, true, false, null);
        if (nonDeletedOOSList != null && nonDeletedOOSList.size() > 0) {
            notifyMeList.addAll(nonDeletedOOSList);
        }
        List<NotifyMe> deletedList = notifyMeDao.searchNotifyMe(null, null, null, null, null, null, true, null);
        if (deletedList != null && deletedList.size() > 0) {
            notifyMeList.addAll(deletedList);
        }

        Map<String, List<NotifyMe>> finalUserListForNotificationMap = evaluateNotifyMeRequest(notifyConversionRate, bufferRate, true, notifyMeList);
        return adminEmailManager.sendNotifyUserMailsForDeletedOOSHiddenProducts(finalUserListForNotificationMap);
    }


    public Map<String, List<NotifyMe>> evaluateNotifyMeRequest(final float notifyConversionRate, final int bufferRate, boolean isSimilarProduct, List<NotifyMe> notifyMeList) {

        Map<String, Integer> allowedUserPerVariantMap = new HashMap<String, Integer>();
        Map<String, List<NotifyMe>> finalUserListForNotificationMap = new HashMap<String, List<NotifyMe>>();
        Map<String, Integer> userPerVariantAlreadyNotifiedMap = new HashMap<String, Integer>();

        try {
            if (notifyMeList != null) {
                for (NotifyMe notifyMe : notifyMeList) {
                    ProductVariant productVariant = notifyMe.getProductVariant();
                    String productVariantId = notifyMe.getProductVariant().getId();
                    String email = notifyMe.getEmail();
                    int allowedUserNumber = 0;
                    // get number of eligible user  to be notified for variant by formula
                    if (!(allowedUserPerVariantMap.containsKey(productVariantId))) {
                        /*send mails if unbooked inventory is greater that zero*/
                        int unbookedInventory = adminInventoryService.getNetInventory(productVariant).intValue() - (adminInventoryService.getBookedInventory(productVariant).intValue());
                        if (isSimilarProduct) {
                            /* product is OOS for similar product Notify me */
                            unbookedInventory = unbookedInventory > 0 ? unbookedInventory : getSumOfSimilarProductInventory(productVariant);
                        }
                        /* Calculate number of users eligible for sending mails */
                        if (unbookedInventory > 0) {
                            allowedUserNumber = (int) (unbookedInventory / (notifyConversionRate * bufferRate));
                            allowedUserPerVariantMap.put(productVariantId, allowedUserNumber);
                        }

                    } else {
                        allowedUserNumber = allowedUserPerVariantMap.get(productVariantId);
                    }
                    if (allowedUserNumber > 0) {
                        if (!(userPerVariantAlreadyNotifiedMap.containsKey(productVariantId))) {
                            userPerVariantAlreadyNotifiedMap.put(productVariantId, 0);
                        }
                        Integer alreadyNotified = userPerVariantAlreadyNotifiedMap.get(productVariantId);
                        if (alreadyNotified != null && alreadyNotified < allowedUserNumber) {
                            /*  List of Notify request per users*/
                            List<NotifyMe> notifyMeListPerUser;
                            if ((finalUserListForNotificationMap.containsKey(email))) {
                                notifyMeListPerUser = finalUserListForNotificationMap.get(email);
                            } else {
                                notifyMeListPerUser = new ArrayList<NotifyMe>();
                            }
                            /*check for user subscription*/
                            if (isUserSubscribed(email)) {
                                boolean productAlreadyPresent = userNotifyListAlreadyContainsProduct(notifyMeListPerUser, notifyMe.getProductVariant());
                                if (!productAlreadyPresent) {
                                    notifyMeListPerUser.add(notifyMe);
                                    finalUserListForNotificationMap.put(email, notifyMeListPerUser);
                                    userPerVariantAlreadyNotifiedMap.put(productVariantId, (alreadyNotified + 1));
                                }
                            }

                        }
                    }
                }
            }

        } catch (Exception ex) {
            logger.error("Unable to send bulk notify me emails " + ex.getMessage());
        }
        return finalUserListForNotificationMap;
    }


    private boolean isUserSubscribed(String email) {
        boolean isSubscribed = false;
        User user = userService.findByLogin(email);
        if (user != null) {
            if (user.isSubscribedForNotify()) {
                isSubscribed = true;
            }
        } else {
            // find existing recipients and check for subscribed
            EmailRecepient emailRecepient = emailRecepientDao.findByRecepient(email);
            if (emailRecepient != null) {
                if (emailRecepient.isSubscribed()) {
                    isSubscribed = true;
                }
            } else {
                isSubscribed = true;
            }

        }
        return isSubscribed;

    }


    /* To evaluate  no. of users to be notified for product which is  OOS : sum inventory of first three of  similar product list order by desc inventory*/
    private int getSumOfSimilarProductInventory(ProductVariant productVariant) {
        int sumOfUnbookedInvn = 0;
        List<ProductInventoryDto> productInventoryDtos = getProductVariantOfSimilarProductWithNthMaxAvailableInventory(productVariant, 3);

        if (productInventoryDtos != null) {
            for (ProductInventoryDto productInventoryDto : productInventoryDtos) {
                sumOfUnbookedInvn = sumOfUnbookedInvn + productInventoryDto.getInventory();
            }
        }
        return sumOfUnbookedInvn;
    }

    public List<Product> getSimilarProductsWithMaxUnbookedInvn(ProductVariant productVariant, int noOfSimilarProduct) {
        List<ProductInventoryDto> similarProductListInvn = getProductVariantsOfSimilarProductWithAvailableUnbookedInventory(productVariant);
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


    public List<ProductInventoryDto> getProductVariantsOfSimilarProductWithAvailableUnbookedInventory(ProductVariant productVariant) {
        List<ProductInventoryDto> similarProductWithInvnInDescOrder = new ArrayList<ProductInventoryDto>();
        List<SimilarProduct> similarProductList = productVariant.getProduct().getSimilarProducts();

        if (similarProductList != null) {
            for (SimilarProduct similarProduct : similarProductList) {
                List<ProductVariant> similarProductVariantList = similarProduct.getSimilarProduct().getProductVariants();
                int availableUnbookedInventoryForProduct = 0;
                for (ProductVariant similarProductVariant : similarProductVariantList) {
                    int bookedInvn = adminInventoryService.getBookedInventory(similarProductVariant).intValue();
                    int netInvn = adminInventoryService.getNetInventory(similarProductVariant).intValue();
                    availableUnbookedInventoryForProduct = availableUnbookedInventoryForProduct + (netInvn - bookedInvn);
                }
                if (availableUnbookedInventoryForProduct > 0) {
                    ProductInventoryDto productInventoryDto = new ProductInventoryDto(similarProduct.getSimilarProduct(), availableUnbookedInventoryForProduct);
                    similarProductWithInvnInDescOrder.add(productInventoryDto);
                }
            }
            Comparator<ProductInventoryDto> InventoryComparator = new ProductInventoryDto.InventoryComparator();
            Collections.sort(similarProductWithInvnInDescOrder, Collections.reverseOrder(InventoryComparator));
        }
        return similarProductWithInvnInDescOrder;
    }


    private boolean userNotifyListAlreadyContainsProduct(List<NotifyMe> notifyMeList, ProductVariant productVariant) {
        if (notifyMeList != null) {
            for (NotifyMe notifyMe : notifyMeList) {
                if (notifyMe.getProductVariant().getProduct().getId().equals(productVariant.getProduct().getId())) {
                    return true;
                }
            }
        }

        return false;
    }

    private List<ProductInventoryDto> getProductVariantOfSimilarProductWithNthMaxAvailableInventory(ProductVariant productVariant, int numberOfSimilarProduct) {
        List<ProductInventoryDto> similarProductWithInvnInDescOrder = getProductVariantsOfSimilarProductWithAvailableUnbookedInventory(productVariant);
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

}
