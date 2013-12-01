package com.hk.listener;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.event.MergeEvent;
import org.hibernate.event.def.DefaultMergeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.Reflection;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.util.BaseUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hk.domain.catalog.product.EntityAuditTrail;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.service.ServiceLocatorFactory;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class HKMergeEventListenerCopy extends DefaultMergeEventListener {

    private static Logger logger = LoggerFactory.getLogger(HKMergeEventListenerCopy.class);

    private UserService   userService;

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
        SessionFactory sessionFactory = (SessionFactory) ServiceLocatorFactory.getService("newSessionFactory");
        Session session = sessionFactory.openSession();
     //   ProductVariant oldProductVariant = getOriginalProductVariantById(productVariantId, session);
        User loggedUser = getUserService().getLoggedInUser();
       /* if (oldProductVariant != null) {
            oldProductVariant.setOptionsAuditString(oldProductVariant.getOptionsPipeSeparated());
        }*/

        try {
            EntityAuditTrail eat = new EntityAuditTrail();
            eat.setEntityId(productVariantId);
            Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
         /*   String oldJson = "";
            if (oldProductVariant != null)
                oldJson = gson.toJson(oldProductVariant);*/
            //eat.setOldJson(oldJson);
            savedProductVariant.setOptionsAuditString(savedProductVariant.getOptionsPipeSeparated());
            String newJson = gson.toJson(savedProductVariant);
            eat.setNewJson(newJson);
            //if (!BaseUtils.getMD5Checksum(oldJson).equals(BaseUtils.getMD5Checksum(newJson))) 
            //{
                eat.setUserEmail(loggedUser != null ? loggedUser.getEmail() : "system");
                eat.setCallingClass(Reflection.getCallerClass(2).getName());
                eat.setStackTrace(JsonUtils.getGsonDefault().toJson(Thread.currentThread().getStackTrace()));
                eat.setCreateDt(new Date());

                try {
                    session.save(eat);
                } catch (Exception e) {
                    logger.error("Error while entering audit trail for product->" + productVariantId, e);
                } finally {
                    if (session != null) {
                        logger.info("Closing connection");
                       // session.flush();
                        //session.clear();
                        session.close();
                        // session.connection().close();

                        logger.info("Closed connection");
                    }
                //}

            }
        } catch (Exception e) {
            logger.error("Error while entering audit trail for product->" + productVariantId, e);
        }

    }

    public ProductVariant getOriginalProductVariantById(String productVariantId, Session session) {

        ProductVariant productVariant = (ProductVariant) session.createQuery("select pv from ProductVariant pv where pv.id=:productVariantId").setString("productVariantId",
                productVariantId).uniqueResult();

        return productVariant;
    }

    public Product getOriginalProductById(String productId, Session session) {

        Product product = (Product) session.createQuery("select p from Product p where p.id=:productId").setString("productId", productId).uniqueResult();
        if (product != null) {
            product.setCategoriesPipeSeparated(product.getPipeSeparatedCategories());
        }

        return product;
    }

    private void audit(String productId, Product savedProduct) {
        SessionFactory sessionFactory = (SessionFactory) ServiceLocatorFactory.getService("newSessionFactory");
        Session session = sessionFactory.openSession();
        //Product oldProduct = getOriginalProductById(productId, session);
        User loggedUser = getUserService().getLoggedInUser();

        try {
            EntityAuditTrail eat = new EntityAuditTrail();
            eat.setEntityId(productId);
            Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
          /*  String oldJson = "";
            if (oldProduct != null)
                oldJson = gson.toJson(oldProduct);
            eat.setOldJson(oldJson);*/
            savedProduct.setCategoriesPipeSeparated(savedProduct.getPipeSeparatedCategories());
            String newJson = gson.toJson(savedProduct);
            eat.setNewJson(newJson);
            //if (!BaseUtils.getMD5Checksum(oldJson).equals(BaseUtils.getMD5Checksum(newJson))) 
            //{
                eat.setUserEmail(loggedUser != null ? loggedUser.getEmail() : "system");
                eat.setCallingClass(Reflection.getCallerClass(2).getName());
                eat.setStackTrace(JsonUtils.getGsonDefault().toJson(Thread.currentThread().getStackTrace()));
                eat.setCreateDt(new Date());

                try {

                    session.save(eat);
                } catch (Exception e) {
                    logger.error("Error while entering audit trail for product->" + productId, e);
                } finally {
                    if (session != null) {
                        logger.info("Closing connection 1");
                       // session.flush();
                       // session.clear();
                        session.close();
                        // session.connection().close();

                        logger.info("Closed connection 1");
                    }
                }

            //}
        } catch (Exception e) {
            logger.error("Error while entering audit trail for product->" + productId, e);
        }
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = (UserService) ServiceLocatorFactory.getService(UserService.class);
        }
        return userService;
    }

}
