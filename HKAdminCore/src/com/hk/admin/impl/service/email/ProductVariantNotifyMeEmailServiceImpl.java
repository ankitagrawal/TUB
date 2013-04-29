package com.hk.admin.impl.service.email;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.service.email.ProductVariantNotifyMeEmailService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.Keys;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void sendNotifyMeEmail(final float notifyConversionRate, final int bufferRate) {

        Map<String, Integer> allowedUserPerVariantMap = new HashMap<String, Integer>();
        Map<String, List<NotifyMe>> finalUserListForNotificationMap = new HashMap<String, List<NotifyMe>>();
        Map<String, Integer> userPerVariantAlreadyNotifiedMap = new HashMap<String, Integer>();
        List<NotifyMe> notifyMeList = notifyMeDao.getNotifyMeListForProductVariantInStock();
        try {

            for (NotifyMe notifyMe : notifyMeList) {
                String productVariantId = notifyMe.getProductVariant().getId();
                String email = notifyMe.getEmail();
                int allowedUserNumber;
                // get number of eligible user  to be notified for variant by formula
                if (!(allowedUserPerVariantMap.containsKey(productVariantId))) {
                    // get inventory in warehouse
                    int availableInventory = adminInventoryService.getNetInventory(notifyMe.getProductVariant()).intValue();
                    allowedUserNumber = availableInventory;
                    if(bufferRate != 0 || notifyConversionRate != 0){
                        allowedUserNumber = (int) (availableInventory / (notifyConversionRate * bufferRate));
                    }
                    allowedUserPerVariantMap.put(productVariantId, allowedUserNumber);

                } else {
                    allowedUserNumber = allowedUserPerVariantMap.get(productVariantId);
                }
                if (!(userPerVariantAlreadyNotifiedMap.containsKey(productVariantId))) {
                    userPerVariantAlreadyNotifiedMap.put(productVariantId, 0);
                }
                int alreadyNotified = userPerVariantAlreadyNotifiedMap.get(productVariantId);
                if (alreadyNotified < allowedUserNumber) {
                    // get List of user to be informed
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
            //send mails
            adminEmailManager.sendNotifyUsersMails(finalUserListForNotificationMap);


        } catch (Exception ex) {
            logger.error("Unable to send bulk notify me emails " + ex.getMessage());
        }
    }
}
