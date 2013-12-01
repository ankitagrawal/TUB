package com.hk.impl.dao.core;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.Supplier;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.core.SupplierDao;

@SuppressWarnings("unchecked")
@Repository
public class SupplierDaoImpl extends BaseDaoImpl implements SupplierDao {

    public Supplier findByName(String name) {
        return (Supplier) getSession().createQuery("from Supplier s where s.name = :name").setString("name", name).uniqueResult();
    }

    public Supplier findByTIN(String tinNumber) {
        return (Supplier) getSession().createQuery("from Supplier s where s.tinNumber = :tinNumber").setString("tinNumber", tinNumber).uniqueResult();
    }

    public List<Supplier> getListOrderedByName() {
        return (List<Supplier>) getSession().createQuery("from Supplier s order by s.name asc").list();
    }

    public Supplier save(Supplier supplier){

        if(supplier.getCreateDate() == null){
            supplier.setCreateDate(BaseUtils.getCurrentTimestamp());
        }
        supplier.setUpdateDate(BaseUtils.getCurrentTimestamp());
        return (Supplier) super.save(supplier);
    }

    public Page getSupplierByTinAndName(String tinNumber, String name, Boolean status, int page, int perPage) {
        return list(getSupplierSearchCriteria(tinNumber, name, status), page, perPage);
    }

    public List<Supplier> getSupplierByTinAndName(String tinNumber, String name, Boolean status) {
        return findByCriteria(getSupplierSearchCriteria(tinNumber, name, status));
    }

    private DetachedCriteria getSupplierSearchCriteria(String tinNumber, String name, Boolean status) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Supplier.class);
        // Criteria criteria = getSession().createCriteria(Supplier.class);
        if (!StringUtils.isBlank(tinNumber))
            criteria.add(Restrictions.eq("tinNumber", tinNumber));

        if (!StringUtils.isBlank(name))
            criteria.add(Restrictions.like("name".toLowerCase(), "%" + name.toLowerCase() + "%"));

	    if (status != null)
            criteria.add(Restrictions.eq("active", status));

        criteria.addOrder(Order.asc("name"));
        return criteria;
    }


    public boolean doesTinNumberExist(String tinNumber) {
        Long count = (Long) (getSession().createQuery("select count(distinct s.tinNumber) from Supplier s where s.tinNumber = :tinNumber").setString("tinNumber", tinNumber).uniqueResult());
        return (count != null && count > 0);
    }
    
    public Supplier findById(Long id) {
        return (Supplier) getSession().createQuery("from Supplier s where s.id = :id").setLong("id", id).uniqueResult();
    }
}