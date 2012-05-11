package com.hk.impl.dao.order;

import java.util.List;

import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.order.CartFreeBieDao;

public class CartFreeBieDaoImpl extends BaseDaoImpl implements CartFreeBieDao {

    public Double getCartValueForProducts(List<String> productList, Order order) {
        String query = "select sum(cli.hkPrice*cli.qty) from CartLineItem cli where cli.productVariant.product.id in (:productList)  and cli.order = :order";
        Double value = (Double) getSession().createQuery(query).setParameterList("productList", productList).setParameter("order", order).uniqueResult();
        return value;
    }

    public Double getCartValueForVariants(List<String> productVariantList, Order order) {
        String query = "select sum(cli.hkPrice*cli.qty) from CartLineItem cli where cli.productVariant.id in (:productVariantList)  and cli.order = :order";
        Double value = (Double) getSession().createQuery(query).setParameterList("productVariantList", productVariantList).setParameter("order", order).uniqueResult();
        return value;
    }
}
