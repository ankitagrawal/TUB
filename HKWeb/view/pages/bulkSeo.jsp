<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp"%>
<s:useActionBean beanclass="com.hk.web.action.core.content.seo.BulkSeoAction"var="bsa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
  <s:form beanclass="com.hk.web.action.core.content.seo.BulkSeoAction"method="get">
    <table>
      <tr>
        <td>
          Enter the category here :
        </td>
        <td>
          <s:text name="category"/>
        </td>
      </tr>
    </table>
    <s:submit name="edit"/>
  </s:form>
  </s:layout-component>
</s:layout-render>