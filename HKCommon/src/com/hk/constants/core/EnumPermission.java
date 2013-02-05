package com.hk.constants.core;



public enum EnumPermission {

  SUPER_LOGIN                                    (PermissionConstants.SUPER_LOGIN),  
  VIEW_ACTION_QUEUE                              (PermissionConstants.VIEW_ACTION_QUEUE),
    OPS_MANAGER_CUSA_UPDATE(PermissionConstants.OPS_MANAGER_CUSA_UPDATE),
    OPS_MANAGER_CUSA_VIEW(PermissionConstants.OPS_MANAGER_CUSA_VIEW),
    UPDATE_ACTION_QUEUE                            (PermissionConstants.UPDATE_ACTION_QUEUE),
  VIEW_DROP_SHIPPING_QUEUE                       (PermissionConstants.VIEW_DROP_SHIPPING_QUEUE),
  UPDATE_DROP_SHIPPING_QUEUE                     (PermissionConstants.UPDATE_DROP_SHIPPING_QUEUE),
  VIEW_SHIPMENT_QUEUE                            (PermissionConstants.VIEW_SHIPMENT_QUEUE),
  VIEW_SERVICE_QUEUE                             (PermissionConstants.VIEW_SERVICE_QUEUE),
  UPDATE_SERVICE_QUEUE                           (PermissionConstants.UPDATE_SERVICE_QUEUE),
  UPDATE_SHIPMENT_QUEUE                          (PermissionConstants.UPDATE_SHIPMENT_QUEUE),
  UPDATE_USER                                    (PermissionConstants.UPDATE_USER),
  CHANGE_USER_ROLES                              (PermissionConstants.CHANGE_USER_ROLES),
  UPDATE_ORDER                                   (PermissionConstants.UPDATE_ORDER),
  UPDATE_PAYMENT                                 (PermissionConstants.UPDATE_PAYMENT),
  CONFIRM_COD                                    (PermissionConstants.CONFIRM_COD),
  SEND_SHIPPING_MAILS                            (PermissionConstants.SEND_SHIPPING_MAILS),
  SEARCH_ORDERS                                  (PermissionConstants.SEARCH_ORDERS),
  SEARCH_SUBSCRIPTIONS                            (PermissionConstants.SEARCH_SUBSCRIPTIONS),
  UPDATE_SUBSCRIPTION                             (PermissionConstants.UPDATE_SUBSCRIPTION),
  SEARCH_USERS                                   (PermissionConstants.SEARCH_USERS),
  VIEW_NOTIFY_LIST                               (PermissionConstants.VIEW_NOTIFY_LIST),
  AWARD_REWARD_POINTS                            (PermissionConstants.AWARD_REWARD_POINTS),
  MODERATE_REWARD_POINTS                         (PermissionConstants.MODERATE_REWARD_POINTS),
  VIEW_PACKING_QUEUE                             (PermissionConstants.VIEW_PACKING_QUEUE),
  UPDATE_PACKING_QUEUE                           (PermissionConstants.UPDATE_PACKING_QUEUE),
  VIEW_COURIER_INFO                              (PermissionConstants.VIEW_COURIER_INFO),
  UPDATE_COURIER_INFO                            (PermissionConstants.UPDATE_COURIER_INFO),
  DOWNLOAD_COURIER_EXCEL                         (PermissionConstants.DOWNLOAD_COURIER_EXCEL),
  VIEW_SHIPMENT_AWAITING_QUEUE                   (PermissionConstants.VIEW_SHIPMENT_AWAITING_QUEUE),
  UPDATE_SHIPMENT_AWAIT_QUEUE                    (PermissionConstants.UPDATE_SHIPMENT_AWAIT_QUEUE),
  VIEW_SHIPPED_EMAIL_AWAIT_Q                     (PermissionConstants.VIEW_SHIPPED_EMAIL_AWAIT_Q),
  SKU_SALES_REPORT                               (PermissionConstants.SKU_SALES_REPORT),
  CATEGORY_PERFORMANCE_REPORTS                   (PermissionConstants.CATEGORY_PERFORMANCE_REPORTS),
  MASTER_PERFORMANCE_REPORTS                     (PermissionConstants.MASTER_PERFORMANCE_REPORTS),
  COD_PERFORMANCE_REPORTS                        (PermissionConstants.COD_PERFORMANCE_REPORTS),
  ACCOUNTING_SALES_REPORT                        (PermissionConstants.ACCOUNTING_SALES_REPORT),
  COURIER_DELIVERY_REPORTS                       (PermissionConstants.COURIER_DELIVERY_REPORTS),
  PERFORMANCE_REPORTS                            (PermissionConstants.PERFORMANCE_REPORTS),
  VIEW_RECONCILIATION_REPORTS                    (PermissionConstants.VIEW_RECONCILIATION_REPORTS),
  UPDATE_RECONCILIATION_REPORTS                  (PermissionConstants.UPDATE_RECONCILIATION_REPORTS),
  VIEW_DELIVERY_QUEUE                            (PermissionConstants.VIEW_DELIVERY_QUEUE),
  UPDATE_DELIVERY_QUEUE                          (PermissionConstants.UPDATE_DELIVERY_QUEUE),
  CRM_REPORTS                                    (PermissionConstants.CRM_REPORTS),

  VIEW_TICKETS                                   (PermissionConstants.VIEW_TICKETS),
  CREATE_TICKETS                                 (PermissionConstants.CREATE_TICKETS),

  SUPPLIER_MANAGEMENT                            (PermissionConstants.SUPPLIER_MANAGEMENT),
  PO_MANAGEMENT                                  (PermissionConstants.PO_MANAGEMENT),
  GRN_CREATION                                   (PermissionConstants.GRN_CREATION),
  CHANGE_WAREHOUSE                               (PermissionConstants.CHANGE_WAREHOUSE),
  INVENTORY_CHECKIN                              (PermissionConstants.INVENTORY_CHECKIN),
  INVENTORY_CHECKOUT                             (PermissionConstants.INVENTORY_CHECKOUT),

  SALES_REPORT                                   (PermissionConstants.SALES_REPORT),
  DETAILED_SALES_REPORT                          (PermissionConstants.DETAILED_SALES_REPORT),
  REPORT_ADMIN                                   (PermissionConstants.REPORT_ADMIN),
  SEND_SMS                                       (PermissionConstants.SEND_SMS),
  CREATE_OFFER                                   (PermissionConstants.CREATE_OFFER),
  UPDATE_OFFER                                   (PermissionConstants.UPDATE_OFFER),
  VIEW_OFFER                                     (PermissionConstants.VIEW_OFFER),
  CREATE_COUPON                                  (PermissionConstants.CREATE_COUPON),
  UPDATE_COUPON                                  (PermissionConstants.UPDATE_COUPON),
  VIEW_COUPON                                    (PermissionConstants.VIEW_COUPON),
  VERIFY_AFFILIATES                              (PermissionConstants.VERIFY_AFFILIATES),
  MANAGE_AFFILIATES                              (PermissionConstants.MANAGE_AFFILIATES),

  DOWNLOAD_PRDOUCT_CATALOG                       (PermissionConstants.DOWNLOAD_PRDOUCT_CATALOG),
  UPLOAD_PRODUCT_CATALOG                         (PermissionConstants.UPLOAD_PRODUCT_CATALOG),
  MANAGE_IMAGE                                   (PermissionConstants.MANAGE_IMAGE),
  UPDATE_PRODUCT_CATALOG                         (PermissionConstants.UPDATE_PRODUCT_CATALOG),
  UPDATE_SEO_METADATA                            (PermissionConstants.UPDATE_SEO_METADATA),
  UPDATE_PRODUCT_DESCRIPTIONS                    (PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS),

  COD_NOT_ALLOWED                                (PermissionConstants.COD_NOT_ALLOWED),
  GET_PRODUCT_LINK                               (PermissionConstants.GET_PRODUCT_LINK),
  ADD_LINEITEM                                   (PermissionConstants.ADD_LINEITEM),
  DELETE_LINEITEM                                (PermissionConstants.DELETE_LINEITEM),
  EDIT_LINEITEM                                  (PermissionConstants.EDIT_LINEITEM),
  HK_EMPLOYEE                                    (PermissionConstants.HK_EMPLOYEE),
  PURCHASE_INVOICE_MANAGEMENT                    (PermissionConstants.PURCHASE_INVOICE_MANAGEMENT),
  RECON_VOUCHER_MANAGEMENT                       (PermissionConstants.RECON_VOUCHER_MANAGEMENT),
  MARKETING_EXPENSE_MANAGEMENT                   (PermissionConstants.MARKETING_EXPENSE_MANAGEMENT),
  ASSIGN_BIN                                     (PermissionConstants.ASSIGN_BIN),
  SEND_MARKETING_MAILS                           (PermissionConstants.SEND_MARKETING_MAILS),
  CREATE_EDIT_SKU                                (PermissionConstants.CREATE_EDIT_SKU),
  MANAGE_PAYMENT_HISTORY                         (PermissionConstants.MANAGE_PAYMENT_HISTORY),
  FINANCE_MANAGEMENT                             (PermissionConstants.FINANCE_MANAGEMENT),
  RUN_ANT_BUILDS                                 (PermissionConstants.RUN_ANT_BUILDS),
  UPDATE_COURIER_DELIVERY_STATUS                 (PermissionConstants.UPDATE_COURIER_DELIVERY_STATUS),
  HK_DELIVERY_WORKSHEET_DOWNLOAD                 (PermissionConstants.HK_DELIVERY_WORKSHEET_DOWNLOAD),
  CREATE_REPLACEMENT_ORDER                       (PermissionConstants.CREATE_REPLACEMENT_ORDER),
  SELECT_HUB                                     (PermissionConstants.SELECT_HUB),
  VIEW_HUB                                       (PermissionConstants.VIEW_HUB),
  ADD_HK_DELIVERY_AGENT                          (PermissionConstants.ADD_HK_DELIVERY_AGENT),
  VIEW_CONSIGNMENT_TRACKING                      (PermissionConstants.VIEW_CONSIGNMENT_TRACKING),
  ADD_HUB                                        (PermissionConstants.ADD_HUB),
  EDIT_PAYMENT_HISTORY                           (PermissionConstants.EDIT_PAYMENT_HISTORY),
  POPULATE_BUSY_DATA                             (PermissionConstants.POPULATE_BUSY_DATA),
  DISPATCH_LOT_OPERATIONS                        (PermissionConstants.DISPATCH_LOT_OPERATIONS),
  EDIT_GRN	                                     (PermissionConstants.EDIT_GRN),
  CREATE_REVERSE_PICKUP_ORDER	                 (PermissionConstants.CREATE_REVERSE_PICKUP_ORDER);


  String permissionName;

  EnumPermission(String permissionName) {
    this.permissionName = permissionName;
  }

  public String getPermissionName() {
    return permissionName;
  }

}
