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



<div class="logo">
<a href="http://www.healthkart.com"><img src="${pageContext.request.contextPath}/images/ny2013/logo.jpg" />  </a>
</div>





<img src="${pageContext.request.contextPath}/images/ny2013/living01.jpg" />

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


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/poop-effortlessly.png"/>
    <h2 class="txt01">Smooth Motion Everyday!</h2>
<p>Constipation, poor bowel movement and gas are now in the past. Here is how you can ensure that you look like Rodin's Thinking Man, when on the John. You'll be surprised to know that tens of thousands of Indians have depended on the goodness of Isabgol, Bel, Cascara Sagrada and Tripala. Just explore and you will surely find the antidote to your writer's block!</p>
</div>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2567' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1131' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT810' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1064' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1527' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1589' />	        
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1531' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT818' />


</div>
<!--container01 close-->



<!--container01 start-->

<div class="container01">

    
<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/alarm.png"/>
    <h2 class="txt01">Sayonara Monsieur Fear</h2>
<p>Do you find Modern Times too insecure? We have the right goodies for every security need that is possible. Whether it's defending yourself from a group of miscreants, or helping you carry a suitable pepper spray in your keychain or simply having an alarm that goes off when someone is trying to elope with your handbag - we have it all covered. Wish 'fear' good bye!</p>
</div>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP003' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DEF01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COP004' />


</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">


<div class="contant01">

<img src="${pageContext.request.contextPath}/images/ny2013/bathe-like-a-queen.png"/>
    <h2 class="txt01">Bathe Like a Queen</h2>
<p>Pamper the Divinity within with a luxurious bath and rejuvenate your senses. Use the best bath salts, lotions, appealing aromas and essences to feel enlightened and spread the scent of the new year.<br/>From soaps out of a poet's dream, to fragrances from Pradise, to bath oils straight out of Valhalla, we have  everything that you need to make your bath more Royal and less like a shower. Indulge yourself, after all, you owe it to the world! </p> 
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OSSG01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI40' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI52' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='YRDLY7' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTCMB06' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA39' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA34' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BTY01' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA12' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP14' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP09' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP13' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE20' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI36' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR34' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG02' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP01' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA22' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR16' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC36' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG03' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA04' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA11' />	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR01' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR24' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PIENSPA5' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PIENSPA3' />
	
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">

<img src="${pageContext.request.contextPath}/images/ny2013/blood-sugar-management.png"/>
    <h2 class="txt01">Control Blood Sugar. Like a Boss!</h2>
<p>Put a rest to all sugar and diabetes related worries with India's favorite glucometers and testing strips. <br/>You don't have to compromise on your taste buds. Enough of that already. Check out Drizzles & Shakes that are digested over a period of 6 - 9 hours. Sounds perfect, doesn't it?</p> 
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT817' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT035' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT033' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT034' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT036' />
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

