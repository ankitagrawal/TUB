package com.hk.admin.impl.service.clm;

import com.hk.admin.pact.service.clm.KarmaProfileService;
import com.hk.admin.pact.dao.clm.KarmaProfileDao;
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
         return karmaProfile;
     }

    public KarmaProfileDao getKarmaProfileDao() {
        return karmaProfileDao;
    }

    public void setKarmaProfileDao(KarmaProfileDao karmaProfileDao) {
        this.karmaProfileDao = karmaProfileDao;
    }
}
