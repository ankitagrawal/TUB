package com.hk.pact.dao.offer;

import java.util.Date;
import java.util.List;

import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface OfferInstanceDao extends BaseDao {

    public OfferInstance save(OfferInstance offerInstance) ;

    /**
     * This method will not only return a list of active offer instances but also checks for validity of every offer
     * instance. If offer instance is no longer active/valid then it will be set as inactive and saved as well. In the
     * end we will be returning only valid offer instances and de-activating invalid offer instances.
     * 
     * @param user
     * @return
     */
    public List<OfferInstance> getActiveOffers(User user) ;

    public OfferInstance getLatestActiveOffer(User user) ;

    public List<OfferInstance> findByUserAndCoupon(User user, Coupon coupon) ;

    public List<OfferInstance> findByCoupon(Coupon coupon) ;

    /**
     * OfferInstance end date will only requires when we want to set the specific end date i.e different from offer's
     * end date
     * 
     * @param offer
     * @param coupon
     * @param user
     * @param offerInstanceEndDate
     * @return
     */
    public OfferInstance createOfferInstance(Offer offer,Coupon coupon, User user, Date offerInstanceEndDate) ;


    public OfferInstance findByUserAndOffer(User user, Offer offer) ;

    public List<OfferInstance> findActiveOfferInstances(User user, Offer offer) ;

    public List<OfferInstance> findOfferInstancesByUserAndOffer(User user, Offer offer) ;

}
