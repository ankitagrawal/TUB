package com.hk.impl.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.core.EnumRole;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.CartLineItemType;
import com.hk.domain.core.City;
import com.hk.domain.core.EmailType;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.core.ProductVariantServiceType;
import com.hk.domain.core.PurchaseFormType;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.core.State;
import com.hk.domain.core.Surcharge;
import com.hk.domain.core.Tax;
import com.hk.domain.courier.BoxSize;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.RegionType;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.inventory.GrnStatus;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.review.ReviewStatus;
import com.hk.domain.store.Store;
import com.hk.domain.subscription.SubscriptionStatus;
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
    private BaseDao          baseDao;
    @Autowired
    private UserService      userService;
    @Autowired
    private CategoryService  categoryService;
    @Autowired
    private RoleService      roleService;
    @Autowired
    private MarketingService marketingService;
    @Autowired
    private StoreService     storeService;
    @Autowired
    private CourierDao       courierDao;
    @Autowired
    private CityService      cityService;
    @Autowired
    private StateService     stateService;
    @Autowired
    private HubService       hubService;
    @Autowired
    private RunSheetService  runsheetService;
@Autowired
private CourierGroupService courierGroupService;

	@Autowired
	private CourierService courierService;

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
        return getBaseDao().getAll(CancellationType.class);
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
        return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.TICKETADMIN));
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
        return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.PO_APPROVER));
    }

    public List<User> getCreatorList() {
        return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.CATEGORY_MANAGER));
    }

    public List<DebitNoteStatus> getDebitNoteStatusList() {
        return getBaseDao().getAll(DebitNoteStatus.class);
    }

    public List<PurchaseInvoiceStatus> getPurchaseInvoiceStatusList() {
        return getBaseDao().getAll(PurchaseInvoiceStatus.class);
    }

    public List<Surcharge> getSurchargeList() {
        return getBaseDao().getAll(Surcharge.class);
    }

    public List<ReconciliationType> getReconciliationTypeList() {
        return getBaseDao().getAll(ReconciliationType.class);
    }

    public List<EmailType> getEmailTypeList() {
        return getBaseDao().getAll(EmailType.class);
    }

    public List<String> getCourierListForDBUpdation() {
        List<String> courierListForDBUpdation = new ArrayList<String>();
        courierListForDBUpdation.add(CourierConstants.AFL);
        courierListForDBUpdation.add(CourierConstants.BLUEDART);
        courierListForDBUpdation.add(CourierConstants.CHHOTU);
        courierListForDBUpdation.add(CourierConstants.DELHIVERY);
        courierListForDBUpdation.add(CourierConstants.DTDC);
        return courierListForDBUpdation;
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

    public List<SubscriptionStatus> getSubscriptionStatusList(){
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
        return courierService.getAllCouriers();
    }

//     public List<Courier> getGroundShippedCourierList() {
//        return courierDao.getCourierByIds(EnumCourier.getCourierIDs(EnumCourier.getCurrentlyApplicableGroundShippedCouriers()));
//    }

    public List<ShippingOrderStatus> getSOStatusForReconcilation(){
        return EnumShippingOrderStatus.getStatusForReconcilationReport();
    }

    public List<Hub> getHubList() {
        return hubService.getAllHubs();
    }

    public List<User> getHKDeliveryAgentList(){
        User loggedOnUser = getUserService().getLoggedInUser();
        Hub currentHub =  hubService.getHubForUser(loggedOnUser);
        if(currentHub != null){
            return hubService.getAgentsForHub(currentHub);
        }
        return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.HK_DELIVERY_GUY));
    }

    public List<RunsheetStatus> getRunsheetStatusList(){
        return getBaseDao().getAll(RunsheetStatus.class);
    }

    public List<User> getAgentsWithOpenRunsheet() {                     
        return runsheetService.getAgentList(getBaseDao().get(RunsheetStatus.class,EnumRunsheetStatus.Open.getId()));
    }

    public List<ConsignmentStatus> getConsignmentStatusList(){
        return getBaseDao().getAll(ConsignmentStatus.class);
    }

	public List<CourierGroup>  getCourierGroupList(){
	return courierGroupService.getAllCourierGroup();	
	}

	public List<Courier> getDisableCourier(){
		return courierService.getDisableCourier();
	}

	public List<Courier> getAvailableCouriers(){
		return courierService.getAvailableCouriers();
	}
}
