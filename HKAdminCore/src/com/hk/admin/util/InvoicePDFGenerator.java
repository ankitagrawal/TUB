package com.hk.admin.util;

import com.hk.admin.dto.accounting.InvoiceDto;
import com.hk.admin.dto.accounting.InvoiceLineItemDto;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.cache.CategoryCache;
import com.hk.constants.core.Keys;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.core.WarehouseService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

@Component
public class InvoicePDFGenerator {
    private InvoiceDto      invoiceDto;
    private Coupon          coupon;
    private String          barcodePath;
    private Category        sexualCareCategory;

    private static Logger   logger = LoggerFactory.getLogger(InvoicePDFGenerator.class);

    @Autowired
    ReferrerProgramManager  referrerProgramManager;
    @Autowired
    BarcodeGenerator        barcodeGenerator;
    @Autowired
    CourierService          courierService;
    @Autowired
    AddressDao              addressDao;
    @Autowired
    BaseDao baseDao;
    @Autowired
    WarehouseService warehouseService;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                  adminDownloadsPath;

    @Autowired
    private CategoryService categoryService;

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
        ReplacementOrder replacementOrder = getBaseDao().get(ReplacementOrder.class, shippingOrder.getId());
        if(replacementOrder != null){
          invoiceDto = new InvoiceDto(replacementOrder, null);
        }
        else{
          invoiceDto = new InvoiceDto(shippingOrder, null);
        }
        barcodePath = barcodeGenerator.getBarcodePath(shippingOrder.getGatewayOrderId(),1.0f, 150, false);
        Image barcodeImage = Image.getInstance(barcodePath);

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

        preface.add(new Paragraph("Name & Address", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));

        preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));

        preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getLine1(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));

        if (shippingOrder.getBaseOrder().getAddress().getLine2() != null) {
            preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getLine2(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        }

/*
        if (routingCode != null && !("".equals(routingCode))) {
            preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getCity() + "-" + shippingOrder.getBaseOrder().getAddress().getPin() + "[" + routingCode + "]",
                    new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        } else {
            preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getCity() + "-" + shippingOrder.getBaseOrder().getAddress().getPin(), new Font(
                    Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        }
*/

        preface.add(new Paragraph(shippingOrder.getBaseOrder().getAddress().getState(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));

        preface.add(new Paragraph("Ph:" + shippingOrder.getBaseOrder().getAddress().getPhone(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));

        if (shippingOrder.getBaseOrder().getUserComments() != null) {
            preface.add(new Paragraph("User Instructions :" + shippingOrder.getBaseOrder().getUserComments(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            addEmptyLine(preface, 1);

        }
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Order Details", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        addEmptyLine(preface, 1);

        if(replacementOrder != null){
          preface.add(new Paragraph("Following order is against a previous order no. "+shippingOrder.getId(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        }
      
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

        copyrightsParagraph.add(new Paragraph("Note: This is to certify that items inside do not contain any prohibited or hazardous material. These items are meant for personal use only and are not for resale.", new Font(
                Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
        // addEmptyLine(copyrightsParagraph,1);
      Warehouse shippingWarehouse = warehouseService.findShippingWarehouse(shippingOrder);
      String warehouseAddress = shippingWarehouse.getName() + " | " + shippingWarehouse.getLine1() + " | " + shippingWarehouse.getLine2() + " | "
          + shippingWarehouse.getCity() + " | " + shippingWarehouse.getState() + " - " + shippingWarehouse.getPincode()
          + " TIN:" + shippingWarehouse.getTin();
      copyrightsParagraph.add(new Paragraph(warehouseAddress, new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));

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
        
        //sexualCareCategory = getCategoryService().getCategoryByName("sexual-care");
        sexualCareCategory = CategoryCache.getInstance().getCategoryByName("sexual-care").getCategory();

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

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public BaseDao getBaseDao() {
      return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
    }

    public CourierService getCourierService() {
        return courierService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }
}
