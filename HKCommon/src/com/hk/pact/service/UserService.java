package com.hk.pact.service;

import java.util.List;
import java.math.BigInteger;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.order.Order;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;

public interface UserService {

  public static final Long ADMIN_USER_ID = 1L;

  public User getUserById(Long userId);

  public User findByLogin(String email);

  public List<User> findByEmail(String email);

  public User getAdminUser();

  public User getLoggedInUser();

  public Warehouse getWarehouseForLoggedInUser();

  public Page getMailingList(Category category, int pageNo, int perPage);

  public Page getAllMailingList(int pageNo, int perPage);

  public Page getAllUnverifiedMailingList(int pageNo, int perPage);

  public void updateIsProductBought(Order order);

  public User findByUserHash(String userHash);

  public User save(User user);

  public List<User> findByRole(Role role);

  public Page findByRole(Role role, int pageNo, int perPage);

  public User findByLoginAndStoreId(String login, Long storeId);

  public List<EmailRecepient> getAllMailingList(EmailCampaign emailCampaign, String[] roles, String[] userIds, int maxResult);

  public BigInteger getAllMailingListCount(EmailCampaign emailCampaign, String [] roles);

  public List<EmailRecepient> getMailingListByCategory(EmailCampaign emailCampaign, Category category, int maxResult);

  public Long getMailingListCountByCategory(EmailCampaign emailCampaign, Category category);

  public List<EmailRecepient> getMailingListByEmailIds(EmailCampaign emailCampaign, List<String> emailList, int maxResult);

  public List<User> findAllUsersNotInEmailRecepient();
}
