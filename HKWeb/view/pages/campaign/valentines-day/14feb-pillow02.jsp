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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/goes-with-pillow-fights.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNESUP001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED062' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED068' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED072' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED074' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1194' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1195' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RC002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RC003' />

    
    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow.jsp">1</a>
             <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow03.jsp">Next</a>
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

