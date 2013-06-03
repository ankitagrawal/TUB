<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = WebContext.isSecure();
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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/clooney-set.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='H2O038' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMSPA4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='IMPL03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='IMPL09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM33' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM41' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM42' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM43' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM44' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM45' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM47' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM50' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM51' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JVN01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KAYA14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LARM3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LARM4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LARM5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LARM6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LASS20' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LASS21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL33' />
  
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow03.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow03.jsp">3</a>
            <span class="pages_link">4</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow06.jsp">6</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow05.jsp">Next</a>
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

