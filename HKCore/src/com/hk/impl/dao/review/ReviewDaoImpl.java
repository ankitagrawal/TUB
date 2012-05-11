package com.hk.impl.dao.review;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.constants.review.EnumReviewStatus;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ReviewStatus;
import com.hk.domain.review.UserReview;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.review.ReviewDao;

@Repository
public class ReviewDaoImpl extends BaseDaoImpl implements ReviewDao {

    public Page getProductReviews(Product product, List<ReviewStatus> reviewStatusList, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserReview.class);
        if (product != null) {
            criteria.add(Restrictions.eq("product", product));
        }
        criteria.add(Restrictions.in("reviewStatus", reviewStatusList));
        criteria.addOrder(org.hibernate.criterion.Order.desc("reviewDate"));
        return list(criteria, page, perPage);
    }

    public Double getStarRating(Product product) {
        Double starRating = (Double) getSession().createQuery(
                "select (sum(o.starRating)/count(o.id)) from UserReview o where o.product = :product and o.reviewStatus.id = :reviewStatusId ").setParameter("product", product).setParameter(
                "reviewStatusId", EnumReviewStatus.Published.getId()).uniqueResult();
        return starRating != null && starRating >= 2.5 ? starRating : 2.5;
    }

    @SuppressWarnings("unchecked")
    public List<ReviewStatus> getReviewStatusList(List<Long> reviewStatusIdList) {
        return getSession().createQuery("from ReviewStatus rs where rs.id in (:statusIdList)").setParameterList("statusIdList", reviewStatusIdList).list();
    }
}
