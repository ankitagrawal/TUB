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
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-living.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-living03.jsp">Next →</a>
         </div>
<div class="cl"></div>


<!--container01 start-->

<div class="container01">
<h2 class="txt01">Don’t Pause at Menopause</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/4fat.jpg"/>
<p>Menopause doesn't have to be what the media makes it out to be. Here is how you can ensure that you're feeling amazing while your body switches gears.</p>
<p>All you need to do is choose the tea that suits your mood. The tea will take care of the detoxification. All you need to do is to let go and enjoy!</p>
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
<h2 class="txt01">GO on a Bike Trip</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/bike-trip.jpg"/>
<p>That long awaited 'muhurat' will never come. The bike is waiting and so are the highways and the dhabhas and your friends. It's finally time for you to hit the road in style and show them how it's done.</p>
<p>Now before you head out to Leh or Himachal, just remember that your bike trip is only as good as your preparation. Things go wrong everyday (trust us on this) - so load up now with stuff that will surely come in handy.</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M002' />    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE584' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HEA007' />	
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH089' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT183' />
</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Help Your Grandparents Travel</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/grandparents-travelling.jpg"/>
<p>They helped you take your first few steps - now it's probably time, you made it possible for them to travel in ease!</p>
<p>Whether it's Pilgrimage or simply revisiting their old 'addas', they will have tremendous fun. Just make sure that they are carrying all the essentials that they require and they're set!</p>
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

