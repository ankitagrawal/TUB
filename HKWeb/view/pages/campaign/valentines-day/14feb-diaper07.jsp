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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/mamas-boy.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB038' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB039' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB040' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB041' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB042' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB149' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1868' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1869' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1879' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2233' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2304' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB379' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0327' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0352' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0353' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER044' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER046' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER047' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1164' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1182' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1189' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1190' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1191' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1192' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1284' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1315' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT426' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT527' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT659' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT684' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT794' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT822' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT940' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT970' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT971' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT993' />






      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper06.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper06.jsp">6</a>
            <span class="pages_link">7</span>
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

