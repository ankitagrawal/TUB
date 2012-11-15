package com.hk.rest.impl.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.imaging.ImageUtils;
import com.akube.framework.util.BaseUtils;
import com.google.gson.Gson;
import com.hk.admin.util.S3Utils;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.rest.pact.service.APIProductService;
import com.hk.util.HKImageUtils;
import com.hk.util.ImageManager;

/**
 * Created with IntelliJ IDEA. User: Pradeep Date: 8/28/12 Time: 3:47 PM
 */
@Service
public class APIProductServiceImpl implements APIProductService {

	@Value ("#{hkEnvProps['" + Keys.Env.healthkartRestUrl + "']}")
	private String healthkartRestUrl;

	private static Logger logger = LoggerFactory.getLogger(APIProductServiceImpl.class);

	@Autowired
	ProductDao productDao;

	@Autowired
	ImageManager imageManager;

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
				productDao.save(product);

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

	private String getImageFilePath() {
		return adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
	}


	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public ImageManager getImageManager() {
		return imageManager;
	}

	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
