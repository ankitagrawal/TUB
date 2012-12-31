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
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">Next →</a>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<h2 class="txt01">Awesome Baby Shower Gift</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/reduce-chopped-skin.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB099' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB100' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB103' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB122' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB123' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB124' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB198' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Confident Breastfeeding</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/reduce-chopped-skin.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB058' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Easy Feeding for Babies</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/prepare-child.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB036' />


</div>
<!--container01 close-->


      <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">Next →</a>
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

