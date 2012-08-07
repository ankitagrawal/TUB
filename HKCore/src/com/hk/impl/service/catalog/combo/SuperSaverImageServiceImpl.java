package com.hk.impl.service.catalog.combo;

import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.pact.dao.catalog.combo.SuperSaverImageDao;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.domain.catalog.product.Product;
import com.akube.framework.dao.Page;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SuperSaverImageServiceImpl implements SuperSaverImageService {

    @Autowired
    SuperSaverImageDao superSaverImageDao;

    public SuperSaverImage saveSuperSaverImage(SuperSaverImage superSaverImage) {
        return (SuperSaverImage) superSaverImageDao.save(superSaverImage);
    }

    public void saveSuperSaverImages(List<SuperSaverImage> superSaverImages) {
        superSaverImageDao.saveOrUpdate(superSaverImages);
    }

    public SuperSaverImage getSuperSaverImageByChecksum(String checksum) {
        return superSaverImageDao.getSuperSaverImageByChecksum(checksum);
    }

    public List<SuperSaverImage> getSuperSaverImages() {
        return superSaverImageDao.getSuperSaverImages();
    }

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible) {
        return superSaverImageDao.getSuperSaverImages(getVisible);
    }

    public List<SuperSaverImage> getSuperSaverImages(Boolean getVisible, Boolean getMainImage) {
        return superSaverImageDao.getSuperSaverImages(getVisible, getMainImage);
    }

    public List<SuperSaverImage> getSuperSaverImages(Product product, Boolean getVisible, Boolean getMainImage, Boolean getDeleted) {
        return superSaverImageDao.getSuperSaverImages(product, getVisible, getMainImage, getDeleted);
    }

    public Page getSuperSaverImages(List<String> categories, List<String> brands, Boolean getVisible, Boolean getDeleted, int page, int perPage) {
        return superSaverImageDao.getSuperSaverImages(categories, brands, getVisible, getDeleted, page, perPage);
    }
}
