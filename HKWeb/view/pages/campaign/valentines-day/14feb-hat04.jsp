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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/hep-as-depp.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR80' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR90' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR91' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN36' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN42' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY195' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY201' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY355' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY430' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY431' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY434' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY435' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY436' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY437' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY438' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY439' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY440' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY441' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY443' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY444' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY446' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY451' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY452' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY453' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY454' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY455' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY456' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY458' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY487' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY493' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY500' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY629' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY680' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRBR28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRBR8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CLRBR9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO45' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO46' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO47' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC087' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC088' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAC089' />
   
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <span class="pages_link">4</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">Next</a>
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

