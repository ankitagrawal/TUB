package com.hk.db;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.google.inject.Inject;
import db.seed.master.*;

/**
 * Author: Kani
 * Date: Jul 23, 2009
 */
public class MasterDataManager {
  private static Logger logger = LoggerFactory.getLogger(MasterDataManager.class);

  @Inject RoleSeedData roleSeedData;
  @Inject PermissionSeedData permissionSeedData;
  @Inject RoleHasPermissionSeedData roleHasPermissionSeedData;
  @Inject CourierSeedData courierSeedData;
 /* @Inject CartLineItemTypeSeedData lineItemStatusSeedData;*/
  @Inject
  CartLineItemTypeSeedData cartLineItemTypeSeedData;
  @Inject OrderStatusSeedData orderStatusSeedData;
  @Inject ShippingOrderStatusSeedData shippingOrderStatusSeedData;
  @Inject PaymentModeSeedData paymentModeSeedData;
  @Inject PaymentStatusSeedData paymentStatusSeedData;
  @Inject TaxSeedData taxSeedData;
  @Inject RewardPointStatusSeedData rewardPointStatusSeedData;
  @Inject RewardPointTxnTypeSeedData rewardPointTxnTypeSeedData;
  @Inject EmailTypeSeedData emailTypeSeedData;
  @Inject TicketTypeSeedData ticketTypeSeedData;
  @Inject TicketStatusSeedData ticketStatusSeedData;
  @Inject ReconciliationStatusSeedData reconciliationStatusSeedData;
  @Inject CancellationTypeSeedData cancellationTypeSeedData;
  @Inject AffiliateTxnTypeSeedData affiliateTxnTypeSeedData;
  @Inject OrderLifecycleActivitySeedData orderLifecycleActivitySeedData;
  @Inject ShippingOrderLifecycleActivitySeedData shippingOrderLifecycleActivitySeedData;
  @Inject BoxSizeSeedData boxSizeSeedData;
  @Inject PurchaseOrderStatusSeedData poStatusSeedData;
  @Inject RewardPointModeSeedData rewardPointModeSeedData;
  @Inject ProductVariantServiceTypeSeedData productVariantServiceTypeSeedData;
  @Inject ProductVariantPaymentTypeSeedData productVariantPaymentTypeSeedData;
  @Inject GrnStatusSeedData grnStatusSeedData;
  @Inject InvTxnTypeSeedData invTxnTypeSeedData;
  @Inject GoogleBannedWordSeedData googleBannedWordSeedData;
  @Inject DebitNoteStatusSeedData debitNoteStatusSeedData;
	@Inject PurchaseInvoiceStatusSeedData purchaseInvoiceStatusSeedData;
	@Inject SurchargeSeedData surchargeSeedData;
	@Inject AdNetworksSeedData adNetworksSeedData;
	@Inject ReconciliationTypeSeedData reconciliationTypeSeedData;
	@Inject ReviewStatusSeedData reviewStatusSeedData;

  public void insert() {

    logger.debug("inserting roles");
    roleSeedData.invokeInsert();

    logger.debug("inserting permissions");
    permissionSeedData.invokeInsert();

    logger.debug("inserting role has permissions");
    roleHasPermissionSeedData.invokeInsert();

    logger.debug("inserting couriers");
    courierSeedData.invokeInsert();

   /* logger.debug("inserting lineItemStatus");
    lineItemStatusSeedData.invokeInsert();*/

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

  }

}
