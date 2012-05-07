package com.hk.pact.dao.email;

import com.hk.domain.email.EmailRecepient;
import com.hk.pact.dao.BaseDao;

public interface EmailRecepientDao extends BaseDao {

    public EmailRecepient getOrCreateEmailRecepient(String recepientEmail);

    public EmailRecepient findByRecepient(String recepientEmail);

    public EmailRecepient findByUnsubscribeToken(String unsubscribeToken);

}
