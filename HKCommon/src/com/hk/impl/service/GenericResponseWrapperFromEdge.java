package com.hk.impl.service;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.constants.DtoJsonConstants;
import com.hk.edge.response.AbstractBaseResponseWrapperFromEdge;

public class GenericResponseWrapperFromEdge extends AbstractBaseResponseWrapperFromEdge {

    @JsonProperty(DtoJsonConstants.RESULTS)
    private GenericResponseFromEdge genericResponseFromEdge;

    public GenericResponseFromEdge getGenericResponseFromEdge() {
        return genericResponseFromEdge;
    }

    public void setGenericResponseFromEdge(GenericResponseFromEdge genericResponseFromEdge) {
        this.genericResponseFromEdge = genericResponseFromEdge;
    }

}
