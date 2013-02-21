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

	public File generateReconVoucherAddExcel(List<CycleCountItem> cycleCountItems, File xlsFile, Map<Long, Integer> cycleCountPVImap) {
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

			int pvi = cycleCountPVImap.get(cycleCountItem.getId());
			int scannedQty = cycleCountItem.getScannedQty();
			int qtyToAdd = Math.abs(pvi - scannedQty);
			xlsWriter.addCell(xlsRow, qtyToAdd);
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


	public File generateCompleteCycleCountExcel(List<CycleCountItem> cycleCountItems, File xlsFile, Map<Long, Integer> cycleCountPVImap , List<SkuGroup> skuGroupList, Map<Long, Integer> skuGroupSystemInventoryMap) {
		this.xlsFile = xlsFile;
		HkXlsWriter xlsWriter = new HkXlsWriter();
		int xlsRow = 1;
		xlsWriter.addHeader(XslConstants.VARIANT_ID, XslConstants.VARIANT_ID);
        xlsWriter.addHeader(XslConstants.VARIANT_NAME, XslConstants.VARIANT_NAME);
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
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getProduct().getName());
			xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
			xlsWriter.addCell(xlsRow, skuGroup.getBarcode());
			int scannedQty = cycleCountItem.getScannedQty().intValue();
			xlsWriter.addCell(xlsRow, scannedQty);
			int sysQty = cycleCountPVImap.get(cycleCountItem.getId());
			xlsWriter.addCell(xlsRow, sysQty);
			int diffQty = sysQty - scannedQty;
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
            Double mrp = 0d;
            if(skuGroup.getMrp() != null){
                mrp = skuGroup.getMrp();
            }
            xlsWriter.addCell(xlsRow, mrp);
			xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());

			xlsRow++;
		}

        /* Add SkuGroup Missed in Scanning */

        for (SkuGroup skuGroup: skuGroupList) {
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getProduct().getName());
            xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
            xlsWriter.addCell(xlsRow, skuGroup.getBarcode());
            xlsWriter.addCell(xlsRow, "0");
            int sysQty = skuGroupSystemInventoryMap.get(skuGroup.getId());
            xlsWriter.addCell(xlsRow, sysQty);
            xlsWriter.addCell(xlsRow, sysQty);
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
            Double mrp = 0d;
            if(skuGroup.getMrp() != null){
                mrp = skuGroup.getMrp();
            }
            xlsWriter.addCell(xlsRow, mrp);
            xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());

            xlsRow++;
        }


        xlsWriter.writeData(xlsFile, "Sheet1");
		return xlsFile;
	}



    public File generateSkuGroupNotScannedExcel(List<SkuGroup> skuGroupList, File xlsFile, Map<Long, Integer> skuGroupSystemInventoryMap) {
        this.xlsFile = xlsFile;
        HkXlsWriter xlsWriter = new HkXlsWriter();
        int xlsRow = 1;
        xlsWriter.addHeader(XslConstants.HK_BARCODE, XslConstants.HK_BARCODE);
        xlsWriter.addHeader(XslConstants.VARIANT_ID, XslConstants.VARIANT_ID);
        xlsWriter.addHeader(XslConstants.VARIANT_NAME, XslConstants.VARIANT_NAME);
        xlsWriter.addHeader(XslConstants.MRP, XslConstants.MRP);
        xlsWriter.addHeader(XslConstants.MFG_DATE, XslConstants.MFG_DATE);
        xlsWriter.addHeader(XslConstants.EXP_DATE, XslConstants.EXP_DATE);

        xlsWriter.addHeader(XslConstants.SCANNED_QTY, XslConstants.SCANNED_QTY);
        xlsWriter.addHeader(XslConstants.BATCH_NUMBER, XslConstants.BATCH_NUMBER);
        xlsWriter.addHeader(XslConstants.SYSTEM_QTY, XslConstants.SYSTEM_QTY);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

        for (SkuGroup skuGroup : skuGroupList) {
            xlsWriter.addCell(xlsRow, skuGroup.getBarcode());
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getProduct().getName());
            Double mrp = 0d;
            if(skuGroup.getMrp() != null){
                mrp = skuGroup.getMrp();
            }
            xlsWriter.addCell(xlsRow, mrp);
            String mfgDate = "";
            if (skuGroup.getMfgDate() != null) {
                mfgDate = sdf.format(skuGroup.getMfgDate());
            }
            xlsWriter.addCell(xlsRow, mfgDate);
            String expiryDate = "";
            if (skuGroup.getExpiryDate() != null) {
                expiryDate = sdf.format(skuGroup.getExpiryDate());
            }
            xlsWriter.addCell(xlsRow, expiryDate);
            int systemQty = skuGroupSystemInventoryMap.get(skuGroup.getId());
            xlsWriter.addCell(xlsRow, "0");
            xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
            xlsWriter.addCell(xlsRow, systemQty);
            xlsRow++;
        }
        xlsWriter.writeData(xlsFile, "MissedBatches");
        return xlsFile;
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
        Map<String, Integer> barcodeQtyMap = new HashMap<String, Integer>();
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String barcode = null;
        while ((barcode = buffer.readLine()) != null) {
            if (barcode.isEmpty()) {
                continue;
            }
            if (barcodeQtyMap.containsKey(barcode)) {
                int qty = barcodeQtyMap.get(barcode);
                barcodeQtyMap.put(barcode, (qty + 1));
            } else {
                barcodeQtyMap.put(barcode, 1);
            }
        }


        return barcodeQtyMap;
    }



}

