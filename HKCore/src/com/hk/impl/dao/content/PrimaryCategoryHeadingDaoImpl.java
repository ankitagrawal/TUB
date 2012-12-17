package com.hk.impl.dao.content;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.content.PrimaryCategoryHeadingDao;

@SuppressWarnings("unchecked")
@Repository
public class PrimaryCategoryHeadingDaoImpl extends BaseDaoImpl implements PrimaryCategoryHeadingDao {

    public List<PrimaryCategoryHeading> getHeadingsByCategory(Category category) {
        List<PrimaryCategoryHeading> headings;
        if (category != null) {
            headings = getSession().createQuery("select h from PrimaryCategoryHeading h where h.category.name = (:categoryName)").setParameter("categoryName", category.getName()).list();
        } else {
            headings = getSession().createQuery("select h from PrimaryCategoryHeading h where h.category is null ").list();
        }
        return headings;
    }

    public List<PrimaryCategoryHeading> getHeadingsByCategoryName(String categoryName) {
        return getSession().createQuery("select h from PrimaryCategoryHeading h where h.category.name = (:categoryName)").setParameter("categoryName", categoryName).list();
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

  public PrimaryCategoryHeading getHeadingById(Long headingId){
       return (PrimaryCategoryHeading)getSession().createQuery("select h from PrimaryCategoryHeading h where h.id = :headingId").setParameter("headingId",headingId).list().get(0);
  }

}
