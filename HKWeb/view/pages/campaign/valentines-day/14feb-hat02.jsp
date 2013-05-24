<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = SslUtil.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Happy Valentines Day">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/14feb.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Happy Valentines Day</span>

		<h1 class="title">Happy Valentines Day</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Happy Valentines Day</s:layout-component>
<s:layout-component name="metaKeywords">Happy Valentines Day</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

<div id="wrapper">
 <jsp:include page="/includes/_menu14feb.jsp"/>
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/hep-as-depp.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY763' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTYGAR1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTYSHAH2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-EYE02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE052' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE053' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE054' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE055' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1750' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE271' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE272' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE340' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE341' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE342' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE415' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE478' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE479' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE674' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN33' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC051' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMLY09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSTHB22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSTHB35' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KHADI36' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LASS10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIVPRF07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL49' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LTHB22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNRL01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEOV2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEOV4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NGENA3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1073' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1728' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OLY003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OLY019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMVED11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRDRM61' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SBMD18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SHNZ10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SHNZ14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='STVKC10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM28' />
    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">Next</a>
         </div>
<div class="cl"></div>



<div class="footer-ny">
<p>© 2013 healthkart.com</p>
<a href="https://twitter.com/healthkart"><img src="${pageContext.request.contextPath}/images/14feb/twitter-img.jpg" /></a>
<a href="https://www.facebook.com/healthkart"><img src="${pageContext.request.contextPath}/images/14feb/fb-img.jpg" /></a>

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

