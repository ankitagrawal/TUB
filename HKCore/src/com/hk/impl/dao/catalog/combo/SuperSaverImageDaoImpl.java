package com.hk.impl.dao.catalog.combo;

import org.springframework.stereotype.Repository;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.combo.SuperSaverImageDao;
import com.hk.domain.catalog.product.combo.SuperSaverImage;

import java.util.List;

@Repository
public class SuperSaverImageDaoImpl extends BaseDaoImpl implements SuperSaverImageDao {
    public SuperSaverImage getSuperSaverImageByChecksum(String checksum) {
        return (SuperSaverImage) getSession().createQuery("from SuperSaverImage ssi where ssi.checksum = :checksum")
                .setString("checksum", checksum)
                .uniqueResult();
    }    

    public List<SuperSaverImage> getSuperSaverImages() {
        return getSuperSaverImages(Boolean.FALSE, Boolean.FALSE);
    }

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible) {
        return getSuperSaverImages(getVisible, Boolean.FALSE);
    }

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible, Boolean getMainImage) {
        String visibleString = "";
        if (getVisible) {
            visibleString = "where ranking is not null and hidden = false";
            if (getMainImage) {
                visibleString += " and isMainImage = true";
            }
        }

        String hqlQuery = "from SuperSaverImage ssi " + visibleString + " order by ranking";
        return (List<SuperSaverImage>) getSession().createQuery(hqlQuery)
                .list();
    }
}
