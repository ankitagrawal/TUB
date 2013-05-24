<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
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

    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/skin-glow.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNDAR06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNDAR10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNDAR01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR36' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG30' />
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
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CZECHSPA1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CZECHSPA3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA41' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR103' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR145' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR129' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR104' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR117' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR118' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR119' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG25' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL57' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MKRI07' />
   




    

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

