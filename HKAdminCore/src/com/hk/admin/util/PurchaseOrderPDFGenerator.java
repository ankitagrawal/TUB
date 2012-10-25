package com.hk.admin.util;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hk.admin.dto.inventory.PoLineItemDto;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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

    public void generatePurchaseOrderPdf(String pdfFilePath, PurchaseOrderDto purchaseOrderDto, String logoImagePath) throws Exception {
        PurchaseOrder purchaseOrder = purchaseOrderDto.getPurchaseOrder();
        Document purchaseOrderDocument = new Document();
        try {
            PdfWriter.getInstance(purchaseOrderDocument, new FileOutputStream(pdfFilePath));
            purchaseOrderDocument.open();
            if(purchaseOrder != null) {
                Paragraph addressParagraph = new Paragraph();
                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);
                addressParagraph.add(new Paragraph("Bright Lifecare Pvt. Ltd.", font));
                addressParagraph.add(new Paragraph(purchaseOrder.getWarehouse().getLine1(), font));
                addressParagraph.add(new Paragraph(purchaseOrder.getWarehouse().getLine2(), font));
                addressParagraph.add(new Paragraph(purchaseOrder.getWarehouse().getCity() + " -" + purchaseOrder.getWarehouse().getPincode(), font));
                addressParagraph.add(new Paragraph(purchaseOrder.getWarehouse().getState(), font));
                addressParagraph.add(new Paragraph("TIN: " + purchaseOrder.getWarehouse().getTin(), font));

                Paragraph header = new Paragraph("PURCHASE ORDER \n\n", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
                header.setAlignment(Element.ALIGN_CENTER);

                Image image = Image.getInstance(logoImagePath);
                image.setAlignment(Element.ALIGN_RIGHT);

                purchaseOrderDocument.add(addressParagraph);
                purchaseOrderDocument.add(image);
                purchaseOrderDocument.add(header);
                createSupplierDetailTable(purchaseOrderDocument, purchaseOrder);
                purchaseOrderDocument.add(new Paragraph("\n"));
                createPODetails(purchaseOrderDocument, purchaseOrderDto);
                createFooter(purchaseOrderDocument);
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
        supplierAddress.append("\n");
        if(purchaseOrder.getSupplier().getLine2() != null) {
            supplierAddress.append(purchaseOrder.getSupplier().getLine2());
        }
        supplierAddress.append("\n");
        if(purchaseOrder.getSupplier().getCity() != null) {
            supplierAddress.append(purchaseOrder.getSupplier().getCity());
        }
        supplierAddress.append("\n");
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
        poDetailTable.addCell(PdfGenerator.createCell("Cost Price\n (Without Tax)", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Tax %", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Taxable", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Tax", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Surcharge", font1));
        poDetailTable.addCell(PdfGenerator.createCell("Payable", font1));

        int counter = 1;
        for(PoLineItemDto poLineItemDto : poLineItemDtoList) {
            ProductVariant productVariant = poLineItemDto.getPoLineItem().getSku().getProductVariant();
            poDetailTable.addCell(PdfGenerator.createCell("" + counter++, font2));
            poDetailTable.addCell(PdfGenerator.createCell(productVariant.getId(), font2));
            poDetailTable.addCell(PdfGenerator.createCell(productVariant.getUpc(), font2));
            poDetailTable.addCell(PdfGenerator.createCell(productVariant.getProduct().getName(), font2));
            poDetailTable.addCell(PdfGenerator.createCell("" + poLineItemDto.getPoLineItem().getQty(), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getPoLineItem().getMrp()), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getPoLineItem().getCostPrice()), font2));
            poDetailTable.addCell(PdfGenerator.createCell(numberFormat.format(poLineItemDto.getPoLineItem().getSku().getTax().getValue() * 100), font2));
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

    private void createFooter(Document document) throws Exception{
        Paragraph footerParagraph = new Paragraph();
        footerParagraph.add(new Paragraph("1) Please indicate Purchase Order number on all invoice and challan and correspondence.\n", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        footerParagraph.add(new Paragraph("2) The item supplied will be subject to our approval and all rejections will be to your account.\n", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        footerParagraph.add(new Paragraph("3) No excess supply will be accepted, unless agreed in writing by us.\n", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));

        document.add(footerParagraph);
    }

}
