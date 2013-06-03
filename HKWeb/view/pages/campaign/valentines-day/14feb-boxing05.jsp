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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2501' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2502' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2516' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1564' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1565' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1566' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1567' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1568' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1569' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1570' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1571' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1572' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1573' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1574' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1575' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1577' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1578' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1579' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1580' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1581' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1583' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1584' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1585' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1586' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1587' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1588' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1589' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1590' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1591' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1592' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1593' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1594' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1595' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1596' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1597' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1598' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1599' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1650' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT190' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2176' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2177' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2380' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2381' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2501' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2502' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2507' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2509' />
 
    



      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing04.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing04.jsp">4</a>
            <span class="pages_link">5</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing06.jsp">6</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing06.jsp">Next</a>
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

