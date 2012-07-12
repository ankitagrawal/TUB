package com.hk.admin.impl.dao.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.util.DateUtils;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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


  public RvLineItem getRvLineItem(ReconciliationVoucher reconciliationVoucher , Sku sku){
      DetachedCriteria rvLineItemCriteria = DetachedCriteria.forClass(RvLineItem.class);
      rvLineItemCriteria.add(Restrictions.eq("reconciliationVoucher",reconciliationVoucher));
      rvLineItemCriteria.add(Restrictions.eq("sku",sku));
      List<RvLineItem> rvLineItemList =findByCriteria(rvLineItemCriteria);
     if(rvLineItemList != null && rvLineItemList.size() > 0)
     return rvLineItemList.get(0);
    else
       return null;

  }
}