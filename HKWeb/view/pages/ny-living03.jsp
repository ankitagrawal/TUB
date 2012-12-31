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
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-living02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living02.jsp">2</a>
            <span class="pages_link">3</span>
        </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<h2 class="txt01">Sizzle Between the Sheets</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KOH001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MEL001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOOD03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS041' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS042' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RIB001' />


</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Learn a new sport</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2499' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2511' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2515' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT176' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT329' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1302' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1345' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1392' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT133' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT123' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1661' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2518' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT908' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1442' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1324' />	
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1319' />


</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Reduce Body Aches</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MDRM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MDRM002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI36' />

</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Work Life Balance</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT410' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT411' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT874' />


</div>
<!--container01 close-->

    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-living02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living02.jsp">2</a>
            <span class="pages_link">3</span>
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

