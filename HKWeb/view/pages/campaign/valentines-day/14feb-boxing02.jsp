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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB419' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB466' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB807' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB808' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CL004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GNNE003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GNNE004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GNNE005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GNNE006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GNNE007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GNNE01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GNNE02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD72' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP031' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP038' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP039' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP040' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP041' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP065' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP066' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP096' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP097' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA022' />
   
   



      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing.jsp">1</a>
	        <span class="pages_link">2</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing06.jsp">6</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing03.jsp">Next</a>
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

