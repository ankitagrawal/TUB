package com.hk.impl.dao.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.order.CartFreeBieDao;

@Repository
public class CartFreeBieDaoImpl extends BaseDaoImpl implements CartFreeBieDao {

    public Double getCartValueForProducts(List<String> productList, Order order) {
        String query = "select sum(cli.markedPrice*cli.qty) from CartLineItem cli where cli.productVariant.product.id in (:productList)  and cli.order = :order";
        Double value = (Double) getSession().createQuery(query).setParameterList("productList", productList).setParameter("order", order).uniqueResult();
        return value;
    }

    public Double getCartValueForVariants(List<String> productVariantList, Order order) {
        String query = "select sum(cli.markedPrice*cli.qty) from CartLineItem cli where cli.productVariant.id in (:productVariantList)  and cli.order = :order";
        Double value = (Double) getSession().createQuery(query).setParameterList("productVariantList", productVariantList).setParameter("order", order).uniqueResult();
        return value;
    }

  public Double getCartValueForProducts(List<String> productList, ShippingOrder order) {
        String query = "select sum(cli.markedPrice*cli.qty) from LineItem cli where cli.sku.productVariant.product.id in (:productList)  and cli.shippingOrder = :order";
        Double value = (Double) getSession().createQuery(query).setParameterList("productList", productList).setParameter("order", order).uniqueResult();
        return value;
    }

    public Double getCartValueForVariants(List<String> productVariantList, ShippingOrder order) {
        String query = "select sum(cli.markedPrice*cli.qty) from LineItem cli where cli.sku.productVariant.id in (:productVariantList)  and cli.shippingOrder = :order";
        Double value = (Double) getSession().createQuery(query).setParameterList("productVariantList", productVariantList).setParameter("order", order).uniqueResult();
        return value;
    }
}
