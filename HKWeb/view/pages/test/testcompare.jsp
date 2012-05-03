<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.CompareAction" var="compareBean"/>
<html>
<head>
  <title></title>
</head>
<body>
<%--<s:form beanclass="com.hk.web.action.core.catalog.CompareAction">--%>
  <%--<table>--%>
    <%--<tr>--%>
      <%--<td>--%>
        <%--Enter Product ID :--%>
      <%--</td>--%>
      <%--<td>--%>
        <%--<s:text name="productId"/>--%>
      <%--</td>--%>
      <%--<td>--%>
        <%--<s:submit name="addToProducts"value="Add"/>--%>
      <%--</td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
      <%--<td>--%>
        <%--<s:submit name="createTable"value="compare"/>--%>
      <%--</td>--%>
    <%--</tr>--%>
  <%--</table>--%>
<%--</s:form>:--%>
<s:form beanclass="com.hk.web.action.core.catalog.CompareAction">
  <s:checkbox name="productIds[]" value="DL001"/>DL001
  <s:checkbox name="productIds[]" value="DL002"/>DL002
  <s:checkbox name="productIds[]" value="DL003"/>DL003
  <s:submit name="createTable">compare</s:submit>


</s:form>
<%--<s:link beanclass="com.hk.web.action.core.catalog.CompareAction" event="createTable" >Compare</s:link>--%>
</body>
</html>