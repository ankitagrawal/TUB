package com.hk.admin.impl.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.SkuItemAuditDao;
import com.hk.domain.inventory.SkuItemAudit;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.BaseDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@SuppressWarnings("unchecked")
@Repository
public class SkuItemAuditDaoImpl extends BaseDaoImpl implements SkuItemAuditDao {

  @Autowired
  BaseDao baseDao;


  public Page search(String brand, String variantId, String skuItemBarcode, String auditedBy,
                     Date startDate, Date endDate, Warehouse warehouse, int pageNo, int perPage) {
    
    DetachedCriteria skuItemAuditCriteria = DetachedCriteria.forClass(SkuItemAudit.class);
    DetachedCriteria skuCriteria = null;
    if (warehouse != null || brand != null || variantId != null)
      skuCriteria = skuItemAuditCriteria.createCriteria("skuItem").createCriteria("skuGroup").createCriteria("sku");
    DetachedCriteria variantCriteria = null;
    if (brand != null || variantId != null)
      variantCriteria = skuCriteria.createCriteria("productVariant");
    DetachedCriteria productCriteria = null;
    if (brand != null)
      productCriteria = variantCriteria.createCriteria("product");
    
    if (brand != null) {
      productCriteria.add(Restrictions.eq("brand", brand));
    }
    if (variantId != null) {
      variantCriteria.add(Restrictions.eq("id", variantId));
    }
    if (auditedBy != null) {
      DetachedCriteria userCriteria = skuItemAuditCriteria.createCriteria("user");
      userCriteria.add(Restrictions.eq("login", auditedBy));
    }
    if (startDate != null && endDate != null) {
      skuItemAuditCriteria.add(Restrictions.between("auditDate", startDate, endDate));
    }
    if (warehouse != null) {
      skuCriteria.add(Restrictions.eq("warehouse", warehouse));
    }

    skuItemAuditCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));

    return list(skuItemAuditCriteria, pageNo, perPage);
  }


}