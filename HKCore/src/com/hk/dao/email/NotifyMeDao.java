package com.hk.dao.email;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;

@SuppressWarnings("unchecked")
@Repository
public class NotifyMeDao extends BaseDaoImpl {

    
    @Transactional
    public NotifyMe save(NotifyMe notifyMe) {
        String query = "select nm from NotifyMe nm  where nm.notifiedDate is null";
        List<NotifyMe> notifyMeList = getSession().createQuery(query).list();
        if (notifyMeList != null) {
            for (NotifyMe notifyMeObject : notifyMeList) {
                if (notifyMe.getProductVariant().equals(notifyMeObject.getProductVariant()) && notifyMe.getEmail().equals(notifyMeObject.getEmail())) {

                }
            }
        }
        if (notifyMe.getCreatedDate() == null) {
            notifyMe.setCreatedDate(BaseUtils.getCurrentTimestamp());
        }
        return (NotifyMe) super.save(notifyMe);
    }

    public Page searchNotifyMe(Date startDate, Date endDate, int pageNo, int perPage, Product product, ProductVariant productVariant, Category primaryCategory) {
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
        if (product != null) {
            if (productVariantCriteria == null) {
                productVariantCriteria = notifyMeCriteria.createCriteria("productVariant");
            }
            productVariantCriteria.add(Restrictions.eq("product", product));
        }
        if (primaryCategory != null) {
            if (productVariantCriteria == null) {
                productVariantCriteria = notifyMeCriteria.createCriteria("productVariant");
            }
            DetachedCriteria productCriteria = productVariantCriteria.createCriteria("product");
            productCriteria.add(Restrictions.eq("primaryCategory", primaryCategory));
        }

        notifyMeCriteria.add(Restrictions.isNull("notifiedDate"));
        notifyMeCriteria.add(Restrictions.isNull("notifiedByUser"));
        notifyMeCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        if (pageNo == 0 && perPage == 0) {
            return list(notifyMeCriteria, 1, 1000);
        }
        return list(notifyMeCriteria, pageNo, perPage);

    }

    public List<String> getPendingNotifyMeProductVariant() {

        String query = "select distinct nm.productVariant from NotifyMe nm  where nm.notifiedDate is null";
        return getSession().createQuery(query).list();

    }

    public List<NotifyMe> getNotifyMeListBetweenDate(Date startDate, Date endDate) {

        if (startDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            startDate = calendar.getTime();
        }
        if (endDate == null) {
            endDate = new Date();
        }

        Criteria criteria = getSession().createCriteria(NotifyMe.class);
        criteria.add(Restrictions.gt("createdDate", startDate));
        criteria.add(Restrictions.lt("createdDate", endDate));
        criteria.add(Restrictions.isNull("notifiedDate"));
        criteria.add(Restrictions.isNull("notifiedByUser"));

        return criteria.list();

    }

    public List<NotifyMe> getNotifyMeListForProductVariantInStock() {
        return (List<NotifyMe>) getSession().createQuery(
                "Select nm from NotifyMe nm, ProductVariant pv where pv =nm.productVariant and nm.notifiedByUser is null"
                        + " and pv.deleted != :deleted and pv.outOfStock != :outOfStock").setBoolean("deleted", true).setBoolean("outOfStock", true).list();
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

    public List<NotifyMe> getPendingNotifyMeList() {
        String query = "select nm from NotifyMe nm  where nm.notifiedDate is null";
        return getSession().createQuery(query).list();
    }

}
