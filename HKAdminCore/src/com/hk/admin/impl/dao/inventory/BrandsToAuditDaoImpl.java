package com.hk.admin.impl.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
import com.hk.domain.inventory.BrandsToAudit;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.Sku;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.constants.inventory.EnumAuditStatus;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@SuppressWarnings ("unchecked")
@Repository
public class BrandsToAuditDaoImpl extends BaseDaoImpl implements BrandsToAuditDao {

	public Page searchAuditList(String brand, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage) {
		DetachedCriteria auditCriteria = DetachedCriteria.forClass(BrandsToAudit.class);
		if (StringUtils.isNotBlank(brand)) {
			auditCriteria.add(Restrictions.eq("brand", brand));
		}
		if (warehouse != null) {
			auditCriteria.add(Restrictions.eq("warehouse", warehouse));
		}
		if (auditor != null) {
			auditCriteria.add(Restrictions.eq("auditor", auditor));
		}
		if (startDate != null && endDate != null) {
			auditCriteria.add(Restrictions.between("auditDate", startDate, endDate));
		}
		auditCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
		return list(auditCriteria, pageNo, perPage);

	}

	public List<String> brandsToBeAudited(Warehouse warehouse) {
		String queryString = "select distinct lower(trim(ba.brand)) from BrandsToAudit ba where ba.warehouse = :warehouse and ba.auditStatus = :auditStatus";
		return (List<String>) findByNamedParams(queryString, new String[]{"warehouse", "auditStatus"}, new Object[]{warehouse, EnumAuditStatus.Pending.getId()});
	}

	public boolean isBrandAudited(String brand, Warehouse warehouse) {
		String queryString = "from BrandsToAudit ba where ba.warehouse = :warehouse and ba.brand = :brand";
		List<BrandsToAudit> brandsToAuditList = findByNamedParams(queryString, new String[]{"warehouse", "brand"}, new Object[]{warehouse, brand});
		if (!brandsToAuditList.isEmpty()) {
			return true;
		}
		return false;
	}

}