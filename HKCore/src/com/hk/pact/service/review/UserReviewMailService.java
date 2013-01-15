package com.hk.pact.service.review;

import com.hk.domain.order.Order;
import com.hk.domain.review.UserReviewMail;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 11:58 AM
 */
public interface UserReviewMailService {

    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date);

    public UserReviewMail save(UserReviewMail userReviewMail);

    public void userEntry(Order order);

}
