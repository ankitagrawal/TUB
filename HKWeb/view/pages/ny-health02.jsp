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





<img src="${pageContext.request.contextPath}/images/ny2013/health01.jpg" />


<div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-health.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-health03.jsp">Next →</a>
         </div>
<div class="cl"></div>



    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/get-more-sleep.png"/>
    <h2 class="txt01">Get More Sleep</h2>
<p>Whether it's mosquitos, or sleep apnea or indigestion that is bothering you - we have it all covered. Seriously, just browse through and get sound sleep everyday!</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1529' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1105' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR42' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEC012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HR017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RES002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CHOICE001' />    
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/eat-healty.png"/>
    <h2 class="txt01">Turn to Ayurveda!</h2>
<p>A number of ailments, both big and small, flee when faced with an Ayurvedic adversary. Browse through our mini arsenal of Ayurvedic goodies and put ill health behind 2012.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1149' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1224' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1505' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1506' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1507' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1515' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1520' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1521' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1529' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1531' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT577' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT584' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT809' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT810' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT814' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT815' />




</div>
<!--container01 close-->


<div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-health.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-health03.jsp">Next →</a>
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

