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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/mamas-boy.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2205' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2240' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2243' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2244' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2245' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2270' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2276' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2313' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB244' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB245' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB246' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB248' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB249' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB251' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB252' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB253' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB254' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB283' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB301' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB302' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB303' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB389' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB390' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB412' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB413' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB415' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB418' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB421' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB422' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB474' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB513' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB521' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB529' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB550' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB879' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB883' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB884' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB885' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB887' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB888' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB905' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB033' />





      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper05.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper05.jsp">5</a>
            <span class="pages_link">6</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper07.jsp">7</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-diaper07.jsp">Next</a>
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

