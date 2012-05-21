package com.hk.web.tag;

import com.hk.constants.core.Keys;
import com.hk.service.ServiceLocatorFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@Component
public class VirtualHostImageTag extends TagSupport {

	@Override
	public int doEndTag() throws JspException {


		String host = (String) ServiceLocatorFactory.getProperty(Keys.Env.virtualHostImage);
		String sslHost = (String) ServiceLocatorFactory.getProperty(Keys.Env.virtualHostImageSsl);

		String useVirtualHostsString = (String) ServiceLocatorFactory.getProperty(Keys.Env.useVirtualHosts);
		String useSslVirtualHostsString = (String) ServiceLocatorFactory.getProperty(Keys.Env.useSslVirtualHosts);

		boolean useVirtualHosts =  Boolean.parseBoolean(useVirtualHostsString);
		boolean useSslVirtualHosts = Boolean.parseBoolean(useSslVirtualHostsString);


		JspWriter out = pageContext.getOut();
		try {
			if (useVirtualHosts) {
				if (useSslVirtualHosts && pageContext.getRequest().isSecure()) {
					out.write(sslHost);
				} else {
					out.write(host);
				}
			} else {
				out.write(((HttpServletRequest) pageContext.getRequest()).getContextPath());
			}
		} catch (IOException e) {
			//should not happen
		}
		return EVAL_PAGE;
	}

}
