<%@ page import="java.util.Enumeration" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<html>
<head>
  <title>Hello</title>
</head>
<body>
<h1>Hello!!</h1>

<table width="100%" border="1" align="center">
<tr bgcolor="#949494">
<th>Header Name</th><th>Header Value(s)</th>
</tr>

<%
	Enumeration headerNames = request.getHeaderNames();
   while(headerNames.hasMoreElements()) {
      String paramName = (String)headerNames.nextElement();
      out.print("<tr><td>" + paramName + "</td>\n");
      String paramValue = request.getHeader(paramName);
      out.println("<td> " + paramValue + "</td></tr>\n");

   }
	String xProto = request.getHeader("x-proto");
        if(xProto != null){
                out.println("this is ssl ");
        }
%>
</table>

<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
	out.println("is secure -> " + isSecure);
%>

<a href="http://www.healthkart.com">HK Unsecure</a>
&nbsp;&nbsp;
<a href="https://www.healthkart.com">HK Secure</a>

</body>
</html>