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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/man-up-kit.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT115' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT116' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT117' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT118' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1260' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1261' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1262' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1263' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1264' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1303' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1304' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1305' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1306' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1307' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1402' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1403' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1404' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1405' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1407' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1408' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1409' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1430' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1431' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1432' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT144' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT145' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1500' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1516' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1560' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1561' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1562' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1563' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1572' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1573' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1577' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1578' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1600' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1650' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1701' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1702' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1723' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1724' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1725' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1726' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1727' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1728' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1801' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1802' />

   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells03.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells03.jsp">3</a>
            <span class="pages_link">4</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells05.jsp">Next</a>
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

