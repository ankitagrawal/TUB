package com.hk.admin.impl.service.accounting;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.admin.pact.dao.payment.PaymentHistoryDao;
import com.hk.admin.pact.service.accounting.PaymentHistoryService;
import com.hk.admin.pact.service.accounting.PurchaseInvoiceService;
import com.hk.constants.inventory.EnumPurchaseInvoiceStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.payment.PaymentHistory;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;

/**
 * User: Rahul Agarwal
 */
@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {
	@Autowired
	PurchaseInvoiceDao purchaseInvoiceDao;

	@Autowired
	PaymentHistoryDao paymentHistoryDao;

	@Override
	public PaymentHistory save(PaymentHistory paymentHistory) {
		return (PaymentHistory)getPaymentHistoryDao().save(paymentHistory);
	}

	@Override
	public PaymentHistory save(PaymentHistory paymentHistory, PurchaseInvoice purchaseInvoice) {
		getPaymentHistoryDao().save(paymentHistory);		
		Double outStandingAmount =  getOutstandingAmountForPurchaseInvoice(purchaseInvoice);
		if(outStandingAmount <= 0.00){
			purchaseInvoice.setPurchaseInvoiceStatus(EnumPurchaseInvoiceStatus.PurchaseInvoiceSettled.asPurchaseInvoiceStatus());
			purchaseInvoice.setPaymentDate(paymentHistory.getActualPaymentDate());
			getPurchaseInvoiceDao().save(purchaseInvoice);
		}
		return paymentHistory;
	}

	@Override
	public Double getOutstandingAmountForPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		List<PaymentHistory> paymentHistories = paymentHistoryDao.getByPurchaseInvoice(purchaseInvoice);
		Double outstandingAmount;
		if (!paymentHistories.isEmpty()) {
                Double paidAmount = 0.00;
                for (PaymentHistory paymentHistory : paymentHistories) {
	                if (paymentHistory.getAmount() != null) {
		                paidAmount += paymentHistory.getAmount();
	                }
                }
                outstandingAmount = purchaseInvoice.getFinalPayableAmount() - paidAmount;
				outstandingAmount = NumberUtil.roundTwoDecimals(outstandingAmount);
                /*int outstandingAmountFormatted = (int)(outstandingAmount*100);
                outstandingAmount = (double)(outstandingAmountFormatted)/100;*/
            }
            else{
              outstandingAmount = purchaseInvoice.getFinalPayableAmount();
            }
		return outstandingAmount;
	}

	@Override
	public boolean deletePaymentHistory(PaymentHistory paymentHistory) {
		PurchaseInvoice purchaseInvoice = paymentHistory.getPurchaseInvoice();
		if(purchaseInvoice != null){
			if(purchaseInvoice.getPurchaseInvoiceStatus().getId().equals(EnumPurchaseInvoiceStatus.PurchaseInvoiceSettled.getId())
					|| purchaseInvoice.getPaymentDate() != null){
				return false;
			}
		}
		getPaymentHistoryDao().delete(paymentHistory);
		return true;  
	}

	@Override
	public List<PaymentHistory> getByPurchaseOrder(PurchaseOrder purchaseOrder) {
		return getPaymentHistoryDao().getByPurchaseOrder(purchaseOrder);
	}

	@Override
	public List<PaymentHistory> getByPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		return getPaymentHistoryDao().getByPurchaseInvoice(purchaseInvoice);
	}

	public PurchaseInvoiceDao getPurchaseInvoiceDao() {
		return purchaseInvoiceDao;
	}

	public PaymentHistoryDao getPaymentHistoryDao() {
		return paymentHistoryDao;
	}
}