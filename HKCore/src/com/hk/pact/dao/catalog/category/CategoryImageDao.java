package com.hk.pact.dao.catalog.category;

import java.util.List;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.category.CategoryImage;
import com.hk.pact.dao.BaseDao;

public interface CategoryImageDao extends BaseDao {

    public CategoryImage getCategoryImageByChecksum(String checksum);

    public List<CategoryImage> getCategoryImageByCategoryHome(Category category);

    public List<CategoryImage> getCategoryImageByCategory(Category category);
}
