package com.hk.util.ga;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 11, 2012
 * Time: 1:45:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class GAUtil {
      public static String formatDate(Date date){
          SimpleDateFormat simpleDate=new SimpleDateFormat("'Y'yyyy'W'ww'M'MM'D'DDD");
          return simpleDate.format(date);
    }
}
