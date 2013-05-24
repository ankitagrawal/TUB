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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/goes-with-pillow-fights.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RE009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK026' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF020' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF028' />

    
    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow02.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow02.jsp">2</a>
             <span class="pages_link">3</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow04.jsp">Next</a>
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

