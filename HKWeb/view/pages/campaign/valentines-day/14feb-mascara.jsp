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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/mascara-banner.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE038' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1718' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE314' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE314' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE048' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE468' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE337' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE338' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE339' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1236' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1238' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE297' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE358' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE402' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE947' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1216' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1217' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE972' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE978' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE652' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE610' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE611' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1206' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE621' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE624' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1208' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1210' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1118' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1713' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1717' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1797' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1799' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1800' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1803' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1814' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1759' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1760' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE634' />
    




    

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

