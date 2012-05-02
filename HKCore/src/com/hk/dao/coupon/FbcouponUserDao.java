package com.hk.dao.coupon;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.coupon.FbcouponUser;

@Repository
public class FbcouponUserDao extends BaseDaoImpl {

    @Transactional
    public FbcouponUser save(FbcouponUser user) {
        if (user != null) {
            if (user.getCreateDate() == null)
                user.setCreateDate(BaseUtils.getCurrentTimestamp());
            user.setUpdateDate(BaseUtils.getCurrentTimestamp());
        }

        return (FbcouponUser) super.save(user);
    }

    public FbcouponUser findByFbuidAndAppId(String fbuid, String appId) {
        Criteria criteria = getSession().createCriteria(FbcouponUser.class);
        criteria.add(Restrictions.eq("fbuid", fbuid));
        criteria.add(Restrictions.eq("appId", appId));
        return (FbcouponUser) criteria.uniqueResult();
    }
}
