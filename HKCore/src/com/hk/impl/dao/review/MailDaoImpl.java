package com.hk.impl.dao.review;

import com.hk.domain.review.Mail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.review.MailDao;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MailDaoImpl extends BaseDaoImpl implements MailDao {

    public Mail save(Mail mail){
        return (Mail) super.save(mail);
    }

    public Mail getMailById(Long id){
        return (Mail) findUniqueByNamedParams("from Mail m where m.id = :id",new String[]{"id"},new Object[]{id});
    }

    public Mail getMailByName(String name){
        return (Mail) findUniqueByNamedParams("from Mail m where m.name = :name",new String[]{"name"},new Object[]{name});
    }
    public List<Mail> findAllMailType(){
        return findByQuery("from Mail");
    }
}
