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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY633' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FACNP1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC047' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC089' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC094' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC097' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC126' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRG009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA45' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA46' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA48' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA49' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA50' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSTHB22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSTHB24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSTHB31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSTHB33' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PLMCL13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PLMCL15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRG055' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRG056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SRTWR06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='YRDLY4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE291' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE720' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RC002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITTAC005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR06' />
    




</div>
<!--container01 close-->

    <div class="cl"></div>
         <div class="pages">
             <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-06.jsp">Previous</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-20.jsp">1</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-02.jsp">2</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-03.jsp">3</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-04.jsp">4</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">5</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-06.jsp">6</a>
             <span class="pages_link">7</span>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">8</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-09.jsp">9</a>
             <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">Next</a>
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

