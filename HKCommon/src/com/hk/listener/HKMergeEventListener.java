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
public class HKMergeEventListener extends DefaultMergeEventListener {

    private static Logger logger = LoggerFactory.getLogger(HKMergeEventListener.class);

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
        ProductVariant oldProductVariant = getOriginalProductVariantById(productVariantId);
        User loggedUser = getUserService().getLoggedInUser();
        if (oldProductVariant != null) {
            oldProductVariant.setOptionsAuditString(oldProductVariant.getOptionsPipeSeparated());
        }

        try {
            EntityAuditTrail eat = new EntityAuditTrail();
            eat.setEntityId(productVariantId);
            Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
            String oldJson = "";
            if (oldProductVariant != null)
                oldJson = gson.toJson(oldProductVariant);
            eat.setOldJson(oldJson);
            savedProductVariant.setOptionsAuditString(savedProductVariant.getOptionsPipeSeparated());
            String newJson = gson.toJson(savedProductVariant);
            eat.setNewJson(newJson);
            if (!BaseUtils.getMD5Checksum(oldJson).equals(BaseUtils.getMD5Checksum(newJson))) {
                eat.setUserEmail(loggedUser != null ? loggedUser.getEmail() : "system");
                eat.setCallingClass(Reflection.getCallerClass(2).getName());
                eat.setStackTrace(JsonUtils.getGsonDefault().toJson(Thread.currentThread().getStackTrace()));
                eat.setCreateDt(new Date());
                Session session = null;
                try {
                    SessionFactory sessionFactory = (SessionFactory) ServiceLocatorFactory.getService("newSessionFactory");
                    session = sessionFactory.openSession();
                    session.save(eat);
                } catch (Exception e) {
                    logger.error("Error while entering audit trail for product->" + productVariantId, e);
                } finally {
                    if (session != null) {
                        session.close();
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Error while entering audit trail for product->" + productVariantId, e);
        }

    }

    public ProductVariant getOriginalProductVariantById(String productVariantId) {
        SessionFactory sessionFactory = (SessionFactory) ServiceLocatorFactory.getService("newSessionFactory");
        Session session = null;
        ProductVariant productVariant = null;
        try {

            session = sessionFactory.openSession();
            productVariant = (ProductVariant) session.createQuery("select pv from ProductVariant pv where pv.id=:productVariantId").setString("productVariantId", productVariantId).uniqueResult();
        } catch (Exception e) {
            logger.error("Error while getting original pv ->" + productVariantId, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return productVariant;
    }

    public Product getOriginalProductById(String productId) {
        SessionFactory sessionFactory = (SessionFactory) ServiceLocatorFactory.getService("newSessionFactory");
        Session session = null;
        Product product = null;
        try {
            session = sessionFactory.openSession();
            product = (Product) session.createQuery("select p from Product p where p.id=:productId").setString("productId", productId).uniqueResult();
            if (product != null) {
                product.setCategoriesPipeSeparated(product.getPipeSeparatedCategories());
            }
        } catch (Exception e) {
            logger.error("Error while getting original product ->" + productId, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return product;
    }

    private void audit(String productId, Product savedProduct) {
        Product oldProduct = getOriginalProductById(productId);
        User loggedUser = getUserService().getLoggedInUser();

        try {
            EntityAuditTrail eat = new EntityAuditTrail();
            eat.setEntityId(productId);
            Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
            String oldJson = "";
            if (oldProduct != null)
                oldJson = gson.toJson(oldProduct);
            eat.setOldJson(oldJson);
            savedProduct.setCategoriesPipeSeparated(savedProduct.getPipeSeparatedCategories());
            String newJson = gson.toJson(savedProduct);
            eat.setNewJson(newJson);
            if (!BaseUtils.getMD5Checksum(oldJson).equals(BaseUtils.getMD5Checksum(newJson))) {
                eat.setUserEmail(loggedUser != null ? loggedUser.getEmail() : "system");
                eat.setCallingClass(Reflection.getCallerClass(2).getName());
                eat.setStackTrace(JsonUtils.getGsonDefault().toJson(Thread.currentThread().getStackTrace()));
                eat.setCreateDt(new Date());
                Session session = null;
                try {
                    SessionFactory sessionFactory = (SessionFactory) ServiceLocatorFactory.getService("newSessionFactory");
                    session = sessionFactory.openSession();
                    session.save(eat);
                } catch (Exception e) {
                    logger.error("Error while entering audit trail for product->" + productId, e);
                } finally {
                    if (session != null) {
                        session.close();
                    }
                }

            }
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
