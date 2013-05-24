<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
	boolean isSecure = SslUtil.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Happy New Year 2013">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/ny2013.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Cyber
		Monday</span>

		<h1 class="title">Happy New Year 2013</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Happy New Year 2013</s:layout-component>
<s:layout-component name="metaKeywords">Happy New Year 2013</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->
<jsp:include page="../includes/_menuNY2013.jsp"/>

<div id="wrapper">



<div class="logo">
<a href="http://www.healthkart.com"><img src="${pageContext.request.contextPath}/images/ny2013/logo.jpg" />  </a>
</div>





<img src="${pageContext.request.contextPath}/images/ny2013/grooming01.jpg" />

<div class="cl"></div>
		<div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">2</a>
            <span class="pages_link">3</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming04.jsp">4</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming04.jsp">Next →</a>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/long-lasting-makeup.png"/>
    <h2 class="txt01">Almost Permanent Makeup</h2>
<p>Ward off chapped skin with the ultra-nourishing goodness of sublimely exotic skin care essentials. Wear a blush this winter while your skin breaths in the freshness of the new season setting in.</p>
</div>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX33' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY04' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MYBLN31' />
	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRBR25' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRBR30' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL16' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY527' />
		
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL20' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC07' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY792' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX17' />
	

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY203' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY351' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY354' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY355' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY629' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY680' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FARA5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FESTGLAM1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GNNE02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LAKABS14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LAKABS7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LAKABS9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LKM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LTHB12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MYBLN20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MYBLN30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SHNZ18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SRTWR02' />   
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/get-hair.png"/>
    <h2 class="txt01">Grow Hair</h2>
<p>They say the more hair you lose, the more head you get. Jokes apart, if you are losing hair and are also losing some good sleep over it, fear not! If it is just stress or hormonal changes then we can reverse it too. We have supplements, shampoos and tonics that will help you with this. If these don’t work, then may we suggest Baba Ramdev's rubbing left and right hand nails exercise!</p>

</div>

<div class="cl"></div>
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY278' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY347' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY574' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LASS1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LTDOFF20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1129' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT667' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ON038' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ON039' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ORIY18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP091' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TPIC01' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY285' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VICHY5' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ORIY17' />
	
	
	
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/pamper-yourself.png"/>
    <h2 class="txt01">Pamper Yourself</h2>
<p>You do it all, manage the house, cuddle the kids, soothe their skinned knees and most importantly forget about yourself. No more. Mark it on the calendar if you have to, just like any other appointment and get away for a spa weekend. We guarantee you will discover a renewed, and revitalized self.</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRUT02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY091' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY274' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY536' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY558' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY559' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY560' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY714' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY715' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY790' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN32' />
    
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR100' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE08' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PIENSPA4' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PIENSPA7' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA05' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR106' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR30' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR36' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBW7' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VIC8' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VIC7' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY616' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY620' />     
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTCMB04' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTCMB06' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI64' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC31' />
   	 <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB16' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR17' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR18' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR15' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA13' />
     <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA39' />
     
     
     
     
</div>
<!--container01 close-->


<div class="cl"></div>
		<div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">2</a>
            <span class="pages_link">3</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming04.jsp">4</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming04.jsp">Next →</a>
         </div>
<div class="cl"></div>




<div class="footer-ny">
<p>© 2013 healthkart.com</p>
<a href="https://twitter.com/healthkart"><img src="${pageContext.request.contextPath}/images/ny2013/twitter-img.jpg" /></a>
<a href="https://www.facebook.com/healthkart"><img src="${pageContext.request.contextPath}/images/ny2013/fb-img.jpg" /></a>
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

