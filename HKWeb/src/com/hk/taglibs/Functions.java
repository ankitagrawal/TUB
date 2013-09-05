package com.hk.taglibs;

import com.akube.framework.util.DateUtils;
import com.akube.framework.util.FormatUtils;
import com.hk.admin.impl.service.email.ProductInventoryDto;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.ProductVariantDamageInventoryDao;
import com.hk.admin.pact.service.catalog.product.ProductVariantSupplierInfoService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.email.ProductVariantNotifyMeEmailService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.GrnLineItemService;
import com.hk.admin.util.CourierStatusUpdateHelper;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.courier.StateList;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.*;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.content.HeadingProduct;
import com.hk.domain.core.Country;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderLifecycle;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.menu.MenuNode;
import com.hk.edge.pact.service.StoreVariantService;
import com.hk.edge.response.variant.StoreVariantBasicApiResponse;
import com.hk.helper.MenuHelper;
import com.hk.impl.service.queue.BucketService;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.manager.LinkManager;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.queue.ActionItemDao;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.dao.sku.SkuDao;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.accounting.InvoiceService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.homeheading.HeadingProductService;
import com.hk.pact.service.image.ProductImageService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService;
import com.hk.report.pact.service.catalog.product.ReportProductVariantService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.CartLineItemUtil;
import com.hk.util.HKImageUtils;
import com.hk.util.OrderUtil;
import com.hk.util.ProductUtil;
import com.hk.web.AppConstants;
import net.sourceforge.stripes.util.CryptoUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class Functions {

    @SuppressWarnings("unused")
    private static final PeriodFormatter formatter;

    private static final String DEFAULT_DELIEVERY_DAYS = "1-3";
    private static final String BUSINESS_DAYS = " business days";

    // TODO: rewrite
    static {
        formatter = new PeriodFormatterBuilder().appendYears().appendSuffix(" year, ", " years, ").appendMonths().appendSuffix(" month, ", " months, ").appendWeeks().appendSuffix(
                " week, ", " weeks, ").appendDays().appendSuffix(" day, ", " days, ").appendHours().appendSuffix(" hour, ", " hours, ").appendMinutes().appendSuffix(" minute, ",
                " minutes, ").appendSeconds().appendSuffix(" second, ", " seconds").toFormatter();

        // menuHelper = ServiceLocatorFactory.getService(MenuHelper.class);

        // TODO: rewrite
    }

    private static Logger logger = LoggerFactory.getLogger(Functions.class);

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static String encrypted(String str) {
        return CryptoUtil.encrypt(str);
    }

    public static String urlEncode(String str) {
        String s = str;
        try {
            s = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException in urlEncode: " + str, e);
        }
        return s;
    }

    public static String formatDate(Date date) {
        return FormatUtils.getDefaultFormattedDate(date);
    }

    public static String timeAgo(Date date) {
        DateTime dateTime = new DateTime(date);

        DateTime now = new DateTime();
        Period period = new Period(dateTime, now);

        String fullPeriod = formatter.print(period.normalizedStandard());
        String formattedPeriod;

        int firstPeriodPos = fullPeriod.indexOf(",");
        if (firstPeriodPos != -1) {
            int secondPeriodPos = fullPeriod.indexOf(",", firstPeriodPos + 1);
            if (secondPeriodPos != -1) {
                formattedPeriod = fullPeriod.substring(0, secondPeriodPos);
            } else {
                formattedPeriod = fullPeriod.substring(0, firstPeriodPos);
            }
        } else {
            formattedPeriod = StringUtils.isBlank(fullPeriod) ? "0 seconds" : fullPeriod;
        }

        return formattedPeriod + " ago";
    }

    public static String periodFromNow(Date date) {
        DateTime dateTime = new DateTime(date);

        DateTime now = new DateTime();
        Period period = new Period(now, dateTime);

        String fullPeriod = formatter.print(period.normalizedStandard());
        String formattedPeriod;

        int firstPeriodPos = fullPeriod.indexOf(",");
        if (firstPeriodPos != -1) {
            int secondPeriodPos = fullPeriod.indexOf(",", firstPeriodPos + 1);
            if (secondPeriodPos != -1) {
                formattedPeriod = fullPeriod.substring(0, secondPeriodPos);
            } else {
                formattedPeriod = fullPeriod.substring(0, firstPeriodPos);
            }
        } else {
            formattedPeriod = StringUtils.isBlank(fullPeriod) ? "0 seconds" : fullPeriod;
        }

        return formattedPeriod;
    }

    public static String decimal2(Double n) {
        if (n == null) {
            return "0.00";
        }
        return FormatUtils.getDecimalFormat(n);
    }

    public static String convertToLettersNumbersUnderscore(String s) {
        if (s == null) {
            return "";
        }
        return s.replaceAll(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");
    }

    @SuppressWarnings("unchecked")
    public static boolean collectionContains(Collection c, Object o) {
        if (c == null) {
            return false;
        }
        return c.contains(o);
    }

    @SuppressWarnings("unchecked")
    public static boolean collectionContainsBoth(Collection c, Object o1, Object o2) {
        if (c == null) {
            return false;
        }
        return c.contains(o1) && c.contains(o2);
    }

    public static boolean firstStringContainsSecond(String s1, String s2) {
        if (s1 == null) {
            return false;
        }
        return s1.contains(s2);
    }

    /**
     * checks if c1 contains all elements of c2
     *
     * @param c1
     * @param c2
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean collectionContainsCollection(Collection c1, Collection c2) {
        if (c1 == null || c2 == null) {
            return false;
        }
        boolean collectionContainsCollection = true;

        for (Object o : c2) {
            if (!c1.contains(o)) {
                collectionContainsCollection = false;
                break;
            }
        }

        /*
         * for (Object o : c2) { if (collectionContains(c1, o) && collectionContains(c2, o)) { return
         * collectionContains(c, o); } }
         */

        return collectionContainsCollection;
    }

    @SuppressWarnings("unchecked")
    public static boolean collectionContainsAnyCollectionItem(Collection c1, Collection c2) {
        if (c1 == null || c2 == null) {
            return false;
        }
        for (Object o : c2) {
            if (c1.contains(o)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean urlContainsAnyCategory(String url, String categoryPipeSeparatedList) {
        String[] categories = null;
        if (categoryPipeSeparatedList.contains(",")) {
            categories = categoryPipeSeparatedList.split(",");
        } else if (categoryPipeSeparatedList.contains("|")) {
            categories = categoryPipeSeparatedList.split(",");
        }
        /*
         * for (Object o : c2) { if (collectionContains(c1, o) && collectionContains(c2, o)) { return
         * collectionContains(c, o); } }
         */

        for (String category : categories) {
            if (url.contains(category.trim())) {
                return true;
            }
        }
        return false;
    }

    public static Long netAvailableUnbookedInventory(Object o) {
        return netInventory(o) - bookedQty(o, null);
    }

    public static Long netInventory(Object o) {
        AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);

        if (o instanceof Sku) {
            Sku sku = (Sku) o;
            return adminInventoryService.getNetInventory(sku);
        } else {
            ProductVariant productVariant = (ProductVariant) o;
            return adminInventoryService.getNetInventory(productVariant);
        }
    }

    public static Long netInventoryAtServiceableWarehouses(Object o) {
        AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);

        if (o instanceof Sku) {
            Sku sku = (Sku) o;
            return adminInventoryService.getNetInventory(sku);
        } else {
            ProductVariant productVariant = (ProductVariant) o;
            return adminInventoryService.getNetInventoryAtServiceableWarehouses(productVariant);
        }
    }

    public static Long bookedQty(Object o, Collection c) {
        AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);
        if (o instanceof Sku) {
            Sku sku = (Sku) o;
            List<Long> shippingOrderStatusIds = (List<Long>) c;
            return adminInventoryService.getBookedInventory(sku, shippingOrderStatusIds);
        } else {
            ProductVariant productVariant = (ProductVariant) o;
            List<Long> shippingOrderStatusIds = (List<Long>) c;
            return adminInventoryService.getBookedInventory(productVariant, shippingOrderStatusIds);
        }
    }

    public static Category topLevelCategory(Object o) {
        CategoryService categoryService = ServiceLocatorFactory.getService(CategoryService.class);
        return categoryService.getTopLevelCategory((Product) o);
    }

    public static boolean hasProductAnyCategory(Object product, String pipeSeparatedCategory) {
        Product pr = (Product) product;
        for (Category category : pr.getCategories()) {
            if (pipeSeparatedCategory.contains(category.getName())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> brandsInCategory(Object o) {
        Category primaryCategory = (Category) o;
        CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);
        return categoryDao.getBrandsByPrimaryCategory(primaryCategory);

        //return CategoryCache.getInstance().getBrandsInCategory(primaryCategory.getName());
    }

    @SuppressWarnings("deprecation")
    public static Double netDiscountOnLineItem(Object o) {
        CartLineItem lineItem = (CartLineItem) o;
        OrderManager orderManager = (OrderManager) ServiceLocatorFactory.getService("OrderManager");
        return orderManager.getNetDiscountOnLineItem(lineItem);
    }

    public static List<String> orderComments(Object o) {
        Order order = (Order) o;
        List<String> comments = new ArrayList<String>();
        OrderLoggingService orderLoggingService = ServiceLocatorFactory.getService(OrderLoggingService.class);
        for (OrderLifecycle orderLifecycle : order.getOrderLifecycles()) {
            if (orderLifecycle.getOrderLifecycleActivity().equals(orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.LoggedComment))) {
                comments.add(orderLifecycle.getComments());
            }
        }
        return comments;
    }

    public static Integer getNonDeletedVariants(Object o) {
        Product product = (Product) o;
        Integer counter = 0;
        for (ProductVariant variant : product.getProductVariants()) {
            if (!variant.isDeleted() && !variant.isOutOfStock()) {
                counter++;
            }
        }
        return counter;
    }

    public static Date getEscalationDate(Object o1) {
        ShippingOrder shippingOrder = (ShippingOrder) o1;
        ShippingOrderLifecycleDao shippingOrderLifecycleDao = ServiceLocatorFactory.getService(ShippingOrderLifecycleDao.class);
        Date date = shippingOrderLifecycleDao.getActivityDateForShippingOrder(shippingOrder, Arrays.asList(EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue,
                EnumShippingOrderLifecycleActivity.SO_EscalatedToProcessingQueue));
        return date;
    }

    public static Long checkedoutItemsCount(Object o) {
        AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);
        LineItem lineItem = (LineItem) o;
        return adminInventoryService.countOfCheckedOutUnitsOfLineItem(lineItem);
    }

    public static Boolean allItemsCheckedOut(Object o) {
        AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);
        ShippingOrder order = (ShippingOrder) o;
        return adminInventoryService.areAllUnitsOfOrderCheckedOut(order);
    }

    public static Long checkedinUnitsCount(Object o) {
        AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);
        GrnLineItem grnLineItem = (GrnLineItem) o;
        return adminInventoryService.countOfCheckedInUnitsForGrnLineItem(grnLineItem);
    }

    public static List<SkuItem> getInStockSkuItems(Object o) {
        AdminSkuItemDao adminSkuItemDao = ServiceLocatorFactory.getService(AdminSkuItemDao.class);
        return adminSkuItemDao.getInStockSkuItems((SkuGroup) o);
    }


    public static List<SkuItem> getNetPhysicalAvailableStockSkuItems(Object o) {
        AdminSkuItemDao adminSkuItemDao = ServiceLocatorFactory.getService(AdminSkuItemDao.class);
        return adminSkuItemDao.getNetPhysicalAvailableStockSkuItems((SkuGroup) o);
    }

    public static Long getComboCount(Object o1) {

        if (o1 != null) {
            CartLineItem lineItem = (CartLineItem) o1;
            if (lineItem.getComboInstance().getComboInstanceProductVariant(lineItem.getProductVariant()) != null) {
                return lineItem.getQty() / lineItem.getComboInstance().getComboInstanceProductVariant(lineItem.getProductVariant()).getQty();
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public static Double getPostpaidAmount(Object o) {
        Double postPaidAmount = 0D;
        if (o != null) {
            ProductVariant productVariant = (ProductVariant) o;
            postPaidAmount = productVariant.getPostpaidAmount() != null ? productVariant.getPostpaidAmount() : 0D;
        }
        return postPaidAmount;
    }

    public static Boolean invoiceAlreadyGenerated(Object o) {
        InvoiceService invoiceService = ServiceLocatorFactory.getService(InvoiceService.class);
        ShippingOrder shippingOrder = (ShippingOrder) o;
        return invoiceService.invoiceAlreadyGenerated(shippingOrder);
    }

    public static Long getProcessedOrdersCount(Object o) {
        UserManager userManager = (UserManager) ServiceLocatorFactory.getService("UserManager");
        User user = (User) o;
        return userManager.getProcessedOrdersCount(user);
    }

    public static String getS3ImageUrl(Object o1, Object o2) {
        EnumImageSize imageSize = (EnumImageSize) o1;
        Long imageId = (Long) o2;
        if (imageId == null) {
            return "";
        }
        return HKImageUtils.getS3ImageUrl(imageSize, imageId);
    }

    public static Boolean isFreeVariant(Object o) {
        ProductVariantDao productVariantDao = ServiceLocatorFactory.getService(ProductVariantDao.class);
        List<ProductVariant> productVariants = productVariantDao.findVariantsFromFreeVariant((ProductVariant) o);
        return productVariants != null && !productVariants.isEmpty();
    }

    public static String getExtraOptionsAsString(Object o1, String str) {
        CartLineItem cartLineItem = (CartLineItem) o1;
        // String seperator = (String) o2;
        return CartLineItemUtil.getExtraOptionsAsString(cartLineItem, str);
    }

    public static String getConfigOptionsAsString(Object o1, String str) {
        CartLineItem cartLineItem = (CartLineItem) o1;
        // Character seperator = (Character) o2;
        return CartLineItemUtil.getConfigOptionsAsString(cartLineItem, str);
    }

    public static Boolean alreadyPublishedDeal(Object o) {
        RewardPointDao rewardPointDao = ServiceLocatorFactory.getService(RewardPointDao.class);
        Order order = (Order) o;
        boolean alreadyPublishedDeal = true;
        try {
            if (rewardPointDao.findByReferredOrderAndRewardMode(order, rewardPointDao.get(RewardPointMode.class, EnumRewardPointMode.FB_SHARING.getId())) == null) {
                alreadyPublishedDeal = false;
            }
        } catch (Exception e) {

        }
        return alreadyPublishedDeal;
    }

    public static ProductVariant getTopDealVariant(Object o) {
        OrderService orderService = ServiceLocatorFactory.getService(OrderService.class);
        Order order = (Order) o;
        return orderService.getTopDealVariant(order);
    }

    public static Long getReCheckedinUnitsCount(Object o1) {
        AdminProductVariantInventoryDao productVariantInventoryDao = ServiceLocatorFactory.getService(AdminProductVariantInventoryDao.class);
        LineItem lineItem = (LineItem) o1;
        return productVariantInventoryDao.getCheckedInPVIAgainstReturn(lineItem);
    }

    public static Long getDamageUnitsCount(Object o1) {
        ProductVariantDamageInventoryDao productVariantDamageInventoryDao = ServiceLocatorFactory.getService(ProductVariantDamageInventoryDao.class);
        LineItem lineItem = (LineItem) o1;
        return productVariantDamageInventoryDao.getCheckedInPVDIAgainstRTO(lineItem);
    }

    public static Long askedPOQty(Object o1, Object o2) {
        PoLineItemDao poLineItemDao = ServiceLocatorFactory.getService(PoLineItemDao.class);
        Long count = 0L;
        PurchaseOrder purchaseOrder = (PurchaseOrder) o1;
        ProductVariant productVariant = (ProductVariant) o2;
        PoLineItem poLineItem = poLineItemDao.getPoLineItem(purchaseOrder, productVariant);
        if (poLineItem != null) {
            count = poLineItem.getQty();
        }
        return count;
    }

    public static String escapeHtml(String str) {
        return StringEscapeUtils.escapeHtml(str.trim());
    }

    public static String escapeXML(String str) {
        return StringEscapeUtils.escapeXml(str.trim());
    }

    public static String stripHtml(String str) {
        return escapeXML(Jsoup.clean(str, Whitelist.none()));
    }

    public static Double getApplicableOfferPrice(Object o) {
        UserManager userManager = (UserManager) ServiceLocatorFactory.getService("UserManager");
        ProductVariant pv = (ProductVariant) o;
        return userManager.getApplicableOfferPriceForUser(pv);
    }

    public static Double getApplicableOfferPrice(Object o1, Object o2) {
        UserManager userManager = (UserManager) ServiceLocatorFactory.getService("UserManager");
        ProductVariant pv = (ProductVariant) o1;
        Order order = (Order) o2;
        return userManager.getApplicableOfferPriceForUser(pv, order);
    }

    public static MenuNode getMenuNodeForProduct(Product product) {
        MenuHelper menuHelper = (MenuHelper) ServiceLocatorFactory.getService("MenuHelper");
        return menuHelper.getMenoNodeFromProduct(product);
    }

    public static List<Courier> getAvailableCouriers(Object o) {
        ShippingOrder shippingOrder = (ShippingOrder) o;
        PincodeCourierService pincodeCourierService = ServiceLocatorFactory.getService(PincodeCourierService.class);
        PincodeService pincodeService = ServiceLocatorFactory.getService(PincodeService.class);
        Pincode pincode = shippingOrder.getBaseOrder().getAddress().getPincode();
        return pincodeCourierService.getApplicableCouriers(pincode, null, Arrays.asList(shippingOrder.getShipment().getShipmentServiceType()), true);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return !StringUtils.isBlank(str1) && str1.equalsIgnoreCase(str2);
    }

    public static boolean containsIgnoreCase(String str1, String str2) {
        return !StringUtils.isBlank(str1) && str1.toLowerCase().contains(str2.toLowerCase());
    }

    public static Combo getCombo(Object o) {
        String productId = (String) o;
        BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);
        return baseDao.get(Combo.class, productId);
    }

    public static Set<CartLineItem> getProductLineItems(Object o) {
        Order order = (Order) o;
        Set<CartLineItem> productLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        return productLineItems;
    }

    public static List<Sku> getMatchingSku(Object o) {
        SkuDao skuDao = ServiceLocatorFactory.getService(SkuDao.class);
        Sku sku = (Sku) o;
        return skuDao.filterProductVariantsByWarehouse(sku.getProductVariant().getProduct().getProductVariants(), sku.getWarehouse());
    }

//    public static List<Product> getCategoryHeadingProductsSortedByOrder(Long primaryCategoryHeadingId, String productReferrer) {
//        ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
//        return productService.productsSortedByOrder(primaryCategoryHeadingId, productReferrer);
//    }

    public static boolean isComboInStock(Object o) {
        ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
        Combo combo = (Combo) o;
        return productService.isComboInStock(combo);
    }

    public static boolean isCombo(String id) {
        Combo combo = getCombo(id);
        if (combo != null) {
            return true;
        }
        return false;
    }

    public static Map<String, List<String>> getRecommendedProducts(Object o) {
        Product product = (Product) o;
        ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
        return productService.getRecommendedProducts(product);
    }

    public static String getDispatchDaysForOrder(Order order) {
        if (order != null) {
            Long[] dispatchDays = OrderUtil.getDispatchDaysForBO(order);
            long minDays = dispatchDays[0], maxDays = dispatchDays[1];

            if (minDays == OrderUtil.DEFAULT_MIN_DEL_DAYS && maxDays == OrderUtil.DEFAULT_MAX_DEL_DAYS) {
                return DEFAULT_DELIEVERY_DAYS.concat(BUSINESS_DAYS);
            }
            return String.valueOf(minDays).concat("-").concat(String.valueOf(maxDays)).concat(BUSINESS_DAYS);
        } else {
            return DEFAULT_DELIEVERY_DAYS.concat(BUSINESS_DAYS);
        }

    }

    public static boolean isCollectionContainsObject(Collection c, Object o) {
        return c.contains(o);
    }

    public static Double getEngravingPrice(Object o) {
        ProductVariant productVariant = (ProductVariant) o;
        VariantConfig variantConfig = productVariant.getVariantConfig();
        if (variantConfig != null) {
            Set<VariantConfigOption> variantConfigOptions = variantConfig.getVariantConfigOptions();
            for (VariantConfigOption variantConfigOption : variantConfigOptions) {
                if (variantConfigOption.getAdditionalParam().equals(VariantConfigOptionParam.ENGRAVING.param())) {
                    for (VariantConfigValues variantConfigValue : variantConfigOption.getVariantConfigValues()) {
                        return variantConfigValue.getAdditonalPrice();
                    }
                }
            }
        }
        return 0D;
    }

    public static boolean isEngravingProvidedForProduct(Object o) {
        ProductVariant productVariant = (ProductVariant) o;
        VariantConfig variantConfig = productVariant.getVariantConfig();
        if (variantConfig != null) {
            Set<VariantConfigOption> variantConfigOptions = variantConfig.getVariantConfigOptions();
            for (VariantConfigOption variantConfigOption : variantConfigOptions) {
                if (variantConfigOption.getAdditionalParam().equals(VariantConfigOptionParam.ENGRAVING.param())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Long findInventorySoldInGivenNoOfDays(Sku sku, int noOfDays) {
        ReportProductVariantService reportProductVariantService = ServiceLocatorFactory.getService(ReportProductVariantService.class);

        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();

        return reportProductVariantService.findSkuInventorySold(DateUtils.getDateMinusDays(noOfDays), endDate, sku);
    }

    public static String getProductURL(Product product, Long productReferrerId) {
        LinkManager linkManager = (LinkManager) ServiceLocatorFactory.getService("LinkManager");

        return linkManager.getProductURL(product, productReferrerId);
    }

    public static String getTryOnImageURL(ProductVariant productVariant) {
        LinkManager linkManager = (LinkManager) ServiceLocatorFactory.getService("LinkManager");

        return linkManager.getTryOnImageURL(productVariant);
    }

    public static String getCodConverterLink(Order order) {
        LinkManager linkManager = (LinkManager) ServiceLocatorFactory.getService("LinkManager");

        return linkManager.getCodConverterLink(order);
    }

    /*
     * public static boolean isCODAllowed(Order order) { AdminOrderService adminOrderService =
     * ServiceLocatorFactory.getService(AdminOrderService.class); return adminOrderService.isCODAllowed(order); }
     */

    public static Hub getHubForHkdeliveryUser(User user) {
        HubService hubService = ServiceLocatorFactory.getService(HubService.class);
        return hubService.getHubForUser(user);
    }

    public static boolean renderNewCatalogFilter(String child, String secondChild) {
        List<String> categoriesForNewCatalogFilter = Arrays.asList("lenses", "sunglasses", "eyeglasses", "protein", "creatine", "weight-gainer", "dietary-supplements",
                "shop-by-concern", "shop-by-need", "weight-management", "alternative-remedies", "healthy-food");
        boolean renderNewCatalogFilter = (Functions.collectionContains(categoriesForNewCatalogFilter, child) || Functions.collectionContains(categoriesForNewCatalogFilter,
                secondChild));
        return renderNewCatalogFilter;
    }

    public static boolean hideFilterHeads(String secondChild, String thirdChild, String attribute) {
        List<String> thirdChildList = Arrays.asList("sunglasses", "weight-gainer");
        List<String> secondChildList = Arrays.asList("dietary-supplements", "shop-by-concern", "shop-by-need", "weight-management", "alternative-remedies", "healthy-food");
        if (thirdChildList.contains(thirdChild) && attribute.equalsIgnoreCase("size")) {
            return true;
        } else if (secondChild.equalsIgnoreCase("protein")) {
            List<String> attributeListProtein = Arrays.asList("age");
            if (attributeListProtein.contains(attribute.toLowerCase())) {
                return true;
            }
        } else if (secondChildList.contains(secondChild)) {
            List<String> attributesHealthNutrition = Arrays.asList("age", "strength", "size", "type");
            if (attributesHealthNutrition.contains(attribute.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static Long searchProductImages(Product product, ProductVariant productVariant, Long imageTypeId, boolean showVariantImages, Object showHiddenImages) {
        ProductImageService productImageService = ServiceLocatorFactory.getService(ProductImageService.class);
        Boolean showHiddenImagesBoolean = (Boolean) showHiddenImages;
        List<ProductImage> productImages = productImageService.searchProductImages(imageTypeId, product, productVariant, showVariantImages, showHiddenImagesBoolean);
        return productImages != null && !productImages.isEmpty() ? productImages.get(0).getId() : null;
    }

    public static List<Warehouse> getApplicableWarehouses(ProductVariant productVariant, Order order) {
        SkuService skuService = ServiceLocatorFactory.getService(SkuService.class);
        //List<Sku> applicableSkus = skuService.getSKUsForProductVariant(productVariant);
        List<Sku> applicableSkus = skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant, order);
        List<Warehouse> applicableWarehouses = new ArrayList<Warehouse>();
        for (Sku applicableSku : applicableSkus) {
            applicableWarehouses.add(applicableSku.getWarehouse());
        }
        return applicableWarehouses;
    }

    public static boolean showOptionOnUI(String optionType) {
        /*    List<String> allowedOptions = Arrays.asList("BABY WEIGHT", "CODE", "COLOR", "FLAVOR", "FRAGRANCE", "NET WEIGHT",
                  "OFFER", "PRODUCT CODE", "QUANTITY", "SIZE", "TYPE", "WEIGHT", "QTY");*/
        boolean showOptionOnUI = ProductUtil.getVariantValidOptions().contains(optionType.toUpperCase());
        return showOptionOnUI;
    }

    public static String getDisplayNameForHkdeliveryTracking(String status) {
        CourierStatusUpdateHelper courierStatusUpdateHelper = new CourierStatusUpdateHelper();
        return courierStatusUpdateHelper.getHkDeliveryStatusForUser(status);
    }

    public static Long getPoLineItemQty(GrnLineItem grnLineItem) {
        GrnLineItemService grnLineItemService = ServiceLocatorFactory.getService(GrnLineItemService.class);
        return grnLineItemService.getPoLineItemQty(grnLineItem);
    }

    public static Long getGrnLineItemQtyAlreadySet(GoodsReceivedNote goodsReceivedNote, Sku sku) {
        GrnLineItemService grnLineItemService = ServiceLocatorFactory.getService(GrnLineItemService.class);
        return grnLineItemService.getGrnLineItemQtyAlreadySet(goodsReceivedNote, sku);
    }

    public static ProductVariant validTryOnProductVariant(Product product) {
        ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
        return productService.validTryOnProductVariant(product);
    }

    public static List<HeadingProduct> getHeadingProductsSortedByRank(Long headingId) {
        HeadingProductService headingProductService = ServiceLocatorFactory.getService(HeadingProductService.class);
        return headingProductService.getHeadingProductsSortedByRank(headingId);
    }

    public static List<Reason> getReasonsByType(String type) {
        ShippingOrderLifecycleService shippingOrderLifecycleService = ServiceLocatorFactory.getService(ShippingOrderLifecycleService.class);
        return shippingOrderLifecycleService.getReasonByType(type);
    }

    public static Country getCountry(Long countryId) {
        AddressService addressService = ServiceLocatorFactory.getService(AddressService.class);
        return addressService.getCountry(countryId);
    }

    public static String encryptOrderId(Long orderId) {
        Long encryptOrderId_1 = (orderId * 99) + 10;
        String encryptOrderId_2 = encryptOrderId_1.toString();
        String encryptedOrderId = "";
        Random randomGenerator = new Random();
        for (int i = 0; i < encryptOrderId_2.length(); i++) {
            if (i == 0) {
                encryptedOrderId += '@';
            }

            char random = (char) (randomGenerator.nextInt(26) + 'a');
            char value = encryptOrderId_2.charAt(i);
            encryptedOrderId += value;
            encryptedOrderId += random;

        }
        logger.debug("\n encrypted order id is" + encryptedOrderId + "\n");
        return encryptedOrderId;
    }

    public static Long decryptOrderId(String encryptedOrderId) {
        String decryptOrderId = "";
        Long orderId = null;
        for (int i = 1; i < encryptedOrderId.length(); i = i + 2) {
            decryptOrderId += encryptedOrderId.charAt(i);
        }
        logger.debug("\n decrypted order id is" + decryptOrderId + "\n");
        orderId = Long.parseLong(decryptOrderId);
        orderId = orderId - 10;
        orderId = orderId / 99;
        logger.debug("\n decrypted order id is" + orderId + "\n");
        return orderId;
    }

    public static String readIssuerImageIcon(byte[] imageByteArray, String filename) {
        GatewayIssuerMappingService gatewayIssuerMappingService = ServiceLocatorFactory.getService(GatewayIssuerMappingService.class);
        return gatewayIssuerMappingService.getImageOfIssuer(imageByteArray, filename);
    }

    public static Boolean isOrderForDiscretePackaging(ShippingOrder shippingOrder) {
        Category discretePackagingCategory = new Category("discrete-packaging", "Discrete Packaging");
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            if (lineItem.getCartLineItem().getProductVariant().getProduct().getCategories().contains(discretePackagingCategory)) {
                return true;
            }
        }
        return false;
    }

    public static ProductVariantSupplierInfo getPVSupplierInfo(Supplier supplier, ProductVariant productVariant) {
        if (supplier != null && productVariant != null) {
            ProductVariantSupplierInfoService productVariantSupplierInfoService = ServiceLocatorFactory.getService(ProductVariantSupplierInfoService.class);
            return productVariantSupplierInfoService.getOrCreatePVSupplierInfo(productVariant, supplier);
        } else {
            return null;
        }
    }

    public static List<ShippingOrder> getActionAwaitingSO(Order order) {
        List<ShippingOrder> actionAwaitingSO = new ArrayList<ShippingOrder>();
        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
            if (EnumShippingOrderStatus.getStatusIdsForActionQueue().contains(shippingOrder.getOrderStatus().getId())) {
                actionAwaitingSO.add(shippingOrder);
            }
        }
        return actionAwaitingSO;
    }

    public static Warehouse getShippingWarehouse(ShippingOrder shippingOrder) {
        WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        return warehouseService.findShippingWarehouse(shippingOrder);
    }

    public static B2bUserDetails getB2bUserDetails(User user) {
        B2bUserDetailsDao b2bUserDetailsDao = ServiceLocatorFactory.getService(B2bUserDetailsDao.class);
        return b2bUserDetailsDao.getB2bUserDetails(user);
    }

    public static String getStateFromTin(String tin) {
        return StateList.getStateByTin(tin);
    }

    public static double getLoyaltyKarmaPointsForUser(Long userId) {
        LoyaltyProgramService loyaltyProgramService = ServiceLocatorFactory.getService(LoyaltyProgramService.class);
        if (userId == null) {
            return 0.0;
        }
        return loyaltyProgramService.calculateLoyaltyPoints(ServiceLocatorFactory.getService(UserService.class).getUserById(userId));
    }

    public static Badge getBadgeInfoForUser(Long userId) {
        LoyaltyProgramService loyaltyProgramService = ServiceLocatorFactory.getService(LoyaltyProgramService.class);
        if (userId == null) {
            return null;
        }
        return loyaltyProgramService.getUserBadgeInfo(ServiceLocatorFactory.getService(UserService.class).getUserById(userId)).getBadge();
    }

    public static String roundNumberForDisplay(double number) {
        String numberString;
        if ((number * 10) % 10 == 0) {
            numberString = (Math.round(number)) + "";
        } else {
            numberString = (Math.round(number * 10) / 10.0) + "";
        }
        return numberString;
    }

    public static String getAppendedURL(String baseUrl, String parameter, String value) {
        ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
        return productService.getAppendedProductURL(baseUrl, parameter, value);
    }

    public static ActionItem getActionItem(ShippingOrder shippingOrder) {
        ActionItemDao actionItemDao = ServiceLocatorFactory.getService(ActionItemDao.class);
        return actionItemDao.searchActionItem(shippingOrder);
    }

    public static List<ActionTask> listNextActionTasks(ActionItem actionItem) {
        BucketService bucketService = ServiceLocatorFactory.getService(BucketService.class);
        return bucketService.listNextActionTasks(actionItem);
    }

    public static List<ProductInventoryDto> similarProductWithUnbookedInventory(ProductVariant productVariant) {
        ProductVariantNotifyMeEmailService productVariantNotifyMeEmailService = ServiceLocatorFactory.getService(ProductVariantNotifyMeEmailService.class);
        return productVariantNotifyMeEmailService.getProductVariantsOfSimilarProductWithAvailableUnbookedInventory(productVariant);
    }

    public static boolean isBOCancelable(Long orderId) {
        OrderService orderService = ServiceLocatorFactory.getService(OrderService.class);
        return orderService.isBOCancelable(orderId);
    }

    public static Long countLineItemQty(ShippingOrder shippingOrder) {
        long qty = 0;
        if (shippingOrder.getLineItems() != null && shippingOrder.getLineItems().size() > 0)
            for (LineItem lineItem : shippingOrder.getLineItems()) {
                qty = qty + lineItem.getQty();
            }
        return qty;
    }

    public static Product showFreebie(Product product) {
        if (!(product.isOutOfStock() || product.isDeleted() || product.isHidden())) {
            for (ProductVariant productVariant : product.getProductVariants()) {
                if (!(productVariant.isOutOfStock() || productVariant.isDeleted())) {
                    ProductVariant freeProductVariant = productVariant.getFreeProductVariant();
                    if (freeProductVariant != null) {
//                          if(!(freeProductVariant.isDeleted() || freeProductVariant.isOutOfStock())){
                        if (!(freeProductVariant.isOutOfStock())) {
                            return freeProductVariant.getProduct();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Product showFreebieForVariant(ProductVariant productVariant) {
        Product product = productVariant.getProduct();
        if (!(product.isHidden() || product.isDeleted() || product.isOutOfStock())) {
            if (!(productVariant.isOutOfStock() || productVariant.isDeleted())) {
                ProductVariant freeProductVariant = productVariant.getFreeProductVariant();
                if (freeProductVariant != null) {
//                    if(!(freeProductVariant.isDeleted() || freeProductVariant.isOutOfStock())){
                    if (!(freeProductVariant.isOutOfStock())) {
                        return freeProductVariant.getProduct();
                    }
                }
            }
        }
        return null;
    }

    public static StoreVariantBasicApiResponse getStoreVariantBasicDetails(String oldVariantId) {
        if (AppConstants.isHybridRelease) {
            StoreVariantService storeVariantService = ServiceLocatorFactory.getService(StoreVariantService.class);
            return storeVariantService.getStoreVariantBasicDetails(oldVariantId);
        }

        throw new UnsupportedOperationException();
    }

}
