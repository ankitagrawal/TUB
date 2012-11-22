package com.hk.admin.pact.service.email;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;

/**
 * Created by IntelliJ IDEA. User: Rohit Date: Jun 13, 2012 Time: 4:12:27 PM To change this template use File | Settings |
 * File Templates.
 */
public interface AdminEmailService {
    public List<EmailRecepient> getAllMailingList(EmailCampaign emailCampaign, List<Role> roleList, int maxResult);

    public List<EmailRecepient> getAllMailingList(EmailCampaign emailCampaign, List<Role> roleList, int pageNo, int maxResult);

    public Long getAllMailingListCount(EmailCampaign emailCampaign, List<Role> roles);

    public List<EmailRecepient> getMailingListByCategory(EmailCampaign emailCampaign, Category category, int maxResult);

    public Long getMailingListCountByCategory(EmailCampaign emailCampaign, Category category);

    public List<EmailRecepient> getMailingListByEmailIds(EmailCampaign emailCampaign, List<String> emailList, int maxResult);

    public List<User> findAllUsersNotInEmailRecepient(int maxResult, List<String> userIdList);

    public List<EmailRecepient> getUserMailingList(EmailCampaign emailCampaign, List<Long> userList, int maxResult);

    @SuppressWarnings("unchecked")
    public boolean saveOrUpdate(Session session, Collection entities) throws DataAccessException;

    Long getMailingListCountByCampaign(EmailCampaign emailCampaign);

    public List<String> getEmailRecepientsByEmailIds(EmailCampaign emailCampaign, List<EmailRecepient> emailList);
}
