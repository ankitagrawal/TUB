package com.hk.pact.dao.review;

import com.hk.domain.review.UserReviewMail;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/7/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserReviewMailDao extends BaseDao {
    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date);
    public UserReviewMail save(UserReviewMail userReviewMail);


}
