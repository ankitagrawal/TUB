package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.payment.PreferredBankGateway;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import java.util.List;

public class PaymentModeAction extends BaseAction {

	List<PreferredBankGateway> bankList;

	public Resolution pre() {
		bankList = getBaseDao().getAll(PreferredBankGateway.class);
		return new ForwardResolution("/pages/paymentMode.jsp");
	}

	public List<PreferredBankGateway> getBankList() {
		return bankList;
	}

	public void setBankList(List<PreferredBankGateway> bankList) {
		this.bankList = bankList;
	}
}
