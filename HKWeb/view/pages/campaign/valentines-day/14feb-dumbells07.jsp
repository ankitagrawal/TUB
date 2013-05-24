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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/man-up-kit.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2717' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2718' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2719' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2720' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2721' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT291' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT297' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT299' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT300' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3000' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT302' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3052' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3053' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3065' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3066' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3121' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3122' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT374' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT375' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT385' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT386' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT387' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT388' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT389' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT390' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT391' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT392' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT393' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT394' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT395' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT396' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT397' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT398' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT399' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT400' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT401' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT402' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT403' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT404' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT405' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT406' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT407' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT408' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT409' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT410' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT411' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT412' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT413' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT414' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT415' />
   
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells06.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells07.jsp">6</a>
            <span class="pages_link">7</span>

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

