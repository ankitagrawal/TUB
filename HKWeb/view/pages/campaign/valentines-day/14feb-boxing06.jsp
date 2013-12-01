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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/awkward-gift-corner.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2514' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2515' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT289' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT416' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT630' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT631' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT632' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT633' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT634' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT865' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT866' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT867' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT868' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT869' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT870' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT902' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT988' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT989' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT990' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT991' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT992' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT993' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT994' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT995' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT996' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT997' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT998' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT999' />




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing05.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing05.jsp">5</a>
            <span class="pages_link">6</span>
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

