




package com.hk.admin.util;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;

@Component
public class BarcodeGenerator {

    @Value("#{hkEnvProps['" + Keys.Env.barcodeDir + "']}")
    String               barcodeDir;

    public static Logger logger = LoggerFactory.getLogger(BarcodeGenerator.class);


    public static void main(String[] args) {
        new BarcodeGenerator().getBarcodePath("psq1334w73472dfsk",1.5f, 225, false);
    }

    public String getBarcodePath(String barcodeString, float inch, int dpi, boolean useFontSize) {
        String barcodeFilePath = barcodeDir + "/" + barcodeString + ".png";
        try {
            Code128Bean bean = new Code128Bean();
//            final int dpi = 150;

            // Configure the barcode generator
            bean.setModuleWidth(UnitConv.in2mm(inch / dpi)); // makes the narrow bar
            // width exactly one pixel
            // bean.setWideFactor(3);
            bean.setHeight(7.5);
            bean.doQuietZone(false);
            if(useFontSize){
                bean.setFontSize(Double.parseDouble("2"));
            }

            // Open output file
            File outputFile = new File(barcodeFilePath);
            OutputStream out = new FileOutputStream(outputFile);
            try {
                // Set up the canvas provider for monochrome PNG output
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

                // Generate the barcode
                bean.generateBarcode(canvas, barcodeString);

                // Signal end of generation
                canvas.finish();
            } finally {
                out.close();
            }
        } catch (IOException e) {
            logger.error("Error while generating/fetching barcode", e);
            
        }

        return barcodeFilePath;
    }

}
