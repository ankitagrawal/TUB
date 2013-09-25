package com.hk.api.edge.integration.response.variant;

import com.hk.api.edge.integration.response.AbstractResponseFromHKR;

/**
 * @author Rimal
 */
public class ComboResponseFromHKR extends AbstractResponseFromHKR {

    @Override
    protected String[] getKeys() {
        return new String[]{"exception", "msgs"};
    }

    @Override
    protected Object[] getValues() {
        return new Object[]{this.exception, this.msgs};
    }
}
