package com.hk.util;

import java.io.File;

import org.apache.log4j.Logger;
import org.jets3t.service.S3Service;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;


public class S3Utils {

  private static Logger logger = Logger.getLogger(S3Utils.class);

  /**
   * The method uploads the image to S3 and gives it public read access
   *
   * @param awsAccess
   * @param awsSecret
   * @param filePath              file should be a jpeg file only
   * @param key
   * @param bucketName            bucket need not exist already. If it doesn't exist, it will be created.
   * @throws Exception
   */

  public void uploadImage(String awsAccess, String awsSecret, String filePath, String key, String bucketName) throws Exception {

    logger.debug("awsAccess : "+ awsAccess);
    logger.debug("awsSecret : "+ awsSecret);
    logger.debug("filePath : "+ filePath);
    logger.debug("key : "+ key);
    logger.debug("bucketName : "+ bucketName);

    AWSCredentials awsCredentials = new AWSCredentials(awsAccess, awsSecret);
    S3Service s3Service = new RestS3Service(awsCredentials);
//    S3Bucket s3Bucket=s3Service.getOrCreateBucket(bucketName);
    S3Bucket s3Bucket = s3Service.getBucket(bucketName);
    //this way is used instead of directly using getOrCreateBucket method ,so that if the bucket already exists, we won't set its access control everytime.
    if (s3Bucket == null) {
      s3Bucket = s3Service.createBucket(bucketName);
      AccessControlList bucketAcl = s3Service.getBucketAcl(s3Bucket);
      bucketAcl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);
      s3Bucket.setAcl(bucketAcl);
      s3Service.putBucketAcl(s3Bucket);
    }

    File file = new File(filePath);
    S3Object s3Object = new S3Object(file);
    s3Object.setKey(key);
    s3Object.setAcl(s3Service.getBucketAcl(s3Bucket));
    s3Object.setContentType("image/jpeg");
    s3Service.putObject(s3Bucket, s3Object);
  }
}

