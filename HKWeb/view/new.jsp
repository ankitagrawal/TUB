<%--
  Created by IntelliJ IDEA.
  User: Rahul
  Date: 11/2/13
  Time: 3:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title></title>
</head>

<%
	Cookie cookie = new Cookie("UI", "N");
	cookie.setMaxAge(30 * 60);
	response.addCookie(cookie);
	String redirectURL = "http://www.healthkart.com";
	response.sendRedirect(redirectURL);
%>

<body>
hello
</body>
</html>