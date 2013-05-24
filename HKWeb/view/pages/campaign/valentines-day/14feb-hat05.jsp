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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/hep-as-depp.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC090' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC092' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC093' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC094' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC095' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC096' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC097' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC101' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC102' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC103' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC104' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC109' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC112' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC125' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC126' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC131' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC134' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC137' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC138' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FACES3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FACES4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FACNP1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FARA5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JRDNA12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD25' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD35' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD36' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD38' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD40' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD41' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD42' />
   
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <span class="pages_link">5</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">Next</a>
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

