package com.hk.util;

import com.hk.admin.pact.dao.marketing.AmazonFeedDao;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.domain.amazon.AmazonFeed;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.DoomDayDao;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Component
public class AmazonXslGenerator {

    public static final String ID                      = "ID";

    @Autowired
    AmazonFeedDao              amazonFeedDao;
    @Autowired
    DoomDayDao                 doomDayDao;
    @Autowired
    ProductVariantDao          productVariantDao;

    public static final String SKU                     = "SKU";
    public static final String RECOMMENDED_BROWSE_NODE = "Recommended Browse Node";
    public static final String DESCRIPTION             = "Description";
    public static final String TITLE                   = "Title";
    public static final String Image                   = "Image";
    public static final String LINK                    = "Link";
    public static final String PRICE                   = "Price";
    public static final String DELIVERY_TIME           = "Delivery Time";
    public static final String STANDARD_PRODUCT_ID     = "Standard Product ID";
    public static final String PRODUCT_ID_TYPE         = "Product ID Type";
    public static final String CATEGORY                = "Category";
    public static final String SHIPPING_COST           = "Shipping Cost";
    public static final String LIST_PRICE              = "List Price";
    public static final String AVAILABILITY            = "Availability";
    public static final String BRAND                   = "Brand";
    public static final String MANUFACTURER            = "Manufacturer";
    public static final String PRODUCT_OPTIONS         = "PRODUCT_OPTIONS";
    public static final String MFR_PART_NUMBER         = "Mfr part number";
    public static final String MODEL_NUMBER            = "Model Number";
    public static final String COLOUR_NAME             = "Colour Name";
    public static final String COLOUR_MAP              = "Colour Map";
    public static final String ITEM_PACKAGE_QTY        = "Item package quantity";
    public static final String WARRANTY                = "Warranty";
    public static final String AGE                     = "Age";
    public static final String GENDER                  = "Gender";
    public static final String FLAVOUR                 = "Flavour";
    public static final String SCENT                   = "Scent";
    public static final String THEME_HPC               = "Theme HPC";
    public static final String BATTERY_TYPE            = "Battery Type";
    public static final String BATTERIES_INCLUDED      = "Batteries Included";
    public static final String BATTERIES_REQUIRED      = "Batteries Required";
    public static final String POWER_SOURCE            = "Power Source";
    public static final String POWER_ADAPTER_INCLUDED  = "Power Adapter Included";
    public static final String ASSEMBLY_REQUIRED       = "Assembly Required";
    public static final String SHIPPING_WEIGHT         = "Shipping Weight";
    public static final String WEIGHT                  = "Weight";
    public static final String LENGTH                  = "Length";
    public static final String HEIGHT                  = "Height";
    public static final String WIDTH                   = "Width";
    public static final String KEYWORD_S1              = "Keywords1";
    public static final String KEYWORD_S2              = "Keywords2";
    public static final String KEYWORD_S3              = "Keywords3";
    public static final String KEYWORD_S4              = "Keywords4";
    public static final String KEYWORD_S5              = "Keywords5";
    public static final String BULLET_POINT1           = "Bullet point1";
    public static final String BULLET_POINT2           = "Bullet point2";
    public static final String BULLET_POINT3           = "Bullet point3";
    public static final String BULLET_POINT4           = "Bullet point4";
    public static final String BULLET_POINT5           = "Bullet point5";
    public static final String OTHER_IMAGE_URL1        = "Other image-url1";
    public static final String OTHER_IMAGE_URL2        = "Other image-url2";
    public static final String OTHER_IMAGE_URL3        = "Other image-url3";
    public static final String OTHER_IMAGE_URL4        = "Other image-url4";
    public static final String OTHER_IMAGE_URL5        = "Other image-url5";
    public static final String OFFER_NOTE              = "Offer note";
    public static final String IS_GIFT_WRAP_AVAILABLE  = "Is Gift Wrap Available";
    public static final String REGISTERED_PARAMETER    = "Registered Parameter";

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
        Sheet sheet1 = wb.createSheet("Amazon");

        Row row = sheet1.createRow(0);
        int totalColumnNo = 75;
        for (int columnNo = 0; columnNo < 2; columnNo++) {
            row.createCell(columnNo);
        }
        setCellValue(row, 0, "Amazon.com Product Ads Header");
        setCellValue(row, 1, "Purge-Replace=false");

        row = sheet1.createRow(1);
        row.setHeightInPoints((short) 25);

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, SKU);
        setCellValue(row, 1, TITLE);
        setCellValue(row, 2, PRODUCT_OPTIONS);
        setCellValue(row, 3, LINK); // link
        setCellValue(row, 4, PRICE); // HK_PRICE
        setCellValue(row, 5, DELIVERY_TIME); // DELIVERY TIME
        setCellValue(row, 6, RECOMMENDED_BROWSE_NODE); // AMAZON CATEGORY
        setCellValue(row, 7, STANDARD_PRODUCT_ID);
        setCellValue(row, 8, PRODUCT_ID_TYPE);
        setCellValue(row, 9, CATEGORY); // DEFAULT AMAZON CATEGORY
        setCellValue(row, 10, DESCRIPTION); // OVERVIEW
        setCellValue(row, 11, SHIPPING_COST); // DEFAULT 0
        setCellValue(row, 12, Image); // IMAGE URL
        setCellValue(row, 13, LIST_PRICE); // LIST PRICE
        setCellValue(row, 14, AVAILABILITY); // TRUE
        setCellValue(row, 15, BRAND);
        setCellValue(row, 16, MANUFACTURER);
        setCellValue(row, 17, MFR_PART_NUMBER);
        setCellValue(row, 18, MODEL_NUMBER);
        setCellValue(row, 19, COLOUR_NAME);
        setCellValue(row, 20, COLOUR_MAP);
        setCellValue(row, 21, ITEM_PACKAGE_QTY);
        setCellValue(row, 22, WARRANTY);
        setCellValue(row, 23, AGE);
        setCellValue(row, 24, GENDER);
        setCellValue(row, 25, FLAVOUR);
        setCellValue(row, 26, SCENT);
        setCellValue(row, 27, THEME_HPC);
        setCellValue(row, 28, BATTERY_TYPE);
        setCellValue(row, 29, BATTERIES_INCLUDED);
        setCellValue(row, 30, BATTERIES_REQUIRED);
        setCellValue(row, 31, POWER_SOURCE);
        setCellValue(row, 32, POWER_ADAPTER_INCLUDED);
        setCellValue(row, 33, ASSEMBLY_REQUIRED);
        setCellValue(row, 34, SHIPPING_WEIGHT);
        setCellValue(row, 35, WEIGHT);
        setCellValue(row, 36, LENGTH);
        setCellValue(row, 37, HEIGHT);
        setCellValue(row, 38, WIDTH);
        setCellValue(row, 39, KEYWORD_S1);
        setCellValue(row, 40, KEYWORD_S2);
        setCellValue(row, 41, KEYWORD_S3);
        setCellValue(row, 42, KEYWORD_S4);
        setCellValue(row, 43, KEYWORD_S5);
        setCellValue(row, 44, BULLET_POINT1);
        setCellValue(row, 45, BULLET_POINT2);
        setCellValue(row, 46, BULLET_POINT3);
        setCellValue(row, 47, BULLET_POINT4);
        setCellValue(row, 48, BULLET_POINT5);
        setCellValue(row, 49, OTHER_IMAGE_URL1);
        setCellValue(row, 50, OTHER_IMAGE_URL2);
        setCellValue(row, 51, OTHER_IMAGE_URL3);
        setCellValue(row, 52, OTHER_IMAGE_URL4);
        setCellValue(row, 53, OTHER_IMAGE_URL5);
        setCellValue(row, 54, OFFER_NOTE);
        setCellValue(row, 55, IS_GIFT_WRAP_AVAILABLE);
        setCellValue(row, 56, REGISTERED_PARAMETER);

        int initialRowNo = 2;
        for (Product product : products) {
            for (ProductVariant productVariant : product.getProductVariants()) {
                if (productVariant.isDeleted()) continue;
                AmazonFeed amazonFeed = amazonFeedDao.findByPV(productVariant);
                row = sheet1.createRow(initialRowNo);
                for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                    row.createCell(columnNo);
                }
                if (!productVariant.isDeleted()) {
                    setCellValue(row, 0, productVariant.getId());
                    String amazonFeedDbProductTitle = amazonFeed != null ? amazonFeed.getTitle() : "";
                    if (org.apache.commons.lang.StringUtils.isNotBlank(amazonFeedDbProductTitle) && amazonFeedDbProductTitle != null) {
                        setCellValue(row, 1, amazonFeedDbProductTitle);
                    } else {
                        setCellValue(row, 1, product.getName());
                    }
                    setCellValue(row, 2, getOptions(productVariant));
                    setCellValue(row, 3, getUrl(product));
                    setCellValue(row, 4, productVariant.getHkPrice(null));
                    setCellValue(row, 5, "2");
                    String amazonFeedDbBrowseNode = amazonFeed != null ? amazonFeed.getAmazonBrowseNode() : "";
                    if (org.apache.commons.lang.StringUtils.isNotBlank(amazonFeedDbBrowseNode) && amazonFeedDbBrowseNode != null) {
                        setCellValue(row, 6, amazonFeedDbBrowseNode);
                    } else {
                        setCellValue(row, 6, "");
                    }
                    if (productVariant.getUpc() != null && !productVariant.getUpc().equals(productVariant.getId())) {
                        setCellValue(row, 7, productVariant.getUpc());
                    } else {
                        setCellValue(row, 7, "");
                    }
                    setCellValue(row, 8, getUpcType(productVariant));
                    setCellValue(row, 9, "HealthAndPersonalCare");
                    String amazonFeedDbDescription = amazonFeed != null ? amazonFeed.getDescription() : "";
                    if (org.apache.commons.lang.StringUtils.isNotBlank(amazonFeedDbDescription) && amazonFeedDbDescription != null) {
                        if (amazonFeedDbDescription.length() <= 2000) {
                            setCellValue(row, 10, amazonFeedDbDescription);
                        } else {
                            setCellValue(row, 10, "");
                        }
                    } else {
                        setCellValue(row, 10, product.getDescription() != null ? Jsoup.parse(product.getDescription()).text() : product.getOverview() != null ? Jsoup.parse(
                                product.getOverview()).text() : null);
                    }
                    setCellValue(row, 11, productVariant.getHkPrice(null) >= 250 ? "0" : "30");
                    setCellValue(row, 12, getImageUrl(product));
                    setCellValue(row, 13, productVariant.getMarkedPrice());
                    setCellValue(row, 14, "TRUE");
                    setCellValue(row, 15, product.getBrand());
                    if (product.getManufacturer() != null) {
                        setCellValue(row, 16, product.getManufacturer().getName());
                    } else {
                        setCellValue(row, 16, product.getBrand());
                    }

                    setCellValue(row, 17, "");
                    setCellValue(row, 18, "");
                    setCellValue(row, 19, "");
                    setCellValue(row, 20, "");

                    String amazonFeedDbItemPackageQuantity = amazonFeed != null && amazonFeed.getItemPackageQuantity() != null ? amazonFeed.getItemPackageQuantity().toString()
                            : "";
                    if (org.apache.commons.lang.StringUtils.isNotBlank(amazonFeedDbItemPackageQuantity) && amazonFeedDbItemPackageQuantity != null) {
                        setCellValue(row, 21, amazonFeedDbItemPackageQuantity);
                    } else {
                        setCellValue(row, 21, "");
                    }

                    String amazonFeedDbWarranty = amazonFeed != null ? amazonFeed.getWarranty() : "";
                    if (org.apache.commons.lang.StringUtils.isNotBlank(amazonFeedDbWarranty) && amazonFeedDbWarranty != null) {
                        setCellValue(row, 22, amazonFeedDbWarranty);
                    } else {
                        setCellValue(row, 22, "");
                    }

                    setCellValue(row, 23, "");

                    String amazonFeedDbGender = amazonFeed != null ? amazonFeed.getGender() : "";
                    if (org.apache.commons.lang.StringUtils.isNotBlank(amazonFeedDbGender) && amazonFeedDbGender != null) {
                        setCellValue(row, 24, amazonFeedDbGender);
                    } else {
                        setCellValue(row, 24, "");
                    }

                    setCellValue(row, 25, "");
                    setCellValue(row, 26, "");
                    setCellValue(row, 27, "");
                    setCellValue(row, 28, "");

                    String amazonFeedDbBatteriesIncluded = amazonFeed != null ? amazonFeed.getBatteriesIncluded() : "";
                    if (org.apache.commons.lang.StringUtils.isNotBlank(amazonFeedDbBatteriesIncluded) && amazonFeedDbBatteriesIncluded != null) {
                        setCellValue(row, 29, amazonFeedDbBatteriesIncluded);
                    } else {
                        setCellValue(row, 29, "");
                    }

                    setCellValue(row, 30, "");
                    setCellValue(row, 31, "");
                    setCellValue(row, 32, "");
                    setCellValue(row, 33, "");
                    setCellValue(row, 34, "");
                    setCellValue(row, 35, "");
                    setCellValue(row, 36, "");
                    setCellValue(row, 37, "");
                    setCellValue(row, 38, "");
                    setCellValue(row, 39, "");
                    setCellValue(row, 40, "");
                    setCellValue(row, 41, "");
                    setCellValue(row, 42, "");
                    setCellValue(row, 43, "");
                    setCellValue(row, 44, "");
                    setCellValue(row, 45, "");
                    setCellValue(row, 46, "");
                    setCellValue(row, 47, "");
                    setCellValue(row, 48, "");
                    setCellValue(row, 49, "");
                    setCellValue(row, 50, "");
                    setCellValue(row, 51, "");
                    setCellValue(row, 52, "");
                    setCellValue(row, 53, "");
                    setCellValue(row, 54, "");
                    setCellValue(row, 55, "");
                    setCellValue(row, 56, "");

                    initialRowNo++;
                }
            }
        }

        wb.write(out);
        out.close();
        return file;
    }

    public File generateTempExcel(String xslFilePath) throws Exception {

        /*
         * File file = new File(xslFilePath); file.getParentFile().mkdirs(); FileOutputStream out = new
         * FileOutputStream(file); Workbook wb = new HSSFWorkbook(); CellStyle style = wb.createCellStyle(); Font font =
         * wb.createFont(); font.setFontHeightInPoints((short) 12); font.setColor(Font.COLOR_NORMAL);
         * font.setBoldweight(Font.BOLDWEIGHT_BOLD); style.setFont(font); Sheet sheet1 = wb.createSheet("Inventory");
         * Row row = sheet1.createRow(0); row.setHeightInPoints((short) 25); int totalColumnNo = 7; List<DoomDay>
         * doomDayList = doomDayDao.listAll(); Cell cell; for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
         * cell = row.createCell(columnNo); cell.setCellStyle(style); } setCellValue(row, 0, "PRIMARY_CATEGORY");
         * setCellValue(row, 1, "VARIANT_ID"); setCellValue(row, 2, "NAME"); setCellValue(row, 3, "INV_SNAPSHOT");
         * setCellValue(row, 4, "CHECKED_IN_INV"); setCellValue(row, 4, "DIFFERENCE"); int initialRowNo = 1; for
         * (DoomDay doomDay : doomDayList) { List<ProductVariant> productVariants =
         * productVariantDao.findVariantFromBarcode(doomDay.getUpcVariantId()); if (productVariants == null ||
         * productVariants.isEmpty() || productVariants.size() > 1) { System.out.println("bug " +
         * doomDay.getUpcVariantId()); // logger.info("bug " + upc); } else { row = sheet1.createRow(initialRowNo); for
         * (int columnNo = 0; columnNo < totalColumnNo; columnNo++) { row.createCell(columnNo); } setCellValue(row, 0,
         * productVariants.get(0).getProduct().getPrimaryCategory().getDisplayName()); setCellValue(row, 1,
         * productVariants.get(0).getId()); setCellValue(row, 2, productVariants.get(0).getProduct().getName());
         * setCellValue(row, 3, productVariants.get(0).getNetInventory()); setCellValue(row, 4,
         * doomDayDao.getCheckedInQty(doomDay.getUpcVariantId())); initialRowNo++; } } wb.write(out); out.close();
         * return file;
         */

        // TODO: #warehouse fix this
        return null;
    }

    private void setCellValue(Row row, int column, Double cellValue) {
        if (cellValue != null) {
            Cell cell = row.getCell(column);
            cell.setCellValue(cellValue);
        }
    }

    @SuppressWarnings("unused")
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

    private String getUrl(Product product) {
        return "http://www.healthkart.com/product/" + product.getSlug() + "/" + product.getId();
    }

    private String getImageUrl(Product product) {
        if (product.getMainImageId() != null && HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, product.getMainImageId()) != null) {
            return HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, product.getMainImageId());
        } else {
            return "http://img.healthkart.com/images/ProductImages/ProductImagesOriginal/" + product.getId();
        }
    }

    private String getUpcType(ProductVariant productVariant) {
        if (productVariant.getUpc() != null) {
            if (productVariant.getUpc().length() == 12) {
                return "UPC";
            } else if (productVariant.getUpc().length() == 13) {
                return "EAN";
            } else if (productVariant.getUpc().length() == 13) {
                return "GTIN";
            }
        }
        return "";
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

}
