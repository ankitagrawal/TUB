package com.hk.web.action.admin.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 4/3/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddVirtualTryOnAction extends BaseAction {

    private String productVariantList;

    @DefaultHandler
    public Resolution pre() {
           return new ForwardResolution("/pages/admin/addVirtualTryOn.jsp");

    }
    public Resolution save(){
        String[] productVariantArray = productVariantList.split(",");
        for(String ProductVariantId:productVariantArray){

        }
        return new RedirectResolution("/pages/admin/addVirtualTryOn.jsp");
    }

}
