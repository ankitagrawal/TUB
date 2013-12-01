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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/insanity-wolf.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SHRCVR02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEDIC34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEDIC39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPTER2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZVR29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DEF01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-PEP002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNOCK001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2635' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS041' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2616' />
    




    
        <div class="cl"></div>
		<div class="pages">
	        <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-razor.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-razor.jsp">1</a>
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

