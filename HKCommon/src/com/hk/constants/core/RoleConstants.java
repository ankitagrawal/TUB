package com.hk.constants.core;

public class RoleConstants {

    public static final String GOD                           = "GOD";
    public static final String ADMIN                         = "ADMIN";
    public static final String ROCKSTAR                      = "ROCKSTAR";
    public static final String HK_USER                       = "HK_USER";
    public static final String COUPON_BLOCKED                = "COUPON_BLOCKED";
    public static final String B2B_USER                      = "B2B_USER";
    public static final String PO_APPROVER                   = "PO_APPROVER";

    public static final String TEMP_USER                     = "TEMP_USER";
    public static final String HK_UNVERIFIED                 = "HKUNVERIFIED";
    public static final String HK_DEACTIVATED                = "HKDEACTIVE";
    public static final String HK_DELETED                    = "HKDELETE";
    public static final String HK_BLOCKED                    = "HKBLOCKED";
    public static final String COD_BLOCKED                   = "CODBLOCKED";
    public static final String OPS_MANAGER_L1                = "OPS_MANAGER_L1";
    public static final String OPS_MANAGER_L2                = "OPS_MANAGER_L2";
    public static final String OPS_MANAGER                   = "OPS_MANAGER";
    public static final String WH_EMPLOYEE                   = "WH_EMPLOYEE";
    public static final String WH_MANAGER                    = "WH_MANAGER";
    public static final String WH_MANAGER_L1                 = "WH_MANAGER_L1";
    public static final String CUSTOMER_SUPPORT              = "CUSTOMER_SUPPORT";
    public static final String CUSTOMER_SUPPORT_L2           = "CUSTOMER_SUPPORT_L2";
    public static final String CUSTOMER_SUPPORT_L3           = "CUSTOMER_SUPPORT_L3";
    public static final String CATEGORY_MANAGER_L1           = "CATEGORY_MANAGER_L1";
    public static final String CATEGORY_MANAGER_L2           = "CATEGORY_MANAGER_L2";
    public static final String CATEGORY_MANAGER              = "CATEGORY_MANAGER";
    public static final String SUPPLIER                      = "SUPPLIER";
    public static final String FINANCE                       = "FINANCE";
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
    public static final String ROLE_GROUP_ADMINS             = GOD + ", " + ADMIN + ", " + ROCKSTAR + ", " + OPS_MANAGER + ", " + CUSTOMER_SUPPORT + ", " + FINANCE + ", "
                                                                     + CATEGORY_MANAGER + ", " + TICKETADMIN + "," + REPORT_ADMIN;
    public static final String HK_AFFILIATE                  = "HK_AFFILIATE";
    public static final String HK_AFFILIATE_UNVERIFIED       = "HK_AFFILIATE_UNVERIFIED";
    public static final String AFFILIATE                     = HK_AFFILIATE_UNVERIFIED + ", " + HK_AFFILIATE;

    public static final String HK_IHO_USER                   = "HK_IHO_USER";
    public static final String MASTER_BUG_RESOLVER           = "MASTER_BUG_RESOLVER";
    public static final String HK_EMPLOYEE                   = "HK_EMPLOYEE";
    public static final String ROLE_GROUP_ACCOUNTING_INVOICE = GOD + ", " + ADMIN + ", " + FINANCE + ", " + ROLE_GROUP_LOGISTICS_ADMIN;

    public static final String  HK_AFFILIATE_MANAGER         = "HK_AFFILIATE_MANAGER";
    public static final String FINANCE_ADMIN                 ="FINANCE_ADMIN";
}
