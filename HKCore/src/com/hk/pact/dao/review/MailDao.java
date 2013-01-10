package com.hk.pact.dao.review;

import com.hk.domain.review.Mail;
import com.hk.pact.dao.BaseDao;

import java.security.PublicKey;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MailDao extends BaseDao {
    public Mail save(Mail mail);
    public Mail getMailById(Long id);
    public List<Mail> findAllMailType();

}
