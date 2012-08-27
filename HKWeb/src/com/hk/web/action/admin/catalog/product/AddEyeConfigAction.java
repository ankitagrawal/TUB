package com.hk.web.action.admin.catalog.product;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.sourceforge.stripes.validation.ValidationError;


/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Aug 18, 2012
 * Time: 5:31:34 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class AddEyeConfigAction extends BaseAction {
    @Autowired
    AdminProductVariantInventoryDao adminProductVariantInventoryDao;

    //    @Validate(required = true)
    private String productVariantList;

    private Long configId;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/AddEyeConfig.jsp");

    }

    public Resolution save() {
        String[] poductVariantArray = productVariantList.split(",");
        for (String prodctVariantId : poductVariantArray) {
            adminProductVariantInventoryDao.updateProductVariantsConfig(prodctVariantId, configId);
        }
        getContext().getMessages().add(new SimpleMessage("Database updated"));
        return new RedirectResolution("/pages/admin/AddEyeConfig.jsp");
    }

    public String getProductVariantList() {
        return productVariantList;
    }

    public void setProductVariantList(String productVariantList) {
        this.productVariantList = productVariantList;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

}
