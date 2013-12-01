<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.content.seo.BulkSeoAction" var="bsa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.core.content.seo.BulkSeoAction">
      <table border="1">
        <tr>
          <th >
            Product Name
          </th>
          <th>
            Heading
          </th>
          <th>
            Title
          </th>
          <th>
            Meta Keywords
          </th>
          <th>
            Meta Description
          </th>
          <th>
            Description
          </th>
        </tr>
        <c:forEach var="seo" items="${bsa.seoProductDtoList}" varStatus="ctr">
          <tr>
            <s:hidden name="seoProductDtoList[${ctr.index}].seoData.id" value="${seo.seoData.id}"/>
            <s:hidden name="seoProductDtoList[${ctr.index}].product" value="${seo.product.id}"/>
            <td>
              <b>${seo.product.name}</b>
            </td>
            <td>
              <s:textarea name="seoProductDtoList[${ctr.index}].seoData.h1"/>
            </td>
            <td>
              <s:textarea name="seoProductDtoList[${ctr.index}].seoData.title"/>
            </td>
            <td>
              <s:textarea name="seoProductDtoList[${ctr.index}].seoData.metaKeyword"/>
            </td>
            <td>
              <s:textarea name="seoProductDtoList[${ctr.index}].seoData.metaDescription"/>
            </td>
            <td>
              <s:textarea name="seoProductDtoList[${ctr.index}].seoData.description"/>
            </td>
          </tr>
        </c:forEach>
      </table>
      <br/>
      <s:submit name="save" value="save"/>
      <s:hidden name="category" value="${bsa.category}"/>
    </s:form>
  </s:layout-component>
</s:layout-render>