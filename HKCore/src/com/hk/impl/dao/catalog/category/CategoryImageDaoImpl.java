package com.hk.impl.dao.catalog.category;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.category.CategoryImage;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.category.CategoryImageDao;

@SuppressWarnings("unchecked")
@Repository
public class CategoryImageDaoImpl extends BaseDaoImpl implements CategoryImageDao{

    public CategoryImage getCategoryImageByChecksum(String checksum) {
        return (CategoryImage) getSession().createQuery("from CategoryImage ci where ci.checksum = :checksum").setString("checksum", checksum).uniqueResult();
    }

   
    public List<CategoryImage> getCategoryImageByCategoryHome(Category category) {
        return (List<CategoryImage>) getSession().createQuery("from CategoryImage ci where ranking is not null and hidden != :hidden and ci.category = :category order by ranking").setBoolean(
                "hidden", true).setParameter("category", category).list();
    }

    public List<CategoryImage> getCategoryImageByCategory(Category category) {
        return (List<CategoryImage>) getSession().createQuery("from CategoryImage ci where ci.category = :category").setParameter("category", category).list();
    }
}
