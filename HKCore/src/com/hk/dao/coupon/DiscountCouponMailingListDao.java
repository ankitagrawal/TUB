package com.hk.dao.coupon;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.coupon.DiscountCouponMailingList;

@SuppressWarnings("unchecked")
@Repository
public class DiscountCouponMailingListDao extends BaseDaoImpl {

    public DiscountCouponMailingList findByMobile(String mobile) {
        List<DiscountCouponMailingList> list = getSession().createQuery("from DiscountCouponMailingList d where d.mobile = :mobile").setString("mobile", mobile).list();
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    public DiscountCouponMailingList findByEmail(String email) {
        List<DiscountCouponMailingList> list = getSession().createQuery("from DiscountCouponMailingList d where d.email = :email").setString("email", email).list();
        return list == null || list.isEmpty() ? null : list.get(0);
    }

}
