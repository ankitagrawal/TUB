package com.hk.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.hk.domain.order.B2BProduct;

@Component
public class B2BOrderManager {
	
	public static final String    VARIANT_ID  = "VARIANT_ID";
	public static final String    QUANTITY  = "QUANTITY";
	List<B2BProduct> b2bProductList;
	
	public List<B2BProduct> parseExcelAndGetProductList(File excelFile) throws Exception {
		b2bProductList = new ArrayList<B2BProduct>();
		InputStream poiInputStream = new FileInputStream(excelFile);
		POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);
		HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);
		HSSFSheet productSheet = workbook.getSheetAt(0);
		Iterator<Row> objRowIt = productSheet.rowIterator();
		Map<Integer, String> headerMap;
		Map<Integer, String> rowMap;
		try {
			headerMap = getRowMap(objRowIt);
			String variantId;
			int quantity;
			while (objRowIt.hasNext()) {
				B2BProduct b2bProduct = new B2BProduct();
				rowMap = getRowMap(objRowIt);
				variantId = getCellValue(VARIANT_ID, rowMap, headerMap);
				quantity = getInteger(getCellValue(QUANTITY, rowMap, headerMap));
				b2bProduct.setProductId(variantId);
				b2bProduct.setQuantity(quantity);
				b2bProductList.add(b2bProduct);
			}
		} catch (Exception e) {

		}
		return b2bProductList;
	}

	private String getCellValue(String header, Map<Integer, String> rowMap, Map<Integer, String> headerMap) {
		Integer columnIndex = getColumnIndex(header, headerMap);
		if (columnIndex == null)
			return null;
		String cellVal = rowMap.get(columnIndex);
		return cellVal == null ? "" : cellVal.trim();
	}

	private Integer getColumnIndex(String header, Map<Integer, String> headerMap) {
		Integer columnIndex = null;
		for (Integer key : headerMap.keySet()) {
			if (headerMap.get(key).equals(header))
				columnIndex = key;
		}
		return columnIndex;
	}

	@SuppressWarnings("unused")
	private Integer getInteger(String value) {
		Integer valueInDouble = null;
		try {
			valueInDouble = Integer.parseInt(value);
		} catch (Exception e) {

		}
		return valueInDouble;
	}

	private Long getLong(String value) {
		Long valueInLong = null;
		try {
			valueInLong = Long.parseLong(value.replace(".0", ""));
		} catch (Exception e) {

		}
		return valueInLong;
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, String> getRowMap(Iterator<Row> objRowIt) {
		// Header are read and related columns are taken care of
		// accordignly.

		Map<Integer, String> headerMap = new HashMap<Integer, String>();

		HSSFRow headers = (HSSFRow) objRowIt.next();
		Iterator objCellIterator = headers.cellIterator();
		while (objCellIterator.hasNext()) {
			HSSFCell headerCell = (HSSFCell) objCellIterator.next();
			int headerColIndex = 0;
			headerColIndex = headerCell.getColumnIndex();
			Object cellValue = null;
			try {
				headerCell.setCellType(Cell.CELL_TYPE_STRING);
				cellValue = headerCell.getStringCellValue();
				headerMap.put(headerColIndex, cellValue.toString());
			} catch (Exception e) {
				try {
					cellValue = headerCell.getNumericCellValue();
					headerMap.put(headerColIndex, cellValue.toString());
				} catch (Exception e1) {
				}
			}
		}

		return headerMap;
	}

}
