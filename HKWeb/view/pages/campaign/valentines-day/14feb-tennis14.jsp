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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/courting-her-literally.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2701' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2702' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2703' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2704' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2705' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2706' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2707' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2709' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2710' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3054' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3055' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3057' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3058' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3059' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3060' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT694' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT695' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT696' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT697' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT698' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT699' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT710' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT711' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT712' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT713' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT714' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT715' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT716' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT717' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT718' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT719' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT720' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT722' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT723' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT724' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT725' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT726' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT727' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT728' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT729' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT730' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT731' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT732' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT733' />
    




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis13.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis08.jsp">8</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis09.jsp">9</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis10.jsp">10</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis11.jsp">11</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis12.jsp">12</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis13.jsp">13</a>
            <span class="pages_link">14</span>

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

