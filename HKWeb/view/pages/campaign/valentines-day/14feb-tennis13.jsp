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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1462' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1463' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1464' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1465' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT165' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT167' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT168' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT169' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT170' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1700' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT171' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT173' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT174' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1758' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1759' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1760' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1773' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1774' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1775' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1776' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT207' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2144' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2145' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2310' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2311' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2312' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2314' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2316' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2317' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2318' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2319' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2320' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2322' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2323' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2324' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2327' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2329' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2330' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2331' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2332' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2369' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT239' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT242' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2511' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2512' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2513' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2700' />
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis12.jsp">Previous</a>
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
            <span class="pages_link">13</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis14.jsp">14</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-tennis14.jsp">Next</a>
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

