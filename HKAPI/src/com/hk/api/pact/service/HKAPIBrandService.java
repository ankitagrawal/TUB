package com.hk.api.pact.service;

import com.hk.api.dto.HKAPIBaseDTO;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 5/14/13
 * Time: 2:20 PM
 */
public interface HKAPIBrandService {

    public HKAPIBaseDTO getAllProductsByBrand(String brand);

}
