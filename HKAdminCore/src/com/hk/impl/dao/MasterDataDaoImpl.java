package com.hk.impl.dao;

import java.util.*;

import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.courier.DispatchLotService;
<<<<<<< HEAD
import com.hk.constants.analytics.EnumReason;
=======
import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.category.CategoryConstants;
>>>>>>> pre-release
import com.hk.constants.core.EnumCancellationType;
import com.hk.constants.core.EnumUserCodCalling;
import com.hk.constants.courier.*;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.pos.DiscountConstants;
import com.hk.constants.reversePickup.EnumReversePickupStatus;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shipment.EnumPacker;
import com.hk.constants.shipment.EnumPicker;
import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.domain.analytics.Reason;
import com.hk.domain.courier.*;
import com.hk.domain.hkDelivery.ConsignmentLifecycleStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.review.Mail;
import com.hk.pact.service.review.MailService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.cache.RoleCache;
import com.hk.cache.vo.RoleVO;
import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.core.EnumRole;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.constants.inventory.EnumCycleCountStatus;
import com.hk.constants.shippingOrder.EnumReplacementOrderReason;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.*;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.inventory.GrnStatus;
import com.hk.domain.inventory.creditNote.CreditNoteStatus;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.review.ReviewStatus;
import com.hk.domain.store.Store;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.MasterDataDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.core.CityService;
import com.hk.pact.service.core.StateService;
import com.hk.pact.service.marketing.MarketingService;
import com.hk.pact.service.store.StoreService;

@Repository
public class MasterDataDaoImpl implements MasterDataDao {

    @Autowired
    private BaseDao baseDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MarketingService marketingService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private CourierDao courierDao;
    @Autowired
    private CityService cityService;
    @Autowired
    private StateService stateService;
    @Autowired
    private HubService hubService;
    @Autowired
    private RunSheetService runsheetService;
    @Autowired
    private CourierGroupService courierGroupService;
    @Autowired
    private CourierService courierService;
    @Autowired
    private DispatchLotService dispatchLotService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private ConsignmentService consignmentService;
    @Autowired
    private MailService mailService;
    @Autowired
    ShippingOrderService shippingOrderService;

    public List<PaymentStatus> getPaymentStatusList() {
        return getBaseDao().getAll(PaymentStatus.class);
    }

    public List<PaymentMode> getPaymentModeList() {
        return getBaseDao().getAll(PaymentMode.class);
    }

    public List<OrderStatus> getOrderStatusList() {
        return getBaseDao().getAll(OrderStatus.class);
    }

    public List<ReconciliationStatus> getReconciliationStatus() {
        return Arrays.asList(EnumReconciliationStatus.DONE.asReconciliationStatus(), EnumReconciliationStatus.PENDING.asReconciliationStatus());
    }

    public List<CartLineItemType> getLineItemTypeList() {
        return getBaseDao().getAll(CartLineItemType.class);
    }

    public List<CancellationType> getCancellationTypeList() {
        return EnumCancellationType.getValidCancellationReasons();
    }

    public List<RewardPointStatus> getRewardPointStatusList() {
        return getBaseDao().getAll(RewardPointStatus.class);
    }

    public List<Tax> getTaxList() {
        return getBaseDao().getAll(Tax.class);
    }

    public List<Manufacturer> getManufacturerList() {
        return getBaseDao().getAll(Manufacturer.class);
    }

    public List<TicketType> getTicketTypeList() {
        return getBaseDao().getAll(TicketType.class);
    }

    public List<TicketStatus> getTicketStatusList() {
        return getBaseDao().getAll(TicketStatus.class);
    }

    public List<User> getTicketAdminList() {
        RoleVO ticketAdminRole = RoleCache.getInstance().getRoleByName(EnumRole.TICKETADMIN);
        // return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.TICKETADMIN));
        return getUserService().findByRole(ticketAdminRole.getRole());
    }

    public List<Category> getTopLevelCategoryList() {
        return getCategoryService().getPrimaryCategories();
    }

    public List<Category> getMarketExpenseCategoriesList() {
        return getMarketingService().marketExpenseCategoriesList();
    }

    public List<AffiliateCategory> getallAffiliateCategories() {
        return getBaseDao().getAll(AffiliateCategory.class);
    }

    public List<PaymentMode> getPaymentModes() {
        return getBaseDao().getAll(PaymentMode.class);
    }

    public List<BoxSize> getBoxSizes() {
        return getBaseDao().getAll(BoxSize.class);
    }

    public List<RewardPointMode> getRewardPointModes() {
        return getBaseDao().getAll(RewardPointMode.class);
    }

    public List<ProductVariantPaymentType> getPaymentTypes() {
        return Arrays.asList(EnumProductVariantPaymentType.Default.asProductVariantPaymentType(), EnumProductVariantPaymentType.Prepaid.asProductVariantPaymentType(),
                EnumProductVariantPaymentType.Postpaid.asProductVariantPaymentType());
    }

    public List<ProductVariantServiceType> getServiceTypes() {
        return getBaseDao().getAll(ProductVariantServiceType.class);
    }

    public List<PurchaseOrderStatus> getPurchaseOrderStatusList() {
        return getBaseDao().getAll(PurchaseOrderStatus.class);
    }

    public List<GrnStatus> getGrnStatusList() {
        return getBaseDao().getAll(GrnStatus.class);
    }

    public List<User> getApproverList() {
        Role poApproverRole = RoleCache.getInstance().getRoleByName(EnumRole.PO_APPROVER).getRole();
        // return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.PO_APPROVER));
        return getUserService().findByRole(poApproverRole);
    }

    public List<User> getCreatorList() {
        Role categoryManRole = RoleCache.getInstance().getRoleByName(EnumRole.CATEGORY_MANAGER).getRole();
        // return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.CATEGORY_MANAGER));
        return getUserService().findByRole(categoryManRole);
    }

    public List<DebitNoteStatus> getDebitNoteStatusList() {
        return getBaseDao().getAll(DebitNoteStatus.class);
    }

    public List<CreditNoteStatus> getCreditNoteStatusList() {
        return getBaseDao().getAll(CreditNoteStatus.class);
    }

    public List<PurchaseInvoiceStatus> getPurchaseInvoiceStatusList() {
        return getBaseDao().getAll(PurchaseInvoiceStatus.class);
    }

    public List<Surcharge> getSurchargeList() {
        return getBaseDao().getAll(Surcharge.class);
    }

    public List<ReconciliationType> getReconciliationTypeList() {
        return EnumReconciliationType.getSubtractReconciliationType();
    }

    public List<ReconciliationType> getDebitNoteReconciliationType() {
        return EnumReconciliationType.getDebitNoteReconciliationType();
    }

    public List<Mail> getAllMailType() {
        return mailService.getAllMailType();
    }

    public List<EmailType> getEmailTypeList() {
        return getBaseDao().getAll(EmailType.class);
    }

    public List<String> getCourierListForAutoDeliveryMarking() {
        List<String> courierListForDeliveryMarking = new ArrayList<String>();
        courierListForDeliveryMarking.add(CourierConstants.AFL);
        courierListForDeliveryMarking.add(CourierConstants.BLUEDART);
        //courierListForDeliveryMarking.add(CourierConstants.CHHOTU);
        courierListForDeliveryMarking.add(CourierConstants.DELHIVERY);
        courierListForDeliveryMarking.add(CourierConstants.DTDC);
        courierListForDeliveryMarking.add(CourierConstants.QUANTIUM);
        courierListForDeliveryMarking.add(CourierConstants.INDIAONTIME);
        courierListForDeliveryMarking.add(CourierConstants.FEDEX);
        return courierListForDeliveryMarking;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public MarketingService getMarketingService() {
        return marketingService;
    }

    public void setMarketingService(MarketingService marketingService) {
        this.marketingService = marketingService;
    }

    public List<PurchaseFormType> getPurchaseFormTypeList() {
        return getBaseDao().getAll(PurchaseFormType.class);
    }

    public List<ShippingOrderStatus> getSOStatusForShipmentDetailsList() {
        return EnumShippingOrderStatus.getStatusForChangingShipmentDetails();
    }

    public List<PurchaseFormType> getPurchaseInvoiceFormTypes() {
        return getBaseDao().getAll(PurchaseFormType.class);
    }

    public List<ReviewStatus> getReviewStatusList() {
        return getBaseDao().getAll(ReviewStatus.class);
    }

    public List<RegionType> getRegionTypeList() {
        return courierDao.getAll(RegionType.class);
    }

    public List<Store> getStoreList() {
        List<Store> storeList = getStoreService().getAllStores();
        storeList.remove(getStoreService().getDefaultStore());
        return storeList;
    }

    public List<SubscriptionStatus> getSubscriptionStatusList() {
        return getBaseDao().getAll(SubscriptionStatus.class);
    }

    public List<State> getStateList() {
        List<State> stateList = stateService.getAllStates();
        Collections.sort(stateList);
        return stateList;

    }

    public List<City> getCityList() {
        List<City> cityList = cityService.getAllCity();
        Collections.sort(cityList, new City());
        return cityList;
    }

    public List<Courier> getCourierList() {
        List<Courier> courierList = courierService.getAllCouriers();
        Courier migrateCourier = EnumCourier.MIGRATE.asCourier();
        courierList.remove(migrateCourier);
        return courierList;
    }

    public List<Courier> getAllActiveCourier() {
        return courierService.getAllActiveCourier();
    }

    public List<ShippingOrderStatus> getSOStatusForReconcilation() {
        return EnumShippingOrderStatus.getStatusForReconcilationReport();
    }

    public List<Hub> getHubList() {
        return hubService.getAllHubs();
    }

    public List<User> getHKDeliveryAgentList() {
        // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        User loggedOnUser = getUserService().getLoggedInUser();
        Hub currentHub = hubService.getHubForUser(loggedOnUser);
        if (currentHub != null) {
            return hubService.getAgentsForHub(currentHub);
        }
        RoleVO hkDelGuyRole = RoleCache.getInstance().getRoleByName(EnumRole.HK_DELIVERY_GUY);
        // return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.HK_DELIVERY_GUY));
        return getUserService().findByRole(hkDelGuyRole.getRole());
    }

    public List<RunsheetStatus> getRunsheetStatusList() {
        return getBaseDao().getAll(RunsheetStatus.class);
    }

    public List<User> getAgentsWithOpenRunsheet() {
        return runsheetService.getAgentList(getBaseDao().get(RunsheetStatus.class, EnumRunsheetStatus.Open.getId()));
    }

    public List<ConsignmentStatus> getConsignmentStatusList() {
        return getBaseDao().getAll(ConsignmentStatus.class);
    }

    public List<CourierGroup> getCourierGroupList() {
        return courierGroupService.getAllCourierGroup();
    }

    public List<Courier> getAvailableCouriers() {
        return courierService.getCouriers(null, null, true, EnumCourierOperations.HK_SHIPPING.getId());
    }

    public List<PurchaseOrderStatus> getPurchaseOrderStatusListForNonApprover() {
        return EnumPurchaseOrderStatus.getStatusForNonApprover();

    }

    public List<ReplacementOrderReason> getReplacementOrderReasonForReplacement() {
        List<Long> replacementOrderReasonIds = EnumReplacementOrderReason.getReasonForReplacementOrder();
        List<ReplacementOrderReason> replacementOrderReasonList = new ArrayList<ReplacementOrderReason>();
        ReplacementOrderReason replacementOrderReason;
        for (Long replacementOrderReasonId : replacementOrderReasonIds) {
            replacementOrderReason = getBaseDao().get(ReplacementOrderReason.class, replacementOrderReasonId);
            if (replacementOrderReason != null) {
                replacementOrderReasonList.add(replacementOrderReason);
            }
        }
        return replacementOrderReasonList;
    }

    public List<ReplacementOrderReason> getReplacementOrderReasonForRto() {
        List<Long> replacementOrderReasonIds = EnumReplacementOrderReason.getReasonForReplacementForRTO();
        List<ReplacementOrderReason> replacementOrderReasonList = new ArrayList<ReplacementOrderReason>();
        ReplacementOrderReason replacementOrderReason;
        for (Long replacementOrderReasonId : replacementOrderReasonIds) {
            replacementOrderReason = getBaseDao().get(ReplacementOrderReason.class, replacementOrderReasonId);
            if (replacementOrderReason != null) {
                replacementOrderReasonList.add(replacementOrderReason);
            }
        }
        return replacementOrderReasonList;
    }

    public List<Country> getAllCountry() {
        return getBaseDao().getAll(Country.class);
    }

    public List<Zone> getAllZones() {
        return getBaseDao().getAll(Zone.class);
    }

    public List<String> getCustomerOnHoldReasonsForHkDelivery() {
        return consignmentService.getCustomerOnHoldReasonsForHkDelivery();
    }

    public List<DispatchLotStatus> getDispatchLotStatusList() {
        return getBaseDao().getAll(DispatchLotStatus.class);
    }

    public List<String> getSourceAndDestinationListForDispatchLot() {
        return dispatchLotService.getSourceAndDestinationListForDispatchLot();
    }

    public List<String> getShipmentStatusForDispatchLot() {
        return dispatchLotService.getShipmentStatusForDispatchLot();
    }

    public List<Warehouse> getServiceableWarehouses() {
        return warehouseService.getServiceableWarehouses();
    }

    public List<AwbStatus> getAllAwbStatus() {
        return EnumAwbStatus.getAllStatusExceptUsed();
    }

    public List<Courier> getListOfVendorCouriers() {
        return courierService.getCouriers(null, null, null, EnumCourierOperations.VENDOR_DROP_SHIP.getId());
    }

    public List<Courier> getCouriersForDispatchLot() {
        return courierService.getCouriers(null, null, null, EnumCourierOperations.DISPATCH_LOT.getId());
    }

    public List<Warehouse> getAllWarehouse() {
        //return warehouseService.getAllWarehouses();
        return warehouseService.getAllActiveWarehouses();
    }

    public List<EnumShipmentServiceType> getAllEnumShipmentServiceTypes() {
        return EnumShipmentServiceType.getAllShipmentServiceType();
    }

    public List<EnumPicker> getAllPicker() {
        return EnumPicker.getAll();
    }

    public List<EnumPacker> getAllPacker() {
        return EnumPacker.getAll();
    }

    public List<EnumBoxSize> getAllBoxSize() {
        return EnumBoxSize.getAllEnumBoxSize();
    }

    public List<ReconciliationType> getAddReconciliationTypeList() {
        return EnumReconciliationType.getAddReconciliationType();
    }

    public List<PaymentMode> getPaymentModeForStore() {
        return Arrays.asList(EnumPaymentMode.COUNTER_CASH.asPaymenMode(), EnumPaymentMode.OFFLINE_CARD_PAYMENT.asPaymenMode());
    }

    public List<EnumCourierOperations> getAllCourierOperations() {
        return EnumCourierOperations.getAllCourierOperations();
    }

    public List<EnumCycleCountStatus> getAllCycleCountStatus() {
        return EnumCycleCountStatus.getAllList();
    }

    public List<Long> getDiscountsForPOS() {
        return Arrays.asList(DiscountConstants.FIFTY_RUPEES_DISCOUNT, DiscountConstants.ONE_HUNDRED_RUPEES_DISCOUNT, DiscountConstants.ONE_HUNDRED_FIFTY_RUPEES_DISCOUNT);
    }

    public List<ReconciliationType> getProductAuditedReconVoucherType() {
        return Arrays.asList(EnumReconciliationType.ProductVariantAudited.asReconciliationType());
    }

    public List<ConsignmentLifecycleStatus> getConsignmentLifecycleStatusList() {
        return getBaseDao().getAll(ConsignmentLifecycleStatus.class);
    }

    public List<EnumUserCodCalling> getUserCodCallStatus() {
        return Arrays.asList(EnumUserCodCalling.PENDING_WITH_KNOWLARITY, EnumUserCodCalling.THIRD_PARTY_FAILED, EnumUserCodCalling.PENDING_WITH_EFFORT_BPO, EnumUserCodCalling.PENDING_WITH_HEALTHKART);
    }


    public List<Reason> getCustomerReasonForReversePickup() {
        List<Long> reasonsIds = Arrays.asList(EnumReason.ProductDamaged.getId(), EnumReason.ProductExpired.getId(), EnumReason.WrongColor.getId(), EnumReason.WrongSize.getId());
        return shippingOrderService.getReasonForReversePickup(reasonsIds);
    }

    public List<Reason> getWarehouseReceivedCondition() {
        List<Long> reasonsIds = Arrays.asList(EnumReason.Good.getId(), EnumReason.Damaged.getId(), EnumReason.Non_Functional.getId(), EnumReason.Near_Expiry.getId(), EnumReason.Expired.getId());
        return shippingOrderService.getReasonForReversePickup(reasonsIds);
    }

    public List<Courier> getCouriersForReversePickup() {
        return Arrays.asList(EnumCourier.FedEx.asCourier());
    }

    public List<EnumReversePickupStatus> getAllReversePickUpStatus() {
        return Arrays.asList(EnumReversePickupStatus.RPU_Initiated, EnumReversePickupStatus.RPU_Picked, EnumReversePickupStatus.RPU_Received, EnumReversePickupStatus.RPU_QC_Checked_In);
    }


		public List<Category> getCategoriesForPOS() {
				List<Category> posCategoryList = new ArrayList<Category>();
				String categoryNames = CategoryConstants.HEALTH_DEVICES + "," + CategoryConstants.SPORTS_NUTRITION + "," + CategoryConstants.HEALTH_NUTRITION + "," + CategoryConstants.SPORTS;

				Set<Category> categorySet = new HashSet<Category>();

				for (String categoryName : categoryNames.split(",")) {
					categorySet.add(CategoryCache.getInstance().getCategoryByName(categoryName).getCategory());
				}

				for (Category category : categorySet) {
					posCategoryList.add(category);
				}
				return posCategoryList;
		}
}
