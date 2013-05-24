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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/awkward-gift-corner.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1908' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2068' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2075' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2079' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB339' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB340' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB925' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CAS009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOS002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH083' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH084' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH085' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH086' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH087' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1534' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1535' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB164' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB214' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB270' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB271' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB272' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB273' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB274' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB276' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB376' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB380' />
    
    



      <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing06.jsp">6</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-boxing02.jsp">Next</a>
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

