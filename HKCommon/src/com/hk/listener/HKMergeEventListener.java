package com.hk.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.event.MergeEvent;
import org.hibernate.event.def.DefaultMergeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.Reflection;

import com.akube.framework.gson.JsonUtils;
import com.hk.domain.catalog.product.EntityAuditTrail;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.json.JSONResponseBuilder;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class HKMergeEventListener extends DefaultMergeEventListener {

    private static Logger logger = LoggerFactory.getLogger(HKMergeEventListener.class);

    private UserService   userService;

    private BaseDao       baseDao;

    @SuppressWarnings("unchecked")
    @Override
    public void onMerge(MergeEvent event) throws HibernateException {
        super.onMerge(event);

        Class entityClass = event.getOriginal().getClass();

        if (Product.class.equals(entityClass)) {
            Product newProduct = (Product) event.getOriginal();
            audit(newProduct.getId(), newProduct);
        } else if (ProductVariant.class.equals(entityClass)) {
            ProductVariant newProductVariant = (ProductVariant) event.getOriginal();
            audit(newProductVariant.getId(), newProductVariant);
        }

    }

    private void audit(String productVariantId, ProductVariant savedProductVariant) {

        JSONResponseBuilder oldProductVariantJsonBuilder = getOriginalProductVariantById(productVariantId);

        String oldProductVariantJson = oldProductVariantJsonBuilder.toString();
        User loggedUser = getUserService().getLoggedInUser();

        JSONResponseBuilder newJsonBuilder = new JSONResponseBuilder();

        Double newMarkedPrice = savedProductVariant.getMarkedPrice();
        Double oldMarkedPrice = (Double) oldProductVariantJsonBuilder.getParam("marked_price");
        boolean newOOS = savedProductVariant.isOutOfStock();
        boolean oldOOS = (Boolean) oldProductVariantJsonBuilder.getParam("out_of_stock");

        boolean syncMrpOOS = false;
        if (!newMarkedPrice.equals(oldMarkedPrice)) {
            syncMrpOOS = true;
        }
        if (newOOS != oldOOS) {
            syncMrpOOS = true;
        }

        newJsonBuilder.addField("id", savedProductVariant.getId());
        newJsonBuilder.addField("hk_price", savedProductVariant.getHkPrice());
        newJsonBuilder.addField("b2b_price", savedProductVariant.getB2bPrice());
        newJsonBuilder.addField("marked_price", savedProductVariant.getMarkedPrice());
        newJsonBuilder.addField("discount_percent", savedProductVariant.getDiscountPercent());
        newJsonBuilder.addField("cost_price", savedProductVariant.getCostPrice());
        newJsonBuilder.addField("out_of_stock", savedProductVariant.isOutOfStock() ? 1 : 0);
        newJsonBuilder.addField("deleted", savedProductVariant.isDeleted() ? 1 : 0);

        List<ProductOption> savedOptions = savedProductVariant.getProductOptions();
        if (savedOptions != null) {
            Map<String, String> optionsMap = new HashMap<String, String>();
            for (ProductOption option : savedOptions) {
                optionsMap.put(option.getName(), option.getValue());
            }

            newJsonBuilder.addField("options", optionsMap);
        }

        String newProductVariantJson = newJsonBuilder.build();

        if (!oldProductVariantJson.equals(newProductVariantJson)) {
            try {
                EntityAuditTrail eat = new EntityAuditTrail();
                eat.setEntityId(productVariantId);
                eat.setOldJson(oldProductVariantJson);
                eat.setNewJson(newProductVariantJson);
                eat.setUserEmail(loggedUser != null ? loggedUser.getEmail() : "system");
                eat.setCallingClass(Reflection.getCallerClass(2).getName());
                eat.setStackTrace(JsonUtils.getGsonDefault().toJson(Thread.currentThread().getStackTrace()));
                eat.setCreateDt(new Date());

                getBaseDao().save(eat);
            } catch (Exception e) {
                logger.error("Error while entering audit trail for product->" + productVariantId, e);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public JSONResponseBuilder getOriginalProductVariantById(String productVariantId) {

        JSONResponseBuilder oldProductVariantJsonBuilder = new JSONResponseBuilder();
        try {
            List<Object[]> pvProperties = getBaseDao().createSqlQuery(
                    "select pv.id, pv.hk_price, pv.b2b_price, pv.marked_price,pv.discount_percent,"
                            + " pv.cost_price,pv.out_of_stock, pv.deleted from product_variant pv where pv.id = '" + productVariantId + "'").list();
            List<Object[]> options = getBaseDao().createSqlQuery(
                    "select po.name,po.value from product_variant pv " + " join product_variant_has_product_option pvo on pv.id = product_variant_id join product_option po "
                            + " on po.id = pvo.product_option_id where pv.id = '" + productVariantId + "'").list();

            if (pvProperties != null && pvProperties.size() > 0) {
                Object[] productVariant = pvProperties.get(0);

                oldProductVariantJsonBuilder.addField("id", productVariant[0]);
                oldProductVariantJsonBuilder.addField("hk_price", productVariant[1]);
                oldProductVariantJsonBuilder.addField("b2b_price", productVariant[2]);
                oldProductVariantJsonBuilder.addField("marked_price", productVariant[3]);
                oldProductVariantJsonBuilder.addField("discount_percent", productVariant[4]);
                oldProductVariantJsonBuilder.addField("cost_price", productVariant[5]);
                oldProductVariantJsonBuilder.addField("out_of_stock", productVariant[6]);
                oldProductVariantJsonBuilder.addField("deleted", productVariant[7]);

                if (options != null) {
                    Map<String, String> optionsMap = new HashMap<String, String>();
                    for (Object[] option : options) {
                        optionsMap.put((String) option[0], (String) option[1]);
                    }

                    oldProductVariantJsonBuilder.addField("options", optionsMap);
                }

            } else {
                oldProductVariantJsonBuilder.addField("id", "NEW_VARIANT");
            }
        } catch (Exception e) {
            logger.error("Error getting old pv :" + productVariantId);
        }

        return oldProductVariantJsonBuilder;
    }

    @SuppressWarnings("unchecked")
    public String getOriginalProductById(String productId) {

        JSONResponseBuilder oldProductJsonBuilder = new JSONResponseBuilder();
        try {
            List<Object[]> productProperties = getBaseDao().createSqlQuery(
                    "select p.id, p.name, p.is_google_ad_disallowed, p.is_jit,p.hidden,"
                            + " p.drop_shipping,p.cod_allowed, p.out_of_stock, p.deleted from product p where p.id = '" + productId + "'").list();

            if (productProperties != null && productProperties.size() > 0) {
                Object[] product = productProperties.get(0);

                oldProductJsonBuilder.addField("id", product[0]);
                oldProductJsonBuilder.addField("name", product[1]);
                oldProductJsonBuilder.addField("is_google_ad_disallowed", product[2]);
                oldProductJsonBuilder.addField("is_jit", product[3]);
                oldProductJsonBuilder.addField("hidden", product[4]);
                oldProductJsonBuilder.addField("drop_shipping", product[5]);
                oldProductJsonBuilder.addField("cod_allowed", product[6]);
                oldProductJsonBuilder.addField("out_of_stock", product[7]);
                oldProductJsonBuilder.addField("deleted", product[7]);

            } else {
                oldProductJsonBuilder.addField("id", "NEW_PRODUCT");
            }
        } catch (Exception e) {
            logger.error("Error getting old product :" + productId);
        }

        return oldProductJsonBuilder.build();
    }

    private void audit(String productId, Product savedProduct) {
        String oldProductJson = getOriginalProductById(productId);
        User loggedUser = getUserService().getLoggedInUser();

        JSONResponseBuilder newJsonBuilder = new JSONResponseBuilder();

        newJsonBuilder.addField("id", savedProduct.getId());
        newJsonBuilder.addField("name", savedProduct.getName());
        newJsonBuilder.addField("is_google_ad_disallowed", savedProduct.isGoogleAdDisallowed() ? 1 : 0);
        newJsonBuilder.addField("is_jit", savedProduct.isJit() ? 1 : 0);
        newJsonBuilder.addField("hidden", savedProduct.isHidden() ? 1 : 0);
        newJsonBuilder.addField("drop_shipping", savedProduct.isDropShipping() ? 1 : 0);
        newJsonBuilder.addField("cod_allowed", savedProduct.isCodAllowed() ? 1 : 0);
        newJsonBuilder.addField("out_of_stock", savedProduct.isOutOfStock() ? 1 : 0);
        newJsonBuilder.addField("deleted", savedProduct.isDeleted() ? 1 : 0);

        String newProductJson = newJsonBuilder.build();

        if (!oldProductJson.equals(newProductJson)) {
            try {
                EntityAuditTrail eat = new EntityAuditTrail();
                eat.setEntityId(productId);
                eat.setOldJson(oldProductJson);
                eat.setNewJson(newProductJson);
                eat.setUserEmail(loggedUser != null ? loggedUser.getEmail() : "system");
                eat.setCallingClass(Reflection.getCallerClass(2).getName());
                eat.setStackTrace(JsonUtils.getGsonDefault().toJson(Thread.currentThread().getStackTrace()));
                eat.setCreateDt(new Date());

                getBaseDao().save(eat);
            } catch (Exception e) {
                logger.error("Error while entering audit trail for product->" + productId, e);
            }
        }

    }

    public UserService getUserService() {
        if (userService == null) {
            userService = (UserService) ServiceLocatorFactory.getService(UserService.class);
        }
        return userService;
    }

    public BaseDao getBaseDao() {
        if (baseDao == null) {
            baseDao = (BaseDao) ServiceLocatorFactory.getService(BaseDao.class);
        }
        return baseDao;
    }

}
