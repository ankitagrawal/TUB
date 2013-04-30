package com.hk.constants.marketing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 19/04/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumMarketingFeed {
    Google_PLA("Google PLA"),
    Google_DR("Google DR");

    EnumMarketingFeed(String name){
        this.name = name;
    }

    String name;
    String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<String> getAllFeeds(){
        List<String> feedNames = new ArrayList<String>();
        feedNames.add(EnumMarketingFeed.Google_PLA.getName());
        feedNames.add(EnumMarketingFeed.Google_DR.getName());
        return feedNames;
    }
}
