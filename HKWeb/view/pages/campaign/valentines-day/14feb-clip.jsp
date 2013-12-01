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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/smells-like-anything.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRUT01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY174' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY229' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY301' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRME01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EDW03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ENBLZR01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ENBLZR02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ENBLZR03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='IMPL07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='IMPL09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM40' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM41' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM42' />
  
    




    
         <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-clip02.jsp">2</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-clip02.jsp">Next</a>
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

