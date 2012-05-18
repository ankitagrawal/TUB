package com.hk.web.tag;

import com.hk.constants.core.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class VirtualHostCssTag extends TagSupport {

	static boolean useVirtualHosts;
	static boolean useSslVirtualHosts;


	@Value("#{hkEnvProps['" + Keys.Env.virtualHostImage + "']}")
	private static String host;
	@Value("#{hkEnvProps['" + Keys.Env.virtualHostImageSsl + "']}")
	private static String sslHost;

	@Value("#{hkEnvProps['" + Keys.Env.useVirtualHosts + "']}")
	private static String useVirtualHostsString;
	@Value("#{hkEnvProps['" + Keys.Env.useSslVirtualHosts + "']}")
	private static String useSslVirtualHostsString;


	@PostConstruct
	public void postConstruction() {
	  useVirtualHosts = Boolean.parseBoolean(useSslVirtualHostsString);
	  useSslVirtualHosts = Boolean.parseBoolean(useSslVirtualHostsString);
	}

  @Override
  public int doEndTag() throws JspException {
    JspWriter out = pageContext.getOut();
    try {
      if (useVirtualHosts) {
        if (useSslVirtualHosts && pageContext.getRequest().isSecure()) {
          out.write(sslHost);
        } else {
          out.write(host);
        }
      } else {
        out.write(((HttpServletRequest)pageContext.getRequest()).getContextPath());
     }
    } catch (IOException e) {
      // should not happen
    }
    return EVAL_PAGE;
  }

}
