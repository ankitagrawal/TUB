package com.hk.util;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseCsvFile {

  private static Logger logger = LoggerFactory.getLogger(ParseCsvFile.class);


  public static List<String> getStringListFromCsv(String filePath) {
    List<String> emailList = new ArrayList<String>();
    Map<String, Long> upcQty = new HashMap<String, Long>();

    try {
      //create BufferedReader to read csv file
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      String strLine = "";
      StringTokenizer st = null;
      int lineNumber = 0, tokenNumber = 0;

      //read comma separated file line by line
      while ((strLine = br.readLine()) != null) {
        lineNumber++;

        //break comma separated line using ","
        st = new StringTokenizer(strLine, ",");

        while (st.hasMoreTokens()) {
          //display csv values
          tokenNumber++;
          emailList.add(st.nextToken());
        }
        //reset token number
        tokenNumber = 0;

      }

    } catch (Exception e) {
      logger.error("exception while reading csv file:" + e);
    }
    return emailList;
  }

  public static Map<String, Long> getMapFromCsv(String filePath) {
    Map<String, Long> upcQty = new HashMap<String, Long>();

    try {
      //create BufferedReader to read csv file
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      String strLine = "";
      StringTokenizer st = null;
      int lineNumber = 0, tokenNumber = 0;

      //read comma separated file line by line
      while ((strLine = br.readLine()) != null) {
        lineNumber++;

        //break comma separated line using ","
        st = new StringTokenizer(strLine, ",");

        upcQty.put(st.nextToken(), Long.parseLong(st.nextToken()));

/*
        while (st.hasMoreTokens()) {
          //display csv values
          tokenNumber++;
        }
*/
        //reset token number
//        tokenNumber = 0;

      }

    } catch (Exception e) {
      logger.error("Exception while reading csv file:  " + e);
      //System.out.println("Exception while reading csv file: " + e);
    }
    return upcQty;
  }

}