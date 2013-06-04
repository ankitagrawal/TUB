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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/cologne-banner.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRM1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRM2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRM3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK06' />
   
    




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne04.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne04.jsp">4</a>
            <span class="pages_link">5</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne06.jsp">6</a>
 
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-cologne06.jsp">Next</a>
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

