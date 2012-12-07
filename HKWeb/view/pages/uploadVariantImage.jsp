<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductVariantDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" var="imageBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.core.catalog.image.UploadImageAction">
      File to Upload
      <s:file name="fileBean" size="30"/>
      <s:select name="productVariant" >
       <c:forEach items="${imageBean.productVariant.product.productVariants}" var="variant" varStatus="ctr">
         <s:option value="${variant}">${variant.id} - 
	         <c:forEach items="${variant.productOptions}"
	                    var="productOption">
		         <c:if test="${hk:showOptionOnUI(productOption.name)}">
			         ${productOption.value};
		         </c:if>
	         </c:forEach>
         </s:option>
       </c:forEach>
      </s:select>
      <s:submit name="uploadProductVariantImage" value="Upload Image"/>
      <s:hidden name="productVariant" value="${imageBean.productVariant.id}"/>
    </s:form>
  </s:layout-component>

</s:layout-render>