package com.akube.framework.shiro.manager;

import org.apache.shiro.web.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.shiro.realm.HibernateSecurityRealm;

public class ShiroSecurityManager extends DefaultWebSecurityManager {

    private HibernateSecurityRealm hibernateSecurityRealm;

    @Autowired
    public ShiroSecurityManager(HibernateSecurityRealm hibernateSecurityRealm) {
        this.hibernateSecurityRealm = hibernateSecurityRealm;

        setRealm(hibernateSecurityRealm);
        setSessionMode(DefaultWebSecurityManager.HTTP_SESSION_MODE);
        setRememberMeCookieMaxAge(3600 * 24 * 30); // 30 days
        setCacheManager(null);
    }

    public HibernateSecurityRealm getHibernateSecurityRealm() {
        return hibernateSecurityRealm;
    }

}
