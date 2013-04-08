package com.hk.admin.impl.dao.inventory;

import com.hk.admin.dto.inventory.CycleCountDto;
import com.hk.admin.pact.dao.inventory.CycleCountDao;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.Product;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.constants.inventory.EnumCycleCountStatus;
import com.akube.framework.dao.Page;
import com.hk.pact.dao.BaseDao;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.criterion.Expression;
import org.hibernate.Criteria;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 14, 2013
 * Time: 3:54:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CycleCountDaoImpl extends BaseDaoImpl implements CycleCountDao {

    @Autowired
    BaseDao baseDao;

    public CycleCountItem getCycleCountItem(CycleCount cycleCount, SkuGroup skuGroup, SkuItem skuItem) {
        DetachedCriteria cycleCountItemCriteria = getCycleCountItemCriteria(cycleCount, skuGroup, skuItem);
        List<CycleCountItem> cycleCountItems = findByCriteria(cycleCountItemCriteria);
        return cycleCountItems != null && cycleCountItems.size() > 0 ? cycleCountItems.get(0) : null;

    }

    private DetachedCriteria getCycleCountItemCriteria(CycleCount cycleCount, SkuGroup skuGroup, SkuItem skuItem) {
        DetachedCriteria cycleCountItemCriteria = DetachedCriteria.forClass(CycleCountItem.class);

        if (skuGroup != null) {
            cycleCountItemCriteria.add(Restrictions.eq("skuGroup", skuGroup));
        }
        if (skuItem != null) {
            cycleCountItemCriteria.add(Restrictions.eq("skuItem", skuItem));
        }

        if (cycleCount != null) {
            cycleCountItemCriteria.add(Restrictions.eq("cycleCount", cycleCount));
        }

        return cycleCountItemCriteria;
    }


    private DetachedCriteria getCycleCountCriteria(String brand, Product product, ProductVariant productVariant, Warehouse warehouse, User auditor, Date startDate, Date endDate, List<Long> cycleStatusList) {
        DetachedCriteria cycleCountDetachedCriteria = DetachedCriteria.forClass(CycleCount.class);


        if (warehouse != null) {
            cycleCountDetachedCriteria.add(Restrictions.eq("warehouse", warehouse));
        }

        if (cycleStatusList != null && cycleStatusList.size() > 0) {
            cycleCountDetachedCriteria.add(Restrictions.in("cycleStatus", cycleStatusList));
        }

        if (auditor != null) {
            cycleCountDetachedCriteria.add(Restrictions.eq("user", auditor));
        }

        if (product != null) {
            cycleCountDetachedCriteria.add(Restrictions.eq("product", product));
        }

        if (productVariant != null) {
            cycleCountDetachedCriteria.add(Restrictions.eq("productVariant", productVariant));
        }

        if (brand != null) {
            cycleCountDetachedCriteria.add(Restrictions.eq("brand", brand));
        }
        if (startDate != null && endDate != null) {
            cycleCountDetachedCriteria.add(Restrictions.between("createDate", startDate, endDate));
        }
        return cycleCountDetachedCriteria;
    }


    public Page searchCycleList(Long cycleCountStatus, String brand, Product product, ProductVariant productVariant, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage) {
        List<Long> cycleCountStatusList = null;
        if (cycleCountStatus != null) {
            cycleCountStatusList = new ArrayList<Long>();
            cycleCountStatusList.add(cycleCountStatus);
        }
        DetachedCriteria cyclecounDetachedCriteria = getCycleCountCriteria(brand, product, productVariant, warehouse, auditor, startDate, endDate, cycleCountStatusList);
        if (cyclecounDetachedCriteria == null) {
            return null;
        }
        cyclecounDetachedCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(cyclecounDetachedCriteria, pageNo, perPage);


    }

    public List<CycleCount> cycleCountInProgress(String brand, Product product, ProductVariant productVariant, Warehouse warehouse) {
        DetachedCriteria cyclecounDetachedCriteria = getCycleCountCriteria(brand, product, productVariant, warehouse, null, null, null, EnumCycleCountStatus.getListOfOpenCycleCountStatus());
        return (List<CycleCount>) findByCriteria(cyclecounDetachedCriteria);
    }


    public List<SkuItem> getScannedSkuItems(Long skuGroupId, Long cycleCountId) {
        List<SkuItem> skuItems = getSession().createQuery(" select ci.skuItem from CycleCountItem ci where ci.cycleCount.id = :cycleCountId and ci.skuItem.skuGroup.id = :skuGroupId").
                setParameter("cycleCountId", cycleCountId).setParameter("skuGroupId", skuGroupId).list();
        return skuItems;
    }


    public void removeScannedSkuItemFromCycleCountItem(CycleCount cycleCount, SkuItem skuItem) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CycleCountItem.class);
        detachedCriteria.add(Restrictions.eq("cycleCount", cycleCount));
        detachedCriteria.add(Restrictions.eq("skuItem", skuItem));
        CycleCountItem cycleCountItem = (CycleCountItem) findByCriteria(detachedCriteria).get(0);
        if (cycleCountItem != null) {
            delete(cycleCountItem);
        }
    }

    @Transactional
    public void deleteCycleCountItem(CycleCountItem cycleCountItem) {
        CycleCount cycleCount = cycleCountItem.getCycleCount();
        List<CycleCountItem> AllCycleCountItems = cycleCount.getCycleCountItems();
        AllCycleCountItems.remove(cycleCountItem);
        getBaseDao().save(cycleCount);
        delete(cycleCountItem);
    }

    public List<CycleCountDto> inProgressCycleCountForVariant(ProductVariant productVariant, Warehouse warehouse) {
        return inProgressCycleCounts(productVariant, warehouse);

    }

    public List<CycleCountDto> inProgressCycleCounts(Warehouse warehouse) {
        return inProgressCycleCounts(null, warehouse);
    }

    public List<CycleCountDto> inProgressCycleCounts(ProductVariant productVariant, Warehouse warehouse) {
        List<Long> cycleCountOpenStatus = EnumCycleCountStatus.getListOfOpenCycleCountStatus();
        String brand = null;
        String variantId = null;
        String productId = null;
        if (productVariant != null) {
            brand = productVariant.getProduct().getBrand();
            variantId = productVariant.getId();
            productId = productVariant.getProduct().getId();
        }
        String sql = "select cc.brand as brand , pr as product , pv  as productVariant from CycleCount cc left join cc.product pr " +
                "  left join cc.productVariant pv  where cc.warehouse.id = :warehouseId  " +
                "and cc.cycleStatus in (:cycleStatusList) ";

        if (productVariant != null) {
            sql = sql + " and (cc.productVariant.id = :productVariantId or cc.brand = :brand or cc.product.id = :productId) ";
        }

        Query query = getSession().createQuery(sql);
        query.setParameter("warehouseId", warehouse.getId()).setParameterList("cycleStatusList", cycleCountOpenStatus);

        if (productVariant != null) {
            query.setParameter("productVariantId", variantId).setParameter("productId", productId)
                    .setParameter("brand", brand);
        }
        return (List<CycleCountDto>) query.setResultTransformer(Transformers.aliasToBean(CycleCountDto.class)).list();


    }


    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
