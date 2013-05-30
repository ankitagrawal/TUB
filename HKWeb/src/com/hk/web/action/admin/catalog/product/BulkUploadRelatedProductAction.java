package com.hk.web.action.admin.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 5/30/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkUploadRelatedProductAction extends BaseAction {

    @DefaultHandler
     @DontValidate
     public Resolution pre() {
       return new ForwardResolution("/pages/bulkUploadRelatedProduct.jsp");
     }

}
