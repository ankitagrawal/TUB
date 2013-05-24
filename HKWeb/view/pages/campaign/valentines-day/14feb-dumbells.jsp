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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU084' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU085' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU086' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU088' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU089' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU093' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU094' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU096' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-SPT001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-SPT002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC113' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC114' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC115' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC116' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC117' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC118' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC121' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC123' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC124' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1292' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT448' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RO002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RU005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT008' />
    


      <div class="cl"></div>
		<div class="pages">
             <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-dumbells02.jsp">Next</a>
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

