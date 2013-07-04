package com.hk.api.impl.service;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.imaging.ImageUtils;
import com.akube.framework.util.BaseUtils;
import com.google.gson.Gson;
import com.hk.admin.util.S3Utils;
import com.hk.api.constants.EnumHKAPIErrorCode;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.dto.product.HKAPIProductDTO;
import com.hk.api.dto.product.HKAPIProductVariantDTO;
import com.hk.api.pact.service.HKAPIProductService;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.util.HKImageUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: Pradeep Date: 8/28/12 Time: 3:47 PM
 */
@Service
public class HKAPIProductServiceImpl implements HKAPIProductService {

	@Value ("#{hkEnvProps['" + Keys.Env.healthkartRestUrl + "']}")
	private String healthkartRestUrl;

	private static Logger logger = LoggerFactory.getLogger(HKAPIProductServiceImpl.class);

	@Autowired
	ProductDao productDao;

	@Autowired
	ProductService productService;


	@Value ("#{hkEnvProps['" + Keys.Env.accessKey + "']}")
	String awsAccessKey;

	@Value ("#{hkEnvProps['" + Keys.Env.secretKey + "']}")
	String awsSecretKey;

	@Value ("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminUploadsPath;

	@Value ("#{hkEnvProps['" + Keys.Env.readBucket + "']}")
	String hkReadBucket;

	private static final float QUALITY = 0.95F;
	private static final String mihAwsBucket = "mih-prod";

	private static final Integer pixelSize = 1024;

    public HKAPIBaseDTO getProductDetails(String productId){
        Product product=productService.getProductById(productId);
        HKAPIBaseDTO hkAPIBaseDto=new HKAPIBaseDTO();
        if(product!=null){
            HKAPIProductDTO productDTO=new HKAPIProductDTO();
            productDTO.setDeleted(product.isDeleted());
            productDTO.setOutOfStock(product.getOutOfStock());
            productDTO.setHidden(product.getHidden());
            productDTO.setProductID(product.getId());
            List<ProductVariant> productVariantList=product.getProductVariants();
            HKAPIProductVariantDTO[] productVariantDTOs = new HKAPIProductVariantDTO[productVariantList.size()];
            int i=0;
            for(ProductVariant variant:productVariantList){
	              HKAPIProductVariantDTO productVariantDTO = new HKAPIProductVariantDTO();
	              productVariantDTO.setProductVariantID(variant.getId());
                productVariantDTO.setHkDiscountPercent(variant.getDiscountPercent());
                productVariantDTO.setHkPrice(variant.getHkPrice());
                productVariantDTO.setMrp(variant.getMarkedPrice());
                productVariantDTO.setDeleted(variant.isDeleted());
                productVariantDTO.setOutOfStock(variant.isOutOfStock());
	              productVariantDTOs[i] = productVariantDTO;
	              i++;
            }
            productDTO.setProductVariantDTOs(productVariantDTOs);
            hkAPIBaseDto.setData(productDTO);
        }else {
            return new HKAPIBaseDTO(EnumHKAPIErrorCode.ProductDoesNotExist);
        }
        return hkAPIBaseDto;
    }

    public HKAPIBaseDTO getOOSHiddenDeletedProducts(){
       List<Product> products= productService.getOOSHiddenDeletedProducts();
       List<String> productIDs= new ArrayList<String>();
        for(Product product : products){
            productIDs.add(product.getId());
        }
        HKAPIBaseDTO baseDTO=new HKAPIBaseDTO();
        baseDTO.setData(productIDs);
        return baseDTO;
    }

	public Product getProductById(String productId) {
		try {
			ClientRequest request = new ClientRequest(healthkartRestUrl + "product/" + productId);
			request.accept(MediaType.APPLICATION_JSON);
			ClientResponse<String> response = request.get(String.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes())));
			Gson gson = JsonUtils.getGsonDefault();

			return gson.fromJson(br, Product.class);

		} catch (IOException e) {
			logger.error("IO exception gettting product" + productId, e);
		} catch (Exception e) {
			logger.error(" exception gettting product" + productId, e);
		}
		return null;
	}

	public String syncContentAndDescription() {
		List<Product> mihNutritionProducts = productDao.getAllProductByCategory("nutrition");
		List<Product> mihSportsProducts = productDao.getAllProductByCategory("sports");
		List<Product> mihProducts = mihNutritionProducts;
		for (Product product : mihSportsProducts) {
			mihProducts.add(product);
		}
		for (Product product : mihProducts) {
			try {
				ClientRequest request = new ClientRequest(healthkartRestUrl + "product/" + product.getId().toString());
				request.accept(MediaType.APPLICATION_JSON);
				ClientResponse<String> response = request.get(String.class);

				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes())));
				Gson gson = JsonUtils.getGsonDefault();

				Product hkProduct = gson.fromJson(br, Product.class);
				product.setDescription(hkProduct.getDescription());
				product.setOverview(hkProduct.getOverview());
				getProductService().save(product);

			} catch (IOException e) {
				logger.error(" exception syncing products" + product.getId(), e);
				return "sync failed for product id " + product.getId().toString();
			} catch (Exception e) {
				logger.error(" exception syncing products" + product.getId(), e);
				return "sync failed for product id" + product.getId().toString();

			}
		}
		return "synced";
	}

	public String syncProductImages() {
		List<Product> mihNutritionProducts = productDao.getAllProductByCategory("nutrition");
		List<Product> mihSportsProducts = productDao.getAllProductByCategory("sports");
		List<Product> mihProducts = mihNutritionProducts;
		for (Product product : mihSportsProducts) {
			mihProducts.add(product);
		}
		for (Product product : mihProducts) {
			try {
				ClientRequest request = new ClientRequest(healthkartRestUrl + "product/" + product.getId().toString());
				request.accept(MediaType.APPLICATION_JSON);
				ClientResponse<String> response = request.get(String.class);

				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes())));
				Gson gson = JsonUtils.getGsonDefault();

				Product hkProduct = gson.fromJson(br, Product.class);

				String imageFilePath = adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
				File imageFile = new File(imageFilePath);
				if (hkProduct.getMainImageId() != null) {
					if (product.getMainImageId() == null) {
						try {
							imageFile.getParentFile().mkdirs();
							S3Utils.downloadData(awsAccessKey, awsSecretKey, HKImageUtils.getS3ImageKey(EnumImageSize.Original, hkProduct.getMainImageId()), "healthkart-prod", imageFile);
							ProductImage productImage = setImage(imageFile, product, true, false);
							if (productImage != null) {
								resizeAndUpload(imageFile.getAbsolutePath(), productImage);
								productImage.setUploaded(true);
								getProductDao().save(productImage);
							}

						} finally {
							if (imageFile.exists()) imageFile.delete();
						}
					}
				}

			} catch (IOException e) {
				logger.error(" exception syncing products" + product.getId(), e);
			} catch (Exception e) {
				logger.error(" exception syncing products" + product.getId(), e);
			}
		}
		return "images synced";
	}

	private ProductImage setImage(File imageFile, Product product, boolean defaultImage, boolean checkExists) {
		String checksum = BaseUtils.getMD5Checksum(imageFile);
		ProductImage productImage = getProductService().getProductImageByChecksum(checksum);
		if (productImage != null && productImage.isUploaded() && checkExists) {
			return null;
		} else {
			if (productImage == null) {
				productImage = new ProductImage();
				productImage.setProduct(product);
				productImage.setUrl(product.getId());
				productImage.setAltText(product.getName());
				productImage.setChecksum(checksum);
				productImage = (ProductImage) getProductDao().save(productImage);
			}

			if (defaultImage) {
				product.setMainImageId(productImage.getId());
			}

			return productImage;
		}
	}

	private void resizeAndUpload(String filePath, ProductImage productImage) throws Exception {

		String repositoryFilePath = null;

		Long id = productImage.getId();

		// saving original image
		String imageUrl = HKImageUtils.getS3ImageKey(EnumImageSize.Original, id);
		S3Utils.uploadData(awsAccessKey, awsSecretKey, filePath, imageUrl, mihAwsBucket);

		// saving thumbnails for all sizes
		for (EnumImageSize enumImageSize : EnumImageSize.values()) {
			if (enumImageSize != EnumImageSize.Original) {
				repositoryFilePath = HKImageUtils.getRepositoryImagePath(enumImageSize, id);
				ImageUtils.createThumbnail(filePath, repositoryFilePath, enumImageSize.getDimension(), QUALITY, false, false, .5F);
				imageUrl = HKImageUtils.getS3ImageKey(enumImageSize, id);
				S3Utils.uploadData(awsAccessKey, awsSecretKey, repositoryFilePath, imageUrl, mihAwsBucket);
			}
		}
	}

	@SuppressWarnings("unchecked")
    public String getProductsWithLowResolutionImages() {
		List<Product> nonDeletedProducts = getProductService().getAllNonDeletedProducts();
		List<Product> productsWithLowResolutionImages = new ArrayList<Product>();
		StringBuilder productIdsForLowResolutionImages = new StringBuilder("");
		File txtFile = new File(adminUploadsPath + "/imageFiles/temp/lowResProd.txt");
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter(txtFile));

			logger.info("--- Low Resolution Finder START ---");
			String lineSeparator = System.getProperty("line.separator");
			printWriter.write("ProductID || CATEGORY" + lineSeparator);
			int counter = 0;
			for (Product product : nonDeletedProducts) {
				if (product.getMainImageId() != null) {
					File imageFile = new File(getImageFilePath());
					S3Utils.downloadData(awsAccessKey, awsSecretKey, HKImageUtils.getS3ImageKey(EnumImageSize.Original, product.getMainImageId()), hkReadBucket, imageFile);
					try {
						ImageInputStream in = ImageIO.createImageInputStream(imageFile);
						try {
							final Iterator readers = ImageIO.getImageReaders(in);
							if (readers.hasNext()) {
								ImageReader reader = (ImageReader) readers.next();
								try {
									reader.setInput(in);
									if (reader.getWidth(0) < pixelSize && reader.getHeight(0) < pixelSize) {
										productsWithLowResolutionImages.add(product);
										productIdsForLowResolutionImages.append(product.getId() + "," + product.getPrimaryCategory() + ";");
										counter++;
										logger.info("Low Resolution ProductID = " + product.getId()+"; Count="+counter);
										printWriter.write(product.getId() + " || " + product.getPrimaryCategory().getDisplayName() + lineSeparator);
									}
									//return new Dimension(reader.getWidth(0), reader.getHeight(0));
								} finally {
									reader.dispose();
								}
							}
						} finally {
							if (in != null) in.close();
							if (imageFile.exists()) {
								imageFile.delete();
							}
						}
					} catch (Exception e) {
					   e.printStackTrace();
					}

				}
			}
			logger.info("--- Low Resolution Finder END ---");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(printWriter);
		}
		return productIdsForLowResolutionImages.toString();
	}

   public String downloadResizeAndUploadImages(String srcImageSize, String targetImageSize){
     StringBuffer stringBuffer = new StringBuffer("IDs:");
     List<Product> productList = getProductService().getAllNonDeletedProducts();
     for (Product product : productList) {
       String resp = downloadResizeAndUploadImage(product, srcImageSize, targetImageSize);
       stringBuffer.append(resp);
     }
    return stringBuffer.toString();
   }

  public String downloadResizeAndUploadImage(String productId, String srcImageSize, String targetImageSize) {
    Product product = getProductById(productId);
    return downloadResizeAndUploadImage(product, srcImageSize, targetImageSize);
  }


  private String downloadResizeAndUploadImage(Product product, String srcImageSize, String targetImageSize) {
    try {
      EnumImageSize srcEnumImageSize = EnumImageSize.getEnumImageSize(srcImageSize);
      EnumImageSize targetEnumImageSize = EnumImageSize.getEnumImageSize(targetImageSize);

      if (product != null &&  product.getMainImageId() != null && srcEnumImageSize != null && targetEnumImageSize != null) {

        String imageFilePath = adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
        File imageFile = new File(imageFilePath);

        imageFile.getParentFile().mkdirs();
        S3Utils.downloadData(awsAccessKey, awsSecretKey, HKImageUtils.getS3ImageKey(srcEnumImageSize, product.getMainImageId()), hkReadBucket, imageFile);


        String repositoryFilePath = HKImageUtils.getRepositoryImagePath(targetEnumImageSize, product.getMainImageId());
        ImageUtils.createThumbnail(imageFilePath, repositoryFilePath, targetEnumImageSize.getDimension(), QUALITY, false, false, .5F);
        String imageUrl = HKImageUtils.getS3ImageKey(targetEnumImageSize, product.getMainImageId());
        S3Utils.uploadData(awsAccessKey, awsSecretKey, repositoryFilePath, imageUrl, hkReadBucket);
        logger.debug("Resized Image for Product="+product.getId());
        return "SUCCESS:"+product.getId();
      }else{
        return "FAILED - Incorrect Values";
      }
    } catch (Exception e) {
      return "FAILED - Exception: " + e.getMessage();
    }
  }

  private String getImageFilePath() {
		return adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
	}


	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
