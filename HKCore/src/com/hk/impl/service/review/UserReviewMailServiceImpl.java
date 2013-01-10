package com.hk.impl.service.review;


import com.hk.domain.review.UserReviewMail;
import com.hk.pact.dao.review.UserReviewMailDao;
import com.hk.pact.service.review.UserReviewMailService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserReviewMailServiceImpl implements UserReviewMailService {
    private UserReviewMailDao userMailReviewDao;

    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date){
        return userMailReviewDao.getAllUserReviewMailByDueDate(date);
    }
    public UserReviewMail save(UserReviewMail userReviewMail){
        return userMailReviewDao.save(userReviewMail);
    }
}
