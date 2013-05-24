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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/give-her-more-balls.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1208' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1209' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1210' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1211' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1212' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1339' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1340' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1341' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1342' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1343' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3133' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3134' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3135' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT483' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT525' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT526' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT527' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT528' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT529' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT537' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT541' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT542' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT543' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT544' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT545' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT546' />
    




    

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

