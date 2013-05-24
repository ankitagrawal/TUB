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
<a href="http://www.healthkart.com"><img src="${pageContext.request.contextPath}/images/ny2013/logo.jpg" />  </a>
</div>





<img src="${pageContext.request.contextPath}/images/ny2013/health01.jpg" />


<div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-health02.jsp">Next →</a>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/complete-nutrition-for-mommy.png"/>
    <h2 class="txt01">Complete Nutrition for Mommy</h2>
<p>New mommies are swamped. Days go by when they don’t know what day it is, forget what they ate for breakfast and manage to carry a temperament that is hard to get through. Here’s a nice set of supplements you can slip in when she decides to eat. They will help fortify her with all the iron and minerals along with proteins and other essentials, such that both the baby and the mum are not left wanting.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1294' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1322' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT410' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT412' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT658' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT812' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT816' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT859' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT903' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT988' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/diabetes-management.png"/>
    <h2 class="txt01">Diabetes Management</h2>
<p>Managing Diabetes and getting the most out of life was never easier. Now you can choose from India's best meters, testing strips, sweeteners, crispies, drizzles and shakes and put all your sugar related worries away!</p>
</div>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT108' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT109' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DL003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DL004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DL005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DL006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM040' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS038' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT133' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT134' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1523' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT689' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT968' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROSMAX001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT033' />
    

</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/dont-stay-hungry.png"/>
    <h2 class="txt01">Do not Go Hungry</h2>
<p>A lean waist a lighter body without starving yourself crazy! It is possible with some good protein bars and other low calorie snacks that we have on sale.</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT651' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT716' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT974' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT036' />
</div>
<!--container01 close-->


    <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-health02.jsp">Next →</a>
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

