package com.hk.admin.util.helper;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.util.io.HkXlsWriter;
import com.hk.constants.XslConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.Resolution;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 16, 2013
 * Time: 12:53:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CycleCountHelper {
	File xlsFile;
   private static Logger logger = LoggerFactory.getLogger(CycleCountHelper.class);

	public File generateReconVoucherAddExcel(List<CycleCountItem> cycleCountItems, File xlsFile, Map<Long, Integer> scannedPviVariance) {
		this.xlsFile = xlsFile;
		HkXlsWriter xlsWriter = new HkXlsWriter();
		int xlsRow = 1;
		xlsWriter.addHeader(XslConstants.VARIANT_ID, XslConstants.VARIANT_ID);
		xlsWriter.addHeader(XslConstants.QTY, XslConstants.QTY);
		xlsWriter.addHeader(XslConstants.BATCH_NUMBER, XslConstants.BATCH_NUMBER);
		xlsWriter.addHeader(XslConstants.EXP_DATE, XslConstants.EXP_DATE);
		xlsWriter.addHeader(XslConstants.MFG_DATE, XslConstants.MFG_DATE);
		xlsWriter.addHeader(XslConstants.MRP, XslConstants.MRP);
		xlsWriter.addHeader(XslConstants.COST, XslConstants.COST);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

		for (CycleCountItem cycleCountItem : cycleCountItems) {
			SkuGroup skuGroup = cycleCountItem.getSkuGroup();
			xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
			int qtyToadd = scannedPviVariance.get(cycleCountItem.getId());
			xlsWriter.addCell(xlsRow, qtyToadd);
			xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
			String expiryDate = "";
			if (skuGroup.getExpiryDate() != null) {
				expiryDate = sdf.format(skuGroup.getExpiryDate());
			}
			xlsWriter.addCell(xlsRow, expiryDate);
			String mfgDate = "";
			if (skuGroup.getMfgDate() != null) {
				mfgDate = sdf.format(skuGroup.getMfgDate());
			}
			xlsWriter.addCell(xlsRow, mfgDate);

			xlsWriter.addCell(xlsRow, skuGroup.getMrp());
			xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());

			xlsRow++;
		}
		xlsWriter.writeData(xlsFile, "Sheet1");
		return xlsFile;
	}


	public File generateCompleteCycleCountExcel(List<CycleCountItem> cycleCountItems, File xlsFile, Map<Long, Integer> scannedPviVariance) {
		this.xlsFile = xlsFile;
		HkXlsWriter xlsWriter = new HkXlsWriter();
		int xlsRow = 1;
		xlsWriter.addHeader(XslConstants.VARIANT_ID, XslConstants.VARIANT_ID);
		xlsWriter.addHeader(XslConstants.QTY, XslConstants.QTY);
		xlsWriter.addHeader(XslConstants.BATCH_NUMBER, XslConstants.BATCH_NUMBER);
		xlsWriter.addHeader(XslConstants.HK_BARCODE, XslConstants.HK_BARCODE);
		xlsWriter.addHeader(XslConstants.SCANNED_QTY, XslConstants.SCANNED_QTY);
		xlsWriter.addHeader(XslConstants.SYSTEM_QTY, XslConstants.SYSTEM_QTY);
		xlsWriter.addHeader(XslConstants.VARINACE_QTY, XslConstants.VARINACE_QTY);
		xlsWriter.addHeader(XslConstants.EXP_DATE, XslConstants.EXP_DATE);
		xlsWriter.addHeader(XslConstants.MFG_DATE, XslConstants.MFG_DATE);
		xlsWriter.addHeader(XslConstants.MRP, XslConstants.MRP);
		xlsWriter.addHeader(XslConstants.COST, XslConstants.COST);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

		for (CycleCountItem cycleCountItem : cycleCountItems) {
			SkuGroup skuGroup = cycleCountItem.getSkuGroup();
			xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
			int qtyToadd = scannedPviVariance.get(cycleCountItem.getId());
			xlsWriter.addCell(xlsRow, qtyToadd);
			xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
			xlsWriter.addCell(xlsRow, skuGroup.getBarcode());
			xlsWriter.addCell(xlsRow, cycleCountItem.getScannedQty().intValue());
			int diffQty = scannedPviVariance.get(cycleCountItem.getId()).intValue();
			int sysQty = cycleCountItem.getScannedQty().intValue() + (diffQty);
			xlsWriter.addCell(xlsRow, sysQty);
			xlsWriter.addCell(xlsRow, diffQty);
			String expiryDate = "";
			if (skuGroup.getExpiryDate() != null) {
				expiryDate = sdf.format(skuGroup.getExpiryDate());
			}
			xlsWriter.addCell(xlsRow, expiryDate);
			String mfgDate = "";
			if (skuGroup.getMfgDate() != null) {
				mfgDate = sdf.format(skuGroup.getMfgDate());
			}
			xlsWriter.addCell(xlsRow, mfgDate);

			xlsWriter.addCell(xlsRow, skuGroup.getMrp());
			xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());

			xlsRow++;
		}
		xlsWriter.writeData(xlsFile, "Sheet1");
		return xlsFile;
	}


	public void generateDocFile(File  barcodeError, Map<String, String> hkBarcodeErrorsMap) throws IOException{
		this.xlsFile = barcodeError;
		if(hkBarcodeErrorsMap != null && hkBarcodeErrorsMap.size() > 0) {
		for (String hkBarcode : hkBarcodeErrorsMap.keySet()) {
			StringBuffer data = new StringBuffer();
			data = data.append(hkBarcode).append("\t").append(hkBarcodeErrorsMap.get(hkBarcode).toString());
			BufferedWriter bufferedWriter = null;
			   FileWriter fileWriter ;

			   try {
			     fileWriter = new FileWriter(barcodeError.getAbsolutePath(), false);
			     bufferedWriter = new BufferedWriter(fileWriter);
			     bufferedWriter.append(data);
			   }
			   finally {
			     try {
			       if (bufferedWriter != null)
			         bufferedWriter.close();
			     } catch (Exception ex) {

			     }
			   }
		}
			download();
		}

	}


	public Resolution download() {
		return new Resolution() {
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				OutputStream out = null;
				InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
				res.setContentLength((int) xlsFile.length());
				res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
				out = res.getOutputStream();

				// Copy the contents of the file to the output stream
				byte[] buf = new byte[4096];
				int count = 0;
				while ((count = in.read(buf)) >= 0) {
					out.write(buf, 0, count);
				}
			}
		};

	}





	public Map<String, Integer> readCycleCountNotepad(File file) throws IOException {
		logger.debug("parsing Cycle Count Notepad : " + file.getAbsolutePath());
		Map<String, Integer> barcodeQty = new HashMap<String, Integer>();
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String barcode = buffer.readLine();
			while (barcode != null) {
				if (barcodeQty.containsKey(barcode)) {
					int qty = barcodeQty.get(barcode);
					barcodeQty.put(barcode, (qty + 1));
				} else {
					barcodeQty.put(barcode, 1);
				}
			}

		} catch (IOException ex) {
			throw new IOException();
		}
		return barcodeQty;
	}



}

