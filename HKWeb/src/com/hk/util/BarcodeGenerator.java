package com.hk.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class BarcodeGenerator {

  
  //@Named(Keys.Env.barcodeDir)
  String barcodeDir;

  public static Logger logger = LoggerFactory.getLogger(BarcodeGenerator.class);

  public String getBarcodePath(String gatewayOrderId) {
    String barcodeFilePath = barcodeDir + "/" + gatewayOrderId + ".png";
    try {
      Code128Bean bean = new Code128Bean();
      final int dpi = 150;

      //Configure the barcode generator
      bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar
      //width exactly one pixel
      //bean.setWideFactor(3);
      bean.setHeight(7.5);
      bean.doQuietZone(false);

      //Open output file
      File outputFile = new File(barcodeFilePath);
      OutputStream out = new FileOutputStream(outputFile);
      try {
        //Set up the canvas provider for monochrome PNG output
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
            out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

        //Generate the barcode
        bean.generateBarcode(canvas, gatewayOrderId);

        //Signal end of generation
        canvas.finish();
      } finally {
        out.close();
      }
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return barcodeFilePath;
  }

}
