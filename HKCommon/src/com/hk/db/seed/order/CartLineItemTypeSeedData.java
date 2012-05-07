package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumCartLineItemType;
import mhc.domain.CartLineItemType;
import mhc.service.dao.order.cartLineItem.CartLineItemTypeDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class CartLineItemTypeSeedData {

  @Inject
  CartLineItemTypeDao cartLineItemTypeDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    CartLineItemType lineItemType = new CartLineItemType();
    lineItemType.setName(name);
    lineItemType.setId(id);
    cartLineItemTypeDao.save(lineItemType);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumCartLineItemType enumLineItemType : EnumCartLineItemType.values()) {

      if (pkList.contains(enumLineItemType.getId()))
        throw new RuntimeException("Duplicate key " + enumLineItemType.getId());
      else pkList.add(enumLineItemType.getId());

      insert(enumLineItemType.getName(), enumLineItemType.getId());
    }
  }

}
