package com.hk.constants.core;

import java.util.Arrays;
import java.util.List;


public enum EnumRoleHasPermission {

    ADMIN(
            EnumRole.ADMIN,
            Arrays.asList(
                    EnumPermission.SUPER_LOGIN,
                    EnumPermission.CHANGE_USER_ROLES,
                    EnumPermission.VIEW_ACTION_QUEUE,
                    EnumPermission.UPDATE_ACTION_QUEUE,
                    EnumPermission.VIEW_NOTIFY_LIST,
                    EnumPermission.VIEW_PACKING_QUEUE,
                    EnumPermission.UPDATE_PACKING_QUEUE,
                    EnumPermission.VIEW_SHIPMENT_QUEUE,
                    EnumPermission.VIEW_SERVICE_QUEUE,
                    EnumPermission.UPDATE_SERVICE_QUEUE,
                    EnumPermission.UPDATE_SHIPMENT_QUEUE,
                    EnumPermission.VIEW_SHIPMENT_AWAITING_QUEUE,
                    EnumPermission.UPDATE_SHIPMENT_AWAIT_QUEUE,
                    EnumPermission.VIEW_SHIPPED_EMAIL_AWAIT_Q,
                    EnumPermission.VIEW_DELIVERY_QUEUE,
                    EnumPermission.UPDATE_DELIVERY_QUEUE,
                    EnumPermission.SKU_SALES_REPORT,
                    EnumPermission.CATEGORY_PERFORMANCE_REPORTS,
                    EnumPermission.COD_PERFORMANCE_REPORTS,
                    EnumPermission.ACCOUNTING_SALES_REPORT,
                    EnumPermission.COURIER_DELIVERY_REPORTS,
                    EnumPermission.VIEW_RECONCILIATION_REPORTS,
                    EnumPermission.UPDATE_RECONCILIATION_REPORTS,
                    EnumPermission.CRM_REPORTS,
                    EnumPermission.DOWNLOAD_COURIER_EXCEL,
                    EnumPermission.VIEW_COURIER_INFO,
                    EnumPermission.UPDATE_COURIER_INFO,
                    EnumPermission.MASTER_PERFORMANCE_REPORTS,
                    EnumPermission.PERFORMANCE_REPORTS,
                    EnumPermission.SUPPLIER_MANAGEMENT,
                    EnumPermission.PO_MANAGEMENT,
                    EnumPermission.INVENTORY_CHECKIN,
                    EnumPermission.INVENTORY_CHECKOUT,
                    EnumPermission.UPDATE_USER,
                    EnumPermission.UPDATE_ORDER,
                    EnumPermission.UPDATE_PAYMENT,
                    EnumPermission.CONFIRM_COD,
                    EnumPermission.SEND_SHIPPING_MAILS,
                    EnumPermission.SEND_SMS,
                    EnumPermission.SALES_REPORT,
                    EnumPermission.DETAILED_SALES_REPORT,
                    EnumPermission.REPORT_ADMIN,
                    EnumPermission.SEARCH_ORDERS,
                    EnumPermission.SEARCH_USERS,
                    EnumPermission.CREATE_OFFER,
                    EnumPermission.UPDATE_OFFER,
                    EnumPermission.VIEW_OFFER,
                    EnumPermission.VIEW_COUPON,
                    EnumPermission.UPDATE_COUPON,
                    EnumPermission.CREATE_COUPON,
                    EnumPermission.EDIT_LINEITEM,
                    EnumPermission.VIEW_TICKETS,
                    EnumPermission.CREATE_TICKETS,
                    EnumPermission.AWARD_REWARD_POINTS,
                    EnumPermission.MODERATE_REWARD_POINTS,
                    EnumPermission.DOWNLOAD_PRDOUCT_CATALOG,
                    EnumPermission.UPLOAD_PRODUCT_CATALOG,
                    EnumPermission.MANAGE_IMAGE,
                    EnumPermission.UPDATE_PRODUCT_CATALOG,
                    EnumPermission.UPDATE_PRODUCT_DESCRIPTIONS,
                    EnumPermission.UPDATE_SEO_METADATA,
                    EnumPermission.GET_PRODUCT_LINK,
                    EnumPermission.ADD_LINEITEM,
                    EnumPermission.DELETE_LINEITEM,
                    EnumPermission.VERIFY_AFFILIATES,
                    EnumPermission.MANAGE_AFFILIATES,
                    EnumPermission.HK_EMPLOYEE,
                    EnumPermission.GRN_CREATION,
                    EnumPermission.PURCHASE_INVOICE_MANAGEMENT,
                    EnumPermission.RECON_VOUCHER_MANAGEMENT,
                    EnumPermission.SEND_MARKETING_MAILS,
                    EnumPermission.ASSIGN_BIN,
                    EnumPermission.MARKETING_EXPENSE_MANAGEMENT,
                    EnumPermission.CREATE_EDIT_SKU,
                    EnumPermission.MANAGE_PAYMENT_HISTORY,
                    EnumPermission.FINANCE_MANAGEMENT,
                    EnumPermission.RUN_ANT_BUILDS,
                    EnumPermission.HK_DELIVERY_WORKSHEET_DOWNLOAD,
                    EnumPermission.UPDATE_COURIER_DELIVERY_STATUS
            )
    ),

    WH_MANAGER(
            EnumRole.WH_MANAGER,
            Arrays.asList(
                    EnumPermission.GRN_CREATION,
                    EnumPermission.RECON_VOUCHER_MANAGEMENT,
                    EnumPermission.SUPPLIER_MANAGEMENT
            )),

    WH_MANAGER_L1(
            EnumRole.WH_MANAGER_L1,
            Arrays.asList(
                    EnumPermission.CHANGE_WAREHOUSE
            )),

    OPS_MANAGER(
            EnumRole.OPS_MANAGER,
            Arrays.asList(
                    EnumPermission.SUPER_LOGIN,
                    EnumPermission.VIEW_ACTION_QUEUE,
                    EnumPermission.UPDATE_ACTION_QUEUE,
                    EnumPermission.VIEW_PACKING_QUEUE,
                    EnumPermission.UPDATE_PACKING_QUEUE,
                    EnumPermission.VIEW_SHIPMENT_QUEUE,
                    EnumPermission.UPDATE_SHIPMENT_QUEUE,
                    EnumPermission.VIEW_SHIPMENT_AWAITING_QUEUE,
                    EnumPermission.UPDATE_SHIPMENT_AWAIT_QUEUE,
                    EnumPermission.VIEW_SHIPPED_EMAIL_AWAIT_Q,
                    EnumPermission.VIEW_DELIVERY_QUEUE,
                    EnumPermission.UPDATE_DELIVERY_QUEUE,
                    EnumPermission.VIEW_COURIER_INFO,
                    EnumPermission.DOWNLOAD_COURIER_EXCEL,
                    EnumPermission.UPDATE_COURIER_INFO,
                    EnumPermission.COURIER_DELIVERY_REPORTS,
                    EnumPermission.MASTER_PERFORMANCE_REPORTS,
                    EnumPermission.VIEW_RECONCILIATION_REPORTS,
                    EnumPermission.UPDATE_RECONCILIATION_REPORTS,
                    EnumPermission.PERFORMANCE_REPORTS,
                    EnumPermission.PO_MANAGEMENT,
                    EnumPermission.INVENTORY_CHECKIN,
                    EnumPermission.INVENTORY_CHECKOUT,
                    EnumPermission.UPDATE_USER,
                    EnumPermission.UPDATE_ORDER,
                    EnumPermission.UPDATE_PAYMENT,
                    EnumPermission.SEND_SHIPPING_MAILS,
                    EnumPermission.SEARCH_ORDERS,
                    EnumPermission.SEARCH_USERS,
                    EnumPermission.VIEW_TICKETS,
                    EnumPermission.CREATE_TICKETS,
                    EnumPermission.REPORT_ADMIN,
                    EnumPermission.ASSIGN_BIN,
                    EnumPermission.HK_EMPLOYEE,
                    EnumPermission.CREATE_EDIT_SKU
            )
    ),

    OPS_MANAGER_L1(
            EnumRole.OPS_MANAGER_L1,
            Arrays.asList(
                    EnumPermission.SKU_SALES_REPORT,
                    EnumPermission.UPDATE_PRODUCT_CATALOG,
                    EnumPermission.DOWNLOAD_PRDOUCT_CATALOG,
                    EnumPermission.UPDATE_COURIER_DELIVERY_STATUS
            )
    ),
    CUSTOMER_SUPPORT(
            EnumRole.CUSTOMER_SUPPORT,
            Arrays.asList(
                    EnumPermission.SUPER_LOGIN,
                    EnumPermission.VIEW_NOTIFY_LIST,
                    EnumPermission.VIEW_ACTION_QUEUE,
                    EnumPermission.VIEW_SERVICE_QUEUE,
                    EnumPermission.VIEW_COURIER_INFO,
                    EnumPermission.UPDATE_USER,
                    EnumPermission.CHANGE_USER_ROLES,
                    EnumPermission.UPDATE_ORDER,
                    EnumPermission.UPDATE_PAYMENT,
                    EnumPermission.CONFIRM_COD,
                    EnumPermission.SEARCH_ORDERS,
                    EnumPermission.SEARCH_USERS,
                    EnumPermission.VIEW_TICKETS,
                    EnumPermission.CREATE_TICKETS,
                    EnumPermission.AWARD_REWARD_POINTS,
                    EnumPermission.HK_EMPLOYEE
            )
    ),

    CUSTOMER_SUPPORT_L2(
            EnumRole.CUSTOMER_SUPPORT_L2,
            Arrays.asList(
                    EnumPermission.CRM_REPORTS,
                    EnumPermission.VIEW_DELIVERY_QUEUE,
                    EnumPermission.UPDATE_DELIVERY_QUEUE,
                    EnumPermission.VIEW_OFFER,
                    EnumPermission.VIEW_COUPON,
                    EnumPermission.EDIT_LINEITEM
            )
    ),

    CUSTOMER_SUPPORT_L3(
            EnumRole.CUSTOMER_SUPPORT_L3,
            Arrays.asList(
                    EnumPermission.REPORT_ADMIN,
                    EnumPermission.MASTER_PERFORMANCE_REPORTS,
                    EnumPermission.COD_PERFORMANCE_REPORTS,
                    EnumPermission.SALES_REPORT,
                    EnumPermission.MODERATE_REWARD_POINTS,
                    EnumPermission.EDIT_LINEITEM
            )
    ),

    CATEGORY_MANAGER(
            EnumRole.CATEGORY_MANAGER,
            Arrays.asList(
                    EnumPermission.SUPER_LOGIN,
                    EnumPermission.CHANGE_USER_ROLES,
                    EnumPermission.UPDATE_ORDER,
                    EnumPermission.SUPPLIER_MANAGEMENT,
                    EnumPermission.VIEW_NOTIFY_LIST,
                    EnumPermission.PO_MANAGEMENT,
                    EnumPermission.INVENTORY_CHECKIN,
                    EnumPermission.INVENTORY_CHECKOUT,
                    EnumPermission.VIEW_ACTION_QUEUE,
                    EnumPermission.UPDATE_ACTION_QUEUE,
                    EnumPermission.VIEW_PACKING_QUEUE,
                    EnumPermission.UPDATE_PACKING_QUEUE,
                    EnumPermission.VIEW_SERVICE_QUEUE,
                    EnumPermission.UPDATE_SERVICE_QUEUE,
                    EnumPermission.SEARCH_ORDERS,
                    EnumPermission.SEARCH_USERS,
                    EnumPermission.AWARD_REWARD_POINTS,
                    EnumPermission.VIEW_OFFER,
                    EnumPermission.VIEW_COUPON,
                    EnumPermission.DOWNLOAD_PRDOUCT_CATALOG,
                    EnumPermission.UPLOAD_PRODUCT_CATALOG,
                    EnumPermission.MANAGE_IMAGE,
                    EnumPermission.VIEW_TICKETS,
                    EnumPermission.CREATE_TICKETS,
                    EnumPermission.UPDATE_PRODUCT_CATALOG,
                    EnumPermission.UPDATE_PRODUCT_DESCRIPTIONS,
                    EnumPermission.MASTER_PERFORMANCE_REPORTS,
                    EnumPermission.PERFORMANCE_REPORTS,
                    EnumPermission.CATEGORY_PERFORMANCE_REPORTS,
                    EnumPermission.COD_PERFORMANCE_REPORTS,
                    EnumPermission.REPORT_ADMIN,
                    EnumPermission.SKU_SALES_REPORT,
                    EnumPermission.VERIFY_AFFILIATES,
                    EnumPermission.MANAGE_AFFILIATES,
                    EnumPermission.HK_EMPLOYEE,
                    EnumPermission.CREATE_EDIT_SKU,
                    EnumPermission.EDIT_LINEITEM,
                    EnumPermission.MANAGE_PAYMENT_HISTORY
            )
    ),

    BASIC_ROLES(
            EnumRole.BASIC_ROLES,
            Arrays.asList(
                    EnumPermission.SEARCH_ORDERS,
                    EnumPermission.SEARCH_USERS
            )
    ),

    SITE_CONTENT_MANAGER(
            EnumRole.SITE_CONTENT_MANAGER,
            Arrays.asList(
                    EnumPermission.UPDATE_PRODUCT_DESCRIPTIONS,
                    EnumPermission.UPDATE_SEO_METADATA,
                    EnumPermission.HK_EMPLOYEE
            )
    ),

    REPORT_ADMIN(
            EnumRole.REPORT_ADMIN,
            Arrays.asList(
                    EnumPermission.REPORT_ADMIN,
                    EnumPermission.MASTER_PERFORMANCE_REPORTS,
                    EnumPermission.PERFORMANCE_REPORTS,
                    EnumPermission.CATEGORY_PERFORMANCE_REPORTS,
                    EnumPermission.SKU_SALES_REPORT,
                    EnumPermission.CRM_REPORTS,
                    EnumPermission.ACCOUNTING_SALES_REPORT,
                    EnumPermission.COURIER_DELIVERY_REPORTS,
                    EnumPermission.UPDATE_RECONCILIATION_REPORTS,
                    EnumPermission.VIEW_RECONCILIATION_REPORTS,
                    EnumPermission.DETAILED_SALES_REPORT,
                    EnumPermission.SALES_REPORT
            )
    ),

    RECONCILIATION(
            EnumRole.RECONCILIATION,
            Arrays.asList(
                    EnumPermission.REPORT_ADMIN,
                    EnumPermission.COURIER_DELIVERY_REPORTS,
                    EnumPermission.UPDATE_RECONCILIATION_REPORTS,
                    EnumPermission.VIEW_RECONCILIATION_REPORTS
            )
    ),

    FINANCE(
            EnumRole.FINANCE,
            Arrays.asList(
                    EnumPermission.REPORT_ADMIN,
                    EnumPermission.ACCOUNTING_SALES_REPORT,
                    EnumPermission.UPDATE_RECONCILIATION_REPORTS,
                    EnumPermission.VIEW_RECONCILIATION_REPORTS,
                    EnumPermission.PURCHASE_INVOICE_MANAGEMENT,
                    EnumPermission.MANAGE_PAYMENT_HISTORY
            )
    ),

    MARKETING(
            EnumRole.MARKETING,
            Arrays.asList(
                    EnumPermission.CREATE_OFFER,
                    EnumPermission.UPDATE_OFFER,
                    EnumPermission.VIEW_OFFER,
                    EnumPermission.VIEW_COUPON,
                    EnumPermission.UPDATE_COUPON,
                    EnumPermission.CREATE_COUPON,
                    EnumPermission.MARKETING_EXPENSE_MANAGEMENT,
                    EnumPermission.SEND_MARKETING_MAILS
            )
    ),

    HK_AFFILIATE(
            EnumRole.HK_AFFILIATE,
            Arrays.asList(
                    EnumPermission.GET_PRODUCT_LINK
            )
    ),

    COD_BLOCKED(
            EnumRole.COD_BLOCKED,
            Arrays.asList(
                    EnumPermission.COD_NOT_ALLOWED
            )
    ),

    MASTER_BUG_RESOLVER(
            EnumRole.MASTER_BUG_RESOLVER,
            Arrays.asList(
                    EnumPermission.ADD_LINEITEM,
                    EnumPermission.DELETE_LINEITEM,
                    EnumPermission.EDIT_LINEITEM
            )
    ),

    CATEGORY_MANAGER_L1(
            EnumRole.CATEGORY_MANAGER_L1,
            Arrays.asList(
                    EnumPermission.VIEW_DELIVERY_QUEUE,
                    EnumPermission.UPDATE_DELIVERY_QUEUE
            )
    ),

    HK_AFFILIATE_MANAGER(
            EnumRole.HK_AFFILIATE_MANAGER,
            Arrays.asList(
                    EnumPermission.VERIFY_AFFILIATES,
                    EnumPermission.MANAGE_AFFILIATES
            )
    ),
    FINANCE_MANAGEMENT(
            EnumRole.FINANCE_ADMIN,
            Arrays.asList(
                    EnumPermission.REPORT_ADMIN,
                    EnumPermission.ACCOUNTING_SALES_REPORT,
                    EnumPermission.UPDATE_RECONCILIATION_REPORTS,
                    EnumPermission.VIEW_RECONCILIATION_REPORTS,
                    EnumPermission.PURCHASE_INVOICE_MANAGEMENT,
                    EnumPermission.MANAGE_PAYMENT_HISTORY,
                    EnumPermission.FINANCE_MANAGEMENT
            )
    ),

    DEVELOPER_TASKS(
            EnumRole.DEVELOPER,
            Arrays.asList(
                    EnumPermission.RUN_ANT_BUILDS
            )
    ),
    HK_DELIVERY_GUY(
            EnumRole.HK_DELIVERY_GUY,
            Arrays.asList(
                    EnumPermission.HK_DELIVERY_WORKSHEET_DOWNLOAD,
                    EnumPermission.VIEW_DELIVERY_QUEUE,
                    EnumPermission.DOWNLOAD_COURIER_EXCEL,
                    EnumPermission.UPDATE_DELIVERY_QUEUE
            )
    );

    EnumRole enumRole;
    List<EnumPermission> enumPermissions;

    EnumRoleHasPermission(EnumRole enumRole, List<EnumPermission> enumPermissions) {
        this.enumRole = enumRole;
        this.enumPermissions = enumPermissions;
    }

    public List<EnumPermission> getEnumPermissions() {
        return enumPermissions;
    }

    public String getRoleName() {
        return enumRole.getRoleName();
    }

}
