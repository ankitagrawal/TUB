package com.hk.api.pact.service;

import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.domain.catalog.product.Product;
import com.hk.constants.catalog.image.EnumImageSize;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 8/28/12
 * Time: 3:46 PM
 */
public interface HKAPIProductService {

  public HKAPIBaseDTO getProductDetails(String productId);

  public Product getProductById(String ProductId);

  public String syncContentAndDescription();

  public String syncProductImages();

  public String getProductsWithLowResolutionImages();

  public HKAPIBaseDTO getOOSHiddenDeletedProducts();

  public String downloadResizeAndUploadImage(String productId, String srcImageSize, String targetImageSize);

}
