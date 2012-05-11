package com.hk.impl.dao.catalog.combo;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;

@Repository
public class ComboInstanceHasProductVariantDaoImpl extends BaseDaoImpl implements ComboInstanceHasProductVariantDao {

    /*
     * public ComboInstanceHasProductVariant getOrCreateComboInstanceHasProductVariant(ComboInstance comboInstance) {
     * ComboInstanceHasProductVariant comboInstanceHasProductVariant = findByComboInstance(comboInstance); if
     * (comboInstanceHasProductVariant == null) { return new ComboInstanceHasProductVariant(); } return
     * comboInstanceHasProductVariant; }
     */

    public List<ComboInstanceHasProductVariant> findByComboInstance(ComboInstance comboInstance) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ComboInstanceHasProductVariant.class);
        criteria.add(Restrictions.eq("comboInstance", comboInstance));
        return findByCriteria(criteria);
    }

    /*
     * public ComboInstanceHasProductVariant findByProductVariantInComboInstance(ComboInstance comboInstance,
     * ProductVariant productVariant) { String query = "select * from ComboInstanceHasProductVariant cipv where
     * cipv.comboInstance =:comboInstance and cipv.productVariant =:productVariant"; return
     * (ComboInstanceHasProductVariant) getSession().createQuery(query) .setParameter("productVariant",productVariant)
     * .setParameter("comboInstance",comboInstance) .list(); }
     */
}
