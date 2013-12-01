package com.hk.impl.dao.sku;

import java.util.List;

import com.hk.impl.dao.warehouse.WarehouseDaoImpl;
import com.hk.pact.dao.warehouse.WarehouseDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuDao;

@Repository
public class SkuDaoImpl extends BaseDaoImpl implements SkuDao {

    public Sku getSku(ProductVariant productVariant, Warehouse warehouse) {
        String queryString = "from Sku sku where sku.warehouse = ? and sku.productVariant = ? ";
        return (Sku) findUnique(queryString, new Object[] { warehouse, productVariant });
    }


    @SuppressWarnings("unchecked")
    public List<Sku> getSkus(ProductVariant productVariant) {
        String queryString = " from Sku sku where sku.productVariant = ? ";
        return (List<Sku>) findByQuery(queryString, new Object[] { productVariant });

    }

	@SuppressWarnings("unchecked")
	public List<Sku> getSkus(ProductVariant productVariant, List<Warehouse> warehouseList) {
		String queryString = " from Sku sku where sku.productVariant = :productVariant and sku.warehouse in (:warehouseList) ";
		return (List<Sku>) findByNamedParams(queryString, new String[]{"productVariant", "warehouseList"}, new Object[] {productVariant, warehouseList});

	}

    @SuppressWarnings("unchecked")
    public List<Sku> filterProductVariantsByWarehouse(List<ProductVariant> productVariants, Warehouse warehouse) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Sku.class);
        criteria.add(Restrictions.in("productVariant", productVariants));
        criteria.add(Restrictions.eq("warehouse", warehouse));
        return (List<Sku>) findByCriteria(criteria);
    }

	@SuppressWarnings("unchecked")
	public List<Sku> getSKUsByProductNameAndWarehouse(String productName, Long warehouseId) {
		String query = "select sku from Sku sku where sku.productVariant.product.name like :productName and sku.warehouse.id = :warehouse ";
		return (List<Sku>) findByNamedParams(query, new String[]{"productName", "warehouse"}, new Object[] {"%" + productName + "%", warehouseId});
	}

}
