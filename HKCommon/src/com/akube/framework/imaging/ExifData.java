package com.akube.framework.imaging;

import java.io.File;
import java.io.IOException;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

/**
 * User: rahul
 * Time: 30 Sep, 2009 10:58:04 AM
 */
public class ExifData {

  private String make;
  private String model;
  private String exposureTime;
  private String fNumber;
  private String focalLength;
  private String isoSpeedRatings;
  private String meteringMode;
  private String dateTimeOriginal;
  private String subjectDistance;
  private String resolution;
  private String orientation;

  public ExifData(File srcFile) {

    IImageMetadata metadata = null;
    try {
      metadata = Sanselan.getMetadata(srcFile);
    } catch (ImageReadException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (metadata instanceof JpegImageMetadata) {
      JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

      resolution = getTagValue(jpegMetadata, TiffConstants.TIFF_TAG_XRESOLUTION);
      dateTimeOriginal = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
      isoSpeedRatings = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_ISO);
      exposureTime = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_EXPOSURE_TIME);
      make = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_MAKE);
      fNumber = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_FNUMBER);
      focalLength = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_FOCAL_LENGTH);
      meteringMode = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_METERING_MODE);
      subjectDistance = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_SUBJECT_DISTANCE);
      orientation = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_ORIENTATION);
      model = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_MODEL);

    }
  }

  private String getTagValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) {
    TiffField field = jpegMetadata.findEXIFValue(tagInfo);
    return field == null ? "" : field.getValueDescription();
  }

  public String getMake() {
    return make;
  }

  public String getModel() {
    return model;
  }

  public String getExposureTime() {
    return exposureTime;
  }

  public String getFNumber() {
    return fNumber;
  }

  public String getFocalLength() {
    return focalLength;
  }

  public String getIsoSpeedRatings() {
    return isoSpeedRatings;
  }

  public String getMeteringMode() {
    return meteringMode;
  }

  public String getDateTimeOriginal() {
    return dateTimeOriginal;
  }

  public String getSubjectDistance() {
    return subjectDistance;
  }

  public String getResolution() {
    return resolution;
  }

  public String getOrientation() {
    return orientation;
  }
}
