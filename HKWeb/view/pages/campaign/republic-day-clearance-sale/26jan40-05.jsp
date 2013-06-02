<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = WebContext.isSecure();
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


<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KENT26' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP05' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP06' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP08' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP09' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP11' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP12' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP14' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP15' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY745' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY750' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY753' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY754' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY756' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY771' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY772' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ORSG01' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRDRM30' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRDRM64' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='QTNC1' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='QTNC2' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='QTNC3' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='QTNC4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='QTNC5' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='QTNC6' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ON006' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY441' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY449' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY454' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY455' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY456' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY457' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA6' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA7' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA8' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM1' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM10' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM11' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM17' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM18' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM19' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM2' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM20' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM22' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM23' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM24' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM25' />


    

</div>
<!--container01 close-->

    <div class="cl"></div>
       <div class="pages">
           <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-04.jsp">Previous</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-40.jsp">1</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-02.jsp">2</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-03.jsp">3</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-04.jsp">4</a>
           <span class="pages_link">5</span>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-06.jsp">6</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-07.jsp">7</a>
           <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-06.jsp">Next</a>
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

