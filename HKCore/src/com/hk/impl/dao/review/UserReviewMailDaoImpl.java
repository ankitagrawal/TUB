package com.hk.impl.dao.review;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.review.UserReviewMail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.review.UserReviewMailDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 2:35 AM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@Repository
public class UserReviewMailDaoImpl extends BaseDaoImpl implements UserReviewMailDao {

    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date){
        return findByNamedParams("from UserReviewMail urm where urm.dueDate = :date", new String[]{"date"}, new Object[]{date});
    }
    public UserReviewMail save(UserReviewMail userReviewMail){
        userReviewMail.setUpdateDt(BaseUtils.getCurrentTimestamp());
        return (UserReviewMail) super.save(userReviewMail);
    }

}
