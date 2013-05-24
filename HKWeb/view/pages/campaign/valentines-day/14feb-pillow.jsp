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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/goes-with-pillow-fights.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1567' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1568' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT190' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2176' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2177' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2509' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2514' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2515' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT865' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT866' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT867' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT868' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT869' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT870' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT988' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AMRON002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AMRON003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AMRON004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AMRON013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BCKRST001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BCKRST002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BCKRST003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BCKRST004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BCKSUP001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED0039' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQUINX001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FSPL001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FSPL002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FSPL003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FSPL004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FSPL005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO009' />
 
    
    
   




      <div class="cl"></div>
		<div class="pages">
             <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow02.jsp">Next</a>
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

