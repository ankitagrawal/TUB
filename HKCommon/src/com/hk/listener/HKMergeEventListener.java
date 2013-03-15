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

        /*
         * if (Product.class.equals(entityClass)) { Product newProduct = (Product) event.getOriginal();
         * audit(newProduct.getId(), newProduct); } else
         */

        if (ProductVariant.class.equals(entityClass)) {
            ProductVariant newProductVariant = (ProductVariant) event.getOriginal();
            audit(newProductVariant.getId(), newProductVariant);
        }

    }

    private void audit(String productVariantId, ProductVariant savedProductVariant) {

        String oldProductVariantJson = getOriginalProductVariantById(productVariantId);
        User loggedUser = getUserService().getLoggedInUser();

        JSONResponseBuilder newJsonBuilder = new JSONResponseBuilder();

        newJsonBuilder.addField("id", savedProductVariant.getId());
       // newJsonBuilder.addField("hk_price", savedProductVariant.getHkPrice());
        //newJsonBuilder.addField("b2b_price", savedProductVariant.getB2bPrice());
        //newJsonBuilder.addField("marked_price", savedProductVariant.getMarkedPrice());
        //newJsonBuilder.addField("discount_percent", savedProductVariant.getDiscountPercent());
        //newJsonBuilder.addField("cost_price", savedProductVariant.getCostPrice());
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
    public String getOriginalProductVariantById(String productVariantId) {

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
                //oldProductVariantJsonBuilder.addField("hk_price", productVariant[1]);
                //oldProductVariantJsonBuilder.addField("b2b_price", productVariant[2]);
                //oldProductVariantJsonBuilder.addField("marked_price", productVariant[3]);
                //oldProductVariantJsonBuilder.addField("discount_percent", productVariant[4]);
                //oldProductVariantJsonBuilder.addField("cost_price", productVariant[5]);
                oldProductVariantJsonBuilder.addField("out_of_stock", productVariant[6]);
                oldProductVariantJsonBuilder.addField("deleted", productVariant[7]);

                if (options != null) {
                    Map<String, String> optionsMap = new HashMap<String, String>();
                    for (Object[] option : options) {
                        optionsMap.put((String) option[0], (String) option[1]);
                    }

                    oldProductVariantJsonBuilder.addField("options", optionsMap);
                }

            }
        } catch (Exception e) {
            logger.error("Error getting old pv :" + productVariantId);
        }

        return oldProductVariantJsonBuilder.build();
    }

    /*
     * public Product getOriginalProductById(String productId, Session session) { Product product = (Product)
     * session.createQuery("select p from Product p where p.id=:productId").setString("productId",
     * productId).uniqueResult(); if (product != null) {
     * product.setCategoriesPipeSeparated(product.getPipeSeparatedCategories()); } return product; }
     */

    /*
     * private void audit(String productId, Product savedProduct) { SessionFactory sessionFactory = (SessionFactory)
     * ServiceLocatorFactory.getService("newSessionFactory"); Session session = sessionFactory.openSession(); // Product
     * oldProduct = getOriginalProductById(productId, session); User loggedUser = getUserService().getLoggedInUser();
     * try { EntityAuditTrail eat = new EntityAuditTrail(); eat.setEntityId(productId); Gson gson = (new
     * GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create(); String oldJson = ""; if (oldProduct != null)
     * oldJson = gson.toJson(oldProduct); eat.setOldJson(oldJson);
     * savedProduct.setCategoriesPipeSeparated(savedProduct.getPipeSeparatedCategories()); String newJson =
     * gson.toJson(savedProduct); eat.setNewJson(newJson); // if
     * (!BaseUtils.getMD5Checksum(oldJson).equals(BaseUtils.getMD5Checksum(newJson))) // { eat.setUserEmail(loggedUser !=
     * null ? loggedUser.getEmail() : "system"); eat.setCallingClass(Reflection.getCallerClass(2).getName());
     * eat.setStackTrace(JsonUtils.getGsonDefault().toJson(Thread.currentThread().getStackTrace())); eat.setCreateDt(new
     * Date()); try { session.save(eat); } catch (Exception e) { logger.error("Error while entering audit trail for
     * product->" + productId, e); } finally { if (session != null) { logger.info("Closing connection 1"); //
     * session.flush(); // session.clear(); session.close(); // session.connection().close(); logger.info("Closed
     * connection 1"); } } // } } catch (Exception e) { logger.error("Error while entering audit trail for product->" +
     * productId, e); } }
     */

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
