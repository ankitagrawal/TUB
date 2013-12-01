package com.hk.impl.dao.coupon;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.coupon.FbcouponCoupon;
import com.hk.domain.marketing.FbcouponCampaign;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.coupon.FbcouponCouponDao;

@Repository
public class FbcouponCouponDaoImpl extends BaseDaoImpl implements FbcouponCouponDao {

    @Transactional
    public FbcouponCoupon save(FbcouponCoupon coupon) {
        // set defaults
        if (coupon != null) {
            if (coupon.getCreateDate() == null)
                coupon.setCreateDate(BaseUtils.getCurrentTimestamp());
        }

        return (FbcouponCoupon) super.save(coupon);
    }

    public FbcouponCoupon findByCode(String code) {
        Criteria criteria = getSession().createCriteria(FbcouponCoupon.class);
        criteria.add(Restrictions.eq("coupon", code));
        return (FbcouponCoupon) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public FbcouponCoupon findFreshCoupon(FbcouponCampaign fbcouponCampaign) {
        Criteria criteria = getSession().createCriteria(FbcouponCoupon.class);
        criteria.add(Restrictions.eq("fbcouponCampaign", fbcouponCampaign));
        criteria.add(Restrictions.eq("couponUseCount", 0L));
        criteria.setMaxResults(1);

        List<FbcouponCoupon> couponList = criteria.list();
        return couponList == null || couponList.isEmpty() ? null : couponList.get(0);
    }

}
