package com.hk.util;

import com.akube.framework.imaging.ImageUtils;
import com.akube.framework.util.BaseUtils;
import com.hk.admin.util.S3Utils;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.category.CategoryImage;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.category.CategoryImageDao;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

@Component
public class ImageManager {

    private static Logger logger = Logger.getLogger(ImageManager.class);

    @Value("#{hkEnvProps['" + Keys.Env.accessKey + "']}")
    String awsAccessKey;

    @Value("#{hkEnvProps['" + Keys.Env.secretKey + "']}")
    String awsSecretKey;

    ImageTagReader imageTagReader;

    @Autowired
    ComboDao comboDao;
    @Autowired
    private SuperSaverImageService superSaverImageService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private ProductVariantService productVariantService;

    /*
    * ComboImageDao comboImageDao;
    */

    // S3Utils s3Utils;
    // ImageUtils imageUtils;
    @Autowired
    CategoryImageDao categoryImageDao;
    private static final float QUALITY = 0.95F;

    //    static String                 awsReadBucket = "hk-prod";
    //    static String                 awsBucket     = "hk-prod";
    @Value("#{hkEnvProps['" + Keys.Env.bucket + "']}")
    String uploadBucket;

    @Value("#{hkEnvProps['" + Keys.Env.readBucket + "']}")
    String downloadBucket;

    static String awsBucket;
    static String awsReadBucket;

    @PostConstruct
    public void postConstruction() {
        awsBucket = StringUtils.isNotBlank(uploadBucket) ? uploadBucket : "";
        awsReadBucket = StringUtils.isNotBlank(downloadBucket) ? downloadBucket : "";
    }

    /**
     * This method uploads the image on S3 and adds it to db. If the productId is null, it reads the tag and uploads.
     * otherwise uploads directly.
     *
     * @param imageFile Should be a jpeg file.
     * @param product   Can be null
     * @return
     * @throws Exception
     */
    public EnumS3UploadStatus uploadProductFile(File imageFile, Product product, boolean checkExists) throws Exception {
        logger.debug("imageFilePath : " + imageFile.getAbsolutePath());
        if (product == null) {
            product = findProductbyImageName(imageFile.getName());
            if (product != null) {
                logger.info("uploading image for productId : " + product.getId());
            } else {
                logger.info("no such product - " + imageFile.getName());
            }
        } else {
            logger.debug("productId : " + product.getId());
        }
        // if product is null even after reading tags, then message that the product was not found.
        if (product == null) {
            return EnumS3UploadStatus.NotFound;
        } else {
            // check is the product has a null image
            boolean setDefault = false;
            if (product.getProductImages() == null || product.getProductImages().isEmpty()) {
                setDefault = true;
            }

            ProductImage productImage = setImage(imageFile, product, setDefault, checkExists);

            if (productImage != null) {
                resizeAndUpload(imageFile.getAbsolutePath(), productImage);
                productImage.setUploaded(true);
                getBaseDao().save(productImage);

                if (imageFile.exists()) {
                    logger.debug("deleting : " + imageFile.getAbsolutePath());
                    imageFile.delete();
                }

                return EnumS3UploadStatus.Uploaded;
            } else {
                return EnumS3UploadStatus.Checked;
            }
        }
    }

    public EnumS3UploadStatus uploadCategoryFile(File imageFile, Category category, boolean checkExists) throws Exception {
        logger.debug("imageFilePath : " + imageFile.getAbsolutePath());

        if (category == null) {
            return EnumS3UploadStatus.NotFound;
        } else {
            // check is the product has a null image
            boolean setDefault = false;
            if (category.getCategoryImages() == null || category.getCategoryImages().isEmpty()) {
                setDefault = true;
            }

            CategoryImage categoryImage = setCategoryImage(imageFile, category, setDefault, checkExists);

            if (categoryImage != null) {
                resizeAndUpload(imageFile.getAbsolutePath(), categoryImage);
                categoryImage.setUploaded(true);
                categoryImageDao.save(categoryImage);
                return EnumS3UploadStatus.Uploaded;
                // imageFile.delete();
            } else {
                return EnumS3UploadStatus.Checked;
            }
        }
    }

    public EnumS3UploadStatus uploadSuperSaverFile(File imageFile, boolean checkExists) {
        SuperSaverImage superSaverImage = setSuperSaverImage(imageFile, checkExists);

        if (superSaverImage != null) {
            resizeAndUpload(imageFile.getAbsolutePath(), superSaverImage);
            superSaverImage.setUploaded(true);
            getSuperSaverImageService().saveSuperSaverImage(superSaverImage);

            if (imageFile.exists()) {
                logger.debug("deleting : " + imageFile.getAbsolutePath());
                imageFile.delete();
            }

            return EnumS3UploadStatus.Uploaded;
        } else {
            return EnumS3UploadStatus.Checked;
        }
    }

    /*
    * public EnumS3UploadStatus uploadComboFile(File imageFile, Combo combo, boolean checkExists) throws Exception {
    * logger.debug("imageFilePath : " + imageFile.getAbsolutePath()); if (combo == null) { logger.debug("comboId : " +
    * ""); combo = findCombo(imageFile.getAbsolutePath()); } else { logger.debug("comboId : " + combo.getId()); } //if
    * product is null even after reading tags, then message that the product was not found. if (combo == null) { return
    * EnumS3UploadStatus.NotFound; } else { // check is the product has a null image boolean setDefault = false; if
    * (combo.getComboImages() == null || combo.getComboImages().isEmpty()) { setDefault = true; } ComboImage comboImage =
    * setImage(imageFile, combo, setDefault, checkExists); if (comboImage != null) {
    * resizeAndUpload(imageFile.getAbsolutePath(), comboImage); comboImage.setUploaded(true);
    * comboImageDao.save(comboImage); return EnumS3UploadStatus.Uploaded; // imageFile.delete(); } else { return
    * EnumS3UploadStatus.Checked; } } }
    */
    public EnumS3UploadStatus uploadProductVariantFile(File imageFile, ProductVariant productVariant) throws Exception {
        logger.debug("imageFilePath : " + imageFile.getAbsolutePath());
        if (productVariant == null) {
            logger.debug("varaintId : " + "");
            // productVariant = findProductVariant(imageFile.getAbsolutePath());
            productVariant = findProductVariantbyImageName(imageFile.getName());
        } else {
            logger.debug("varaintId : " + productVariant.getId());
        }
        // if product is null even after reading tags, then message that the product was not found.
        if (productVariant == null) {
            return EnumS3UploadStatus.NotFound;
        } else {
            // check is the product has a null image
            boolean setDefault = false;
            if (productVariant.getProductImages() == null || productVariant.getProductImages().isEmpty()) {
                setDefault = true;
            }

            ProductImage productImage = setImage(imageFile, productVariant, setDefault, false);
            if (productImage != null) {
                BufferedImage image = ImageIO.read(imageFile);
                productImage.setWidth((long) image.getWidth());
                productImage.setHeight((long) image.getHeight());

                resizeAndUpload(imageFile.getAbsolutePath(), productImage);
                productImage.setUploaded(true);
                getBaseDao().save(productImage);
                return EnumS3UploadStatus.Uploaded;
                // imageFile.delete();
            } else {
                return EnumS3UploadStatus.Checked;
            }
        }
    }

    /**
     * uploads all the images of the directory to S3 by reading tag and them to db. If an image is already uploaded,
     * then doesn't upload it again.
     *
     * @param directory should be a directory containing jpeg files only
     * @return
     * @throws Exception
     */
    public Long bulkUploadProduct(File directory) throws Exception {
        Long count = 0L;
        if (!directory.isDirectory()) {
            logger.info("its a directory");
        } else {
            logger.info("directory name: " + directory.getName());
            logger.info("directory path: " + directory.getAbsolutePath());
            File[] fileList = directory.listFiles();
            if (fileList != null)
                logger.info("file list size: " + fileList.length);

            for (File file : fileList) {
                logger.info("file name: " + file.getName());
                logger.info("file path: " + file.getAbsolutePath());
                if (file.isFile()) {
                    logger.info("yes its a file - uploading image");
                    EnumS3UploadStatus status = uploadProductFile(file, null, true);// true to avoid re-upload of
                    // already uploaded images
                    logger.info("came back - " + status.getMessage());
                    if (status == EnumS3UploadStatus.Uploaded) {
                        count++;
                        logger.info("uploaded image#" + count);
                    }
                }
            }
        }
        return count;
    }

    public Long bulkUploadProductVariant(File directory) throws Exception {
        Long count = 0L;
        if (!directory.isDirectory()) {
            logger.info("testing 1");
        } else {
            logger.info("directory name" + directory.getName());
            logger.info("directory path" + directory.getAbsolutePath());
            File[] fileList = directory.listFiles();

            logger.info("testing 2");
            if (fileList != null)
                logger.info("file list size " + fileList.length);

            for (File file : fileList) {

                logger.info("file path" + file.getName());
                logger.info("file path" + file.getAbsolutePath());

                if (file.isFile()) {
                    logger.info("testing 3");
                    EnumS3UploadStatus status = uploadProductVariantFile(file, null);
                    logger.info("testing 4");
                    if (status == EnumS3UploadStatus.Uploaded)
                        logger.info("testing 5");
                    count++;
                }

            }
        }
        return count;
    }

    /**
     * This method is called by upload method to read tags of the file , and look for productId of some product. The
     * method returns the product whose id is among the tags. if no such product exists, null is returned.
     *
     * @param imageFilePath should be path to a jpeg file.
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private Product findProduct(String imageFilePath) throws Exception {
        Set<String> tags = imageTagReader.readImageMetadata(imageFilePath);
        for (String keyword : tags) {
            logger.info(keyword);
            Product product = getProductService().getProductById(keyword);
            if (product != null) {
                return product;
            }
        }
        return null;
    }

    private Product findProductbyImageName(String fileName) throws Exception {
        String productId = BaseUtils.getFilenameWithoutExtension(fileName);
        logger.info("product id derived from image name: " + productId);
        Product product = getProductService().getProductById(productId);
        if (product != null) {
            return product;
        }
        return null;
    }

    private ProductVariant findProductVariantbyImageName(String fileName) throws Exception {
        String productVariantId = BaseUtils.getFilenameWithoutExtension(fileName);
        logger.info("uploading product variant image where product variant id is " + productVariantId);
        ProductVariant productVariant = getProductVariantService().getVariantById(productVariantId);
        if (productVariant != null) {
            logger.info("productVariant " + productVariant.getId());
            return productVariant;
        }
        return null;
    }

    @SuppressWarnings("unused")
    private Combo findCombo(String imageFilePath) throws Exception {
        Set<String> tags = imageTagReader.readImageMetadata(imageFilePath);
        for (String keyword : tags) {
            Combo combo = getBaseDao().get(Combo.class, keyword);
            if (combo != null) {
                return combo;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    private ProductVariant findProductVariant(String imageFilePath) throws Exception {
        Set<String> tags = imageTagReader.readImageMetadata(imageFilePath);
        for (String keyword : tags) {
            ProductVariant productVariant = getProductVariantService().getVariantById(keyword);
            if (productVariant != null) {
                return productVariant;
            }
        }
        return null;
    }

    /**
     * This method checks for the checksum of the image in DB. If image already exists there ,null is returned. Else a
     * ProductImage object is created using imageFile, and it's attributes are set based on the product, and then it is
     * returned. This method is called by upload method.
     *
     * @param imageFile should be a jpeg file.
     * @param product
     * @return
     */
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
                productImage = (ProductImage) getBaseDao().save(productImage);
            }

            if (defaultImage) {
                product.setMainImageId(productImage.getId());
            }

            return productImage;
        }
    }

    private CategoryImage setCategoryImage(File imageFile, Category category, boolean defaultImage, boolean checkExists) {
        String checksum = BaseUtils.getMD5Checksum(imageFile);
        CategoryImage categoryImage = categoryImageDao.getCategoryImageByChecksum(checksum);
        if (categoryImage != null && categoryImage.isUploaded() && checkExists) {
            return null;
        } else {
            if (categoryImage == null) {
                categoryImage = new CategoryImage();
                categoryImage.setCategory(category);
                categoryImage.setUrl(category.getName());
                categoryImage.setAltText(category.getName());
                categoryImage.setChecksum(checksum);
                categoryImage = (CategoryImage) categoryImageDao.save(categoryImage);
            }

            return categoryImage;
        }
    }

    private SuperSaverImage setSuperSaverImage(File imageFile, boolean checkExists) {
        String checksum = BaseUtils.getMD5Checksum(imageFile);
        SuperSaverImage superSaverImage = getSuperSaverImageService().getSuperSaverImageByChecksum(checksum);
        if (superSaverImage != null && superSaverImage.isUploaded() && checkExists) {
            return null;
        } else {
            if (superSaverImage == null) {
                superSaverImage = new SuperSaverImage();
                superSaverImage.setUrl("");
                superSaverImage.setChecksum(checksum);
                superSaverImage = getSuperSaverImageService().saveSuperSaverImage(superSaverImage);
            }
            return superSaverImage;
        }
    }

    /*
    * private ComboImage setImage(File imageFile, Combo combo, boolean defaultImage, boolean checkExists) { String
    * checksum = BaseUtils.getMD5Checksum(imageFile); ComboImage comboImage =
    * comboImageDao.getComboImageByChecksum(checksum); if (comboImage != null && comboImage.isUploaded() &&
    * checkExists) { return null; } else { if (comboImage == null) { comboImage = new ComboImage();
    * comboImage.setCombo(combo); comboImage.setUrl(combo.getId()); comboImage.setAltText(combo.getName());
    * comboImage.setChecksum(checksum); comboImage = comboImageDao.save(comboImage); } if (defaultImage) {
    * combo.setMainImageId(comboImage.getId()); } return comboImage; } }
    */

    private ProductImage setImage(File imageFile, ProductVariant productVariant, boolean defaultImage, boolean checkExists) {
        String checksum = BaseUtils.getMD5Checksum(imageFile);
        ProductImage productImage = getProductService().getProductImageByChecksum(checksum);
        if (productImage != null && productImage.isUploaded() && checkExists) {
            return null;
        } else {
            if (productImage == null) {
                productImage = new ProductImage();
                productImage.setProduct(productVariant.getProduct());
                productImage.setProductVariant(productVariant);
                productImage.setUrl(productVariant.getId());
                productImage.setAltText(productVariant.getColorOptionsValue());
                productImage.setChecksum(checksum);
                getBaseDao().save(productImage);
            }
            productImage = getProductService().getProductImageByChecksum(checksum);
            if (defaultImage) {
                productVariant.setMainImageId(productImage.getId());
            }
            return productImage;
        }
    }

    /**
     * This method resizes the image at filePath in all the sizes and upload all of them on S3 on appropriate URLs. This
     * method is called by upload method
     *
     * @param filePath     should be path to a jpeg file
     * @param productImage
     * @throws Exception
     */
    private void resizeAndUpload(String filePath, ProductImage productImage) throws Exception {

        String repositoryFilePath = null;

        Long id = productImage.getId();

        // saving original image
        String imageUrl = HKImageUtils.getS3ImageKey(EnumImageSize.Original, id);
        S3Utils.uploadData(awsAccessKey, awsSecretKey, filePath, imageUrl, awsBucket);

        // saving thumbnails for all sizes
        for (EnumImageSize enumImageSize : EnumImageSize.values()) {
            if (enumImageSize != EnumImageSize.Original) {
                repositoryFilePath = HKImageUtils.getRepositoryImagePath(enumImageSize, id);
                ImageUtils.createThumbnail(filePath, repositoryFilePath, enumImageSize.getDimension(), QUALITY, false, false, .5F);
                imageUrl = HKImageUtils.getS3ImageKey(enumImageSize, id);
                S3Utils.uploadData(awsAccessKey, awsSecretKey, repositoryFilePath, imageUrl, awsBucket);
            }
        }
    }

    private void resizeAndUpload(String filePath, CategoryImage categoryImage) throws Exception {

        String repositoryFilePath = null;

        Long id = categoryImage.getId();

        // saving original image
        String imageUrl = HKImageUtils.getS3CategoryImageKey(EnumImageSize.Original, id);
        S3Utils.uploadData(awsAccessKey, awsSecretKey, filePath, imageUrl, awsBucket);

        // saving thumbnails for all sizes
        for (EnumImageSize enumImageSize : EnumImageSize.values()) {
            if (enumImageSize != EnumImageSize.Original) {
                repositoryFilePath = HKImageUtils.getRepositoryImagePath(enumImageSize, id);
                ImageUtils.createThumbnail(filePath, repositoryFilePath, enumImageSize.getDimension(), QUALITY, false, false, .5F);
                imageUrl = HKImageUtils.getS3CategoryImageKey(enumImageSize, id);
                S3Utils.uploadData(awsAccessKey, awsSecretKey, repositoryFilePath, imageUrl, awsBucket);
            }
        }

    }

    private void resizeAndUpload(String filePath, SuperSaverImage superSaverImage) {
        String repositoryFilePath = null;
        Long id = superSaverImage.getId();

        // saving original image
        String imageKey = HKImageUtils.getS3SuperSaverImageKey(EnumImageSize.Original, id);
        S3Utils.uploadData(awsAccessKey, awsSecretKey, filePath, imageKey, awsBucket);

        // saving thumbnails for all sizes
        for (EnumImageSize enumImageSize : EnumImageSize.values()) {
            if (enumImageSize != EnumImageSize.Original) {
                repositoryFilePath = HKImageUtils.getRepositoryImagePath(enumImageSize, id);
                ImageUtils.createThumbnail(filePath, repositoryFilePath, enumImageSize.getDimension(), QUALITY, false, false, .5F);
                imageKey = HKImageUtils.getS3SuperSaverImageKey(enumImageSize, id);
                S3Utils.uploadData(awsAccessKey, awsSecretKey, repositoryFilePath, imageKey, awsBucket);
            }
        }
    }

    /*
    * private void resizeAndUpload(String filePath, ComboImage comboImage) throws Exception { String repositoryFilePath =
    * null; Long id = comboImage.getId(); // saving original image String imageUrl =
    * getS3ImageKey(EnumImageSize.Original, id); s3Utils.uploadMultipleData(awsAccessKey, awsSecretKey, filePath, imageUrl,
    * awsBucket); // saving thumbnails for all sizes for (EnumImageSize enumImageSize : EnumImageSize.values()) { if
    * (enumImageSize != EnumImageSize.Original) { repositoryFilePath = getRepositoryImagePath(enumImageSize, id);
    * imageUtils.createThumbnail(filePath, repositoryFilePath, enumImageSize.getDimension(), QUALITY, false, false,
    * .5F); imageUrl = getS3ImageKey(enumImageSize, id); s3Utils.uploadMultipleData(awsAccessKey, awsSecretKey,
    * repositoryFilePath, imageUrl, awsBucket); } } }
    */

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public SuperSaverImageService getSuperSaverImageService() {
        return superSaverImageService;
    }
}