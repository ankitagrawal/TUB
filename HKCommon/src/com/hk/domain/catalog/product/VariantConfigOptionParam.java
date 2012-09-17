package com.hk.domain.catalog.product;

import org.apache.commons.lang.StringUtils;

/**
 * @author vaibhav.adlakha
 */
public enum VariantConfigOptionParam {
    THICKNESS("TH"), BFTHICKNESS("THBF"), COATING("CO"), BFCOATING("COBF"), PRESCRIPTION("PR"), ENGRAVING("ENG"),
    BRANDCO("BRANDCO"), BRANDTH("BRANDTH"), BRANDTHBF("BRANDTHBF");

    public String param() {
        return this.param;
    }

    public static boolean shouldPriceBeDoubledForParam(String addParam) {
        boolean result = false;
        if (StringUtils.isNotEmpty(addParam)) {
            if (addParam.equalsIgnoreCase(THICKNESS.param) || addParam.equalsIgnoreCase(BFTHICKNESS.param) || addParam.equalsIgnoreCase(COATING.param)
                    || addParam.equalsIgnoreCase(BFCOATING.param) || addParam.equalsIgnoreCase(BRANDCO.param) 
                    || addParam.equalsIgnoreCase(BRANDTH.param) || addParam.equalsIgnoreCase(BRANDTHBF.param)) {
                result = true;
            }
        }
        return result;
    }

    private String param;

    VariantConfigOptionParam(String param) {
        this.param = param;
    }
    
    
}
