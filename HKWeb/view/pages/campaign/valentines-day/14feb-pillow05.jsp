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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/goes-with-pillow-fights.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT060' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT061' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT062' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT063' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT065' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT073' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT088' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT089' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT090' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT091' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT092' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT093' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT094' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT095' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT096' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT097' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT098' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT116' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT117' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT118' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT214' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT224' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT225' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT385' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT387' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT388' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR016' />

    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow04.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow04.jsp">4</a>
            <span class="pages_link">5</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow06.jsp">Next</a>
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

