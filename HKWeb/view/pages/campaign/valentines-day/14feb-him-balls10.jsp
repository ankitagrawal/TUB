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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/give-him-some-balls.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1311' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1312' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1313' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1314' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1315' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1316' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1317' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1318' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1333' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1334' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1397' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1399' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1400' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1401' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1452' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1454' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT148' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT150' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT155' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT156' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT157' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT158' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT159' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT161' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT162' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT163' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT164' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1717' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1718' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT191' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT194' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT196' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT197' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2289' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2499' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2500' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT258' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT262' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT263' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3039' />
  
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls09.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls08.jsp">8</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls9.jsp">9</a>
            <span class="pages_link">10</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls11.jsp">Next</a>
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

