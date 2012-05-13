<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductVariantAction" var="pva"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.core.catalog.product.ProductVariantAction">
      Choose Variant
      <s:select name="productVariant">
        <c:forEach items="${pva.productVariant.product.productVariants}" var="variant" varStatus="ctr">
          <s:option value="${variant}">${variant.id}</s:option>
        </c:forEach>
      </s:select>
      <s:submit name="renderManageImages" value="Edit"/>
      <s:hidden name="productVariant" value="${pva.productVariant.id}"/>
    </s:form>

    <table>
    <tr>
      <th>
        Image
      </th>
      <th>
        Hide
      </th>
      <th>
        Set as Anchor
      </th>
      <th>
        Set as PV image
      </th>
    </tr>
    <s:form beanclass="com.hk.web.action.core.catalog.product.ProductVariantAction">
      ${pva.productVariant.colorOptionsValue}
      <c:forEach var="productImage" items="${pva.productVariant.productImages}" varStatus="ctr2">
        <tr>
          <td>
            <hk:productImage imageId="${productImage.id}" size="<%=EnumImageSize.MediumSize%>"/>
          </td>
          <td>
            <s:hidden name="productImages[${ctr2.index}]"/>
            <s:checkbox name="productImages[${ctr2.index}].hidden"/>
          </td>
          <td>
            <s:radio value="${productImage.id}" name="mainImageId"/>
          </td>
            <td>
              <s:radio value="${productImage.id}" name="mainProductImageId"/>
            </td>
        </tr>
      </c:forEach>
      <tr>
        <td>
          <s:hidden name="productVariant2" value="${pva.productVariant.id}"/>
          <s:submit name="editImageSettings" value="Submit"/>
        </td>
      </tr>
      </table>
    </s:form>
  </s:layout-component>

</s:layout-render>