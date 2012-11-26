package com.hk.impl.dao.coupon;

import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.order.Order;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.coupon.Coupon;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.coupon.CouponDao;

import java.util.List;

@Repository
public class CouponDaoImpl extends BaseDaoImpl implements CouponDao {

    @Transactional
    public Coupon save(Coupon coupon) {
        if (coupon != null) {
            if (coupon.getAlreadyUsed() == null) {
                coupon.setAlreadyUsed(0L);
            }
            if (coupon.getCreateDate() == null) {
                coupon.setCreateDate(BaseUtils.getCurrentTimestamp());
            }
        }
        return (Coupon) super.save(coupon);
    }

    public Coupon findByCode(String couponCode) {
        Criteria criteria = getSession().createCriteria(Coupon.class);
        criteria.add(Restrictions.eq("code", couponCode));
        return (Coupon) criteria.uniqueResult();
    }

	@Override
	public List<Coupon> affiliateCoupon(Affiliate affiliate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Coupon.class);
		criteria.add(Restrictions.eq("referrerUser", affiliate.getUser()));
		criteria.add(Restrictions.eq("alreadyUsed", 0L));
		return findByCriteria(criteria);
	}

}
