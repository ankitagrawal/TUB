package com.hk.web.action.admin.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hk.admin.pact.service.accounting.PaymentHistoryService;
import com.hk.admin.pact.service.accounting.PurchaseInvoiceService;
import com.hk.constants.core.EnumPermission;
import com.hk.pact.service.UserService;
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
    private PurchaseInvoiceService purchaseInvoiceService;

	@Autowired
	PaymentHistoryService paymentHistoryService;

	@Autowired
	private UserService userService;

    private PaymentHistory       paymentHistory;
    private Long                 purchaseOrderId;
    private Long                 purchaseInvoiceId;
    private PurchaseOrder        purchaseOrder;
    private PurchaseInvoice      purchaseInvoice;
    // private Double ;
    private Double               outstandingAmount;
    private Supplier             supplier;

	private boolean              isEditable;

    private List<PaymentHistory> paymentHistories   = new ArrayList<PaymentHistory>();
    private List<PaymentHistory> paymentHistoriesPO = new ArrayList<PaymentHistory>();

    @DefaultHandler
    public Resolution pre() {
        if (purchaseOrderId != null) {
            purchaseOrder = getBaseDao().get(PurchaseOrder.class, purchaseOrderId);
            paymentHistories = getPaymentHistoryService().getByPurchaseOrder(purchaseOrder);
            Collections.reverse(paymentHistories);
	        isEditable = true;
        } else if (purchaseInvoiceId != null) {
            outstandingAmount = 0.00;
            purchaseInvoice = getPurchaseInvoiceService().getPurchaseInvoiceById(purchaseInvoiceId);
            purchaseOrder = StockProcurementHelper.getPurchaseOrderForPurchaseInvoice(purchaseInvoice);
            //purchaseInvoice = purchaseInvoiceDao.find(purchaseInvoiceId);
            supplier = purchaseInvoice.getSupplier();
            paymentHistories = getPaymentHistoryService().getByPurchaseInvoice(purchaseInvoice);
            if (purchaseOrder != null && paymentHistories.isEmpty()) {
                paymentHistoriesPO = getPaymentHistoryService().getByPurchaseOrder(purchaseOrder);
                Collections.reverse(paymentHistoriesPO);
                for (PaymentHistory paymentHistoryPO : paymentHistoriesPO) {
                    PaymentHistory paymentHistoryNewPi = new PaymentHistory();
                    paymentHistoryNewPi.setPurchaseInvoice(purchaseInvoice);
                    paymentHistoryNewPi.setActualPaymentDate(paymentHistoryPO.getActualPaymentDate());
                    paymentHistoryNewPi.setAmount(paymentHistoryPO.getAmount());
                    paymentHistoryNewPi.setModeOfPayment(paymentHistoryPO.getModeOfPayment());
                    paymentHistoryNewPi.setRemarks(paymentHistoryPO.getRemarks());
                    paymentHistoryNewPi.setScheduledPaymentDate(paymentHistoryPO.getScheduledPaymentDate());
                    paymentHistoryNewPi.setPaymentReference(paymentHistoryPO.getPaymentReference());
                    getPaymentHistoryService().save(paymentHistoryNewPi, purchaseInvoice);
                }
            }
            paymentHistories = getPaymentHistoryService().getByPurchaseInvoice(purchaseInvoice);
            Collections.reverse(paymentHistories);
            outstandingAmount = getPaymentHistoryService().getOutstandingAmountForPurchaseInvoice(purchaseInvoice);

	        boolean isLoggedInUserHasFinancePermission=userService.getLoggedInUser().hasPermission(EnumPermission.EDIT_PAYMENT_HISTORY);

	        if(outstandingAmount.doubleValue() > 0.00){
		        isEditable = true;
	        }
	        else if(isLoggedInUserHasFinancePermission){
		        isEditable = true;
	        }
	        else{
		        isEditable = false;
	        }
        }

        return new ForwardResolution("/pages/admin/paymentHistory.jsp");
    }


    public Resolution editPurchaseInvoice(){
/*      PurchaseInvoice purchaseInvoiceTemp = getBaseDao().get(PurchaseInvoice.class, purchaseInvoiceId);
      purchaseInvoiceTemp.setPaymentDetails(purchaseInvoice.getPaymentDetails());
      purchaseInvoiceTemp.setPaymentDate(purchaseInvoice.getPaymentDate());*/
      if(purchaseInvoice != null){
        try{
          purchaseInvoiceService.save(purchaseInvoice);
          addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
          if (purchaseInvoiceId != null) {
              return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseInvoiceId", purchaseInvoiceId);
          }
        }catch (Exception e){
          logger.error("Unable to save purchase invoice", e);
        }
      }
      return new ForwardResolution("/pages/admin/paymentHistory.jsp");
    }

    @DontValidate
    public Resolution save() {
        // pincodeDefaultCourierDao.save(pincodeDefaultCourier);
        /*
         * if(paymentHistory.getActualPaymentDate() == null){ paymentHistory.setActualPaymentDate(null); }
         */
        for (PaymentHistory paymentHistory : paymentHistories) {
	        if(paymentHistory.getPurchaseInvoice() != null){
                getPaymentHistoryService().save(paymentHistory, paymentHistory.getPurchaseInvoice());
	        }
	        else{
		        getPaymentHistoryService().save(paymentHistory);
	        }
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
        boolean deleteStatus = getPaymentHistoryService().deletePaymentHistory(paymentHistory);
	    if(deleteStatus){
            addRedirectAlertMessage(new SimpleMessage("Payment History record deleted.."));
	    }
	    else{
		    addRedirectAlertMessage(new SimpleMessage("Payment History cannot be deleted as it is associated with a settled Purchase Invoice."));
	    }
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

                purchaseInvoice =getPurchaseInvoiceService().getPurchaseInvoiceById(purchaseInvoiceId);
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
            if (paymentHistory.getPaymentReference() != null) {
                paymentHistoryNew.setPaymentReference(paymentHistory.getPaymentReference());
            }

	        if(purchaseInvoice != null){
		        outstandingAmount = getPaymentHistoryService().getOutstandingAmountForPurchaseInvoice(purchaseInvoice);
		        outstandingAmount -= paymentHistoryNew.getAmount();
		        if(outstandingAmount <= 0 && paymentHistoryNew.getActualPaymentDate() == null){
			        addRedirectAlertMessage(new SimpleMessage("Please enter the actual payment date !"));
			        return new RedirectResolution(PaymentHistoryAction.class).addParameter("purchaseInvoiceId", purchaseInvoiceId);
		        }
                getPaymentHistoryService().save(paymentHistoryNew, purchaseInvoice);
	        }
	        else{
		        getPaymentHistoryService().save(paymentHistory);
	        }
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

	public PaymentHistoryService getPaymentHistoryService() {
		return paymentHistoryService;
	}

	public PurchaseInvoiceService getPurchaseInvoiceService() {
		return purchaseInvoiceService;
	}

	public boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
