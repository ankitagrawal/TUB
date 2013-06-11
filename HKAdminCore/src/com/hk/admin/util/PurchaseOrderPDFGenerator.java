package com.hk.admin.util;

import static com.akube.framework.util.BaseUtils.newline;
import com.hk.admin.dto.inventory.PoLineItemDto;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.constants.courier.StateList;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;
import com.hk.pact.service.core.WarehouseService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/6/12
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PurchaseOrderPDFGenerator {
    private static Logger logger = LoggerFactory.getLogger(InvoicePDFGenerator.class);

  @Autowired
  WarehouseService warehouseService;

    public void generatePurchaseOrderPdf(String pdfFilePath, PurchaseOrderDto purchaseOrderDto) throws Exception {
        PurchaseOrder purchaseOrder = purchaseOrderDto.getPurchaseOrder();
        Document purchaseOrderDocument = new Document();
        try {
            PdfWriter.getInstance(purchaseOrderDocument, new FileOutputStream(pdfFilePath));
            purchaseOrderDocument.open();
            if(purchaseOrder != null) {
                Paragraph addressParagraph = new Paragraph();
                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);
                Warehouse warehouse = purchaseOrder.getWarehouse();
                addressParagraph.add(new Paragraph(warehouse.getName(), font));
                addressParagraph.add(new Paragraph(warehouse.getLine1(), font));
                addressParagraph.add(new Paragraph(warehouse.getLine2(), font));
                addressParagraph.add(new Paragraph(warehouse.getCity() + " -" + warehouse.getPincode(), font));
                addressParagraph.add(new Paragraph(warehouse.getState(), font));
                addressParagraph.add(new Paragraph("TIN:"+warehouse.getTin(), font));

              Paragraph header = new Paragraph("PURCHASE ORDER " + newline + newline , new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
                header.setAlignment(Element.ALIGN_CENTER);

                purchaseOrderDocument.add(addressParagraph);
                purchaseOrderDocument.add(header);
                createSupplierDetailTable(purchaseOrderDocument, purchaseOrder);
                purchaseOrderDocument.add(new Paragraph(newline));
                createPODetails(purchaseOrderDocument, purchaseOrderDto);
                createFooter(purchaseOrderDocument, purchaseOrderDto);
            }
        } catch (Exception e) {
	        e.printStackTrace();
            logger.error("Exception occurred while generating pdf." + e.getMessage());
        } finally {
            purchaseOrderDocument.close();
        }
    }

    private void createSupplierDetailTable(Document document, PurchaseOrder purchaseOrder) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        float[] widths = {25f, 25f, 25f, 25f};
        PdfPTable supplierDetailTable = PdfGenerator.createTable(4, widths, 100f);

        Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD);
        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);

        StringBuffer supplierAddress = new StringBuffer();

        if(purchaseOrder.getSupplier().getLine1() != null) {
            supplierAddress.append(purchaseOrder.getSupplier().getLine1());
        }
        supplierAddress.append(newline);
        if(purchaseOrder.getSupplier().getLine2() != null) {
            supplierAddress.append(purchaseOrder.getSupplier().getLine2());
        }
        supplierAddress.append(newline);
        if(purchaseOrder.getSupplier().getCity() != null) {
            supplierAddress.append(purchaseOrder.getSupplier().getCity());
        }
        supplierAddress.append(newline);
        if(purchaseOrder.getSupplier().getState() != null) {
            supplierAddress.append(purchaseOrder.getSupplier().getState());
        }

        supplierDetailTable.addCell(PdfGenerator.createCell("Supplier", font1));
        supplierDetailTable.addCell(PdfGenerator.createCell(purchaseOrder.getSupplier().getName(), font2));
        supplierDetailTable.addCell(PdfGenerator.createCell("PO Place Date", font1));
	    if (purchaseOrder.getPoPlaceDate() != null)
		    supplierDetailTable.addCell(PdfGenerator.createCell(simpleDateFormat.format(purchaseOrder.getPoPlaceDate()), font2));
	    else
		    supplierDetailTable.addCell(PdfGenerator.createCell(""));
        supplierDetailTable.addCell(PdfGenerator.createCell("Address", font1));
        supplierDetailTable.addCell(PdfGenerator.createCell(supplierAddress.toString(), font2));
        supplierDetailTable.addCell(PdfGenerator.createCell("PO#", font1));
        supplierDetailTable.addCell(PdfGenerator.createCell(""+purchaseOrder.getId(), font2));
        supplierDetailTable.addCell(PdfGenerator.createCell("Contact Name", font1));
        supplierDetailTable.addCell(PdfGenerator.createCell(purchaseOrder.getSupplier().getContactPerson(), font2));
        supplierDetailTable.addCell(PdfGenerator.createCell("Contact Number", font1));
        supplierDetailTable.addCell(PdfGenerator.createCell(purchaseOrder.getSupplier().getContactNumber(), font2));

        document.add(supplierDetailTable);
    }

    private void createPODetails(Document document, PurchaseOrderDto purchaseOrderDto) throws Exception{
        java.util.List<PoLineItemDto> poLineItemDtoList = purchaseOrderDto.getPoLineItemDtoList();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        float[] widths = {3.33f, 11.33f, 11.33f,  13.33f,  6.33f,  8.33f,  8.33f,  4.33f, 8.33f, 8.33f,  8.33f,  8.33f};

        PdfPTable poDetailTable = PdfGenerator.createTable(12, widths, 100f);

        Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD);
        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);

        poDetailTable.addCell(PdfGenerator.createCell("S. No", font1));
        poDetailTable.addCell(PdfGenerator.createCell("VariantID", font1));
        poDetailTable.addCell(PdfGenerator.createCell("UPC", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Details", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Qty", font1));
        poDetailTable.addCell(PdfGenerator.createCell("MRP", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Cost Price" + newline + " (Without Tax)", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Tax %", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Taxable", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Tax", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Surcharge", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Payable", font1));

        int counter = 1;
        for(PoLineItemDto poLineItemDto : poLineItemDtoList) {
            ProductVariant productVariant = poLineItemDto.getPoLineItem().getSku().getProductVariant();
	        TaxComponent taxComponent = TaxUtil.getSupplierTaxForPV(purchaseOrderDto.getPurchaseOrder().getSupplier(), poLineItemDto.getPoLineItem().getSku(), poLineItemDto.getTaxable());
            poDetailTable.addCell(PdfGenerator.createCell("" + counter++, font2));
            poDetailTable.addCell(PdfGenerator.createCell(productVariant.getId(), font2));
            poDetailTable.addCell(PdfGenerator.createCell(productVariant.getUpc(), font2));
            poDetailTable.addCell(PdfGenerator.createCell(productVariant.getProduct().getName() + newline + productVariant.getOptionsCommaSeparated(), font2));
            poDetailTable.addCell(PdfGenerator.createCell("" + poLineItemDto.getPoLineItem().getQty(), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getPoLineItem().getMrp()), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getPoLineItem().getCostPrice()), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(taxComponent.getTaxRate() * 100), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getTaxable()), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getTax()), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getSurcharge()), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getPayable()), font2));
        }

        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph("Total", font2));
        cell.setColspan(8);
        poDetailTable.addCell(cell);

        poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(purchaseOrderDto.getTotalTaxable()), font2));
        poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(purchaseOrderDto.getTotalTax()), font2));
        poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(purchaseOrderDto.getTotalSurcharge()), font2));
        poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(purchaseOrderDto.getTotalPayable()), font2));

        document.add(poDetailTable);
    }

    private void createFooter(Document document, PurchaseOrderDto purchaseOrderDto) throws Exception{
        Paragraph footerParagraph = new Paragraph();
        footerParagraph.add(new Paragraph("1) Any Cost Price or MRP changes should be highlighted in advance for acceptance of goods at the warehouse by sending the updated catalog." + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        footerParagraph.add(new Paragraph("2) No excess/damaged/without MRP goods will be accepted against the purchase order raised. The courier charges in case of return of any goods will need to be borne by you." + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        footerParagraph.add(new Paragraph("3) Please ensure that all details like TIN No, Address, Product Names, Company Name are correct in the invoice sent. Goods will not be accepted at the warehouse if any of the invoice details are incorrect." + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        footerParagraph.add(new Paragraph("4) PO number and any special schemes should be mentioned on all invoices." + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        footerParagraph.add(new Paragraph("5) Physical products should be packaged well and unique codes, product name and MRP should be clearly mentioned as specified in the catalog." + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        footerParagraph.add(new Paragraph("6) Goods with expiry date in the next 6 months or already expired will not be accepted. Goods about to expire will need to be replaced or returned on request." + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        footerParagraph.add(new Paragraph("7) Please share any unique codes for the products that you may be using in your system, so we can include the same in the PO next time for easy identification of the products while you are sending the goods and while we receive them at our warehouse." + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        if(purchaseOrderDto.getPurchaseOrder().getWarehouse().getState().equalsIgnoreCase(StateList.MAHARASHTRA)){
        	footerParagraph.add(new Paragraph("8) Kindly ship the goods to our warehouse address as follows - Bright Lifecare Private Limited, Mumbai Warehouse: Safexpress Private Limited,Mumbai Nashik Highway N.H-3, Walsind, Lonad, District- Thane- 421302, Maharashtra" + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        }
        if(purchaseOrderDto.getPurchaseOrder().getWarehouse().getState().equalsIgnoreCase(StateList.HARYANA)){
        	footerParagraph.add(new Paragraph("8) Kindly ship the goods to our warehouse address as follows - Bright Lifecare Private Limited, Gurgaon Warehouse: Khasra No. 146/25/2/1, Village Badshahpur, Distt Gurgaon, Haryana-122101; TIN Haryana - 06101832036" + newline, new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        }
        document.add(footerParagraph);
    }


  public WarehouseService getWarehouseService() {
    return warehouseService;
  }
}
