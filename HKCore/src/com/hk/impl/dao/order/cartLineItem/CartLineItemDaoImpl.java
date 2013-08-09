package com.hk.impl.dao.order.cartLineItem;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CartLineItemDaoImpl extends BaseDaoImpl implements CartLineItemDao {

  @SuppressWarnings("unused")
  private Logger logger = LoggerFactory.getLogger(CartLineItemDaoImpl.class);

  @Transactional
  public void remove(Long id) {
    CartLineItem cartLineItem = get(CartLineItem.class, id);
    if (cartLineItem.getCartLineItemExtraOptions() != null) {
      for (CartLineItemExtraOption cartLineItemExtraOption : cartLineItem.getCartLineItemExtraOptions()) {
        delete(cartLineItemExtraOption);
      }
    }
    delete(cartLineItem);
  }

  @Transactional
  public CartLineItem save(CartLineItem cartLineItem) {
    ProductVariant productVariant = cartLineItem.getProductVariant();
    if (cartLineItem.getDiscountOnHkPrice() == null) {
      cartLineItem.setDiscountOnHkPrice(0D);
    }

    if (productVariant != null && cartLineItem.getHkPrice() != null && cartLineItem.getHkPrice() > productVariant.getHkPrice() && cartLineItem.getCartLineItemConfig() == null) {
      cartLineItem.setHkPrice(productVariant.getHkPrice());
    }
    return (CartLineItem) super.save(cartLineItem);
  }

  @Transactional
  public void flipProductVariants(ProductVariant srcPV, ProductVariant dstPV, Order order) {

    CartLineItem cli = (CartLineItem) findUniqueByNamedParams(" from CartLineItem cli where cli.order = :order and cli.productVariant = :srcPV ", new String[]{
        "order",
        "srcPV"}, new Object[]{order, srcPV});
    cli.setProductVariant(dstPV);
    update(cli);
  }

  public CartLineItem getLineItem(ProductVariant productVariant, Order order) {
    String query = "select cli from CartLineItem cli where cli.productVariant.id = :productVariantId  and cli.order.id = :orderId and cli.lineItemType.id = "
        + EnumCartLineItemType.Product.getId();
    return (CartLineItem) getSession().createQuery(query).setString("productVariantId", productVariant.getId()).setLong("orderId", order.getId()).uniqueResult();
  }

  public List<CartLineItem> getClisForInPlacedOrder(ProductVariant productVariant, Double mrp) {

    String query = "from CartLineItem c  where  c.productVariant = :productVariant and c.markedPrice = :mrp and c.order.orderStatus.id in (:orderStatusIds)"
        + " and c.skuItemCLIs.size <= 0  order by c.order.createDate  asc ";
    return (List<CartLineItem>) getSession().createQuery(query).setParameterList("orderStatusIds", Arrays.asList(EnumOrderStatus.Placed.getId(), EnumOrderStatus.OnHold.getId())).setParameter("productVariant", productVariant).setParameter("mrp", mrp).list();
  }

  public List<CartLineItem> getClisForOrderInProcessingState(ProductVariant productVariant, Long skuId, Double mrp) {
    List<Long> statuses = new ArrayList<Long>();
    statuses.addAll(EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory()));
    statuses.add(EnumShippingOrderStatus.SO_ReadyForDropShipping.getId());

    String query = " select c from CartLineItem c inner join c.order as o inner join o.shippingOrders as so  inner join so.lineItems as li where  c.productVariant = :productVariant and c.markedPrice = :mrp and so.shippingOrderStatus.id in (:shippingOrderStatusIds) and li.sku.id = :skuId  "
        + " and c.skuItemCLIs.size <= 0  order by so.targetDispatchDate,so.createDate  asc ";
    return (List<CartLineItem>) getSession().createQuery(query).setParameterList("shippingOrderStatusIds", statuses).setParameter("productVariant", productVariant).setParameter("mrp", mrp).setParameter("skuId", skuId).list();

  }

}
