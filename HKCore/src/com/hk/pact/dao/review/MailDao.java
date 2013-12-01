package com.hk.pact.dao.review;

import com.hk.domain.review.Mail;
import com.hk.pact.dao.BaseDao;

import java.security.PublicKey;
import java.util.List;


public interface MailDao extends BaseDao {

    public Mail save(Mail mail);

    public Mail getMailById(Long id);

    public Mail getMailByName(String name);

    public List<Mail> findAllMailType();

}
