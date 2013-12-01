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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY786' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY787' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOTS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARM006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARMG06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARMTR16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AURITA1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AURITA2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR60' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR61' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY111' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY112' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY135' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY136' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY190' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY275' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY538' />
    
   




      <div class="cl"></div>
		<div class="pages">
             <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">Next</a>
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

