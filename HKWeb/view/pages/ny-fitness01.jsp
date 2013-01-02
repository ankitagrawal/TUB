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
        <a class="next"  href="${pageContext.request.contextPath}/pages/ny-fitness.jsp">← Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-fitness.jsp">1</a>
	        <span class="pages_link">2</span>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">
<h2 class="txt01">Run a Marathon!</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>26.2 miles of physical endurance along with meticulous planning equals a marathon. Good shoes, first aid kit, chafing cream and heart rate monitor and adequate recovery after workouts will minimize the risk of inujury. Last minute preps? No more! Pack these essentials along for that incredible winning experience! </p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1508' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1320' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT779' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT616' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AS004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1519' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU085' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEAT011' />       
    
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Gain Lean Muscle</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/gain-mass.jpg"/>
<p>Gaining lean muscle mass was never this easy. Take your pick from the lot that the champs rely on. It doesn't matter if you are a hard gainer or a vegetarian. With the right diet, workout and the most appropriate mass gainer. Whether you want to look Spartan or a Space Marine, we have it all covered.</p>
</div>

<div class="cl"></div>

   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1368' />
   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1808' />
   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT912' />
   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT476' />
   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT922' />
   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT905' />
   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1222' />
   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1332' />
   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT825' />

</div>
<!--container01 close-->




    <!--container01 start-->

<div class="container01">
<h2 class="txt01">Hit the GYM Like a PRO!</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/go-gym.jpg"/>
<p>Gym gear is the most under appreciated part of hitting the gym daily. Proper gear ensures that you lift with the right form, don't have unwanted sweat dripping along with annoying itches. Hygiene apart, good gym gear also includes a sipper which can keep you hydrated & energized with your favourite intra workout! All of this is incomplete without a duffel bag!</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2100' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2101' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2102' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT303' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT448' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1701' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2313' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT198' />
	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1580' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1581' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT248' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT359' />
</div>
<!--container01 close-->



        <!--container01 start-->

<div class="container01">
<h2 class="txt01">No More Love Handles!</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>You can get rid of love handles forever. Just take your pick & get started, working on reducing mid section fat. You can also opt for the waist shaper if you don't want to start exercising right away!</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RD001' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT001' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2348' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT015' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT437' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT106' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2516' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT837' />

</div>
<!--container01 close-->


<div class="container01">
<h2 class="txt01">Build a Home Gym!</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/home-gym.jpg"/>
<p>Getting to the gym can get difficult and there are times, when getting to the gym is just not enough? Do you find yourself lacking focus in the gym? Do you absolutely hate random gym music? Do you hate waiting for your turn for any machine? Do you hate gym rats, smelly bathrooms and annoying trainers? It's time to build a shrine to the beast within!</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1800' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT951' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT708' />
	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT055' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT022' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1560' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT813' />
	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2363' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2366' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT954' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1496' />
</div>
<!--container01 close-->

<div class="container01">
<h2 class="txt01">Pick Up Your Favorite Sport!</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/learn-newsport.jpg"/>
	<p>Improve your fitness, socialize, build a newer character and tweak your team work skills in one stroke! All you need to do, is choose a sport that resonates with your physical attributes & personality. The sport equipment you choose will decide how fun your game turns out to be.</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2177' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2512' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1357' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1472' />
	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT279' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT870' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT153' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1325' />
	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT516' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2287' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1454' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1738' />
</div>
<!--container01 close-->


<div class="cl"></div>
		<div class="pages">
        <a class="next"  href="${pageContext.request.contextPath}/pages/ny-fitness.jsp">← Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-fitness.jsp">1</a>
	        <span class="pages_link">2</span>
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

