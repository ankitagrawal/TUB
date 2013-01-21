<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@ page import="com.hk.util.ImageManager" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<rss version="2.0" xmlns:g="http://base.google.com/ns/1.0">
<s:useActionBean beanclass="com.hk.web.action.core.catalog.GoogleCatalogAction" var="googleBean"/>
                <channel>
                <title>Healthkart Google Product Feed</title>
                <link>http://www.healthkart.com</link>
                <description>Healthkart Online Health Store</description>
                <c:forEach items="${googleBean.products}" var="product">
                    <c:if test="${fn:length(product.productVariants) > 0
                            && product.mainImageId != null&& product.minimumMRPProducVariant.hkPrice != null && product.maximumMRPProducVariant.hkPrice != null}">
                        <item>
                        <title><![CDATA[${hk:escapeXML(product.name)}]]></title>
                        <link>http://www.healthkart.com/product/${product.slug}/${product.id}</link>
                        <description>![CDATA[${hk:stripHtml(product.description)}]]</description>
                        <g:id>${product.id}</g:id>
                        <g:condition>New</g:condition>
                        <g:price>${product.minimumMRPProducVariant.hkPrice} INR</g:price>
                        <%--<c:if test="${product.brand != null}">
                            <g:brand><![CDATA[${hk:escapeXML(product.brand)}]]</g:brand>
                        </c:if>
--%>
                        <g:image_link>
                            <c:set var="imageId" value="${product.mainImageId}"/>
                            <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
                            <%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId,false)%>
                        </g:image_link>
                        </item>
                    </c:if>
                </c:forEach>
                </channel>
    </rss>