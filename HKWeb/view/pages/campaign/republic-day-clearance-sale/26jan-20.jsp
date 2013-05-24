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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NGE1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SYST8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SYST4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU25' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VGAYU31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYAH7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SYST10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SYST5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SYST7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SYST9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYAH2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYAH5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYAH1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VNCP30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALV1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ALVDA21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA35' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA41' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA42' />
   

   

</div>
<!--container01 close-->

     <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-09.jsp">9</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-02.jsp">Next</a>
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

