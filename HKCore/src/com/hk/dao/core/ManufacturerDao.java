package com.hk.dao.core;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.Manufacturer;

@Repository
public class ManufacturerDao extends BaseDaoImpl {
    
    public Manufacturer findByName(String name) {
        return (Manufacturer) getSession().createQuery("from Manufacturer m where m.name = :name").setString("name", name).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public Page findManufacturersOrderedByName(int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Manufacturer.class);
        // Criteria criteria = getSession().createCriteria(Manufacturer.class);
        criteria.addOrder(Order.asc("name"));
        return list(criteria, pageNo, perPage);
    }

}
