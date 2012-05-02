package com.hk.util;

/*
 * Copyright (c) 2011 Tribal Fusion, Inc. All rights reserved.
 * User: Vikash Agarwal
 * Date: January 31, 2011
 */
public enum SiteCrawlCompressionMode {
    GzipAndDeflate, Gzip, Deflate, None;

    public static SiteCrawlCompressionMode getSiteCrawlCompressionMode(String siteCrawlCompressionMode) {
        return siteCrawlCompressionMode == null ? SiteCrawlCompressionMode.None :
                siteCrawlCompressionMode.equalsIgnoreCase("GzipAndDeflate") ? SiteCrawlCompressionMode.GzipAndDeflate :
                        siteCrawlCompressionMode.equalsIgnoreCase("Gzip") ? SiteCrawlCompressionMode.Gzip :
                                siteCrawlCompressionMode.equalsIgnoreCase("Deflate") ? SiteCrawlCompressionMode.Deflate : SiteCrawlCompressionMode.None;
    }

    public String getRequestHeader() {
        if (equals(SiteCrawlCompressionMode.GzipAndDeflate)) {
            return "gzip,deflate";
        } else if (equals(SiteCrawlCompressionMode.Gzip)) {
            return "gzip";
        } else if (equals(SiteCrawlCompressionMode.Deflate)) {
            return "deflate";
        }
        return null;
    }
}
