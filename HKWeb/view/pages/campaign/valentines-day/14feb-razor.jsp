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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-PCW001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS041' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AMR009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PPS010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PW001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PW002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PW003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PW005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PW011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PW013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PW014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PW017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARMTR3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRN2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY256' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY775' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY777' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY778' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY779' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY780' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY781' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY782' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLN003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OSSG02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OSSG03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI06' />
    




    
         <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-razor02.jsp">2</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-razor02.jsp">Next</a>
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

