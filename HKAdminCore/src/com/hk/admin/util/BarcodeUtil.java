package com.hk.admin.util;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Mar 9, 2012
 * Time: 5:13:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class BarcodeUtil {

  public static final String BARCODE_SKU_GROUP_PREFIX = "HK-INVN-";


  public static String generateBarCodeForSKuGroup(Long skuGroupId) {
    return BARCODE_SKU_GROUP_PREFIX + skuGroupId;
  }
}
