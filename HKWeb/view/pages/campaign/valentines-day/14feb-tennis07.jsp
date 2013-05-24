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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/courting-her-literally.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT180' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT181' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT182' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT183' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT184' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT186' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT187' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT188' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT189' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT198' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT199' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT200' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT201' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT202' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT203' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT204' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT205' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2104' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2105' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2107' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2108' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2109' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2111' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2112' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2113' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2114' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2115' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2116' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2117' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2118' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2119' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2121' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2122' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2123' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2124' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2125' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2126' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2127' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2128' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2129' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2130' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2131' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2132' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2133' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2134' />
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis06.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis06.jsp">6</a>
            <span class="pages_link">7</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis11.jsp">11</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis12.jsp">12</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis13.jsp">13</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis14.jsp">14</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis08.jsp">Next</a>
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

