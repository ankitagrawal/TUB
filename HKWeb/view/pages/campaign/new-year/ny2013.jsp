<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Happy New Year 2013">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/ny2013.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Cyber
		Monday</span>

		<h1 class="title">Happy New Year 2013</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Happy New Year 2013</s:layout-component>
<s:layout-component name="metaKeywords">Happy New Year 2013</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->
<jsp:include page="../includes/_menuNY2013.jsp"/>

<div id="wrapper">

<img src="${pageContext.request.contextPath}/images/ny2013/main_banner.jpg" />
    <div class="cl"></div>
<a href="${pageContext.request.contextPath}/pages/ny-brain.jsp"><img src="${pageContext.request.contextPath}/images/ny2013/brain.jpg" /></a>
    <div class="cl"></div>
<a href="${pageContext.request.contextPath}/pages/ny-health.jsp"><img src="${pageContext.request.contextPath}/images/ny2013/health.jpg" /></a>
    <div class="cl"></div>
<a href="${pageContext.request.contextPath}/pages/ny-fitness.jsp"><img src="${pageContext.request.contextPath}/images/ny2013/fitness.jpg" /></a>
    <div class="cl"></div>
<a href="${pageContext.request.contextPath}/pages/ny-parenting.jsp"><img src="${pageContext.request.contextPath}/images/ny2013/parenting.jpg" /></a>
    <div class="cl"></div>
<a href="${pageContext.request.contextPath}/pages/ny-grooming.jsp"><img src="${pageContext.request.contextPath}/images/ny2013/grooming.jpg" /></a>
    <div class="cl"></div>
<a href="${pageContext.request.contextPath}/pages/ny-living.jsp"><img src="${pageContext.request.contextPath}/images/ny2013/living.jpg" /></a>
<div class="cl"></div>





<div class="footer-ny">
<p>© 2013 healthkart.com</p>
<a href="https://twitter.com/healthkart"><img src="${pageContext.request.contextPath}/images/ny2013/twitter-img.jpg" /></a>
<a href="https://www.facebook.com/healthkart"><img src="${pageContext.request.contextPath}/images/ny2013/fb-img.jpg" /></a>
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

