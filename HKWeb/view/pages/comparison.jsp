<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.CompareAction" var="compareBean"/>

<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="topHeading">Product Comparison</s:layout-component>
  <s:layout-component name="lhsContent">

    <table class="zebra_vert" width="955px" style="margin-bottom:20px;">
      <thead>
      <tr>
        <td>
          Feature
        </td>
        <c:forEach var="product" items="${compareBean.products}">
          <td>
              ${product.name}
          </td>
        </c:forEach>
      </tr>
      </thead>
      <tbody>
      <tr class="even_row">
        <td>
          &nbsp;
        </td>
        <c:forEach var="product" items="${compareBean.products}">
          <td>
            <c:choose>
              <c:when test="${product.mainImageId != null}">
                <hk:productImage imageId="${product.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
              </c:when>
              <c:otherwise>
                <img class="prod48" src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${product.id}.jpg" alt="${product.name}"/>
              </c:otherwise>
            </c:choose>
          </td>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${compareBean.table.rows}">
        <tr>
          <td style="font-size:0.9em; color:black; font-weight:bold;">
              ${row.feature}
          </td>
          <c:forEach var="attribute" items="${row.cells}">
            <td style="font-size:0.9em;">
                ${attribute}
            </td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr class="even_row">
        <td>
          &nbsp;
        </td>
        <c:forEach var="product" items="${compareBean.products}">
          <td>
            <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" title="${product.name}" class="prod_link">
              <s:param name="productId" value="${product.id}"/>
              <s:param name="productSlug" value="${product.slug}"/>
              Buy Now
            </s:link>
          </td>
        </c:forEach>
      </tr>
      </tbody>

    </table>
  </s:layout-component>
</s:layout-render>
