package com.hk.admin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.dao.inventory.GrnLineItemDao;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.dao.inventory.RetailLineItemDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.XslConstants;
import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.catalog.product.EnumProductVariantServiceType;
import com.hk.constants.core.EnumRole;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.report.ReportConstants;
import com.hk.domain.RetailLineItem;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.City;
import com.hk.domain.core.Pincode;
import com.hk.domain.core.State;
import com.hk.domain.core.Tax;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.exception.HealthKartCatalogUploadException;
import com.hk.impl.dao.ReconciliationStatusDaoImpl;
import com.hk.impl.dao.affiliate.AffiliateCategoryDaoImpl;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.dao.inventory.LowInventoryDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.CityService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.core.StateService;
import com.hk.pact.service.core.TaxService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.service.ServiceLocatorFactory;

@Component
@SuppressWarnings("unchecked")
public class XslParser {

  @Autowired
  private BaseDao baseDao;

  private static Logger logger = LoggerFactory.getLogger(XslParser.class);

  public static Pattern p = Pattern.compile("([0-9]*\\.?[0-9]*) ?%");

  // public static void main(String[] args) {
  // Matcher matcher = p.matcher("VAT - 3.125 %");
  // if (matcher.find()) {
  // System.out.println(matcher.group(1));
  // }
  // }

  private Set<Product> colProductList = null;

  @Autowired
  private PurchaseOrderDao purchaseOrderDao;
  @Autowired
  private LowInventoryDao lowInventoryDao;
  @Autowired
  private SupplierDao supplierDao;
  @Autowired
  private GrnLineItemDao grnLineItemDao;
  @Autowired
  private PoLineItemDao poLineItemDao;
  @Autowired
  private RetailLineItemDao retailLineItemDao;
  @Autowired
  private AffiliateCategoryDaoImpl affiliateCategoryDao;

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductVariantService productVariantService;
  @Autowired
  private CourierService courierService;
  @Autowired
  private PincodeService pincodeService;
  @Autowired
  private SkuService skuService;
  @Autowired
  private ShipmentService shipmentService;
  @Autowired
  private TaxService taxService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private WarehouseService warehouseService;
  @Autowired
  private ShippingOrderService shippingOrderService;
  @Autowired
  private PaymentService paymentService;
  @Autowired
  private AdminInventoryService adminInventoryService;
  @Autowired
  private InventoryService inventoryService;
  @Autowired
  CityService cityService;
  @Autowired
  StateService stateService;
 

  public Set<Product> readProductList(File objInFile, User loggedOnUser) throws Exception {

    logger.info("parsing products from product catalog : " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    HSSFSheet productSheet = workbook.getSheet("Product");
    HSSFSheet manufacturerSheet = workbook.getSheet("Manufacturer");
    Iterator<Row> objRowIt = productSheet.rowIterator();
    //Iterator objCellIterator = null;

    // Declaring data elements
    Product product = null;
    ProductVariant productVariant = null;
    List<ProductVariant> productVariants = null;
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    colProductList = new HashSet<Product>();

    int rowCount = 1;
    try {
      headerMap = getRowMap(objRowIt);

      // Iterating on the available rows
      boolean productDeleted = false;
      boolean outOfStock = false;
      boolean isJitBoolean = false;
      String refProdId = "";
      Boolean refIsService = false;
      Product refProduct = null;
      while (objRowIt.hasNext()) {
        rowMap = getRowMap(objRowIt);

        // checking if variantId is present. if no variant id is present then tis is a blank row, continue
        String variantId = getCellValue(XslConstants.VARIANT_ID, rowMap, headerMap);
        if (StringUtils.isBlank(variantId))
          continue;

        if (StringUtils.isBlank(getCellValue(XslConstants.PRODUCT_ID, rowMap, headerMap))) {
          // this is not a new product. add a new product variant

        } else {
          // this is a new product
          productVariants = new ArrayList<ProductVariant>();

          String productId = getCellValue(XslConstants.PRODUCT_ID, rowMap, headerMap);
          refProdId = productId;
          refProduct = getProductService().getProductById(refProdId);
          String overview = getCellValue(XslConstants.OVERVIEW, rowMap, headerMap);
          String description = getCellValue(XslConstants.DESCRIPTION, rowMap, headerMap);

          Product productInDB = getProductService().getProductById(productId);
          if (loggedOnUser != null && !loggedOnUser.getRoles().contains(getRoleService().getRoleByName(EnumRole.GOD.getRoleName()))) {
            if (productInDB != null) {
              throw new HealthKartCatalogUploadException("The same product has already been created, Pls make updates through UI only, Exception @ Row:", rowCount);
            }
            if (colProductList.contains(productInDB)) {
              throw new HealthKartCatalogUploadException("Already the same product id exists in excel, Exception @ Row:", rowCount);
            }
          }
          String videoEmbedCode = null;
          if (productInDB != null) {
            overview = productInDB.getOverview();
            description = productInDB.getDescription();
            videoEmbedCode = productInDB.getVideoEmbedCode();
          }
          product = new Product();
          product.setCategories(getCategroyListFromCategoryString(getCellValue(XslConstants.CATEGORY, rowMap, headerMap)));
          List<Category> listFromPrimaryCategoryString = getCategroyListFromCategoryString(getCellValue(XslConstants.PRIMARY_CATEGORY, rowMap, headerMap));
          if (listFromPrimaryCategoryString != null && !listFromPrimaryCategoryString.isEmpty()) {
            product.setPrimaryCategory(listFromPrimaryCategoryString.get(0));
          }
          String secondaryCategory = getCellValue(XslConstants.SECONDARY_CATEGORY, rowMap, headerMap);
          if (StringUtils.isNotBlank(secondaryCategory)) {
            Category secondaryCat = getCategoryService().getCategoryByName(Category.getNameFromDisplayName(secondaryCategory));
            if (secondaryCat != null && secondaryCat.getName() != null) {
              product.setSecondaryCategory(secondaryCat);
            }
            // Set secondary category same as primary category if secondary category is null
            else {
              if (listFromPrimaryCategoryString != null && !listFromPrimaryCategoryString.isEmpty()) {
                product.setSecondaryCategory(listFromPrimaryCategoryString.get(0));
              }
            }
          }
          List<Category> listFromSecondaryCategoryString = getCategroyListFromCategoryString(getCellValue(XslConstants.SECONDARY_CATEGORY, rowMap, headerMap));
          if (listFromSecondaryCategoryString != null && !listFromSecondaryCategoryString.isEmpty()) {
            product.setSecondaryCategory(listFromSecondaryCategoryString.get(0));
          }
          product.setId(getCellValue(XslConstants.PRODUCT_ID, rowMap, headerMap));
          product.setName(getCellValue(XslConstants.PRODUCT_NAME, rowMap, headerMap));
          Double sortingOrder = getDouble(getCellValue(XslConstants.SORTING, rowMap, headerMap));
          if (sortingOrder == null) {
            sortingOrder = 100000.0;
          }
          product.setOrderRanking(sortingOrder);
          product.setBrand(getCellValue(XslConstants.BRAND, rowMap, headerMap));
          product.setManufacturer(getManufacturerDetails(getCellValue(XslConstants.MANUFACTURER, rowMap, headerMap), manufacturerSheet));
          product.setOverview(overview);
          product.setDescription(description);
          product.setVideoEmbedCode(videoEmbedCode);
          product.setThumbUrl(getCellValue(XslConstants.Image, rowMap, headerMap));
          String isProductHaveColorOptions = getCellValue(XslConstants.COLOR_PRODUCT, rowMap, headerMap);
          boolean isProductHaveColorOptionsBoolean = StringUtils.isNotBlank(isProductHaveColorOptions) && isProductHaveColorOptions.trim().toLowerCase().equals("y") ? true
              : false;
          product.setProductHaveColorOptions(isProductHaveColorOptionsBoolean);
          String isService = getCellValue(XslConstants.IS_SERVICE, rowMap, headerMap);
          boolean isServiceBoolean = StringUtils.isNotBlank(isService) && isService.trim().toLowerCase().equals("y") ? true : false;
          refIsService = isServiceBoolean;
          product.setService(isServiceBoolean);
          String isGoogleAdDisallowed = getCellValue(XslConstants.IS_GOOGLE_AD_DISALLOWED, rowMap, headerMap);
          boolean isGoogleAdDisallowedBoolean = StringUtils.isNotBlank(isGoogleAdDisallowed) && isGoogleAdDisallowed.trim().toLowerCase().equals("y") ? true : false;
          product.setGoogleAdDisallowed(isGoogleAdDisallowedBoolean);
          String isJit = getCellValue(XslConstants.IS_JIT, rowMap, headerMap);
          isJitBoolean =  StringUtils.isNotBlank(isJit) && isJit.trim().toLowerCase().equals("y") ? true : false;
          product.setJit(isJitBoolean);  
          product.setProductVariants(productVariants);
          product.setRelatedProducts(getRelatedProductsFromExcel(getCellValue(XslConstants.RELATED_PRODUCTS, rowMap, headerMap)));
          productDeleted = true;
          product.setDeleted(productDeleted);
            product.setSupplier(getSupplierDetails(getCellValue(XslConstants.SUPPLIER_TIN, rowMap, headerMap),
              getCellValue(XslConstants.SUPPLIER_STATE, rowMap, headerMap), rowCount));

          product.setMaxDays(getLong(getCellValue(XslConstants.MAX_DAYS_TO_PROCESS, rowMap, headerMap)));
          product.setMinDays(getLong(getCellValue(XslConstants.MIN_DAYS_TO_PROCESS, rowMap, headerMap)));
          colProductList.add(product);
        }

        // product options
        List<ProductOption> productOptions = getProductOptions(getCellValue(XslConstants.OPTIONS, rowMap, headerMap));
        // product extra options - For Eye
        List<ProductExtraOption> productExtraOptions = getProductExtraOptions(getCellValue(XslConstants.EXTRA_OPTIONS, rowMap, headerMap));
        ProductVariant productVariantDb = getProductVariantService().getVariantById(variantId);
        if (loggedOnUser != null && !loggedOnUser.getRoles().contains(getRoleService().getRoleByName(EnumRole.GOD.getRoleName()))) {
          if (productVariantDb != null) {
            throw new HealthKartCatalogUploadException("Variant ID already exists in DB, Exception @ Row:", rowCount);
          }
        }
        productVariant = new ProductVariant();
        if (!variantId.startsWith(refProdId + "-")) {
          throw new HealthKartCatalogUploadException("Exception @ Row: Variant Id is not correct", rowCount);
        }
        productVariant.setId(variantId);
        productVariant.setColorHex(getCellValue(XslConstants.COLOR_HEX, rowMap, headerMap));
        productVariant.setLength(getDouble(getCellValue(XslConstants.LENGTH, rowMap, headerMap)) == null ? 0.0 : Double.parseDouble(getCellValue(XslConstants.LENGTH,
            rowMap, headerMap)));
        productVariant.setBreadth(getDouble(getCellValue(XslConstants.BREADTH, rowMap, headerMap)) == null ? 0.0 : Double.parseDouble(getCellValue(XslConstants.BREADTH,
            rowMap, headerMap)));
        productVariant.setHeight(getDouble(getCellValue(XslConstants.HEIGHT, rowMap, headerMap)) == null ? 0.0 : Double.parseDouble(getCellValue(XslConstants.HEIGHT,
            rowMap, headerMap)));
        productVariant.setWeight(getDouble(getCellValue(XslConstants.WEIGHT, rowMap, headerMap)) == null ? 0.0 : Double.parseDouble(getCellValue(XslConstants.WEIGHT,
            rowMap, headerMap)));
        if (refIsService != null && refIsService) {
          String serviceTypeString = getCellValue(XslConstants.SERVICE_TYPE, rowMap, headerMap);
          if (serviceTypeString != null) {
            if (serviceTypeString.equalsIgnoreCase(EnumProductVariantServiceType.Merchant2Home.getName())) {
              productVariant.setServiceType(getProductVariantService().getVariantServiceType(EnumProductVariantServiceType.Merchant2Home));
            } else if (serviceTypeString.equalsIgnoreCase(EnumProductVariantServiceType.Home2Merchant.getName())) {
              productVariant.setServiceType(getProductVariantService().getVariantServiceType(EnumProductVariantServiceType.Home2Merchant));
            } else if (serviceTypeString.equalsIgnoreCase(EnumProductVariantServiceType.BothWays.getName())) {
              productVariant.setServiceType(getProductVariantService().getVariantServiceType(EnumProductVariantServiceType.BothWays));
            }
          }
          String paymentTypeString = getCellValue(XslConstants.PAYMENT_TYPE, rowMap, headerMap);
          if (paymentTypeString != null) {
            if (paymentTypeString.equalsIgnoreCase(EnumProductVariantPaymentType.Prepaid.getName())) {
              productVariant.setPaymentType(getPaymentService().findVariantPaymentType(EnumProductVariantPaymentType.Prepaid));
            } else if (paymentTypeString.equalsIgnoreCase(EnumProductVariantPaymentType.Postpaid.getName())) {
              productVariant.setPaymentType(getPaymentService().findVariantPaymentType(EnumProductVariantPaymentType.Postpaid));
            }
          }
        }
        productVariant.setUpc(getCellValue(XslConstants.UPC, rowMap, headerMap) == null ? "" : getCellValue(XslConstants.UPC, rowMap, headerMap));
        productVariant.setOtherRemark(getCellValue(XslConstants.OTHER_REMARK, rowMap, headerMap) == null ? "" : getCellValue(XslConstants.OTHER_REMARK, rowMap, headerMap));
        productVariant.setSupplierCode(getCellValue(XslConstants.SUPPLIER_CODE, rowMap, headerMap) == null ? "" : getCellValue(XslConstants.SUPPLIER_CODE, rowMap, headerMap));
        productVariant.setColorHex(getCellValue(XslConstants.COLOR_HEX, rowMap, headerMap));
        productVariant.setVariantName(getCellValue(XslConstants.VARIANT_NAME, rowMap, headerMap));
        Double mrp = Double.parseDouble(getCellValue(XslConstants.MRP, rowMap, headerMap));
        if (mrp == 0.0) {
          logger.error("Exception @ Row: MRP is zero " + rowCount + " ");
          throw new Exception("MRP is zero Exception @ Row:" + rowCount + " ");
        }
        productVariant.setMarkedPrice(mrp);
        Double postpaidAmount = getDouble(getCellValue(XslConstants.POSTPAID_AMOUNT, rowMap, headerMap)) == null ? 0.0 : Double.parseDouble(getCellValue(
            XslConstants.POSTPAID_AMOUNT, rowMap, headerMap));
        productVariant.setPostpaidAmount(postpaidAmount);
        Double hkprice = Double.parseDouble(getCellValue(XslConstants.HK_PRICE, rowMap, headerMap));
        productVariant.setHkPrice(hkprice);
        Double discountPercent = mrp - (hkprice + postpaidAmount);
        productVariant.setDiscountPercent(discountPercent / mrp);
        String costString = getCellValue(XslConstants.COST, rowMap, headerMap);
        if (!StringUtils.isNotBlank(costString)) {
          logger.error("Exception @ Row: Cost Price is null or blank " + rowCount + " ");
          throw new HealthKartCatalogUploadException("<br>Cost price cannot be blank:  " + rowCount, rowCount);
        }
        if (StringUtils.isNotBlank(costString) && !StringUtils.equals(costString, "0")) {
          productVariant.setCostPrice(Double.parseDouble(costString));
        }
        if (productVariant.getCostPrice() != null && productVariant.getCostPrice() > productVariant.getHkPrice(null)) {
          throw new Exception("HK Price of variant " + productVariant.getId() + " is less than Cost Price. Please fix it.");
        }
        productVariant.setShippingBasePrice(0D);
        productVariant.setShippingBaseQty(1L);
        productVariant.setShippingAddPrice(0D);
        productVariant.setShippingAddQty(1L);
        // not applicable now as part of catalog.
        // productVariant.setTax(getTaxDetails(getCellValue(TAX, rowMap, headerMap)));

        productVariant.setProductOptions(productOptions);
        productVariant.setProductExtraOptions(productExtraOptions);
        String availability = getCellValue(XslConstants.AVAILABILITY, rowMap, headerMap);
         if (isJitBoolean) {
              outOfStock = false;
          } else {
              outOfStock = StringUtils.isNotBlank(availability) && availability.trim().toLowerCase().equals("y") ? false : true;
          }
          productVariant.setOutOfStock(outOfStock);
        String deleted = getCellValue(XslConstants.DELETED, rowMap, headerMap);
        boolean deletedBoolean = StringUtils.isNotBlank(deleted) && deleted.trim().toLowerCase().equals("y") ? true : false;
        productVariant.setDeleted(deletedBoolean);
        if (!deletedBoolean) {
          productDeleted = false;
          product.setDeleted(productDeleted);
          colProductList.add(product);
        }
        Double variantSortingOrder = getDouble(getCellValue(XslConstants.VARIANT_SORTING, rowMap, headerMap));
        if (variantSortingOrder == null) {
          variantSortingOrder = 1000.0;
        }
        productVariant.setOrderRanking(variantSortingOrder);
        productVariant.setAffiliateCategory(getAffiliateCategoryDao().getAffiliateCategoryByName(
            (getCellValue(XslConstants.AFFILIATE_CATEGORY, rowMap, headerMap)) != null ? (getCellValue(XslConstants.AFFILIATE_CATEGORY, rowMap, headerMap)) : ""));
        if (StringUtils.isNotBlank(getCellValue(XslConstants.MAIN_IMAGE_ID, rowMap, headerMap))) {
          if (getBaseDao().get(ProductImage.class, getLong(getCellValue(XslConstants.MAIN_IMAGE_ID, rowMap, headerMap))) != null) {
            productVariant.setMainImageId(getLong(getCellValue(XslConstants.MAIN_IMAGE_ID, rowMap, headerMap)));
          }
        }
        // productVariant.setQty(getLong(getCellValue(INVENTORY, rowMap, headerMap)));
        // TODO: #warehouse fix this
        // productVariant.setCutOffInventory(getLong(getCellValue(CUTOFF_INVENTORY, rowMap, headerMap)));
        productVariants.add(productVariant);

        logger.debug("read row " + rowCount);
        rowCount++;
      }
    } catch (Exception e) {

      logger.error("Exception @ Row:" + rowCount + " " + e.getMessage());
      throw new Exception("Exception @ Row:" + rowCount + " " + e.getMessage(), e);
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }
    return colProductList;
  }

  public Set<CourierServiceInfo> readCourierServiceInfoList(File objInFile) throws Exception {

    logger.debug("parsing courier service info : " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    HSSFSheet courierServiceInfoSheet = workbook.getSheet("CourierServiceInfo");
    Iterator<Row> objRowIt = courierServiceInfoSheet.rowIterator();
    // Iterator objCellIterator = null;

    // Declaring data elements
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    Set<CourierServiceInfo> courierServiceInfoList = new HashSet<CourierServiceInfo>();

    int rowCount = 1;
    try {
      headerMap = getRowMap(objRowIt);

      while (objRowIt.hasNext()) {
        rowMap = getRowMap(objRowIt);
        CourierServiceInfo courierServiceInfo = new CourierServiceInfo();
        String id = getCellValue(XslConstants.ID, rowMap, headerMap);
        if (StringUtils.isNotBlank(id)) {
          courierServiceInfo.setId(getLong(id));
        }
        String lPincode = null;
        try {
          lPincode = getCellValue(XslConstants.PINCODE, rowMap, headerMap).replace(".0", "");
          if (lPincode != null) {
            Pincode pincode = getPincodeService().getByPincode(lPincode);
            if (pincode != null) {
              courierServiceInfo.setPincode(pincode);
              String codAvailable = getCellValue(XslConstants.COD_AVAILABLE, rowMap, headerMap);
              boolean isCODAvailable = StringUtils.isNotBlank(codAvailable) && codAvailable.trim().toLowerCase().equals("y") ? true : false;
              courierServiceInfo.setCodAvailable(isCODAvailable);
              Courier courier = getCourierService().getCourierById(getLong(getCellValue(XslConstants.COURIER_ID, rowMap, headerMap)));
              courierServiceInfo.setCourier(courier);
              String deleted = getCellValue(XslConstants.IS_DELETED, rowMap, headerMap);
              boolean isDeleted = StringUtils.isNotBlank(deleted) && deleted.trim().toLowerCase().equals("y") ? true : false;
              courierServiceInfo.setDeleted(isDeleted);
              String preferred = getCellValue(XslConstants.IS_PREFERRED, rowMap, headerMap);
              boolean isPreferred = StringUtils.isNotBlank(preferred) && preferred.trim().toLowerCase().equals("y") ? true : false;
              courierServiceInfo.setPreferred(isPreferred);
              String preferredCod = getCellValue(XslConstants.IS_PREFERRED_COD, rowMap, headerMap);
              boolean isPreferredCod = StringUtils.isNotBlank(preferredCod) && preferredCod.trim().toLowerCase().equals("y") ? true : false;
              courierServiceInfo.setPreferredCod(isPreferredCod);
              courierServiceInfo.setRoutingCode(getCellValue(XslConstants.ROUTING_CODE, rowMap, headerMap));
                
              String groundShippingAvailable = getCellValue(XslConstants.GROUND_SHIPPING_AVAILABLE, rowMap, headerMap);
              boolean isGroundShippingAvailable =  StringUtils.isNotBlank(groundShippingAvailable) && groundShippingAvailable.trim().toLowerCase().equals("y") ? true : false;
              courierServiceInfo.setGroundShippingAvailable(isGroundShippingAvailable);               
              String codAvailableOnGroundShipping = getCellValue(XslConstants.COD_ON_GROUND_SHIPPING, rowMap, headerMap);
              boolean iscodAvailableOnGroundShipping =  StringUtils.isNotBlank(codAvailableOnGroundShipping) && codAvailableOnGroundShipping.trim().toLowerCase().equals("y") ? true : false;
              courierServiceInfo.setGroundShippingAvailable(iscodAvailableOnGroundShipping);
              courierServiceInfoList.add(courierServiceInfo);
            }
          }
        } catch (Exception e) {
          logger.debug("issue with pin = " + lPincode);
        }

        logger.debug("read row " + rowCount);
        rowCount++;
      }

    } catch (Exception e) {
      logger.error("Exception @ Row:" + rowCount + 1 + e.getMessage());
      throw new Exception("Exception @ Row:" + rowCount + 1, e);
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }
    return courierServiceInfoList;

  }

  @SuppressWarnings("unchecked")
public Set<Pincode> readPincodeList(File objInFile) throws Exception {

    logger.debug("parsing pincode info : " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    HSSFSheet pincodeSheet = workbook.getSheet("PincodeInfo");
    Iterator<Row> objRowIt = pincodeSheet.rowIterator();
    Iterator objCellIterator = null;

    // Declaring data elements
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    Set<Pincode> pincodeList = new HashSet<Pincode>();

    int rowCount = 2;
    headerMap = getRowMap(objRowIt);

    try {
      while (objRowIt.hasNext()) {
        rowMap = getRowMap(objRowIt);
        String pincodeValue = getCellValue(XslConstants.PINCODE, rowMap, headerMap).replace(".0", "");
        if (StringUtils.isEmpty(pincodeValue)) {
          logger.error("Pincode cannot be null @ Row:" + rowCount);
          throw new NullPointerException("Pincode cannot be null @row" + rowCount);
        }
        Pincode pincode = getPincodeService().getByPincode(pincodeValue);
        if (pincode == null) {
          pincode = new Pincode();
        }
        pincode.setPincode(pincodeValue);
        City city = cityService.getCityByName(getCellValue(XslConstants.CITY, rowMap, headerMap));
        if(city == null){
         logger.error("Exception @ Row:" + rowCount);
            throw new Exception("City is incorrect Please check spelling @ Row:" + rowCount);
        }
        pincode.setCity(city);
        State state=stateService.getStateByName(getCellValue(XslConstants.STATE, rowMap, headerMap));
        if(state == null){
         logger.error("Exception @ Row:" + rowCount);
            throw new Exception("City is incorrect Please check spelling @ Row:" + rowCount);
        }
        pincode.setState(state);
        pincode.setLocality(getCellValue(XslConstants.LOCALITY, rowMap, headerMap));
        pincode.setRegion(getCellValue(XslConstants.REGION, rowMap, headerMap));
        String courierId = getCellValue(XslConstants.DEFAULT_COURIER_ID, rowMap, headerMap);
        if (StringUtils.isNotEmpty(courierId)) {
          Courier courier = getCourierService().getCourierById(getLong(getCellValue(XslConstants.DEFAULT_COURIER_ID, rowMap, headerMap)));
          if (courier == null) {
            logger.error("Exception @ Row:" + rowCount);
            throw new Exception("Courier Id is incorrect @ Row:" + rowCount);
          } else
            pincode.setDefaultCourier(courier);

        }
        pincodeList.add(pincode);

        logger.debug("read row " + rowCount);
        rowCount++;
      }
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }
    return pincodeList;
  }

  public Set<PincodeDefaultCourier> readDefaultPincodeList(File objInFile) throws Exception {

    logger.debug("parsing default pincode info : " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    HSSFSheet defaultPincodeSheet = workbook.getSheet(XslConstants.DEFAULT_COURIER_SHEET);
    Iterator<Row> objRowIt = defaultPincodeSheet.rowIterator();
    Iterator objCellIterator = null;

    // Declaring data elements
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    Set<PincodeDefaultCourier> defaultPincodeList = new HashSet<PincodeDefaultCourier>();

    int rowCount = 2;
    headerMap = getRowMap(objRowIt);

    try {
      while (objRowIt.hasNext()) {
        rowMap = getRowMap(objRowIt);
        String pincodeValue = getCellValue(XslConstants.PINCODE, rowMap, headerMap).replace(".0", "");
        String wareHouseValue = getCellValue(XslConstants.WAREHOUSE, rowMap, headerMap);
        if (StringUtils.isEmpty(pincodeValue)) {
          logger.error("Pincode cannot be null @ Row:" + rowCount);
          throw new NullPointerException("Pincode cannot be null @row" + rowCount);
        }
        Pincode pincode = getPincodeService().getByPincode(pincodeValue);
        if (pincode == null) {
          logger.error("Pincode Does not exists @ Row:" + rowCount);
          throw new NullPointerException("Pincode does not exists @row" + rowCount);
        }

        if (StringUtils.isEmpty(wareHouseValue)) {
          logger.error("Warehouse cannot be null @ Row:" + rowCount);
          throw new NullPointerException("Pincode cannot be null @row" + rowCount);
        }
        Warehouse warehouse = getWarehouseService().getWarehouseById(getLong(wareHouseValue));
        if (warehouse == null) {
          logger.error("Warehouse Does not exists @ Row:" + rowCount);
          throw new NullPointerException("Warehouse does not exists @row" + rowCount);
        }

        String codCourierId = getCellValue(XslConstants.COD_COURIER_ID, rowMap, headerMap);
        String techCourierId = getCellValue(XslConstants.TECH_PROCESS_COURIER_ID, rowMap, headerMap);
        if (StringUtils.isNotEmpty(codCourierId) || StringUtils.isNotEmpty(techCourierId)) {
          Courier courier_cod = getCourierService().getCourierById(getLong(codCourierId));
          Courier courier_tech = getCourierService().getCourierById(getLong(techCourierId));
          if (courier_cod == null || courier_tech == null) {
            logger.error("Exception @ Row:" + rowCount);
            throw new Exception("Courier Id is incorrect @ Row:" + rowCount);
          }

          Double estimatedShippingCostCod = getDouble(getCellValue(XslConstants.ESTIMATED_SHIPPING_COST_COD, rowMap, headerMap));
          Double estimatedShippingCostNonCod = getDouble(getCellValue(XslConstants.ESTIMATED_SHIPPING_COST_NON_COD, rowMap, headerMap));

          PincodeDefaultCourier pincodeDefaultCourier = new PincodeDefaultCourier();
          pincodeDefaultCourier.setPincode(pincode);
          pincodeDefaultCourier.setCodCourier(courier_cod);
          pincodeDefaultCourier.setNonCodCourier(courier_tech);
          pincodeDefaultCourier.setWarehouse(warehouse);
          pincodeDefaultCourier.setEstimatedShippingCostCod(estimatedShippingCostCod);
          pincodeDefaultCourier.setEstimatedShippingCostNonCod(estimatedShippingCostNonCod);
          defaultPincodeList.add(pincodeDefaultCourier);

          logger.debug("read row " + rowCount);
          rowCount++;
        }
      }
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }
    return defaultPincodeList;
  }

  public Set<SkuGroup> readAndBulkCheckinInventory(GoodsReceivedNote goodsReceivedNote, File objInFile) throws Exception {

    logger.debug("parsing inventory group checkin : " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    // HSSFSheet courierServiceInfoSheet = workbook.getSheet("Product");
    HSSFSheet courierServiceInfoSheet = workbook.getSheetAt(0);
    Iterator<Row> objRowIt = courierServiceInfoSheet.rowIterator();
    Iterator objCellIterator = null;

    // Declaring data elements
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    Set<SkuGroup> skuGroupSet = new HashSet<SkuGroup>();
    int rowCount = 1;
    try {
      headerMap = getRowMap(objRowIt);
      while (objRowIt.hasNext()) {
        rowMap = getRowMap(objRowIt);
        ProductVariant productVariant = getProductVariantService().getVariantById(getCellValue(XslConstants.VARIANT_ID, rowMap, headerMap));
        if (productVariant != null) {
          // Long qty = getLong(getCellValue(QTY, rowMap, headerMap));
          Long checkinQty = getLong(getCellValue(XslConstants.CHECKIN_QTY, rowMap, headerMap));
          Long askedQty = 0L;
          // GrnLineItem grnLineItem = grnLineItemDao.find(getLong(getCellValue(GRN_LINE_ITEM_ID, rowMap,
          // headerMap)));
          GrnLineItem grnLineItem = grnLineItemDao.getGrnLineItem(goodsReceivedNote, productVariant);
          if (grnLineItem != null) {
            askedQty = grnLineItem.getQty();
            Long alreadyCheckedInQty = getAdminInventoryService().countOfCheckedInUnitsForGrnLineItem(grnLineItem);

            if (checkinQty > (askedQty - alreadyCheckedInQty)) {
              logger.error("Qty mentioned - " + checkinQty + " is exceeding required checked in qty. Plz check.");
              throw new Exception("Qty mentioned - " + checkinQty + " is exceeding required checked in qty. Plz check row@" + rowCount);
            }
            logger.debug("checkinQty of variant - " + productVariant.getId() + " is - " + checkinQty);
            if (checkinQty != null && checkinQty > 0) {
              String batch = getCellValue(XslConstants.BATCH_NUMBER, rowMap, headerMap);
              SkuGroup skuGroup = adminInventoryService.createSkuGroup(batch, getDate(getCellValue(XslConstants.MFG_DATE, rowMap, headerMap)), getDate(getCellValue(
                  XslConstants.EXP_DATE, rowMap, headerMap)), 0.0, 0.0, goodsReceivedNote, null, null, null);
              adminInventoryService.createSkuItemsAndCheckinInventory(skuGroup, checkinQty, null, grnLineItem, null, null, getInventoryService().getInventoryTxnType(
                  EnumInvTxnType.INV_CHECKIN), null);

              /*
              * SkuGroup skuGroup = getAdminInventoryService().createSkuGroup(batch,
              * getDate(getCellValue(XlsConstants.MFG_DATE, rowMap, headerMap)),
              * getDate(getCellValue(XlsConstants.EXP_DATE, rowMap, headerMap)), goodsReceivedNote, null,
              * null); getAdminInventoryService().createSkuItemsAndCheckinInventory(skuGroup, checkinQty,
              * null, grnLineItem, null,
              * getInventoryService().getInventoryTxnType(EnumInvTxnType.INV_CHECKIN), null);
              */
              getInventoryService().checkInventoryHealth(productVariant);
            }

            // Setting checked in qty to avoid multiple calls
            grnLineItem.setCheckedInQty(alreadyCheckedInQty + checkinQty);
            grnLineItemDao.save(grnLineItem);
          }/*
                         * else { logger.error("Plz mention grn line item id."); throw new Exception("Plz mention grn
                         * line item id.. Plz check row@" + rowCount); }
                         */
        }
        logger.debug("read row " + rowCount);
        rowCount++;
      }

    } catch (Exception e) {
      logger.error("Exception @ Row:" + rowCount + 1 + e.getMessage());
      throw new Exception("Exception @ Row:" + rowCount, e);
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }

    return skuGroupSet;

  }

  public Set<PoLineItem> readAndCreatePOLineItems(File objInFile, PurchaseOrder purchaseOrder) throws Exception {
    logger.debug("parsing po line items : " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    HSSFSheet courierServiceInfoSheet = workbook.getSheetAt(0);
    Iterator<Row> objRowIt = courierServiceInfoSheet.rowIterator();

    // Declaring data elements
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    Set<PoLineItem> poLineItems = new HashSet<PoLineItem>();
    int rowCount = 1;
    try {
      headerMap = getRowMap(objRowIt);
      while (objRowIt.hasNext()) {
        rowMap = getRowMap(objRowIt);
        ProductVariant productVariant = getProductVariantService().getVariantById(getCellValue(XslConstants.VARIANT_ID, rowMap, headerMap));
        if (productVariant != null) {

          Long qty = getLong(getCellValue(XslConstants.QTY, rowMap, headerMap));
          logger.debug("qty of variant - " + productVariant.getId() + " is - " + qty);
          if (qty != null && qty > 0) {
            Double cost = getDouble(getCellValue(XslConstants.COST, rowMap, headerMap));
            if (cost == null || cost == 0D) {
              cost = productVariant.getCostPrice();
            }
            Double mrp = getDouble(getCellValue(XslConstants.MRP, rowMap, headerMap));
            if (mrp == null || mrp == 0D) {
              mrp = productVariant.getMarkedPrice();
            }
            PoLineItem poLineItem = new PoLineItem();
            poLineItem.setPurchaseOrder(purchaseOrder);
            poLineItem.setQty(qty);
            Sku sku = skuService.getSKU(productVariant, purchaseOrder.getWarehouse());
            poLineItem.setSku(sku);
            poLineItem.setCostPrice(cost);
            poLineItem.setMrp(mrp);
            poLineItemDao.save(poLineItem);

            poLineItems.add(poLineItem);
          }

        }
        logger.debug("read row " + rowCount);
        rowCount++;
      }

    } catch (Exception e) {
      logger.error("Exception @ Row:" + rowCount + 1 + e.getMessage());
      throw new Exception("Exception @ Row:" + rowCount, e);
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }
    return poLineItems;
  }

  public HashMap<ReconciliationStatus, List<String>> readAndUpdateReconciliationStatus(File objInFile) throws Exception {
    HashMap<ReconciliationStatus, List<String>> reconciliationMap = new HashMap<ReconciliationStatus, List<String>>();
    List<String> reconciledOrders = new ArrayList<String>();
    List<String> reconcilationPendingOrders = new ArrayList<String>();
    ReconciliationStatusDaoImpl reconciliationStatusDao = ServiceLocatorFactory.getService(ReconciliationStatusDaoImpl.class);
    logger.debug("parsing reconciled order excel: " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    // HSSFSheet courierServiceInfoSheet = workbook.getSheet("Product");
    HSSFSheet courierServiceInfoSheet = workbook.getSheetAt(0);
    Iterator<Row> objRowIt = courierServiceInfoSheet.rowIterator();
    Iterator objCellIterator = null;

    // Declaring data elements
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    int rowCount = 1;
    try {
      headerMap = getRowMap(objRowIt);
      while (objRowIt.hasNext()) {
        rowMap = getRowMap(objRowIt);
        String gatewayId = getCellValue(ReportConstants.INVOICE_ID, rowMap, headerMap);
        if (gatewayId != null) {
          String reconciled = getCellValue(ReportConstants.RECONCILED, rowMap, headerMap);
          if (StringUtils.isNotBlank(reconciled) && reconciled.trim().toLowerCase().equals("y")) {
            reconciledOrders.add(gatewayId);
          } else {
            reconcilationPendingOrders.add(gatewayId);
          }
        }
        logger.debug("read row " + rowCount);
        rowCount++;
      }

      reconciliationMap.put(getBaseDao().get(ReconciliationStatus.class, EnumReconciliationStatus.DONE.getId()), reconciledOrders);
      reconciliationMap.put(getBaseDao().get(ReconciliationStatus.class, EnumReconciliationStatus.PENDING.getId()), reconcilationPendingOrders);

    } catch (Exception e) {
      logger.error("Exception @ Row:" + rowCount + 1 + e.getMessage());
      throw new Exception("Exception @ Row:" + rowCount, e);
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }
    return reconciliationMap;

  }

  public String updateCourierCollectionCharges(File objInFile) throws Exception {

    String messagePostUpdation = "";
    logger.debug("parsing collection and shipping charges for orders : " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    HSSFSheet courierCollectionChargesSheet = workbook.getSheetAt(0);
    Iterator<Row> objRowIt = courierCollectionChargesSheet.rowIterator();

    // Declaring data elements
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    Set<RetailLineItem> retailLineItemList = new HashSet<RetailLineItem>();
    int rowCount = 0;
    try {
      headerMap = getRowMap(objRowIt);
      while (objRowIt.hasNext()) {
        rowCount++;
        /*
        * List<LineItem> productLineItems = null; List<LineItem> productLineItemsByCourier = null; List<LineItem>
        * productLineItemsByAwb = null; RetailLineItem retailLineItem = null; Double
        * totalHkPriceDeliveredByCourier = 0.0; Double totalWeightDeliveredByCourier = 0.0; Long
        * totalItemsInCourier = 0L;
        */
        rowMap = getRowMap(objRowIt);
        ShippingOrder shippingOrder = getShippingOrderService().findByGatewayOrderId(getCellValue(ReportConstants.GATEWAY_ORDER_ID, rowMap, headerMap));
        String awb = getCellValue(ReportConstants.AWB, rowMap, headerMap);
        Courier courier = getCourierService().getCourierByName(getCellValue(ReportConstants.COURIER, rowMap, headerMap));
        Double shippingCharge = getDouble(getCellValue(ReportConstants.SHIPPING_CHARGE, rowMap, headerMap));
        Double collectionCharge = getDouble(getCellValue(ReportConstants.COLLECTION_CHARGE, rowMap, headerMap));
        shippingCharge = (shippingCharge == null ? 0.0 : shippingCharge);
        collectionCharge = (collectionCharge == null ? 0.0 : collectionCharge);
        // System.out.println("shippingOrder->" + shippingOrder + " awb->" + awb + " courier->" + courier +
        // "shippingcharge->" + shippingCharge + " collectionCharge->" + collectionCharge);
        if (shippingOrder == null) { // || (awb == null && courier == null) ){
          messagePostUpdation += " shippingOrder not found for Row " + rowCount + "<br/>";
          continue;
        }
        Shipment shipment = shippingOrder.getShipment();
        if (shipment == null) {
          messagePostUpdation += " Shipment corresponding to shipping order not found for Row " + rowCount + "<br/>";
          continue;
        }
        if (StringUtils.isBlank(awb) && courier == null) {
          messagePostUpdation += " awb and courier both cannot be null at Row " + rowCount + "<br/>";
          continue;
        }
        if (courier != null) {
          if (!(shipment.getCourier().equals(courier))) {
            messagePostUpdation += "Courier Name in the list and present in our DataBase mismatches at Row " + rowCount + "<br/>";
            continue;
          }
        }
        if (StringUtils.isNotBlank(awb)) {

          if (!(shipment.getAwb() != null && shipment.getAwb().getAwbNumber().equals(awb))) {
            messagePostUpdation += "AWB and shippingOrder no. mismatch at row" + rowCount + ".<br/>";
            continue;
          }
        }
          shipment.setCollectionCharge(collectionCharge);
          shipment.setShipmentCharge(shippingCharge);
          shipment.setShippingOrder(shippingOrder);
          shipmentService.save(shipment);

        /*
        * if (courier != null) { productLineItemsByCourier =
        * shippingOrder.getProductLineItemByCourier(courier); } if (StringUtils.isNotBlank(awb)) {
        * productLineItemsByAwb = shippingOrder.getProductLineItemWithAwb(awb); }
        */

        // productLineItemsByAwb.removeAll(Collections.singleton(null));
        // productLineItemsByCourier.removeAll(Collections.singleton(null));
        // productLineItems taken from awb will be preffered
        /*
        * if (productLineItemsByAwb != null && !productLineItemsByAwb.isEmpty()) { productLineItems =
        * productLineItemsByAwb; } else { productLineItems = productLineItemsByCourier; } if (productLineItems ==
        * null || productLineItems.isEmpty()) { messagePostUpdation += " no products delivered with gateway
        * shippingOrder id " + shippingOrder.getGatewayOrderId() + " at Row " + rowCount + "<br/> "; continue; }
        * for (LineItem productLineItem : productLineItems) { totalHkPriceDeliveredByCourier +=
        * productLineItem.getHkPrice() * productLineItem.getQty(); totalItemsInCourier +=
        * productLineItem.getQty(); }
        *//**
         * This for loop is added to distribute the shipping charges as per the weight of each product
         * variant. And this is set to 0 when the product variant has 0 value due to insufficient data.
         */
        /*
        * for (LineItem productLineItem : productLineItems) {
        *//*
                     * if (productLineItem.getProductVariant().getWeight() != null &&
                     * productLineItem.getProductVariant().getWeight() != 0.0) { totalWeightDeliveredByCourier +=
                     * productLineItem.getProductVariant().getWeight() * productLineItem.getQty(); } else {
                     * totalWeightDeliveredByCourier = 0.0D; break; }
                     *//*
                     * } for (LineItem productLineItem : productLineItems) { Double shippingChargedPerLineItem = 0.0D,
                     * collectionChargedPerLineItem = 0.0D; retailLineItem =
                     * retailLineItemDao.getRetailLineItem(productLineItem); if (retailLineItem == null) {
                     * messagePostUpdation += " retail line item not found for product line item " +
                     * productLineItem.getId() + " gateway shippingOrder id " + shippingOrder.getGatewayOrderId() + " at
                     * Row " + rowCount + "<br/> "; continue; } if (totalWeightDeliveredByCourier == 0.0) {
                     * shippingChargedPerLineItem = 1 / totalItemsInCourier.doubleValue() * shippingCharge; } else {
                     * //shippingChargedPerLineItem = (productLineItem.getProductVariant().getWeight() /
                     * totalWeightDeliveredByCourier) * shippingCharge; } collectionChargedPerLineItem =
                     * (productLineItem.getHkPrice() / totalHkPriceDeliveredByCourier) * collectionCharge;
                     * retailLineItem.setShippingChargedByCourier(shippingChargedPerLineItem);
                     * retailLineItem.setCollectionChargedByCourier(collectionChargedPerLineItem);
                     * retailLineItemDao.save(retailLineItem); }
                     *//*
                     * ProductVariant productVariant = productVariantDao.find(getCellValue(VARIANT_ID, rowMap,
                     * headerMap)); if (productVariant != null) { Long qty = getLong(getCellValue(QTY, rowMap,
                     * headerMap)); logger.debug("qty of variant - " + productVariant.getId() + " is - " + qty); if (qty !=
                     * null && qty > 0) { Double cost = getDouble(getCellValue(COST, rowMap, headerMap)); Double mrp =
                     * getDouble(getCellValue(MRP, rowMap, headerMap));
                     *//**//*
                         * RetailLineItem retailLineItem = new RetailLineItem();
                         * retailLineItem.setPurchaseOrder(purchaseOrder); retailLineItem.setQty(qty);
                         * retailLineItem.setProductVariant(productVariant); retailLineItem.setCostPrice(cost);
                         * retailLineItem.setMrp(mrp); poLineItemDao.save(retailLineItem);
                         * retailLineItemList.add(retailLineItem);
                         *//**//*
                         * } }
                         *//*
                     * logger.debug("read row " + rowCount);
                     */
      }

    } catch (Exception e) {
      logger.error("Exception @ Row:" + rowCount + 1 + e.getMessage());
      throw new Exception("Exception @ Row:" + rowCount, e);
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }
    logger.debug("parsing collection and shipping charges for orders  : " + objInFile.getAbsolutePath() + " completed ");
    logger.debug("message post updation " + messagePostUpdation);
    return messagePostUpdation;
  }

  public String updateEstimatedCourierCollectionCharges(File objInFile) throws Exception {
    /**
     * The excel here will have 6 columns 1. GATEWAY_ORDER_ID 2. AWB 3. COURIER 4. SHIPPING_CHARGE 5.
     * COLLECTION_CHARGE 6. EXTRA_CHARGES
     */
    String messagePostUpdation = "";
    logger.debug("parsing estimated collection and shipping charges for orders : " + objInFile.getAbsolutePath());

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    HSSFSheet courierCollectionChargesSheet = workbook.getSheetAt(0);
    Iterator<Row> objRowIt = courierCollectionChargesSheet.rowIterator();

    // Declaring data elements
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    Set<RetailLineItem> retailLineItemList = new HashSet<RetailLineItem>();
    int rowCount = 0;
    try {
      headerMap = getRowMap(objRowIt);
      while (objRowIt.hasNext()) {
        rowCount++;
        /*
        * List<LineItem> productLineItems = null; List<LineItem> productLineItemsByCourier = null; List<LineItem>
        * productLineItemsByAwb = null; RetailLineItem retailLineItem = null; Double
        * totalHkPriceDeliveredByCourier = 0.0; Double totalWeightDeliveredByCourier = 0.0; Long
        * totalItemsInCourier = 0L;
        */
        rowMap = getRowMapStringFormat(objRowIt);
        ShippingOrder shippingOrder = getShippingOrderService().findByGatewayOrderId(getCellValue(ReportConstants.GATEWAY_ORDER_ID, rowMap, headerMap));
        String awb = getCellValue(ReportConstants.AWB, rowMap, headerMap);
        Courier courier = getCourierService().getCourierByName(getCellValue(ReportConstants.COURIER, rowMap, headerMap));
        Double shippingChargeFromExcel = getDouble(getCellValue(ReportConstants.SHIPPING_CHARGE, rowMap, headerMap));
        Double collectionChargeFromExcel = getDouble(getCellValue(ReportConstants.COLLECTION_CHARGE, rowMap, headerMap));
        Double extraCharges = getDouble(getCellValue(ReportConstants.EXTRA_CHARGES, rowMap, headerMap));
        shippingChargeFromExcel = (shippingChargeFromExcel == null ? 0.0 : shippingChargeFromExcel);
        collectionChargeFromExcel = (collectionChargeFromExcel == null ? 0.0 : collectionChargeFromExcel);
        extraCharges = (extraCharges == null ? 0.0 : extraCharges);
        // System.out.println("shippingOrder->" + shippingOrder + " awb->" + awb + " courier->" + courier +
        // "shippingcharge->" + shippingChargeFromExcel + " collectionChargeFromExcel->" +
        // collectionChargeFromExcel + " extraCharges->" + extraCharges);
        if (shippingOrder == null) { // || (awb == null && courier == null) ){
          messagePostUpdation += " shippingOrder not found for Row " + rowCount + "<br/>";
          continue;
        }
        Shipment shipment = shippingOrder.getShipment();
        if (shipment == null) {
          messagePostUpdation += " Shipment corresponding to shipping order not found for Row " + rowCount + "<br/>";
          continue;
        }
        if (StringUtils.isBlank(awb) && courier == null) {
          messagePostUpdation += " awb and courier both cannot be null at Row " + rowCount + "<br/>";
          continue;
        }
        if (courier != null) {
          if (!(shipment.getCourier().equals(courier))) {
            messagePostUpdation += "Courier Name in the list and present in our DataBase mismatches at Row " + rowCount + "<br/>";
            continue;
          }
        }
        if (StringUtils.isNotBlank(awb)) {

          if (!(shipment.getAwb() != null && shipment.getAwb().getAwbNumber().equals(awb))) {
            messagePostUpdation += "AWB and shippingOrder no. mismatch at row" + rowCount + ".<br/>";
            continue;
          }
        }
        shipment.setEstmCollectionCharge(collectionChargeFromExcel);
        shipment.setEstmShipmentCharge(shippingChargeFromExcel);
        shipment.setExtraCharge(extraCharges);
        getShipmentService().save(shipment);
        // this is to give preference to productlineitem list by awb in case of mismatch.
        /*
        * if (productLineItemsByAwb != null && !productLineItemsByAwb.isEmpty()) { productLineItems =
        * productLineItemsByAwb; } else { productLineItems = productLineItemsByCourier; } if (productLineItems ==
        * null || productLineItems.isEmpty()) { messagePostUpdation += " no products delivered with gateway
        * shippingOrder id " + shippingOrder.getGatewayOrderId() + " at Row " + rowCount + ".<br/> ";
        * continue; } for (LineItem productLineItem : productLineItems) { totalHkPriceDeliveredByCourier +=
        * productLineItem.getHkPrice() * productLineItem.getQty(); totalItemsInCourier +=
        * productLineItem.getQty(); }
        *//**
         * This for loop is added to distribute the shipping charges as per the weight of each product
         * variant. And this is set to 0 when the product variant has 0 value due to insufficient data.
         */
        /*
        * for (LineItem productLineItem : productLineItems) { if
        * (productLineItem.getSku().getProductVariant().getWeight() != null &&
        * productLineItem.getSku().getProductVariant().getWeight() != 0.0) { totalWeightDeliveredByCourier +=
        * productLineItem.getSku().getProductVariant().getWeight() * productLineItem.getQty(); } else {
        * totalWeightDeliveredByCourier = 0.0D; break; } } for (LineItem productLineItem : productLineItems) {
        * retailLineItem = retailLineItemDao.getRetailLineItem(productLineItem); if (retailLineItem == null) {
        * messagePostUpdation += " retail line item not found for product line item " + productLineItem.getId() + "
        * gateway shippingOrder id " + shippingOrder.getGatewayOrderId() + " at Row " + rowCount + "<br/> ";
        * continue; } CourierCharge courierCharge = CourierChargeUtil.getCourierCharge(productLineItem,
        * totalWeightDeliveredByCourier, totalHkPriceDeliveredByCourier, totalItemsInCourier,
        * shippingChargeFromExcel, collectionChargeFromExcel, extraCharges);
        * retailLineItem.setEstimatedShippingChargedByCourier(courierCharge.getShippingCharged());
        * retailLineItem.setEstimatedCollectionChargedByCourier(courierCharge.getCollectionCharged());
        * retailLineItem.setExtraCharge(courierCharge.getExtraCharge());
        * retailLineItemDao.save(retailLineItem); } logger.debug("read row " + rowCount); }
        */

        /*
        * ProductVariant productVariant = productVariantDao.find(getCellValue(VARIANT_ID, rowMap, headerMap)); }
        */

      }
    } catch (Exception e) {
      logger.error("Exception @ Row:" + rowCount + 1 + e.getMessage());
      throw new Exception("Exception @ Row:" + rowCount, e);
    } finally {
      if (poiInputStream != null) {
        IOUtils.closeQuietly(poiInputStream);
      }
    }
    logger.debug("parsing collection and shipping charges for orders  : " + objInFile.getAbsolutePath() + " completed ");
    logger.debug("message post updation " + messagePostUpdation);
    return messagePostUpdation;
  }

  private Tax getTaxDetails(String taxName) {

    /*
    * Matcher matcher = p.matcher(taxName); String value = "0.0"; //Fix for Null pointer error if (matcher.find()) {
    * value = matcher.group(1); } Tax tax = new Tax(); tax.setName(taxName); double percentVal = 0; try {
    * percentVal = Double.parseDouble(value); } catch (NumberFormatException e) { throw new
    * RuntimeException("Invalid tax value/or unable to parse : " + taxName); } tax.setValue(percentVal / 100);
    * return tax;
    */
    Tax taxDb = getTaxService().findByName(taxName);
    if (taxDb == null) {
      taxDb = getTaxService().findByName("0.125");
    }

    return taxDb;

  }

  /**
   * reads all column values that start with the prefix VAR_ returns a list of product options option name = value of
   * string minus the VAR_ prefix option value = value in the cell
   *
   * @param productOptionsStr
   * @return
   */
  private List<ProductOption> getProductOptions(String productOptionsStr) {
    List<ProductOption> productOptions = new ArrayList<ProductOption>();
    if (StringUtils.isBlank(productOptionsStr)) {
      return productOptions;
    }

    String[] productOptionStrArray = StringUtils.split(productOptionsStr, "|");

    for (String productOptionStr : productOptionStrArray) {
      String[] optionNameValueArray = StringUtils.split(productOptionStr, ":");
      if (optionNameValueArray.length > 1) {
        ProductOption productOption = new ProductOption(optionNameValueArray[0].trim(), optionNameValueArray[1].trim());
        productOptions.add(productOption);
      }
    }

    return productOptions;
  }

  private List<ProductExtraOption> getProductExtraOptions(String productOptionsStr) {
    List<ProductExtraOption> productOptions = new ArrayList<ProductExtraOption>();
    if (StringUtils.isBlank(productOptionsStr)) {
      return productOptions;
    }

    String[] productOptionStrArray = StringUtils.split(productOptionsStr, "|");

    for (String productOptionStr : productOptionStrArray) {
      String[] optionNameValueArray = StringUtils.split(productOptionStr, ":");
      ProductExtraOption productOption = new ProductExtraOption(optionNameValueArray[0].trim(), optionNameValueArray[1].trim());
      productOptions.add(productOption);
    }

    return productOptions;
  }

  private List<Product> getRelatedProductsFromExcel(String relatedProductStr) {
    List<Product> relatedProducts = new ArrayList<Product>();
    if (StringUtils.isBlank(relatedProductStr)) {
      return relatedProducts;
    }

    String[] relatedProductStrArray = StringUtils.split(relatedProductStr, "|");

    for (String relatedProductId : relatedProductStrArray) {
      relatedProducts.add(getProductService().getProductById(relatedProductId));
    }
    return relatedProducts;
  }

  public Set<Product> readAndSetRelatedProducts(File objInFile) throws Exception {
    Set<Product> productSet = new HashSet<Product>();

    InputStream poiInputStream = new FileInputStream(objInFile);
    POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    for (int i = 0; i < 8; i++) {
      HSSFSheet excelSheet = workbook.getSheetAt(i);
      if (excelSheet != null) {
        Iterator<Row> objRowIt = excelSheet.rowIterator();

        // Declaring data elements
        Map<Integer, String> headerMap;
        Map<Integer, String> rowMap;

        int rowCount = 1;
        try {
          headerMap = getRowMap(objRowIt);
          Product mainProduct = null;
          List<Product> relatedProducts = new ArrayList<Product>();
          while (objRowIt.hasNext()) {
            rowMap = getRowMap(objRowIt);
            if (StringUtils.isBlank(getCellValue(XslConstants.PRODUCT_ID, rowMap, headerMap))) {
              // this is not a new product. add a new product variant
            } else {
              Product product = getProductService().getProductById(getCellValue(XslConstants.PRODUCT_ID, rowMap, headerMap));
              if (mainProduct == null || !mainProduct.equals(product)) {
                if (mainProduct != null) {
                  getProductService().save(mainProduct);
                  logger.debug("Saved old product. Will set new product as main product now.");
                  productSet.add(mainProduct);
                }
                mainProduct = product;
                relatedProducts = new ArrayList<Product>();
              }
            }
            if (mainProduct != null && relatedProducts.size() < 6) {
              Product crossProduct = getProductService().getProductById(getCellValue(XslConstants.CROSS_PRODUCT_ID, rowMap, headerMap));
              if (crossProduct != null && !crossProduct.equals(mainProduct) && !crossProduct.isDeleted()) {
                relatedProducts.add(crossProduct);
                mainProduct.setRelatedProducts(relatedProducts);
              }
            }
            logger.debug("Read row " + rowCount);
            rowCount++;
          }
        } catch (Exception e) {
          logger.error("Exception @ Row:" + rowCount + 1 + e.getMessage());
          throw new Exception("Exception @ Row:" + rowCount + " at Sheet:" + i, e);
        } finally {
          if (poiInputStream != null) {
            IOUtils.closeQuietly(poiInputStream);
          }
        }
      }
    }
    return productSet;

  }

  /**
   * Returns a list of categories. Category format Eg: <p/> Baby, Baby Food, Cereal ; Baby, Baby Food, Formula <p/>
   * Here parent relationships are set . left most category is the parent. <p/> Eg:
   * <p/>
   * <p/> Category String = Diabetes>Testing Supplies>Meters>GLUCOCARD01| Home Health Devices>Diabetes Meters>Blood
   * Glucose Meters>GLUCOCARD01 <p/> <p/> Parsing is done as follows for the above string: <p/>
   * <p/>
   * <pre>
   *   [diabetes: Diabetes ()]
   * &lt;p/&gt;
   *            [diabetes&gt;testing supplies: Testing Supplies (Diabetes)]
   *                 |                                   |        |
   *   {fullname-&gt;primary key.. md5 of this is taken}    |    {parent display name}
   *                                                     |
   *                                    {display name-&gt; for UI}
   * &lt;p/&gt;
   * </pre>
   * <p/>
   * <p/> and so on... <p/> [diabetes>testing supplies>meters: Meters (Testing Supplies)] [diabetes>testing
   * supplies>meters>glucocard01: GLUCOCARD01 (Meters)] [home health devices: Home Health Devices ()] [home health
   * devices>diabetes meters: Diabetes Meters (Home Health Devices)] [home health devices>diabetes meters>blood
   * glucose meters: Blood Glucose Meters (Diabetes Meters)] [home health devices>diabetes meters>blood glucose
   * meters>glucocard01: GLUCOCARD01 (Blood Glucose Meters)]
   *
   * @param categoryString
   * @return
   */
  public List<Category> getCategroyListFromCategoryString(String categoryString) {
    logger.debug("Category -> " + categoryString);
    List<Category> catFamilyList = new ArrayList<Category>();

    if (StringUtils.isNotBlank(categoryString)) {
      String[] catFamilies = StringUtils.split(categoryString, "|");

      for (int i = 0; i < catFamilies.length; i++) {
        String catFamily = catFamilies[i].trim();
        if (catFamily.charAt(catFamily.length() - 1) != '>') {
          catFamily = catFamily + ">";
        }

        Category parentCategory = null;
        String categoryTree = "";

        while (catFamily.indexOf('>') != -1) {
          String displayName = catFamily.substring(0, catFamily.indexOf('>'));
          String fullName = categoryTree + displayName;
          categoryTree = categoryTree + displayName + ">";

          Category category = new Category();
          category.setName(Category.getNameFromDisplayName(displayName));
          category.setDisplayName(displayName);
          catFamilyList.add(category);

          parentCategory = category;

          catFamily = catFamily.substring(catFamily.indexOf('>') + 1, catFamily.length());
        }
      }
    }
    logger.debug("catFamilyList: " + catFamilyList.size());
    return catFamilyList;
  }

  public static void main(String[] args) {
    String catString = "Diabetes>Testing Supplies>Meters>GLUCOCARD01| Home Health Devices>Diabetes Meters>Blood Glucose Meters>GLUCOCARD01";
    XslParser xslParser = new XslParser();
    List<Category> categoryList = xslParser.getCategroyListFromCategoryString(catString);
    for (Category category : categoryList) {
      ;
      // System.out.println(category.toString());
    }
  }

  private String getCellValue(String header, Map<Integer, String> rowMap, Map<Integer, String> headerMap) {
    Integer columnIndex = getColumnIndex(header, headerMap);
    if (columnIndex == null)
      return null;
    String cellVal = rowMap.get(columnIndex);
    return cellVal == null ? "" : cellVal.trim();
  }

  private Integer getColumnIndex(String header, Map<Integer, String> headerMap) {
    Integer columnIndex = null;
    for (Integer key : headerMap.keySet()) {
      if (headerMap.get(key).equals(header))
        columnIndex = key;
    }
    return columnIndex;
  }

  private Date getDate(String value) {
    Date date = null;
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
      date = (Date) formatter.parse(value);
    } catch (Exception e) {

    }
    return date;
  }

  private Double getDouble(String value) {
    Double valueInDouble = null;
    try {
      valueInDouble = Double.parseDouble(value);
    } catch (Exception e) {

    }
    return valueInDouble;
  }

  private Long getLong(String value) {
    Long valueInLong = null;
    try {
      valueInLong = Long.parseLong(value.replace(".0", ""));
    } catch (Exception e) {

    }
    return valueInLong;
  }

  private Map<Integer, String> getRowMap(Iterator<Row> objRowIt) {
    // Header are read and related columns are taken care of
    // accordignly.

    Map<Integer, String> headerMap = new HashMap<Integer, String>();

    HSSFRow headers = (HSSFRow) objRowIt.next();
    Iterator objCellIterator = headers.cellIterator();
    while (objCellIterator.hasNext()) {
      HSSFCell headerCell = (HSSFCell) objCellIterator.next();
      int headerColIndex = 0;
      headerColIndex = headerCell.getColumnIndex();
      Object cellValue = null;
      try {
        cellValue = headerCell.getStringCellValue();
        headerMap.put(headerColIndex, cellValue.toString());
      } catch (Exception e) {
        logger.debug("error trying to read column " + headerColIndex + " as String on Row " + headers.getRowNum() + " : Cell toString = " + headerCell.toString());
        logger.debug("Now trying to read as numeric");
        try {
          cellValue = headerCell.getNumericCellValue();
          headerMap.put(headerColIndex, cellValue.toString());
        } catch (Exception e1) {
          logger.debug("error reading cell value as numeric - " + headerCell.toString());
        }
      }
    }

    return headerMap;
  }

  private Manufacturer getManufacturerDetails(String manufacturerName, HSSFSheet manufacturerSheet) {
    if (manufacturerSheet != null) {
      Iterator<Row> rowIterator = manufacturerSheet.rowIterator();
      Map<Integer, String> headerMap = getRowMap(rowIterator);
      Manufacturer manufacturer = null;

      while (rowIterator.hasNext()) {
        Map<Integer, String> rowMap = getRowMap(rowIterator);

        String name = getCellValue(XslConstants.MANUFACTURER_NAME, rowMap, headerMap);
        if (name != null && name.equals(manufacturerName)) {
          manufacturer = new Manufacturer();
          manufacturer.setName(name);
          manufacturer.setDescription(getCellValue(XslConstants.MANUFACTURER_DESCRIPTION, rowMap, headerMap));
          manufacturer.setWebsite(getCellValue(XslConstants.MANUFACTURER_WEBSITE, rowMap, headerMap));
          manufacturer.setEmail(getCellValue(XslConstants.MANUFACTURER_EMAIL, rowMap, headerMap));
          String isPanIndia = getCellValue(XslConstants.MANUFACTURER_PAN_INDIA, rowMap, headerMap);
          boolean isPanIndiaBoolean = StringUtils.isNotBlank(isPanIndia) && isPanIndia.trim().toLowerCase().equals("y") ? true : false;
          manufacturer.setAvailableAllOverIndia(isPanIndiaBoolean);
          break;
        }
      }

      return manufacturer;
    } else
      return null;
  }

  private Supplier getSupplierDetails(String supplier_tin, String state, Integer rowCount) throws HealthKartCatalogUploadException {
    Supplier supplier = null;
    supplier = supplierDao.findByTIN(supplier_tin);
    if (supplier == null) {
      throw new HealthKartCatalogUploadException("<br>Unable to find the supplier with given tin @row: " + rowCount, rowCount);
    }
    return supplier;
  }

  public Product getProductFromList(String id) {
    for (Iterator iterator = colProductList.iterator(); iterator.hasNext();) {
      Product p = (Product) iterator.next();
      if (p != null && p.getId() != null && p.getId().equals(id))
        return p;
    }
    return null;
  }

  private Map<Integer, String> getRowMapStringFormat(Iterator<Row> objRowIt) {
    // Header are read and related columns are taken care of
    // accordignly.

    Map<Integer, String> headerMap = new HashMap<Integer, String>();

    HSSFRow headers = (HSSFRow) objRowIt.next();
    Iterator objCellIterator = headers.cellIterator();
    while (objCellIterator.hasNext()) {
      HSSFCell headerCell = (HSSFCell) objCellIterator.next();
      int headerColIndex = 0;
      headerColIndex = headerCell.getColumnIndex();
      Object cellValue = null;
      try {
        headerCell.setCellType(Cell.CELL_TYPE_STRING);
        cellValue = headerCell.getStringCellValue();
        headerMap.put(headerColIndex, cellValue.toString());
      } catch (Exception e) {
        logger.debug("error trying to read column " + headerColIndex + " as String on Row " + headers.getRowNum() + " : Cell toString = " + headerCell.toString());
        logger.debug("Now trying to read as numeric");
        try {
          cellValue = headerCell.getNumericCellValue();
          headerMap.put(headerColIndex, cellValue.toString());
        } catch (Exception e1) {
          logger.debug("error reading cell value as numeric - " + headerCell.toString());
        }
      }
    }

    return headerMap;
  }


//  public Set<Awb> readAwbExcel(File file) throws Exception {
//    logger.debug("parsing Awb info : " + file.getAbsolutePath());
//    Set<Awb> awbSet = new HashSet<Awb>();
//    InputStream awbInputStream = new FileInputStream(file);
//    POIFSFileSystem awbInFileSys = new POIFSFileSystem(awbInputStream);
//
//    HSSFWorkbook workbook = new HSSFWorkbook(awbInFileSys);
//
//    // Assuming there is only one sheet, the first one only will be picked
//    HSSFSheet awbSheet = workbook.getSheet("AWB");
//    Iterator<Row> objRowIt = awbSheet.rowIterator();
//    Map<Integer, String> headerMap;
//    Map<Integer, String> rowMap;
//    int rowCount = 1;
//    headerMap = getRowMap(objRowIt);
//    try {
//      while (objRowIt.hasNext()) {
//        rowCount++;
//        rowMap = getRowMap(objRowIt);
//        String courierId = getCellValue(XslConstants.COURIER_ID, rowMap, headerMap);
//        String awbNumber = getCellValue(XslConstants.AWB_NUMBER, rowMap, headerMap);
//        String cod = getCellValue(XslConstants.COD, rowMap, headerMap);
//
//        Awb awb = new Awb();
//        if (StringUtils.isEmpty(courierId)) {
//
//          if (StringUtils.isEmpty(awbNumber) && cod.isEmpty()) {
//            if (awbSet.size() > 0) {
//              return awbSet;
//            }
//            return null;
//
//          } else {
//            logger.error("courier id cannot be call");
//            throw new ExcelBlankFieldException("courier ID  cannot be null" + "    ", rowCount);
//          }
//
//        }
//        Courier courier = courierDao.getCourierById(getLong(courierId));
//        awb.setCourier(courier);
//        if (StringUtils.isEmpty(awbNumber)) {
//          logger.error("awbNumber cannot be call");
//          throw new ExcelBlankFieldException("awbNumber cannot be null " + "    ", rowCount);
//
//        }
//        awb.setAwbNumber(awbNumber);
//        awb.setAwbBarCode(awbNumber);
//        awb.setUsed(false);
//        if (userService.getWarehouseForLoggedInUser() == null) {
//          throw new ExcelBlankFieldException("Please login in warehouse");
//        }
//        awb.setWarehouse(userService.getWarehouseForLoggedInUser());
//        if (cod.isEmpty()) {
//          throw new ExcelBlankFieldException("Please enter mode of payment");
//        }
//        if (getLong(cod).equals(0l)) {
//          awb.setCod(true);
//        } else if (getLong(cod).equals(1l)) {
//          awb.setCod(false);
//        }
//        awbSet.add(awb);
//      }
//    } catch (ExcelBlankFieldException e) {
//      throw new ExcelBlankFieldException(e.getMessage());
//
//    }
//
//    finally {
//    }
//    if (awbInputStream != null) {
//      IOUtils.closeQuietly(awbInputStream);
//    }
//
//    if (awbSet.size() > 0) {
//      return awbSet;
//    }
//
//    return null;
//
//  }



  public boolean checkIfNotEmpty(String excelField, int rowCount) {
    if (StringUtils.isEmpty(excelField)) {
      logger.error("excelField cannot be call");
      throw new ExcelBlankFieldException("excelField cannot be null " + "    ", rowCount);
    }
    return true;
  }


  public PurchaseOrderDao getPurchaseOrderDao() {
    return purchaseOrderDao;
  }

  public void setPurchaseOrderDao(PurchaseOrderDao purchaseOrderDao) {
    this.purchaseOrderDao = purchaseOrderDao;
  }

  public LowInventoryDao getLowInventoryDao() {
    return lowInventoryDao;
  }

  public void setLowInventoryDao(LowInventoryDao lowInventoryDao) {
    this.lowInventoryDao = lowInventoryDao;
  }

  public SupplierDao getSupplierDao() {
    return supplierDao;
  }

  public void setSupplierDao(SupplierDao supplierDao) {
    this.supplierDao = supplierDao;
  }

  public GrnLineItemDao getGrnLineItemDao() {
    return grnLineItemDao;
  }

  public void setGrnLineItemDao(GrnLineItemDao grnLineItemDao) {
    this.grnLineItemDao = grnLineItemDao;
  }

  public PoLineItemDao getPoLineItemDao() {
    return poLineItemDao;
  }

  public void setPoLineItemDao(PoLineItemDao poLineItemDao) {
    this.poLineItemDao = poLineItemDao;
  }

  public RetailLineItemDao getRetailLineItemDao() {
    return retailLineItemDao;
  }

  public void setRetailLineItemDao(RetailLineItemDao retailLineItemDao) {
    this.retailLineItemDao = retailLineItemDao;
  }

  public ProductService getProductService() {
    return productService;
  }

  public void setProductService(ProductService productService) {
    this.productService = productService;
  }

  public ProductVariantService getProductVariantService() {
    return productVariantService;
  }

  public void setProductVariantService(ProductVariantService productVariantService) {
    this.productVariantService = productVariantService;
  }

  public CourierService getCourierService() {
    return courierService;
  }

  public void setCourierService(CourierService courierService) {
    this.courierService = courierService;
  }

  public PincodeService getPincodeService() {
    return pincodeService;
  }

  public void setPincodeService(PincodeService pincodeService) {
    this.pincodeService = pincodeService;
  }

  public SkuService getSkuService() {
    return skuService;
  }

  public void setSkuService(SkuService skuService) {
    this.skuService = skuService;
  }

  public ShipmentService getShipmentService() {
    return shipmentService;
  }

  public void setShipmentService(ShipmentService shipmentService) {
    this.shipmentService = shipmentService;
  }

  public TaxService getTaxService() {
    return taxService;
  }

  public void setTaxService(TaxService taxService) {
    this.taxService = taxService;
  }

  public RoleService getRoleService() {
    return roleService;
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  public CategoryService getCategoryService() {
    return categoryService;
  }

  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  public WarehouseService getWarehouseService() {
    return warehouseService;
  }

  public void setWarehouseService(WarehouseService warehouseService) {
    this.warehouseService = warehouseService;
  }

  public ShippingOrderService getShippingOrderService() {
    return shippingOrderService;
  }

  public void setShippingOrderService(ShippingOrderService shippingOrderService) {
    this.shippingOrderService = shippingOrderService;
  }

  public PaymentService getPaymentService() {
    return paymentService;
  }

  public void setPaymentService(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  public AffiliateCategoryDaoImpl getAffiliateCategoryDao() {
    return affiliateCategoryDao;
  }

  public void setAffiliateCategoryDao(AffiliateCategoryDaoImpl affiliateCategoryDao) {
    this.affiliateCategoryDao = affiliateCategoryDao;
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

  public AdminInventoryService getAdminInventoryService() {
    return adminInventoryService;
  }

  public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
    this.adminInventoryService = adminInventoryService;
  }

  public InventoryService getInventoryService() {
    return inventoryService;
  }

  public void setInventoryService(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

}
