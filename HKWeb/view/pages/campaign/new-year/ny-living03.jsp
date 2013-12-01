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
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-living02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living02.jsp">2</a>
            <span class="pages_link">3</span>
        </div>
<div class="cl"></div>





    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/learn-sport.png"/>
    <h2 class="txt01">Learn a New Sport</h2>
<p>You can bundle health, fitness, fun and socializing into 1 awesome thing - a sport! Whether it's TT or football or basky or gully cricket, we have everything you need to get started. This new year (and the winters too) is perfect for any Sport. Just get up and go do something.</p>
</div>

    <a href="http://www.healthkart.com/sports/fab-corner/win-win-deals">
<div class="offer02">
<div class="offer-inside"><span class="discount-txt">Get Up to 40% on badminton, wrestling <br/> and gym gears</span>
</div>
</div></a>

    <a href="http://www.healthkart.com/sports/fitness-accessories?brand=Grizzly">
<div class="offer02b">
<div class="offer-inside"><span class="discount-txt">Get  Up to 10-20% off on Grizzly training belts, balls gloves and knee sleeves.</span>
</div>
</div></a>

    <div class="cl"></div>
    <a href="http://www.healthkart.com/sports/fitness-accessories?brand=Adidas">
<div class="offer01">
<div class="offer-inside"><span class="discount-txt">Get Flat 30% off on Adidas Gym accessories</span>

<div class="cl"></div>
<div class="coupon-box">
<span class="coupon-txt">Use Coupon Code: HKADIDAS</span></div>
<span class="coupon-expiry">*Coupon expiry on 10th January 2013</span>
</div>
</div></a>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2499' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2511' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2515' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT176' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT329' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1302' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1345' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1392' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT133' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT123' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1661' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2518' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT908' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1442' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1324' />	
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1319' />


</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/get-laid-safe-sex.png"/>
    <h2 class="txt01">Sizzle Between the Sheets</h2>
<p>We really don't want to say anything. Just make sure that your partner and you play safe (but not too safe).The world is your rabbit hole and you are all bunny rabbits!</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KOH001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MEL001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOOD03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS041' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS042' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RIB001' />


</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/reduce-body-aches.png"/>
    <h2 class="txt01">Reduce Body Aches</h2>
<p>It's a New Year, it's a new You and You are unstoppable! That annoying ache has had it enough! It's time to put it to rest and move on to more gratifying vistas.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HEA011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HEA010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HEA004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GO001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU70' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP033' />

</div>
<!--container01 close-->
   
    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-living02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-living02.jsp">2</a>
            <span class="pages_link">3</span>
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

