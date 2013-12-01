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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/the-lie-detector.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU084' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU085' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU086' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU088' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU089' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU093' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU094' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1518' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1519' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1520' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1521' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1522' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1523' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1525' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1530' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARKRAY001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARKRAY002' />
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU031' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU032' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CITIZN001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-HHD-001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-HHD-002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-HHD-003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-HHD-004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EASY009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB015' />
   
    




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector03.jsp">3</a>
      
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-detector03.jsp">Next</a>
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

