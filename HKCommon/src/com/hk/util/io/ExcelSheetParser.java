package com.hk.util.io;

import static org.apache.poi.ss.util.NumberToTextConverter.toText;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vaibhav.adlakha
 */
public class ExcelSheetParser implements FileParser {

    private static Logger        logger         = LoggerFactory.getLogger(ExcelSheetParser.class);

    private File                 file;
    private boolean              containsHeader = true;
    private String               sheetName;
    // private HKRow curHkRow;
    private Map<Integer, String> headingNames   = new HashMap<Integer, String>();

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

    public String getHeadingNames(int i) {
        return headingNames.get(i);
    }

    public Map<Integer, String>  getHeadingNamesMap() {
           return headingNames;
       }


    public Iterator<HKRow> parse() {
        try {
            POIFSFileSystem objInFileSys = new POIFSFileSystem(new FileInputStream(file));
            HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

            HSSFSheet s;
            if (StringUtils.isEmpty(sheetName)) {
                throw new UnsupportedOperationException("cannot parse without a sheet name");
            } else {
                // HSSFSheet productSheet = workbook.getSheet(sheetName);
                s = workbook.getSheet(sheetName);
            }
            return new SheetIterator(s, containsHeader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public class SheetIterator implements Iterator<HKRow> {

        private HSSFSheet            sheet  = null;
        private int                  lineNo = 0;
        private Map<String, Integer> columnNamesToIndex;

        public SheetIterator(HSSFSheet sheet, boolean containsHeader, int rowNum) {
            this.lineNo = rowNum;
            this.sheet = sheet;
            if (containsHeader) {
                // read the column names and store the position for headers
                String[] columnNames = getColumnNames(sheet.getRow(lineNo));
                columnNamesToIndex = new HashMap<String, Integer>(columnNames.length);

                for (int i = 0; i < columnNames.length; i++) {
                    columnNamesToIndex.put(columnNames[i], i);
                    headingNames.put(i, columnNames[i]);

                }
            }
        }

        public SheetIterator(HSSFSheet sheet, boolean containsHeader) {
            this(sheet, containsHeader, 0);
        }

        // get column values , it is always in form of string which will be converted later on
        private String[] getColumnNames(HSSFRow row) {
            List<String> columnValues = new ArrayList<String>();
            String cellValue = null;
            if (row != null) {
                Iterator<Cell> cellItr = row.cellIterator();
                Calendar cal = Calendar.getInstance();
                while (cellItr.hasNext()) {
                    Cell cell = cellItr.next();
                    if (cell != null) {
                        try {
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_BLANK:
                                    cellValue = "";
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    cellValue = cell.getStringCellValue().trim();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    try {
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            Double doub = cell.getNumericCellValue();
                                            if (HSSFDateUtil.isValidExcelDate(doub)) {

                                                DataFormatter dateFormat = new DataFormatter();
                                                cellValue = dateFormat.formatCellValue(cell);
                                            }

                                        } else {

                                            Double doub = cell.getNumericCellValue();
                                            cellValue = toText(doub);
                                        }
                                    } catch (IllegalStateException IS) {

                                        logger.debug("error trying to read column " + columnValues + " as numeric on Row " + sheet.getLastRowNum());
                                        logger.debug("Now trying to read as numeric");
                                        System.out.println("error in reading cell value as Numeric" + IS.getMessage());
                                        break;
                                    }
                                    break;

                            }
                            cellValue = cellValue.trim();
                        } catch (IllegalStateException ISE) {
                            System.out.println("error in switch case" + ISE.getMessage());
                        }
                        columnValues.add(cellValue);
                    }
                }
            }
            if (cellValue != null) {
                String[] val = columnValues.toArray(new String[columnValues.size()]);
                return val;
            } else
                return null;
        }

        public boolean hasNext() {
            return lineNo < sheet.getLastRowNum();
        }

        public HKRow next() {

            if (hasNext()) {
                String[] columnNames = getColumnNames(sheet.getRow(++lineNo));

                if (columnNamesToIndex != null) {

                    return new HKRow(lineNo, columnNames, columnNamesToIndex);
                } else {
                    return new HKRow(lineNo, columnNames);
                }

            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static void main(String[] args) {
        ExcelSheetParser excel = new ExcelSheetParser("C:\\Users\\Seema\\Documents\\cityTAT.xls", "Sheet1", true);
        Iterator<HKRow> iter = excel.parse();

        while (iter.hasNext()) {
            HKRow row = iter.next();
            String city = row.getColumnValue("CITY");
            String courier = row.getColumnValue("COURIER_ID");
            String citytat = row.getColumnValue("CITY_TAT");
            System.out.println(" city ---> " + city + "    courier -->  " + courier + "   city tat -->  " + citytat);

        }

    }

}
