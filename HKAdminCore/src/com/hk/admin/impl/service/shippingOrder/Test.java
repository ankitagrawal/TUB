package com.hk.admin.impl.service.shippingOrder;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;

import com.hk.constants.core.Keys;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Oct 8, 2012
 * Time: 11:27:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    @Value("#{hkEnvProps['" + Keys.Env.barcodeDir + "']}")
    static String               barcodeDir;

    public static void main (String args[]){
        String code = "231221";
        String awbNo = "12313312313";
        String barcodeString = "232131";
        String barcodeFilePath = barcodeDir + "/" + code + "-R-" + awbNo + "|" + barcodeString + ".png";
        try{
         File outputFile = new File(barcodeFilePath);
            OutputStream out = new FileOutputStream(outputFile);
        }
        catch(Exception e){

        }
    }
}
