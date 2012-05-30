package com.hk.impl.service.clm;

import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.dao.clm.KarmaProfileDao;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;
import com.hk.pact.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 29, 2012
 * Time: 3:57:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class KarmaProfileServiceImpl implements KarmaProfileService{

    @Autowired
    private KarmaProfileDao karmaProfileDao;
    @Autowired
    private OrderService orderService;

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
             karmaProfile.setKarmaPoints(100);
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
}
