<%--
  Created by IntelliJ IDEA.
  User: nitin
  Date: 13 May, 2011
  Time: 1:35:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp"%>
<s:useActionBean beanclass="com.hk.web.action.test.DoAdditionAction" var="addBean"/>

<html>
  <head>
    <title>
      Name is Addition, but does subtraction too.
    </title>
  <style type="text/css">
    input.error{background-color:red;}
  </style>
  </head>
  <body>
  <s:form beanclass="com.hk.web.action.test.DoAdditionAction">
    <s:errors/>
    <table>
      <tr>
        <td>
          Number 1 :
        </td>
        <td>
          <s:text name="number1"/>
        </td>
      </tr>
      <tr>
        <td>
          Number 2 :
        </td>
        <td>
          <s:text name="number2"/>
        </td>
      </tr>
      <tr>
        <td>
          <s:submit name="addition" value="add"/>
        </td>
        <td>
          <s:submit name="subtraction" value="subtract"/>
        </td>
      </tr>
      <tr>
        <td>
          Result :
        </td>
        <td>
          ${addBean.result}
        </td>
      </tr>
    </table>
  </s:form>
  </body>
</html>