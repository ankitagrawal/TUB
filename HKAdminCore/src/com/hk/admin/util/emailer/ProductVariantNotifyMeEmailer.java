package com.hk.admin.util.emailer;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.util.SendGridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/12/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ProductVariantNotifyMeEmailer {
    @Autowired
    NotifyMeDao notifyMeDao;
    @Autowired
    EmailCampaignDao emailCampaignDao;
    @Autowired
    AdminEmailManager adminEmailManager;
    @Autowired
    AdminInventoryService adminInventoryService;

    float notifyConversionRate = 0.01f;
    float bufferRate = 2;

    public void sendNotifyMeEmail() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<ProductVariant, Integer> allowedUserPerVariantMap = new HashMap<ProductVariant, Integer>();
        Map<String, List<ProductVariant>> finalUserListForNotificationMap = new HashMap<String, List<ProductVariant>>();
        Map<ProductVariant, Integer> userPerVariantAlreadyNotifiedMap = new HashMap<ProductVariant, Integer>();
        List<NotifyMe> notifyMeList = notifyMeDao.getNotifyMeListForProductVariantInStock();
        try {

            for (NotifyMe notifyMe : notifyMeList) {
                ProductVariant productVariant = notifyMe.getProductVariant();
                String email = notifyMe.getEmail();
                int allowedUserNumber;
                // get number of eligible user  to be notified for variant by formula
                if (!(allowedUserPerVariantMap.containsKey(productVariant))) {
                    // get inventory in warehouse

                    int availableInventory = adminInventoryService.getNetInventory(productVariant).intValue();
                    allowedUserNumber = (int) (availableInventory / (notifyConversionRate * bufferRate));
                    allowedUserPerVariantMap.put(productVariant, allowedUserNumber);


                } else {
                    allowedUserNumber = allowedUserPerVariantMap.get(productVariant);

                }
                if (!(userPerVariantAlreadyNotifiedMap.containsKey(productVariant))) {
                    userPerVariantAlreadyNotifiedMap.put(productVariant, 0);
                }
                int alreadyNotified = userPerVariantAlreadyNotifiedMap.get(productVariant);
                if (alreadyNotified <= allowedUserNumber) {
                    // get List of user to be informed
                    List<ProductVariant> variantList = null;
                    if ((finalUserListForNotificationMap.containsKey(email))) {
                        variantList = finalUserListForNotificationMap.get(email);
                    } else {
                        variantList = new ArrayList<ProductVariant>();
                    }
                    variantList.add(productVariant);
                    finalUserListForNotificationMap.put(email, variantList);
                    userPerVariantAlreadyNotifiedMap.put(productVariant, (alreadyNotified + 1));

                }


            }


            //send mails

            adminEmailManager.sendNotifyUsersMails(finalUserListForNotificationMap);
        } catch (Exception ex) {


        }
    }


}
