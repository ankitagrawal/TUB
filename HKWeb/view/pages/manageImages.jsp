<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.EditProductAttributesAction" var="pa"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <%--<p>${pa.product.id}</p>--%>
    <h2>
      ${pa.product.name}
    </h2>
    <s:form beanclass="com.hk.web.action.admin.EditProductAttributesAction">
       <table>
        <tr>
          <th>
            Image
          </th>
          <th>
            Hide
          </th>
          <th>
            Set as Main
          </th>
        </tr>
        <c:forEach var="productImage" items="${pa.productImages}" varStatus="ctr">
          <tr>
            <td>
              <hk:productImage imageId="${productImage.id}" size="<%=EnumImageSize.MediumSize%>"/>
            </td>
            <td>
              <s:hidden name="productImages[${ctr.index}]"/>
              <s:checkbox name="productImages[${ctr.index}].hidden"/>
            </td>
            <td>
              <s:radio value="${productImage.id}" name="mainImageId"/>
            </td>
          </tr>
        </c:forEach>
        <tr>
          <td>
          <s:submit name="editImageSettings" value="Submit"/>
           <s:hidden name="productId" value="${pa.productId}"/>
          </td>
        </tr>
      </table>

    </s:form>

  </s:layout-component>

</s:layout-render>