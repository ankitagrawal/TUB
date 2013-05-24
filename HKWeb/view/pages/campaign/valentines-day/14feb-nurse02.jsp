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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/Hot-nurse.jpg"/>
<div class="cl"></div>

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
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLSUP009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLSUP010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEC008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEC009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB009' />
   



         <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-nurse.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-nurse.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-nurse03.jsp">3</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-nurse03.jsp">Next</a>
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

