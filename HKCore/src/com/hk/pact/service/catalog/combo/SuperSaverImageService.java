package com.hk.pact.service.catalog.combo;

import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.domain.catalog.product.Product;
import com.akube.framework.dao.Page;

import java.util.List;

public interface SuperSaverImageService {
    public SuperSaverImage saveSuperSaverImage(SuperSaverImage superSaverImage);

    public void saveSuperSaverImages(List<SuperSaverImage> superSaverImages);

    public SuperSaverImage getSuperSaverImageByChecksum(String checksum);

    public List<SuperSaverImage> getSuperSaverImages();

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible);

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible, Boolean getMainImage);

    public List<SuperSaverImage> getSuperSaverImages(Product product, Boolean getVisible, Boolean getMainImage);

    public Page getSuperSaverImages(List<String> categories, List<String> brands, Boolean getVisible, int page, int perPage);
}
