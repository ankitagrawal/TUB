package com.hk.web.action.admin.catalog.product;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import com.akube.framework.stripes.action.BaseAction;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Aug 18, 2012
 * Time: 5:31:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddEyeConfigAction extends BaseAction {

    String productVariantList = "";
    Integer configId = 0;

   @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/AddEyeConfig.jsp");

    }

    public Resolution save(){
         String [] poductVariantArray =         productVariantList.split(",");
        for (String prodctVariantId : poductVariantArray) {
            updateProductVariantsConfig (prodctVariantId,configId);
            
        }
        
        return new ForwardResolution("/pages/admin/AddEyeConfig.jsp");
    }

    public String getProductVariantList() {
           return productVariantList;
       }

       public void setProductVariantist(String productVariantist) {
           this.productVariantList = productVariantist;
       }

}
