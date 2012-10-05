package com.hk.impl.dao.user;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCart;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UserCartDao;

@SuppressWarnings("unchecked")
@Repository
public class UserCartDaoImpl extends BaseDaoImpl implements UserCartDao {

    @Transactional
    public void addToCartHistory(Product product, User user) {

        if ( product !=null && user !=null && !incrementProductCounterIfAlreadyExists(product, user)) {
            UserCart userCart = new UserCart();
            userCart.setCreateDate(new Date());
            userCart.setLastDate(new Date());
            userCart.setProduct(product);
            userCart.setUser(user);
            userCart.setBought(false);
            userCart.setCounter(1L);
            save(userCart);
        }
    }

    @Transactional
    private boolean incrementProductCounterIfAlreadyExists(Product product, User user) {
        UserCart userCart = productAlreadyInCartHistory(product, user);
        if (userCart != null) {
            userCart.setLastDate(new Date());
            userCart.setBought(false);
            userCart.setCounter(userCart.getCounter() + 1);
            save(userCart);
            return true;
        }
        return false;
    }

    @Transactional
    private UserCart productAlreadyInCartHistory(Product product, User user) {
        Criteria criteria = getSession().createCriteria(UserCart.class);
        criteria.add(Restrictions.eq("product", product));
        criteria.add(Restrictions.eq("user", user));
        List<UserCart> userCartList = criteria.list();
        return userCartList == null || userCartList.isEmpty() ? null : userCartList.get(0);
    }

    public List<UserCart> findByUser(User user) {
        Criteria criteria = getSession().createCriteria(UserCart.class);
        criteria.add(Restrictions.eq("user", user));
        return criteria.list();
    }

    @Transactional
    public void updateIsProductBought(Order order) {
        User user = order.getUser();
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
        for (CartLineItem productLineItem : productCartLineItems) {
            Criteria criteria = getSession().createCriteria(UserCart.class);
            criteria.add(Restrictions.eq("product", productLineItem.getProductVariant().getProduct()));
            criteria.add(Restrictions.eq("user", user));
            List<UserCart> userCartList = criteria.list();
            UserCart userCartObject = userCartList == null || userCartList.isEmpty() ? null : userCartList.get(0);
            if (userCartObject != null) {
                userCartObject.setBought(true);
                save(userCartObject);
            }
        }
    }
}
