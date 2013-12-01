<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean beanclass="com.hk.web.action.test.SayHelloAction" var="helloBean"/>

<html>
  <head><title>Simple jsp page</title></head>
  <body>
  hello ${helloBean.name}
  </body>
</html>