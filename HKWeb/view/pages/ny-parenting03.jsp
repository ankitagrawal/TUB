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





<img src="${pageContext.request.contextPath}/images/ny2013/parenting.jpg" />

    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">2</a>
            <span class="pages_link">3</span>
        </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<h2 class="txt01">Poop Effortlessly</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/poopeffortlessly.jpg"/>
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
<h2 class="txt01">Potty Train ur baby</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/potty-train.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1931' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1934' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB483' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Prepare your child for Illness</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/prepare-child.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1908' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BCKRST003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HT004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HT006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HT007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OPT001' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Send ur Child to School</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/send-child-school.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT651' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT716' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT775' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT779' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT791' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT868' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT870' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT036' />


</div>
<!--container01 close-->


    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">2</a>
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

