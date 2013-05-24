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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LDBR02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2100' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2101' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2102' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2281' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2280' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2103' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE550' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1217' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE439' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE436' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1216' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1219' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1214' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1215' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='STETH001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPTER005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SCARE010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPT002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NRWNA14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEC004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROM011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN010' />


</div>
<!--container01 close-->

    <div class="cl"></div>
      <div class="pages">
          <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan-80.jsp">Previous</a>
          <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-80.jsp">1</a>
          <span class="pages_link">2</span>
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

