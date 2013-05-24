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

  <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN7' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN8' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN9' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO48' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO49' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC073' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC074' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC075' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC077' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC078' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC079' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC080' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC082' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC084' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI09' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI10' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI11' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI12' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI13' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI15' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI17' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI18' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI20' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI35' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI36' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC01' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC02' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC03' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC04' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC15' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC18' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC19' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC20' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC21' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC22' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC23' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC24' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC34' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC35' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT1' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT2' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT6' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT7' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT8' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA1' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA3' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA6' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA7' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRISA8' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT50' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT51' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT52' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT53' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT54' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT55' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT56' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT57' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT58' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT73' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPTER3' />




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <span class="pages_link">11</span>
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

