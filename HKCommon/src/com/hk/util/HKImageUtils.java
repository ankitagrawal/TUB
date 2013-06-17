package com.hk.util;

import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.Keys;
import com.hk.web.filter.WebContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HKImageUtils {

    @Value("#{hkEnvProps['" + Keys.Env.accessKey + "']}")
    String awsAccessKey;

    @Value("#{hkEnvProps['" + Keys.Env.secretKey + "']}")
    String awsSecretKey;

    @Value("#{hkEnvProps['" + Keys.Env.bucket + "']}")
    String uploadBucket;

    @Value("#{hkEnvProps['" + Keys.Env.readBucket + "']}")
    String downloadBucket;

    @Value("#{hkEnvProps['" + Keys.Env.imageDistributionDomain_prefix + "']}")
    String imageDistributionDomain_prefix;

    @Value("#{hkEnvProps['" + Keys.Env.imageDistributionDomain_suffix + "']}")
    String imageDistributionDomain_suffix;

    @Value("#{hkEnvProps['" + Keys.Env.imageUploads + "']}")
    String imageUploads;

    @Value("#{hkEnvProps['" + Keys.Env.noOfImagesInRepositorySubDir + "']}")
    Long noOfImagesInRepositoryDir;

    @Value("#{hkEnvProps['" + Keys.Env.bucket + "']}")
    private static String awsBucketStr;

    static String awsBucket;
    static String awsReadBucket;
    static String awsImageDistributionDomain_prefix;
    static String awsImageDistributionDomain_suffix;
    static String imageUploadsPath;
    static Long noOfImagesInRepositorySubDir;

    @PostConstruct
    public void postConstruction() {
        awsBucket = StringUtils.isNotBlank(awsBucketStr) ? awsBucketStr : "";
        awsReadBucket = StringUtils.isNotBlank(downloadBucket) ? downloadBucket : "";
        awsImageDistributionDomain_prefix = StringUtils.isNotBlank(imageDistributionDomain_prefix) ? imageDistributionDomain_prefix : "";
        awsImageDistributionDomain_suffix = StringUtils.isNotBlank(imageDistributionDomain_suffix) ? imageDistributionDomain_suffix : "";
        imageUploadsPath = StringUtils.isNotBlank(imageUploads) ? imageUploads : "";
        noOfImagesInRepositorySubDir = (noOfImagesInRepositoryDir != null) ? noOfImagesInRepositoryDir : 0;
    }

    public static String getS3CategoryImageKey(EnumImageSize imageSize, Long imageId) {
        return (imageId / noOfImagesInRepositorySubDir + 1) + "/" + "c_" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getS3ImageKey(EnumImageSize imageSize, Long imageId) {
        return (imageId / noOfImagesInRepositorySubDir + 1) + "/" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getS3SuperSaverImageKey(EnumImageSize imageSize, Long imageId) {
        return (imageId / noOfImagesInRepositorySubDir + 1) + "/" + "com_" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getS3CategoryImageUrl(EnumImageSize imageSize, Long imageId) {
        String prefix = "http://";
        if (WebContext.isSecure()) {
            prefix = "https://";
        }
        return prefix + awsReadBucket + ".s3.amazonaws.com/" + (imageId / noOfImagesInRepositorySubDir + 1) + "/" + "c_" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getS3ImageUrl(EnumImageSize imageSize, Long imageId) {

        String prefix = "http://";
        if (WebContext.isSecure()) {
            prefix = "https://";
        }
        Long urlInt = imageId%5; // Assuming FIVE CDNs for images
        String imageDistributionUrl = awsImageDistributionDomain_prefix + urlInt.intValue() + "." + awsImageDistributionDomain_suffix + "/";

        return prefix + imageDistributionUrl + (imageId / noOfImagesInRepositorySubDir + 1) + "/" + imageId + "_" + imageSize.getSuffix() + ".jpg";
        //return prefix + awsReadBucket + ".s3.amazonaws.com/" + (imageId / noOfImagesInRepositorySubDir + 1) + "/" + imageId + "_" + imageSize.getSuffix() + ".jpg";
    }

    public static String getS3SuperSaverImageUrl(EnumImageSize imageSize, Long imageId) {
        String prefix = "http://";
        if (WebContext.isSecure()) {
            prefix = "https://";
        }
        return prefix + awsReadBucket + ".s3.amazonaws.com/" + getS3SuperSaverImageKey(imageSize, imageId);
    }

    public static String getRepositoryImagePath(EnumImageSize imageSize, Long imageId) {
        return imageUploadsPath + "/" + getS3ImageKey(imageSize, imageId);
    }

}