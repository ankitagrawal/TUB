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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/mamas-boy.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1260' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1261' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1268' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1269' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1270' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1275' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1276' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1284' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1286' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1312' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1313' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1314' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1402' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB147' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1510' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1511' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1512' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1513' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1514' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1515' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1516' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1517' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1518' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1519' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1520' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1548' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1549' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1551' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1617' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1619' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1620' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1621' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1641' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1643' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1647' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1648' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1649' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1650' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1651' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1652' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1653' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1655' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1657' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1749' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1751' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1752' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1756' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1758' />






      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper02.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper02.jsp">2</a>
            <span class="pages_link">3</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper07.jsp">7</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper04.jsp">Next</a>
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

