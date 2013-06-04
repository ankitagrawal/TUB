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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/clooney-set.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY788' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY789' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY790' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRME01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRME03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRME04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRME05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EDW03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ENBLZR01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ENBLZR02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ENBLZR03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN58' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN59' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN61' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FDW14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FDW15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT17' />
  



      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow06.jsp">6</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow03.jsp">Next</a>
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

