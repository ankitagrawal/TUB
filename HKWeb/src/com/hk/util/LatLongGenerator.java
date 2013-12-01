package com.hk.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.hk.domain.LocalityMap;
import com.hk.domain.MapIndia;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.user.Address;
import com.hk.pact.dao.core.ManufacturerDao;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.service.core.AddressService;

/**
 * Created by IntelliJ IDEA. User: USER Date: Oct 20, 2011 Time: 11:36:36 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class LatLongGenerator {
    // private static Logger logger = LoggerFactory.getLogger(LatLongGenerator.class);

    @Autowired
    MapIndiaDao     mapIndiaDao;
    @Autowired
    LocalityMapDao  localityMapDao;
    @Autowired
    AddressService  addressDao;
    @Autowired
    ManufacturerDao manufacturerDao;
    String          line2;
    String          city;
    String          state;
    String          lattitude;
    String          longitude;

/*
    public void readCsvFile(String filePath, Manufacturer manufacturer) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String strLine = "";
        while ((strLine = br.readLine()) != null) {
            if (strLine.indexOf(",") != -1) {
                line2 = strLine.substring(0, strLine.indexOf(","));
                city = strLine.substring(strLine.indexOf(",") + 1, strLine.indexOf(";"));
            }
            // default pincode, can be edited later
            String pincode = "000000";
            Address thyrocareAddress = new Address();
            thyrocareAddress.setCity(city);
            thyrocareAddress.setLine1(line2);
            thyrocareAddress.setLine2(line2);
            thyrocareAddress.setState("India");
            thyrocareAddress.setPin(pincode);
            thyrocareAddress.setName(manufacturer.getName());
            thyrocareAddress.setPhone("1-800-1034466");
            thyrocareAddress = addressDao.save(thyrocareAddress);
            createLocalityMap(thyrocareAddress, manufacturer);
        }
    }
*/

    public void parseXmlResponse(String city, String state) throws IOException {
        XPathFactory factory = XPathFactory.newInstance();

        XPath xpath = factory.newXPath();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(city, "UTF-8") + ","
                    + URLEncoder.encode(state, "UTF-8") + ",India&sensor=true&region=in").openConnection();
            // The xpath evaluator requires the XML be in the format of an InputSource
            //System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputSource inputXml = new InputSource(connection.getInputStream());

                // Because the evaluator may return multiple entries, we specify that the expression
                // return a NODESET and place the result in a NodeList.
                String XPATH_EXPRESSION1 = "/GeocodeResponse/result/geometry/location";
                NodeList nodes1 = (NodeList) xpath.evaluate(XPATH_EXPRESSION1, inputXml, XPathConstants.NODESET);

                // We can then iterate over the NodeList and extract the content via getTextContent().
                // NOTE: this will only return text for element nodes at the returned context.

                if (nodes1.item(0).getTextContent() != null) {
                    String[] text = org.apache.commons.lang.StringUtils.split(nodes1.item(0).getTextContent(), "\n");
                    MapIndia mapIndia = new MapIndia();
                    mapIndia.setCity(city);
                    mapIndia.setState(state);
                    mapIndia.setLattitude(Double.parseDouble(text[0]));
                    mapIndia.setLongitude(Double.parseDouble(text[1]));
                    mapIndiaDao.save(mapIndia);
                }
            }
        } catch (XPathExpressionException ex) {
            System.out.print("XPath Error");
        } catch (Exception e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void createLocalityMap(Address address, Manufacturer manufacturer) {
        XPathFactory factory = XPathFactory.newInstance();

        XPath xpath = factory.newXPath();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address.getLine2(), "UTF-8")
                    + "," + URLEncoder.encode(address.getCity(), "UTF-8") + "," + URLEncoder.encode(address.getState(), "UTF-8") + "&sensor=true&region=in").openConnection(); //add ,India

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputSource inputXml = new InputSource(connection.getInputStream());
                String XPATH_EXPRESSION1 = "/GeocodeResponse/result/geometry/location";
                NodeList nodes1 = (NodeList) xpath.evaluate(XPATH_EXPRESSION1, inputXml, XPathConstants.NODESET);

                if (nodes1.item(0).getTextContent() != null) {
                    String[] text = org.apache.commons.lang.StringUtils.split(nodes1.item(0).getTextContent(), "\n");
                    LocalityMap localityMap = localityMapDao.findByAddress(address);
                    if (localityMap == null) {
                        localityMap = new LocalityMap();
                        localityMap.setAddress(address);
                    }
                    localityMap.setLattitude(Double.parseDouble(text[0]));
                    localityMap.setLongitude(Double.parseDouble(text[1]));
                    localityMapDao.save(localityMap);
                    List<Address> addresses = manufacturer.getAddresses();
                    addresses.add(address);
                    manufacturer.setAddresses(addresses);
                }
            }
        } catch (XPathExpressionException ex) {
            System.out.print("XPath Error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}