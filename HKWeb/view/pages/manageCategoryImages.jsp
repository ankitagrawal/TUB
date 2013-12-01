<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" var="ua"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <%--<p>${pa.product.id}</p>--%>
    <h2>
        ${ua.category.displayName}
    </h2>

    <s:form beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction">
      <table>
        <tr>
          <br>
          <br>
          ${ua.errorMessage}
        </tr>
        <tr>
          <th>
            Image
          </th>
          <th>
            Hide
          </th>
          <th>
            Rank
          </th>
          <td>
            link
          </td>
        </tr>
        <c:forEach var="categoryImage" items="${ua.categoryImages}" varStatus="ctr">
          <tr>
            <td>
              <hk:categoryImage imageId="${categoryImage.id}" size="<%=EnumImageSize.SmallSize%>"/>
            </td>
            <td>
              <s:checkbox name="categoryImages[${ctr.index}].hidden"/>
            </td>
            <td>
              <s:text name="categoryImages[${ctr.index}].ranking"/>
            </td>
            <td>
              http://www.healthkart.com <s:text name="categoryImages[${ctr.index}].link" />
            </td>
            <s:hidden name="categoryImages[${ctr.index}]"/>
          </tr>

        </c:forEach>
        <tr>
          <td>          
            <s:submit name="editCategoryImageSettings" value="Submit"/>
            <s:hidden name="category" value="${ua.category}"/>
          </td>
        </tr>
      </table>

    </s:form>

  </s:layout-component>

</s:layout-render>


