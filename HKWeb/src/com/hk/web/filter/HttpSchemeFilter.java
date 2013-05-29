package com.hk.web.filter;

import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HttpSchemeFilter implements Filter {

	/**
	 *
	 <filter> <filter-name>httpSchemeFilter</filter-name> <filter-class>
	 * com.hk.web.filter.HttpSchemeFilter </filter-class> <init-param>
	 * <param-name>urlPattern</param-name>
	 * <param-value>/core/auth/*</param-value> </init-param> <init-param>
	 * <param-name>http</param-name> <param-value>80</param-value> </init-param>
	 * <init-param> <param-name>https</param-name>
	 * <param-value>443</param-value> </init-param> </filter>
	 *
	 *
	 */

	private static final String URL_PATTERN_DELIMITERS = ",; \t\n";
	private static final String URL_PATTERN = "urlPattern";
	protected static final PatternMatcher pathMatcher = new AntPathMatcher();

	private static enum RequestScheme {
		HTTP(80, "http"), HTTPS(443, "https");

		private int defaultPort;
		private int assignedPort;
		private String scheme;

		private RequestScheme(int port, String scheme) {
			this.defaultPort = port;
			this.scheme = scheme;
		}

		public boolean isDefault() {
			return assignedPort == defaultPort;
		}
	}

	private String[] urlPatterns = new String[0];

	public void init(FilterConfig config) throws ServletException {
		String urlPattern = config.getInitParameter(URL_PATTERN);
		setUrlPatterns(StringUtils.tokenizeToStringArray(urlPattern, URL_PATTERN_DELIMITERS));
		String http = config.getInitParameter("http");
		if (http != null) {
			RequestScheme.HTTP.assignedPort = Integer.valueOf(http.trim());
		}
		String https = config.getInitParameter("https");
		if (https != null) {
			RequestScheme.HTTPS.assignedPort = Integer.valueOf(https.trim());
		}
	}

	public void setUrlPatterns(String[] urlPatterns) {
		if (urlPatterns != null) {
			Assert.noNullElements(urlPatterns, "Url Patterns must not be null");
			this.urlPatterns = new String[urlPatterns.length];
			for (int i = 0; i < urlPatterns.length; i++) {
				this.urlPatterns[i] = urlPatterns[i].trim();
			}
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		String requestURI = getPathWithinApplication(request);
		/*if (requestURI.endsWith(".js") || requestURI.endsWith(".css") || requestURI.endsWith(".jpg")
				|| requestURI.endsWith(".png") || requestURI.endsWith(".gif")) {
			chain.doFilter(request, response);
			return;
		}*/

		boolean pathMatches = false;
		for (String url : urlPatterns) {
			if (pathsMatch(url, request)) {
				pathMatches = true;
				break;
			}
		}

	/*	if (isSecure() && !pathMatches) {
			issueRedirect(request, response, RequestScheme.HTTP);
			return;
		}*/

		if (!isSecure() && pathMatches) {
			issueRedirect(request, response, RequestScheme.HTTPS);
			return;
		}

		chain.doFilter(request, response);
	}

	protected boolean pathsMatch(String path, ServletRequest request) {
		String requestURI = getPathWithinApplication(request);
		return pathMatcher.matches(path, requestURI);
	}

	protected String getPathWithinApplication(ServletRequest request) {
		return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
	}

	public static boolean isSecure() {
			HttpServletRequest curRequest =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			String isSecureString = curRequest.getHeader("x-proto");
			return isSecureString != null ? isSecureString.equals("SSL") : false;
		}

	protected void issueRedirect(ServletRequest request, ServletResponse response, RequestScheme scheme)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(scheme.scheme).append("://");
		sb.append(request.getServerName());
		if (!scheme.isDefault()) {
			sb.append(":");
			sb.append(scheme.assignedPort);
		}
		sb.append(WebUtils.toHttp(request).getRequestURI());
		String query = WebUtils.toHttp(request).getQueryString();
		if (query != null) {
			sb.append("?").append(query);
		}
		WebUtils.issueRedirect(request, response, sb.toString());
	}

	public void destroy() {
		return;
	}
}