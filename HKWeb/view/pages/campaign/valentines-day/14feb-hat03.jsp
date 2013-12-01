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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEDIC35' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEDIC36' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VNCP20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE33' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AUD018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR48' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR62' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR63' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR64' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR66' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR79' />
    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <span class="pages_link">3</span>            
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">Next</a>
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

