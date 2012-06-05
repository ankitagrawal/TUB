package com.hk.impl.service.clm;

import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.dao.clm.KarmaProfileDao;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.clm.EnumCLMMargin;
import com.hk.constants.clm.CLMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: Pradeep Date: May 29, 2012 Time: 3:57:32 PM To change this template use File |
 * Settings | File Templates.
 */
@Service
public class KarmaProfileServiceImpl implements KarmaProfileService {

    private static Logger   logger             = LoggerFactory.getLogger(KarmaProfileService.class);
    @Autowired
    private KarmaProfileDao karmaProfileDao;
    @Autowired
    private OrderService    orderService;
    @Autowired
    private SkuService      skuService;

    @Transactional
    public KarmaProfile save(KarmaProfile karmaProfile) {
        return getKarmaProfileDao().save(karmaProfile);
    }

    public KarmaProfile findByUser(User user) {
        return getKarmaProfileDao().findByUser(user);
    }

    public KarmaProfile updateKarmaAfterOrder(Order order) {
        User user = order.getUser();
        KarmaProfile karmaProfile = findByUser(user);
        if (karmaProfile == null) {
            karmaProfile = new KarmaProfile();
            karmaProfile.setUser(user);
            karmaProfile.setKarmaPoints(getKarmaPoints(order));
        }
        this.save(karmaProfile);
        setKarmaInOrderForUser(order, user);
        return karmaProfile;
    }

    public boolean isPriorityUser(User user){
        KarmaProfile karmaProfile=this.findByUser(user);
        if(karmaProfile!=null){
            if(karmaProfile.getKarmaPoints() >= CLMConstants.thresholdScore){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean isPriorityOrder(Order order){
        int score = order.getScore().intValue();

        if(order.getScore() >= CLMConstants.thresholdScore){
            return true;
        }else{
            return false;
        }

    }


    private void setKarmaInOrderForUser(Order order, User user) {
        KarmaProfile karmaProfile = this.findByUser(user);
        if (karmaProfile != null) {
            order.setScore(new Long(karmaProfile.getKarmaPoints()));
            getOrderService().save(order);
        }
    }

    private int getKarmaPoints(Order order) {
        Double points = 0.0;
        try {
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            for (CartLineItem lineItem : productCartLineItems) {
                Double costPrice = 0.0;
                if (lineItem.getProductVariant().getCostPrice() != null) {
                    costPrice = lineItem.getProductVariant().getCostPrice();
                } else {
                    costPrice = lineItem.getHkPrice();
                }

                String basketCategoryName = lineItem.getProductVariant().getProduct().getPrimaryCategory().getName();

                EnumCLMMargin marginFactor = EnumCLMMargin.getMarginFromCategory(basketCategoryName);

                points += ((lineItem.getHkPrice() - costPrice) * lineItem.getQty())*marginFactor.getMargin();
            }
        } catch (Exception e) {
            logger.error("Error while calculating points for order in karma profile - " + order.getId(), e);
        }
        int karmaPoints = points.intValue();
        return karmaPoints;
    }

    public KarmaProfileDao getKarmaProfileDao() {
        return karmaProfileDao;
    }

    public void setKarmaProfileDao(KarmaProfileDao karmaProfileDao) {
        this.karmaProfileDao = karmaProfileDao;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }
}
