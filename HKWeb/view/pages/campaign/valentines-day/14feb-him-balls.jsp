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
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT547' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1137' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1138' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1265' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1266' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1267' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1268' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1269' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1270' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1271' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1272' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1273' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1274' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1275' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1276' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1277' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1278' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1279' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1280' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1302' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1326' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1344' />
    
   




      <div class="cl"></div>
		<div class="pages">
             <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-him-balls02.jsp">Next</a>
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

