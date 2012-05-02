package com.akube.framework.imaging;

import java.io.Serializable;

/**
 * User: rahul
 * Time: 30 Sep, 2009 11:19:45 AM
 */
@SuppressWarnings("serial")
public class ImageDimensionDTO implements Serializable {

  private Long imageHeight;
  private Long imageWidth;

  private Long thumbnailHeight;
  private Long thumbnailWidth;

  private String make;
  private String model;
  private String exposureTime;
  private String fNumber;
  private String focalLength;
  private String isoSpeedRatings;
  private String meteringMode;
  private String dateTimeOriginal;
  private String subjectDistance;
  private String orientation;

  private boolean forceChangedToJpg;

  public ImageDimensionDTO() {
  }

  public Long getImageHeight() {
    return imageHeight;
  }

  public void setImageHeight(Long imageHeight) {
    this.imageHeight = imageHeight;
  }

  public Long getImageWidth() {
    return imageWidth;
  }

  public void setImageWidth(Long imageWidth) {
    this.imageWidth = imageWidth;
  }

  public Long getThumbnailHeight() {
    return thumbnailHeight;
  }

  public void setThumbnailHeight(Long thumbnailHeight) {
    this.thumbnailHeight = thumbnailHeight;
  }

  public Long getThumbnailWidth() {
    return thumbnailWidth;
  }

  public void setThumbnailWidth(Long thumbnailWidth) {
    this.thumbnailWidth = thumbnailWidth;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getExposureTime() {
    return exposureTime;
  }

  public void setExposureTime(String exposureTime) {
    this.exposureTime = exposureTime;
  }

  public String getFNumber() {
    return fNumber;
  }

  public void setFNumber(String fNumber) {
    this.fNumber = fNumber;
  }

  public String getFocalLength() {
    return focalLength;
  }

  public void setFocalLength(String focalLength) {
    this.focalLength = focalLength;
  }

  public String getIsoSpeedRatings() {
    return isoSpeedRatings;
  }

  public void setIsoSpeedRatings(String isoSpeedRatings) {
    this.isoSpeedRatings = isoSpeedRatings;
  }

  public String getMeteringMode() {
    return meteringMode;
  }

  public void setMeteringMode(String meteringMode) {
    this.meteringMode = meteringMode;
  }

  public String getDateTimeOriginal() {
    return dateTimeOriginal;
  }

  public void setDateTimeOriginal(String dateTimeOriginal) {
    this.dateTimeOriginal = dateTimeOriginal;
  }

  public String getSubjectDistance() {
    return subjectDistance;
  }

  public void setSubjectDistance(String subjectDistance) {
    this.subjectDistance = subjectDistance;
  }

  public String getOrientation() {
    return orientation;
  }

  public void setOrientation(String orientation) {
    this.orientation = orientation;
  }

  public boolean isForceChangedToJpg() {
    return forceChangedToJpg;
  }

  public void setForceChangedToJpg(boolean forceChangedToJpg) {
    this.forceChangedToJpg = forceChangedToJpg;
  }
}
