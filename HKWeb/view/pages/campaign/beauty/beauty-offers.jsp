<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Beauty Offers">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/bty-offers.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Beauty Offers</span>

		<h1 class="title">Beauty Offers</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Beauty Offers</s:layout-component>
<s:layout-component name="metaKeywords">Beauty Offers</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->
 <div id="wrapper">
     <a class="banners" href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris"><img src="${pageContext.request.contextPath}/images/bty-offers/bty01.jpg"/></a>
     <a class="banners" href="http://www.healthkart.com/brand/beauty/maybelline"><img src="${pageContext.request.contextPath}/images/bty-offers/bty2.jpg"/></a>
     <a class="banners04" href="http://www.healthkart.com/brand/beauty/Manufaktura+Home+Spa"><img src="${pageContext.request.contextPath}/images/bty-offers/bty3.jpg"/></a>

     <div class="cl"></div>
     <div class="offers">

     <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE  Loreal color riche vernis nail paint worth Rs 225/- with purchase of loreal skin and make up worth  Rs 1200/-</a></p>

     <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE  Loreal superliner worth Rs 625/- with purchase of loreal skin and make up worth Rs 1500/-</a></p>
         <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE loreal infallible lipstick worth Rs 990/- with purchase of loreal skin and make up worth Rs 1800/-</a></p>
         <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE manicure kit with purchase of loreal skin and make up worth Rs 2200/-</a></p>
         <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE loreal glamshine 6h lipgloss worth Rs 875/- with purchase of loreal skin and make up worth Rs 3000/-</a></p>
         <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE trousseau box with purchase of loreal skin  and make up worth Rs 4500/-</a></p>
         <p><a href="http://www.healthkart.com/brand/beauty/maybelline">FREE maybelline watershine liquid diamonds lip gloss worth Rs 210/- with purchase of maybelline make up worth Rs 1200/-</a></p>
         <p><a href="http://www.healthkart.com/product/maybelline-color-senstational-lip-color/MYBLN5">FREE lipstick holder with purchase of maybelline color sensational lip color</a></p>


          <!---
         <p><a href="http://www.healthkart.com/product/auravedic-ritual-skin-lightening-mask/ARVDC33">FREE auravedic skin lighting mask with purchase of  Beauty products worth Rs 1000/-</a></p>
          --->


     </div>

     
    

 







</div>
<!--wrapper close-->


<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

</s:layout-render>

