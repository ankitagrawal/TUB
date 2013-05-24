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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB031' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB042' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NISCO035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NISCO037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP048' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP049' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VITAL001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VITAL003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VITAL004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPT002' />  
  
    




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector02.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector02.jsp">2</a>
            <span class="pages_link">3</span>
           

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

