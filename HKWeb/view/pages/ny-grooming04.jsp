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
            <a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">3</a>
            <span class="pages_link">4</span>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<h2 class="txt01">Reduced Chapped Skin</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>For a skin that deserves a cakewalk, nothing works better than the wholesome luxury of handcrafted recreational skin butters and bath foams. From lining your lips for a seriously head-turning pout to scrubbing off dead scales of skin, here is a class of skin elixirs you cannot say no to, especially when you have a resolution to keep. Love yourself. Indulge.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY300' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY305' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY309' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY311' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KHADI29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL39' />    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA15' />    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF010' />    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT69' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR100' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MYBLN37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEAT005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIVEPRF12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN65' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE35' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY03' />
    
    
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Remove dark undearms</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>
    
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY302' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY310' />
    

</div>
<!--container01 close-->


<!--container01 start-->

<div class="container01">
<h2 class="txt01">Replace specs with fashion</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE052' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE054' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE271' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE272' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE340' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE341' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE342' />

</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Shaving</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY256' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MSH005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MSH008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OSSG02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEDIC39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET5' />


</div>
<!--container01 close-->


<div class="cl"></div>
		<div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">3</a>
            <span class="pages_link">4</span>
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

