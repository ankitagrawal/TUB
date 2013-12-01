package com.hk.admin.impl.dao.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.util.DateUtils;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
        reconciliationVoucherCriteria.addOrder(Order.desc("id"));
        return list(reconciliationVoucherCriteria, pageNo, perPage);
    }

    @SuppressWarnings("unchecked")
    public RvLineItem getRvLineItem(ReconciliationVoucher reconciliationVoucher, Sku sku) {
        DetachedCriteria rvLineItemCriteria = DetachedCriteria.forClass(RvLineItem.class);
        rvLineItemCriteria.add(Restrictions.eq("reconciliationVoucher", reconciliationVoucher));
        rvLineItemCriteria.add(Restrictions.eq("sku", sku));
        List<RvLineItem> rvLineItemList = findByCriteria(rvLineItemCriteria);
        if (rvLineItemList != null && rvLineItemList.size() > 0)
            return rvLineItemList.get(0);
        else
            return null;

    }


    public RvLineItem getRvLineItems(ReconciliationVoucher reconciliationVoucher, Sku sku, SkuGroup skuGroup, ReconciliationType reconciliationType) {
        DetachedCriteria rvLineItemCriteria = DetachedCriteria.forClass(RvLineItem.class);
        rvLineItemCriteria.add(Restrictions.eq("reconciliationVoucher", reconciliationVoucher));
        rvLineItemCriteria.add(Restrictions.eq("sku", sku));
        rvLineItemCriteria.add(Restrictions.eq("skuGroup", skuGroup));
        rvLineItemCriteria.add(Restrictions.eq("reconciliationType", reconciliationType));
        List<RvLineItem> rvLineItemList = (List<RvLineItem>) findByCriteria(rvLineItemCriteria);
        if (rvLineItemList != null && rvLineItemList.size() > 0)
            return rvLineItemList.get(0);
        else
            return null;
    }
    
    @Override
    public DebitNote getDebitNote(ReconciliationVoucher reconciliationVoucher) {
    	String query = "from DebitNote dn where dn.reconciliationVoucher=:reconciliationVoucher ";
		DebitNote debitNote = (DebitNote) findUniqueByNamedParams(query, new String[]{"reconciliationVoucher"}, new Object[]{reconciliationVoucher});
		if(debitNote!=null){
			return debitNote;
		}
		else
			return null;
    }


}