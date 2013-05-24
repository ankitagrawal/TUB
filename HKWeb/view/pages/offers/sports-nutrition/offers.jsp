<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
	boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Nutrition Offers">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/nut-offers.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Nutrition Offers</span>

		<h1 class="title">Nutrition Offers</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Nutrition Offers</s:layout-component>
<s:layout-component name="metaKeywords">Nutrition Offers</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->
    <div id="wrapper">


        <div class="row01"> <a href="https://www.healthkart.com/brand/sports-nutrition/twinlab" target="_blank"><img src="${pageContext.request.contextPath}/images/nut-offers/banner6.jpg" alt="Up to 45% off on Twinlab Nutrition" /></a>
            <div class="space"></div>
            <a href="https://www.healthkart.com/brand/sports-nutrition/allmax" target="_blank"><img src="${pageContext.request.contextPath}/images/nut-offers/banner4.jpg" alt="Up to 30% off on Allmax Nutrition" /></a>
        </div>
        <div class="space"></div>
        <div class="cl"></div>

        <div class="row02"> <a href="https://www.healthkart.com/brand/sports-nutrition/vitamin+shoppe+bodytech" target="_blank"><img src="${pageContext.request.contextPath}/images/nut-offers/banner2.jpg" alt="Up to 30% off on Vitamine Shoppe BodyTech" /></a>
            <div class="space"></div>
            <a href="https://www.healthkart.com/brand/sports-nutrition/muscletech+six+star" target="_blank"><img src="${pageContext.request.contextPath}/images/nut-offers/banner3.jpg" alt="Up to 30% off on Six Star Pro Nutrition" /></a>
            <div class="space"></div>
            <div class="cl"></div>
            <a href="https://www.healthkart.com/brand/sports-nutrition/vitamin+shoppe+true+athlete" target="_blank"><img src="${pageContext.request.contextPath}/images/nut-offers/banner5.jpg" alt="Up to 30% off on True Athlete Nutrition" /></a>
        </div>
        <div class="space"></div>
        <a href="https://www.healthkart.com/brand/sports-nutrition/weider" target="_blank"><img src="${pageContext.request.contextPath}/images/nut-offers/banner1.jpg" alt="Up to 10% off on Weider Nutrition" /></a>


    </div>
<!--wrapper close-->


<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

</s:layout-render>

