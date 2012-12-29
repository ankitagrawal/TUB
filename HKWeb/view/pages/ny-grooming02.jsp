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





<img src="${pageContext.request.contextPath}/images/ny2013/main_banner.jpg" />

 <div class="cl"></div>
		<div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming04.jsp">4</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">Next →</a>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<h2 class="txt01">Hair Care</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KHADI20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KHADI27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LORMAS2' />



</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Hair Color</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY153' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY163' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY164' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY409' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT76' />

</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Hair Styling</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR67' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI12' />

</div>
<!--container01 close-->


<div class="cl"></div>
		<div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming04.jsp">4</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">Next →</a>
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

