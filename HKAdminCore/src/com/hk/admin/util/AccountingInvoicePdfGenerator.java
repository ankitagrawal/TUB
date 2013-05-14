package com.hk.admin.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.admin.dto.accounting.InvoiceDto;
import com.hk.admin.dto.accounting.InvoiceLineItemDto;
import com.hk.cache.CategoryCache;
import com.hk.constants.core.EnumTax;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.B2bUserDetails;
import com.hk.helper.InvoiceNumHelper;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.service.catalog.CategoryService;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by IntelliJ IDEA. User: Rajni Date: May 18, 2012 Time: 11:21:48 AM To change this template use File |
 * Settings | File Templates.
 */
@Component
public class AccountingInvoicePdfGenerator {

    private static Logger           logger    = LoggerFactory.getLogger(AccountingInvoicePdfGenerator.class);

    // @Named(Keys.Env.adminDownloads)
    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                          adminDownloadsPath;

    @Autowired
    private B2bUserDetailsDao       b2bUserDetailsDao;

    @Autowired
    private CategoryService         categoryService;

    @Autowired
    BaseDao                         baseDao;

    private InvoiceDto              invoiceDto;
    private B2bUserDetails b2bUserDetails;
    private Category                sexualCareCategory;
    private java.util.List<EnumTax> enumTaxes = Arrays.asList(EnumTax.values());

    public void generateAccountingInvoicePDF(java.util.List<ShippingOrder> shippingOrderList, String pdfFilePath) {
        logger.info("Inside generateAccountingInvoicePDF of AccountingInvoicePdfGenerator .");
        Document orderDetailsDocument = new Document();
        try {
            PdfWriter.getInstance(orderDetailsDocument, new FileOutputStream(pdfFilePath));
            orderDetailsDocument.open();
            if (shippingOrderList != null & shippingOrderList.size() > 0) {
                for (ShippingOrder shippingOrderObj : shippingOrderList) {
                    if (shippingOrderObj != null) {
                        b2bUserDetails = null;
                        String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrderObj.isServiceOrder(), shippingOrderObj.getBaseOrder().getB2bOrder());
                        if (invoiceType.equals(InvoiceNumHelper.PREFIX_FOR_B2B_ORDER)) {
                            b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(shippingOrderObj.getBaseOrder().getUser());
                        }
                        ReplacementOrder replacementOrder = getBaseDao().get(ReplacementOrder.class, shippingOrderObj.getId());

                        if (replacementOrder != null) {
                            invoiceDto = new InvoiceDto(replacementOrder, b2bUserDetails);
                        } else {
                            invoiceDto = new InvoiceDto(shippingOrderObj, b2bUserDetails);
                        }
                    }

                    addOrderDetailsContent(orderDetailsDocument, shippingOrderObj, b2bUserDetails, invoiceDto);
                    orderDetailsDocument.newPage();
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred while generating accounting invoice pdf." + e.getMessage());
        } finally {
            orderDetailsDocument.close();
        }
    }

    private void addOrderDetailsContent(Document document, ShippingOrder shippingOrder, B2bUserDetails b2bUserDetails, InvoiceDto invoiceDto) throws DocumentException,
            MalformedURLException, IOException {
        ReplacementOrder replacementOrder = getBaseDao().get(ReplacementOrder.class, shippingOrder.getId());

        if (replacementOrder != null) {
            invoiceDto = new InvoiceDto(replacementOrder, b2bUserDetails);
        } else {
            invoiceDto = new InvoiceDto(shippingOrder, b2bUserDetails);
        }
        /*
         * String invoiceHeading = null; String stateName = shippingOrder.getBaseOrder().getAddress().getState(); if
         * (b2bUserDetails != null) { String tinNumber = invoiceDto.getB2bUserDetails().getTin(); if
         * (stateName.equalsIgnoreCase("HARYANA") && tinNumber != null) { invoiceHeading = TAX_INVOICE; } else if
         * (stateName.equalsIgnoreCase("MAHARASHTRA") && tinNumber != null) { invoiceHeading = TAX_INVOICE; } else {
         * invoiceHeading = RETAIL_INVOICE; } } else { invoiceHeading = RETAIL_INVOICE; }
         */
        Paragraph preface = new Paragraph();
        Paragraph header = new Paragraph(invoiceDto.getAccountingInvoiceHeading(), new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL));
        header.setAlignment(Element.ALIGN_CENTER);
        preface.add(header);
        addEmptyLine(preface, 1);
        document.add(preface);
        createAddrssDetailTable(document, shippingOrder, invoiceDto);
        createInvoiceDetailTable(document, invoiceDto);
        createTotalSummaryTable(document, invoiceDto);
        createTaxSummaryTable(document, invoiceDto);
        addFooter(document, shippingOrder, b2bUserDetails);
    }

    private void createAddrssDetailTable(Document document, ShippingOrder shippingOrder, InvoiceDto invoiceDto) throws BadElementException, DocumentException {

        Paragraph addressParagraph = new Paragraph();
        Paragraph invoiceDetailParagraph = new Paragraph();
        Paragraph customerAddressParagraph = new Paragraph();
        /* String tinNumber = null; */
        String invoiceNumString = shippingOrder.getAccountingInvoiceNumber() == null ? "" : shippingOrder.getAccountingInvoiceNumber().toString();
        String shipmentDateString = shippingOrder.getShipment().getShipDate() == null ? "" : shippingOrder.getShipment().getShipDate().toString();
        /* String warehouseState = invoiceDto.getWarehouseState(); */
        /*
         * Set<InvoiceLineItemDto> invoiceLineItems = new HashSet<InvoiceLineItemDto>(); invoiceLineItems =
         * invoiceDto.getInvoiceLineItemDtos();
         */
        /*
         * if (warehouseState.equalsIgnoreCase("HARYANA")) { tinNumber = "TIN# 06101832036"; } if
         * (warehouseState.equalsIgnoreCase("MAHARASHTRA")) { tinNumber = "TIN# 27210893736"; }
         */

        // Address paragraph
        if (invoiceDto.getB2bUserDetails() != null) {
            addressParagraph.add(new Paragraph("BrightLifecare Pvt Ltd.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            addressParagraph.add(new Paragraph("3rd Floor, Parshavnath Arcadia,", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            addressParagraph.add(new Paragraph("1, MG Road, Gurgaon, Haryana - 122001", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            addressParagraph.add(new Paragraph(invoiceDto.getInvoiceTinNumber(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            addressParagraph.add(new Paragraph(invoiceDto.getInvoiceDLnumber(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        } else {
            addressParagraph.add(new Paragraph("Aquamarine HealthCare Pvt. Ltd.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            addressParagraph.add(new Paragraph("3rd Floor, Parshavnath Arcadia,", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            addressParagraph.add(new Paragraph("1, MG Road, Gurgaon, Haryana 122001", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            addressParagraph.add(new Paragraph(invoiceDto.getInvoiceTinNumber(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        }

        // Invoice Detail paragraph
        invoiceDetailParagraph.add(new Paragraph("Invoice#:" + invoiceDto.getInvoiceType() + "-" + invoiceNumString, new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        invoiceDetailParagraph.add(new Paragraph("Invoice Date:" + shipmentDateString, new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        invoiceDetailParagraph.add(new Paragraph("Payment Mode:" + shippingOrder.getBaseOrder().getPayment().getPaymentMode().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 8,
                Font.NORMAL)));
        invoiceDetailParagraph.add(new Paragraph("Courier:" + shippingOrder.getShipment().getAwb().getCourier().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        invoiceDetailParagraph.add(new Paragraph("Order#:" + shippingOrder.getGatewayOrderId() + "on" + shippingOrder.getBaseOrder().getPayment().getCreateDate(), new Font(
                Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));

        // Customer Address paragraph
        customerAddressParagraph.add(new Paragraph("Customer:" + shippingOrder.getBaseOrder().getAddress().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        customerAddressParagraph.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getLine1(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        if (shippingOrder.getBaseOrder().getAddress().getLine2() != null) {
            customerAddressParagraph.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getLine2(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        }
        customerAddressParagraph.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getCity() + "," + shippingOrder.getBaseOrder().getAddress().getState() + "-"
                + shippingOrder.getBaseOrder().getAddress().getPincode().getPincode(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        customerAddressParagraph.add(new Paragraph("Ph:" + shippingOrder.getBaseOrder().getAddress().getPhone(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));

        if (invoiceDto.getB2bUserDetails() != null) {
            if (invoiceDto.getB2bUserDetails().getTin() != null) {
                customerAddressParagraph.add(new Paragraph("TIN-" + invoiceDto.getB2bUserDetails().getTin(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));

            }
            if (invoiceDto.getB2bUserDetails().getDlNumber() != null) {
                customerAddressParagraph.add(new Paragraph("DL Number-" + invoiceDto.getB2bUserDetails().getDlNumber(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            }
        }

        PdfPTable addressDetailTable = new PdfPTable(3);
        float[] widths = { 33.33f, 33.33f, 33.33f };
        addressDetailTable.setWidths(widths);
        addressDetailTable.setWidthPercentage(100f);

        PdfPCell c1 = new PdfPCell();
        // c1.setFixedHeight(60f);
        c1.addElement(addressParagraph);
        addressDetailTable.addCell(c1);

        c1 = new PdfPCell();
        // c1.setFixedHeight(60f);
        c1.addElement(invoiceDetailParagraph);
        addressDetailTable.addCell(c1);

        c1 = new PdfPCell();
        // c1.setFixedHeight(60f);
        c1.addElement(customerAddressParagraph);
        addressDetailTable.addCell(c1);
        document.add(addressDetailTable);

    }

    private void createInvoiceDetailTable(Document document, InvoiceDto invoiceDto) throws BadElementException, DocumentException {
        double rate = 0.0;
        double taxable = 0.0;
        double tax = 0.0;
        double surcharge = 0.0;
        double totalTaxable = 0.0;
        double totalTax = 0.0;
        double totalSurcharge = 0.0;

        Paragraph invoiceDetailPara = new Paragraph();
        addEmptyLine(invoiceDetailPara, 1);
        document.add(invoiceDetailPara);
        try {
            // Limiting double values to 2 decimal places.
            totalTaxable = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getTotalTaxable()));
            totalTax = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getTotalTax()));
            totalSurcharge = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getTotalSurcharge()));
        } catch (Exception ex) {
            logger.error("Exception occurred while generating accounting invoice pdf." + ex.getMessage());
        }

        Set<InvoiceLineItemDto> invoiceLineItems = new HashSet<InvoiceLineItemDto>();
        invoiceLineItems = invoiceDto.getInvoiceLineItemDtos();

        PdfPTable invoiceDetailTable = new PdfPTable(7);
        float[] widths = { 40f, 10f, 10f, 10f, 10f, 10f, 10f };
        invoiceDetailTable.setWidths(widths);
        invoiceDetailTable.setWidthPercentage(100f);

        // sexualCareCategory = categoryService.getCategoryByName("sexual-care");
        sexualCareCategory = CategoryCache.getInstance().getCategoryByName("sexual-care").getCategory();

        PdfPCell c1 = new PdfPCell(new Phrase("Item", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Qty", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Rate (per unit)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Tax Rate", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Taxable", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Tax", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Surcharge", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceDetailTable.addCell(c1);

        invoiceDetailTable.setHeaderRows(1);

        if (!invoiceLineItems.isEmpty()) {

            for (InvoiceLineItemDto invoiceLineItemDto : invoiceLineItems) {

                try {
                    // Limiting double values to 2 decimal places.
                    rate = Double.parseDouble(new DecimalFormat("#.##").format(invoiceLineItemDto.getRate()));
                    taxable = Double.parseDouble(new DecimalFormat("#.##").format(invoiceLineItemDto.getTaxable()));
                    tax = Double.parseDouble(new DecimalFormat("#.##").format(invoiceLineItemDto.getTax()));
                    surcharge = Double.parseDouble(new DecimalFormat("#.##").format(invoiceLineItemDto.getSurcharge()));
                } catch (Exception ex) {
                    logger.error("Exception occurred while generating accounting invoice pdf." + ex.getMessage());
                }

                StringBuffer itemDetail = new StringBuffer();
                if (invoiceLineItemDto.getProductCategories().contains(sexualCareCategory)) {
                    itemDetail.append("Personal Care Product");
                } else {
                    itemDetail.append(invoiceLineItemDto.getProductName().toString());
                }

                if (invoiceLineItemDto.getVariantName() != null) {
                    itemDetail.append(invoiceLineItemDto.getVariantName());
                }
                itemDetail.append(invoiceLineItemDto.getProductOptionsPipeSeparated());
                itemDetail.append(invoiceLineItemDto.getExtraOptionsPipeSeparated());
                itemDetail.append(invoiceLineItemDto.getConfigOptionsPipeSeparated());

                invoiceDetailTable.addCell(new PdfPCell(new Phrase(itemDetail.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC))));
                invoiceDetailTable.addCell(new PdfPCell(new Phrase("" + invoiceLineItemDto.getQty(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                invoiceDetailTable.addCell(new PdfPCell(new Phrase("" + rate, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                invoiceDetailTable.addCell(new PdfPCell(new Phrase(invoiceLineItemDto.getTaxRate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                invoiceDetailTable.addCell(new PdfPCell(new Phrase("" + taxable, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                invoiceDetailTable.addCell(new PdfPCell(new Phrase("" + tax, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                invoiceDetailTable.addCell(new PdfPCell(new Phrase("" + surcharge, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
            }
        }

        PdfPCell totalCell = new PdfPCell();
        totalCell.setColspan(4);
        totalCell.addElement(new Phrase("Total", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        invoiceDetailTable.addCell(totalCell);
        invoiceDetailTable.addCell(new PdfPCell(new Phrase("" + totalTaxable, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        invoiceDetailTable.addCell(new PdfPCell(new Phrase("" + totalTax, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        invoiceDetailTable.addCell(new PdfPCell(new Phrase("" + totalSurcharge, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        document.add(invoiceDetailTable);

    }

    private void createTotalSummaryTable(Document document, InvoiceDto invoiceDto) throws BadElementException, DocumentException {
        double shippingCost = 0.0;
        double grandTotal = 0.0;
        double codCharges = 0.0;
        try {
            // Limiting double values to 2 decimal places.
            shippingCost = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getShipping()));
            grandTotal = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getGrandTotal()));
            codCharges = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getCod()));
        } catch (Exception ex) {
            logger.error("Exception occurred while generating accounting invoice pdf." + ex.getMessage());
        }

        Paragraph summaryPara = new Paragraph();
        summaryPara.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(summaryPara, 1);
        document.add(summaryPara);

        PdfPTable totalSummaryTable = new PdfPTable(2);
        float[] widths = { 65f, 35f };
        totalSummaryTable.setWidths(widths);
        totalSummaryTable.setWidthPercentage(100f);

        totalSummaryTable.setHeaderRows(1);

        totalSummaryTable.addCell(new PdfPCell(new Phrase("Shipping cost", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        totalSummaryTable.addCell(new PdfPCell(new Phrase(" " + shippingCost, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        totalSummaryTable.addCell(new PdfPCell(new Phrase("Cod Cost", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        totalSummaryTable.addCell(new PdfPCell(new Phrase(" " + codCharges, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        totalSummaryTable.addCell(new PdfPCell(new Phrase("Grand Total", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        totalSummaryTable.addCell(new PdfPCell(new Phrase(" " + grandTotal, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        document.add(totalSummaryTable);

    }

    private void createTaxSummaryTable(Document document, InvoiceDto invoiceDto) throws BadElementException, DocumentException {
        double amount = 0.0;
        double taxOnAmt = 0.0;
        double surcharge = 0.0;
        double grossAmt = 0.0;
        double totalSummaryAmount = 0.0;
        double totalSummaryTax = 0.0;
        double totalSummarySurcharge = 0.0;
        double totalSummaryPayable = 0.0;

        try {
            // Limiting double values to 2 decimal places.
            totalSummaryAmount = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getTotalSummaryAmount()));
            totalSummaryTax = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getTotalSummaryTax()));
            totalSummarySurcharge = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getTotalSummarySurcharge()));
            totalSummaryPayable = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getTotalSummaryPayable()));
        } catch (Exception ex) {
            logger.error("Exception occurred while generating accounting invoice pdf." + ex.getMessage());
        }

        Paragraph taxSummaryPara = new Paragraph();
        taxSummaryPara.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(taxSummaryPara, 1);
        document.add(taxSummaryPara);
        Paragraph headingPara = new Paragraph(new Phrase("Tax Summary", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        headingPara.setAlignment(Element.ALIGN_CENTER);
        PdfPTable taxSummaryTable = new PdfPTable(6);
        float[] widths = { 10f, 10f, 10f, 10f, 10f, 40f };
        taxSummaryTable.setWidths(widths);
        taxSummaryTable.setWidthPercentage(100f);

        taxSummaryTable.setHeaderRows(1);

        PdfPCell taxSummaryHeaderCell = new PdfPCell();
        taxSummaryHeaderCell.setColspan(6);
        taxSummaryHeaderCell.addElement(headingPara);
        taxSummaryTable.addCell(taxSummaryHeaderCell);

        taxSummaryTable.addCell(new PdfPCell(new Phrase("VAT Percent", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("Qty", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("Amount", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("Tax on Amount", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("Surcharge", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("Gross Amount(Amt+Tax+Surcharge)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));

        // Fetching various maps based on EnumTax and creating rows in table.
        if (enumTaxes.size() > 0) {
            for (EnumTax enumTaxObj : enumTaxes) {
                if (invoiceDto.getSummaryQtyMap().get(enumTaxObj.getName()) != 0l) {
                    amount = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getSummaryAmountMap().get(enumTaxObj.getName())));
                    taxOnAmt = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getSummaryTaxMap().get(enumTaxObj.getName())));
                    surcharge = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getSummarySurchargeMap().get(enumTaxObj.getName())));
                    grossAmt = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getSummaryPayableMap().get(enumTaxObj.getName())));

                    taxSummaryTable.addCell(new PdfPCell(new Phrase(enumTaxObj.getName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                    taxSummaryTable.addCell(new PdfPCell(new Phrase("" + invoiceDto.getSummaryQtyMap().get(enumTaxObj.getName()), new Font(Font.FontFamily.TIMES_ROMAN, 10,
                            Font.NORMAL))));
                    taxSummaryTable.addCell(new PdfPCell(new Phrase("" + amount, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                    taxSummaryTable.addCell(new PdfPCell(new Phrase("" + taxOnAmt, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                    taxSummaryTable.addCell(new PdfPCell(new Phrase("" + surcharge, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                    taxSummaryTable.addCell(new PdfPCell(new Phrase("" + grossAmt, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                }
            }
        }

        // Fetching grand totals and creating row in table.
        taxSummaryTable.addCell(new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("" + invoiceDto.getTotalSummaryQty(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("" + totalSummaryAmount, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("" + totalSummaryTax, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("" + totalSummarySurcharge, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        taxSummaryTable.addCell(new PdfPCell(new Phrase("" + totalSummaryPayable, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));

        document.add(taxSummaryTable);

    }

    private void addFooter(Document document, ShippingOrder shippingOrder, B2bUserDetails b2bUserDetails) throws DocumentException {
        Paragraph footerParagraph = new Paragraph();
        Paragraph authorisedSigntry = new Paragraph(new Phrase("(Authorised Signatory)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        authorisedSigntry.setAlignment(Element.ALIGN_RIGHT);
        footerParagraph.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(footerParagraph, 5);
        footerParagraph.add(new Paragraph("Terms & Conditions:", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        footerParagraph.add(new Paragraph("1. All disputes are subject to Gurgaon Jurisdiction.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        if (b2bUserDetails != null) {
            footerParagraph.add(new Paragraph("2. This is computer generated invoice.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        }
        footerParagraph.setAlignment(Element.ALIGN_RIGHT);
        footerParagraph.add(authorisedSigntry);
        document.add(footerParagraph);
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public InvoiceDto getInvoiceDto() {
        return invoiceDto;
    }

    public B2bUserDetails getB2bUserDetails() {
        return b2bUserDetails;
    }

    public Category getSexualCareCategory() {
        return sexualCareCategory;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
