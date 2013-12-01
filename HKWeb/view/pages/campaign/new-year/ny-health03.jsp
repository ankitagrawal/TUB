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





<img src="${pageContext.request.contextPath}/images/ny2013/health01.jpg" />

    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-health02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health02.jsp">2</a>
            <span class="pages_link">3</span>
        </div>
<div class="cl"></div>

    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/root-canal.png"/>
    <h2 class="txt01">No More Root Canal</h2>
<p>A stitch in time saves 9000. Especially when it comes to a root canal (don't even get us started on how much it hurts and how long it takes and did we mention costs?) So here are some numbers that will bring down the pain a level, not to mention prevent root canal completely.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY107' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY108' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH071' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH101' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRS003' />

    

</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/quit-smoking.png"/>
    <h2 class="txt01">QUIT Smoking</h2>
<p>You have always dreaded the 3-day-hump and the prospect of going cold turkey. You keep saying that you’ve quit smoking but that lasted only for 15 minutes. Now you can quit smoking like a manly man who keeps his word. Here’s how!</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1401' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT804' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT861' />
</div>
<!--container01 close-->


        <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/reduce-medical-bills.png"/>
    <h2 class="txt01">Reduce Medical Expenses</h2>
<p>A range of medical tests for and stellar rates, so that you are not taken in by surprise when something pops up without warning.</p>
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SER002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SER121' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SER124' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SER165' />




</div>
<!--container01 close-->


    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-health02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-health02.jsp">2</a>
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

