package com.hk.dao.content;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.content.PrimaryCategoryHeading;

@SuppressWarnings("unchecked")
@Repository
public class PrimaryCategoryHeadingDao extends BaseDaoImpl {

    public List<PrimaryCategoryHeading> getHeadingsByCategory(Category category) {
        List<PrimaryCategoryHeading> headings;
        if (category != null) {
            headings = getSession().createQuery("select h from PrimaryCategoryHeading h where h.category.name = (:categoryName)").setParameter("categoryName", category.getName()).list();
        } else {
            headings = getSession().createQuery("select h from PrimaryCategoryHeading h where h.category is null ").list();
        }
        return headings;
    }

    public List<PrimaryCategoryHeading> getHeadingsWithRankingByCategory(Category category) {
        List<PrimaryCategoryHeading> headings;
        if (category != null) {
            headings = getSession().createQuery("select h from PrimaryCategoryHeading h where h.category.name = (:categoryName) and h.ranking is not null order by h.ranking ").setParameter(
                    "categoryName", category.getName()).list();
        } else {
            headings = getSession().createQuery("select h from PrimaryCategoryHeading h where h.category is null ").list();
        }
        return headings;
    }

    public List<PrimaryCategoryHeading> getHeadingsOrderedByRankingByCategory(Category category) {
        List<PrimaryCategoryHeading> headings;
        if (category != null) {
            headings = getSession().createQuery("select h from PrimaryCategoryHeading h where h.category.name = (:categoryName) order by h.ranking").setParameter("categoryName",
                    category.getName()).list();
        } else {
            headings = getSession().createQuery("select h from PrimaryCategoryHeading h where h.category is null ").list();
        }
        return headings;
    }

}
