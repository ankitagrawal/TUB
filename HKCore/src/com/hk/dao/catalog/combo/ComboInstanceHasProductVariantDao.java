package com.hk.dao.catalog.combo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;


@Repository
public class ComboInstanceHasProductVariantDao extends BaseDaoImpl {


  public ComboInstanceHasProductVariant getOrCreateComboInstanceHasProductVariant(ComboInstance comboInstance) {
    ComboInstanceHasProductVariant comboInstanceHasProductVariant = findByComboInstance(comboInstance);
    if (comboInstanceHasProductVariant == null) {
      return new ComboInstanceHasProductVariant();
    }
    return comboInstanceHasProductVariant;
  }

  private ComboInstanceHasProductVariant findByComboInstance(ComboInstance comboInstance) {
    Criteria criteria = getSession().createCriteria(ComboInstanceHasProductVariant.class);
    criteria.add(Restrictions.eq("comboInstance", comboInstance));
    return (ComboInstanceHasProductVariant) criteria.uniqueResult();
  }

/*
  public ComboInstanceHasProductVariant findByProductVariantInComboInstance(ComboInstance comboInstance, ProductVariant productVariant) {
    String query = "select * from ComboInstanceHasProductVariant cipv where cipv.comboInstance =:comboInstance and cipv.productVariant =:productVariant";

    return (ComboInstanceHasProductVariant) getSession().createQuery(query)
        .setParameter("productVariant",productVariant)
        .setParameter("comboInstance",comboInstance)
        .list();
  }
*/
}

