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
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting03.jsp">Next →</a>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<h2 class="txt01">Get rid of Pregnancy Stretch Marks</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/get-rid-pregmarks.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB150' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB151' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT411' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT821' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT874' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHL002' />


</div>
<!--container01 close-->

     <!--container01 start-->

<div class="container01">
<h2 class="txt01">Help Elders to get on top of health</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/help-elders.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HR006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT510' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT511' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT654' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT660' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT686' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT687' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT717' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RC004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR043' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Ovulation Kit</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/ovulation-kit.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER001' />


</div>
<!--container01 close-->


  <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting03.jsp">Next →</a>
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

