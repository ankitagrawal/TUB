package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.Product;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.product.ProductDao;

@Component
public class ProductTypeConverter implements TypeConverter<Product> {
    public void setLocale(Locale locale) {
    }

    @Autowired
    private BaseDao baseDao;

    ProductDao      productDao;

    public Product convert(String s, Class<? extends Product> aClass, Collection<ValidationError> validationErrors) {
        if (s == null) {
            return null;
        } else {
            return getBaseDao().get(Product.class, s);
            // return productDao.getProductById(s);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
