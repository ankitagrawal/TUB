package com.hk.util;

import com.hk.constants.core.Keys;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

public class FtlUtils {
  private static Logger logger = LoggerFactory.getLogger(FtlUtils.class);

    @Value("#{hkEnvProps['" + Keys.Env.bucket + "']}")
    static
    String awsBucket;

    public static File generateFtlFromHtml(File htmlFile, String destinationPath, String contentFolderName) {
    String basicAmazonS3Path = getBasicAmazonS3Path(contentFolderName);
    String line;
    File ftlFile = new File(destinationPath);

    BufferedReader br = null;
    PrintWriter out = null;
    try {
      br = new BufferedReader(new FileReader(htmlFile));
      out = new PrintWriter(new FileWriter(ftlFile));
      while ((line = br.readLine()) != null) {
        line = addAbsolutePathsToFtl(line, basicAmazonS3Path);
        //adding "can't view this email and unsubsribe link" div to the ftl
        if (line.matches(bodyStartTagRegex)) {
          out.write(line + lineSeperator);
          out.write(FtlUtils.getCantViewEmailDiv(basicAmazonS3Path) + lineSeperator);
        } else if (line.matches(bodyEndTagRegex)) {
          out.write(FtlUtils.getUnsubscribeEmailDiv() + lineSeperator);
          out.write(line + lineSeperator);
        } else {
          out.write(line + lineSeperator);
        }
      }
//      ftlFile = addExtraDataToFtl(ftlFile, basicAmazonS3Path);
      return ftlFile;
    } catch (IOException ioe) {
      logger.error("error generating ftl from html: " + ioe);
    } finally {
      IOUtils.closeQuietly(br);
      IOUtils.closeQuietly(out);
    }
    return null;
  }

  private static String addAbsolutePathsToFtl(String line, String basicAmazonS3Path) {
    line = line.replaceAll(imageSourceRegex, "$1$2" + basicAmazonS3Path + "$3");
    line = line.replaceAll(backgroundImageSourceRegex, "$1" + basicAmazonS3Path + "$2\"");
    return line;
  }

  private static File addExtraDataToFtl(File ftlFile, File htmlFile, String basicAmazonS3Path) {
    String line;

    BufferedReader br = null;
    PrintWriter out = null;
    try {
      br = new BufferedReader(new FileReader(ftlFile));
      out = new PrintWriter(new FileWriter(ftlFile));
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
      return ftlFile;
    } catch (IOException ioe) {
      logger.error("error adding data to ftl: " + ioe);
    } finally {
      IOUtils.closeQuietly(br);
      IOUtils.closeQuietly(out);
    }
    return null;
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
        "    � 2011 HealthKart.com. All Rights Reserved. </div>";
  }

  public static String getBasicAmazonS3Path(String contentFolder) {
    return "http://" + awsBucket + ".s3.amazonaws.com/" + contentFolder + "/";
  }

  private static final String lineSeperator = System.getProperty("line.separator");
  private static final String imageSourceRegex = "(<img .*)(src=\"(?!http))(.*\")";
  private static final String backgroundImageSourceRegex = "(background-image:url\\((?!http))(.*)\"";
  private static final String bodyStartTagRegex = "<body .*>";
  private static final String bodyEndTagRegex = "</body>";
}
