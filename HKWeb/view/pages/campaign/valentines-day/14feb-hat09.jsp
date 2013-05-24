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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/hep-as-depp.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO33' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC031' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC038' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC083' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC085' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC086' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT17' />
  
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <span class="pages_link">9</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">Next</a>
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

