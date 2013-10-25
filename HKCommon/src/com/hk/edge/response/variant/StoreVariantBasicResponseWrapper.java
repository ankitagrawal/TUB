package com.hk.edge.response.variant;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.constants.DtoJsonConstants;
import com.hk.edge.response.AbstractBaseResponseWrapperFromEdge;

public class StoreVariantBasicResponseWrapper extends AbstractBaseResponseWrapperFromEdge {

    @JsonProperty(DtoJsonConstants.RESULTS)
    private StoreVariantBasicResponse storeVariantBasic;


    public StoreVariantBasicResponse getStoreVariantBasic() {
      return storeVariantBasic;
    }

    public void setStoreVariantBasic(StoreVariantBasicResponse storeVariantBasic) {
      this.storeVariantBasic = storeVariantBasic;
    }
  }
