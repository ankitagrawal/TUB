package com.hk.impl.service.mooga;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 7/13/12
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryPriority {
    private static final Map<String, Integer> categoryRecommendationLevelMap;
    static {
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put("services", 3);
        categoryRecommendationLevelMap = Collections.unmodifiableMap(aMap);
    }

    public static int getRecommendationLevel(String primaryCategory){
       return (categoryRecommendationLevelMap.get(primaryCategory) != null) ?
               categoryRecommendationLevelMap.get(primaryCategory) : 1;
    }
}
