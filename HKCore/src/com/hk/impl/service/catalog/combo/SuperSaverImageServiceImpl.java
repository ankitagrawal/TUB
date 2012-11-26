package com.hk.impl.service.catalog.combo;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.pact.dao.catalog.combo.SuperSaverImageDao;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.util.ProductUtil;

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
    List<SuperSaverImage> superSaverImages = superSaverImageDao.getSuperSaverImages(product, getVisible, getMainImage, getDeleted);
    Iterator<SuperSaverImage> superSaverImageIterator = superSaverImages.listIterator();
    while (superSaverImageIterator.hasNext()) {
      SuperSaverImage superSaverImage = superSaverImageIterator.next();
      if (!ProductUtil.isDisplayedInResults(superSaverImage)) {
        superSaverImageIterator.remove();
      }
    }
    return superSaverImages;
  }

  @SuppressWarnings("unchecked")
public Page getSuperSaverImages(List<String> categories, List<String> brands, Boolean getVisible, Boolean getDeleted, int page, int perPage) {
    Page superSaverPage;
    List<SuperSaverImage> superSaverImages = superSaverImageDao.getSuperSaverImages(categories, brands, getVisible, getDeleted);

    Iterator<SuperSaverImage> superSaverImageIterator = superSaverImages.iterator();
    while (superSaverImageIterator.hasNext()) {
      SuperSaverImage superSaverImage = superSaverImageIterator.next();
      if (!ProductUtil.isDisplayedInResults(superSaverImage)) {
        superSaverImageIterator.remove();
      }
    }

    if (superSaverImages.size() > 0) {
      int firstResult = (page - 1) * perPage;
      int lastResult = Math.min(firstResult + perPage, superSaverImages.size());
      if (firstResult <= lastResult) {
        List resultList = superSaverImages.subList(firstResult, lastResult);

        superSaverPage = new Page(resultList, perPage, page, superSaverImages.size());
      } else {
        superSaverPage = new Page(Collections.EMPTY_LIST, perPage, page, superSaverImages.size());
      }
    } else {
      superSaverPage = new Page(Collections.EMPTY_LIST, perPage, page, superSaverImages.size());
    }
    return superSaverPage;
  }
}
