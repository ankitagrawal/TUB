package com.hk.web.action.admin.inventory;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.product.SimilarProductsDao;
import com.hk.pact.service.catalog.ProductService;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.SimilarProduct;

import com.hk.web.HealthkartResponse;
import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: Neha Date: Aug 17, 2012 Time: 5:50:32 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class EditSimilarProductsAction extends BaseAction {
    private static Logger  logger              = LoggerFactory.getLogger(EditSimilarProductsAction.class);

    @Autowired
    BaseDao                baseDao;

    @Autowired
    private ProductService productService;

    @Autowired
    SimilarProductsDao     similarProductsDao;

    public String          productId;
    Product                inputProduct;

    List<SimilarProduct>   similarProductsList = new ArrayList<SimilarProduct>();
    SimilarProduct         similarProductToDel;

    @DefaultHandler
    public Resolution pre() {
        logger.debug("similarProduct@Pre: ");
        return new ForwardResolution("/pages/admin/addSimilarProducts.jsp");
    }

    @SuppressWarnings("unchecked")
    public Resolution getPVDetails() {
        Map dataMap = new HashMap();
        if (StringUtils.isNotBlank(productId)) {
            Product p = getProductService().getProductById(productId);
            if (p != null) {
                try {
                    dataMap.put("product", p.getName());
                    // dataMap.put("options", pv.getOptionsCommaSeparated());
                    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product", dataMap);
                    noCache();
                    return new JsonResolution(healthkartResponse);
                } catch (Exception e) {
                    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(), dataMap);
                    noCache();
                    return new JsonResolution(healthkartResponse);
                }
            }
        } else {
            logger.error("null or empty product id passed to load pv details in getPvDetails method of EditSimilarProductsAction");
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product ID", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
    }

    public Resolution search() {
        if (inputProduct != null && inputProduct.getId() != null) {
            similarProductsList = inputProduct.getSimilarProducts();
        } else {
            similarProductsList = new ArrayList<SimilarProduct>();
            addRedirectAlertMessage(new SimpleMessage("Please enter a valid product in the text field above."));
        }
        if (similarProductsList.size() == 0) {
            addRedirectAlertMessage(new SimpleMessage("No similar products exist for this product"));
        }
        return new ForwardResolution("/pages/admin/addSimilarProducts.jsp");
    }

    public Resolution delete() {
        if (similarProductToDel != null) {
            baseDao.delete(similarProductToDel);
        }
        return new RedirectResolution(EditSimilarProductsAction.class, "search").addParameter("inputProduct", inputProduct.getId());
    }

    public Resolution update() {
        if (similarProductsList.size() != 0) {
            for (SimilarProduct similarItem : similarProductsList) {
                baseDao.update(similarItem);
            }
            addRedirectAlertMessage(new SimpleMessage("Updated Changes."));
        } else {
            addRedirectAlertMessage(new SimpleMessage("Nothing to Update"));
        }
        return new RedirectResolution(EditSimilarProductsAction.class, "search").addParameter("inputProduct", inputProduct.getId());
    }

    public Resolution save() {
        if (inputProduct != null && inputProduct.getId() != null) {
            for (SimilarProduct similarItem : similarProductsList) {
                similarItem.setProduct(inputProduct);
                baseDao.save(similarItem);
            }
            addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please enter a valid product in the text field above."));
        }

        RedirectResolution redirectResolution = new RedirectResolution(EditSimilarProductsAction.class, "search");
        
        if(inputProduct.getId() !=null){
            redirectResolution.addParameter("inputProduct", inputProduct.getId());
        }
        return redirectResolution;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public Product getInputProduct() {
        return inputProduct;
    }

    public void setInputProduct(Product inputProduct) {
        this.inputProduct = inputProduct;
    }

    public List<SimilarProduct> getSimilarProductsList() {
        return similarProductsList;
    }

    public void setSimilarProductsList(List<SimilarProduct> similarProductsList) {
        this.similarProductsList = similarProductsList;
    }

    public SimilarProduct getSimilarProductToDel() {
        return similarProductToDel;
    }

    public void setSimilarProductToDel(SimilarProduct similarProductToDel) {
        this.similarProductToDel = similarProductToDel;
    }
}
