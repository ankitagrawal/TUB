package com.hk.impl.dao.user;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.Order;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.domain.user.UserProductHistory;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UserProductHistoryDao;

@Repository
public class UserProductHistoryDaoImpl extends BaseDaoImpl implements UserProductHistoryDao {
  @Transactional
  public void addToUserProductHistory(Product product, User user) {
    if (!user.getRoles().contains(get(Role.class, RoleConstants.HK_EMPLOYEE))){
      if (!incrementProductCounterIfAlreadyExists(product, user)) {
        UserProductHistory userProductHistory = new UserProductHistory();
        userProductHistory.setCreateDate(new Date());
        userProductHistory.setLastDate(new Date());
        userProductHistory.setProduct(product);
        userProductHistory.setUser(user);
        userProductHistory.setBought(false);
        userProductHistory.setCounter(1L);
        save(userProductHistory);
      }
    }
  }  

  @Transactional
  private boolean incrementProductCounterIfAlreadyExists(Product product, User user) {
    UserProductHistory userProductHistory = findByProductAndUser(product, user);
    if (userProductHistory != null && userProductHistory.getOrder() == null) {
      userProductHistory.setLastDate(new Date());
      userProductHistory.setBought(false);
      userProductHistory.setCounter(userProductHistory.getCounter() + 1);
      save(userProductHistory);
      return true;
    }
    return false;
  }

  @Transactional
  public UserProductHistory findByProductAndUser(Product product, User user) {
    Criteria criteria = getSession().createCriteria(UserProductHistory.class);
    criteria.add(Restrictions.eq("product", product));
    criteria.add(Restrictions.eq("user", user));
    criteria.add(Restrictions.isNull("order"));
    List<UserProductHistory> userProductHistoryList = criteria.list();
    return userProductHistoryList == null || userProductHistoryList.isEmpty() ? null : userProductHistoryList.get(0);
  }

  public List<UserProductHistory> findByUser(User user) {
      Criteria criteria = getSession().createCriteria(UserProductHistory.class);
      criteria.add(Restrictions.eq("user", user));
      return criteria.list();
    }


  @Transactional
  public void updateIsAddedToCart(Product product, User user, Order order) {
    Criteria criteria = getSession().createCriteria(UserProductHistory.class);
    criteria.add(Restrictions.eq("product", product));
    criteria.add(Restrictions.eq("user", user));
    List<UserProductHistory> userProductHistoryList = criteria.list();
    if(!(userProductHistoryList == null || userProductHistoryList.isEmpty())){
      for(UserProductHistory userProductHistory : userProductHistoryList){
        if(userProductHistory.getOrder() == null){
          userProductHistory.setBought(true);
          userProductHistory.setOrder(order);
          save(userProductHistory);
          break;
        }
      }
    }
  }
}
