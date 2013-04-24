package com.hk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;

@Component
public class FtlUtils {
  private static Logger logger = LoggerFactory.getLogger(FtlUtils.class);
  static String awsBucket;

  @Value("#{hkEnvProps['" + Keys.Env.bucket + "']}")
  private String awsBucketStr;

  @PostConstruct
  public void postConstruction() {
    awsBucket = StringUtils.isNotBlank(awsBucketStr) ? awsBucketStr : "";
  }

  public static File generateFtlFromHtml(File htmlFile, String destinationPath, String emailCampaignName) {
    String htmlPath = htmlFile.getAbsolutePath();
    String basicAmazonS3Path = getBasicAmazonS3Path() + HKFileUtils.getInBetweenPath(htmlPath, "emailContentFiles", "emailer.html");
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
          if(!StringUtils.contains(destinationPath,"reviewCollection")){
            out.write(FtlUtils.getCantViewEmailDiv(basicAmazonS3Path) + lineSeperator);
          }
        } else if (line.matches(bodyEndTagRegex)) {
          out.write(FtlUtils.getUnsubscribeEmailDiv() + lineSeperator);
          out.write(line + lineSeperator);
        } else {
          out.write(line + lineSeperator);
        }
      }
    } catch (IOException ioe) {
      logger.error("error generating ftl from html: " + ioe);
      return null;
    } finally {
      IOUtils.closeQuietly(br);
      IOUtils.closeQuietly(out);
    }
    ftlFile = addUtmParams(ftlFile, emailCampaignName);
    return ftlFile;
  }

  private static String addAbsolutePathsToFtl(String line, String basicAmazonS3Path) {
    line = line.replaceAll(imageSourceRegex, "$1$2" + basicAmazonS3Path + "$3");
    line = line.replaceAll(backgroundImageSourceRegex, "$1" + basicAmazonS3Path + "$2\"");
    return line;
  }

  private static File addUtmParams(File ftlFile, String emailCampaignName) {
    Document document = null;
    try {
      document = Jsoup.parse(ftlFile, "UTF-8", "");
      Elements aTags = document.select("a");

      for (Element aTag : aTags) {
        String href = aTag.attr("href");
        if (href.contains("www.healthkart.com")) {
          href = href + (href.contains("?") ? "&amp;" : "?");
          href = href + "utm_source=enewsletter&utm_medium=email&utm_campaign=" + emailCampaignName;
          aTag.attr("href", href);
        }
      }

      FileUtils.writeStringToFile(ftlFile, document.html());
      return ftlFile;
    } catch (IOException e) {
      logger.error("Error while parsing temporary ftl file: " + e);
    }
    return null;
  }

  private static String getCantViewEmailDiv(String basicAmazonS3Path) {
    return "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
        "<tr>" +
        "<td style=\"font-size:11px; text-align:center; color:#000000; padding:15px\">" +
        "Can't view this email? " +
        "<a href=\"" + basicAmazonS3Path + "emailer.html\">Click here</a> " +
        "to view a web version." +
        "</td>" +
        "</tr>" +
        "</table>";
  }

  private static String getUnsubscribeEmailDiv() {
    return "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
        "<tr>" +
        "<td align=\"center\" valign=\"middle\" style=\"border-top: solid #97b8ca 1px; font-size:11px; text-align:center; color:#666666; padding:10px\">" +
        " If you prefer not to receive HealthKart.com email, <a href=\"${unsubscribeLink}\">click here to Unsubscribe</a>" +
        "<br />  Parsvanath Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA<br />\n" +
        "    &copy; 2013 HealthKart.com. All Rights Reserved. " +
        "</td>" +
        "</tr>" +
        "</table>";
  }

  public static String getBasicAmazonS3Path() {
    return "http://" + awsBucket + ".s3.amazonaws.com/emailContentFiles/";
  }

  private static final String lineSeperator = System.getProperty("line.separator");
  private static final String imageSourceRegex = "(<img .*)(src=\"(?!http))(.*\")";
  private static final String backgroundImageSourceRegex = "(background-image:url\\((?!http))(.*)\"";
  private static final String bodyStartTagRegex = "<body .*>";
  private static final String bodyEndTagRegex = "</body>";
}
