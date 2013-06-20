package com.hk.constants.core;

import java.util.List;
import java.util.Arrays;

public class RoleConstants {

    public static final String GOD                           = "GOD";
    public static final String ADMIN                         = "ADMIN";
    public static final String ROCKSTAR                      = "ROCKSTAR";
    public static final String ROLE_MANAGER                  = "ROLE_MANAGER";
    public static final String HK_USER                       = "HK_USER";
    public static final String COUPON_BLOCKED                = "COUPON_BLOCKED";
    public static final String B2B_USER                      = "B2B_USER";
    public static final String FITNESSPRO_USER               = "FITNESSPRO_USER";
    public static final String PO_APPROVER                   = "PO_APPROVER";

    public static final String TEMP_USER                     = "TEMP_USER";
    public static final String HK_UNVERIFIED                 = "HKUNVERIFIED";
    public static final String HK_DEACTIVATED                = "HKDEACTIVE";
    public static final String HK_DELETED                    = "HKDELETE";
    public static final String HK_BLOCKED                    = "HKBLOCKED";
    public static final String COD_BLOCKED                   = "CODBLOCKED";
    public static final String OPS_MANAGER_L1                = "OPS_MANAGER_L1";
    public static final String OPS_MANAGER_L2                = "OPS_MANAGER_L2";
    public static final String OPS_MANAGER_L3                = "OPS_MANAGER_L3";
    public static final String OPS_MANAGER                   = "OPS_MANAGER";
    public static final String WH_EMPLOYEE                   = "WH_EMPLOYEE";
    public static final String WH_MANAGER                    = "WH_MANAGER";
    public static final String WH_MANAGER_L1                 = "WH_MANAGER_L1";
    public static final String CUSTOMER_SUPPORT              = "CUSTOMER_SUPPORT";
    public static final String CUSTOMER_SUPPORT_L2           = "CUSTOMER_SUPPORT_L2";
    public static final String CUSTOMER_SUPPORT_L3           = "CUSTOMER_SUPPORT_L3";
    public static final String CUSTOMER_SUPPORT_L3_ADMINS    = CUSTOMER_SUPPORT_L3 + ", " + ADMIN;
	public static final String CUSTOMER_SUPPORT_REVERSE_PICKUP = "CUSTOMER_SUPPORT_REVERSE_PICKUP";
    public static final String CATEGORY_MANAGER_L1           = "CATEGORY_MANAGER_L1";
    public static final String CATEGORY_MANAGER_L2           = "CATEGORY_MANAGER_L2";
    public static final String CATEGORY_MANAGER              = "CATEGORY_MANAGER";
    public static final String SUPPLIER                      = "SUPPLIER";
    public static final String FINANCE                       = "FINANCE";
    public static final String HR                            = "HR";
    public static final String MARKETING                     = "MARKETING";
    public static final String TICKETADMIN                   = "TICKETADMIN";
    public static final String SITE_CONTENT_MANAGER          = "SITE_CONTENT_MANAGER";
    public static final String REPORT_ADMIN                  = "REPORT_ADMIN";
    public static final String BASIC_ROLES                   = "BASIC_ROLES";
    public static final String RECONCILIATION                = "RECONCILIATION";
    public static final String UNSUBSCRIBED_USER             = "UNSUBSCRIBED_USER";
    public static final String ROLE_GROUP_USER_OR_UNVERIFIED = TEMP_USER + ", " + HK_UNVERIFIED;
    public static final String ROLE_GROUP_TEMP_OR_UNVERIFIED = HK_USER + ", " + HK_UNVERIFIED;
    public static final String ROLE_GROUP_NEEDS_ACTIVATION   = HK_DEACTIVATED + ", " + HK_UNVERIFIED;
    public static final String ROLE_GROUP_CATMAN_ADMIN       = CATEGORY_MANAGER + ", " + ADMIN;
    public static final String ROLE_GROUP_MULTIPLE_WAREHOUSE = CATEGORY_MANAGER + ", " + ADMIN + ", " + WH_MANAGER_L1;
    public static final String ROLE_GROUP_LOGISTICS_ADMIN    = OPS_MANAGER + ", " + ADMIN;

    public static final String DEVELOPER                     = "DEVELOPER";
    public static final String HK_DELIVERY_GUY               = "HK_DELIVERY_GUY";
    public static final String PAYMENT_MANAGER               = "PAYMENT_MANAGER";
    public static final String SPLITTER               = "SPLITTER";
    public static final String HK_DELIVERY_ADMIN             = "HK_DELIVERY_ADMIN";
    public static final String HK_DELIVERY_HUB_MANAGER       = "HK_DELIVERY_HUB_MANAGER";
    public static final String ROLE_GROUP_HK_DELIVERY_ADMIN  = HK_DELIVERY_GUY+","+HK_DELIVERY_ADMIN+","+HK_DELIVERY_HUB_MANAGER;


    public static final String ROLE_GROUP_ADMINS             = GOD + ", " + ADMIN + ", " + ROCKSTAR + ", " + OPS_MANAGER + ", " + CUSTOMER_SUPPORT + ", " + FINANCE + ", "
            + CATEGORY_MANAGER + ", " + TICKETADMIN + "," + REPORT_ADMIN +"," +BASIC_ROLES+","+ROLE_GROUP_HK_DELIVERY_ADMIN;
    public static final List<String> ROLE_GROUP_ADMINS_LIST = Arrays.asList(GOD, ADMIN, ROCKSTAR, OPS_MANAGER, CUSTOMER_SUPPORT, FINANCE,
        CATEGORY_MANAGER, TICKETADMIN, REPORT_ADMIN, BASIC_ROLES, ROLE_GROUP_HK_DELIVERY_ADMIN);

    public static final String HK_AFFILIATE                  = "HK_AFFILIATE";
    public static final String HK_AFFILIATE_UNVERIFIED       = "HK_AFFILIATE_UNVERIFIED";
    public static final String AFFILIATE                     = HK_AFFILIATE_UNVERIFIED + ", " + HK_AFFILIATE;

    public static final String HK_IHO_USER                   = "HK_IHO_USER";
    public static final String MASTER_BUG_RESOLVER           = "MASTER_BUG_RESOLVER";
    public static final String HK_EMPLOYEE                   = "HK_EMPLOYEE";
    public static final String ROLE_GROUP_ACCOUNTING_INVOICE = GOD + ", " + ADMIN + ", " + FINANCE + ", " + ROLE_GROUP_LOGISTICS_ADMIN;
    public static final String ACTION_QUEUE = "ACTION_QUEUE";
    public static final String ACTION_QUEUE_MANAGER = GOD + ", " + ADMIN + ", " + ACTION_QUEUE;

    public static final String HK_AFFILIATE_MANAGER          = "HK_AFFILIATE_MANAGER";
    public static final String FINANCE_ADMIN                 ="FINANCE_ADMIN";
    public static final String REWARD_POINT_MANAGER_L1       = "REWARD_POINT_MANAGER_L1";
    public static final String REWARD_POINT_MANAGER_L2       = "REWARD_POINT_MANAGER_L2";

    public static final String STORE_MANAGER                 = "STORE_MANAGER";
    public static final String HK_LOYALTY_USER               = "HK_LOYALTY_USER";
    public static final String NOTIFY_ME_EMAIL               ="NOTIFY_ME_EMAIL";
    public static final String B2B_ROLE               ="B2B_ROLE";
}
