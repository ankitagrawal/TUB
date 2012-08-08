package com.hk.admin.util;

import com.hk.admin.dto.inventory.PoLineItemDto;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.net.URL;
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

    public void generatePurchaseOrderPdf(String pdfFilePath, PurchaseOrderDto purchaseOrderDto) throws Exception {
        PurchaseOrder purchaseOrder = purchaseOrderDto.getPurchaseOrder();
        Document purchaseOrderDocument = new Document();
        try {
            PdfWriter.getInstance(purchaseOrderDocument, new FileOutputStream(pdfFilePath));
            purchaseOrderDocument.open();
            if(purchaseOrder != null) {
                Paragraph addressParagraph = new Paragraph();
                addressParagraph.add(new Paragraph("Bright Lifecare Pvt. Ltd.", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
                addressParagraph.add(new Paragraph(purchaseOrder.getWarehouse().getLine1(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
                addressParagraph.add(new Paragraph(purchaseOrder.getWarehouse().getLine2(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
                addressParagraph.add(new Paragraph(purchaseOrder.getWarehouse().getCity() + " -" + purchaseOrder.getWarehouse().getPincode(),
                        new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
                addressParagraph.add(new Paragraph(purchaseOrder.getWarehouse().getState(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
                addressParagraph.add(new Paragraph("TIN: " + purchaseOrder.getWarehouse().getTin(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));

                Paragraph header = new Paragraph("PURCHASE ORDER \n\n", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
                header.setAlignment(Element.ALIGN_CENTER);
                //TODO: Change this logo path
                Image image = Image.getInstance("C:/Users/Rohit/IdeaProjects/rewrite/HKRejuvenate/HealthKart/dist/images/logo/HealthKartLogo.png");
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
            logger.error("Exception occurred while generating pdf." + e.getMessage());
        } finally {
            purchaseOrderDocument.close();
        }
    }

    private void createSupplierDetailTable(Document document, PurchaseOrder purchaseOrder) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        PdfPTable supplierDetailTable = new PdfPTable(4);
        float[] widths = {25f, 25f, 25f, 25f};
        supplierDetailTable.setWidths(widths);
        supplierDetailTable.setWidthPercentage(100f);

        PdfPCell c1 = new PdfPCell();
        Paragraph supplierParagraph = new Paragraph("Supplier", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD));
        c1.addElement(supplierParagraph);
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph(purchaseOrder.getSupplier().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph("PO Date", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph(simpleDateFormat.format(purchaseOrder.getPoDate()), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph("Address", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        supplierDetailTable.addCell(c1);
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

        c1 = new PdfPCell();
        c1.addElement(new Paragraph(supplierAddress.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph("PO#", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph(""+purchaseOrder.getId(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph("Contact Name", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph(purchaseOrder.getSupplier().getContactPerson(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph("Contact Number", new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD)));
        supplierDetailTable.addCell(c1);

        c1 = new PdfPCell();
        c1.addElement(new Paragraph(purchaseOrder.getSupplier().getContactNumber(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        supplierDetailTable.addCell(c1);

        document.add(supplierDetailTable);
    }

    private void createPODetails(Document document, PurchaseOrderDto purchaseOrderDto) throws Exception{
        java.util.List<PoLineItemDto> poLineItemDtoList = purchaseOrderDto.getPoLineItemDtoList();
        PdfPTable poDetailTable = new PdfPTable(11);
        float[] widths = {4.33f, 11.33f, 11.33f,  14.66f,  8.33f,  8.33f,  8.33f,  8.33f, 8.33f,  8.33f,  8.33f};
        poDetailTable.setWidths(widths);
        poDetailTable.setWidthPercentage(100f);

        PdfPCell cell = new PdfPCell();

        cell.addElement(new Paragraph("S.No.", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("VariantID", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("UPC", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("Details", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("Qty", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("Cost Price\n(Without Tax)", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("MRP", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("Taxable", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("Tax", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("Surcharge", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("Payable", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        poDetailTable.addCell(cell);

        int counter = 1;
        for(PoLineItemDto poLineItemDto : poLineItemDtoList) {
            ProductVariant productVariant = poLineItemDto.getPoLineItem().getSku().getProductVariant();
            cell = new PdfPCell();
            cell.addElement(new Paragraph("" + counter, new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph(productVariant.getId(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph(productVariant.getUpc(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph(productVariant.getProduct().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph("" + poLineItemDto.getPoLineItem().getQty(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph("" + poLineItemDto.getPoLineItem().getCostPrice(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph("" + poLineItemDto.getPoLineItem().getMrp(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph("" + poLineItemDto.getTaxable(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph("" + poLineItemDto.getTax(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph("" + poLineItemDto.getSurcharge(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Paragraph("" + poLineItemDto.getPayable(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
            poDetailTable.addCell(cell);
        }

        cell = new PdfPCell();
        cell.addElement(new Paragraph("Total", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        cell.setColspan(7);
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("" + purchaseOrderDto.getTotalTaxable(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("" + purchaseOrderDto.getTotalTax(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("" + purchaseOrderDto.getTotalSurcharge(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        poDetailTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("" + purchaseOrderDto.getTotalPayable(), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL)));
        poDetailTable.addCell(cell);


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
