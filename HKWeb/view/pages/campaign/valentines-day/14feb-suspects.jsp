<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = SslUtil.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Happy Valentines Day">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/14feb.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Happy Valentines Day</span>

		<h1 class="title">Happy Valentines Day</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Happy Valentines Day</s:layout-component>
<s:layout-component name="metaKeywords">Happy Valentines Day</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

<div id="wrapper">

    <jsp:include page="/includes/_menu14feb.jsp"/>
    

    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/suspects.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LARM1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALV1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ENBLZR03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNDAR06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNDAR10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNDAR01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNDAR04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRG009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR36' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR45' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR48' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR105' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR97' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR98' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR108' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR107' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRSH02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CZECHSPA1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CZECHSPA3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA41' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR103' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRG056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY456' />




     <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-suspects-02.jsp">2</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-suspects-02.jsp">Next</a>
         </div>

    <div class="cl"></div>
<div class="footer-ny">
<p>© 2013 healthkart.com</p>
<a href="https://twitter.com/healthkart"><img src="${pageContext.request.contextPath}/images/14feb/twitter-img.jpg" /></a>
<a href="https://www.facebook.com/healthkart"><img src="${pageContext.request.contextPath}/images/14feb/fb-img.jpg" /></a>

</div>

 </div>
<!--wrapper close-->


<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

<s:layout-component name="menu">
</s:layout-component>

</s:layout-render>

