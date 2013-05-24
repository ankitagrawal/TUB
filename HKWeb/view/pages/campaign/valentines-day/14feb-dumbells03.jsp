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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/man-up-kit.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT068' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT069' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT070' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT071' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT072' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT073' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT074' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT075' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT076' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT077' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT078' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT079' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT080' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT081' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT082' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT083' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT084' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT085' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT086' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT087' />
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
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT099' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT100' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT101' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT102' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT103' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT104' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT105' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT107' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT108' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT109' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT111' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT112' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT113' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT114' />
   
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells02.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells02.jsp">2</a>
            <span class="pages_link">3</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells04.jsp">Next</a>
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

