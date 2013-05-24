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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/clooney-set.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL36' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL38' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL40' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL61' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL62' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LTCM02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LTHB15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR48' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MSH004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MSH005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MSH010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NGE3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OLDSP1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OLDSP2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OLDSP3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ORIY02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ORIY07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ORIY08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG026' />
  
    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow04.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow04.jsp">4</a>
            <span class="pages_link">5</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow06.jsp">6</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow06.jsp">Next</a>
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

