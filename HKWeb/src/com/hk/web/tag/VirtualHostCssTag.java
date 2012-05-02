package com.hk.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class VirtualHostCssTag extends TagSupport {

  /*static final boolean useVirtualHosts;
  static final String host;
  static final String sslHost;
  static final boolean useSslVirtualHosts;

  static {
    useVirtualHosts = ServiceLocatorFactory.getService(Key.get(Boolean.class, Names.named(Keys.Env.useVirtualHosts)));
    host = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.virtualHostCss)));
    sslHost = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.virtualHostCssSsl)));
    useSslVirtualHosts = ServiceLocatorFactory.getService(Key.get(Boolean.class, Names.named(Keys.Env.useSslVirtualHosts)));
  }*/

  @Override
  public int doEndTag() throws JspException {
    JspWriter out = pageContext.getOut();
    try {
    /*  if (useVirtualHosts) {
        if (useSslVirtualHosts && pageContext.getRequest().isSecure()) {
          out.write(sslHost);
        } else {
          out.write(host);
        }
      } else {*/
        out.write(((HttpServletRequest)pageContext.getRequest()).getContextPath());
     //}
    } catch (IOException e) {
      // should not happen
    }
    return EVAL_PAGE;
  }

}
