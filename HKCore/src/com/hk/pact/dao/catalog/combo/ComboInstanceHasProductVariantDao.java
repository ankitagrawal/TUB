package com.hk.pact.dao.catalog.combo;

import java.util.List;

import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.pact.dao.BaseDao;

public interface ComboInstanceHasProductVariantDao extends BaseDao {

    public List<ComboInstanceHasProductVariant> findByComboInstance(ComboInstance comboInstance);

}
