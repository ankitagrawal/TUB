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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/give-him-some-balls.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1720' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1721' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1722' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2097' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2098' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2099' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2146' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT243' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2518' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT516' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT517' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1111' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1112' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1113' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1114' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1115' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1116' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1117' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1118' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1119' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1121' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1122' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1123' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1124' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1125' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1173' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1174' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1175' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1176' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1177' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1178' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1179' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1180' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1181' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1182' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1183' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1184' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1185' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1186' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1187' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1188' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT126' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT127' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT129' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1295' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1310' />
 




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls08.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls08.jsp">8</a>
            <span class="pages_link">9</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls10.jsp">Next</a>
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

