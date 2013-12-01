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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/mc-steamy-steamy.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AYUCR4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOOD01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOOD02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS040' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS052' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS054' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAC002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KOH001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MEL001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOOD03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOOD04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PM012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PP043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS042' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PS046' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RIB001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKR001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKR002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKR004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKR005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKR006' />
    
    




    

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

