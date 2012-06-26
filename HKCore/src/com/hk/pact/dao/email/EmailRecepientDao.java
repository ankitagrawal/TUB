package com.hk.pact.dao.email;

import com.hk.domain.email.EmailRecepient;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface EmailRecepientDao extends BaseDao {

    public EmailRecepient getOrCreateEmailRecepient(String recepientEmail);

    public EmailRecepient findByRecepient(String recepientEmail);

    public EmailRecepient findByUnsubscribeToken(String unsubscribeToken);

    public List<String> findEmailIdsPresentInEmailRecepient(List<String> mailingList);

    public EmailRecepient createEmailRecepient(String recepientEmail);

    //For creating object only, and not saving into DB
    public EmailRecepient createEmailRecepientObject(String recepientEmail);
}
