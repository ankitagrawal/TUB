package com.hk.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.db.seed.catalog.ProductVariantPaymentTypeSeedData;
import com.hk.db.seed.catalog.ProductVariantServiceTypeSeedData;
import com.hk.db.seed.core.AffiliateTxnTypeSeedData;
import com.hk.db.seed.core.CancellationTypeSeedData;
import com.hk.db.seed.core.PermissionSeedData;
import com.hk.db.seed.core.RoleHasPermissionSeedData;
import com.hk.db.seed.core.RoleSeedData;
import com.hk.db.seed.core.SurchargeSeedData;
import com.hk.db.seed.core.TaxSeedData;
import com.hk.db.seed.courier.BoxSizeSeedData;
import com.hk.db.seed.courier.CourierGroupSeedData;
import com.hk.db.seed.courier.CourierSeedData;
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
import com.hk.db.seed.ticket.TicketStatusSeedData;
import com.hk.db.seed.ticket.TicketTypeSeedData;

/**
 * Author: Kani Date: Jul 23, 2009
 */
public class MasterDataManager {
    private static Logger                  logger = LoggerFactory.getLogger(MasterDataManager.class);

    RoleSeedData                           roleSeedData;
    PermissionSeedData                     permissionSeedData;
    RoleHasPermissionSeedData              roleHasPermissionSeedData;
    CourierSeedData                        courierSeedData;
    /* CartLineItemTypeSeedData lineItemStatusSeedData; */

    CartLineItemTypeSeedData               cartLineItemTypeSeedData;
    OrderStatusSeedData                    orderStatusSeedData;
    ShippingOrderStatusSeedData            shippingOrderStatusSeedData;
    PaymentModeSeedData                    paymentModeSeedData;
    PaymentStatusSeedData                  paymentStatusSeedData;
    TaxSeedData                            taxSeedData;
    RewardPointStatusSeedData              rewardPointStatusSeedData;
    RewardPointTxnTypeSeedData             rewardPointTxnTypeSeedData;
    EmailTypeSeedData                      emailTypeSeedData;
    TicketTypeSeedData                     ticketTypeSeedData;
    TicketStatusSeedData                   ticketStatusSeedData;
    ReconciliationStatusSeedData           reconciliationStatusSeedData;
    CancellationTypeSeedData               cancellationTypeSeedData;
    AffiliateTxnTypeSeedData               affiliateTxnTypeSeedData;
    OrderLifecycleActivitySeedData         orderLifecycleActivitySeedData;
    ShippingOrderLifecycleActivitySeedData shippingOrderLifecycleActivitySeedData;
    BoxSizeSeedData                        boxSizeSeedData;
    PurchaseOrderStatusSeedData            poStatusSeedData;
    RewardPointModeSeedData                rewardPointModeSeedData;
    ProductVariantServiceTypeSeedData      productVariantServiceTypeSeedData;
    ProductVariantPaymentTypeSeedData      productVariantPaymentTypeSeedData;
    GrnStatusSeedData                      grnStatusSeedData;
    InvTxnTypeSeedData                     invTxnTypeSeedData;
    GoogleBannedWordSeedData               googleBannedWordSeedData;
    DebitNoteStatusSeedData                debitNoteStatusSeedData;
    PurchaseInvoiceStatusSeedData          purchaseInvoiceStatusSeedData;
    SurchargeSeedData                      surchargeSeedData;
    AdNetworksSeedData                     adNetworksSeedData;
    ReconciliationTypeSeedData             reconciliationTypeSeedData;
    ReviewStatusSeedData                   reviewStatusSeedData;
	  ProductReferrerSeedData                productReferrerSeedData;
    CourierGroupSeedData                   courierGroupSeedData;
    PrimaryReferrerForOrderSeedData        primaryReferrerForOrderSeedData;
    SecondaryReferrerForOrderSeedData      secondaryReferrerForOrderSeedData;
    PurchaseFormTypeSeedData               purchaseFormTypeSeedData;

    public void insert() {

        logger.debug("inserting roles");
        roleSeedData.invokeInsert();

        logger.debug("inserting permissions");
        permissionSeedData.invokeInsert();

        logger.debug("inserting role has permissions");
        roleHasPermissionSeedData.invokeInsert();

        logger.debug("inserting couriers");
        courierSeedData.invokeInsert();

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
        
        logger.debug("inserting courier group seed data");
        courierGroupSeedData.invokeInsert();

        logger.debug("inserting product referrer seed data");
        productReferrerSeedData.invokeInsert();

        logger.debug("inserting primary source for order");
        primaryReferrerForOrderSeedData.invokeInsert();

        logger.debug("inserting purchase form-type  seed data");
        purchaseFormTypeSeedData.invokeInsert();

        logger.debug("inserting secondary source for order");
        secondaryReferrerForOrderSeedData.invokeInsert();
    }
}
