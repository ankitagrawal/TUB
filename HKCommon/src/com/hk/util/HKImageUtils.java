package com.hk.util;

import org.springframework.beans.factory.annotation.Value;

import com.hk.constants.catalog.image.EnumImageSize;

public class HKImageUtils {

    
 // @Named(Keys.Env.accessKey)
    @Value("#{hkEnvProps['awsAccessKey']}")
    String                        awsAccessKey;

    // @Named(Keys.Env.secretKey)
    @Value("#{hkEnvProps['awsSecretKey']}")
    String                        awsSecretKey;
    
    static String                 imageUploadsPath             = "/usr/local/projects/production/HealthKartWork/imageUploads";
    static int                    noOfImagesInRepositorySubDir = 100;
    static String                 awsBucket                    = "healthkart-prod";
    static String                 awsReadBucket                = "healthkart-prod";

    static {

        /*imageUploadsPath = (String) ServiceLocatorFactory.getProperty(Keys.Env.imageUploads);
        noOfImagesInRepositorySubDir = Integer.parseInt((String) ServiceLocatorFactory.getProperty(Keys.Env.noOfImagesInRepositorySubDir));
        awsBucket = (String) ServiceLocatorFactory.getProperty(Keys.Env.bucket);
        awsReadBucket = (String) ServiceLocatorFactory.getProperty(Keys.Env.readBucket);
*/
        // TODO: rewrite
    }


    
    public static String getS3CategoryImageKey(EnumImageSize imageSize, Long imageId) {
        return (imageId / noOfImagesInRepositorySubDir + 1) + "/" + "c_" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getS3ImageKey(EnumImageSize imageSize, Long imageId) {
        return (imageId / noOfImagesInRepositorySubDir + 1) + "/" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getS3CategoryImageUrl(EnumImageSize imageSize, Long imageId) {
        return "http://" + awsReadBucket + ".s3.amazonaws.com/" + (imageId / noOfImagesInRepositorySubDir + 1) + "/" + "c_" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getS3ImageUrl(EnumImageSize imageSize, Long imageId) {
        return "http://" + awsReadBucket + ".s3.amazonaws.com/" + (imageId / noOfImagesInRepositorySubDir + 1) + "/" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getRepositoryImagePath(EnumImageSize imageSize, Long imageId) {
        return imageUploadsPath + "/" + getS3ImageKey(imageSize, imageId);
    }

}
