<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
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
 <jsp:include page="../includes/_menu14feb.jsp"/>
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/cologne-banner.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS25' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRUT01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRUT03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRUT04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ046' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ047' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY151' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY152' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY170' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY174' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY213' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY229' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY231' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY235' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY236' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY245' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY301' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY304' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY305' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY408' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY786' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY787' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRME03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRME05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM003' />
    
    




      <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/14feb-cologne02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/14feb-cologne03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/14feb-cologne04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/14feb-cologne05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/14feb-cologne06.jsp">6</a>
 
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/14feb-cologne02.jsp">Next</a>
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

