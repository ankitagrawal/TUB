package com.hk.util;


import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.iptc.IptcDirectory;



public class ImageTagReader {

  private static Logger logger = Logger.getLogger(ImageTagReader.class);

  /** This method will return the set of all the IPTC tags.
   *
   * 
   * @param imageFilePath    It should be path to a jpeg file only.
   * @return
   * @throws Exception
   */

  public Set<String> readImageMetadata(String imageFilePath) throws Exception {
    logger.debug("imageFilePath : "+imageFilePath);
    Set<String> tags = new HashSet<String>();
    File jpegFile = new File(imageFilePath);
    Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
    Directory directory = metadata.getDirectory(IptcDirectory.class);

    //modification due to null pointer exception in the case when no Iptc keywords of any type were there.
    if (directory != null) {
      String[] keywordArray = directory.getStringArray(IptcDirectory.TAG_KEYWORDS);
      if (keywordArray != null) {
        logger.debug("keywordArray : "+keywordArray.toString());
        tags.addAll(Arrays.asList(keywordArray));
      }
    }
    return tags;
  }
   
}
