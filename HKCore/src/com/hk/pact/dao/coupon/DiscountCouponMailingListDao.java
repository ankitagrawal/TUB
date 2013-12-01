package com.hk.pact.dao.coupon;

import com.hk.domain.coupon.DiscountCouponMailingList;
import com.hk.pact.dao.BaseDao;

public interface DiscountCouponMailingListDao extends BaseDao {

    public DiscountCouponMailingList findByMobile(String mobile);

    public DiscountCouponMailingList findByEmail(String email);

}
