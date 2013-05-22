package com.hk.web.action.core.auth;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.Resolution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * User: Pratham
 * Date: 17/05/13  Time: 11:46
*/
public class ProxyConnectAction extends BaseAction {

    public Resolution pre() {

        StringBuilder builder = new StringBuilder("");

        if (getSubject().isAuthenticated()) {
            builder.append("UniqueID=").append(getPrincipal().getId()).append("\n");
            builder.append("Name=").append(getPrincipal().getName()).append("\n");
            builder.append("Email=").append(getPrincipal().getEmail());
        }

        final String msg = builder.toString();

        return new Resolution() {
            public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/plain");
                response.getWriter().write(msg);
                response.flushBuffer();
            }
        };
    }

}
