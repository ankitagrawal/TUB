package com.hk.impl.dao.sale;

import com.hk.pact.dao.BaseDao;
import com.hk.domain.sale.ClearanceSaleProduct;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 4 May, 2013
 * Time: 2:43:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClearanceSaleProductDao extends BaseDao  {

  public List<ClearanceSaleProduct> getProducts();
}
