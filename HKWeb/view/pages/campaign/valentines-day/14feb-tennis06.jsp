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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/courting-her-literally.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1251' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1252' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1253' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1254' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1255' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1256' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1257' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1258' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1259' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1296' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1297' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1298' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1299' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1300' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1301' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1466' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1467' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1468' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1469' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1470' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1471' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1472' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1473' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1474' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1475' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1476' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1477' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1478' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1479' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1480' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1481' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1482' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1483' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1484' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1485' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1486' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1487' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1488' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1489' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1490' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1491' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1492' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1493' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1494' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT176' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT177' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT178' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT179' />
 




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis05.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis05.jsp">5</a>
            <span class="pages_link">6</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis11.jsp">11</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis12.jsp">12</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis13.jsp">13</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis14.jsp">14</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis07.jsp">Next</a>
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

