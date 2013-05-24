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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1937' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1938' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1939' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1940' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1941' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1942' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1943' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1947' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1949' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1950' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1951' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1952' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1954' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2235' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2237' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2238' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2239' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2307' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB457' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0331' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0336' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0338' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0368' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0370' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0371' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0372' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0373' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0379' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0380' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0381' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1128' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1132' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1139' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1164' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1165' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1167' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1168' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1169' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1170' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1172' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1178' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1190' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1191' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1192' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1221' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1259' />






      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper07.jsp">7</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper03.jsp">Next</a>
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

