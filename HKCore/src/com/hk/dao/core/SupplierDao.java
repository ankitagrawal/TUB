package com.hk.dao.core;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.Supplier;

@SuppressWarnings("unchecked")
@Repository
public class SupplierDao extends BaseDaoImpl {

    public Supplier findByName(String name) {
        return (Supplier) getSession().createQuery("from Supplier s where s.name = :name").setString("name", name).uniqueResult();
    }

    public Supplier findByTIN(String tinNumber) {
        return (Supplier) getSession().createQuery("from Supplier s where s.tinNumber = :tinNumber").setString("tinNumber", tinNumber).uniqueResult();
    }

    public List<Supplier> getListOrderedByName() {
        return (List<Supplier>) getSession().createQuery("from Supplier s order by s.name asc").list();
    }

    public Page getSupplierByTinAndName(String tinNumber, String name, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Supplier.class);
        // Criteria criteria = getSession().createCriteria(Supplier.class);
        if (!StringUtils.isBlank(tinNumber))
            criteria.add(Restrictions.eq("tinNumber", tinNumber));

        if (!StringUtils.isBlank(name))
            criteria.add(Restrictions.like("name".toLowerCase(), "%" + name.toLowerCase() + "%"));

        criteria.addOrder(Order.asc("name"));
        return list(criteria, page, perPage);
    }

    public boolean doesTinNumberExist(String tinNumber) {
        Long count = (Long) (getSession().createQuery("select count(distinct s.tinNumber) from Supplier s where s.tinNumber = :tinNumber").setString("tinNumber", tinNumber).uniqueResult());
        return (count != null && count > 0);
    }
}