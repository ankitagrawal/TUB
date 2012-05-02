package com.hk.helper;

public class InvoiceNumHelper {

    private static final String SERVICE_INVOICE_IDENTIFIER = "S";
    private static final String B2B_INVOICE_IDENTIFIER = "T";
    private static final String RETAIL_INVOICE_IDENTIFIER = "R";

    /**
     * @param isServiceOrder
     * @param isB2bOrder
     * @return String -> R for Retail Orders : S for Service Orders : T for B2B orders
     */
    public static String getInvoiceType(boolean isServiceOrder, Boolean isB2bOrder) {

        if (isServiceOrder) {
            return SERVICE_INVOICE_IDENTIFIER;
        } else if (Boolean.TRUE.equals(isB2bOrder)) {
            return B2B_INVOICE_IDENTIFIER;
        }
        return RETAIL_INVOICE_IDENTIFIER;
    }
}
