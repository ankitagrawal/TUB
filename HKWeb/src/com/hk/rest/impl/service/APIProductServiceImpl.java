package com.hk.rest.impl.service;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.imaging.ImageUtils;
import com.akube.framework.util.BaseUtils;
import com.google.gson.Gson;
import com.hk.admin.util.S3Utils;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.rest.pact.service.APIProductService;
import com.hk.util.HKImageUtils;
import com.hk.util.ImageManager;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 8/28/12
 * Time: 3:47 PM
 */
@Service
public class APIProductServiceImpl implements APIProductService {


    @Value("#{hkEnvProps['" + Keys.Env.healthkartRestUrl + "']}")
    private String        healthkartRestUrl;


    @Autowired
    ProductDao productDao;

    @Autowired
    ImageManager imageManager;

    @Autowired
    ProductService productService;

    @Value("#{hkEnvProps['" + Keys.Env.accessKey + "']}")
    String awsAccessKey;

    @Value("#{hkEnvProps['" + Keys.Env.secretKey + "']}")
    String awsSecretKey;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String         adminUploadsPath;

    private static final float QUALITY = 0.95F;
    private static final String mihAwsBucket="mih-prod";

    public Product getProductById(String productId){
        try {
            ClientRequest request = new ClientRequest(healthkartRestUrl+"product/"+productId);
            request.accept(MediaType.APPLICATION_JSON);
            ClientResponse<String> response = request.get(String.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(response.getEntity().getBytes())));
            Gson gson= JsonUtils.getGsonDefault();

            return gson.fromJson(br, Product.class);

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    public String syncContentAndDescription(){
        List<Product> mihNutritionProducts=productDao.getAllProductByCategory("nutrition");
        List<Product> mihSportsProducts=productDao.getAllProductByCategory("sports");
        List<Product> mihProducts=mihNutritionProducts;
        for(Product product:mihSportsProducts){
            mihProducts.add(product);
        }
        for(Product product:mihProducts){
            try {
                ClientRequest request = new ClientRequest(healthkartRestUrl+"product/"+product.getId().toString());
                request.accept(MediaType.APPLICATION_JSON);
                ClientResponse<String> response = request.get(String.class);

                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + response.getStatus());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new ByteArrayInputStream(response.getEntity().getBytes())));
                Gson gson= JsonUtils.getGsonDefault();

                Product hkProduct= gson.fromJson(br, Product.class);
                product.setDescription(hkProduct.getDescription());
                product.setOverview(hkProduct.getOverview());
                productDao.save(product);

            } catch (IOException e) {

                e.printStackTrace();
                return "sync failed for product id "+product.getId().toString();

            } catch (Exception e) {

                e.printStackTrace();
                return "sync failed for product id"+product.getId().toString();

            }
        }
        return "synced";
    }

    public String syncProductImages(){
        List<Product> mihNutritionProducts=productDao.getAllProductByCategory("nutrition");
        List<Product> mihSportsProducts=productDao.getAllProductByCategory("sports");
        List<Product> mihProducts=mihNutritionProducts;
        for(Product product:mihSportsProducts){
            mihProducts.add(product);
        }
        for(Product product:mihProducts){
            try {
                ClientRequest request = new ClientRequest(healthkartRestUrl+"product/"+product.getId().toString());
                request.accept(MediaType.APPLICATION_JSON);
                ClientResponse<String> response = request.get(String.class);

                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + response.getStatus());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new ByteArrayInputStream(response.getEntity().getBytes())));
                Gson gson= JsonUtils.getGsonDefault();

                Product hkProduct= gson.fromJson(br, Product.class);

                String imageFilePath = adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
                File imageFile = new File(imageFilePath);
                if(hkProduct.getMainImageId()!=null){
                    if(product.getMainImageId()==null){
                        try {
                            imageFile.getParentFile().mkdirs();
                            S3Utils.downloadData(awsAccessKey,awsSecretKey, HKImageUtils.getS3ImageKey(EnumImageSize.Original, hkProduct.getMainImageId()),"healthkart-prod",imageFile);
                            ProductImage productImage=setImage(imageFile, product, true, false);
                            if (productImage != null) {
                                resizeAndUpload(imageFile.getAbsolutePath(), productImage);
                                productImage.setUploaded(true);
                                getProductDao().save(productImage);
                            }

                        } finally {
                            if (imageFile.exists())
                                imageFile.delete();
                        }
                    }
                }

            } catch (IOException e) {

                e.printStackTrace();
                //return "image sync failed for product id "+product.getId().toString();

            } catch (Exception e) {

                e.printStackTrace();
                //return "image sync failed for product id"+product.getId().toString();

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
