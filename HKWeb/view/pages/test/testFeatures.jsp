<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.test.EditFeaturesAction" var="featuresBean"/>


<html>
  <head><title></title></head>
  <body>
  <s:form beanclass="web.action.ProductAction" method="get">
    <table>
      <tr>
        <td>
          Enter the product id :
        </td>
        <td>
          <s:text name="product"/>
        </td>
        <td>
          <s:submit name="editFeatures"/>
        </td>
      </tr>
    </table>
  </s:form>
  </body>
</html>