package com.hk.impl.dao.catalog.product;

import com.akube.framework.dao.Page;
import com.hk.constants.inventory.EnumCycleCountStatus;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.UpdatePvPriceDao;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UpdatePvPriceDaoImpl extends BaseDaoImpl implements UpdatePvPriceDao {

    public Page getPVForPriceUpdate(Category primaryCategory, ProductVariant productVariant, Long status, int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UpdatePvPrice.class);
        if (status != null) {
            criteria.add(Restrictions.eq("status", status));
        }
        if (primaryCategory != null) {
            DetachedCriteria productVariantCriteria = criteria.createCriteria("productVariant");
            DetachedCriteria productCriteria = productVariantCriteria.createCriteria("product");
            productCriteria.add(Restrictions.eq("primaryCategory", primaryCategory));
        } else if (productVariant != null) {
            criteria.add(Restrictions.eq("productVariant", productVariant));
        }

        criteria.addOrder(org.hibernate.criterion.Order.desc("txnDate"));
        return list(criteria, pageNo, perPage);
    }

    public UpdatePvPrice getPVForPriceUpdate(ProductVariant productVariant, Long status) {
        List<UpdatePvPrice> updatePvPriceList = (List<UpdatePvPrice>) findByNamedParams("from UpdatePvPrice upp where upp.productVariant =  :productVariant and upp.status = :status", new String[]{"productVariant", "status"}, new Object[]{productVariant, status});
        if (updatePvPriceList != null && !updatePvPriceList.isEmpty()) {
            return updatePvPriceList.get(0);
        }
        return null;
    }


    public boolean isAuditClosed(ProductVariant productVariant) {
        List<Long> cycleCountOpenStatus = EnumCycleCountStatus.getListOfOpenCycleCountStatus();

        String brand = productVariant.getProduct().getBrand();
        String variantId = productVariant.getId();
        String productId = productVariant.getProduct().getId();

        String sql = "select cc.brand as brand , cc.product.id as productId  , cc.productVariant.id as productVariantId  from CycleCount cc left join cc.product " +
                "  left join cc.productVariant  where   " +
                " cc.cycleStatus in (:cycleStatusList) ";
        sql = sql + " and (cc.productVariant.id = :productVariantId or cc.brand = :brand or cc.product.id = :productId) ";

        Query query = getSession().createQuery(sql).setParameterList("cycleStatusList", cycleCountOpenStatus)
                .setParameter("productVariantId", variantId).setParameter("productId", productId).setParameter("brand", brand);
        List<CycleCount> cycleCountList = query.list();
        if (cycleCountList != null && cycleCountList.size() > 0) {
            return false;
        }
        return true;

    }

}