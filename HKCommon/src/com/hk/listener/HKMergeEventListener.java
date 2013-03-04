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
        }

    }

    public Product getOriginalProductById(String productId) {
        SessionFactory sessionFactory = (SessionFactory) ServiceLocatorFactory.getService("newSessionFactory");
        Session session = sessionFactory.openSession();
        Product product = (Product) session.createQuery("select p from Product p where p.id=:productId").setString("productId", productId).uniqueResult();
        if (product != null) {
            product.setCategoriesPipeSeparated(product.getPipeSeparatedCategories());
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

                } finally {
                    if (session != null) {
                        session.close();
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Error while entering audit trail for product->" + productId);
        }
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = (UserService) ServiceLocatorFactory.getService(UserService.class);
        }
        return userService;
    }

}
