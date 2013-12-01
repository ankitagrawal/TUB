package com.akube.framework.shiro.manager;

import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.shiro.realm.HibernateSecurityRealm;

public class ShiroSecurityManager extends DefaultWebSecurityManager {

    private HibernateSecurityRealm hibernateSecurityRealm;

    private static final String rememberMeCookieName="SSID";

    @Autowired
    public ShiroSecurityManager(HibernateSecurityRealm hibernateSecurityRealm) {
        this.hibernateSecurityRealm = hibernateSecurityRealm;

        setRealm(hibernateSecurityRealm);
        CookieRememberMeManager rememberMeManager=new CookieRememberMeManager();
        SimpleCookie cookie=new SimpleCookie();
        cookie.setName(rememberMeCookieName);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600 * 24 * 30);
        cookie.setPath("/");
        rememberMeManager.setCookie(cookie);
        setRememberMeManager(rememberMeManager);
        setCacheManager(null);
    }

    public HibernateSecurityRealm getHibernateSecurityRealm() {
        return hibernateSecurityRealm;
    }
    
   

}
