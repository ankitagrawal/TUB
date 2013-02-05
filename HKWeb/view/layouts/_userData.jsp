<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%
	PrincipalImpl principal = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
	if (principal != null) {
		pageContext.setAttribute("user_hash", principal.getUserHash());
	} else {
		pageContext.setAttribute("user_hash", "guest");
	}
%>
