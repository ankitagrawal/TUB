package web.action.core.affiliate;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.dao.affiliate.AffiliateTxnDao;
import com.hk.domain.affiliate.Affiliate;

public class AffiliatePaymentDetailsAction extends BaseAction {
  
  AffiliateTxnDao affiliateTxnDao;


  private Affiliate affiliate;

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
