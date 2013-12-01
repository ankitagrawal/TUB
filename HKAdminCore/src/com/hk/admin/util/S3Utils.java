package com.hk.admin.util;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.multi.SimpleThreadedStorageService;
import org.jets3t.service.security.AWSCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.util.HKFileUtils;


public class S3Utils {
    private static Logger logger = LoggerFactory.getLogger(S3Utils.class);

	public static S3Service s3Service = null;

	private static S3Service initializeConnectionParams(String awsAccess, String awsSecret, String bucketName) {
		try {
			if (s3Service == null) {
				AWSCredentials awsCredentials = new AWSCredentials(awsAccess, awsSecret);
				s3Service = new RestS3Service(awsCredentials);
				S3Bucket s3Bucket = s3Service.getBucket(bucketName);
				//if the bucket already exists, we won't set its access control everytime.
				if (s3Bucket == null) {
					s3Bucket = s3Service.createBucket(bucketName);
					AccessControlList bucketAcl = s3Service.getBucketAcl(s3Bucket);
					bucketAcl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);
					s3Bucket.setAcl(bucketAcl);
					s3Service.putBucketAcl(s3Bucket);
				}
			}
			return s3Service;

		} catch (ServiceException se) {
			logger.error("error in uplaoding data on s3: " + se);
		}
		return null;
	}

    /**
     * The method uploads the data to S3 and gives it public read access
     *
     * @param awsAccess:  aws access key
     * @param awsSecret:  aws secret key
     * @param filePath:   path for the file to be uploaded
     * @param key:        key for file at Amazon S3
     * @param bucketName: bucket need not exist already. If it doesn't exist, it will be created.
     * @return
     */
    public static Boolean uploadData(String awsAccess, String awsSecret, String filePath, String key, String bucketName) {
        File file = new File(filePath);
        return uploadData(awsAccess, awsSecret, file, key, bucketName);
    }

    public static Boolean uploadData(String awsAccess, String awsSecret, File file, String key, String bucketName) {
        S3Service s3Service = initializeConnectionParams(awsAccess, awsSecret, bucketName);
        try {
            S3Bucket s3Bucket = s3Service.getBucket(bucketName);
            logger.debug("Uploading file: " + file.getName() + " in bucket: " + bucketName + " at Amazon S3");

            String contentType = HKFileUtils.getContentType(file.getAbsolutePath());

            S3Object s3Object = new S3Object(file);
            s3Object.setKey(key);
            s3Object.setAcl(s3Service.getBucketAcl(s3Bucket));
            s3Object.setContentType(contentType);
            s3Object.addMetadata("Expires", "Thu, 15 Apr 2040 20:00:00");
            s3Service.putObject(s3Bucket, s3Object);
            return Boolean.TRUE;
        } catch (ServiceException se) {
            logger.error("error in uplaoding data on s3: " + se);
        } catch (IOException ioe) {
            logger.error("error in uplaoding data on s3: " + ioe);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("error in uplaoding data on s3: " + nsae);
        }
        return Boolean.FALSE;
    }

    public static Boolean downloadData(String awsAccess, String awsSecret, String objectKey, String bucketName, File fileToWriteTo){
        S3Service s3Service = initializeConnectionParams(awsAccess, awsSecret, bucketName);
	    InputStream dataInputStream = null;
	    FileOutputStream fileOutputStream = null;
        try {
            S3Object s3Object = s3Service.getObject(bucketName, objectKey);
	        dataInputStream = s3Object.getDataInputStream();
	        fileOutputStream = new FileOutputStream(fileToWriteTo);
	        IOUtils.copy(dataInputStream, fileOutputStream);
        } catch (ServiceException se) {
            logger.error("error in downloading data from s3: " + se);
        } catch (FileNotFoundException fnfe){
            logger.error("error in downloading data from s3: " + fnfe) ;
        } catch (IOException ioe){
            logger.error("error in downloading data from s3: " + ioe) ;
        } finally {
	        if (dataInputStream != null) IOUtils.closeQuietly(dataInputStream);
	        if (fileOutputStream != null) IOUtils.closeQuietly(fileOutputStream);
        }
        return Boolean.FALSE;
    }

    public static Boolean uploadMultipleData(String awsAccess, String awsSecret, File folder, String bucketName) {
        try {
            S3Service s3Service = initializeConnectionParams(awsAccess, awsSecret, bucketName);
            S3Bucket s3Bucket = s3Service.getBucket(bucketName);
            logger.debug("Uploading folder: " + folder.getName() + " in bucket: " + bucketName + " at Amazon S3");

            List<S3Object> folderContents;
            folderContents = readFolderContents(folder, s3Service, s3Bucket);
            if (folderContents != null && folderContents.size() > 0) {
                return uploadFolderContents(folder, s3Service, s3Bucket, folderContents);
            }
        } catch (S3ServiceException se) {
            logger.error("error in uplaoding data on s3: " + se);
        }
        return Boolean.FALSE;
    }

    private static List<S3Object> readFolderContents(File folder, S3Service s3Service, S3Bucket s3Bucket) {
        List<S3Object> folderContents = new ArrayList<S3Object>();
        Iterator<File> filesInFolder = FileUtils.iterateFiles(folder, null, true);
        try {
            while (filesInFolder.hasNext()) {
                File file = filesInFolder.next();
                String key = generateFileKey(file.getAbsolutePath());
                String contentType = HKFileUtils.getContentType(file.getAbsolutePath());

                S3Object s3Object = new S3Object(s3Bucket, file);
                s3Object.setKey(key);
                s3Object.setAcl(s3Service.getBucketAcl(s3Bucket));
                s3Object.setContentType(contentType);
                logger.debug("path for file: " + file.getAbsolutePath());
                logger.debug("key for file: " + file.getName() + " is: " + key);
                folderContents.add(s3Object);
            }
            return folderContents;
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("error uplaoding data on s3: " + nsae);
        } catch (S3ServiceException sse) {
            logger.error("error uplaoding data on s3: " + sse);
        } catch (IOException ioe) {
            logger.error("error uplaoding data on s3: " + ioe);
        }
        return null;
    }

    private static Boolean uploadFolderContents(File folder, S3Service s3Service, S3Bucket s3Bucket, List<S3Object> folderContents) {
        int numberOfObjectsToBeUploaded = folderContents.size();
        SimpleThreadedStorageService storageService = new SimpleThreadedStorageService(s3Service);
        logger.debug("Uploading files in folder: " + folder.getAbsolutePath());
        try {
            storageService.putObjects(s3Bucket.getName(), folderContents.toArray(new S3Object[numberOfObjectsToBeUploaded]));
            return Boolean.TRUE;
        } catch (ServiceException se) {
            logger.error("Error encountered while uploading email content to amazon s3.", se);
        }
        return Boolean.FALSE;
    }

    private static String generateFileKey(String contentPath) {
        return HKFileUtils.getPathAfterSubstring(contentPath, "adminUploads");
    }

    public static void main(String[] args) {
        String unixPath = "/usr/local/projects/staging/HealthKartWork/adminUploads/emailContentFiles/beautyCampaign20120702/images/img01_c.jpg";
        System.out.println("unix path key: " + generateFileKey(unixPath));
        String windowsPath = "\\usr\\local\\projects\\staging\\HealthKartWork\\adminUploads\\emailContentFiles\\beautyCampaign20120702\\images\\img01_c.jpg";
        System.out.println("windows path key: " + generateFileKey(windowsPath));
    }
}