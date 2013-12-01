package com.hk.pact.dao.catalog.combo;

import java.util.List;

import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.pact.dao.BaseDao;

public interface ComboInstanceDao extends BaseDao {

    public List<CartLineItem> getSiblingLineItems(CartLineItem cartLineItem);

    public ComboInstance getOrCreateComboInstanceHasProductVariant(Combo combo);

}
