package com.akube.framework.imaging;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.OpImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.AWTImageDescriptor;
import javax.media.jai.operator.CropDescriptor;
import javax.media.jai.operator.RotateDescriptor;
import javax.media.jai.operator.ScaleDescriptor;
import javax.media.jai.operator.SubsampleAverageDescriptor;
import javax.media.jai.operator.UnsharpMaskDescriptor;

import mediautil.image.jpeg.LLJTran;
import mediautil.image.jpeg.LLJTranException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.exception.ImagingException;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.SeekableStream;

/**
 * User: rahul
 * Time: 30 Sep, 2009 11:32:17 AM
 */
public class ImageUtils {
  private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

  private static final float DefaultJpegQuality = .95f;
  private static final int minHeightForImageTextComposition = 600;
  private static String tempPath = "./test-output/images/tempImages/";


  /**
   * This method not noly return the image dimesions and exif info
   * But it will also focrefully change large size fake png files to
   * jpg images. It will set the forceChangeToJpg to true if it does do.
   * <p/>
   * Logic to detect the large size fake png files:
   * 1. Format type png
   * 2. AreaFactor * ImageArea < filesize (for now areaFaqctor = 1.0)
   * <p/>
   * if any image passes the above 2 criterias then it will be converted to jpg
   * and forceChangeToJpg will be set true.
   * <p/>
   * Note: we have added this logic here to save loading of image into memory
   * 2nd time for this operation.
   *
   * @param srcFile
   * @return
   */
  public static ImageDimensionDTO getImageDimensions(File srcFile) {

    ImageDimensionDTO imageDimensionDTO = new ImageDimensionDTO();
    long fileSize = srcFile.length();
    RenderedOp image = loadImage(srcFile);

    imageDimensionDTO.setImageWidth((long) image.getWidth());
    imageDimensionDTO.setImageHeight((long) image.getHeight());

    ExifData exifData = new ExifData(srcFile);
    imageDimensionDTO.setMake(exifData.getMake());
    imageDimensionDTO.setModel(exifData.getModel());
    imageDimensionDTO.setDateTimeOriginal(exifData.getDateTimeOriginal());
    imageDimensionDTO.setExposureTime(exifData.getExposureTime());
    imageDimensionDTO.setFNumber(exifData.getFNumber());
    imageDimensionDTO.setFocalLength(exifData.getFocalLength());
    imageDimensionDTO.setIsoSpeedRatings(exifData.getIsoSpeedRatings());
    imageDimensionDTO.setMeteringMode(exifData.getMeteringMode());
    imageDimensionDTO.setSubjectDistance(exifData.getSubjectDistance());
    String orientation = StringUtils.isBlank(exifData.getOrientation()) ? null : exifData.getOrientation().substring(0, 1);
    imageDimensionDTO.setOrientation(orientation);

    if (getImageType(srcFile.getAbsolutePath()).equalsIgnoreCase("png")) {
      long imageArea = image.getWidth() * image.getHeight();
      if (1.0 * imageArea < fileSize) {
        logger.info("Force changing file type to jpg");
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImage.createGraphics().drawImage(image.getAsBufferedImage(), 0, 0, Color.BLACK, null);
        saveBufferedImage(newImage, srcFile.getAbsolutePath(), "jpeg");
        imageDimensionDTO.setForceChangedToJpg(true);
      }
    }

    return imageDimensionDTO;
  }

  /**
   * @param filePath
   * @param dest
   * @param thumbMaxSize The maximum dimension of the image. Aspect ratio is retained. -1 for no scaling
   * @param quality      Scale of 0.0 to 1.0, 1.0 being the highest
   * @param returnInfo   sets Exif data in return info if true
   * @param unsharp      if false, then do not apply unsharp mask
   * @param unsharpQty   gainFactor for the unsharp mask - In general gain factors should be restricted to a range of [-1, 2]. Lesser than 0 means smoothing.
   * @return
   */
  public static ImageDimensionDTO createThumbnail(String filePath, String dest, int thumbMaxSize, float quality, boolean returnInfo, boolean unsharp, float unsharpQty) {
    logger.debug("creating thumbnail for image");
    ImageDimensionDTO imageDimensionDTO = new ImageDimensionDTO();

    File srcFile = new File(filePath);
    RenderedOp image = loadImage(srcFile);

    imageDimensionDTO.setImageWidth((long) image.getWidth());
    imageDimensionDTO.setImageHeight((long) image.getHeight());

    RenderedOp scaledImage = scaleDownImage(image, thumbMaxSize);
    if (unsharp)
      scaledImage = UnsharpMaskDescriptor.create(scaledImage, null, unsharpQty, null);

    logger.debug("saving image");
    saveImage(scaledImage, dest, getImageType(filePath), quality);

    imageDimensionDTO.setThumbnailWidth((long) scaledImage.getWidth());
    imageDimensionDTO.setThumbnailHeight((long) scaledImage.getHeight());

    if (returnInfo) {
      ExifData exifData = new ExifData(srcFile);
      imageDimensionDTO.setMake(exifData.getMake());
      imageDimensionDTO.setModel(exifData.getModel());
      imageDimensionDTO.setDateTimeOriginal(exifData.getDateTimeOriginal());
      imageDimensionDTO.setExposureTime(exifData.getExposureTime());
      imageDimensionDTO.setFNumber(exifData.getFNumber());
      imageDimensionDTO.setFocalLength(exifData.getFocalLength());
      imageDimensionDTO.setIsoSpeedRatings(exifData.getIsoSpeedRatings());
      imageDimensionDTO.setMeteringMode(exifData.getMeteringMode());
      imageDimensionDTO.setSubjectDistance(exifData.getSubjectDistance());
    }

    return imageDimensionDTO;
  }

  public static void rotateImage(String srcImagePath, String destImagePath, double rotationAngle) {
    if (srcImagePath.equals(destImagePath)) rotateImage(srcImagePath, rotationAngle);
    else {
      RenderedOp image = loadImage(srcImagePath);
      saveImage(rotateAndReturnImage(image, rotationAngle), destImagePath, getImageType(srcImagePath));
    }
  }

  /**
   * Positive angle value rotates the image Clockwise
   *
   * @param filePath
   * @param rotationAngle
   */
  public static void rotateImage(String filePath, double rotationAngle) {

    String format = getImageType(filePath);
    File srcFile = new File(filePath);
    RenderedOp image;
    /**
     *  Lotcha:
     *  Since there is a problem in rotating the GIF images when source and destination file are same.
     *  So we will copy the image first to temp folder and then rotate the image and finally save the image to the destination i.e filePath.
     *
     *  A neat and clean solution will be provided in the future for the same.
     */
    if (format.equalsIgnoreCase("gif")) {
      File tempFile = new File(tempPath + srcFile.getName());
      try {
        copyFile(srcFile, tempFile, false);
        image = loadImage(tempFile);
      } catch (IOException e) {
        logger.error("Exception while rotation of image", e);
        throw new ImagingException("Exception while copying image during rotation", tempFile.getAbsolutePath());
      } finally {
        if (tempFile.exists()) tempFile.delete();
      }
    } else {
      image = loadImage(srcFile);
    }

    saveImage(rotateAndReturnImage(image, rotationAngle), filePath, format);
  }

  /**
   * The moethod will try to do a lossless rotation if it is supported ** . Two clauses are required
   * 1. the image must be a jpeg with the proper encoding (most are)
   * 2. the rotation angle must be a multiple of 90
   * <p/>
   * if lossless is not supported the call is delegated to regular rotation instead {@link #rotateImage(String, double)}
   * <p/>
   * ** lossless rotation is also not possible in all image sizes
   * (height and widht must be a multiple of MCU dimension, usually 8, 16 or 24)
   * See file:///D:/ReevsateLib/JAI/mediautil-1.0/docs/javadocs/mediautil/image/jpeg/LLJTran.html#OPT_XFORM_TRIM
   * for more details
   * <p/>
   * Not using this for now
   *
   * @param filePath
   * @param rotationAngle
   */
  public static void rotateImageLossless(String filePath, int rotationAngle) {
    File srcFile = new File(filePath);
    LLJTran llj = null;
    boolean losslessSupported = false;

    // check that rotation angle is a multiple of 90.
    if (rotationAngle % 90 == 0) {
      llj = new LLJTran(srcFile);
      try {
        llj.read(LLJTran.READ_ALL, true);
        losslessSupported = true;
      } catch (LLJTranException e) {
        // if exception is thrown then this means that LLJTran does not support transforming this file,
        // use the regular method instead, losslessSupported is still false, so the control will pass on
        // to regular rotate
      }
    }

    if (losslessSupported) {
      File tempFile = new File(tempPath + srcFile.getName());
      FileOutputStream fileOutputStream = null;
      try {
        rotationAngle = rotationAngle % 360;
        if (rotationAngle < 0) rotationAngle += 360;
        if (rotationAngle == 0) return;

        switch (rotationAngle) {
          case 90:
            llj.transform(LLJTran.ROT_90, LLJTran.OPT_XFORM_ADJUST_EDGES);
            break;
          case 180:
            llj.transform(LLJTran.ROT_180, LLJTran.OPT_XFORM_ADJUST_EDGES);
            break;
          case 270:
            llj.transform(LLJTran.ROT_270, LLJTran.OPT_XFORM_ADJUST_EDGES);
        }

        fileOutputStream = FileUtils.openOutputStream(tempFile);
        llj.save(fileOutputStream);
        FileUtils.copyFile(tempFile, srcFile);
      } catch (IOException e) {
        logger.error("Exception while rotation of image", e);
        throw new ImagingException("Exception while rotation of image", tempFile.getAbsolutePath());
      } finally {
        try {
          if (fileOutputStream != null) fileOutputStream.close();
        } catch (IOException e) {
          // do nothing
        }
        if (tempFile.exists()) tempFile.delete();
        llj.freeMemory();
      }
    } else {
      if (llj != null) llj.freeMemory();
      rotateImage(filePath, rotationAngle);
    }

  }

  /**
   * Orientation as sotred in the exif tag. the letter F is shown as how it would look for respective orientation values
   * <p/>
   * 1        2       3      4         5            6           7          8
   * <p/>
   * 888888  888888      88  88      8888888888  88                  88  8888888888
   * 88          88      88  88      88  88      88  88          88  88      88  88
   * 8888      8888    8888  8888    88          8888888888  8888888888          88
   * 88          88      88  88
   * 88          88  888888  888888
   *
   * @param filePath
   */
  public static void autoRotate(String filePath) {

    ExifData exifData = new ExifData(new File(filePath));
//    System.out.println("orientation:" + exifData.getOrientation());

    if (exifData.getOrientation() == null)
      return;

    switch (exifData.getOrientation().charAt(0)) {
      case '3':
      case '4':
        rotateImage(filePath, 180);
        break;

      case '5':
      case '6':
        rotateImage(filePath, 90);
        break;

      case '7':
      case '8':
        rotateImage(filePath, 270);
        break;

      default:
        break;
    }

  }

  public static int getRotateAngleFromExifOrientation(String exifOrientation) {
    if (exifOrientation == null) return 0;

    switch (exifOrientation.charAt(0)) {
      case '3':
      case '4':
        return 180;

      case '5':
      case '6':
        return 90;

      case '7':
      case '8':
        return 270;

      default:
        return 0;
    }
  }


  public static void cropImageAndResizeMax(String srcImagePath, String destImagePath, Double aspectRatio, Double x1, Double y1, Double width, int maxPixel) {

    RenderedOp image = loadImage(srcImagePath);
    float croppedImageX = (float) (x1 * image.getWidth());
    float croppedImageY = (float) (y1 * image.getWidth());
    float croppedImageWidth = (float) (width * image.getWidth());
    float croppedImageHeight = (float) (croppedImageWidth / aspectRatio);

    /**
     * To avoide rounding off errors we are applying the following logic.
     *
     * Example :
     * Print Attributes for product 5x7 and image attributes as width = 3648, height = 2736
     *
     * Rotation angle = 0
     * Padding = 0
     * width = 1
     * x1 = 0
     * y1 = .0367
     * Aspect ratio = 1.333
     *
     * Now cropped value for image comes out to be
     * croppedImageX = 0
     * croppedImagey = 133.88
     * croppedImageWidth = 3648
     * croppedImageHeight = 2605.71
     *
     * since in this case croppedImageHeight + croppedImageY - image.getHeight()
     * is greater than zero so image crop will throw out of crop area exception
     * to solve this problem given below logic is applied.
     */
    float heightDifference = croppedImageHeight + croppedImageY - image.getHeight();
    if (heightDifference >= 0) {
      croppedImageHeight -= heightDifference + .01;   // locha : this extra .01 is being substracted from height to account for possible round off errors  (was happening in rare cases, not all)
    }
    float widthDifference = croppedImageWidth + croppedImageX - image.getWidth();
    if (widthDifference >= 0) {
      croppedImageWidth -= widthDifference + .01;     // locha : this extra .01 is being substracted from width to account for possible round off errors
    }

    RenderedOp croppedScaledImage;
    if (maxPixel != 0) {
      croppedScaledImage = scaleDownImage(CropDescriptor.create(image, croppedImageX, croppedImageY, croppedImageWidth, croppedImageHeight, null), maxPixel);
    } else {
      croppedScaledImage = CropDescriptor.create(image, croppedImageX, croppedImageY, croppedImageWidth, croppedImageHeight, null);
    }

    saveBufferedImage(croppedScaledImage.getAsBufferedImage(), destImagePath, getImageType(srcImagePath));

  }

  public static void cropImage(String srcImagePath, String destImagePath, int x, int y, int width, int height) {

    RenderedOp image = cropImageAndReturnImage(srcImagePath, x, y, width, height);

    // normal saving of images does not work properly with JPG format. thats why BufferedImage is used here for saving the cropped file
    saveBufferedImage(image.getAsBufferedImage(), destImagePath, getImageType(srcImagePath));
  }


  public static void cropImageAndResizeMax(String srcImagePath, String destImagePath, int x, int y, int width, int height, float maxPixel) {

    RenderedOp image = cropImageAndReturnImage(srcImagePath, x, y, width, height);

    saveImage(scaleDownImage(image, (int) maxPixel), destImagePath, getImageType(srcImagePath));

  }

  /**
   * @param srcImagePath
   * @param destImagePath
   * @param topBottomBorder [top border + bottom border] : equal borders are applied
   * @param leftRightBorder [left border + right border] : equal borders are applied
   * @param colorR
   * @param colorG
   * @param colorB
   */
  public static void addBorder(String srcImagePath, String destImagePath, int topBottomBorder, int leftRightBorder, int colorR, int colorG, int colorB) {

    RenderedOp borderedImage = addBorderAndReturnImage(loadImage(srcImagePath), topBottomBorder, leftRightBorder, colorR, colorG, colorB);

    saveImage(borderedImage, destImagePath, getImageType(srcImagePath));
  }

  public static void addBorderAndResizeMax(String srcImagePath, String destImagePath, double aspectRatio, int maxPixel) {

    RenderedOp image = loadImage(srcImagePath);

    int topBottomBorder = 0;
    int leftRightBorder = 0;

    if (aspectRatio < 1) {
      topBottomBorder = (int) ((image.getWidth() - aspectRatio * image.getHeight()) / aspectRatio);
      if (topBottomBorder < 0) {
        topBottomBorder = 0;
        leftRightBorder = (int) (aspectRatio * image.getHeight() - image.getWidth());
      }
    } else {
      leftRightBorder = (int) (aspectRatio * image.getHeight() - image.getWidth());
      if (leftRightBorder < 0) {
        leftRightBorder = 0;
        topBottomBorder = (int) ((image.getWidth() - aspectRatio * image.getHeight()) / aspectRatio);
      }
    }

    RenderedOp borderedScaledImage = null;
    if (maxPixel != 0) {
      borderedScaledImage = scaleDownImage(addBorderAndReturnImage(image, topBottomBorder, leftRightBorder, 255, 255, 255), maxPixel);
    } else {
      borderedScaledImage = addBorderAndReturnImage(image, topBottomBorder, leftRightBorder, 255, 255, 255);
    }

    saveImage(borderedScaledImage, destImagePath, getImageType(srcImagePath));
  }

  /**
   * @param srcImagePath
   * @param destImagePath
   * @param topBottomBorder [top border + bottom border] : equal borders are applied
   * @param leftRightBorder [left border + right border] : equal borders are applied
   * @param colorR
   * @param colorG
   * @param colorB
   * @param maxPixel        The maximum dimension of the image. Aspect ratio is retained. -1 for no scaling
   */
  public static void addBorderAndResizeMax(String srcImagePath, String destImagePath, int topBottomBorder, int leftRightBorder, int colorR, int colorG, int colorB, int maxPixel) {

    RenderedOp image = loadImage(srcImagePath);
    RenderedOp borderedImage = addBorderAndReturnImage(image, topBottomBorder, leftRightBorder, colorR, colorG, colorB);

    saveImage(scaleDownImage(borderedImage, maxPixel), destImagePath, getImageType(srcImagePath));

  }

  /**
   * @param srcImagePath
   * @param destImagePath
   * @param maxPixel      The maximum dimension of the image. Aspect ratio is retained. -1 for no scaling
   * @param rotationAngle
   */
  public static void imageResizeMaxAndRotate(String srcImagePath, String destImagePath, int maxPixel, double rotationAngle) {

    RenderedOp image = loadImage(srcImagePath);
    RenderedOp scaledImage = scaleDownImage(image, maxPixel);
    saveImage(rotateAndReturnImage(scaledImage, rotationAngle), destImagePath, getImageType(srcImagePath));

  }

  public static void addCaption(String srcImage, String caption, String fontFilePath) {

    RenderedOp image1 = loadImage(srcImage);

    if (image1.getHeight() < minHeightForImageTextComposition) {

      float width = (int) image1.getWidth() * ((float) minHeightForImageTextComposition / image1.getHeight());
      image1 = scaleImage(image1, (int) width, minHeightForImageTextComposition);
    }

    BufferedImage bufferedImage1 = image1.getAsBufferedImage();   //lotcha: this step is necessary, if u will not make a seperate object of the BufferedImage then no compostion will take place
    Graphics2D g = bufferedImage1.createGraphics();
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));

    // creating a new image of 10x10 pixels
    int image2Width = 10, image2Height = 10;
    BufferedImage bufferedImage2 = new BufferedImage(image2Width, image2Height, BufferedImage.TYPE_INT_RGB);

    // setting the each pixel's color to white
    for (int i = 0; i < image2Width; i++) {
      for (int j = 0; j < image2Height; j++) bufferedImage2.setRGB(i, j, 0xFFFFFF);
    }

    int overlayedImageHeight = bufferedImage2.getHeight() * (image1.getHeight() / 300);
    int overlayedImageOriginY = image1.getMaxY() - 2 * overlayedImageHeight;
    g.drawImage(bufferedImage2, 0, overlayedImageOriginY, image1.getWidth(), overlayedImageHeight, null);

    int font_size = image1.getHeight() / 40 + 1;
    // find the font size to write the given caption accurately. i.e if string width is more than image width then reduce the font size.
    do {
      font_size -= 1;
      g.setFont(new Font("Arial", Font.PLAIN, font_size));
    } while (g.getFontMetrics().stringWidth(caption) + 2 * font_size > image1.getWidth());

    g.setColor(Color.black);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    g.drawString(caption, font_size, (overlayedImageOriginY + overlayedImageHeight - (overlayedImageHeight - font_size) / 2 - 1));

    RenderedOp renderedImage = AWTImageDescriptor.create(bufferedImage1, null);

    saveImage(renderedImage, srcImage, getImageType(srcImage));
  }

  //helper methods start here -----

  private static File saveImage(RenderedOp image, String destImagePath, String format) {
    return saveImage(image, destImagePath, format, DefaultJpegQuality);
  }

  /**
   * @param image
   * @param destImagePath
   * @param format
   * @param jpegQuality   Scale of 0 to 1, 1 being the highest
   * @return
   */
  private static File saveImage(RenderedOp image, String destImagePath, String format, float jpegQuality) {
    File destFile = new File(destImagePath);
    destFile.getParentFile().mkdirs();
    if (format.equalsIgnoreCase("jpeg")) {
      JPEGEncodeParam ep = new JPEGEncodeParam();
      ep.setQuality(jpegQuality);
      RenderedOp finalImage = JAI.create("filestore", image, destFile.getAbsolutePath(), "JPEG", ep);
      finalImage.dispose();
    } else if (format.equalsIgnoreCase("png") || format.equalsIgnoreCase("gif")) {
      FileOutputStream fileOutputStream = null;
      try {
        fileOutputStream = new FileOutputStream(destFile.getAbsolutePath());
        RenderedOp finalImage = JAI.create("encode", image, fileOutputStream, "PNG");
        finalImage.dispose();
      } catch (IOException e) {
//        System.out.println("IOException: " + e);
        throw new ImagingException("Exception while saving the file", destImagePath);
      } finally {
        if (fileOutputStream != null) try {
          fileOutputStream.close();
        } catch (IOException e) {
        }
      }
    }

    return destFile;
  }

  private static File saveBufferedImage(BufferedImage bufferedImage, String destImagePath, String format) {

    File destFile = new File(destImagePath);
    destFile.getParentFile().mkdirs();
    try {
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destFile));

      if (format.equalsIgnoreCase("jpeg"))
        ImageIO.write(bufferedImage, "JPEG", bufferedOutputStream);
      else if (format.equalsIgnoreCase("png") || format.equalsIgnoreCase("gif"))
        ImageIO.write(bufferedImage, "PNG", bufferedOutputStream);

      bufferedOutputStream.close();

    } catch (IOException ioe) {

//      System.out.println("IOException: " + ioe);
      throw new ImagingException("Exception while saving the cropped file", destImagePath);
    }

    return destFile;
  }

  private static RenderedOp loadImage(String srcImagePath) {
    File srcFile = new File(srcImagePath);
    return loadImage(srcFile);
  }

  private static RenderedOp loadImage(File srcFile) {
    SeekableStream seekableStream = null;
    try {
      seekableStream = SeekableStream.wrapInputStream(new FileInputStream(srcFile), true);
    } catch (FileNotFoundException fnfe) {
//      System.out.println("FNFE: " + fnfe);
      throw new ImagingException("Exception while loading the file", srcFile.getAbsolutePath());
    }
    RenderedOp image = JAI.create("stream", seekableStream);
    ((OpImage) image.getRendering()).setTileCache(null);

    return image;
  }

  private static RenderedOp scaleDownImage(RenderedOp image, int maxPixel) {
    if (maxPixel == 0 || maxPixel == -1) {
      return image;
    }
    int imageResolution = image.getWidth() > image.getHeight() ? image.getWidth() : image.getHeight();
    double scale = (double) maxPixel / imageResolution;

    // scaled image size must not be larger than the original image so we will set the scaling factor to 0 if it is greater than 1
    if (scale > 1) scale = 1;

    return performScaling(image, scale, scale);

  }

  private static RenderedOp scaleImage(RenderedOp image, int width, int height) {
    double widthScaleFactor = (double) width / (double) image.getWidth();
    double heightScaleFactor = (double) height / (double) image.getHeight();

    return performScaling(image, widthScaleFactor, heightScaleFactor);

  }

  /**
   * If any scale factor is negative, set it to 1.0
   *
   * @param image
   * @param widthScaleFactor
   * @param heightScaleFactor
   * @return
   */
  private static RenderedOp performScaling(RenderedOp image, double widthScaleFactor, double heightScaleFactor) {
    if (widthScaleFactor < 0) {
      logger.warn("Negative ScaleX value {} passed. Resetting it to 1.0", widthScaleFactor);
      widthScaleFactor = 1.0;
    }
    if (heightScaleFactor < 0) {
      logger.warn("Negative ScaleY value {} passed. Resetting it to 1.0", heightScaleFactor);
      heightScaleFactor = 1.0;
    }

    if (widthScaleFactor == 1.0 && heightScaleFactor == 1.0) {
      return image;
    }

    RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

    if (widthScaleFactor > 1 || heightScaleFactor > 1)
      return ScaleDescriptor.create(image, (float) widthScaleFactor, (float) heightScaleFactor, 0f, 0f, new InterpolationBilinear(), renderingHints);   // here bilinear Interpolation is used because its better than Nearest neighbor and also it converts the indexedColorModel into RGB
    else
      return SubsampleAverageDescriptor.create(image, widthScaleFactor, heightScaleFactor, renderingHints);
  }

  private static RenderedOp cropImageAndReturnImage(String srcImagePath, int x, int y, int width, int height) {

    File srcFile = new File(srcImagePath);
    RenderedOp image = loadImage(srcFile);

    return CropDescriptor.create(image, (float) x, (float) y, (float) width, (float) height, null);
  }

  /**
   * Positive angle value rotates the image clockwise
   *
   * @param image
   * @param rotationAngle
   * @return
   */
  private static RenderedOp rotateAndReturnImage(RenderedOp image, double rotationAngle) {
    if (rotationAngle == 0) {
      return image;
    }
    float angle = (float) Math.toRadians(rotationAngle);
    float centerX = 0;
    float centerY = 0;
    return RotateDescriptor.create(image, centerX, centerY, angle, new InterpolationBilinear(), null, null);
  }

  /**
   * @param image
   * @param topBottomBorder [top border + bottom border] : equal borders are applied
   * @param leftRightBorder [left border + right border] : equal borders are applied
   * @param colorR
   * @param colorG
   * @param colorB
   * @return
   */
  private static RenderedOp addBorderAndReturnImage(RenderedOp image, int topBottomBorder, int leftRightBorder, int colorR, int colorG, int colorB) {

    ParameterBlock params = new ParameterBlock();
    params.addSource(image);
    params.add(leftRightBorder / 2);
    params.add(leftRightBorder / 2);
    params.add(topBottomBorder / 2);
    params.add(topBottomBorder / 2);

    /**
     *  This line of the code will define the color of the border to be applied.
     *
     *  Since transparent png/gif has a alpha channel attached to it thats why we have provided the 4th value in the double array i.e for transparency. 0 ==> transparent, 255 ==> opaque
     *  Right now this solution does not work for IndexedColorModel. In case of index color model, colors are much less than RGB scheme and are indexed so color of the border is not predictable.
     *
     *  A bad hack could be to convert the index color scheme into RGB ComponentColorModel and then apply border. But its not that simple.
     *  PNG format image can be converted easily using the PNGDescriptor and PNDDecodeParam but solution for GIF is not ready made so solution could be to convert the gif image as png.
     *  Other Solution could be to scale the image using Bilinear interpolation technique which converts the indexedColorModel into RGB internally
     *
     *  Right now nothing special is done to handle indexedColorModel images.
     */
    params.add(new BorderExtenderConstant(new double[]{colorR, colorG, colorB, 255}));

    return JAI.create("border", params);
  }

  private static String getImageType(String srcImage) {
    String formatName = null;

    try {
      ImageInputStream imageStream = ImageIO.createImageInputStream(new File(srcImage));
      Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
      ImageReader reader = null;
      if (!readers.hasNext()) {
        imageStream.close();
        throw new ImagingException("Exception while finding image format", srcImage);
      } else {
        reader = readers.next();
      }

      reader.setInput(imageStream, true, true);

      reader.dispose();
      imageStream.close();

      formatName = reader.getFormatName();
    } catch (IOException e) {
      e.printStackTrace();
      throw new ImagingException("Exception while finding image format", srcImage);
    }
    return formatName;
  }

  public static Long copyFile(File srcFile, File destFile, boolean verify)
      throws IOException {
    if (srcFile.getAbsolutePath().equals(destFile.getAbsolutePath())) {
      return null;
    }
    File destDir = destFile.getParentFile();
    destDir.mkdirs();
    InputStream in = new FileInputStream(srcFile);
    OutputStream out = new FileOutputStream(destFile);
    CRC32 checksum = null;
    if (verify) {
      checksum = new CRC32();
      checksum.reset();
    }
    byte[] buffer = new byte[32768];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) >= 0) {
      if (verify) {
        checksum.update(buffer, 0, bytesRead);
      }
      out.write(buffer, 0, bytesRead);
    }
    out.close();
    in.close();
    if (verify) {
      return checksum.getValue();
    } else {
      return null;
    }
  }
}
