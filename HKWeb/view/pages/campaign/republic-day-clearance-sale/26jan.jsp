<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = SslUtil.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Happy Republic Day">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/26jan.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Happy Republic Day</span>

		<h1 class="title">Happy New Year 2013</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Happy Republic Day</s:layout-component>
<s:layout-component name="metaKeywords">Happy Republic Day</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

<div id="wrapper">

<jsp:include page="../includes/_menu26jan.jsp"/>





<!--container01 start-->

<div class="container01">


<div class="contant01">
    <h2 class="txt01">Celebrate 26 January With a Health Boost!</h2>
    <div class="cl"></div>
<p>Our First Ever Clearance Sale is Here! Now, you can save anywhere up to 80% on your favorite health and wellness products, available only till 31st January. Did we mention that stocks are limited?</p>
</div>


<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT304' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNOCK001' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT730' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS001' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT804' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB004' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB152' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT448' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT722' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT326' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1368' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PA012' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT125' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE341' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT109' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV001' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT033' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM011' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH071' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHL002' />
</div>
<!--container01 close-->




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

