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





<img src="${pageContext.request.contextPath}/images/ny2013/brain.jpg" />





<!--container01 start-->

<div class="container01">
<h2 class="txt01">Higher Grades this Year!</h2>

<div class="contant01">

<img src="${pageContext.request.contextPath}/images/ny2013/brain-power.jpg"/>
<p>Poor grades are not due to lacking intelligence. Rather, they come from a lack of interest & a nutritional imbalance. Give the best morning beverage to your child to boost her/his memory, along with a heap of Chyawanprash for higher immunity & concentration.</p>

</div>

<div class="cl"></div>

<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB152' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT822' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT684' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT970' />
<div class="cl"></div>

<div class="container01">
<h2 class="txt01">Better Memory, Higher Focus & Higher IQ</h2>

<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/resu01.jpg"/>
<p>Studies reveal that the brain needs fuel, just like the body. A good regime of puzzles, tricks and memory exercises go a long way in ensuring that mental acumen is both honed and preserved. Enriching your diet with Gingko Biloba, Curcumin & Omega 3 Fatty acids, leads to an overall improvement in memory, concentration and productivity. Further, you can also go the Ayurveda way and enhance your memory and cognition. Time to be the Jedi! </p>
</div>


<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT432' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT449' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1321' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1464' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1065' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1730' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1678' />

<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1285' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT993' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1139' />

<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2408' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1140' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT558' />

<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1204' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1516' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1526' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1767' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT794' />

</div>
<!--container01 close-->




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

