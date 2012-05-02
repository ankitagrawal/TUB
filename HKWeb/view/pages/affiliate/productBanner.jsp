<html>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.admin.EditProductAttributesAction" var="pa" event="pre"/>
<head>
  <title>HealthKart.com Affiliate Program: Refer Customers and Earn Money</title>
  <meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type"/>
  <jsp:include page="/includes/_style.jsp"/>
</head>
<body>
<div class="round-cont" style="width:150px; margin-top: 20px;">
  <div class="hk_banner" style="margin-left:20px">
    <div>
      <a href="http://www.healthkart.com/product/${pa.product.slug}/${pa.product.id}?affid=${pa.affiliate.code}" title="${pa.product.name}" target="_parent">
        <c:choose>
          <c:when test="${pa.product.mainImageId != null}">
            <hk:productImage imageId="${pa.product.mainImageId}" size="<%=EnumImageSize.SmallSize%>"/>
          </c:when>
          <c:otherwise>
            <img class='prod320' src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${pa.product.id}.jpg" alt="${pa.product.slug}" title="${pa.product.name}">
          </c:otherwise>
        </c:choose>
      </a>
    </div>
    <div>
      <a href="http://www.healthkart.com/product/${pa.product.slug}/${pa.product.id}?affid=${pa.affiliate.code}" title="${pa.product.name}" target="_parent">${pa.product.name}</a>
      <a href="http://www.healthkart.com/product/${pa.product.slug}/${pa.product.id}?affid=${pa.affiliate.code}" title="${pa.product.name}" target="_parent">
        <div class='prices' style="font-size: 12px;margin-left:10px;margin-bottom:5px">
          <div class='cut' style="font-size: 12px;">
                      <span class='num' style="font-size: 12px;">
                        Rs <fmt:formatNumber value="${pa.product.productVariants[0].markedPrice}" maxFractionDigits="0"/>
                      </span>
          </div>
          <div class='hk' style="font-size: 12px;margin-top:0px">
                      <span class='num' style="font-size: 12px;;text-decoration:none">
                        Rs <fmt:formatNumber value="${pa.product.productVariants[0].hkPrice}" maxFractionDigits="0"/>
                      </span>
          </div>
          <div class="special green" style="font-size: 12px;margin-top:0px">
            you save
                                      <span style="font-weight: bold;;text-decoration:none"><fmt:formatNumber
                                          value="${pa.product.productVariants[0].discountPercent*100}"
                                          maxFractionDigits="0"/>%</span>
          </div>
        </div>
      </a>
      <a href="http://www.healthkart.com/product/${pa.product.slug}/${pa.product.id}?affid=${pa.affiliate.code}" target="_parent"><img src="${pageContext.request.contextPath}/images/icons/buy_button_1.png" alt="Buy from HealthKart"/></a>
    </div>
  </div>
</div>
</body>
</html>