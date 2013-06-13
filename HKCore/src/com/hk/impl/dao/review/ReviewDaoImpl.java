package com.hk.impl.dao.review;

import java.util.List;

import com.hk.domain.user.User;
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

    public Page getProductReviewsForCustomer(Product product, List<Long> reviewStatusList, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserReview.class);
        if (product != null) {
            criteria.add(Restrictions.eq("product", product));
        }
        criteria.add(Restrictions.in("reviewStatus.id", reviewStatusList));
        criteria.addOrder(org.hibernate.criterion.Order.desc("reviewDate"));
        return list(criteria, page, perPage);
    }

   public Long getAllReviews(Product product, List<Long> reviewStatusList) {
    Long starRating = (Long) getSession().createQuery("select count(o.id) from UserReview o where o.product = :product and o.reviewStatus.id in (:reviewStatusList) ").
        setParameter("product", product).
        setParameterList("reviewStatusList", reviewStatusList).uniqueResult();
    return starRating;
  }

    public Double getAverageRating(Product product) {
        Double starRating = (Double) getSession().createQuery(
                "select (sum(o.starRating)/count(o.id)) from UserReview o where o.product = :product and o.reviewStatus.id = :reviewStatusId ").setParameter("product", product).setParameter(
                "reviewStatusId", EnumReviewStatus.Published.getId()).uniqueResult();
        return starRating;
    }

    @Override
    public Page getProductReviewsForAdmin(Product product, List<Long> reviewStatusList, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserReview.class);
        if (product != null) {
            criteria.add(Restrictions.eq("product", product));
        }
        criteria.add(Restrictions.in("reviewStatus.id", reviewStatusList));
        criteria.addOrder(org.hibernate.criterion.Order.desc("reviewDate"));
        return list(criteria, page, perPage);
    }

    public UserReview getReviewByUserAndProduct(User user, Product product){
        List<UserReview> result =  findByNamedParams("from UserReview u where u.postedBy = :user AND u.product = :product ORDER BY u.reviewDate DESC", new String[]{"user","product"}, new Object[]{user, product});
        if(result !=  null && result.size()>0){
            return result.get(0);
        }else
            return null;
    }

    @SuppressWarnings("unchecked")
    public List<ReviewStatus> getReviewStatusList(List<Long> reviewStatusIdList) {
        return getSession().createQuery("from ReviewStatus rs where rs.id in (:statusIdList)").setParameterList("statusIdList", reviewStatusIdList).list();
    }
}
