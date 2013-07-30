package com.hk.pact.dao;

import java.util.List;

import com.hk.constants.core.EnumUserCodCalling;
import com.hk.constants.courier.EnumCourierOperations;
import com.hk.constants.reversePickup.EnumReversePickupStatus;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shipment.EnumPacker;
import com.hk.constants.shipment.EnumPicker;
import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.constants.inventory.EnumCycleCountStatus;
import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.analytics.Reason;
import com.hk.domain.courier.*;
import com.hk.domain.hkDelivery.ConsignmentLifecycleStatus;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.*;
import com.hk.domain.inventory.GrnStatus;
import com.hk.domain.inventory.creditNote.CreditNoteStatus;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.review.Mail;
import com.hk.domain.review.ReviewStatus;
import com.hk.domain.store.Store;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

public interface MasterDataDao {
    public static final int USER_COMMENT_TYPE_PACKING_BASE_ORDER = 1;
    public static final int USER_COMMENT_TYPE_DELIVERY_BASE_ORDER = 2;
    public static final int USER_COMMENT_TYPE_OTHERS_BASE_ORDER = 3;

    public List<PaymentStatus> getPaymentStatusList();

    public List<PaymentMode> getPaymentModeList();

    public List<OrderStatus> getOrderStatusList();

    public List<Courier> getCourierList();

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

    public List<CreditNoteStatus> getCreditNoteStatusList();

    public List<PurchaseInvoiceStatus> getPurchaseInvoiceStatusList();

    public List<Surcharge> getSurchargeList();

    public List<ReconciliationType> getReconciliationTypeList();

    public List<ShippingOrderStatus> getSOStatusForShipmentDetailsList();

    public List<PurchaseFormType> getPurchaseInvoiceFormTypes();

    public List<ReviewStatus> getReviewStatusList();

    public List<String> getCourierListForAutoDeliveryMarking();

    public List<RegionType> getRegionTypeList();

    public List<Store> getStoreList();

    public List<State> getStateList();

    public List<City> getCityList();

    public List<ShippingOrderStatus> getSOStatusForReconcilation();

    public List<Hub> getHubList();

    public List<Mail> getAllMailType();

    public List<User> getHKDeliveryAgentList();

    public List<RunsheetStatus> getRunsheetStatusList();

    public List<SubscriptionStatus> getSubscriptionStatusList();

    public List<User> getAgentsWithOpenRunsheet();

    public List<ConsignmentStatus> getConsignmentStatusList();

    public List<CourierGroup> getCourierGroupList();

    public List<Courier> getAvailableCouriers();

    public List<PurchaseOrderStatus> getPurchaseOrderStatusListForNonApprover();

    public List<ReplacementOrderReason> getReplacementOrderReasonForReplacement();

    public List<ReplacementOrderReason> getReplacementOrderReasonForRto();

    public List<Country> getAllCountry();

    public List<Zone> getAllZones();

    public List<String> getCustomerOnHoldReasonsForHkDelivery();

    public List<DispatchLotStatus> getDispatchLotStatusList();

    public List<String> getSourceAndDestinationListForDispatchLot();

    public List<String> getShipmentStatusForDispatchLot();

    public List<Warehouse> getServiceableWarehouses();

    public List<AwbStatus> getAllAwbStatus();

    public List<ReconciliationType> getAddReconciliationTypeList();

    public List<Courier> getListOfVendorCouriers();

    public List<Warehouse> getAllWarehouse();

    public List<EnumShipmentServiceType> getAllEnumShipmentServiceTypes();

    public List<EnumPicker> getAllPicker();

    public List<EnumPacker> getAllPacker();

    public List<EnumBoxSize> getAllBoxSize();

    public List<Courier> getCouriersForDispatchLot();

    public List<PaymentMode> getPaymentModeForStore();

    public List<EnumCourierOperations> getAllCourierOperations();

    public List<EnumCycleCountStatus> getAllCycleCountStatus();

    public List<Courier> getAllActiveCourier();

    public List<Long> getDiscountsForPOS();

    public List<ReconciliationType> getProductAuditedReconVoucherType();

    public List<ConsignmentLifecycleStatus> getConsignmentLifecycleStatusList();

    public List<EnumUserCodCalling> getUserCodCallStatus();
    
    public List<ReconciliationType> getDebitNoteReconciliationType();

    public List<Reason> getCustomerReasonForReversePickup();

    public List<Reason> getWarehouseReceivedCondition();

    public List<Courier> getCouriersForReversePickup();

    public List<EnumReversePickupStatus> getAllReversePickUpStatus();

}
