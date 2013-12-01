package com.akube.framework.dao;


/**
 * @author vaibhav.adlakha
 */
public class SearchDao {

 /* protected com.google.inject.Provider<Session> sessionProvider;
  //protected com.google.inject.Provider<Session> sessionProviderRead;

  @Inject
  public void setSessionProvider(Provider<Session> sessionProvider) {
    this.sessionProvider = sessionProvider;
  }

  @Inject
  public void setSessionProviderRead(@ReadDBSession Provider<Session> sessionProviderRead) {
    this.sessionProviderRead = sessionProviderRead;
  }

  @SuppressWarnings("unchecked")
  public Object find(Class entityClass, Serializable id) {
    return getSession().get(entityClass, id);
  }

  public com.google.inject.Provider<Session> getSessionProvider() {
    DBConfigValue configValue = DBConfigThreadLocal.get();

    if (configValue != null) {
      if (DBConfigValue.ReadOnlySlave == configValue) {
        return sessionProviderRead;
      }
    }
    return sessionProvider;
  }*/
}
