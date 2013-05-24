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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/hot-office-secy.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE676' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE677' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE739' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE745' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE781' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1772' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1170' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE960' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1566' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1651' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1654' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1637' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1781' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1784' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1788' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1727' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1195' />
    




    

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

