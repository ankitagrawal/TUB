<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
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



<a href="http://www.healthkart.com"><img class="logo" src="${pageContext.request.contextPath}/images/ny2013/logo.jpg" />  </a>





<img src="${pageContext.request.contextPath}/images/ny2013/homepage.jpg" usemap="#Map" border="0" />
<map name="Map" id="Map">
  <area shape="rect" coords="0,0,963,369" href="${pageContext.request.contextPath}/pages/ny2013.jsp" />
  <area shape="rect" coords="1,369,960,737" href="${pageContext.request.contextPath}/pages/ny-brain.jsp" />
  <area shape="rect" coords="1,738,957,1104" href="${pageContext.request.contextPath}/pages/ny-health.jsp" />
  <area shape="rect" coords="3,1107,956,1470" href="${pageContext.request.contextPath}/pages/ny-fitness.jsp" />
  <area shape="rect" coords="3,1474,959,1840" href="${pageContext.request.contextPath}/pages/ny-parenting.jsp" />
  <area shape="rect" coords="4,1844,956,2210" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp" />
  <area shape="rect" coords="3,2214,961,2575" href="${pageContext.request.contextPath}/pages/ny-living.jsp" />
</map>





<div class="footer-ny">
<p>© 2012 healthkart.com</p>
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

