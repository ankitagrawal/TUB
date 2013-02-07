package com.hk.impl.dao.review;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.review.UserReviewMail;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.review.UserReviewMailDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class UserReviewMailDaoImpl extends BaseDaoImpl implements UserReviewMailDao {

    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date){
        return findByNamedParams("from UserReviewMail urm where date(urm.dueDate) <= :date AND urm.isMailSent = false", new String[]{"date"}, new Object[]{date});
    }

    public UserReviewMail getUserReviewMailByOrder(Order order){
        List<UserReviewMail> result = findByNamedParams("from UserReviewMail urm where urm.baseOrder = :order ORDER BY urm.updateDt DESC", new String[]{"order"}, new Object[]{order});
        if(result !=  null && result.size()>0){
            return result.get(0);
        }else
            return null;
    }
    public UserReviewMail save(UserReviewMail userReviewMail){
        return (UserReviewMail) super.save(userReviewMail);
    }

    public UserReviewMail getByUserAndProductVariant(User user, ProductVariant productVariant){
        List<UserReviewMail> result = findByNamedParams("from UserReviewMail urm where urm.user = :user AND urm.productVariant = :productVariant ORDER BY urm.updateDt DESC", new String[]{"user", "productVariant"}, new Object[]{user, productVariant});
        if(result !=null && result.size()>0){
            return result.get(0);
        }else
            return null;

    }

}
