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





<img src="${pageContext.request.contextPath}/images/ny2013/parenting01.jpg" />

  <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">Next →</a>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/baby-shower.png"/>
    <h2 class="txt01">Baby Shower Gifts</h2>
<p>The best a baby can have apart from grandparents are dainty little creams, oils and of course soaps and powders for those tiny toes and fingers. A thoughtful set of baby shower gifts from HealthKart.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB099' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB100' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB103' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB122' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB123' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB124' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB198' />


</div>
<!--container01 close-->

<!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/comfortable-baby-feeding.png"/>
    <h2 class="txt01">Feed Babies With Ease</h2>
<p>Babies get moody, wail like banshees when you don’t hold them right and just plain refuse to latch. Mum’s have a hard time when nipples go sore or when babies refuse to feed. Here’s how we solve both the problems.</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1311' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB150' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1959' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB444' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB241' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB243' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB405' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB363' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FER017' />




</div>
<!--container01 close-->


      <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting03.jsp">3</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">Next →</a>
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

