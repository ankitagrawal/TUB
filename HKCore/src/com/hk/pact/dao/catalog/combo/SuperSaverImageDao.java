package com.hk.pact.dao.catalog.combo;

import com.hk.pact.dao.BaseDao;
import com.hk.domain.catalog.product.combo.SuperSaverImage;

import java.util.List;

public interface SuperSaverImageDao extends BaseDao {
    public SuperSaverImage getSuperSaverImageByChecksum(String checksum);

    public List<SuperSaverImage> getSuperSaverImages();

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible);

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible, Boolean getMainImage);
}
