<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
	boolean isSecure = SslUtil.isSecure();
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
<a href="http://www.healthkart.com"><img  src="${pageContext.request.contextPath}/images/ny2013/logo.jpg" />  </a>

</div>




<img src="${pageContext.request.contextPath}/images/ny2013/fitness01.jpg" />





<a href="http://www.healthkart.com/sports/footwear?brand=Joola">
<div class="offer">
<div class="offer-inside"><span class="discount-txt">10% off on Joola tennis shoes</span>
<div class="cl"></div>
<div class="coupon-box">
<span class="coupon-txt">Use Coupon Code: HKJOOLA</span></div>
<span class="coupon-expiry">*Coupon expiry on 10th January 2013</span>
</div>
</div></a>


 <a href="http://www.healthkart.com/sports/footwear?brand=Nivia">
<div class="offer">
<div class="offer-inside"><span class="discount-txt">Get Flat 20% off on Nivia Shoes</span>

<div class="cl"></div>
<div class="coupon-box">
<span class="coupon-txt">Use Coupon Code: HKNIVIA</span></div>
<span class="coupon-expiry">*Coupon expiry on 10th January 2013</span>
</div>
</div></a>


<a href="http://www.healthkart.com/sports/footwear?brand=Vector+X">
<div class="offer">
<div class="offer-inside"><span class="discount-txt"> Get Flat20% off on Vector X Shoes</span>
<div class="cl"></div>
<div class="coupon-box">
<span class="coupon-txt">Use Coupon Code: HKVECTORX</span></div>
<span class="coupon-expiry">*Coupon expiry on 10th January 2013</span>
</div>
</div></a>    









  <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-fitness01.jsp">2</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-fitness01.jsp">Next →</a>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<div class="contant01">

<img src="${pageContext.request.contextPath}/images/ny2013/4-percent.png"/>

    <h2 class="txt01">4% Body Fat </h2>
<p>Show the world that you have arrived! Slash your body fat to 4 %, show off your abs, biceps, triceps, calves and quads and see some jaws drop. To get there, you need not just a few gruelling months with a killer work out; you’ll need some fat burners, whey proteins and aminos. These supplements will enhance fat loss, muscle recovery and growth and pure mass gains! You'll get the added energy from pre workout supplements & aminos. We also have gym accessories to make the sessions  far more effective!</p>

</div>


<a href="http://www.healthkart.com/sports/fitness-equipment?brand=Tunturi">
<div class="offer02">
<div class="offer-inside"><span class="discount-txt"> Flat 25% off on Tunturi Gym and Work-out Equipment</span>
<div class="cl"></div>
<div class="coupon-box">
<span class="coupon-txt">Use Coupon Code: HKFITNESS</span></div>
<span class="coupon-expiry">*Coupon expiry on 10th January 2013</span>
</div>
</div></a>  

<a href="http://www.healthkart.com/sports/fitness-equipment?brand=Finnlo">
<div class="offer02b">
<div class="offer-inside"><span class="discount-txt"> Extra 5% off on Finnlo Bio Force Sport Multi-Gym</span>
<div class="cl"></div>
<div class="coupon-box">
<span class="coupon-txt">Use Coupon Code: HKFINNLO</span></div>
<span class="coupon-expiry">*Coupon expiry on 10th January 2013</span>
</div>
</div></a>
    

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


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/bigger-chest.png"/>
    <h2 class="txt01">Get That Massive Chest, with 500 Push Ups a Day!</h2>
<p>Want a 44 inch chest? Conquer the league of 500 pushups and get that tone you desire. Press away twice as much of your body weight at a throw-away convenience with compact push up accessories & supplements for a bigger chest. Bring home the perfect equipment for chest expansion and lid your space issues with expedient chest expanders. Enjoy handy chest enhancements with potent proteins, whey add-ons & ideal-for-home chest expansion accessories.</p>
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

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/easier-physiotherapy.png"/>
    <h2 class="txt01">Become Your Own Physiotherapist!</h2>    
<p>This New Year, be your own physiotherapist! You heard right, we’re going to make a physio out of you and help you save tons and tons of money. Therapy accessories that are a breeze to set up and extremely effective for you, right here at HealthKart.</p>
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

