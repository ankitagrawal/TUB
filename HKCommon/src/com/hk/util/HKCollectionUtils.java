package com.hk.util;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/16/13
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class HKCollectionUtils {

    public static boolean listContainsKey(List<String> sourceList, String key) {
        for (String source : sourceList) {
            if (source.equalsIgnoreCase(key)) {
                return true;
            }

        }
        return false;

    }

}
