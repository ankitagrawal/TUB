<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp"%>
<html>
  <head><title>Simple jsp page</title></head>
  <body>

  <s:form beanclass="com.hk.web.action.test.SayHelloAction">
    <s:text name="name"/>
    <s:submit name="pre"/>
  </s:form>

  </body>
</html>