<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" var="pa"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <%
        pageContext.setAttribute("imageTypeList", EnumImageType.getAllImageTypes());
    %>

    <s:layout-component name="content">
    <%--<p>${pa.product.id}</p>--%>
    <h2>
      ${pa.product.name}
    </h2>
    <s:form beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction">
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
            <th>
                Set Image Type
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
              <td>
                  <s:select name="productImages[${ctr.index}].imageType" style="height:30px;font-size:1.2em;padding:1px;">
                      <s:option value="">-None-</s:option>
                      <c:forEach items="${imageTypeList}" var="imageType">
                          <s:option value="${imageType.id}">${imageType.name}</s:option>
                      </c:forEach>
                  </s:select>
              </td>
              <td>
                  <s:select name="productImages[${ctr.index}].productVariant" >
                      <s:option value="">-None-</s:option>
                      <c:forEach items="${pa.product.productVariants}" var="variant" varStatus="ctr">
                          <s:option value="${variant}">${variant.id}, ${variant.optionsCommaSeparated}</s:option>
                      </c:forEach>
                  </s:select>
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