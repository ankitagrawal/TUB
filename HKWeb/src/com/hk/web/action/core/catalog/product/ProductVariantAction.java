package com.hk.web.action.core.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.service.image.ProductImageService;
import com.hk.util.HKImageUtils;
import com.hk.util.ImageManager;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductVariantAction extends BaseAction {
	private static Logger logger = Logger.getLogger(ProductAction.class);

	ProductVariant productVariant;
	ProductVariant productVariant2;
	List<ProductImage> productImages;
	String mainImageId;
	String mainProductImageId;
	Long imageType;
	@Autowired
	ProductVariantDao productVariantDao;
	@Autowired
	ImageManager imageManager;
	@Autowired
	ProductImageService productImageService;

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
		// checking that mainImageId should not be null , otherwise may throw exception.
		if (mainImageId != null) {
			productVariant2.setMainImageId(Long.parseLong(mainImageId));
		}
		if (mainProductImageId != null) {
			productVariant2.setMainProductImageId(Long.parseLong(mainProductImageId));
		}
		productVariantDao.save(productVariant2);
		addRedirectAlertMessage(new SimpleMessage("Changes Saved"));
		return new RedirectResolution(ProductVariantAction.class, "manageVariantImages").addParameter("productVariant", productVariant2);
	}

	public Resolution changeProductLink() {
		if (mainProductImageId != null) {
			String productImageLink = HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, Long.parseLong(mainProductImageId));
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", productImageLink);
			return new JsonResolution(healthkartResponse);
		} else {
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "no image set -fail", "");
			return new JsonResolution(healthkartResponse);
		}
	}

	public Resolution searchImages(){
		productImages = productImageService.searchProductImages(imageType, productVariant.getProduct(), productVariant, true, false);
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", productImages);
		return new JsonResolution(healthkartResponse);
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

	public Long getImageType() {
		return imageType;
	}

	public void setImageType(Long imageType) {
		this.imageType = imageType;
	}
}
