package com.hk.impl.dao.catalog.combo;

import org.springframework.stereotype.Repository;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.combo.SuperSaverImageDao;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.domain.catalog.product.Product;

import java.util.List;

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

        StringBuilder hqlBuilder = new StringBuilder("from SuperSaverImage ssi where 1=1 ");

        if (product != null) {
            hqlBuilder.append(" and product.id = '" + product.getId() + "'");
        }

        if (getVisible) {
            hqlBuilder.append(" and ranking is not null and hidden = false");
            if (getMainImage) {
                hqlBuilder.append(" and isMainImage = true");
            }
        }
        hqlBuilder.append(" order by ranking");

        return (List<SuperSaverImage>) getSession().createQuery(hqlBuilder.toString())
                .list();
    }
}
