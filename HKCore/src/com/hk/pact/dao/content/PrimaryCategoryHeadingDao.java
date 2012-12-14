package com.hk.pact.dao.content;

import java.util.List;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.pact.dao.BaseDao;

public interface PrimaryCategoryHeadingDao extends BaseDao {

    public List<PrimaryCategoryHeading> getHeadingsByCategory(Category category);

    public List<PrimaryCategoryHeading> getHeadingsByCategoryName(String categoryName);

    public List<PrimaryCategoryHeading> getHeadingsWithRankingByCategory(Category category);

    public List<PrimaryCategoryHeading> getHeadingsOrderedByRankingByCategory(Category category);

    public PrimaryCategoryHeading getHeadingById(Long headingId);

}
