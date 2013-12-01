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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/awkward-gift-corner.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DET001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DET002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='IMPL08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIF001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIF002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIF003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITTMN001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LUX004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHL004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PIENSPA2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP042' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP059' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP094' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP095' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1430' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1431' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1432' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1433' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1434' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1435' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1436' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1437' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1438' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1439' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1440' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1441' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1442' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1443' />
  



      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing02.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing02.jsp">2</a>
            <span class="pages_link">3</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing06.jsp">6</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing04.jsp">Next</a>
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

