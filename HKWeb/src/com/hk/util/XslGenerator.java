package com.hk.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;

import com.hk.domain.order.OrderPaymentReconciliation;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.XslConstants;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.hkDelivery.HkdeliveryPaymentReconciliation;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.io.HkXlsWriter;

@Component
public class XslGenerator {

	private static Logger         logger             = Logger.getLogger(XslGenerator.class);

	public static final String    ID                 = "ID";
	public static final String    PINCODE            = "PINCODE";
	public static final String    IS_PREFERRED       = "IS_PREFERRED";
	public static final String    IS_PREFERRED_COD   = "IS_PREFERRED_COD";
	public static final String    ROUTING_CODE       = "ROUTING_CODE";
	public static final String    COD_AVAILABLE      = "COD_AVAILABLE";
	public static final String    COURIER_ID         = "COURIER_ID";
	public static final String    IS_DELETED         = "IS_DELETED";
	public static final String    CITY               = "CITY";
	public static final String    STATE              = "STATE";
	public static final String    LOCALITY           = "LOCALITY";
	public static final String    DEFAULT_COURIER_ID = "DEFAULT_COURIER_ID";

	public static final String WAREHOUSE             = "WAREHOUSE";
	public static final String COD_COURIER_ID        = "COD_COURIER_ID";
	public static final String TECH_PROCESS_COURIER_ID = "TECH_PROCESS_COURIER_ID";
//	public static final String ESTIMATED_SHIPPING_COST_COD = "ESTIMATED_SHIPPING_COST_COD";
    public static final String ESTIMATED_SHIPPING_COST     =        "ESTIMATED_SHIPPING_COST";
    public static final String    GROUND_SHIPPING_AVAILABLE      = "GROUND_SHIPPING_AVAILABLE";
    public static final String    COD_ON_GROUND_SHIPPING         = "COD_ON_GROUND_SHIPPING";

	@Autowired
	private CourierService courierService;
	@Autowired
	private InventoryService      inventoryService;
	@Autowired
	private AdminInventoryService adminInventoryService;
	/* @Autowired
	private SkuService                    skuService;*/

	private Map<String, Integer>  headerMap          = new HashMap<String, Integer>();

	public File generateCatalogExcel(List<Product> products, String xslFilePath) throws Exception {

		File file = new File(xslFilePath);
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		Workbook wb = new HSSFWorkbook();

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setColor(Font.COLOR_NORMAL);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		Sheet sheet1 = wb.createSheet("Product");
		Row row = sheet1.createRow(0);
		row.setHeightInPoints((short) 25);

		int totalColumnNo = 54;

		Set<Manufacturer> manufacturers = new HashSet<Manufacturer>();
		Cell cell;
		for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
			cell = row.createCell(columnNo);
			cell.setCellStyle(style);
		}

		int cellCounter = 0;
		setHeaderCellValue(row, cellCounter++, XslConstants.PRODUCT_ID);
		setHeaderCellValue(row, cellCounter++, XslConstants.CATEGORY);
		setHeaderCellValue(row, cellCounter++, XslConstants.PRIMARY_CATEGORY);
		setHeaderCellValue(row, cellCounter++, XslConstants.SECONDARY_CATEGORY);
		setHeaderCellValue(row, cellCounter++, XslConstants.PRODUCT_NAME);
		setHeaderCellValue(row, cellCounter++, XslConstants.SORTING);
		setHeaderCellValue(row, cellCounter++, XslConstants.BRAND);
		setHeaderCellValue(row, cellCounter++, XslConstants.MANUFACTURER);
		setHeaderCellValue(row, cellCounter++, XslConstants.SUPPLIER_TIN);
		setHeaderCellValue(row, cellCounter++, XslConstants.SUPPLIER_STATE);
		setHeaderCellValue(row, cellCounter++, XslConstants.MIN_DAYS_TO_PROCESS);
		setHeaderCellValue(row, cellCounter++, XslConstants.MAX_DAYS_TO_PROCESS);
    setHeaderCellValue(row, cellCounter++, XslConstants.IS_DELETED);
    setHeaderCellValue(row, cellCounter++, XslConstants.OUT_OF_STOCK);
    setHeaderCellValue(row, cellCounter++, XslConstants.GROUND_SHIPPING_AVAILABLE);
    setHeaderCellValue(row, cellCounter++, XslConstants.IS_HIDDEN);
		setHeaderCellValue(row, cellCounter++, XslConstants.OVERVIEW);
		setHeaderCellValue(row, cellCounter++, XslConstants.DESCRIPTION);
		setHeaderCellValue(row, cellCounter++, XslConstants.RELATED_PRODUCTS);
		setHeaderCellValue(row, cellCounter++, XslConstants.COLOR_PRODUCT);
		setHeaderCellValue(row, cellCounter++, XslConstants.IS_SERVICE);
		setHeaderCellValue(row, cellCounter++, XslConstants.IS_GOOGLE_AD_DISALLOWED);
		setHeaderCellValue(row, cellCounter++, XslConstants.IS_JIT);
		setHeaderCellValue(row, cellCounter++, XslConstants.VARIANT_ID);
		setHeaderCellValue(row, cellCounter++, XslConstants.VARIANT_SORTING);
		setHeaderCellValue(row, cellCounter++, XslConstants.VARIANT_NAME);
		setHeaderCellValue(row, cellCounter++, XslConstants.OPTIONS);
		setHeaderCellValue(row, cellCounter++, XslConstants.EXTRA_OPTIONS);
		setHeaderCellValue(row, cellCounter++, XslConstants.MRP);
		setHeaderCellValue(row, cellCounter++, XslConstants.COST);
		setHeaderCellValue(row, cellCounter++, XslConstants.TAX);
		setHeaderCellValue(row, cellCounter++, XslConstants.PERCENTAGE_DISCOUNT);
		setHeaderCellValue(row, cellCounter++, XslConstants.HK_PRICE);
		setHeaderCellValue(row, cellCounter++, XslConstants.POSTPAID_AMOUNT);
		setHeaderCellValue(row, cellCounter++, XslConstants.SHIPPING);
		setHeaderCellValue(row, cellCounter++, XslConstants.MARGIN_HK_CP);
		setHeaderCellValue(row, cellCounter++, XslConstants.MARGIN_MRP_CP);
		setHeaderCellValue(row, cellCounter++, XslConstants.AVAILABILITY);
		setHeaderCellValue(row, cellCounter++, XslConstants.MANUFACTURER_CODE);
		setHeaderCellValue(row, cellCounter++, XslConstants.DELETED);
		setHeaderCellValue(row, cellCounter++, XslConstants.INVENTORY);
		setHeaderCellValue(row, cellCounter++, XslConstants.CUTOFF_INVENTORY);
		setHeaderCellValue(row, cellCounter++, XslConstants.AFFILIATE_CATEGORY);
		setHeaderCellValue(row, cellCounter++, XslConstants.COLOR_HEX);
		setHeaderCellValue(row, cellCounter++, XslConstants.MAIN_IMAGE_ID);
		setHeaderCellValue(row, cellCounter++, XslConstants.LENGTH);
		setHeaderCellValue(row, cellCounter++, XslConstants.BREADTH);
		setHeaderCellValue(row, cellCounter++, XslConstants.HEIGHT);
		setHeaderCellValue(row, cellCounter++, XslConstants.WEIGHT);
		setHeaderCellValue(row, cellCounter++, XslConstants.UPC);
		setHeaderCellValue(row, cellCounter++, XslConstants.SERVICE_TYPE);
		setHeaderCellValue(row, cellCounter++, XslConstants.PAYMENT_TYPE);
		setHeaderCellValue(row, cellCounter++, XslConstants.OTHER_REMARK);
		setHeaderCellValue(row, cellCounter++, XslConstants.SUPPLIER_CODE);

		int initialRowNo = 1;
		for (Product product : products) {
			if (product.getManufacturer() != null) {
				manufacturers.add(product.getManufacturer());
			}
			int variantCtr = 0;

			for (ProductVariant productVariant : product.getProductVariants()) {
				variantCtr++;
				row = sheet1.createRow(initialRowNo);
				for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
					row.createCell(columnNo);
				}
				if (variantCtr == 1) {
					setCellValue(row, this.getColumnIndex(XslConstants.PRODUCT_ID), product.getId());
					setCellValue(row, this.getColumnIndex(XslConstants.CATEGORY), getCategories(product));
					if (product.getPrimaryCategory() != null) {
						setCellValue(row, this.getColumnIndex(XslConstants.PRIMARY_CATEGORY), product.getPrimaryCategory().getDisplayName());
					} else {
						setCellValue(row, this.getColumnIndex(XslConstants.PRIMARY_CATEGORY), "");
					}
					if (product.getSecondaryCategory() != null) {
						setCellValue(row, this.getColumnIndex(XslConstants.SECONDARY_CATEGORY), product.getSecondaryCategory().getDisplayName());
					} else {
						setCellValue(row, this.getColumnIndex(XslConstants.SECONDARY_CATEGORY), "");
					}
					setCellValue(row, this.getColumnIndex(XslConstants.PRODUCT_NAME), product.getName());
					setCellValue(row, this.getColumnIndex(XslConstants.SORTING), product.getOrderRanking());
					setCellValue(row, this.getColumnIndex(XslConstants.BRAND), product.getBrand());
					if (product.getManufacturer() != null) {
						setCellValue(row, this.getColumnIndex(XslConstants.MANUFACTURER), product.getManufacturer().getName());
					}
					if (product.getSupplier() != null) {
						setCellValue(row, this.getColumnIndex(XslConstants.SUPPLIER_TIN), product.getSupplier().getTinNumber());
						setCellValue(row, this.getColumnIndex(XslConstants.SUPPLIER_STATE), product.getSupplier().getState());
					}
					setCellValue(row, this.getColumnIndex(XslConstants.MIN_DAYS_TO_PROCESS), product.getMinDays());
					setCellValue(row, this.getColumnIndex(XslConstants.MAX_DAYS_TO_PROCESS), product.getMaxDays());
          setCellValue(row, this.getColumnIndex(XslConstants.IS_DELETED), product.isDeleted() !=null ? product.isDeleted() ? "Y" : "N" : null);
          setCellValue(row, this.getColumnIndex(XslConstants.OUT_OF_STOCK), product.isOutOfStock()!=null ? product.isOutOfStock() ? "Y": "N" : null);
          setCellValue(row, this.getColumnIndex(XslConstants.GROUND_SHIPPING_AVAILABLE),product.isGroundShipping() ? "Y" : "N");
          setCellValue(row, this.getColumnIndex(XslConstants.IS_HIDDEN), product.isHidden()!=null ? product.isHidden() ? "Y" : "N" : null);
					setCellValue(row, this.getColumnIndex(XslConstants.OVERVIEW), "");
					setCellValue(row, this.getColumnIndex(XslConstants.DESCRIPTION), "");
					setCellValue(row, this.getColumnIndex(XslConstants.RELATED_PRODUCTS), getRelatedProducts(product));
					setCellValue(row, this.getColumnIndex(XslConstants.COLOR_PRODUCT), product.isProductHaveColorOptions() != null ? product.isProductHaveColorOptions() ? "Y"
							: "N" : null);
					setCellValue(row, this.getColumnIndex(XslConstants.IS_SERVICE), product.isService() != null ? product.isService() ? "Y" : "N" : null);
					setCellValue(row, this.getColumnIndex(XslConstants.IS_GOOGLE_AD_DISALLOWED), product.isGoogleAdDisallowed() != null ? product.isGoogleAdDisallowed() ? "Y"
							: "N" : null);
					setCellValue(row, this.getColumnIndex(XslConstants.IS_JIT), product.isJit() != null ? product.isJit() ? "Y" : "N" : null);
				}
				if (productVariant.getId().startsWith(product.getId() + "-")) {
					setCellValue(row, this.getColumnIndex(XslConstants.VARIANT_ID), productVariant.getId());
					setCellValue(row, this.getColumnIndex(XslConstants.VARIANT_SORTING), productVariant.getOrderRanking());
					setCellValue(row, this.getColumnIndex(XslConstants.VARIANT_NAME), productVariant.getVariantName());
					setCellValue(row, this.getColumnIndex(XslConstants.OPTIONS), getOptions(productVariant));
					setCellValue(row, this.getColumnIndex(XslConstants.EXTRA_OPTIONS), getExtraOptions(productVariant));
					setCellValue(row, this.getColumnIndex(XslConstants.MRP), productVariant.getMarkedPrice());
					setCellValue(row, this.getColumnIndex(XslConstants.COST), productVariant.getCostPrice());
					setCellValue(row, this.getColumnIndex(XslConstants.POSTPAID_AMOUNT), productVariant.getPostpaidAmount());
					setCellValue(row, this.getColumnIndex(XslConstants.HK_PRICE), productVariant.getHkPrice(null));

					// not applicable now as part of catalog.

					/* setCellValue(row, this.getColumnIndex(XslParser.TAX), productVariant.getTax().getName()); */
					setCellValue(row, this.getColumnIndex(XslConstants.PERCENTAGE_DISCOUNT), productVariant.getDiscountPercent());
					setCellValue(row, this.getColumnIndex(XslConstants.SHIPPING), productVariant.getShippingBasePrice());
					if(productVariant.getHkPrice() > 0) {
						setCellValue(row, this.getColumnIndex(XslConstants.MARGIN_HK_CP), (productVariant.getHkPrice() - productVariant.getCostPrice())/productVariant.getHkPrice() * 100);
					} else {
						setCellValue(row, this.getColumnIndex(XslConstants.MARGIN_HK_CP), 0L);
					}
					if(productVariant.getMarkedPrice() > 0) {
						setCellValue(row, this.getColumnIndex(XslConstants.MARGIN_MRP_CP), (productVariant.getMarkedPrice() - productVariant.getCostPrice())/productVariant.getMarkedPrice() * 100);
					} else {
						setCellValue(row, this.getColumnIndex(XslConstants.MARGIN_MRP_CP), 0L);
					}

					setCellValue(row, this.getColumnIndex(XslConstants.AVAILABILITY), productVariant.isOutOfStock() ? "N" : "Y");
					setCellValue(row, this.getColumnIndex(XslConstants.MANUFACTURER_CODE), "");
					setCellValue(row, this.getColumnIndex(XslConstants.DELETED), productVariant.isDeleted() ? "Y" : "N");
					Long inventory = getAdminInventoryService().getNetInventory(productVariant);
					setCellValue(row, this.getColumnIndex(XslConstants.INVENTORY), inventory);
					Long cutoffInventory = inventoryService.getAggregateCutoffInventory(productVariant);
					setCellValue(row, this.getColumnIndex(XslConstants.CUTOFF_INVENTORY), cutoffInventory);
					setCellValue(row, this.getColumnIndex(XslConstants.AFFILIATE_CATEGORY),
							productVariant.getAffiliateCategory() != null ? productVariant.getAffiliateCategory().getAffiliateCategoryName() : "");
					setCellValue(row, this.getColumnIndex(XslConstants.COLOR_HEX), productVariant.getColorHex() == null ? "" : productVariant.getColorHex());
					setCellValue(row, this.getColumnIndex(XslConstants.MAIN_IMAGE_ID), productVariant.getMainImageId() == null ? null : productVariant.getMainImageId());
					setCellValue(row, this.getColumnIndex(XslConstants.LENGTH), productVariant.getLength() == null ? null : productVariant.getLength());
					setCellValue(row, this.getColumnIndex(XslConstants.BREADTH), productVariant.getBreadth() == null ? null : productVariant.getBreadth());
					setCellValue(row, this.getColumnIndex(XslConstants.HEIGHT), productVariant.getHeight() == null ? null : productVariant.getHeight());
					setCellValue(row, this.getColumnIndex(XslConstants.WEIGHT), productVariant.getWeight() == null ? null : productVariant.getWeight());
					setCellValue(row, this.getColumnIndex(XslConstants.UPC), productVariant.getUpc() == null ? null : productVariant.getUpc());
					setCellValue(row, this.getColumnIndex(XslConstants.SERVICE_TYPE), productVariant.getServiceType() != null ? productVariant.getServiceType().getName() : null);
					setCellValue(row, this.getColumnIndex(XslConstants.PAYMENT_TYPE), productVariant.getPaymentType() != null ? productVariant.getPaymentType().getName() : null);
					setCellValue(row, this.getColumnIndex(XslConstants.OTHER_REMARK), productVariant.getOtherRemark() == null ? null : productVariant.getOtherRemark());
					setCellValue(row, this.getColumnIndex(XslConstants.SUPPLIER_CODE), productVariant.getSupplierCode() == null ? null : productVariant.getSupplierCode());
					initialRowNo++;
				}
			}
		}

		Sheet sheet2 = wb.createSheet("Manufacturer");
		int manufacturerColumnNo = 10;
		row = sheet2.createRow(0);
		row.setHeightInPoints((short) 25);

		for (int columnNo = 0; columnNo < manufacturerColumnNo; columnNo++) {
			cell = row.createCell(columnNo);
			cell.setCellStyle(style);
		}
		setCellValue(row, 0, XslConstants.MANUFACTURER_NAME);
		setCellValue(row, 1, XslConstants.MANUFACTURER_WEBSITE);
		setCellValue(row, 2, XslConstants.MANUFACTURER_DESCRIPTION);
		setCellValue(row, 3, XslConstants.MANUFACTURER_EMAIL);
		setCellValue(row, 4, XslConstants.MANUFACTURER_PAN_INDIA);

		int rowNo = 1;
		for (Manufacturer manufacturer : manufacturers) {
			row = sheet2.createRow(rowNo);
			for (int cellNo = 0; cellNo < manufacturerColumnNo; cellNo++) {
				cell = row.createCell(cellNo);
			}
			rowNo++;
		}
		rowNo = 1;
		for (Manufacturer manufacturer : manufacturers) {
			row = sheet2.getRow(rowNo);
			setCellValue(row, 0, manufacturer.getName());
			setCellValue(row, 1, manufacturer.getWebsite());
			setCellValue(row, 2, manufacturer.getDescription());
			setCellValue(row, 3, manufacturer.getEmail());
			setCellValue(row, 4, manufacturer.isAvailableAllOverIndia() == null ? "N" : (manufacturer.isAvailableAllOverIndia() ? "Y" : "N"));
			rowNo++;
		}

		wb.write(out);
		out.close();
		return file;
	}

	public File generateCouerierServiceInfoXsl(List<CourierServiceInfo> courierServiceInfoList, String xslFilePath) throws Exception {
        
		 File file = new File(xslFilePath);
        file.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(file);
        Workbook wb = new HSSFWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setColor(Font.COLOR_NORMAL);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet1 = wb.createSheet("CourierServiceInfo");
        Row row = sheet1.createRow(0);
        row.setHeightInPoints((short) 25);

        int totalColumnNo = 10;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, PINCODE);
        setCellValue(row, 1, COURIER_ID);
        setCellValue(row, 2, COD_AVAILABLE);
        setCellValue(row, 3, IS_PREFERRED);
        setCellValue(row, 4, IS_PREFERRED_COD);
        setCellValue(row, 5, ROUTING_CODE);
        setCellValue(row, 6, IS_DELETED);
        setCellValue(row, 7, GROUND_SHIPPING_AVAILABLE);
        setCellValue(row, 8, COD_ON_GROUND_SHIPPING);

        int initialRowNo = 1;
        for (CourierServiceInfo courierServiceInfo : courierServiceInfoList) {

            row = sheet1.createRow(initialRowNo);
            for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                row.createCell(columnNo);
            }

            Pincode pincode = courierServiceInfo.getPincode();
            setCellValue(row, 0, pincode.getPincode());
            setCellValue(row, 1, courierServiceInfo.getCourier().getId());
            setCellValue(row, 2, courierServiceInfo.isCodAvailable() ? "Y" : "N");
            setCellValue(row, 3, courierServiceInfo.isPreferred() ? "Y" : "N");
            setCellValue(row, 4, courierServiceInfo.isPreferredCod() ? "Y" : "N");
            setCellValue(row, 5, courierServiceInfo.getRoutingCode());
            setCellValue(row, 6, courierServiceInfo.isDeleted() ? "Y" : "N");
            setCellValue(row, 7, courierServiceInfo.isGroundShippingAvailable() ? "Y" : "N");
            setCellValue(row, 8, courierServiceInfo.isCodAvailableOnGroundShipping() ? "Y" : "N");

            initialRowNo++;

        }

        Sheet sheet2 = wb.createSheet("Courier IDs");
        row = sheet2.createRow(0);
        row.setHeightInPoints((short) 25);

        int totalColumnNo2 = 2;

        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, "Courier");
        setCellValue(row, 1, "ID");

        int initialRowNo2 = 1;
        List<Courier> courierList = courierService.getAllCouriers();
        for (Courier courier : courierList) {
            row = sheet2.createRow(initialRowNo2);
            for (int columnNo = 0; columnNo < totalColumnNo2; columnNo++) {
                row.createCell(columnNo);
            }

            setCellValue(row, 0, courier.getName());
            setCellValue(row, 1, courier.getId());

            initialRowNo2++;
        }

        wb.write(out);
        out.close();
        return file;
	}

	public File generateGRNXsl(GoodsReceivedNote goodsReceivedNote, String xslFilePath) throws Exception {
		AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);
		File file = new File(xslFilePath);
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		Workbook wb = new HSSFWorkbook();

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setColor(Font.COLOR_NORMAL);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		Sheet sheet1 = wb.createSheet("GRN-" + goodsReceivedNote.getId());
		Row row = sheet1.createRow(0);
		row.setHeightInPoints((short) 25);

		int totalColumnNo = 10;

		Cell cell;
		for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
			cell = row.createCell(columnNo);
			cell.setCellStyle(style);
		}
		setCellValue(row, 0, XslConstants.GRN_LINE_ITEM_ID);
		setCellValue(row, 1, XslConstants.VARIANT_ID);
		setCellValue(row, 2, XslConstants.QTY);
		setCellValue(row, 3, XslConstants.COST);
		setCellValue(row, 4, XslConstants.MRP);
		setCellValue(row, 5, XslConstants.BATCH_NUMBER);
		setCellValue(row, 6, XslConstants.MFG_DATE);
		setCellValue(row, 7, XslConstants.EXP_DATE);
		setCellValue(row, 8, XslConstants.CHECKIN_QTY);

		int initialRowNo = 1;
		for (GrnLineItem grnLineItem : goodsReceivedNote.getGrnLineItems()) {

			row = sheet1.createRow(initialRowNo);
			for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
				row.createCell(columnNo);
			}

			setCellValue(row, 0, grnLineItem.getId());
			setCellValue(row, 1, grnLineItem.getProductVariant().getId());
			setCellValue(row, 2, grnLineItem.getQty());
			setCellValue(row, 3, grnLineItem.getCostPrice());
			setCellValue(row, 4, grnLineItem.getMrp());
			setCellValue(row, 5, "");
			setCellValue(row, 6, "");
			setCellValue(row, 7, "");
			setCellValue(row, 8, adminInventoryService.countOfCheckedInUnitsForGrnLineItem(grnLineItem));

			initialRowNo++;
		}

		wb.write(out);
		out.close();
		return file;
	}

	public File generatePincodeXsl(List<Pincode> pincodeList, String xslFilePath) throws Exception {

		File file = new File(xslFilePath);
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		Workbook wb = new HSSFWorkbook();

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setColor(Font.COLOR_NORMAL);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		Sheet sheet1 = wb.createSheet("PincodeInfo");
		Row row = sheet1.createRow(0);
		row.setHeightInPoints((short) 25);

		int totalColumnNo = 7;

		Cell cell;
		for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
			cell = row.createCell(columnNo);
			cell.setCellStyle(style);
		}
		setCellValue(row, 0, ID);
		setCellValue(row, 1, PINCODE);
		setCellValue(row, 2, CITY);
		setCellValue(row, 3, STATE);
		setCellValue(row, 4, LOCALITY);
		setCellValue(row, 5, DEFAULT_COURIER_ID);

		int initialRowNo = 1;
		for (Pincode pincode : pincodeList) {

			row = sheet1.createRow(initialRowNo);
			for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
				row.createCell(columnNo);
			}

			setCellValue(row, 0, pincode.getId());
			setCellValue(row, 1, pincode.getPincode());
			setCellValue(row, 2, pincode.getCity().getName());
			setCellValue(row, 3, pincode.getState().getName());
			setCellValue(row, 4, pincode.getLocality());
			setCellValue(row, 5, pincode.getDefaultCourier() != null ? pincode.getDefaultCourier().getId() : null);

			initialRowNo++;
		}

		wb.write(out);
		out.close();
		return file;
	}

	public File generatePincodeDefaultCourierXsl(List<PincodeDefaultCourier> pincodeDefaultCourierList, String xslFilePath) throws Exception {
       File file = new File(xslFilePath);
        file.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(file);
        Workbook wb = new HSSFWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setColor(Font.COLOR_NORMAL);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet1 = wb.createSheet(XslConstants.DEFAULT_COURIER_SHEET);
        Row row = sheet1.createRow(0);
        row.setHeightInPoints((short) 25);

        int totalColumnNo = 8;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, PINCODE);
        setCellValue(row, 1, WAREHOUSE);
        setCellValue(row, 2, COURIER_ID);
        setCellValue(row, 3, COD_AVAILABLE);
        setCellValue(row, 4, GROUND_SHIPPING_AVAILABLE);
        setCellValue(row, 5, ESTIMATED_SHIPPING_COST);

        int initialRowNo = 1;
        for (PincodeDefaultCourier pincodeDefaultCourier : pincodeDefaultCourierList) {

            row = sheet1.createRow(initialRowNo);
            for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                row.createCell(columnNo);
            }

            setCellValue(row, 0, pincodeDefaultCourier.getPincode().getPincode());
            setCellValue(row, 1, pincodeDefaultCourier.getWarehouse().getId());
            if(pincodeDefaultCourier.getCourier() != null){
                setCellValue(row, 2, pincodeDefaultCourier.getCourier().getId());
            }
            else{
                setCellValue(row, 2, -1L);
            }
            setCellValue(row, 3, pincodeDefaultCourier.isCod() ? "Y" : "N"  );
            setCellValue(row, 4, pincodeDefaultCourier.isGroundShipping() ? "Y" : "N" );
            setCellValue(row, 5, pincodeDefaultCourier.getEstimatedShippingCost());

            initialRowNo++;
        }

        wb.write(out);
        out.close();
        return file;
	}

	public File generateGRNListExcel(File xlsFile, List<GoodsReceivedNote> grnList) {
		HkXlsWriter xlsWriter = new HkXlsWriter();

		if (grnList != null) {
			int xlsRow = 1;
			xlsWriter.addHeader(XslConstants.GRN_ID, XslConstants.GRN_ID);
			xlsWriter.addHeader(XslConstants.PO_ID, XslConstants.PO_ID);
			xlsWriter.addHeader(XslConstants.INVOICE_NO, XslConstants.INVOICE_NO);
			xlsWriter.addHeader(XslConstants.RECEIVED_BY, XslConstants.RECEIVED_BY);
			xlsWriter.addHeader(XslConstants.WAREHOUSE, XslConstants.WAREHOUSE);
			xlsWriter.addHeader(XslConstants.SUPPLIER_NAME, XslConstants.SUPPLIER_NAME);
			xlsWriter.addHeader(XslConstants.SUPPLIER_TIN, XslConstants.SUPPLIER_TIN);
			xlsWriter.addHeader(XslConstants.STATUS, XslConstants.STATUS);
			xlsWriter.addHeader(XslConstants.RECONCILED, XslConstants.RECONCILED);
			xlsWriter.addHeader(XslConstants.PAYABLE, XslConstants.PAYABLE);

			for (GoodsReceivedNote goodsReceivedNote : grnList) {
				xlsWriter.addCell(xlsRow, goodsReceivedNote.getId());
				xlsWriter.addCell(xlsRow, goodsReceivedNote.getPurchaseOrder().getId());
				xlsWriter.addCell(xlsRow, goodsReceivedNote.getInvoiceNumber());
				if(goodsReceivedNote.getReceivedBy() != null) {
					xlsWriter.addCell(xlsRow, goodsReceivedNote.getReceivedBy().getName());
				} else {
					xlsWriter.addCell(xlsRow, null);
				}

				if(goodsReceivedNote.getWarehouse() != null) {
					xlsWriter.addCell(xlsRow, goodsReceivedNote.getWarehouse().getName());
				}else {
					xlsWriter.addCell(xlsRow, null);
				}
				xlsWriter.addCell(xlsRow, goodsReceivedNote.getPurchaseOrder().getSupplier().getName());
				xlsWriter.addCell(xlsRow, goodsReceivedNote.getPurchaseOrder().getSupplier().getTinNumber());
				xlsWriter.addCell(xlsRow, goodsReceivedNote.getGrnStatus().getName());
				xlsWriter.addCell(xlsRow, goodsReceivedNote.getReconciled()!=null && goodsReceivedNote.getReconciled().booleanValue() ? "Yes" : "No");
				xlsWriter.addCell(xlsRow, goodsReceivedNote.getPayable());

				xlsRow++;
			}
			xlsWriter.writeData(xlsFile, XslConstants.GRN_LIST_SHEET);
		}
		return xlsFile;
	}

	public File generatePOListExcel(File xlsFile, List<PurchaseOrder> purchaseOrderList) {
		HkXlsWriter xlsWriter = new HkXlsWriter();

		if (purchaseOrderList != null) {
			int xlsRow = 1;
			xlsWriter.addHeader(XslConstants.PO_ID, XslConstants.PO_ID);
			xlsWriter.addHeader(XslConstants.CREATE_DATE, XslConstants.CREATE_DATE);
			xlsWriter.addHeader(XslConstants.CREATED_BY, XslConstants.CREATED_BY);
			xlsWriter.addHeader(XslConstants.NO_OF_SKU, XslConstants.NO_OF_SKU);
			xlsWriter.addHeader(XslConstants.APPROVER, XslConstants.APPROVER);
			xlsWriter.addHeader(XslConstants.SUPPLIER_NAME, XslConstants.SUPPLIER_NAME);
			xlsWriter.addHeader(XslConstants.SUPPLIER_TIN, XslConstants.SUPPLIER_TIN);
			xlsWriter.addHeader(XslConstants.WAREHOUSE, XslConstants.WAREHOUSE);
			xlsWriter.addHeader(XslConstants.STATUS, XslConstants.STATUS);
			xlsWriter.addHeader(XslConstants.LAST_UPDATED_DATE, XslConstants.LAST_UPDATED_DATE);
			xlsWriter.addHeader(XslConstants.PAYABLE, XslConstants.PAYABLE);

			for (PurchaseOrder purchaseOrder : purchaseOrderList) {
				xlsWriter.addCell(xlsRow, purchaseOrder.getId());
				xlsWriter.addCell(xlsRow, purchaseOrder.getCreateDate());
				xlsWriter.addCell(xlsRow, purchaseOrder.getCreatedBy().getName());
				xlsWriter.addCell(xlsRow, purchaseOrder.getNoOfSku());
				if(purchaseOrder.getApprovedBy() != null) {
					xlsWriter.addCell(xlsRow, purchaseOrder.getApprovedBy().getName());
				} else {
					xlsWriter.addCell(xlsRow, purchaseOrder.getApprovedBy());
				}
				xlsWriter.addCell(xlsRow, purchaseOrder.getSupplier().getName());
				xlsWriter.addCell(xlsRow, purchaseOrder.getSupplier().getTinNumber());
				if(purchaseOrder.getWarehouse() != null) {
					xlsWriter.addCell(xlsRow, purchaseOrder.getWarehouse().getName());
				} else {
					xlsWriter.addCell(xlsRow, null);
				}

				xlsWriter.addCell(xlsRow, purchaseOrder.getPurchaseOrderStatus().getName());
				xlsWriter.addCell(xlsRow, purchaseOrder.getUpdateDate());
				xlsWriter.addCell(xlsRow, purchaseOrder.getPayable());

				xlsRow++;
			}
			xlsWriter.writeData(xlsFile, "POList");
		}
		return xlsFile;
	}

	public File generateSupplierListExcel(File xlsFile, List<Supplier> supplierList) {
		HkXlsWriter xlsWriter = new HkXlsWriter();
		int xlsRow = 1;
		xlsWriter.addHeader(XslConstants.SUPPLIER_NAME, XslConstants.SUPPLIER_NAME);
		xlsWriter.addHeader(XslConstants.SUPPLIER_TIN, XslConstants.SUPPLIER_TIN);
		xlsWriter.addHeader(XslConstants.ADDRESS, XslConstants.ADDRESS);
		xlsWriter.addHeader(XslConstants.CONTACT_PERSON, XslConstants.CONTACT_PERSON);
		xlsWriter.addHeader(XslConstants.CONTACT_NUMBER, XslConstants.CONTACT_NUMBER);
		xlsWriter.addHeader(XslConstants.CREDIT_DAYS, XslConstants.CREDIT_DAYS);
		xlsWriter.addHeader(XslConstants.TARGET_CREDIT_DAYS, XslConstants.TARGET_CREDIT_DAYS);
		xlsWriter.addHeader(XslConstants.LEAD_TIME, XslConstants.LEAD_TIME);
		xlsWriter.addHeader(XslConstants.ACTIVE, XslConstants.ACTIVE);
		xlsWriter.addHeader(XslConstants.MARGIN, XslConstants.MARGIN);
		xlsWriter.addHeader(XslConstants.BRAND, XslConstants.BRAND);
		xlsWriter.addHeader(XslConstants.VALIDITY_TERMS_OF_TRADE, XslConstants.VALIDITY_TERMS_OF_TRADE);

		for (Supplier supplier : supplierList) {
			xlsWriter.addCell(xlsRow, supplier.getName());
			xlsWriter.addCell(xlsRow, supplier.getTinNumber());
			StringBuffer supplierAddress = new StringBuffer();

			if(supplier.getLine1() != null) {
				supplierAddress.append(supplier.getLine1());
				supplierAddress.append(", ");
			}
			if(supplier.getLine2() != null) {
				supplierAddress.append(supplier.getLine2());
				supplierAddress.append(", ");
			}
			if(supplier.getCity() != null) {
				supplierAddress.append(supplier.getCity());
				supplierAddress.append(", ");
			}
			if(supplier.getPincode() != null) {
				supplierAddress.append(supplier.getPincode());
				supplierAddress.append(", ");
			}
			if(supplier.getState() != null) {
				supplierAddress.append(supplier.getState());
			}

			xlsWriter.addCell(xlsRow, supplierAddress.toString());
			xlsWriter.addCell(xlsRow, supplier.getContactPerson());
			xlsWriter.addCell(xlsRow, supplier.getContactNumber());
			xlsWriter.addCell(xlsRow, supplier.getCreditDays());
			xlsWriter.addCell(xlsRow, supplier.getTargetCreditDays());
			xlsWriter.addCell(xlsRow, supplier.getLeadTime());
			xlsWriter.addCell(xlsRow, supplier.getActive());
			xlsWriter.addCell(xlsRow, supplier.getMargins());
			xlsWriter.addCell(xlsRow, supplier.getBrandName());
			xlsWriter.addCell(xlsRow, supplier.getTOT());
			xlsRow++;
		}
		xlsWriter.writeData(xlsFile, "SupplierList");
		return xlsFile;
	}

	public File generateExcelForCOD(File excelFile, List<OrderPaymentReconciliation> orderPaymentReconciliationList) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		HkXlsWriter xlsWriter = new HkXlsWriter();
		xlsWriter.addHeader("SHIPPING ORDER ID", "SHIPPING ORDER ID");
		xlsWriter.addHeader("GATEWAY ORDER ID", "GATEWAY ORDER ID");
		xlsWriter.addHeader("COURIER", "COURIER");
		xlsWriter.addHeader("SHIPMENT DATE", "SHIPMENT DATE");
		xlsWriter.addHeader("ACTUAL AMOUNT", "ACTUAL AMOUNT");
		xlsWriter.addHeader("AMOUNT RECONCILED", "AMOUNT RECONCILED");
		xlsWriter.addHeader("AMOUNT DIFFERENCE", "AMOUNT DIFFERENCE");

		int row = 1;
		for (OrderPaymentReconciliation orderPaymentReconciliation : orderPaymentReconciliationList) {
			xlsWriter.addCell(row, orderPaymentReconciliation.getShippingOrder());
			xlsWriter.addCell(row, orderPaymentReconciliation.getShippingOrder().getGatewayOrderId());

			if(orderPaymentReconciliation.getShippingOrder().getShipment() != null && orderPaymentReconciliation.getShippingOrder().getShipment().getAwb() != null
					&& orderPaymentReconciliation.getShippingOrder().getShipment().getAwb().getCourier() != null) {
				xlsWriter.addCell(row, orderPaymentReconciliation.getShippingOrder().getShipment().getAwb().getCourier().getName());
			} else {
				xlsWriter.addCell(row, "");
			}

			if(orderPaymentReconciliation.getShippingOrder().getShipment() != null) {
				xlsWriter.addCell(row, sdf.format( orderPaymentReconciliation.getShippingOrder().getShipment().getShipDate() ));
			} else {
				xlsWriter.addCell(row, "");
			}

			if(orderPaymentReconciliation.getShippingOrder().getAmount() != null) {
				xlsWriter.addCell(row, orderPaymentReconciliation.getShippingOrder().getAmount());
			} else {
				xlsWriter.addCell(row, "");
			}

			xlsWriter.addCell(row, orderPaymentReconciliation.getReconciledAmount());

			if(orderPaymentReconciliation.getShippingOrder().getAmount() != null) {
				xlsWriter.addCell(row, orderPaymentReconciliation.getShippingOrder().getAmount().doubleValue() - orderPaymentReconciliation.getReconciledAmount().doubleValue());
			} else {
				xlsWriter.addCell(row, "");
			}

			row++;
		}
		xlsWriter.writeData(excelFile, "COD");
		return excelFile;
	}

	public File generateExcelForPrepaid(File excelFile, List<OrderPaymentReconciliation> orderPaymentReconciliationList) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		HkXlsWriter xlsWriter = new HkXlsWriter();
		xlsWriter.addHeader("BASE ORDER ID", "BASE ORDER ID");
		xlsWriter.addHeader("GATEWAY ORDER ID", "GATEWAY ORDER ID");
		xlsWriter.addHeader("PAYMENT MODE", "PAYMENT MODE");
		xlsWriter.addHeader("PAYMENT DATE", "PAYMENT DATE");
		xlsWriter.addHeader("ACTUAL AMOUNT", "ACTUAL AMOUNT");
		xlsWriter.addHeader("AMOUNT RECONCILED", "AMOUNT RECONCILED");
		xlsWriter.addHeader("AMOUNT DIFFERENCE", "AMOUNT DIFFERENCE");

		int row = 1;
		for (OrderPaymentReconciliation orderPaymentReconciliation : orderPaymentReconciliationList) {
			xlsWriter.addCell(row, orderPaymentReconciliation.getBaseOrder());
			xlsWriter.addCell(row, orderPaymentReconciliation.getBaseOrder().getGatewayOrderId());

			if(orderPaymentReconciliation.getBaseOrder().getPayment() != null) {
				xlsWriter.addCell(row, orderPaymentReconciliation.getBaseOrder().getPayment().getPaymentMode().getName());
			} else {
				xlsWriter.addCell(row, "");
			}

			if(orderPaymentReconciliation.getBaseOrder().getPayment() != null && orderPaymentReconciliation.getBaseOrder().getPayment().getPaymentDate() != null) {
				xlsWriter.addCell(row, sdf.format(orderPaymentReconciliation.getBaseOrder().getPayment().getPaymentDate()));
			} else {
				xlsWriter.addCell(row, "");
			}

			if(orderPaymentReconciliation.getBaseOrder() != null) {
				xlsWriter.addCell(row, orderPaymentReconciliation.getBaseOrder().getAmount());
			} else {
				xlsWriter.addCell(row, "");
			}

			xlsWriter.addCell(row, orderPaymentReconciliation.getReconciledAmount());
			if(orderPaymentReconciliation.getBaseOrder().getAmount() != null) {
				xlsWriter.addCell(row, orderPaymentReconciliation.getBaseOrder().getAmount().doubleValue() - orderPaymentReconciliation.getReconciledAmount().doubleValue());
			} else {
				xlsWriter.addCell(row, "");
			}

			row++;
		}
		xlsWriter.writeData(excelFile, "Prepaid");
		return excelFile;

	}


	public File generateHKDPaymentReconciliationXls(String xslFilePath, HkdeliveryPaymentReconciliation hkdPaymentReconciliation) throws IOException {
		File file = new File(xslFilePath);
		FileOutputStream out = new FileOutputStream(file);
		Workbook wb = new HSSFWorkbook();
		Sheet sheet1 = wb.createSheet("HKDelivery Payment Reconciliation"+ new Date());
		Row row = null;
		Cell cell = null;
		int rowCounter = 0;
		int totalColumnNoInSheet1 = 6;
		//creating different styles for different elements of excel.
		CellStyle style = wb.createCellStyle();
		CellStyle style_header = wb.createCellStyle();
		CellStyle style_data = wb.createCellStyle();

		//setting borders for all cells in sheet.
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);

		style_data.setBorderTop(CellStyle.BORDER_THIN);
		style_data.setBorderBottom(CellStyle.BORDER_THIN);
		style_data.setBorderLeft(CellStyle.BORDER_THIN);
		style_data.setBorderRight(CellStyle.BORDER_THIN);
		//enabling addition of new lines in cells(using "\n" )
		style_data.setWrapText(true);

		//creating different fonts for the excel data.

		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setColor(Font.COLOR_NORMAL);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);

		Font fontForHeader = wb.createFont();
		fontForHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontForHeader.setFontHeightInPoints((short) 16);
		style_header.setFont(fontForHeader);
		style_header.setAlignment(HSSFCellStyle.ALIGN_CENTER);


		//creating rows for header.

		row = sheet1.createRow(++rowCounter);
		cell = row.createCell(2);
		cell.setCellStyle(style_header);
		setCellValue(row, 2,"HKDelivery Payment Reconciliation");

		row = sheet1.createRow(++rowCounter);
		for (int i = 0; i < 6; i++) {
			sheet1.autoSizeColumn(i);
			cell = row.createCell(i);
			cell.setCellStyle(style);
		}
		setCellValue(row, 0, "Reconciliation Date");
		setCellValue(row, 1, hkdPaymentReconciliation.getCreateDate()+"");
		setCellValue(row, 2, "Expected COD Amt");
		setCellValue(row, 3, hkdPaymentReconciliation.getExpectedAmount()+"");
		setCellValue(row, 4, "Actual Amount");
		setCellValue(row, 5, hkdPaymentReconciliation.getActualAmount()+"");

		++rowCounter;
		//addEmptyLine(row, sheet1, ++rowCounter, cell);


		row = sheet1.createRow(++rowCounter);
		// style.setFont(font);
		for (int i = 0; i < 6; i++) {
			sheet1.autoSizeColumn(i);
			cell = row.createCell(i);
			cell.setCellStyle(style);
		}
		//Date currentDate = new Date();
		setCellValue(row, 0, "Reconciliation Done By");
		setCellValue(row, 1, hkdPaymentReconciliation.getUser().getName());
		setCellValue(row, 2, "Remarks");
		setCellValue(row, 3, hkdPaymentReconciliation.getRemarks());
		setCellValue(row, 4, "");
		setCellValue(row, 5, "");
		//addEmptyLine(row, sheet1, ++rowCounter, cell);
		row = sheet1.createRow(++rowCounter);
		for (int i = 0; i < 6; i++) {
			sheet1.autoSizeColumn(i);
			cell = row.createCell(i);
			cell.setCellStyle(style);
		}
		setCellValue(row, 0, "Sl No.");
		setCellValue(row, 1, "Awb Number");
		setCellValue(row, 2, "Cnn Number(GatewayOrder Id)");
		setCellValue(row, 3, "Amount");
		setCellValue(row, 4,"Payment Mode");
		setCellValue(row, 5, "Consignment Status");

		int slNumber = 0;
		for (Consignment consignment: hkdPaymentReconciliation.getConsignments()) {
			rowCounter++;
			row = sheet1.createRow(rowCounter);
			for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
				sheet1.autoSizeColumn(columnNo);
				cell = row.createCell(columnNo);
				cell.setCellStyle(style_data);
			}
			++slNumber;
			setCellValue(row, 0,slNumber+"");
			setCellValue(row, 1,consignment.getAwbNumber());
			setCellValue(row, 2, consignment.getCnnNumber());
			setCellValue(row, 3, consignment.getAmount());
			setCellValue(row, 4, consignment.getPaymentMode());
			setCellValue(row, 5, consignment.getConsignmentStatus().getStatus());


		}
		wb.write(out);
		out.close();
		return file;
	}

	private void setCellValue(Row row, int column, Double cellValue) {
		if (cellValue != null) {
			Cell cell = row.getCell(column);
			cell.setCellValue(cellValue);
		}
	}

	private void setCellValue(Row row, int column, Long cellValue) {
		if (cellValue != null) {
			Cell cell = row.getCell(column);
			cell.setCellValue(cellValue);
		}
	}

	private void setCellValue(Row row, int column, String cellValue) {
		if (cellValue == null)
			cellValue = "";
		Cell cell = row.getCell(column);
		cell.setCellValue(cellValue);
	}

	private void setHeaderCellValue(Row row, int column, String cellValue) {
		if (cellValue == null)
			cellValue = "";
		Cell cell = row.getCell(column);
		cell.setCellValue(cellValue);

		this.setRowMap(cellValue, column);
	}

	public String getCategories(Product product) {
		String result = "";
		for (Category category : product.getCategories()) {
			result = result + category.getDisplayName() + "|";
		}
		if (!result.isEmpty())
			result = result.charAt(result.length() - 1) == '|' ? result.substring(0, result.length() - 1) : result;
		return result;
	}

	private String getRelatedProducts(Product product) {
		String result = "";
		for (Product relatedProduct : product.getRelatedProducts()) {
			result = result + relatedProduct.getId() + "|";
		}
		if (!result.isEmpty())
			result = result.charAt(result.length() - 1) == '|' ? result.substring(0, result.length() - 1) : result;
		return result;
	}

	private String getOptions(ProductVariant productVariant) {
		String result = "";
		for (ProductOption productOption : productVariant.getProductOptions()) {
			result = result + productOption.getName() + ":" + productOption.getValue() + "|";
		}
		if (!result.isEmpty())
			result = result.charAt(result.length() - 1) == '|' ? result.substring(0, result.length() - 1) : result;
		return result;
	}

	private String getExtraOptions(ProductVariant productVariant) {
		String result = "";
		for (ProductExtraOption productExtraOption : productVariant.getProductExtraOptions()) {
			result = result + productExtraOption.getName() + ":" + productExtraOption.getValue() + "|";
		}
		if (!result.isEmpty())
			result = result.charAt(result.length() - 1) == '|' ? result.substring(0, result.length() - 1) : result;
		return result;
	}

	private void setRowMap(String columnHeader, int columnIndex) {
		headerMap.put(columnHeader, columnIndex);
	}

	private Integer getColumnIndex(String columnHeader) {
		return headerMap.get(columnHeader);
	}

	public AdminInventoryService getAdminInventoryService() {
		return adminInventoryService;
	}

	public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
		this.adminInventoryService = adminInventoryService;
	}

}
