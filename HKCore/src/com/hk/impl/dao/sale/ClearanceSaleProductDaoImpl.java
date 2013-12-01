package com.hk.impl.dao.sale;

import com.hk.domain.sale.ClearanceSaleProduct;
import com.hk.domain.courier.Awb;
import com.hk.domain.catalog.product.Product;
import com.hk.impl.dao.BaseDaoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 4 May, 2013
 * Time: 2:46:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClearanceSaleProductDaoImpl extends BaseDaoImpl implements ClearanceSaleProductDao {

  public List<ClearanceSaleProduct> getProducts() {
    /* String query = "select csp from ClearanceSaleProduct csp, Product p " +
   "where csp.product.id = p.id and p.deleted <> 1 and p.outOfStock <> 1 and csp.deleted <> 1 " +
   "order by csp.sorting asc limit 100";
return findByNamedParams(query, new String[]{}, new Object[]{});*/

    DetachedCriteria clearanceCriteria = DetachedCriteria.forClass(ClearanceSaleProduct.class);
    clearanceCriteria.add(Restrictions.eq("deleted", false));
    DetachedCriteria productCriteria = clearanceCriteria.createCriteria("product");
    productCriteria.add(Restrictions.eq("deleted", false));
    productCriteria.add(Restrictions.eq("outOfStock", false));
    clearanceCriteria.addOrder(org.hibernate.criterion.Order.asc("sorting"));
    return (List<ClearanceSaleProduct>) findByCriteria(clearanceCriteria, 1, 100);
  }
}
