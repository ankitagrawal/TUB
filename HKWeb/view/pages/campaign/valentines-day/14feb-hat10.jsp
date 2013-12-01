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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/hep-as-depp.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LASS6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR67' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRN001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRN002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRN003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN6' />
    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <span class="pages_link">10</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">Next</a>
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

