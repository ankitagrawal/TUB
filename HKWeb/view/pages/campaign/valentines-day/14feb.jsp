<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = WebContext.isSecure();
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
    
<a href="#girl" class="girl"></a>
<a href="#boy" class="boy"></a>
<div class="cl"></div>




<div class="container01">
<a name="girl" id="girl"></a>
  <span></span>
   <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-suspects.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Shampoo.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bulb.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Bulb.jpg" width="220" height="220" />  </a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-foot.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Stilletoes.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tape.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Measuring-tape.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Tennis-ball.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-balls.jsp"><img src="${pageContext.request.contextPath}/images/14feb/her-balls.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-nurse.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Nurse-cap.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-glasses.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Thick-Rimmed-Glasses.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-mascara.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Mascara.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing.jsp"><img src="${pageContext.request.contextPath}/images/14feb/her-gloves.jpg" width="220" height="220" /></a>
  <a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-razor.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Razor.jpg" width="220" height="220" /></a>
<div class="cl"></div>


 <div class="container02">
 <a name="boy" id="boy"></a>
 <span></span>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Cologne.jpg" width="220" height="220" /></a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector.jsp"><img src="${pageContext.request.contextPath}/images/14feb/BP-Monitor.jpg" width="220" height="220" /></a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Dumbells.jpg" width="220" height="220" /></a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Hat.jpg" width="220" height="220" /></a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Balls.jpg" width="220" height="220" /></a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Boxing-gloves.jpg" width="220" height="220" /></a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-mcsteamy.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Stethoscope.jpg" width="220" height="220" /></a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Bow.jpg" width="220" height="220" /></a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-clip.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Cloth-clip.jpg" width="220" height="220" /> </a>
<a href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper.jsp"><img src="${pageContext.request.contextPath}/images/14feb/Diaper.jpg" width="220" height="220" /></a>
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

