<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.test.EditFeaturesAction" var="featuresBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">

    <%--<p>${featuresBean.productId}</p>--%>
    <h2>${featuresBean.product.name}</h2>
                 
    <s:form beanclass="com.hk.web.action.test.EditFeaturesAction">
      <s:hidden name="productId" value="${featuresBean.productId}"/>
      <table>
        <tr>
          <td>
            Name :
          </td>
          <td>
            <s:text name="featureName"/>
          </td>
        </tr>
        <tr>
          <td>
            Value :
          </td>
          <td>
            <s:text name="featureValue"/>
          </td>
        </tr>
        <tr>
          <td>
            <s:submit name="save" value="Add"/>
          </td>
        </tr>
      </table>
    </s:form>

     </s:layout-component>

</s:layout-render>
