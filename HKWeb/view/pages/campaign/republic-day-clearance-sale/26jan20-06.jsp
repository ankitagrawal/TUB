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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MINE10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MINE6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MINE7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYSA11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RVAYR18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT842' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE403' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RT011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITT005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EASY021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RT001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB622' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LITT007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RT002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RT003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB298' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB246' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB156' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LAKABS12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LAKABS14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LAKABS7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARM017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARM020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARM033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRG033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FACES1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FACES2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FACES4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY627' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY628' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY629' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY632' />
   
   



</div>
<!--container01 close-->

    <div class="cl"></div>
         <div class="pages">
             <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">Previous</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-20.jsp">1</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-02.jsp">2</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-03.jsp">3</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-04.jsp">4</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">5</a>
             <span class="pages_link">6</span>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-07.jsp">7</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">8</a>
             <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-09.jsp">9</a>
             <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-07.jsp">Next</a>
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

