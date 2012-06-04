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
import com.hk.constants.order.EnumCartLineItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 29, 2012
 * Time: 3:57:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class KarmaProfileServiceImpl implements KarmaProfileService{

    private static Logger logger = LoggerFactory.getLogger(KarmaProfileService.class);
    @Autowired
    private KarmaProfileDao karmaProfileDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SkuService skuService;

    //the following contain avg first purchase margins
    final static Double beautyAvgMargin=0.0;
    final static Double beautyAvgScore=0.0;
    final static Double nutritionAvgMargin=0.0;
    final static Double nutritionAvgScore=0.0;
    final static Double eyeAvgMargin=0.0;
    final static Double eyeAvgScore=0.0;
    final static Double pcAvgMargin=0.0;
    final static Double pcAvgScore=0.0;
    final static Double sportsAvgMargin=0.0;
    final static Double sportsAvgScore=0.0;
    final static Double hmAvgMargin=0.0;
    final static Double hmAvgScore=0.0;
    final static Double parentingAvgMargin=0.0;
    final static Double parentingAvgScore=0.0;
    final static Double servicesAvgMargin=0.0;
    final static Double servicesAvgScore=0.0;
    final static Double diabetesAvgMargin=0.0;
    final static Double diabetesAvgScore=0.0;


    @Transactional
     public KarmaProfile save(KarmaProfile karmaProfile) {
        return getKarmaProfileDao().save(karmaProfile);
      }


     public KarmaProfile findByUser(User user){
       return getKarmaProfileDao().findByUser(user);
     }

     public KarmaProfile updateKarmaAfterOrder(Order order){
         User user= order.getUser();
         KarmaProfile karmaProfile=findByUser(user);
         if(karmaProfile ==null){
             karmaProfile = new KarmaProfile();
             karmaProfile.setUser(user);
             karmaProfile.setKarmaPoints(getKarmaPoints(order));
         }
         this.save(karmaProfile);
         setKarmaInOrder(order);
         return karmaProfile;
     }

     public void setKarmaInOrder(Order order){
        User user=order.getUser();
        KarmaProfile karmaProfile=this.findByUser(user);
        order.setScore(new Long(karmaProfile.getKarmaPoints()));
        getOrderService().save(order);
    }


    public int getKarmaPoints(Order order){
            Double points =0.0;
            try {
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            for (CartLineItem lineItem : productCartLineItems) {
                    Double costPrice = 0.0;
                    if (lineItem.getProductVariant().getCostPrice() != null) {
                        costPrice = lineItem.getProductVariant().getCostPrice();
                    } else {
                        costPrice = lineItem.getHkPrice();
                    }

                    String basketCategoryName=lineItem.getProductVariant().getProduct().getPrimaryCategory().getName();
                   if(basketCategoryName.equals("diabetes")){
                          points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/diabetesAvgMargin)*diabetesAvgScore;
                   }else if(basketCategoryName.equals("eye")){
                        points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/eyeAvgMargin)*eyeAvgScore;
                  } else if(basketCategoryName.equals("personal-care")){
                        points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/pcAvgMargin)*pcAvgScore;
                  }else if(basketCategoryName.equals("home-devices")){
                        points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/hmAvgMargin)*hmAvgScore;
                  }else if(basketCategoryName.equals("beauty")){
                        points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/beautyAvgMargin)*beautyAvgScore;
                  }else if(basketCategoryName.equals("nutrition")){
                        points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/nutritionAvgMargin)*nutritionAvgScore;
                  }else if(basketCategoryName.equals("services")){
                        points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/servicesAvgMargin)*servicesAvgScore;
                  }else if(basketCategoryName.equals("parenting")){
                        points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/parentingAvgMargin)*parentingAvgScore;
                  }else if(basketCategoryName.equals("sports")){
                        points += (((lineItem.getHkPrice()-costPrice)* lineItem.getQty())/sportsAvgMargin)*sportsAvgScore;
                  } else{
                       //donothing - impossible
                   }


            }
        } catch (Exception e) {
            logger.error("Error while calculating points for order in karma profile - " + order.getId());
            logger.error(e.getMessage(), e);
        }
        int karmaPoints=points.intValue();
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
