package com.hk.pact.dao;

import java.util.List;

import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.CartLineItemType;
import com.hk.domain.core.City;
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

public interface MasterDataDao 
{

    public List<PaymentStatus> getPaymentStatusList();

    public List<PaymentMode> getPaymentModeList();

    public List<OrderStatus> getOrderStatusList();

    public List<Courier> getCourierList();

    public List<Courier> getGroundShippedCourierList();

    public List<ReconciliationStatus> getReconciliationStatus();

    public List<CartLineItemType> getLineItemTypeList();

    public List<CancellationType> getCancellationTypeList();

    public List<RewardPointStatus> getRewardPointStatusList();

    public List<Tax> getTaxList();

    public List<Manufacturer> getManufacturerList();

    public List<TicketType> getTicketTypeList();

    public List<TicketStatus> getTicketStatusList();

    public List<User> getTicketAdminList();

    public List<Category> getTopLevelCategoryList();

    public List<Category> getMarketExpenseCategoriesList();

    public List<AffiliateCategory> getallAffiliateCategories();

    public List<PaymentMode> getPaymentModes();

    public List<BoxSize> getBoxSizes();

    public List<RewardPointMode> getRewardPointModes();

    public List<ProductVariantPaymentType> getPaymentTypes();

    public List<ProductVariantServiceType> getServiceTypes();

    public List<PurchaseOrderStatus> getPurchaseOrderStatusList();

    public List<GrnStatus> getGrnStatusList();

    public List<User> getApproverList();

    public List<User> getCreatorList();

    public List<DebitNoteStatus> getDebitNoteStatusList();

    public List<PurchaseInvoiceStatus> getPurchaseInvoiceStatusList();

    public List<Surcharge> getSurchargeList();

    public List<ReconciliationType> getReconciliationTypeList();

    public List<ShippingOrderStatus> getSOStatusForShipmentDetailsList();

    public List<PurchaseFormType> getPurchaseInvoiceFormTypes();

    public List<ReviewStatus> getReviewStatusList();

    public List<String> getCourierListForDBUpdation();

    public List<RegionType> getRegionTypeList();

    public List<Store> getStoreList();

    public List<State> getStateList();

    public List<City> getCityList();

    public List<ShippingOrderStatus> getSOStatusForReconcilation();

    public List<Hub> getHubList();

    public List<User>  getHKDeliveryAgentList();

    public List<RunsheetStatus> getRunsheetStatusList();

    public List<SubscriptionStatus> getSubscriptionStatusList();

    public List<User> getAgentsWithOpenRunsheet();

    public List<ConsignmentStatus> getConsignmentStatusList();

	public List<PurchaseOrderStatus> getPurchaseOrderStatusListForNonApprover();

}
