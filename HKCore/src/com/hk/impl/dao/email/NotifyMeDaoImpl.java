package com.hk.impl.dao.email;

import java.util.Date;
import java.util.List;


import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.email.NotifyMeDao;

@SuppressWarnings("unchecked")
@Repository
public class NotifyMeDaoImpl extends BaseDaoImpl implements NotifyMeDao {

    @Transactional
    public NotifyMe save(NotifyMe notifyMe) {
        Date currentDate = new Date();
        if (notifyMe.getCreatedDate() == null) {
            notifyMe.setCreatedDate(currentDate);
        }
        return (NotifyMe) super.save(notifyMe);
    }

    public Page searchNotifyMe(Date startDate, Date endDate, int pageNo, int perPage, Product product, ProductVariant productVariant, Category primaryCategory,
                               Boolean productInStock, Boolean productDeleted) {
        DetachedCriteria notifyMeCriteria = getNotifyMeListSearchCriteria(startDate, endDate, product, productVariant, primaryCategory,
                productInStock, productDeleted, null);
        if (pageNo == 0 && perPage == 0) {
            return list(notifyMeCriteria, 1, 1000);
        }
        return list(notifyMeCriteria, pageNo, perPage);

    }

    public List<NotifyMe> searchNotifyMe(Date startDate, Date endDate, Product product, ProductVariant productVariant, Category primaryCategory,
                                         Boolean productInStock, Boolean productDeleted) {
        DetachedCriteria notifyMeCriteria = getNotifyMeListSearchCriteria(startDate, endDate, product, productVariant, primaryCategory,
                productInStock, productDeleted, null);
        return findByCriteria(notifyMeCriteria);
    }

    public List<NotifyMe> searchNotifyMe(Date startDate, Date endDate, Product product, ProductVariant productVariant, Category primaryCategory, Boolean productInStock, Boolean productDeleted, Boolean productHidden) {
        DetachedCriteria notifyMeCriteria = getNotifyMeListSearchCriteria(startDate, endDate, product, productVariant, primaryCategory,
                productInStock, productDeleted, productHidden);
        return findByCriteria(notifyMeCriteria);
    }

    private DetachedCriteria getNotifyMeListSearchCriteria(Date startDate, Date endDate, Product product, ProductVariant productVariant, Category primaryCategory,
                                                           Boolean productInStock, Boolean productDeleted, Boolean productHidden) {
        DetachedCriteria notifyMeCriteria = DetachedCriteria.forClass(NotifyMe.class);
        if (startDate != null) {
            notifyMeCriteria.add(Restrictions.gt("createdDate", startDate));
        }
        if (endDate != null) {
            notifyMeCriteria.add(Restrictions.lt("createdDate", endDate));

        }
        if (productVariant != null) {
            notifyMeCriteria.add(Restrictions.eq("productVariant", productVariant));
        }
        DetachedCriteria productVariantCriteria = null;
        if (product != null || primaryCategory != null || productDeleted != null || productInStock != null || productHidden != null) {
            productVariantCriteria = notifyMeCriteria.createCriteria("productVariant");
        }

        if (product != null) {
            productVariantCriteria.add(Restrictions.eq("product", product));
        }
        DetachedCriteria productCriteria = null;
        if (primaryCategory != null) {
            productCriteria = productVariantCriteria.createCriteria("product");
            productCriteria.add(Restrictions.eq("primaryCategory", primaryCategory));
        }
        if (productDeleted != null) {
            productVariantCriteria.add(Restrictions.eq("deleted", productDeleted));
        }
        if (productInStock != null) {
            productVariantCriteria.add(Restrictions.eq("outOfStock", productInStock));
        }
        if (productHidden != null) {
            if (productCriteria == null) {
                productCriteria = productVariantCriteria.createCriteria("product");
            }
            productCriteria.add(Restrictions.eq("hidden", productHidden));
        }

        notifyMeCriteria.add(Restrictions.isNull("notifiedDate"));
        notifyMeCriteria.add(Restrictions.isNull("notifiedByUser"));
        notifyMeCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));

        return notifyMeCriteria;
    }

    public List<String> getPendingNotifyMeProductVariant() {

        String query = "select distinct nm.productVariant from NotifyMe nm  where nm.notifiedDate is null";
        return getSession().createQuery(query).list();

    }

    public List<NotifyMe> getNotifyMeListForProductVariantInStock() {
        return (List<NotifyMe>) getSession().createQuery(
                "Select nm from NotifyMe nm, ProductVariant pv  where pv =nm.productVariant and nm.notifiedByUser is null "
                        + " and pv.deleted != :deleted and pv.outOfStock != :outOfStock and (pv.product.hidden is null or pv.product.hidden = :hidden) order by nm.id asc").setBoolean("deleted", true).setBoolean("outOfStock", true).setBoolean("hidden",false).list();
    }

    public Page getNotifyMeListForProductVariantInStock(int pageNo, int perPage) {
        List<NotifyMe> notifyMeList = getSession().createQuery(
                "Select nm.id from NotifyMe nm, ProductVariant pv where pv =nm.productVariant and nm.notifiedByUser is null"
                        + " and pv.deleted != :deleted and pv.outOfStock != :outOfStock").setBoolean("deleted", true).setBoolean("outOfStock", true).list();
        if (notifyMeList == null || notifyMeList.isEmpty()) {
            return null;
        }
        DetachedCriteria notifyMeCriteria = DetachedCriteria.forClass(NotifyMe.class);
        notifyMeCriteria.add(Restrictions.in("id", notifyMeList));
        notifyMeCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(notifyMeCriteria, pageNo, perPage);
    }

    public List<NotifyMe> getAllNotifyMeForSameUser(String notifyMeEmail) {
        return getSession().createQuery(
                "Select nm from NotifyMe nm,ProductVariant pv where nm.email =:notifyMeEmail and pv =nm.productVariant and nm.notifiedByUser is null"
                        + " and pv.deleted != :deleted and pv.outOfStock != :outOfStock").setParameter("notifyMeEmail", notifyMeEmail).setBoolean("deleted", true).setBoolean(
                "outOfStock", true).list();
    }

    public List<NotifyMe> getPendingNotifyMeList(String notifyMeEmail, ProductVariant productVariant) {
        String query = "select nm from NotifyMe nm  where nm.notifiedDate is null and nm.productVariant = :productVariant and nm.email =:notifyMeEmail";
        return getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("notifyMeEmail", notifyMeEmail).list();
    }

    public List<NotifyMe> getPendingNotifyMeListByVariant(String notifyMeEmail, List<ProductVariant> productVariantList) {
        String query = "select nm from NotifyMe nm  where nm.notifiedDate is null and nm.productVariant in (:productVariantList) and nm.email =:notifyMeEmail";
        return getSession().createQuery(query).setParameterList("productVariantList", productVariantList).setParameter("notifyMeEmail", notifyMeEmail).list();
    }

    public Page getNotifyMeListForDeletedHiddenOOSProduct(Date startDate, Date endDate, int pageNo, int perPage, Product product, ProductVariant productVariant, Category primaryCategory, Boolean productInStock, Boolean productDeleted, Boolean productHidden) {
        DetachedCriteria notifyMeDetachedCriteria = getNotifyMeListSearchCriteria(startDate, endDate, product, productVariant, primaryCategory, productInStock, productDeleted, productHidden);
        int totalResults = count(notifyMeDetachedCriteria,false);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.alias(Projections.property("id"), "id"));
        projectionList.add(Projections.alias(Projections.property("name"), "name"));
        projectionList.add(Projections.alias(Projections.property("email"), "email"));
        projectionList.add(Projections.alias(Projections.property("productVariant"), "productVariant"));
        projectionList.add(Projections.groupProperty("productVariant"));
        projectionList.add(Projections.alias(Projections.count("id"), "userCount"));
        notifyMeDetachedCriteria.setProjection(projectionList);
        notifyMeDetachedCriteria.setResultTransformer(Transformers.aliasToBean(NotifyMeDto.class));
        int firstResult = (pageNo - 1) * perPage;
        List resultList = findByCriteria(notifyMeDetachedCriteria, firstResult, perPage);
        return new Page(resultList, perPage, pageNo, totalResults);

    }
}
