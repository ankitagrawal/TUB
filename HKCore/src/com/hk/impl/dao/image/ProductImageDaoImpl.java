package com.hk.impl.dao.image;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.image.ProductImageDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 9/10/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ProductImageDaoImpl extends BaseDaoImpl implements ProductImageDao {


	@Override
	public List<ProductImage> searchProductImages(Long imageTypeId, Product product, ProductVariant productVariant, boolean fetchAllImages, Boolean fetchHiddenImages) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductImage.class);
		if (product != null) {
			criteria.add(Restrictions.eq("product", product));
		}
		if (!fetchAllImages && productVariant != null) {
			criteria.add(Restrictions.eq("productVariant", productVariant));
		}
		if (imageTypeId != null) {
			criteria.add(Restrictions.eq("imageType", imageTypeId));
		}
        if (fetchHiddenImages != null) {
            criteria.add(Restrictions.eq("hidden", fetchHiddenImages));
        }
        return (List<ProductImage>) findByCriteria(criteria);
	}
}
