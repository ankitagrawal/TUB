package com.hk.pact.service.review;

import com.hk.domain.review.Mail;

import java.util.List;


public interface MailService {
    public Mail save(Mail mail);
    public Mail getMailById(Long id);
    public Mail getMailByName(String name);
    public List<Mail> getAllMailType();
}
