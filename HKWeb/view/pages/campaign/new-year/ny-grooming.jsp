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



<img src="${pageContext.request.contextPath}/images/ny2013/grooming01.jpg" />

 <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming04.jsp">4</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">Next →</a>
         </div>
<div class="cl"></div>



 <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/fair-skin.png"/>
    <h2 class="txt01">Fair Skin</h2>
<p>Want to look fairer? Tired of creams and your grandmother’s home-made remedies? Get sublime natural herbs and potions to work their magic on you, expect some heads to turn.</p> 
</div>

<div class="cl"></div>

	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY305' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY308' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY004' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MYBLN30' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA10' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTYVLC2' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSTHB29' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OLY011' />	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR104' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR108' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL52' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR02' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY337' />
	
	

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NGENA4' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SHNZ05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI21' />
    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT70' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/kill-body-odor.png"/>
    <h2 class="txt01">Get Rid of Body Odour</h2>
<p>The days of marking your territory with your natural scent are long gone. This new year, let the menace of body odor fade into oblivion with deodorants & the like that have helped many a Genin, turn into a true Shinobi of Konoha!</p>
</div>

<div class="cl"></div>
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA6' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA4' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='YRDLY2' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS5' />
	
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA3' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR39' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY02' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY03' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY04' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LARM3' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRG056' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LARM2' />
	<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ENBLZR03' />	
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ADIDAS1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY174' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY301' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY302' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY310' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK03' />

</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/get-in-shape.png"/>
    <h2 class="txt01">Get a Shapely Body</h2>
<p>Get that dream shape that you have always had on your mind. Whether it’s a sensuous curve or washboard abs or simply some arcane spot reduction - we do it all!</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AYUCR1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY531' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY581' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY586' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT448' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RD001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT050' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT051' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT052' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT081' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT093' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT095' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT105' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT108' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT109' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1260' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1261' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1303' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1306' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT144' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT145' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT215' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT389' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT390' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT391' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT402' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT431' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT433' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT657' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT659' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT664' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT686' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT706' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT707' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT739' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT742' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT838' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLC001' />
</div>
<!--container01 close-->


    <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming04.jsp">4</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">Next →</a>
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

