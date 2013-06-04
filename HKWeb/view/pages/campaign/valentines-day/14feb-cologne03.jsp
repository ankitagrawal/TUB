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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/cologne-banner.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='H2O015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='IMPL03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM29' />
   
    




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne02.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne02.jsp">2</a>
            <span class="pages_link">3</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne06.jsp">6</a>
     
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne04.jsp">Next</a>
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

