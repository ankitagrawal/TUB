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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/tape-banner.jpg"/>
<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT339' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT342' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT371' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT421' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT433' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT441' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT469' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT519' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT887' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT928' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT949' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT982' />
    




        <div class="cl"></div>
		<div class="pages">
	        <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tape.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tape.jsp">1</a>
            <span class="pages_link">2</span>
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

