package com.hk.impl.dao.catalog.combo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;

@Repository
public class ComboInstanceDaoImpl extends BaseDaoImpl implements ComboInstanceDao {

    public List<CartLineItem> getSiblingLineItems(CartLineItem cartLineItem) {
        if (cartLineItem == null) {
            return Collections.emptyList();
        }
        List<CartLineItem> siblingLineItems = new ArrayList<CartLineItem>();
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(cartLineItem.getOrder().getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        for (CartLineItem li : productCartLineItems) {
            if (li.getComboInstance() != null && li.getComboInstance().equals(cartLineItem.getComboInstance())) {
                siblingLineItems.add(li);
            }
        }
        return siblingLineItems;
    }

    public ComboInstance getOrCreateComboInstanceHasProductVariant(Combo combo) {
        ComboInstance comboInstance = findByCombo(combo);
        if (comboInstance == null) {
            comboInstance.setCombo(combo);
            comboInstance = (ComboInstance) save(comboInstance);
        }
        return comboInstance;
    }

    private ComboInstance findByCombo(Combo combo) {
        Criteria criteria = getSession().createCriteria(ComboInstance.class);
        criteria.add(Restrictions.eq("combo", combo));
        return (ComboInstance) criteria.uniqueResult();
    }

}
