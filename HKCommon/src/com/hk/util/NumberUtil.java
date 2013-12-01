package com.hk.util;

import com.akube.framework.util.FormatUtils;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NumberUtil {

	private static Logger logger = LoggerFactory.getLogger(NumberUtil.class);

  public static Double roundOff(Double numberToRound) {
    if(numberToRound !=null){
      return  FormatUtils.getCurrencyPrecision(numberToRound);
    }

    return new Double(0);
  }

	
	public static double roundTwoDecimals(Double numberToRound) {
		try {
			DecimalFormat twoDecimalPlaceFormat = new DecimalFormat("#.##");
			if (numberToRound == null) {
				return 0D;
			}
			return Double.valueOf(twoDecimalPlaceFormat.format(numberToRound));
		} catch (Exception e) {
			logger.warn("exception while converting double to two decimal places ::" + numberToRound);
			return 0D;
		}
	}
}

