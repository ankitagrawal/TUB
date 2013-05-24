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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT039' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT044' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT046' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT047' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT048' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT049' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT050' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT051' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT052' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT053' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT054' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT055' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT057' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT058' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT059' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT060' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT061' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT062' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT063' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT065' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT066' />
   
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells03.jsp">Next</a>
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

