package com.hk.impl.dao.catalog.combo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.order.Order;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.combo.ComboDao;

@SuppressWarnings ("unchecked")
@Repository
public class ComboDaoImpl extends BaseDaoImpl implements ComboDao {

	public Combo getComboById(String id) {
		return get(Combo.class, id);
	}

	public ComboProduct getComboProduct(Product product, Combo combo) {
		return (ComboProduct) getSession().createQuery("select p from Combo c left join c.comboProducts p where c = :combo and p.product = :product").setParameter("combo", combo).setParameter("product", product).uniqueResult();
	}

	public Page getComboByCategoryAndBrand(List<Category> categories, String brand, int page, int perPage) {
		List<String> productIds = getSession().createQuery("select p.id from Combo p inner join p.categories c where c in (:categories) group by p.id having count(*) = :tagCount").setParameterList("categories", categories).setInteger("tagCount", categories.size()).list();

		if (productIds != null && productIds.size() > 0) {
			DetachedCriteria criteria = DetachedCriteria.forClass(Combo.class);
			if (StringUtils.isNotBlank(brand)) {
				// criteria.add(Restrictions.eq("brand", brand));
			}
			criteria.add(Restrictions.in("id", productIds));
			criteria.add(Restrictions.eq("deleted", false));

			return list(criteria, page, perPage);
		}
		return null;
	}

	public List<Combo> getCombos() {
		return getSession().createQuery("select c from Combo c where c.deleted != :deleted").setBoolean("deleted", true).list();
	}

	public List<Combo> getCombos(Product product) {
		return (List<Combo>) findByNamedParams("select cp.combo from ComboProduct cp where cp.product = :product", new String[]{"product"}, new Object[]{product});
	}

  public List<Combo> getCombos(ProductVariant productVariant) {
		return (List<Combo>) getSession().createQuery("select distinct c from Combo c left join c.comboProducts p left join p.allowedProductVariants pv where pv = :productVariant").setParameter("productVariant", productVariant).list();
	}

}