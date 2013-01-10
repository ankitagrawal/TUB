package com.hk.pact.service.review;

import com.hk.domain.review.Mail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MailService {
    public Mail save(Mail mail);
    public Mail getMailById(Long id);
    public List<Mail> getAllMailType();
}
