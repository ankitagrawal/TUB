<%--
  Created by IntelliJ IDEA.
  User: Marut
  Date: 1/8/13
  Time: 4:23 PM
  To change this template use File | Settings | File Templates.
--%>
<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@ page import="com.hk.util.ImageManager" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.VizuryCatalogAction" var="vizuryBean"/>
<products>
    <c:forEach items="${vizuryBean.products}" var="product">
        <c:if test="${fn:length(product.productVariants) > 0 && product.minimumMRPProducVariant.hkPrice != null && product.maximumMRPProducVariant.hkPrice != null}">
            <product id="${product.id}">
                <title><![CDATA[${hk:escapeXML(product.name)}]]></title>
                <link>http://www.healthkart.com/product/${product.slug}/${product.id}</link>
                <price>"${product.minimumMRPProducVariant.hkPrice}"</price>
                <description>${hk:escapeXML(product.description)}</description>
                <c:when test="${product.mainImageId != null}">
                <c:set var="imageId" value="${product.mainImageId}"/>
                <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
                <image_link><=%HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId,false)%></image_link>
                </c:when>
                <%--<images>
                    <c:choose>
                        <c:when test="${product.mainImageId != null}">
                            <c:set var="imageId" value="${product.mainImageId}"/>
                            <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
                            <image
                                    srcSmall="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId,false)%>"
                                    srcMedium="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, imageId,false)%>"
                                    srcLarge="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.LargeSize, imageId,false)%>"
                                    />
                        </c:when>
                        <c:otherwise>
                            <image
                                    srcSmall="<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${product.id}.jpg"
                                    srcMedium="<hk:vhostImage/>/images/ProductImages/ProductImagesOriginal/${product.id}.jpg"
                                    srcLarge="<hk:vhostImage/>/images/ProductImages/ProductImagesOriginal/${product.id}.jpg"
                                    />
                        </c:otherwise>
                    </c:choose>
                </images>--%>
            </product>
        </c:if>
    </c:forEach>
</products>

