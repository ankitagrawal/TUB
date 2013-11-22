package com.hk.api.edge.integration.response.variant.response;

import com.hk.api.edge.integration.response.AbstractResponseFromHKR;
import com.hk.api.edge.integration.response.variant.ComboHKR;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rimal
 */
public class ComboResponseFromHKR extends AbstractResponseFromHKR {

    private List<ComboHKR> comboHKRList = new ArrayList<ComboHKR>();


    public List<ComboHKR> getComboHKRList() {
        return comboHKRList;
    }

    public void setComboHKRList(List<ComboHKR> comboHKRList) {
        this.comboHKRList = comboHKRList;
    }


    @Override
    protected String[] getKeys() {
        return new String[]{"combos", "exception", "msgs"};
    }

    @Override
    protected Object[] getValues() {
        return new Object[]{this.comboHKRList, this.exception, this.msgs};
    }
}