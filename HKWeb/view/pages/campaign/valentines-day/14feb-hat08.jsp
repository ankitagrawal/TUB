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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BLISS14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN44' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN45' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN46' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN47' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN48' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN49' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN50' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN54' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN56' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRN004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY638' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY639' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY640' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY641' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY642' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY644' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY645' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY646' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY647' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY648' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY649' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY650' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY652' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY653' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY654' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY655' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY656' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY657' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY658' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DENMAN5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO21' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO25' />
  
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat07.jsp">7</a>
            <span class="pages_link">8</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">9</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat10.jsp">10</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat11.jsp">11</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-hat09.jsp">Next</a>
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

