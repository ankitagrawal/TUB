package com.hk.dao.order.cartLineItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.order.Order;

@Repository
public class CartLineItemDao extends BaseDaoImpl {

  private Logger logger = LoggerFactory.getLogger(CartLineItemDao.class);


  @Transactional
  public void remove(Long id) {
    CartLineItem cartLineItem = get(CartLineItem.class, id);
    if (cartLineItem.getCartLineItemExtraOptions() != null) {
      for (CartLineItemExtraOption cartLineItemExtraOption : cartLineItem.getCartLineItemExtraOptions()) {
          delete(cartLineItemExtraOption);
        //cartLineItemExtraOptionDaoProvider.delete(cartLineItemExtraOption.getId());
      }
    }
    delete(cartLineItem);
  }

  @Transactional
  public CartLineItem save(CartLineItem cartLineItem) {

    if (cartLineItem.getDiscountOnHkPrice() == null) {
      cartLineItem.setDiscountOnHkPrice(0D);
    }
    if (cartLineItem.getHkPrice() == 0) {
      logger.error("Update cart Line Item hk price in dao:" + cartLineItem.getHkPrice() + "id : " + cartLineItem.getId() + " qty :" + cartLineItem.getQty() + " type :" + cartLineItem.getLineItemType());
      if(cartLineItem.getProductVariant() !=null){
        logger.error("HK price null for variant" + cartLineItem.getProductVariant().getId());
      }
      logger.error("HK price null for order " + cartLineItem.getOrder() );
    }
    
    //logger.error("save cart Line Item hk price in dao:" + cartLineItem.getHkPrice() + "id : " + cartLineItem.getId() + " qty :" + cartLineItem.getQty() + " type :" + cartLineItem.getLineItemType());
    return (CartLineItem) super.save(cartLineItem);
  }

  @Transactional
  public void flipProductVariants(ProductVariant srcPV, ProductVariant dstPV, Order order) {
    getSession().createQuery("update CartLineItem cli  set cli.productVariant = :dstPV where cli.order = :order and cli.productVariant = :srcPV")
        .setParameter("dstPV", dstPV)
        .setParameter("order", order)
        .setParameter("srcPV", srcPV)
        .executeUpdate();
  }

  public CartLineItem getLineItem(ProductVariant productVariant, Order order) {
    String query = "select cli from CartLineItem cli where cli.productVariant = :productVariant  and cli.order = :order";
    return (CartLineItem) getSession().createQuery(query)
        .setEntity("productVariant", productVariant)
        .setEntity("order", order)
        .uniqueResult();
  }

}

