package com.hk.api.edge.internal.response.variant;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.api.edge.constants.DtoJsonConstants;
import com.hk.api.edge.internal.response.AbstractBaseResponseApiWrapper;

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
