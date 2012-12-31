package com.hk.pact.dao.affiliate;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTxn;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

public interface AffiliateTxnDao extends BaseDao {

    public List<AffiliateTxn> getTxnListByAffiliate(Affiliate affiliate);

    public AffiliateTxn getTxnByOrder(Order order);

    public AffiliateTxn saveTxn(Affiliate affiliate, Double amountToAdd, AffiliateTxnType affiliateTxnType, Order order, Date chequeIssueDate);

    public AffiliateTxn saveTxn(Affiliate affiliate, Double amountToAdd, AffiliateTxnType affiliateTxnType, Order order);


    @SuppressWarnings("unchecked")
    public Page getReferredOrderListByAffiliate(Affiliate affiliate, Date startDate, Date endDate, int pageNo, int perPage);

    public long getReferredOrdersCountByAffiliate(Affiliate affiliate, Date startDate, Date endDate);

    public Double getAmountInAccount(Affiliate affiliate, Date startDate, Date endDate);

	public void approvePendingAffiliateTxn(Affiliate affiliate, Order order);

	void markAffiliateTxnAsDue(Affiliate affiliate);

	void markDueAffiliateTxnAsPaid(Affiliate affiliate, Date endDate);

	Double getPayableAmount(Affiliate affiliate);
}
