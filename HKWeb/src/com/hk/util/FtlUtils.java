package com.hk.util;

import java.io.*;

public class FtlUtils {

  public static String awsBucket = "healthkart-pratham";

  public static File generateFtlFromHtml(File htmlFile, String destinationPath, String basicAmazonS3Path) throws IOException {
    String line;
    File ftlFile = new File(destinationPath);
    BufferedReader br = new BufferedReader(new FileReader(htmlFile));
    PrintWriter out = new PrintWriter(new FileWriter(ftlFile));
    while ((line = br.readLine()) != null) {
      line = addAbsolutePathsToFtl(line, basicAmazonS3Path);
      out.write(line + lineSeperator);
    }
    br.close();
    out.close();

    ftlFile = addExtraDataToFtl(ftlFile, basicAmazonS3Path);
    return ftlFile;
  }

  private static String addAbsolutePathsToFtl(String line, String basicAmazonS3Path) {
    line = line.replaceAll(imageSourceRegex, "$1$2" + basicAmazonS3Path + "$3");
    line = line.replaceAll(backgroundImageSourceRegex, "$1" + basicAmazonS3Path + "$2\"");
    return line;
  }

  private static File addExtraDataToFtl(File ftlFile, String basicAmazonS3Path) throws IOException {
    String line;
    BufferedReader br = new BufferedReader(new FileReader(ftlFile));
    PrintWriter out = new PrintWriter(new FileWriter(ftlFile));
    while ((line = br.readLine()) != null) {
      //adding "can't view this email and unsubsribe link" div to the ftl
      if (line.matches(bodyStartTagRegex)) {
        out.write(line + lineSeperator);
        out.write(FtlUtils.getCantViewEmailDiv(basicAmazonS3Path) + lineSeperator);
      } else if (line.matches(bodyEndTagRegex)) {
        out.write(FtlUtils.getUnsubscribeEmailDiv() + lineSeperator);
        out.write(line + lineSeperator);
        break;
      }
    }

    br.close();
    out.close();
    return ftlFile;
  }

  private static String getCantViewEmailDiv(String basicAmazonS3Path) {
    return "<div style=\"font-size:11px; text-align:center; color:#000000; padding:15px\">" +
        "Can't view this email? " +
        "<a href=\"" + basicAmazonS3Path + "emailer.html\">Click here</a> " +
        "to view a web version." +
        "</div>";
  }

  private static String getUnsubscribeEmailDiv() {
    return "<div align=\"center\" valign=\"middle\" style=\"border-top: solid #97b8ca 1px; font-size:11px; text-align:center; color:#666666; padding:10px\">" +
        " If you prefer not to receive HealthKart.com email, <a href=\"${unsubscribeLink}\">click here to Unsubscribe</a>" +
        "<br />  Parsvanath Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA<br />\n" +
        "    © 2011 HealthKart.com. All Rights Reserved. </div>";
  }

  private static final String lineSeperator = System.getProperty("line.separator");
  private static final String imageSourceRegex = "(<img .*)(src=\"(?!http))(.*\")";
  private static final String backgroundImageSourceRegex = "(background-image:url\\((?!http))(.*)\"";
  private static final String bodyStartTagRegex = "<body .*>";
  private static final String bodyEndTagRegex = "</body>";
}
