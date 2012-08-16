package com.hk.impl.service.clm;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.clm.EnumCLMMargin;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.clm.CategoryKarmaProfile;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.dao.clm.CategoryKarmaProfileDao;
import com.hk.pact.dao.clm.KarmaProfileDao;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.service.inventory.SkuService;

/**
 * Created by IntelliJ IDEA. User: Pradeep Date: May 29, 2012 Time: 3:57:32 PM To change this template use File |
 * Settings | File Templates.
 */
@Service
public class KarmaProfileServiceImpl implements KarmaProfileService {

    private static Logger   logger = LoggerFactory.getLogger(KarmaProfileService.class);
    @Autowired
    private KarmaProfileDao karmaProfileDao;
    @Autowired
    private CategoryKarmaProfileDao categoryKarmaProfileDao;
    @Autowired
    private SkuService      skuService;

    @Transactional
    public KarmaProfile save(KarmaProfile karmaProfile) {
        return getKarmaProfileDao().save(karmaProfile);
    }

    public CategoryKarmaProfile save(CategoryKarmaProfile categoryKarmaProfile){
        return getCategoryKarmaProfileDao().save(categoryKarmaProfile);
    }

    public KarmaProfile findByUser(User user) {
        return getKarmaProfileDao().findByUser(user);
    }

    public CategoryKarmaProfile findByUserAndCategory(User user, Category category){
         return getCategoryKarmaProfileDao().findByUserAndCategory(user,category);
    }


    public KarmaProfile updateKarmaAfterOrder(Order order) {
        User user = order.getUser();
        KarmaProfile karmaProfile = findByUser(user);
        if (karmaProfile == null) {
            karmaProfile = new KarmaProfile();
            karmaProfile.setUser(user);
            karmaProfile.setKarmaPoints(getKarmaPoints(order));
        }
        karmaProfile = this.save(karmaProfile);
        return karmaProfile;
    }

    //not using this method anywhere
    public KarmaProfile updateKarmaAfterOrderCancellation(Order order){
        User user = order.getUser();
        KarmaProfile karmaProfile = findByUser(user);
        //the below code is written for orders which might be pending before the karmaprofile feature is implemented
        //and must be removed in the future
        if (karmaProfile == null) {
            karmaProfile = new KarmaProfile();
            karmaProfile.setUser(user);
        }
        //0 karma points is a safe hardcoding unless the customer buys again the same month
        List<Order> orderList=user.getOrders();
        if(orderList.size()>0)  {
            for(Order ord : orderList){
                if(ord.getOrderStatus().getId() != EnumOrderStatus.Cancelled.getId()) {
                    if(ord.getId() == order.getId()){
                        karmaProfile.setKarmaPoints(0);
                        karmaProfile = this.save(karmaProfile);
                        break;
                    }
                   break;
                }
            }

        }
        return karmaProfile;
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


                if (marginFactor != null) {
                    points += ((lineItem.getHkPrice() - costPrice) * lineItem.getQty()) * marginFactor.getMargin();
                }
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

    public CategoryKarmaProfileDao getCategoryKarmaProfileDao() {
        return categoryKarmaProfileDao;
    }

    public void setCategoryKarmaProfileDao(CategoryKarmaProfileDao categoryKarmaProfileDao) {
        this.categoryKarmaProfileDao = categoryKarmaProfileDao;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }
}
