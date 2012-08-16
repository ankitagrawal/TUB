package com.hk.web.action.core.affiliate;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.impl.dao.affiliate.AffiliateTxnDaoImpl;

public class AffiliatePaymentDetailsAction extends BaseAction {
    @Autowired
  AffiliateTxnDaoImpl affiliateTxnDao;


  /*private Affiliate affiliate;*/

//  public Resolution pre() {
//    List<AffiliateTxn> affiliateTxnList = affiliateTxnDao.getTxnListByAffiliate(affiliate);
//
//  }
//
//  public Affiliate getAffiliate() {
//    return affiliate;
//  }
//
//  public void setAffiliate(Affiliate affiliate) {
//    this.affiliate = affiliate;
//  }
}
