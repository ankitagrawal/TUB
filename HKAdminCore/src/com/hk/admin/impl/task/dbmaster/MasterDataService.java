package com.hk.admin.impl.task.dbmaster;

import com.hk.db.seed.catalog.ProductVariantPaymentTypeSeedData;
import com.hk.db.seed.catalog.ProductVariantServiceTypeSeedData;
import com.hk.db.seed.core.*;
import com.hk.db.seed.courier.*;
import com.hk.db.seed.email.EmailTypeSeedData;
import com.hk.db.seed.inventory.*;
import com.hk.db.seed.marketing.AdNetworksSeedData;
import com.hk.db.seed.marketing.GoogleBannedWordSeedData;
import com.hk.db.seed.marketing.ProductReferrerSeedData;
import com.hk.db.seed.order.*;
import com.hk.db.seed.payment.PaymentModeSeedData;
import com.hk.db.seed.payment.PaymentStatusSeedData;
import com.hk.db.seed.reward.ReviewStatusSeedData;
import com.hk.db.seed.reward.RewardPointModeSeedData;
import com.hk.db.seed.reward.RewardPointStatusSeedData;
import com.hk.db.seed.reward.RewardPointTxnTypeSeedData;
import com.hk.db.seed.sku.SkuItemStatusSeedData;
import com.hk.db.seed.subscription.SubscriptionLifeCycleActivitySeedData;
import com.hk.db.seed.subscription.SubscriptionOrderStatusSeedData;
import com.hk.db.seed.subscription.SubscriptionStatusSeedData;
import com.hk.db.seed.ticket.TicketStatusSeedData;
import com.hk.db.seed.ticket.TicketTypeSeedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: Kani Date: Jul 23, 2009
 */
@Component
public class MasterDataService {
	private static Logger logger = LoggerFactory.getLogger(MasterDataService.class);
	//    @Autowired
//    RoleSeedData                           roleSeedData;
//    @Autowired
//    PermissionSeedData                     permissionSeedData;
//    @Autowired
//    RoleHasPermissionSeedData              roleHasPermissionSeedData;
//    @Autowired
//    CourierSeedData                        courierSeedData;
	/* CartLineItemTypeSeedData lineItemStatusSeedData; */
	@Autowired
	CartLineItemTypeSeedData cartLineItemTypeSeedData;
	@Autowired
	OrderStatusSeedData orderStatusSeedData;
	@Autowired
	ShippingOrderStatusSeedData shippingOrderStatusSeedData;
	@Autowired
	PaymentModeSeedData paymentModeSeedData;
	@Autowired
	PaymentStatusSeedData paymentStatusSeedData;
	@Autowired
	TaxSeedData taxSeedData;
	@Autowired
	RewardPointStatusSeedData rewardPointStatusSeedData;
	@Autowired
	RewardPointTxnTypeSeedData rewardPointTxnTypeSeedData;
	@Autowired
	EmailTypeSeedData emailTypeSeedData;
	@Autowired
	TicketTypeSeedData ticketTypeSeedData;
	@Autowired
	TicketStatusSeedData ticketStatusSeedData;
	@Autowired
	ReconciliationStatusSeedData reconciliationStatusSeedData;
	@Autowired
	CancellationTypeSeedData cancellationTypeSeedData;
	@Autowired
	AffiliateTxnTypeSeedData affiliateTxnTypeSeedData;
	@Autowired
	OrderLifecycleActivitySeedData orderLifecycleActivitySeedData;
	@Autowired
	ShippingOrderLifecycleActivitySeedData shippingOrderLifecycleActivitySeedData;
	@Autowired
	BoxSizeSeedData boxSizeSeedData;
	@Autowired
	PurchaseOrderStatusSeedData poStatusSeedData;
	@Autowired
	RewardPointModeSeedData rewardPointModeSeedData;
	@Autowired
	ProductVariantServiceTypeSeedData productVariantServiceTypeSeedData;
	@Autowired
	ProductVariantPaymentTypeSeedData productVariantPaymentTypeSeedData;
	@Autowired
	GrnStatusSeedData grnStatusSeedData;
	@Autowired
	InvTxnTypeSeedData invTxnTypeSeedData;
	@Autowired
	GoogleBannedWordSeedData googleBannedWordSeedData;
	@Autowired
	DebitNoteStatusSeedData debitNoteStatusSeedData;
  @Autowired
	CreditNoteStatusSeedData creditNoteStatusSeedData;
	@Autowired
	PurchaseInvoiceStatusSeedData purchaseInvoiceStatusSeedData;
	@Autowired
	SurchargeSeedData surchargeSeedData;
	@Autowired
	AdNetworksSeedData adNetworksSeedData;
	@Autowired
	ReconciliationTypeSeedData reconciliationTypeSeedData;
	@Autowired
	ReviewStatusSeedData reviewStatusSeedData;
	@Autowired
	ProductReferrerSeedData productReferrerSeedData;
	//    @Autowired
//    CourierGroupSeedData                   courierGroupSeedData;
//    @Autowired
//    CourierGroupHasCourierSeedData        courierGroupHasCourierSeedData;
	@Autowired
	PurchaseFormTypeSeedData purchaseFormTypeSeedData;
	@Autowired
	PrimaryReferrerForOrderSeedData primaryReferrerForOrderSeedData;
	@Autowired
	SecondaryReferrerForOrderSeedData secondaryReferrerForOrderSeedData;
	@Autowired
	SubscriptionStatusSeedData subscriptionStatusSeedData;
	@Autowired
	SubscriptionOrderStatusSeedData subscriptionOrderStatusSeedData;
	@Autowired
	SubscriptionLifeCycleActivitySeedData subscriptionLifeCycleActivitySeedData;
	@Autowired
	StateSeedData stateSeedData;
	@Autowired
	AwbStatusSeedData awbStatusSeedData;
	@Autowired
	ConsignmentStatusSeedData consignmentStatusSeedData;
	@Autowired
	RunsheetStatusSeedData runsheetStatusSeedData;
	@Autowired
	ConsignmentLifecycleStatusSeedData consignmentLifecycleStatusSeedData;
	@Autowired
	ReplacementOrderReasonSeedData replacementOrderReasonSeedData;
	@Autowired
	DispatchLotStatusSeedData dispatchLotStatusSeedData;
	@Autowired
	SkuItemStatusSeedData skuItemStatusSeedData;
	@Autowired
	PickupStatusSeedData pickupStatusSeedData;
	@Autowired
	StockTransferStatusSeed stockTransferStatusSeed;
	@Autowired
	ExtraInventoryLineItemTypeSeedData extraInventoryLineItemTypeSeedData;
	@Autowired
	DebitNoteTypeSeedData debitNoteTypeSeedData;
	@Autowired
	PurchaseOrderTypeSeedData purchaseOrderTypeSeedData;
    @Autowired
    ReversePickupStatusSeedData reversePickupStatusSeedData;
	

	public void insert() {

		//todo do all new roles insertions through db, insert scripts till a new framework is not in place
//        logger.debug("inserting roles");
//        roleSeedData.invokeInsert();
//
//        logger.debug("inserting permissions");
//        permissionSeedData.invokeInsert();

//        logger.debug("inserting role has permissions");
//        roleHasPermissionSeedData.invokeInsert();

//        logger.debug("inserting couriers");
//        courierSeedData.invokeInsert();

		/*
				 * logger.debug("inserting lineItemStatus"); lineItemStatusSeedData.invokeInsert();
				 */

		logger.debug("inserting lineItemTypes");
		cartLineItemTypeSeedData.invokeInsert();

		logger.debug("inserting orderStatus");
		orderStatusSeedData.invokeInsert();

		logger.debug("inserting shippingOrderStatus");
		shippingOrderStatusSeedData.invokeInsert();

		logger.debug("inserting paymentMode");
		paymentModeSeedData.invokeInsert();

		logger.debug("inserting paymentStatus");
		paymentStatusSeedData.invokeInsert();

		logger.debug("inserting tax");
		taxSeedData.invokeInsert();

		logger.debug("inserting rewardPointStatus");
		rewardPointStatusSeedData.invokeInsert();

		logger.debug("inserting rewardPointTxnType");
		rewardPointTxnTypeSeedData.invokeInsert();

		logger.debug("inserting emailType");
		emailTypeSeedData.invokeInsert();

		logger.debug("inserting/updating ticket type domain..");
		ticketTypeSeedData.invokeInsert();

		logger.debug("inserting/updating ticket status domain..");
		ticketStatusSeedData.invokeInsert();

		logger.debug("inserting/updating reconciliation status domain..");
		reconciliationStatusSeedData.invokeInsert();

		logger.debug("inserting/updating cancellation type domain..");
		cancellationTypeSeedData.invokeInsert();

		logger.debug("inserting affiliateTxnType");
		affiliateTxnTypeSeedData.invokeInsert();

		logger.debug("inserting orderLifecycleActivity");
		orderLifecycleActivitySeedData.invokeInsert();

		logger.debug("inserting shippingOrderLifecycleActivity");
		shippingOrderLifecycleActivitySeedData.invokeInsert();

		logger.debug("inserting boxSize");
		boxSizeSeedData.invokeInsert();

		logger.debug("inserting purchaseOrderStatus");
		poStatusSeedData.invokeInsert();

		logger.debug("inserting rewardPointMode");
		rewardPointModeSeedData.invokeInsert();

		logger.debug("inserting servicePaymentType");
		productVariantPaymentTypeSeedData.invokeInsert();

		logger.debug("inserting serviceType");
		productVariantServiceTypeSeedData.invokeInsert();

		logger.debug("inserting grn status");
		grnStatusSeedData.invokeInsert();

		logger.debug("inserting inv txn type");
		invTxnTypeSeedData.invokeInsert();

		logger.debug("inserting google banned words");
		googleBannedWordSeedData.invokeInsert();

		logger.debug("inserting debit note status");
		debitNoteStatusSeedData.invokeInsert();

    logger.debug("inserting credit note status");
		creditNoteStatusSeedData.invokeInsert();

		logger.debug("inserting purchase invoice status seed data");
		purchaseInvoiceStatusSeedData.invokeInsert();

		logger.debug("inserting surcharge seed data");
		surchargeSeedData.invokeInsert();

		logger.debug("inserting ad_networks seed data");
		adNetworksSeedData.invokeInsert();

		logger.debug("inserting reconcillation seed data");
		reconciliationTypeSeedData.invokeInsert();

		logger.debug("inserting review status seed data");
		reviewStatusSeedData.invokeInsert();
//
//        logger.debug("inserting courier group seed data");
//        courierGroupSeedData.invokeInsert();

		logger.debug("inserting product referrer seed data");
		productReferrerSeedData.invokeInsert();

//        logger.debug("inserting courier group has courier seed data");
//        courierGroupHasCourierSeedData.invokeInsert();

		logger.debug("inserting purchase form-type  seed data");
		purchaseFormTypeSeedData.invokeInsert();

		logger.debug("inserting primary referrer for order  seed data");
		primaryReferrerForOrderSeedData.invokeInsert();

		logger.debug("inserting secondary referrer for order  seed data");
		secondaryReferrerForOrderSeedData.invokeInsert();

		logger.debug("inserting state names");
		stateSeedData.invokeInsert();

		logger.debug("inserting subscription status data");
		subscriptionStatusSeedData.invokeInsert();

		logger.debug("inserting subscription order status data");
		subscriptionOrderStatusSeedData.invokeInsert();

		logger.debug("inserting subscription lifecycle activity data");
		subscriptionLifeCycleActivitySeedData.invokeInsert();

		logger.debug("inserting awb status");
		awbStatusSeedData.invokeInsert();

		logger.debug("inserting consignment status");
		consignmentStatusSeedData.invokeInsert();

		logger.debug("inserting runsheet status");
		runsheetStatusSeedData.invokeInsert();

		logger.debug("inserting consignment lifecycle status");
		consignmentLifecycleStatusSeedData.invokeInsert();

		logger.debug("inserting consignment Replacement order reasons");
		replacementOrderReasonSeedData.invokeInsert();

		logger.debug("inserting Dispatch Lot Status");
		dispatchLotStatusSeedData.invokeInsert();

		logger.debug("inserting Sku Item Status");
		skuItemStatusSeedData.invokeInsert();

		logger.debug("inserting courier pickup status");
		pickupStatusSeedData.invokeInsert();

		logger.debug("inserting Stock Transfer Status");
		stockTransferStatusSeed.invokeInsert();
		
		logger.debug("inserting Extra Inventory Line Item Type");
		extraInventoryLineItemTypeSeedData.invokeInsert();
		
		logger.debug("inserting Debit Note Type");
		debitNoteTypeSeedData.invokeInsert();
		
		logger.debug("inserting Purchase Order type");
		purchaseOrderTypeSeedData.invokeInsert();

        logger.debug("inserting Reverse Pickup Status");
        reversePickupStatusSeedData.invokeInsert();
	}
}
