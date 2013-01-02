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





<img src="${pageContext.request.contextPath}/images/ny2013/fitness.jpg" />

  <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-fitness01.jsp">2</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-fitness01.jsp">Next →</a>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<h2 class="txt01">4% Body Fat </h2>

<div class="contant01">

<img src="${pageContext.request.contextPath}/images/ny2013/4fat.jpg"/>
<p>With the approaching 2013, tell the world that you are there. Slash your body fat to 4 %, show off your abs, biceps, triceps, calves and quads and rule the gym. To get there, you need a few gruelling months with a killer workout, some fat burners, whey proteins and aminos. With a proper diet chart in place, these supplements will enhance fat loss, muscle recovery and growth and pure mass gains! You'll get the added energy from pre workout supplements  & aminos. Get glovesfor easy lifting and a duffel bag to hit the gym in style the whole year!</p>

</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1141' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1151' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1159' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1160' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT390' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT402' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT108' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1304' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1225' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1226' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1257' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1270' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1277' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT132' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1327' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1362' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1366' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1369' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1427' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1600' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1798' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT318' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT641' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT722' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT890' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT893' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT910' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT921' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT939' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT962' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT982' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT984' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT986' />
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Get That Massive Chest, with 500 Push Ups a Day!</h2>

<div class="contant01">
<<<<<<< HEAD
<img src="${pageContext.request.contextPath}/images/ny2013/reduce-body-aches.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
=======
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>Been tired enough trying to accomplish a bigger chest? Here is why you should up yourself and put your enerrgy back into it. Because you want it! Conquer the league of 500 push ups and get that tone you desire. Press away twice as much of your body weight at a throw-away convenience with compact push up accessories & supplements for a bigger chest. Bring home the perfect equipment for chest expansion and lid your space issues with expedient chest expanders. Enjoy handy chest enhancement with blasting proteins, whey addons & ideal-for-home chest expansion accessories.</p>
>>>>>>> 947a234796aa58f7155e22ebc7d80b2c4bfc4953
</div>

<div class="cl"></div>

<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1431' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT414' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT772' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT020' />

<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1432' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT074' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT053' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT395' />

<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT056' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1153' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1157' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1162' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1208' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1220' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1264' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1274' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1279' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT130' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT131' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1339' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1340' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1367' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1375' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1415' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT300' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT304' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT468' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT471' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT472' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT475' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT482' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT517' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT572' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT573' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT590' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT593' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT639' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT680' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT681' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT726' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT729' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT911' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT959' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT960' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT980' />


</div>
<!--container01 close-->




    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Become Your Own Physiotherapist!</h2>

<div class="contant01">
<<<<<<< HEAD
<img src="${pageContext.request.contextPath}/images/ny2013/reduce-chopped-skin.jpg"/>
<p>The Treadmill T2000 is a great buy for anyone looking to embark on a journey towards better health and fitness. This treadmill will go from a speed of 0.8 to 12Km and also has an incline function to add more challenge to your workouts. </p>
=======
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>They New Year, break free from your medical expenses and emerge as your own physio to treat the nasty roots of pain. Professional physiotherapy runs up to thousands, burning a hole in your pocket. This is where, at home, therapy accessories come to your resuce. With easier physiotherapy options, you can cut the cost of physio sessions by choosing quick to use therapy accessories. Heal without worrying about escalated therapy costs with a range of rehab supports. You'll surprise yourself with the ease of use that these products have to offer.</p>
>>>>>>> 947a234796aa58f7155e22ebc7d80b2c4bfc4953
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT678' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR024' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT649' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT666' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT654' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT676' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT663' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT665' />
</div>
<!--container01 close-->



  <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-fitness01.jsp">2</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-fitness01.jsp">Next →</a>
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

