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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1591' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1134' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT517' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2042' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1359' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1853' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1127' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1290' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT593' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT994' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1701' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2041' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2082' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RO005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RP006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP068' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VKR001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1942' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RT010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1820' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH066' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH060' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RP010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RT009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIT001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RP009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RT012' />


    

</div>
<!--container01 close-->

  <div class="cl"></div>
       <div class="pages">
           <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-06.jsp">Previous</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-40.jsp">1</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-02.jsp">2</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-03.jsp">3</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-04.jsp">4</a>

           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-05.jsp">5</a>

           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-06.jsp">6</a>
           <span class="pages_link">7</span>
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

