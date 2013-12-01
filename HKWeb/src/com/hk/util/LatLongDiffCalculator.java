package com.hk.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Oct 21, 2011
 * Time: 3:18:34 AM
 * To change this template use File | Settings | File Templates.
 */

public class LatLongDiffCalculator {
  private static Logger logger = LoggerFactory.getLogger(LatLongDiffCalculator.class);

  public Long getDistanceInMeters(Double originLattitude, Double originLongitude, Double destinationLattitude, Double destinationLongitude) throws IOException {
    XPathFactory factory = XPathFactory.newInstance();

    XPath xpath = factory.newXPath();
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL("http://maps.googleapis.com/maps/api/distancematrix/xml?origins=" + originLattitude + "," + originLongitude + "&destinations=" + destinationLattitude + "," + destinationLongitude).openConnection();
      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputSource inputXml = new InputSource(connection.getInputStream());

        String XPATH_EXPRESSION1 = "/DistanceMatrixResponse/row/element/distance/value";
        NodeList nodes1 = (NodeList) xpath.evaluate(XPATH_EXPRESSION1, inputXml, XPathConstants.NODESET);

        return Long.parseLong(nodes1.item(0).getTextContent());
      }
    }
    catch (MalformedURLException e) {
      logger.error("MalformedURLException in LatLongDistCalculator", e);
    }
    catch (IOException e) {
      logger.error("IOException in LatLongDistCalculator", e);
    }
    catch (XPathExpressionException ex) {
      logger.debug("XPath Error in LatLongDistCalculator");
    } catch (Exception e) {
      logger.debug("Error LatLongDistCalculator", e);
    }
    return 0L;
  }
}
