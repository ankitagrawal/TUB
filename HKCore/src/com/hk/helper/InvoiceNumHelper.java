package com.hk.helper;

public class InvoiceNumHelper {

    public static final String PREFIX_FOR_SERVICE_ORDER = "S";
    public static final String PREFIX_FOR_RETAIL_ORDER = "R";
    public static final String PREFIX_FOR_B2B_ORDER = "T";
    public static final String PREFIX_FOR_DEBIT_NOTE = "DN" ;

    /**
     * @param isServiceOrder
     * @param isB2bOrder
     * @return String -> R for Retail Orders : S for Service Orders : T for B2B orders
     */
    public static String getInvoiceType(boolean isServiceOrder, Boolean isB2bOrder) {

        if (isServiceOrder) {
            return PREFIX_FOR_SERVICE_ORDER;
        } else if (Boolean.TRUE.equals(isB2bOrder)) {
            return PREFIX_FOR_B2B_ORDER;
        }
        return PREFIX_FOR_RETAIL_ORDER;
    }


}
