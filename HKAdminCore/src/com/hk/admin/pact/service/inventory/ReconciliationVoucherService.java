package com.hk.admin.pact.service.inventory;

import com.hk.domain.user.User;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationVoucher;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Aug 6, 2012
 * Time: 1:23:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReconciliationVoucherService {

    public void save(User loggedOnUser, List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher);
}
