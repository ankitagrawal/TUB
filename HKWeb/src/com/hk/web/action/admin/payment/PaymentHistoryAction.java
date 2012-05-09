package com.hk.web.action.admin.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.dao.payment.PaymentHistoryDao;
import com.hk.admin.util.StockProcurementHelper;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.payment.PaymentHistory;

@Secure(hasAnyPermissions = { PermissionConstants.MANAGE_PAYMENT_HISTORY })
public class PaymentHistoryAction extends BaseAction {
    private static Logger        logger             = LoggerFactory.getLogger(PaymentHistoryAction.class);

    
    @Autowired
    private PaymentHistoryDao    paymentHistoryDao;

    private PaymentHistory       paymentHistory;
    private Long                 purchaseOrderId;
    private Long                 purchaseInvoiceId;
    private PurchaseOrder        purchaseOrder;
    private PurchaseInvoice      purchaseInvoice;
    // private Double ;
    private Double               outstandingAmount;
    private Supplier             supplier;

    private List<PaymentHistory> paymentHistories   = new ArrayList<PaymentHistory>();
    private List<PaymentHistory> paymentHistoriesPO = new ArrayList<PaymentHistory>();

    @DefaultHandler
    public Resolution pre() {

        if (purchaseOrderId != null) {
            purchaseOrder = getBaseDao().get(PurchaseOrder.class, purchaseOrderId);
            paymentHistories = paymentHistoryDao.getByPurchaseOrder(purchaseOrder);
            Collections.reverse(paymentHistories);
        } else if (purchaseInvoiceId != null) {
            outstandingAmount = 0.00;
            purchaseInvoice = getBaseDao().get(PurchaseInvoice.class, purchaseInvoiceId);
            purchaseOrder = StockProcurementHelper.getPurchaseOrderForPurchaseInvoice(purchaseInvoice);
            //purchaseInvoice = purchaseInvoiceDao.find(purchaseInvoiceId);
            supplier = purchaseInvoice.getSupplier();
            paymentHistories = paymentHistoryDao.getByPurchaseInvoice(purchaseInvoice);
            if (purchaseOrder != null && paymentHistories.isEmpty()) {
                paymentHistoriesPO = paymentHistoryDao.getByPurchaseOrder(purchaseOrder);
                Collections.reverse(paymentHistoriesPO);
                for (PaymentHistory paymentHistoryPO : paymentHistoriesPO) {
                    PaymentHistory paymentHistoryNewPi = new PaymentHistory();
                    paymentHistoryNewPi.setPurchaseInvoice(purchaseInvoice);
                    paymentHistoryNewPi.setActualPaymentDate(paymentHistoryPO.getActualPaymentDate());
                    paymentHistoryNewPi.setAmount(paymentHistoryPO.getAmount());
                    paymentHistoryNewPi.setModeOfPayment(paymentHistoryPO.getModeOfPayment());
                    paymentHistoryNewPi.setRemarks(paymentHistoryPO.getRemarks());
                    paymentHistoryNewPi.setScheduledPaymentDate(paymentHistoryPO.getScheduledPaymentDate());
                    paymentHistoryDao.save(paymentHistoryNewPi);
                }
            }
            paymentHistories = paymentHistoryDao.getByPurchaseInvoice(purchaseInvoice);
            Collections.reverse(paymentHistories);
            if (!paymentHistories.isEmpty()) {
                Double paidAmount = 0.00;
                for (PaymentHistory paymentHistoryTemp : paymentHistories) {
                    paidAmount += paymentHistoryTemp.getAmount();
                }
                outstandingAmount = purchaseInvoice.getPayableAmount() - paidAmount;
            }
        }

        return new ForwardResolution("/pages/admin/paymentHistory.jsp");
    }

    public Resolution search() {
        try {
            if (purchaseOrderId == null && purchaseInvoiceId == null) {
                addRedirectAlertMessage(new SimpleMessage("Please add either purchase invoice id or purchase order id."));
                return new RedirectResolution(PaymentHistoryAction.class);
            } else if (purchaseOrderId != null) {
                purchaseOrder = getBaseDao().get(PurchaseOrder.class, purchaseOrderId);
                paymentHistories = paymentHistoryDao.getByPurchaseOrder(purchaseOrder);
                return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseOrderId", purchaseOrderId);
            } else {
                outstandingAmount = 0.00;
                purchaseInvoice = getBaseDao().get(PurchaseInvoice.class, purchaseInvoiceId);
                supplier = purchaseInvoice.getSupplier();
                //StockProcurementService stopProcurementService = new StockProcurementService();
                purchaseOrder = StockProcurementHelper.getPurchaseOrderForPurchaseInvoice(purchaseInvoice);
                paymentHistories = paymentHistoryDao.getByPurchaseInvoice(purchaseInvoice);
                if (purchaseOrder != null && paymentHistories.isEmpty()) {
                    paymentHistoriesPO = paymentHistoryDao.getByPurchaseOrder(purchaseOrder);
                    Collections.reverse(paymentHistoriesPO);
                    for (PaymentHistory paymentHistoryPO : paymentHistoriesPO) {
                        PaymentHistory paymentHistoryNewPi = new PaymentHistory();
                        paymentHistoryNewPi.setPurchaseInvoice(purchaseInvoice);
                        paymentHistoryNewPi.setActualPaymentDate(paymentHistoryPO.getActualPaymentDate());
                        paymentHistoryNewPi.setAmount(paymentHistoryPO.getAmount());
                        paymentHistoryNewPi.setModeOfPayment(paymentHistoryPO.getModeOfPayment());
                        paymentHistoryNewPi.setRemarks(paymentHistoryPO.getRemarks());
                        paymentHistoryNewPi.setScheduledPaymentDate(paymentHistoryPO.getScheduledPaymentDate());
                        paymentHistoryDao.save(paymentHistoryNewPi);
                    }
                }
                paymentHistories = paymentHistoryDao.getByPurchaseInvoice(purchaseInvoice);

                // paymentHistoriesPO = paymentHistoryDao.getByPurchaseOrder(purchaseOrder);
                // paymentHistories.addAll(paymentHistoriesPO);
                Collections.reverse(paymentHistories);
                if (!paymentHistories.isEmpty()) {
                    Double paidAmount = 0.00;
                    for (PaymentHistory paymentHistoryTemp : paymentHistories) {
                        paidAmount += paymentHistoryTemp.getAmount();
                    }
                    outstandingAmount = purchaseInvoice.getPayableAmount() - paidAmount;
                }
                return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseInvoiceId", purchaseInvoiceId);
            }
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
        }
        addRedirectAlertMessage(new SimpleMessage("No Payment History found"));
        return new RedirectResolution(PaymentHistoryAction.class);
    }

    @DontValidate
    public Resolution save() {
        // pincodeDefaultCourierDao.save(pincodeDefaultCourier);
        /*
         * if(paymentHistory.getActualPaymentDate() == null){ paymentHistory.setActualPaymentDate(null); }
         */
        for (PaymentHistory paymentHistory : paymentHistories) {
            paymentHistoryDao.save(paymentHistory);
        }
        if (purchaseOrderId != null) {
            return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseOrderId", purchaseOrderId);
        }
        if (purchaseInvoiceId != null) {
            return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseInvoiceId", purchaseInvoiceId);
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        return new ForwardResolution("/pages/admin/paymentHistory.jsp");
    }

    public Resolution delete() {
        // pincodeDefaultCourierDao.save(pincodeDefaultCourier);
        getBaseDao().delete(paymentHistory);
        addRedirectAlertMessage(new SimpleMessage("Payment History record deleted.."));
        if (purchaseOrderId != null) {
            return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseOrderId", purchaseOrderId);
        }
        if (purchaseInvoiceId != null) {
            return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseInvoiceId", purchaseInvoiceId);
        }
        return new ForwardResolution("/pages/admin/paymentHistory.jsp");
    }

    public Resolution add_paymentHistory() {

        if (purchaseOrderId == null && purchaseInvoiceId == null) {
            addRedirectAlertMessage(new SimpleMessage("Both, Purchase Order and purchase invoice cannot be null "));
            return new RedirectResolution(PaymentHistoryAction.class);
        }
        try {
            PaymentHistory paymentHistoryNew = new PaymentHistory();
            if (purchaseOrderId != null) {
                paymentHistoryNew.setPurchaseOrder(getBaseDao().get(PurchaseOrder.class, purchaseOrderId));
            }
            if (purchaseInvoiceId != null) {

                purchaseInvoice = getBaseDao().get(PurchaseInvoice.class, purchaseInvoiceId);
                //StockProcurementService stopProcurementService = new StockProcurementService();
                purchaseOrder = StockProcurementHelper.getPurchaseOrderForPurchaseInvoice(purchaseInvoice);
                paymentHistoryNew.setPurchaseInvoice(purchaseInvoice);
            }
            if (paymentHistory.getAmount() != null) {
                paymentHistoryNew.setAmount(paymentHistory.getAmount());
            }
            if (paymentHistory.getScheduledPaymentDate() != null) {
                paymentHistoryNew.setScheduledPaymentDate(paymentHistory.getScheduledPaymentDate());
            }
            if (paymentHistory.getActualPaymentDate() != null) {
                paymentHistoryNew.setActualPaymentDate(paymentHistory.getActualPaymentDate());
            }
            if (paymentHistory.getModeOfPayment() != null) {
                paymentHistoryNew.setModeOfPayment(paymentHistory.getModeOfPayment());
            }
            if (paymentHistory.getRemarks() != null) {
                paymentHistoryNew.setRemarks(paymentHistory.getRemarks());
            }
            paymentHistoryDao.save(paymentHistoryNew);
        } catch (Exception e) {
            logger.error("Could not insert new payment detail: ", e); // To change body of catch statement use File |
            // Settings | File Templates.
        }

        if (purchaseOrderId != null) {
            return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseOrderId", purchaseOrderId);
        }
        if (purchaseInvoiceId != null) {
            return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseInvoiceId", purchaseInvoiceId);
        }

        addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        return new ForwardResolution("/pages/admin/paymentHistory.jsp");
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(PaymentHistory paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Long getPurchaseInvoiceId() {
        return purchaseInvoiceId;
    }

    public void setPurchaseInvoiceId(Long purchaseInvoiceId) {
        this.purchaseInvoiceId = purchaseInvoiceId;
    }

    public List<PaymentHistory> getPaymentHistories() {
        return paymentHistories;
    }

    public void setPaymentHistories(List<PaymentHistory> paymentHistories) {
        this.paymentHistories = paymentHistories;
    }

    public Double getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public PurchaseInvoice getPurchaseInvoice() {
        return purchaseInvoice;
    }

    public void setPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        this.purchaseInvoice = purchaseInvoice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
