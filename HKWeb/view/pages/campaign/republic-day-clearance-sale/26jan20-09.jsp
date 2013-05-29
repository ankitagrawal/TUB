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


<div class="contant01">



<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR79' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK020' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITTAC007' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RM003' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY10' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY11' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY13' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NVEY14' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE292' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BCKSUP001' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL1' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL13' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL15' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL17' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL2' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL21' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL5' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL7' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR18' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR22' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR23' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR24' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR34' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR35' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY618' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY711' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY712' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY713' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA32' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA34' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA40' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA44' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA5' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG08' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG16' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG26' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG29' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG31' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG32' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PIENSPA3' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VDLSN06' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RA004' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEAT004' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITTAC027' />





</div>
<!--container01 close-->

 <div class="cl"></div>
         <div class="pages">
             <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">Previous</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-20.jsp">1</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-02.jsp">2</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-03.jsp">3</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-04.jsp">4</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">5</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-06.jsp">6</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-07.jsp">7</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">8</a>
             <span class="pages_link">9</span>
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

