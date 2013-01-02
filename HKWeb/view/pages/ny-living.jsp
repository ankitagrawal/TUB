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





<img src="${pageContext.request.contextPath}/images/ny2013/living.jpg" />

 <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-living02.jsp">Next →</a>
         </div>
<div class="cl"></div>


<!--container01 start-->

<div class="container01">
<h2 class="txt01">Poop Effortlessly</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1204' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1521' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1531' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT818' />


</div>
<!--container01 close-->



<!--container01 start-->

<div class="container01">
<h2 class="txt01">Alarm</h2>
    
<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/reduce-body-aches.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP004' />


</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Bathe Like a Queen</h2>

<div class="contant01">

<img src="${pageContext.request.contextPath}/images/ny2013/long-lasting-makeup.jpg"/>
<p>Pamper the Divinity within with a luxurious bath and rejuvenate your senses. Use the best bath salts, lotions, appealing aromas and essences to feel enlightened and spread the scent of the new year.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OSSG01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI40' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI52' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='YRDLY7' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTCMB06' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM01' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA39' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA34' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BTY01' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA12' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP14' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BPP09' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BPP13' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE20' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI36' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR34' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG02' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP01' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA22' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR16' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC36' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG03' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA4' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA11' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VICA8' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR01' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR24' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PENSPA5' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PENSPA3' />
	
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Control Blood Sugar</h2>

<div class="contant01">

<img src="${pageContext.request.contextPath}/images/ny2013/reduce-medical.jpg"/>
<p>Put a rest to all sugar and diabetes related worries with regular testing and Diabetes friendly snacks and deserts.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT817' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT035' />


</div>
<!--container01 close-->

  <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-living02.jsp">Next →</a>
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

