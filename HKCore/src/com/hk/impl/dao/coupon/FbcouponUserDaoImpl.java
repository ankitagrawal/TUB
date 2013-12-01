package com.hk.impl.dao.coupon;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.coupon.FbcouponUser;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.coupon.FbcouponUserDao;

@Repository
public class FbcouponUserDaoImpl extends BaseDaoImpl implements FbcouponUserDao {

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
