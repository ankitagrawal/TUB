package com.hk.impl.service.review;

import com.hk.domain.review.Mail;
import com.hk.pact.dao.review.MailDao;
import com.hk.pact.service.review.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private MailDao mailDao;

    public Mail save(Mail mail){
        return mailDao.save(mail);
    }
    public Mail getMailById(Long id){
        return mailDao.getMailById(id);
    }

    public Mail getMailByName(String name){
        return mailDao.getMailByName(name);
    }
    public List<Mail> getAllMailType(){
        return mailDao.findAllMailType();
    }
}
