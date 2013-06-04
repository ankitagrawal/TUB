<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = WebContext.isSecure();
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



<div class="logo">
<a href="http://www.healthkart.com"><img src="${pageContext.request.contextPath}/images/ny2013/logo.jpg" />  </a>
</div>





<img src="${pageContext.request.contextPath}/images/ny2013/living01.jpg" />

<div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-living.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-living03.jsp">Next →</a>
         </div>
<div class="cl"></div>


<!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/dont-pause-at-menopause.png"/>
    <h2 class="txt01">Don’t Pause at Menopause</h2>
<p>There is a cougar in you. Get fabulous while your body switches gears. Choose the tea that suits your mood and detoxify yourself, rev up your engines and get back on the prowl.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1401' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1402' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1403' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1404' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1405' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1406' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1407' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1471' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1513' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT802' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT803' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT804' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT805' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT807' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT861' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT862' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT863' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT884' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT977' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="	container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/bike-trip.png"/>
    <h2 class="txt01">Go on a Bike Trip</h2>
<p>The bike is waiting, so are the highways, the dhabhas and your friends. The road comes free with it. Now before you head out to Leh or Himachal, just remember that your bike trip is only as good as your preparation. Things go wrong everyday (trust us on this) - so load up with these numbers and you will be less sorry later. <br/> Also, shades don't hurt!</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M002' />        
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HEA007' />	
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH089' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT183' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE584' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1236' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1713' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1715' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1716' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE297' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE583' />
</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/travel-grandparents.png"/>
    <h2 class="txt01">Help Your Grandparents Travel</h2>
<p>They helped you take your first few steps now it's your turn. Help them travel with ease, whether it's Pilgrimage or simply revisiting their old 'addas'. Just make sure that they are carrying all the essentials that watch them have the time of their lives!</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADU001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITTMN001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PA001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ACUL009' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ACUL010' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ACUL011' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA002' />	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED024' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA001' />
</div>
<!--container01 close-->

 <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-living.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-living03.jsp">Next →</a>
         </div>
<div class="cl"></div>


<div class="footer-ny">
<p>© 2013 healthkart.com</p>
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

