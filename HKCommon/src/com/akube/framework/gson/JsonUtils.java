package com.akube.framework.gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.beanlib.hibernate.HibernateBeanReplicator;
import net.sf.beanlib.hibernate3.Hibernate3BeanReplicator;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

  private static final Gson defaultGson;

  static {
    defaultGson = new GsonBuilder()
        .setExclusionStrategies(new DefaultExclusionStrategy())
        .create();
  }

  private static class DefaultExclusionStrategy implements ExclusionStrategy {
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
      return fieldAttributes.getAnnotation(JsonSkip.class) != null;
    }

    public boolean shouldSkipClass(Class<?> aClass) {
      return false;
    }
  }

  public static Gson getGsonDefault() {
    return defaultGson;
  }

  public static Object hydrateHibernateObject(Object hibernateObject) {
    HibernateBeanReplicator beanReplicator = new Hibernate3BeanReplicator();
    return beanReplicator.copy(hibernateObject);
  }

  @SuppressWarnings("unchecked")
public static List hydrateHibernateList(List objects) {
    List list = new ArrayList();
    for (Object object : objects) {
      list.add(hydrateHibernateObject(object));
    }
    return list;
  }

  @SuppressWarnings("unchecked")
public static Set hydrateHibernateList(Set objects) {
    Set set = new HashSet();
    for (Object object : objects) {
      set.add(hydrateHibernateObject(object));
    }
    return set;
  }

}
