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


<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY414' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY415' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY417' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY418' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY419' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY420' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY421' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY422' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY426' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY428' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY429' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY430' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY431' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DBRH1' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DBRH4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY416' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN11' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY644' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY647' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY649' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY654' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY656' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY658' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN1' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN2' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN5' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRSH02' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HSTYL1' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HSTYL10' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT02' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT03' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT05' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT06' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT07' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT08' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT09' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT10' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT11' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT12' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT14' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT15' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT16' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT17' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT18' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT19' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT23' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT24' />


    

</div>
<!--container01 close-->

      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-03.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-40.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-03.jsp">3</a>
            <span class="pages_link">4</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-07.jsp">7</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-05.jsp">Next</a>
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

