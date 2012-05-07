package com.hk.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author vaibhav.adlakha
 */
public class ExcelSheetParser implements FileParser {

  private File file;
  private boolean containsHeader = true;
  private String sheetName;

  public ExcelSheetParser(String filePath, String sheetName, boolean containsHeader) {
    this(filePath, sheetName);
    this.containsHeader = containsHeader;
  }

  public ExcelSheetParser(String filePath, boolean containsHeader) {
    this(filePath);
    this.containsHeader = containsHeader;
  }

  public ExcelSheetParser(String filePath, String sheetName) {
    this(filePath);
    this.sheetName = sheetName;
  }

  public ExcelSheetParser(String filePath) {
    this.file = new File(filePath);
    if (!this.file.exists()) {
      throw new IllegalArgumentException("No such file exists at path :" + filePath);
    }
  }

  public Iterator<HKRow> parse() {
    try {
      POIFSFileSystem objInFileSys = new POIFSFileSystem(new FileInputStream(file));
      HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

      HSSFSheet s;
      if (StringUtils.isEmpty(sheetName)) {
        throw new UnsupportedOperationException("cannot parse without a sheet name");
      } else {
        HSSFSheet productSheet = workbook.getSheet("vaibhav");
        s = workbook.getSheet(sheetName);
      }
      return new SheetIterator(s, containsHeader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static class SheetIterator implements Iterator<HKRow> {

    private HSSFSheet sheet;
    private int lineNo;
    private Map<Object, Integer> columnNamesToIndex;

    public SheetIterator(HSSFSheet sheet, boolean containsHeader) {
      this.sheet = sheet;
      if (containsHeader) {
        // read the column names and store the position for headers
        Object[] columnNames = getColumnValues(sheet.getRow(lineNo++));
        columnNamesToIndex = new HashMap<Object, Integer>(columnNames.length);
        for (int i = 0; i < columnNames.length; i++) {
          columnNamesToIndex.put(columnNames[i], i);
        }
      }
    }

    private Object[] getColumnValues(Row row) {
      List<Object> columnValues = new ArrayList<Object>();

      Iterator<Cell> cellItr = row.cellIterator();
      while (cellItr.hasNext()) {
        Cell cell = cellItr.next();
        //columnValues.add(cell.getCellType())
      }
      /*for (int i = 0; i < row.length; i++) {
          columnValues[i] = row[i].getContents().trim();
      }*/
      return (Object[]) columnValues.toArray();
    }

    public boolean hasNext() {
      return lineNo < sheet.getLastRowNum();
    }

    public HKRow next() {
      if (hasNext()) {
        Object[] columnValues = getColumnValues(sheet.getRow(lineNo++));
        if (columnNamesToIndex != null) {
          return new HKRow(lineNo, columnValues, columnNamesToIndex);
        } else {
          return new HKRow(lineNo, columnValues);
        }
      }
      throw new NoSuchElementException();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  public static void main(String[] args) {
    ExcelSheetParser parser = new ExcelSheetParser("D:" + "/" + "test.xls","rahul");

    Iterator<HKRow> rowIterator = parser.parse();

    while(rowIterator.hasNext()){
      HKRow curHkRow = rowIterator.next();

      Object column1 = curHkRow.getColumnValue("Test1");
      System.out.println("by name:" + column1);
      Object colum1Idx = curHkRow.getColumnValue(1);
      System.out.println("by idx:" + colum1Idx);

    }
  }
}
