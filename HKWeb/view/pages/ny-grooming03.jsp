<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
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



<a href="http://www.healthkart.com"><img class="logo" src="${pageContext.request.contextPath}/images/ny2013/logo.jpg" />  </a>





<img src="${pageContext.request.contextPath}/images/ny2013/grooming.jpg" />

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
<h2 class="txt01">Long Lasting Makeup</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/long-lasting-makeup.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR52' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY051' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY203' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY313' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY351' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY354' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY355' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY629' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY680' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN26' />
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
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MYBLN37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP039' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SHNZ18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SRTWR02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT2' />
    
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Obliterate hair Loss</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/hair-loss.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
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

</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Pamper Yourself</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/pamper-yourself.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRUT02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY091' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY149' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY150' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY157' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY171' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY172' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY208' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY209' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY231' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY274' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY279' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY299' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY303' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY308' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY536' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY558' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY559' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY560' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY714' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY715' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY790' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DNM005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMLY19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='IMPL01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='IMPL02' />


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
<p>© 2012 healthkart.com</p>
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

