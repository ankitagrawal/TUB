package com.hk.impl.dao.order.cartLineItem;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;

@Repository
public class CartLineItemDaoImpl extends BaseDaoImpl implements CartLineItemDao {

    private Logger logger = LoggerFactory.getLogger(CartLineItemDaoImpl.class);

    @Transactional
    public void remove(Long id) {
        CartLineItem cartLineItem = get(CartLineItem.class, id);
        if (cartLineItem.getCartLineItemExtraOptions() != null) {
            for (CartLineItemExtraOption cartLineItemExtraOption : cartLineItem.getCartLineItemExtraOptions()) {
                delete(cartLineItemExtraOption);
                // cartLineItemExtraOptionDaoProvider.delete(cartLineItemExtraOption.getId());
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
        if (cartLineItem.getHkPrice() == 0) {
            logger.error("Update cart Line Item hk price in dao:" + cartLineItem.getHkPrice() + ", id : " + cartLineItem.getId() + ", qty :" + cartLineItem.getQty() + ", type :"
                    + cartLineItem.getLineItemType());
	        if (productVariant != null) {
                logger.error("HK price null for variant" + productVariant.getId());
            }
            logger.error("HK price null for order " + cartLineItem.getOrder());
        }else if(productVariant != null && cartLineItem.getHkPrice() != null
		        && cartLineItem.getHkPrice() > productVariant.getHkPrice() && cartLineItem.getCartLineItemConfig() == null ){
	        logger.error("HK price of CLI is more than PV. Setting it as PV");
	        cartLineItem.setHkPrice(productVariant.getHkPrice());
        }

        // logger.error("save cart Line Item hk price in dao:" + cartLineItem.getHkPrice() + "id : " +
        // cartLineItem.getId() + " qty :" + cartLineItem.getQty() + " type :" + cartLineItem.getLineItemType());
        return (CartLineItem) super.save(cartLineItem);
    }

    @Transactional
    public void flipProductVariants(ProductVariant srcPV, ProductVariant dstPV, Order order) {

        CartLineItem cli = (CartLineItem) findUniqueByNamedParams(" from CartLineItem cli where cli.order = :order and cli.productVariant = :srcPV ", new String[]{"order","srcPV"}, new Object[]{order,srcPV});
        cli.setProductVariant(dstPV);
        update(cli);
        /*
         * getSession().createQuery("update CartLineItem cli set cli.productVariant = :dstPV where cli.order = :order
         * and cli.productVariant = :srcPV").setParameter("dstPV", dstPV).setParameter( "order",
         * order).setParameter("srcPV", srcPV).executeUpdate();
         */
    }

    public CartLineItem getLineItem(ProductVariant productVariant, Order order) {
        logger.error("getting line items for " + productVariant.getId() + " order" + order.getId());
        String query = "select cli from CartLineItem cli where cli.productVariant = :productVariant  and cli.order = :order";
        List<CartLineItem> allItems =  getSession().createQuery(query).setEntity("productVariant", productVariant).setEntity("order", order).list();
        for(CartLineItem item : allItems){
            logger.error("***ID: " + item.getId());
        }
        return (CartLineItem) getSession().createQuery(query).setEntity("productVariant", productVariant).setEntity("order", order).uniqueResult();
    }

}
