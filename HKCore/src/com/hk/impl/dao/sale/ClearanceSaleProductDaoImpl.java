package com.hk.impl.dao.sale;

import com.hk.domain.sale.ClearanceSaleProduct;
import com.hk.impl.dao.BaseDaoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

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
    String query = "select csp from ClearanceSaleProduct csp, Product p " +
        "where csp.product.id = p.id and p.deleted <> 1 and p.outOfStock <> 1 and csp.deleted <> 1 " +
        "order by csp.sorting asc limit 100";
    return findByNamedParams(query, new String[]{}, new Object[]{});
  }
}
