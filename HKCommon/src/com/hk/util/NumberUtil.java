package com.hk.util;

import com.akube.framework.util.FormatUtils;


public class NumberUtil {

  public static Double roundOff(Double numberToRound) {
    if(numberToRound !=null){
      return  FormatUtils.getCurrencyPrecision(numberToRound);
    }

    return new Double(0);
  }
}

