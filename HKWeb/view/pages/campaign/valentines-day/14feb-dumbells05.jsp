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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1803' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1804' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1805' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1883' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1884' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2050' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2051' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2052' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2053' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2054' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2055' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2061' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2066' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT213' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT214' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT215' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT216' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2163' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2164' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2165' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2166' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2167' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2168' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2169' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT217' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2170' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2171' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2172' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2174' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT218' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT219' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT220' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT221' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT222' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT223' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT224' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT225' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT226' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT227' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT228' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2282' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2283' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2284' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT229' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2297' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT230' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2306' />
  
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells04.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells04.jsp">4</a>
            <span class="pages_link">5</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells06.jsp">Next</a>
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

