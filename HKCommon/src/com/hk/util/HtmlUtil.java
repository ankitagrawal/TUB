package com.hk.util;

public class HtmlUtil {

    @SuppressWarnings( { "ConstantConditions" })
    public static String convertNewLineToBr(String str) {
        if (str == null)
            return null;
        return str.replaceAll("\n", "<br/>");
    }

}
