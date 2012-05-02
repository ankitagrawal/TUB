package com.hk.web;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: kani
 * Time: 7 Jan, 2010 6:00:50 PM
 */
@Component
public class BatchProcessWorkManager 
//implements WorkManager 
{

    
  @SuppressWarnings("unused")
private final SessionFactory sessionFactory;

  @Autowired
  public BatchProcessWorkManager(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void beginWork() {
    // do nothing if a session is already open
   /* if (ManagedSessionContext.hasBind(sessionFactory.openSession())) {
      return;
    }

    // open session
    ManagedSessionContext.bind(sessionFactory.get().openSession());*/
  }

  public void endWork() {
 /*   // do nothing if there is no session open
    SessionFactory sessionFactory = sessionFactory.get();
    if (!ManagedSessionContext.hasBind(sessionFactory)) {
      return;
    }

    // close session when done
    try {
      sessionFactory.getCurrentSession().close();
    } finally {
      ManagedSessionContext.unbind(sessionFactory);
    }*/
  }
}

