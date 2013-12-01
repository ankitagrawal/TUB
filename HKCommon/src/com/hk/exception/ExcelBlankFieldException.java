package com.hk.exception;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 1, 2012
 * Time: 3:49:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExcelBlankFieldException extends HealthkartRuntimeException {


  public ExcelBlankFieldException(String message, int rowNumber) {
    super(message+" at row "+rowNumber);

  }

   public ExcelBlankFieldException(String message) {
    super(message);
}

}