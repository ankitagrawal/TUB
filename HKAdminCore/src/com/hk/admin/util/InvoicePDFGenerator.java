package com.hk.admin.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import org.krysalis.barcode4j.BarcodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.hk.admin.dto.accounting.InvoiceDto;
import com.hk.admin.dto.accounting.InvoiceLineItemDto;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.constants.core.EnumRole;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.shipment.EnumCourier;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.Address;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.core.AddressDao;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class InvoicePDFGenerator {
    private InvoiceDto     invoiceDto;
    private Coupon         coupon;
    private String         barcodePath;
    private Category       sexualCareCategory;

    private static Logger  logger = LoggerFactory.getLogger(InvoicePDFGenerator.class);

    @Autowired
    ReferrerProgramManager referrerProgramManager;
    @Autowired
    BarcodeGenerator       barcodeGenerator;
    @Autowired
    CategoryDao            categoryDao;
    @Autowired
    CourierServiceInfoDao  courierServiceInfoDao;
    @Autowired
    AddressDao             addressDao;

    // @Named(Keys.Env.adminDownloads)
    @Value("#{hkEnvProps['adminDownloads']}")
    String                 adminDownloadsPath;

    /*
     * public void generateInvoicePDF(ShippingOrder shippingOrder) { try { Document orderDetailsDocument = new
     * Document(); PdfWriter.getInstance(orderDetailsDocument, new FileOutputStream(FILE)); coupon =
     * referrerProgramManager.getOrCreateRefferrerCoupon(shippingOrder.getBaseOrder().getUser());
     * orderDetailsDocument.open(); addOrderDetailsContent(orderDetailsDocument, shippingOrder, coupon);
     * orderDetailsDocument.close(); } catch (Exception e) { logger.error("Exception occurred while generating pdf." +
     * e.getMessage()); } k }
     */

    public void generateMasterInvoicePDF(java.util.List<ShippingOrder> shippingOrderList, String pdfFilePath) {
        Document orderDetailsDocument = new Document();
        try {
            PdfWriter.getInstance(orderDetailsDocument, new FileOutputStream(pdfFilePath));
            orderDetailsDocument.open();
            if (shippingOrderList != null & shippingOrderList.size() > 0) {
                for (ShippingOrder shippingOrderObj : shippingOrderList) {
                    coupon = referrerProgramManager.getOrCreateRefferrerCoupon(shippingOrderObj.getBaseOrder().getUser());
                    addOrderDetailsContent(orderDetailsDocument, shippingOrderObj, coupon);
                    orderDetailsDocument.newPage();
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred while generating pdf." + e.getMessage());
        } finally {
            orderDetailsDocument.close();
        }
    }

    private void addOrderDetailsContent(Document document, ShippingOrder shippingOrder, Coupon coupon) throws DocumentException, MalformedURLException, IOException {
        InvoiceDto invoiceDto = new InvoiceDto(shippingOrder, null);
        barcodePath = barcodeGenerator.getBarcodePath(shippingOrder.getGatewayOrderId());
        Image barcodeImage = Image.getInstance(barcodePath);
        String routingCode = null;
        Address address = addressDao.find(shippingOrder.getBaseOrder().getAddress().getId());
        boolean isCod = shippingOrder.isCOD();
        CourierServiceInfo courierServiceInfo = null;
        if (EnumCourier.BlueDart_COD.getId() == shippingOrder.getShipment().getCourier().getId()) {
            if (isCod) {
                courierServiceInfo = courierServiceInfoDao.getCourierServiceByPincodeAndCourier(EnumCourier.BlueDart_COD.getId(), address.getPin(), true);
            } else {
                courierServiceInfo = courierServiceInfoDao.getCourierServiceByPincodeAndCourier(EnumCourier.BlueDart.getId(), address.getPin(), false);
            }
            if (courierServiceInfo != null) {
                routingCode = courierServiceInfo.getRoutingCode();
            }
        }

        Paragraph preface = new Paragraph();
        Paragraph header = new Paragraph("ORDER INVOICE", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL));
        header.setAlignment(Element.ALIGN_CENTER);
        preface.add(header);
        preface.add(Image.getInstance(barcodeImage));
        /*
         * Paragraph logoParagraph = new Paragraph(); logoParagraph.setAlignment(Element.ALIGN_RIGHT); Image logoImage =
         * getLogoImage(shippingOrder); logoParagraph.add(logoImage); preface.add(logoParagraph);
         */
        // createAlignmentTable(preface, shippingOrder, coupon);
        preface.add(new Paragraph("Proforma Invoice for Order :" + shippingOrder.getGatewayOrderId(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        preface.add(new Paragraph("Placed On :" + shippingOrder.getBaseOrder().getPayment().getCreateDate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        // full fillment center and return location,
        preface.add(new Paragraph("Fulfillment Centre :" + shippingOrder.getWarehouse().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        if (shippingOrder.getWarehouse().getId() == 1) {
            preface.add(new Paragraph("Return Location: DEL/ITG/111117", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        } else if (shippingOrder.getWarehouse().getId() == 2) {
            preface.add(new Paragraph("Return Location: BOM/BPT/421302", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        }
        long codId = EnumPaymentMode.COD.getId();
        if (shippingOrder.getBaseOrder().getPayment().getPaymentMode().getId() == codId) {
            preface.add(new Paragraph("Cash on delivery : Rs." + invoiceDto.getGrandTotal(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        }
        preface.add(new Paragraph("Please do not accept if the box is tampered", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        addEmptyLine(preface, 1);

        /*
         * if (!shippingOrder.getBaseOrder().getUser().equals("support@madeinhealth.com")) { Paragraph referAndEarnPara =
         * new Paragraph(); referAndEarnPara.setAlignment(Element.ALIGN_RIGHT);
         * referAndEarnPara.setIndentationLeft(335f); referAndEarnPara.add(new Paragraph( "Introducing the Refer and
         * Earn program", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD))); referAndEarnPara.add(new Paragraph(
         * "Your referral coupon code :", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
         *//*
             * referAndEarnPara.add(new Paragraph(coupon.getCode().toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN,
             * 10, Font.BOLD)));
             *//*
             * referAndEarnPara.add(new Paragraph( "How it works:", new Font(Font.FontFamily.TIMES_ROMAN, 8,
             * Font.NORMAL))); referAndEarnPara.add(new Paragraph( "Pass this coupon code to your friends and
             * family.They", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL))); referAndEarnPara.add(new
             * Paragraph( "get a Rs. 100 discount on their first purchase* at", new Font(Font.FontFamily.TIMES_ROMAN, 8,
             * Font.NORMAL))); referAndEarnPara.add(new Paragraph( "healthkart.com and you get reward points worth Rs.
             * 100", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL))); referAndEarnPara.add(new Paragraph( "in
             * your account for your referral*.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
             * preface.add(referAndEarnPara); }
             */
        preface.add(new Paragraph("Name & Address", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));

        preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));

        preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getLine1(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));

        if (shippingOrder.getBaseOrder().getAddress().getLine2() != null) {
            preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getLine2(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        }

        if (routingCode != null && !("".equals(routingCode))) {
            preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getCity() + "-" + shippingOrder.getBaseOrder().getAddress().getPin() + "[" + routingCode + "]",
                    new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        } else {
            preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getCity() + "-" + shippingOrder.getBaseOrder().getAddress().getPin(), new Font(
                    Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        }

        preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getState(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));

        preface.add(new Paragraph("Ph:" + shippingOrder.getBaseOrder().getAddress().getPhone(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));

        /*
         * if (shippingOrder.getBaseOrder().getOfferInstance() != null) { preface.add(new Paragraph("You have won a
         * Complementary Coupon!", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))); preface.add(new
         * Paragraph(shippingOrder.getBaseOrder().getOfferInstance().getOffer().getDescription(), new
         * Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))); preface.add(new Paragraph("Your Complementary Coupon
         * Code :" + shippingOrder.getBaseOrder().getOfferInstance().getCoupon().getComplimentaryCoupon(), new
         * Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))); }
         */

        if (shippingOrder.getBaseOrder().getUserComments() != null) {
            preface.add(new Paragraph("User Instructions :" + shippingOrder.getBaseOrder().getUserComments(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            addEmptyLine(preface, 1);

        }
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Order Details", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        addEmptyLine(preface, 1);
        document.add(preface);
        createOrderDetailTable(document, invoiceDto);
        createOrderSummaryTable(document, invoiceDto);
        addFooter(shippingOrder, document);
    }

    private void addFooter(ShippingOrder shippingOrder, Document document) throws DocumentException {
        Paragraph copyrightsParagraph = new Paragraph();
        copyrightsParagraph.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(copyrightsParagraph, 5);

        copyrightsParagraph.add(new Paragraph("Note: This is to certify that items inside do not contain any prohibited or hazardous material.", new Font(
                Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
        // addEmptyLine(copyrightsParagraph,1);
        if (shippingOrder.getBaseOrder().getUser().getRoles().contains(EnumRole.B2B_USER.getRoleName())) {
            copyrightsParagraph.add(new Paragraph("Bright Lifecare Pvt. Ltd. | Khasra No. 146/25/2/1, Jail Road, Dhumaspur, Badshahpur |"
                    + " Gurgaon, Haryana- 122101 | TIN:06101832036", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        } else {
            copyrightsParagraph.add(new Paragraph("Aquamarine Healthcare Pvt. Ltd." + "|" + shippingOrder.getWarehouse().getLine1() + "|" + shippingOrder.getWarehouse().getLine2()
                    + "|" + shippingOrder.getWarehouse().getCity() + "," + shippingOrder.getWarehouse().getState() + "-" + shippingOrder.getWarehouse().getPincode() + "|" + ""
                    + "TIN :" + shippingOrder.getWarehouse().getTin(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
        }
        document.add(copyrightsParagraph);
    }

    private Image getLogoImage(ShippingOrder shippingOrder) {
        Image logo = null;
        URL myURL = null;
        try {
            if (shippingOrder.getBaseOrder().getUser().equals("support@madeinhealth.com")) {
                myURL = this.getClass().getClassLoader().getResource("mih-logo.jpg");
            } else {
                myURL = this.getClass().getClassLoader().getResource("logo.gif");
            }

            String filePath = null;
            if (myURL != null) {
                filePath = myURL.getPath();
            }

            logo = Image.getInstance(filePath);
            logo.setAbsolutePosition(50, 800); // Code 1
            logo.scaleAbsolute(170, 40); // Code 2

        } catch (Exception e) {
            logger.error("Exception occurred while forming image url." + e.getMessage());
        }
        return logo;
    }

    private void createOrderDetailTable(Document document, InvoiceDto invoiceDto) throws BadElementException, DocumentException {

        Set<InvoiceLineItemDto> invoiceLineItems = new HashSet<InvoiceLineItemDto>();
        invoiceLineItems = invoiceDto.getInvoiceLineItemDtos();

        PdfPTable orderDetailTable = new PdfPTable(4);
        float[] widths = { 50f, 15f, 15f, 15f };
        orderDetailTable.setWidths(widths);
        orderDetailTable.setWidthPercentage(100f);
        sexualCareCategory = categoryDao.find("sexual-care");

        PdfPCell c1 = new PdfPCell(new Phrase("Item", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        orderDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Quantity", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        orderDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Unit Price", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        orderDetailTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total(Rs.)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        orderDetailTable.addCell(c1);

        orderDetailTable.setHeaderRows(1);

        if (!invoiceLineItems.isEmpty()) {
            for (InvoiceLineItemDto invoiceLineItemDto : invoiceLineItems) {
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

                orderDetailTable.addCell(new PdfPCell(new Phrase(itemDetail.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC))));
                orderDetailTable.addCell(new PdfPCell(new Phrase("" + invoiceLineItemDto.getQty(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                orderDetailTable.addCell(new PdfPCell(new Phrase("" + invoiceLineItemDto.getHkPrice(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
                orderDetailTable.addCell(new PdfPCell(new Phrase("" + invoiceLineItemDto.getLineItemTotal(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
            }
        }
        document.add(orderDetailTable);

    }

    private void createOrderSummaryTable(Document document, InvoiceDto invoiceDto) throws BadElementException, DocumentException {
        double grandTotal = 0.0;
        double itemTotal = 0.0;
        double shipping = 0.0;
        double redeemPoints = 0.0;
        double discount = 0.0;
        double codCharges = 0.0;
        try {
            grandTotal = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getGrandTotal()));
            redeemPoints = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getRewardPoints()));
            itemTotal = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getItemsTotal()));
            shipping = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getShipping()));
            discount = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getTotalDiscount()));
            codCharges = Double.parseDouble(new DecimalFormat("#.##").format(invoiceDto.getCod()));
        } catch (Exception ex) {
            ex.getMessage();
        }
        Paragraph emptyPara = new Paragraph();
        addEmptyLine(emptyPara, 1);
        document.add(emptyPara);
        Paragraph orderSummaryPara = new Paragraph("Order Summary", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
        addEmptyLine(orderSummaryPara, 1);
        document.add(orderSummaryPara);
        Set<InvoiceLineItemDto> invoiceLineItems = new HashSet<InvoiceLineItemDto>();
        invoiceLineItems = invoiceDto.getInvoiceLineItemDtos();
        PdfPTable orderSummaryTable = new PdfPTable(2);
        float[] widths = { 50f, 50f };
        orderSummaryTable.setWidths(widths);
        orderSummaryTable.setWidthPercentage(100f);

        orderSummaryTable.setHeaderRows(1);

        orderSummaryTable.addCell(new PdfPCell(new Phrase("Item Total", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        orderSummaryTable.addCell(new PdfPCell(new Phrase("Rs." + itemTotal, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        orderSummaryTable.addCell(new PdfPCell(new Phrase("Shipping", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        orderSummaryTable.addCell(new PdfPCell(new Phrase("Rs." + shipping, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        orderSummaryTable.addCell(new PdfPCell(new Phrase("Discount", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        orderSummaryTable.addCell(new PdfPCell(new Phrase("Rs." + discount, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        if (invoiceDto.getRewardPoints() > 0.0) {
            orderSummaryTable.addCell(new PdfPCell(new Phrase("Redeemed Rewards Points", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
            orderSummaryTable.addCell(new PdfPCell(new Phrase("Rs." + redeemPoints, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        }
        if (invoiceDto.getCod() > 0.0) {
            orderSummaryTable.addCell(new PdfPCell(new Phrase("COD Charges", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
            orderSummaryTable.addCell(new PdfPCell(new Phrase("Rs." + codCharges, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))));
        }
        orderSummaryTable.addCell(new PdfPCell(new Phrase("Grand Total", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        orderSummaryTable.addCell(new PdfPCell(new Phrase("Rs." + grandTotal, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD))));
        document.add(orderSummaryTable);

    }

    /*
     * private void createAlignmentTable(Paragraph document, ShippingOrder shippingOrder, Coupon coupon) throws
     * BadElementException, DocumentException { PdfPTable table = new PdfPTable(2); float[] widths = {50f, 50f};
     * table.setWidths(widths); table.setWidthPercentage(100f); //
     * table.getDefaultCell().setBorder(Rectangle.NO_BORDER); //table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
     * table.getDefaultCell().setFixedHeight(50f); table.setHeaderRows(1); PdfPCell cellLeft = new PdfPCell();
     * //cellLeft.setBorder(0); cellLeft.setBorderWidth(0f); Paragraph leftPara = new Paragraph(); leftPara.add(new
     * Paragraph( "Proforma Invoice for Order ::::::" + shippingOrder.getGatewayOrderId(), new
     * Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL))); leftPara.add(new Paragraph( "Placed On :" +
     * shippingOrder.getBaseOrder().getPayment().getCreateDate(), new Font(Font.FontFamily.TIMES_ROMAN, 10,
     * Font.NORMAL))); cellLeft.addElement(leftPara); table.addCell(cellLeft); PdfPCell cellRight = new PdfPCell();
     * cellRight.setBorderWidth(0f); Paragraph rightPara = new Paragraph(); rightPara.add(new Paragraph( "Introducing
     * the Refer and Earn programmmmmmm", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD))); rightPara.add(new
     * Paragraph( "Your referral coupon code :", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
     * rightPara.add(new Paragraph(coupon.getCode().toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 10,
     * Font.BOLD))); rightPara.add(new Paragraph( "How it works:", new Font(Font.FontFamily.TIMES_ROMAN, 8,
     * Font.NORMAL))); rightPara.add(new Paragraph( "Pass this coupon code to your friends and family.They", new
     * Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL))); rightPara.add(new Paragraph( "get a Rs. 100 discount on
     * their first purchase* at", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL))); rightPara.add(new Paragraph(
     * "healthkart.com and you get reward points worth Rs. 100", new Font(Font.FontFamily.TIMES_ROMAN, 8,
     * Font.NORMAL))); rightPara.add(new Paragraph( "in your account for your referral*.", new
     * Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL))); cellRight.addElement(rightPara); table.addCell(cellRight);
     *//*
         * table.addCell(new PdfPCell(new Phrase(""))); table.addCell(new PdfPCell(new Phrase("Introducing the Refer and
         * Earn program", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)))); table.addCell(new PdfPCell(new
         * Phrase(""))); table.addCell(new PdfPCell(new Phrase("Your referral coupon code :", new
         * Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)))); table.addCell(new PdfPCell(new Phrase("")));
         * table.addCell(new PdfPCell(new Phrase("" + coupon.getCode().toUpperCase(), new
         * Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)))); table.addCell(new PdfPCell(new Phrase("Proforma Invoice
         * for Order :", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)))); table.addCell(new PdfPCell(new
         * Phrase("How it works:", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)))); table.addCell(new
         * PdfPCell(new Phrase("Placed On :" + shippingOrder.getBaseOrder().getPayment().getCreateDate(), new
         * Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)))); table.addCell(new PdfPCell(new Phrase("Pass this
         * coupon code to your friends and family.They", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)))); long
         * codId = EnumPaymentMode.COD.getId(); if (shippingOrder.getBaseOrder().getPayment().getPaymentMode().getId() ==
         * codId) { table.addCell(new PdfPCell(new Phrase("Cash on delivery : Rs." + invoiceDto.getGrandTotal(), new
         * Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)))); } else { table.addCell(new PdfPCell(new Phrase(""))); }
         * table.addCell(new PdfPCell(new Phrase("get a Rs. 100 discount on their first purchase* at", new
         * Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)))); table.addCell(new PdfPCell(new Phrase("Please do not
         * accept if the box is tampered", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)))); table.addCell(new
         * PdfPCell(new Phrase("healthkart.com and you get reward points worth Rs. 100", new
         * Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)))); table.addCell(new PdfPCell(new Phrase("")));
         * table.addCell(new PdfPCell(new Phrase("in your account for your referral*.", new
         * Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)))); table.addCell(new PdfPCell(new Phrase("")));
         *//*
         * document.add(table); }
         */

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public InvoiceDto getInvoiceDto() {
        return invoiceDto;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public String getBarcodePath() {
        return barcodePath;
    }
}
