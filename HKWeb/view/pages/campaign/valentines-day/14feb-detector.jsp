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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/the-lie-detector.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AS009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AS010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AS011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP055' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP057' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP058' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP059' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP060' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP061' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP062' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP063' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP065' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP066' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP068' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP069' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP070' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP071' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP072' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIT001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIT002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIT003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIT004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITT005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITT007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITTAC025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NISCO017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RSTR010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RSTR011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RSTR012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='STETH001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VKR001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VKR002' />

    
    




      <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector03.jsp">3</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector02.jsp">Next</a>
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

