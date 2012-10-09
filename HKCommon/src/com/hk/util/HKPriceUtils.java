package com.hk.util;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.pact.dao.BaseDao;
import com.hk.service.ServiceLocatorFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 9/26/12
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class HKPriceUtils {
    /**
     * IF its a product then return the price of minimum variant else if its a combo returns the Combo Price
     * @param product
     * @return
     */
    
    /**
     * Not the best example of using ServiceLocator, please avoid using in such ways.
     */
    public static double getMinimumProductPrice(Product product){
        BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);
        Combo combo = baseDao.get(Combo.class, product.getId());
        boolean isCombo = (combo != null);
        if (!isCombo){
            return product.getMinimumHKPriceProductVariant().getHkPrice();
        }
        return combo.getHkPrice();
    }

    public static double getMinimumMarkedPrice(Product product){
        BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);
        Combo combo = baseDao.get(Combo.class, product.getId());
        boolean isCombo = (combo != null);
        if (!isCombo){
            return product.getMinimumHKPriceProductVariant().getMarkedPrice();
        }
        return combo.getMarkedPrice();
    }

}
