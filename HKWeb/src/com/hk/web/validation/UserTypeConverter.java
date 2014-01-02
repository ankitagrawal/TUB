package com.hk.web.validation;

import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.user.UserDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

@Component
public class UserTypeConverter implements TypeConverter<User> {
  public void setLocale(Locale locale) {
  }

  @Autowired
  private BaseDao baseDao;

  @Autowired
  private UserDao userDao;

  public User convert(String s, Class<? extends User> aClass, Collection<ValidationError> validationErrors) {
    User user = null;
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
      user = getBaseDao().get(User.class, idLong);
    } catch (NumberFormatException e) {
      //Try getting user through Hash
      user = getUserDao().findByUserHash(s);
    }
    return user;
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

  public UserDao getUserDao() {
    return userDao;
  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
}
