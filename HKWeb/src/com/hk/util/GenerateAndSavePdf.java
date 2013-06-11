package com.hk.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;


public class GenerateAndSavePdf {
  private static Logger logger = LoggerFactory.getLogger(GenerateAndSavePdf.class);

  /*public static void main(String[] args) {
    (new GenerateAndSavePdf()).generatePdf("http://localhost:8080/healthkart/AccountingInvoice.action?accountingInvoice=uCrwhKCUAzI%3D", "C:\\Users\\Ajeet\\IdeaProjects\\HealthKartWork\\adminDownloads\\invoices\\20120221/R-4.html", "C:\\Users\\Ajeet\\IdeaProjects\\HealthKartWork\\adminDownloads\\invoices\\20120221/R-4.pdf");
  }

  public void generatePdf(String webUrl, String inputFileName, String outputFileName) {
    try {
      URL pageURL = new URL(webUrl);
      HttpURLConnection con = (HttpURLConnection) pageURL.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(pageURL
          .openStream()));
      BufferedWriter bw = new BufferedWriter(new FileWriter(inputFileName));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        bw.write(inputLine);
      }
      in.close();
      bw.flush();
      bw.close();

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      //  turn off external dtd loading, we don't need it
      dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE);
      dbf.setFeature("http://xml.org/sax/features/validation", Boolean.FALSE);
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(con.getInputStream());

      //  to add custom font we need to specify it in style

      Element style = doc.createElement("style");
       style.setTextContent(
         "body { font-family: \"Arial Unicode MS\"; }" +
       //  and page numbering
         "@page { @bottom-right { content: \"Page \" counter(page);} }"
       );

      //  and add it to <head>

      //Element root = doc.getDocumentElement();
      //root.getElementsByTagName("head").item(0).appendChild(style);

      //  we've got document, now let's render it

      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocument(doc, null);
      renderer.layout();
      renderer.createPDF(new FileOutputStream(outputFileName));
      renderer.finishPDF();

    } catch (Exception e) {
      logger.error("Error while creating pdf - " + outputFileName);
      e.printStackTrace();
    }
  }*/


}