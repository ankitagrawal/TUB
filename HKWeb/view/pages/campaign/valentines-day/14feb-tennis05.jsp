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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT285' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3103' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3104' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3105' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT494' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT495' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT496' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT497' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT498' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT499' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT500' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT501' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT502' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT503' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT504' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT505' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT506' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT507' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT508' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT509' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT510' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT511' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT515' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT635' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT636' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT637' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT660' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT661' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT663' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT681' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT932' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT933' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT934' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT935' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT936' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT937' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT938' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT939' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT940' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT941' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1250' />
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis04.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis04.jsp">4</a>
            <span class="pages_link">5</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis11.jsp">11</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis12.jsp">12</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis13.jsp">13</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis14.jsp">14</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis06.jsp">Next</a>
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

