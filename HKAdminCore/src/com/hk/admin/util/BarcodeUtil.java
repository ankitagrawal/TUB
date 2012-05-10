package com.hk.admin.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: user Date: Mar 9, 2012 Time: 5:13:41 PM To change this template use File | Settings |
 * File Templates.
 */
public class BarcodeUtil {

    public static final String BARCODE_SKU_GROUP_PREFIX = "HK-INVN-";

    public static String generateBarCodeForSKuGroup(Long skuGroupId) {
        return BARCODE_SKU_GROUP_PREFIX + skuGroupId;
    }

    public static void createBarcodeFile(String barcodeFilePath, String data) throws IOException {
        File printBarcode = new File(barcodeFilePath);
        if (!printBarcode.exists()) {
            printBarcode.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(barcodeFilePath, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.append(data);
        bufferedWriter.append("\r\n");
        bufferedWriter.close();
    }
}
