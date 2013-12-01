package com.hk.impl.dao.offer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.DateUtils;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.offer.OfferInstanceDao;

@SuppressWarnings("unchecked")
@Repository
public class OfferInstanceDaoImpl extends BaseDaoImpl implements OfferInstanceDao {

    @Transactional
    public OfferInstance save(OfferInstance offerInstance) {
        if (offerInstance != null) {
            if (offerInstance.getCreateDate() == null)
                offerInstance.setCreateDate(BaseUtils.getCurrentTimestamp());
        }
        return (OfferInstance) super.save(offerInstance);
    }

    /**
     * This method will not only return a list of active offer instances but also checks for validity of every offer
     * instance. If offer instance is no longer active/valid then it will be set as inactive and saved as well. In the
     * end we will be returning only valid offer instances and de-activating invalid offer instances.
     * 
     * @param user
     * @return
     */
    public List<OfferInstance> getActiveOffers(User user) {
        List<OfferInstance> activeOffers = new ArrayList<OfferInstance>();
        for (OfferInstance offerInstance : getOffersByUserAndState(user, true)) {
            if (!offerInstance.isValid()) {
                offerInstance.setActive(false);
                save(offerInstance);
            } else {
                activeOffers.add(offerInstance);
            }
        }
        return activeOffers;
    }

    public List<OfferInstance> getAllOffers(User user) {
        return getOffersByUserAndState(user, false);
    }

    private List<OfferInstance> getOffersByUserAndState(User user, Boolean onlyActive) {
        Criteria criteria = getSession().createCriteria(OfferInstance.class);
        criteria.add(Restrictions.eq("user", user));
        if (onlyActive) {
            criteria.add(Restrictions.eq("active", true));
        }
        criteria.addOrder(Order.desc("createDate"));
        return (List<OfferInstance>) criteria.list();
    }

    public OfferInstance getLatestActiveOffer(User user) {
        List<OfferInstance> offerInstances = getActiveOffers(user);
        return offerInstances.size() > 0 ? offerInstances.get(0) : null;
    }

    /*
     * public OfferInstance findByUserAndCoupon(User user, Coupon coupon) { Criteria criteria =
     * getSession().createCriteria(OfferInstance.class); criteria.add(Restrictions.eq("user", user));
     * criteria.add(Restrictions.eq("coupon", coupon)); return (OfferInstance) criteria.uniqueResult(); }
     */

    public List<OfferInstance> findByUserAndCoupon(User user, Coupon coupon) {
        Criteria criteria = getSession().createCriteria(OfferInstance.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("coupon", coupon));
        criteria.add(Restrictions.eq("active", Boolean.TRUE));
        return criteria.list();
    }

    public List<OfferInstance> findByCoupon(Coupon coupon) {
        Criteria criteria = getSession().createCriteria(OfferInstance.class);
        criteria.add(Restrictions.eq("coupon", coupon));
        return criteria.list();
    }

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
    public OfferInstance createOfferInstance(Offer offer, Coupon coupon, User user, Date offerInstanceEndDate) {

        // todo to check whether this offer is valid for the user or not i.e role permissions etc.

        if (offerInstanceEndDate == null)
            return save(new OfferInstance(offer, coupon, user));

        OfferInstance offerInstance = new OfferInstance(offer, coupon, user);
        offerInstance.setEndDate(DateUtils.getEndOfDay(offerInstanceEndDate));
        return save(offerInstance);
    }

    public OfferInstance findByUserAndOffer(User user, Offer offer) {
        Criteria criteria = getSession().createCriteria(OfferInstance.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("offer", offer));
        return (OfferInstance) criteria.uniqueResult();
    }

    public List<OfferInstance> findActiveOfferInstances(User user, Offer offer) {
        Criteria criteria = getSession().createCriteria(OfferInstance.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("offer", offer));
        criteria.add(Restrictions.eq("active", true));
        return criteria.list();
    }

    public List<OfferInstance> findOfferInstancesByUserAndOffer(User user, Offer offer) {
        Criteria criteria = getSession().createCriteria(OfferInstance.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("offer", offer));
        return criteria.list();
    }

}
