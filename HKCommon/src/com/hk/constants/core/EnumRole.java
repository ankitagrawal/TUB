package com.hk.constants.core;

import com.hk.domain.user.Role;

import java.util.Arrays;
import java.util.List;

public enum EnumRole {

    GOD(RoleConstants.GOD),
    PO_APPROVER(RoleConstants.PO_APPROVER),
    ROCKSTAR(RoleConstants.ROCKSTAR),
    ADMIN(RoleConstants.ADMIN),
    HK_USER(RoleConstants.HK_USER),
    COUPON_BLOCKED(RoleConstants.COUPON_BLOCKED),
    B2B_USER(RoleConstants.B2B_USER),
    FITNESSPRO_USER(RoleConstants.FITNESSPRO_USER),
    HK_IHO_USER(RoleConstants.HK_IHO_USER),
    TEMP_USER(RoleConstants.TEMP_USER),
    HK_UNVERIFIED(RoleConstants.HK_UNVERIFIED),
    HK_DEACTIVATED(RoleConstants.HK_DEACTIVATED),
    HK_BLOCKED(RoleConstants.HK_BLOCKED),
    HK_DELETED(RoleConstants.HK_DELETED),
    COD_BLOCKED(RoleConstants.COD_BLOCKED),
    BASIC_ROLES(RoleConstants.BASIC_ROLES),
    RECONCILIATION(RoleConstants.RECONCILIATION),
    OPS_MANAGER_L1(RoleConstants.OPS_MANAGER_L1),
    OPS_MANAGER_L2(RoleConstants.OPS_MANAGER_L2),
    OPS_MANAGER(RoleConstants.OPS_MANAGER),
    WH_EMPLOYEE(RoleConstants.WH_EMPLOYEE),
    WH_MANAGER(RoleConstants.WH_MANAGER),
    WH_MANAGER_L1(RoleConstants.WH_MANAGER_L1),
    CUSTOMER_SUPPORT(RoleConstants.CUSTOMER_SUPPORT),
    CUSTOMER_SUPPORT_L2(RoleConstants.CUSTOMER_SUPPORT_L2),
    REWARD_POINT_MANAGER_L1(RoleConstants.REWARD_POINT_MANAGER_L1),
    REWARD_POINT_MANAGER_L2(RoleConstants.REWARD_POINT_MANAGER_L2),
    CUSTOMER_SUPPORT_L3(RoleConstants.CUSTOMER_SUPPORT_L3),
    CATEGORY_MANAGER_L1(RoleConstants.CATEGORY_MANAGER_L1),
    CATEGORY_MANAGER_L2(RoleConstants.CATEGORY_MANAGER_L2),
    CATEGORY_MANAGER(RoleConstants.CATEGORY_MANAGER),
    SUPPLIER(RoleConstants.SUPPLIER),
    TICKETADMIN(RoleConstants.TICKETADMIN),
    SITE_CONTENT_MANAGER(RoleConstants.SITE_CONTENT_MANAGER),
    REPORT_ADMIN(RoleConstants.REPORT_ADMIN),
    FINANCE(RoleConstants.FINANCE),
    MARKETING(RoleConstants.MARKETING),
    UNSUBSCRIBED_USER(RoleConstants.UNSUBSCRIBED_USER),
    HK_AFFILIATE(RoleConstants.HK_AFFILIATE),
    HK_AFFILIATE_UNVERIFIED(RoleConstants.HK_AFFILIATE_UNVERIFIED),
    MASTER_BUG_RESOLVER(RoleConstants.MASTER_BUG_RESOLVER),
    HK_EMPLOYEE(RoleConstants.HK_EMPLOYEE),
    HK_AFFILIATE_MANAGER(RoleConstants.HK_AFFILIATE_MANAGER),
    FINANCE_ADMIN(RoleConstants.FINANCE_ADMIN),
    DEVELOPER(RoleConstants.DEVELOPER),
    PAYMENT_MANAGER(RoleConstants.PAYMENT_MANAGER),
    SPLITTER(RoleConstants.SPLITTER),
    HR(RoleConstants.HR),
    HK_DELIVERY_GUY(RoleConstants.HK_DELIVERY_GUY),
    HK_DELIVERY_ADMIN(RoleConstants.HK_DELIVERY_ADMIN),
    HK_DELIVERY_HUB_MANAGER(RoleConstants.HK_DELIVERY_HUB_MANAGER),
    STORE_MANAGER(RoleConstants.STORE_MANAGER),
    OPS_MANAGER_COURIER_PRICING_UPDATE(RoleConstants.OPS_MANAGER_COURIER_PRICING_UPDATE),
    OPS_MANAGER_COURIER_PRICING_VIEW(RoleConstants.OPS_MANAGER_COURIER_PRICING_VIEW) ,
    NEFT_DEPOSIT(RoleConstants.NEFT_DEPOSIT)
    ;


    String roleName;

    EnumRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static List<String> listBasicRoles() {
        return Arrays.asList(
                EnumRole.COD_BLOCKED.getRoleName(),
                EnumRole.COUPON_BLOCKED.getRoleName(),
                EnumRole.HK_BLOCKED.getRoleName(),
                EnumRole.HK_DEACTIVATED.getRoleName(),
                EnumRole.HK_DELETED.getRoleName(),
                EnumRole.HK_UNVERIFIED.getRoleName(),
                EnumRole.TEMP_USER.getRoleName(),
                EnumRole.UNSUBSCRIBED_USER.getRoleName(),
                EnumRole.HK_USER.getRoleName(),
                EnumRole.HK_IHO_USER.getRoleName());
    }

    public static List<String> listCustomerSupportRoles() {
        return Arrays.asList(
                EnumRole.COD_BLOCKED.getRoleName(),
                EnumRole.CUSTOMER_SUPPORT.getRoleName(),
                EnumRole.CUSTOMER_SUPPORT_L2.getRoleName(),
                EnumRole.SPLITTER.getRoleName(),
                EnumRole.REWARD_POINT_MANAGER_L1.getRoleName(),
                EnumRole.REWARD_POINT_MANAGER_L2.getRoleName(),
                EnumRole.COUPON_BLOCKED.getRoleName(),
                EnumRole.HK_BLOCKED.getRoleName(),
                EnumRole.HK_DEACTIVATED.getRoleName(),
                EnumRole.HK_DELETED.getRoleName(),
                EnumRole.HK_UNVERIFIED.getRoleName(),
                EnumRole.TEMP_USER.getRoleName(),
                EnumRole.UNSUBSCRIBED_USER.getRoleName(),
                EnumRole.HK_USER.getRoleName(),
                EnumRole.HK_IHO_USER.getRoleName(),
                EnumRole.NEFT_DEPOSIT.getRoleName());
    }

    public static List<String> listOpsRoles() {
        return Arrays.asList(
                EnumRole.HK_USER.getRoleName(),
                EnumRole.HK_DELIVERY_GUY.getRoleName(),
                EnumRole.BASIC_ROLES.getRoleName(),
                EnumRole.TICKETADMIN.getRoleName(),
                EnumRole.OPS_MANAGER.getRoleName(),
                EnumRole.WH_EMPLOYEE.getRoleName(),
                EnumRole.WH_MANAGER.getRoleName(),
                EnumRole.WH_MANAGER_L1.getRoleName(),
                EnumRole.HK_DELIVERY_HUB_MANAGER.getRoleName());
    }

    public static List<String> listHRRoles() {
        return Arrays.asList(
                EnumRole.HK_USER.getRoleName(),
                EnumRole.HK_EMPLOYEE.getRoleName(),
                EnumRole.BASIC_ROLES.getRoleName(),
                EnumRole.TICKETADMIN.getRoleName(),
                EnumRole.WH_EMPLOYEE.getRoleName(),
                EnumRole.WH_MANAGER.getRoleName());
    }

    public Role toRole() {
        Role role = new Role();
        role.setName(this.getRoleName());
        return role;
    }
}
