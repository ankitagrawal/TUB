package com.hk.web.action.admin.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.service.catalog.ProductVariantService;
import net.sourceforge.stripes.action.*;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 4/3/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddVirtualTryOnAction extends BaseAction {
    @Autowired
    ProductVariantService productVariantService;

    private String productVariantList;
    private final Long optionId = 14166L;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/addVirtualTryOn.jsp");

    }

    public Resolution save() {
        String[] productVariantArray = productVariantList.split(",");
        ProductOption productOption = getProductVariantService().getProductOptionById(optionId);
        for (String productVariantId : productVariantArray) {
            ProductVariant productVariant = getProductVariantService().getVariantById(productVariantId);
            if (productVariant == null) {
                addRedirectAlertMessage(new SimpleMessage("Product Variant " + productVariantId + " is not available"));
            } else {
                ProductVariant productVariant1 = getProductVariantService().getVariantByTryOn(productVariantId);
                if (productVariant1 == null) {
                    addRedirectAlertMessage(new SimpleMessage("Product Variant " + productVariantId + " is already have TryOn Filter"));
                } else {
                boolean productVariantBool=getProductVariantService().isImageType(productVariantId);
                    if(!productVariantBool)  {
                    addRedirectAlertMessage(new SimpleMessage("Product Variant " + productVariantId + " do not have image type 7"));
                } else {
                     List<ProductOption> productOptions = new ArrayList<ProductOption>();
                    if(productVariant.getProductOptions()!=null){
                        productOptions = productVariant.getProductOptions();
                    }
                    productOptions.add(productOption);
                    productVariant.setProductOptions(productOptions);
                    getProductVariantService().save(productVariant);
                    addRedirectAlertMessage(new SimpleMessage("Database updated"));
                }
                }
            }
        }
        return new RedirectResolution("/pages/admin/addVirtualTryOn.jsp");
    }


    public String getProductVariantList() {
        return productVariantList;
    }

    public void setProductVariantList(String productVariantList) {
        this.productVariantList = productVariantList;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }
}
