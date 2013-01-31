package com.hk.admin.impl.dao.inventory;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.hibernate.Query;
import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
import com.hk.constants.inventory.EnumAuditStatus;
import com.hk.domain.inventory.BrandsToAudit;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;



@SuppressWarnings ("unchecked")
@Repository
public class BrandsToAuditDaoImpl extends BaseDaoImpl implements BrandsToAuditDao {

	public Page searchAuditList(String brand, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage) {
		DetachedCriteria auditCriteria = getBrandsToAuditCriteria(brand, warehouse, auditor, startDate, endDate, null);
		auditCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
		return list(auditCriteria, pageNo, perPage);

	}

	public List<String> brandsToBeAudited(Warehouse warehouse) {
		String queryString = "select distinct lower(trim(ba.brand)) from BrandsToAudit ba where ba.warehouse = :warehouse and ba.auditStatus = :auditStatus";
		return (List<String>) findByNamedParams(queryString, new String[]{"warehouse", "auditStatus"}, new Object[]{warehouse, EnumAuditStatus.Pending.getId()});
	}

	public boolean isBrandAudited(String brand, Warehouse warehouse) {
		String queryString = "from BrandsToAudit ba where ba.warehouse = :warehouse and ba.brand = :brand and ba.auditStatus = :auditStatus";
		List<BrandsToAudit> brandsToAuditList = findByNamedParams(queryString,
				new String[]{"warehouse", "brand", "auditStatus"},
				new Object[]{warehouse, brand, EnumAuditStatus.Done.getId()});
		if (!brandsToAuditList.isEmpty()) {
			return true;
		}
		return false;
	}

    	public boolean isBrandAudited(String brand) {
		String queryString = "from BrandsToAudit ba where ba.brand = :brand and ba.auditStatus = :auditStatus";
		List<BrandsToAudit> brandsToAuditList = findByNamedParams(queryString,
				new String[]{"brand", "auditStatus"},
				new Object[]{brand, EnumAuditStatus.Done.getId()});
		if (!brandsToAuditList.isEmpty()) {
			return true;
		}
		return false;
	}

	public DetachedCriteria getBrandsToAuditCriteria(String brand, Warehouse warehouse, User auditor, Date startDate, Date endDate, Long auditStatus) {
	DetachedCriteria auditCriteria = DetachedCriteria.forClass(BrandsToAudit.class);
		if (StringUtils.isNotBlank(brand) ) {
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
		if (auditStatus != null) {
			auditCriteria.add(Restrictions.eq("auditStatus", auditStatus));
		}
		return  auditCriteria;
	}

	public List<BrandsToAudit> getBrandsToAudit(String brand, Long auditStatus) {
		DetachedCriteria brandDetachedCriteria =   getBrandsToAuditCriteria(brand, null, null, null, null, auditStatus);
		return findByCriteria(brandDetachedCriteria);
	}

}