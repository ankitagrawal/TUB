<%--
  Created by IntelliJ IDEA.
  User: Marut
  Date: 1/8/13
  Time: 4:23 PM
  To change this template use File | Settings | File Templates.
--%>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@ page import="com.hk.util.ImageManager" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.GoogleCatalogAction" var="googleBean"/>
<products>
    <c:forEach items="${googleBean.products}" var="product">
        <c:if test="${fn:length(product.productVariants) > 0 && product.minimumMRPProducVariant.hkPrice != null && product.maximumMRPProducVariant.hkPrice != null}">
            <product id="${product.id}">
                <title><![CDATA[${hk:escapeXML(product.name)}]]></title>
                <link>http://www.healthkart.com/product/${product.slug}/${product.id}</link>
                <price>"${product.minimumMRPProducVariant.hkPrice}"</price>
                <description>${hk:escapeXML(product.description)}</description>
                <condition>new</condition>
                <image_link>
                    <c:if test="${product.mainImageId != null}">
                        <c:set var="imageId" value="${product.mainImageId}"/>
                        <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
                        <%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId,false)%>
                    </c:if>
                </image_link>
            </product>
        </c:if>
    </c:forEach>
</products>

