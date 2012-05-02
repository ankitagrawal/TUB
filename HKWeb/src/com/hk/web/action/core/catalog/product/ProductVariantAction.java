package com.hk.web.action.core.catalog.product;

import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.dao.catalog.product.ProductVariantDao;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.util.ImageManager;
import com.hk.web.HealthkartResponse;

@Component
public class ProductVariantAction extends BaseAction {
  private static Logger logger = Logger.getLogger(ProductAction.class);

  ProductVariant productVariant;
  ProductVariant productVariant2;
  List<ProductImage> productImages;
  String mainImageId;
  String mainProductImageId;

   ProductVariantDao productVariantDao;
   ImageManager imageManager;

  public Resolution renderManageImages() {
    logger.debug("varaintId: " + productVariant.getId());
    productImages = productVariant.getProductImages();
    mainImageId = productVariant.getMainImageId() != null ? productVariant.getMainImageId().toString() : "";
    mainProductImageId = productVariant.getMainProductImageId() != null ? productVariant.getMainProductImageId().toString() : "";
    return new RedirectResolution(ProductVariantAction.class, "manageVariantImages").addParameter("productVariant", productVariant);
  }

  public Resolution manageVariantImages() {
    logger.debug("varaintId: " + productVariant.getId());
    productImages = productVariant.getProductImages();
    mainImageId = productVariant.getMainImageId() != null ? productVariant.getMainImageId().toString() : "";
    mainProductImageId = productVariant.getMainProductImageId() != null ? productVariant.getMainProductImageId().toString() : "";
    return new ForwardResolution("/pages/manageVariantImages.jsp");
  }

  public Resolution editImageSettings() {
    logger.debug("varaintId: " + productVariant2.getId());
    if (productImages != null) {
      for (ProductImage productImage : productImages) {
        logger.debug("productImageId : " + productImage.getId());
        productVariantDao.save(productImage);
      }
    }
    //checking that mainImageId should not be null , otherwise may throw exception.
    if (mainImageId != null){
      productVariant2.setMainImageId(Long.parseLong(mainImageId));
    }
    if(mainProductImageId != null){
      productVariant2.setMainProductImageId(Long.parseLong(mainProductImageId));
    }
    productVariantDao.save(productVariant2);
    addRedirectAlertMessage(new SimpleMessage("Changes Saved"));
    return new RedirectResolution(ProductVariantAction.class, "manageVariantImages").addParameter("productVariant", productVariant2);
  }

  public Resolution changeProductLink(){
    if(mainProductImageId != null) {
     String productImageLink = ImageManager.getS3ImageUrl(EnumImageSize.MediumSize, Long.parseLong(mainProductImageId));
     HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", productImageLink);
     return new JsonResolution(healthkartResponse);
    } else{
      HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "no image set -fail", "");
      return new JsonResolution(healthkartResponse);
    }
  }

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public ProductVariant getProductVariant2() {
    return productVariant2;
  }

  public void setProductVariant2(ProductVariant productVariant2) {
    this.productVariant2 = productVariant2;
  }

  public List<ProductImage> getProductImages() {
    return productImages;
  }

  public void setProductImages(List<ProductImage> productImages) {
    this.productImages = productImages;
  }

  public String getMainImageId() {
    return mainImageId;
  }

  public void setMainImageId(String mainImageId) {
    this.mainImageId = mainImageId;
  }

  public String getMainProductImageId() {
    return mainProductImageId;
  }

  public void setMainProductImageId(String mainProductImageId) {
    this.mainProductImageId = mainProductImageId;
  }
}
