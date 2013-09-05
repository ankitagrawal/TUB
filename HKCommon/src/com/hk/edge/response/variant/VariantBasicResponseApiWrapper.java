package com.hk.edge.response.variant;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.AbstractBaseResponseApiWrapper;
import com.hk.edge.constants.DtoJsonConstants;

public class VariantBasicResponseApiWrapper extends AbstractBaseResponseApiWrapper {

    @JsonProperty(DtoJsonConstants.RESULTS)
    private StoreVariantBasicApiResponse storeVariantBasic;


    public StoreVariantBasicApiResponse getStoreVariantBasic() {
      return storeVariantBasic;
    }

    public void setStoreVariantBasic(StoreVariantBasicApiResponse storeVariantBasic) {
      this.storeVariantBasic = storeVariantBasic;
    }
  }
