package com.hk.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HkXlsWriter implements HkWriter {

  private static Logger logger = LoggerFactory.getLogger(HkXlsWriter.class);

  private Map<String, String> headersMap;
  private List<List<Object>> data;

  private Workbook workBook;
  private CellStyle style;
  private List<Sheet> sheets = new ArrayList<Sheet>();

  public HkXlsWriter() {
    headersMap = new LinkedHashMap<String, String>();
    data = new ArrayList<List<Object>>();

  }

  public HkWriter addHeader(String headerKey, String headerValue) {
    headersMap.put(headerKey, headerValue);
    return this;
  }

  public HkWriter addCell(int rowNo, Object value) {

    List<Object> rowData = null;
    try {
      rowData = data.get(rowNo - 1); // The -1 is to get the right index. It assumes that data rows will start as row=1
    } catch (IndexOutOfBoundsException e) {
//      logger.error("Error adding data to file");
    }
    if (rowData == null) {
      rowData = new ArrayList<Object>();
      data.add(rowData);
    }
    rowData.add(value);
    return this;
  }


  private void writeHeaders(Sheet sheet1) {
    Row row = sheet1.createRow(0);
    row.setHeightInPoints((short) 30);
    int totalColumnNo = headersMap.size(), columnmCtr = 0;

    Cell cell;
    for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
      cell = row.createCell(columnNo);
      cell.setCellStyle(style);
    }

    for (Map.Entry<String, String> headersEntry : headersMap.entrySet()) {
      setCellValue(row, columnmCtr, headersEntry.getValue());
      columnmCtr++;
    }
  }


  private void writeRowData(Sheet sheet, int rowNo, List<Object> rowData) {
    Row row = sheet.createRow(rowNo);
    int columnmCtr = 0;

    Cell cell;
    for (int columnNo = 0; columnNo < rowData.size(); columnNo++) {
      cell = row.createCell(columnNo);
      cell.setCellStyle(style);
    }

    for (Object hkCell : rowData) {
      setCellValue(row, columnmCtr++, hkCell);
    }
  }


  public File writeData(File file, String sheetName) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file);
      workBook = new HSSFWorkbook();
    } catch (FileNotFoundException fnfe) {
      logger.error("no file at location :", fnfe);
      fnfe.printStackTrace();
    }

    //Headers Style
    initDefaultHeaderStyle();

    //TODO:work for multiple sheets currently only one is supported
    Sheet sheet1 = workBook.createSheet(sheetName);
    sheets.add(sheet1);
    writeHeaders(sheet1);

    //Row Data Style
    initDefaultCellStyle();
    int rowNo = 1;
    for (List<Object> rowData : data) {
      writeRowData(sheet1, rowNo, rowData);
      rowNo++;
    }

    try {
      if (out != null) {
        workBook.write(out);
        out.flush();
      }
    } catch (IOException ioe) {
      logger.error("error while writing file : ", ioe);
    } finally {
      if (out != null) {
        IOUtils.closeQuietly(out);
      }
    }
    return file;
  }

  public File writeData(String filePath) {
    File file = new File(filePath);
    return this.writeData(file);
  }

  public File writeData(File file) {
    return this.writeData(file, "Sheet-1");
  }


  public static void main(String[] args) {

    HkXlsWriter xlsWriter = new HkXlsWriter();

    xlsWriter.addHeader("Test1", "test1");
    xlsWriter.addHeader("Test2", "test2");
    xlsWriter.addHeader("Test3", "test3");
    xlsWriter.addHeader("Test4", "test4");
    xlsWriter.addHeader("Test5", "test5");


    for (int i = 1; i < 10; i++) {
      for (int j = 0; j < 5; j++) {
        // HKCell cell = new HKCell("value" + j, "Test" + i);
        //System.out.println("Adding to row : " + i + "col:" + cell.getValue());
        xlsWriter.addCell(i, "value" + j);
      }
    }

    xlsWriter.writeData("E:" + "\\" + "test.xls");

  }


  private void setCellValue(Row row, int column, Object cellValue) {
    if (cellValue != null) {
      Cell cell = row.getCell(column);
      if (cellValue instanceof Double) {
        cell.setCellValue((Double) cellValue);
      } else if (cellValue instanceof Date) {
        cell.setCellValue((Date) cellValue);
      } else if (cellValue instanceof Long) {
        cell.setCellValue((Long) cellValue);
      } else
        cell.setCellValue(cellValue.toString());
    }
  }

  /*private void setHKCellValue(Row row, int column, HKCell hkCell) {
    Object cellValue = hkCell.getValue();
    setCellValue(row, column, cellValue);
  }*/

  private CellStyle initDefaultHeaderStyle() {
    style = workBook.createCellStyle();
    Font font = workBook.createFont();
    font.setFontHeightInPoints((short) 11);
    font.setColor(Font.COLOR_NORMAL);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style.setFont(font);

    return style;
  }

  private CellStyle initDefaultCellStyle() {
    style = workBook.createCellStyle();
    Font font = workBook.createFont();
    font.setFontHeightInPoints((short) 10);
    style.setFont(font);

    return style;
  }
}
