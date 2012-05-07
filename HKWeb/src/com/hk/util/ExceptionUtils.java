package com.hk.util;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 2/7/12
 * Time: 1:02 AM
 * To change this template use File | Settings | File Templates.
 */

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
  public static String getStackTrace(Throwable ex) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.flush();
    sw.flush();
    ex.printStackTrace(pw);
    return sw.toString();
  }
}
