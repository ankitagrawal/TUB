package com.hk.admin.impl.dao.inventory;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.akube.framework.util.DateUtils;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class ReconciliationVoucherDaoImpl extends BaseDaoImpl implements ReconciliationVoucherDao {

    @SuppressWarnings("unchecked")
    public Page searchReconciliationVoucher(Date createDate, String userLogin, Warehouse warehouse, int pageNo, int perPage) {
        DetachedCriteria reconciliationVoucherCriteria = DetachedCriteria.forClass(ReconciliationVoucher.class);
        if (createDate != null) {
            reconciliationVoucherCriteria.add(Restrictions.gt("createDate", createDate));
            reconciliationVoucherCriteria.add(Restrictions.lt("createDate", DateUtils.getEndOfDay(createDate)));
        }

        if (warehouse != null) {
            reconciliationVoucherCriteria.add(Restrictions.eq("warehouse", warehouse));
        }

        if (!StringUtils.isBlank(userLogin)) {
            DetachedCriteria userCriteria = reconciliationVoucherCriteria.createCriteria("createdBy");
            userCriteria.add(Restrictions.like("login".toLowerCase(), "%" + userLogin.toLowerCase() + "%"));
        }
        return list(reconciliationVoucherCriteria, pageNo, perPage);
    }
}