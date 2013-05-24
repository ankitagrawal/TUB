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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1427' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1961' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2072' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2113' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2114' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB398' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1966' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2083' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB346' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB374' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB403' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB404' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB162' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1960' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB365' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB483' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1449' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1451' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1452' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1820' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1922' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1923' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1924' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1925' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1926' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1927' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1929' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1930' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1931' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1932' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1933' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1934' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1935' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1936' />






      <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper07.jsp">7</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper02.jsp">Next</a>
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

