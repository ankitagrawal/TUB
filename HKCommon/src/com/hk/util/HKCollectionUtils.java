package com.hk.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/16/13
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class HKCollectionUtils {

  private static Logger logger = LoggerFactory.getLogger(HKCollectionUtils.class);

  public static boolean listContainsKey(List<String> sourceList, String key) {
    for (String source : sourceList) {
      if (source.equalsIgnoreCase(key)) {
        return true;
      }
    }
    return false;
  }

  /**
   * This API checks and returns the first duplicate parameter from a list of objects where
   * duplicity is based on user defined properties of the objects. The duplicate object needs
   * to be down casted for further use.
   * @param objectList- List in which to find duplicity
   * @param delimiter- User defined delimiter for key ('#" default delimiter if no delimiter given)
   * @param propertyNames- Object properties on which uniqueness has to be checked
   */
  public static Object findDuplicate( List<? extends Object> objectList, String delimiter , String... propertyNames) {

    Map<String,Object> storageMap = new HashMap<String, Object>();
    StringBuilder key;
    if (delimiter == null || delimiter.isEmpty()) {
      // use a default delimiter
      delimiter = "#";
    }
    for (Object object: objectList) {
      key = new StringBuilder();

      // for all the properties mentioned, access their values
      for (String propertyName : propertyNames) {
        try {
          Object propertyObject = PropertyUtils.getProperty(object, propertyName);
          key.append(propertyObject.toString()).append(delimiter);
        } catch (IllegalAccessException iae) {
          logger.error(iae.getMessage());
          return object;
        }catch (NoSuchMethodException nme) {
          logger.error(nme.getMessage());
          return object;
        }catch (InvocationTargetException ivt) {
          logger.error(ivt.getMessage());
          return object;
        }
      }
      if (!storageMap.containsKey(key.toString())) {
        storageMap.put(key.toString(),object);
      } else {
        return object;
      }
    }
    return null;
  }
}
