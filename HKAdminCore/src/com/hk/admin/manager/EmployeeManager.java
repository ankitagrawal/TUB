package com.hk.admin.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.cache.RoleCache;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.discount.OfferConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.OfferManager;
import com.hk.pact.service.discount.CouponService;

@Component
public class EmployeeManager {

    /*
     * @Autowired private RoleDao roleDao;
     */

    @Autowired
    OfferManager  offerManager;

    @Autowired
    CouponService couponService;

    /*
     * @Autowired UserService userService;
     */
    /*
     * @Autowired private RoleService roleService;
     */

    public Coupon createEmpCoupon(User user, String code) {
        Coupon empCoupon = null;
        Role hkEmpRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_EMPLOYEE).getRole();
        // if (user.getRoles().contains(getRoleService().getRoleByName(RoleConstants.HK_EMPLOYEE)) && code != null &&
        // code.equals(OfferConstants.HK_EMPLOYEE_CODE)) {
        if (user.getRoles().contains(hkEmpRole) && code != null && code.equals(OfferConstants.HK_EMPLOYEE_CODE)) {
            Offer offer = getOfferManager().getOfferForEmployee();
            empCoupon = getCouponService().createCoupon(code, null, null, null, offer, null, true, null);
        }
        return empCoupon;
    }

    /*
     * public UserService getUserService() { return userService; } public void setUserService(UserService userService) {
     * this.userService = userService; }
     */

    /*
     * public RoleDao getRoleDao() { return roleDao; } public void setRoleDao(RoleDao roleDao) { this.roleDao = roleDao; }
     */

    public OfferManager getOfferManager() {
        return offerManager;
    }

    public void setOfferManager(OfferManager offerManager) {
        this.offerManager = offerManager;
    }

    public CouponService getCouponService() {
        return couponService;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }

    /*
     * public RoleService getRoleService() { return roleService; } public void setRoleService(RoleService roleService) {
     * this.roleService = roleService; }
     */

}
