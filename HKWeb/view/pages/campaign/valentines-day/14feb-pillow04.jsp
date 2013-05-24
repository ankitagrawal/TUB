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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF031' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF033' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF037' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF040' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF041' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF042' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF046' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RM001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RM003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RO001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RO002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RS001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RS002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RS003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RS004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RS005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW009' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW011' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW013' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW014' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW016' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RZ001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RZ002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RZ003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RZ005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT043' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT044' />
 
    
    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow03.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow03.jsp">3</a>
            <span class="pages_link">4</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow07.jsp">7</a>
 			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-pillow05.jsp">Next</a>
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

