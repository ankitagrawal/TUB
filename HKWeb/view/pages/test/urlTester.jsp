<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.test.UrlTesterAction" var="testBean"/>

<html>
<head><title>Url Tester</title></head>
<body>
<h1>URL Tester</h1>

<p>
  <s:form beanclass="com.hk.web.action.test.UrlTesterAction">
    <strong>Server (eg. http://staging01.healthkart.com) </strong> <s:text name="server"/> <br/>
    <strong>Primary Category (eg. /nutrition) </strong> <s:text name="category"/><br/>
    <s:submit name="testUrl" value="Test"/>
  </s:form>
</p>

<table cellpadding="2" cellspacing="2" border="1px dashed #ccc">
  <thead>
  <tr>
    <th>URL</th>
    <th>Code</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${testBean.urlResult}" var="urlResult">
    <tr>
      <td>${urlResult.key}</td>
      <td>${urlResult.value}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>

</body>
</html>