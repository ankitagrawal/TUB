package com.hk.impl.dao.catalog.combo;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.combo.SuperSaverImageDao;

@Repository
public class SuperSaverImageDaoImpl extends BaseDaoImpl implements SuperSaverImageDao {
    public SuperSaverImage getSuperSaverImageByChecksum(String checksum) {
        return (SuperSaverImage) getSession().createQuery("from SuperSaverImage ssi where ssi.checksum = :checksum")
                .setString("checksum", checksum)
                .uniqueResult();
    }

    public List<SuperSaverImage> getSuperSaverImages() {
        return getSuperSaverImages(null, Boolean.FALSE, Boolean.FALSE);
    }

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible) {
        return getSuperSaverImages(null, getVisible, Boolean.FALSE);
    }

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible, Boolean getMainImage) {
        return getSuperSaverImages(null, getVisible, getMainImage);
    }

    public List<SuperSaverImage> getSuperSaverImages(Product product, Boolean getVisible, Boolean getMainImage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SuperSaverImage.class);
        criteria.add(Restrictions.eq("deleted", Boolean.FALSE));

        if (product != null) {
            criteria.add(Restrictions.eq("product", product));
        }

        if (getVisible) {
            criteria.add(Restrictions.isNotNull("ranking"));
            criteria.add(Restrictions.eq("hidden", Boolean.FALSE));
            if (getMainImage) {
                criteria.add(Restrictions.eq("isMainImage", Boolean.TRUE));
            }
        }

        criteria.addOrder(Order.asc("ranking"));
        return (List<SuperSaverImage>) findByCriteria(criteria);
    }

    public Page getSuperSaverImages(List<String> categories, List<String> brands, Boolean getVisible, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SuperSaverImage.class);
        criteria.add(Restrictions.eq("deleted", Boolean.FALSE));
        DetachedCriteria productCriteria = criteria.createCriteria("product");

        if (brands != null) {
            productCriteria.add(Restrictions.in("brand", brands));
        }

        if (categories != null) {
            DetachedCriteria categoryCriteria = productCriteria.createCriteria("categories");
            categoryCriteria.add(Restrictions.in("name", categories));
        }

        if (getVisible) {
            criteria.add(Restrictions.isNotNull("ranking"));
            criteria.add(Restrictions.eq("hidden", Boolean.FALSE));
        }

        criteria.addOrder(Order.asc("ranking"));
        return list(criteria, page, perPage);
    }
}