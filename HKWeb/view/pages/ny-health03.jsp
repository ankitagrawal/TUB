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
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-health02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health02.jsp">2</a>
            <span class="pages_link">3</span>
        </div>
<div class="cl"></div>


<!--container01 start-->

<div class="container01">
<h2 class="txt01">Hydrate Everyday</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1289' />

</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">No more Root Canal</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY107' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY108' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH071' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH101' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRS003' />

    

</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">QUIT Smoking</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1401' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT804' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT861' />




</div>
<!--container01 close-->


        <!--container01 start-->

<div class="container01">
<h2 class="txt01">Reduce Medical Expenses</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SER002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SER121' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SER124' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SER165' />




</div>
<!--container01 close-->


    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-health02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health02.jsp">2</a>
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

