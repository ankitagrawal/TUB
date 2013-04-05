package com.hk.web.action.admin.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.*;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 4/3/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddVirtualTryOnAction extends BaseAction {
    @Autowired
    AdminProductVariantInventoryDao adminProductVariantInventoryDao;

    private Long optionId;

    private String productVariantList;

    @DefaultHandler
    public Resolution pre() {
           return new ForwardResolution("/pages/admin/addVirtualTryOn.jsp");

    }
    public Resolution save(){
        String[] productVariantArray = productVariantList.split(",");
        for(String ProductVariantId:productVariantArray){
            adminProductVariantInventoryDao.updateProductVariantsTryOn(ProductVariantId,optionId);
        }
        getContext().getMessages().add(new SimpleMessage("Database updated"));
        return new RedirectResolution("/pages/admin/addVirtualTryOn.jsp");
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getProductVariantList() {
        return productVariantList;
    }

    public void setProductVariantList(String productVariantList) {
        this.productVariantList = productVariantList;
    }
}
