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
<<<<<<< HEAD
<img src="${pageContext.request.contextPath}/images/ny2013/hair-care.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
=======
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>Want beautiful, silky hair? Tired of trying your moms home-made remedies for the same but all in vain? Check out our catalogue of advanced products to get smooth, lustrous hair without any side-effects.</p>
>>>>>>> 947a234796aa58f7155e22ebc7d80b2c4bfc4953
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KHADI20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KHADI27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LORMAS2' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIV07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIV01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VDLSN06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LTDOFF14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LORMAS1' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY716' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ONO38' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BVR27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LYD1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LASS4' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTQ036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY348' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR94' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKOF02' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PLMCL04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PLMCL07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKOF032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRV26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NRWNA5' />

	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BVR28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN52' />

</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Hair Color</h2>

<div class="contant01">
<<<<<<< HEAD
<img src="${pageContext.request.contextPath}/images/ny2013/hair-color.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
=======
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>As much as we all wish for it not to occur, at some point in your life, a gray hair or two is bound to pop out. It is totally your choice whether to go ahead with it and let the grays shine, or apply hair color to just blend them away. Want to go natural and avoid early signs of graying? Then may we suggest supplementing your diet with hair boosting supplements. </p>
>>>>>>> 947a234796aa58f7155e22ebc7d80b2c4bfc4953
</div>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY044' />	
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
<img src="${pageContext.request.contextPath}/images/ny2013/hair-styling.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
</div>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI35' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA1' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS15' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA8' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRN002' />
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

