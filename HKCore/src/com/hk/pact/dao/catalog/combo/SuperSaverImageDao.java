package com.hk.pact.dao.catalog.combo;

import java.util.List;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.pact.dao.BaseDao;

public interface SuperSaverImageDao extends BaseDao {
    public SuperSaverImage getSuperSaverImageByChecksum(String checksum);

    public List<SuperSaverImage> getSuperSaverImages();

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible);

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible, Boolean getMainImage);

    public List<SuperSaverImage> getSuperSaverImages(Product product, Boolean getVisible, Boolean getMainImage, Boolean getDeleted);

    public List<SuperSaverImage> getSuperSaverImages(List<String> categories, List<String> brands, Boolean getVisible, Boolean getDeleted);
}
