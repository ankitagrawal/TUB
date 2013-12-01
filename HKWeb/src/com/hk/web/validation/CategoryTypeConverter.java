package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.category.Category;
import com.hk.pact.dao.BaseDao;

@Component
public class CategoryTypeConverter implements TypeConverter<Category> {

    public void setLocale(Locale locale) {
        // nothing
    }

    // CategoryDao categoryDao;
    @Autowired
    private BaseDao baseDao;

    public Category convert(String id, Class<? extends Category> aClass, Collection<ValidationError> validationErrors) {
        if (id == null) {
            return null;
        } else {
            return getBaseDao().get(Category.class, id);
            // return categoryDao.getCategoryByName(id);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
