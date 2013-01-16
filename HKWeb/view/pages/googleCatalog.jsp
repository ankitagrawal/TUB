<%--
  Created by IntelliJ IDEA.
  User: Marut
  Date: 1/8/13
  Time: 4:23 PM
  To change this template use File | Settings | File Templates.
--%>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@ page import="com.hk.util.ImageManager" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<rss version="2.0"
     xmlns:g="http://base.google.com/ns/1.0">

<s:useActionBean beanclass="com.hk.web.action.core.catalog.GoogleCatalogAction" var="googleBean"/>
    <c:forEach items="${googleBean.products}" var="product">
        <c:if test="${fn:length(product.productVariants) > 0 && product.minimumMRPProducVariant.hkPrice != null && product.maximumMRPProducVariant.hkPrice != null}">

            <channel>
                <title>Healthkart Google Data Feed</title>
                <link>http://www.example.com</link>
                <description>Product Information</description>
                <item>
                    <g:id>${product.id}</g:id>
                    <title><![CDATA[${hk:escapeXML(product.name)}]]></title>
                    <link>http://www.healthkart.com/product/${product.slug}/${product.id}</link>
                    <description>${hk:escapeXML(product.description)}</description>
                    <g:image_link>
                        <c:if test="${product.mainImageId != null}">
                        <c:set var="imageId" value="${product.mainImageId}"/>
                        <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
                        <%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId,false)%>
                        </c:if>
                    </g:image_link>
                    <g:price>${product.minimumMRPProducVariant.hkPrice} INR</g:price>
                    <g:condition>new</g:condition>
                </item>
            </channel>

        </c:if>
    </c:forEach>
</rss>