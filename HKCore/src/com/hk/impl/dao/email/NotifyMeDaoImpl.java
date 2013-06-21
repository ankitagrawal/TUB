package com.hk.impl.dao.email;

import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.hibernate.Query;
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
                               Boolean productVariantOutOfStock, Boolean productDeleted) {
        DetachedCriteria notifyMeCriteria = getNotifyMeListSearchCriteria(startDate, endDate, product, productVariant, primaryCategory,
                productVariantOutOfStock, productDeleted);
        if (pageNo == 0 && perPage == 0) {
            return list(notifyMeCriteria, 1, 1000);
        }
        return list(notifyMeCriteria, pageNo, perPage);

    }

    public List<NotifyMe> searchNotifyMe(Date startDate, Date endDate, Product product, ProductVariant productVariant, Category primaryCategory,
                                         Boolean productVariantOutOfStock, Boolean productDeleted) {
        DetachedCriteria notifyMeCriteria = getNotifyMeListSearchCriteria(startDate, endDate, product, productVariant, primaryCategory,
                productVariantOutOfStock, productDeleted);
        return findByCriteria(notifyMeCriteria);
    }


    private DetachedCriteria getNotifyMeListSearchCriteria(Date startDate, Date endDate, Product product, ProductVariant productVariant, Category primaryCategory,
                                                           Boolean productVariantOutOfStock, Boolean productDeleted) {
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
        if (product != null || primaryCategory != null || productDeleted != null || productVariantOutOfStock != null) {
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
        if (productVariantOutOfStock != null) {
            productVariantCriteria.add(Restrictions.eq("outOfStock", productVariantOutOfStock));
        }

        notifyMeCriteria.add(Restrictions.isNull("notifiedDate"));
        notifyMeCriteria.add(Restrictions.isNull("notifiedByUser"));
        notifyMeCriteria.addOrder(org.hibernate.criterion.Order.asc("id"));

        return notifyMeCriteria;
    }

    public List<String> getPendingNotifyMeProductVariant() {

        String query = "select distinct nm.productVariant from NotifyMe nm  where nm.notifiedDate is null";
        return getSession().createQuery(query).list();

    }

    public List<NotifyMe> getNotifyMeListForProductVariantInStock() {
        return (List<NotifyMe>) getSession().createQuery(
                "Select nm from NotifyMe nm, ProductVariant pv  where pv =nm.productVariant and nm.notifiedByUser is null "
                        + " and pv.deleted != :deleted and pv.outOfStock != :outOfStock and (pv.product.hidden is null or pv.product.hidden = :hidden) order by nm.id asc").setBoolean("deleted", true).setBoolean("outOfStock", true).setBoolean("hidden", false).list();
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


    public List<NotifyMe> notifyMeForSimilarProductsMails(Date endDate, Boolean productVariantOutOfStock, Boolean productVariantDeleted) {
        String hql = "select distinct(nm) from NotifyMe as nm join nm.productVariant as  pv join pv.product as  p where p.similarProducts.size > :size " +
                "  and nm.notifiedDate is null and nm.notifiedByUser is null  ";
        if (endDate != null) {
            hql = hql + " and nm.createdDate < :endDate ";
        }

        if (productVariantOutOfStock != null) {
            hql = hql + "and pv.outOfStock = :productVariantOutOfStock";
        }

        if (productVariantDeleted != null) {
            hql = hql + "  and pv.deleted = :productVariantDeleted ";
        }
        Query query = getSession().createQuery(hql).setParameter("size", 0);

        if (endDate != null) {
            query.setDate("endDate", endDate);
        }

        if (productVariantOutOfStock != null) {
            query.setParameter("productVariantOutOfStock", productVariantOutOfStock);
        }

        if (productVariantDeleted != null) {
            query.setParameter("productVariantDeleted", productVariantDeleted);
        }
        return query.list();
    }

    private Page getNotifyMeList(int pageNo, int perPage, Product product, Category primaryCategory, Boolean productVariantOutOfStock, Boolean similarProduct) {
        String hql = "Select nm.id as id , nm.name as name , nm.email as email , nm.productVariant as productVariant  , count(nm.id) as userCount  from NotifyMe nm ," +
                " ProductVariant pv  , Product  p where pv = nm.productVariant and p = pv.product and nm.notifiedByUser is null and nm.notifiedDate is null ";

        if (productVariantOutOfStock != null) {
            hql = hql + " and pv.outOfStock = :outOfStock ";
        }

        if (product != null) {
            hql = hql + " and pv.product.id = :productId ";
        }

        if (primaryCategory != null) {
            hql = hql + " and pv.product.primaryCategory.id = :primaryCategory";
        }

        if (similarProduct != null) {
            if (similarProduct) {
                hql = hql + " and p.similarProducts.size > 0 ";
            } else {
                hql = hql + " and p.similarProducts.size = 0 ";
            }
        }

        hql = hql + " group by nm.productVariant  order by nm.id asc";
        Query query = getSession().createQuery(hql);

        if (productVariantOutOfStock != null) {
            query.setBoolean("outOfStock", productVariantOutOfStock);
        }
        if (product != null) {
            query.setParameter("productId", product.getId());
        }
        if (primaryCategory != null) {
            query.setParameter("primaryCategory", primaryCategory.getName());
        }
        return exceuteQuery(query, pageNo, perPage);

    }


    public Page getAllNotifyMeList(int pageNo, int perPage, Product product, Category primaryCategory) {
        return getNotifyMeList(pageNo, perPage, product, primaryCategory, null, null);
    }

    public Page getNotifyMeListForSimilarProducts(int pageNo, int perPage, Product product, Category primaryCategory, Boolean productVariantOutOfStock, Boolean similarProduct) {
      return getNotifyMeList(pageNo, perPage, product, primaryCategory, true, similarProduct);
    }


    public Page notifyMeListForInStockProduct(int pageNo, int perPage, Product product, Category primaryCategory) {

        String hql = "Select nm.id as id , nm.name as name , nm.email as email , nm.productVariant as productVariant  , count(nm.id) as userCount  from NotifyMe nm ," +
                " ProductVariant pv  where pv = nm.productVariant and nm.notifiedByUser is null and nm.notifiedDate is null  "
                + " and pv.deleted != :deleted and pv.outOfStock != :outOfStock and (pv.product.hidden is null or pv.product.hidden = :hidden) ";

        if (product != null) {
            hql = hql + " and pv.product.id = :productId ";
        }

        if (primaryCategory != null) {
            hql = hql + "and pv.product.primaryCategory = :primaryCategory";
        }
        hql = hql + " group by nm.productVariant  order by nm.id asc";

        Query query = getSession().createQuery(hql).setBoolean("deleted", true).setBoolean("outOfStock", true).setBoolean("hidden", false);
        if (product != null) {
            query.setParameter("productId", product.getId());
        }
        if (primaryCategory != null) {
            query.setParameter("primaryCategory", primaryCategory.getName());
        }
        return exceuteQuery(query, pageNo, perPage);
    }

    private Page exceuteQuery(Query query, int pageNo, int perPage) {
        query.setResultTransformer(Transformers.aliasToBean(NotifyMeDto.class));
        int totalResults = query.list().size();
        int firstResult = (pageNo - 1) * perPage;
        query.setFirstResult(firstResult);
        query.setMaxResults(perPage);
        List resultList = query.list();
        return new Page(resultList, perPage, pageNo, totalResults);

    }

}
