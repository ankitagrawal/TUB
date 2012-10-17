package com.hk.impl.dao.catalog.product;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.inventory.BrandsToAudit;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.UpdatePvPriceDao;
import com.hk.constants.inventory.EnumAuditStatus;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UpdatePvPriceDaoImpl extends BaseDaoImpl implements UpdatePvPriceDao {

	public Page getPVForPriceUpdate(Category primaryCategory, ProductVariant productVariant, Long status, int pageNo, int perPage) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UpdatePvPrice.class);
		if (status != null) {
			criteria.add(Restrictions.eq("status", status));
		}
		if (primaryCategory != null) {
			DetachedCriteria productVariantCriteria = criteria.createCriteria("productVariant");
			DetachedCriteria productCriteria = productVariantCriteria.createCriteria("product");
			productCriteria.add(Restrictions.eq("primaryCategory", primaryCategory));
		} else if (productVariant != null) {
			criteria.add(Restrictions.eq("productVariant", productVariant));
		}

		criteria.addOrder(org.hibernate.criterion.Order.desc("txnDate"));
		return list(criteria, pageNo, perPage);
	}

	public UpdatePvPrice getPVForPriceUpdate(ProductVariant productVariant, Long status) {
		List<UpdatePvPrice> updatePvPriceList = (List<UpdatePvPrice>) findByNamedParams("from UpdatePvPrice upp where upp.productVariant =  :productVariant and upp.status = :status", new String[]{"productVariant", "status"}, new Object[]{productVariant, status});
		if (updatePvPriceList != null && !updatePvPriceList.isEmpty()) {
			return updatePvPriceList.get(0);
		}
		return null;
	}

	public boolean isBrandAudited(String brand) {
		String queryString = "from BrandsToAudit ba where ba.brand = :brand and ba.auditStatus = :auditStatus";
		List<BrandsToAudit> brandsToAuditList = findByNamedParams(queryString, new String[]{"brand", "auditStatus"}, new Object[]{brand, EnumAuditStatus.Done.getId()});
		if (!brandsToAuditList.isEmpty()) {
			return true;
		}
		return false;
	}

}