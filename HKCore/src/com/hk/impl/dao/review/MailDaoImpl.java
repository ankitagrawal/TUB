package com.hk.impl.dao.review;

import com.hk.domain.review.Mail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.review.MailDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class MailDaoImpl extends BaseDaoImpl implements MailDao {
    public Mail save(Mail mail){
        return (Mail) super.save(mail);
    }

    public Mail getMailById(Long id){
        return (Mail) findUniqueByNamedParams("from Mail m where m.Id = :id",new String[]{"id"},new Object[]{id});
    }

    public List<Mail> findAllMailType(){
        return findByQuery("from Mail");
    }
}
