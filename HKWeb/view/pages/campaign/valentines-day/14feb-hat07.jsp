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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC33' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PANAC34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SHNZ16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SRTWR02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VIVI02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT35' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT36' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT38' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT40' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT41' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT46' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT48' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPTER1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE314' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE337' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE338' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE339' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ044' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY153' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY163' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY164' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY409' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT76' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZVR13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZVR28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR44' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR45' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR51' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS12' />
 
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <span class="pages_link">7</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">Next</a>
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

