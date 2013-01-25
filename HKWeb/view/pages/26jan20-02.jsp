<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Happy Republic Day">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/26jan.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Happy Republic Day</span>

		<h1 class="title">Happy New Year 2013</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Happy Republic Day</s:layout-component>
<s:layout-component name="metaKeywords">Happy Republic Day</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

<div id="wrapper">

<jsp:include page="../includes/_menu26jan.jsp"/>


<!--container01 start-->

<div class="container01">

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA43' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA45' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN19' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='H2O022' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='H2O023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROOT31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY606' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY608' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY609' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY610' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY611' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY613' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY614' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY615' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA8' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1136' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT117' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT603' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1320' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1213' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT404' />
    
    
   

</div>
<!--container01 close-->

     <div class="cl"></div>
		<div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/26jan-20.jsp">← Previous</a>
	        <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-20.jsp">1</a>
            <span class="pages_link">2</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-09.jsp">9</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/26jan20-03.jsp">Next →</a>
         </div>
<div class="cl"></div>




<div class="footer-ny">
<p>© 2013 healthkart.com</p>
<a href="https://twitter.com/healthkart"><img src="${pageContext.request.contextPath}/images/ny2013/twitter-img.jpg" /></a>
<a href="https://www.facebook.com/healthkart"><img src="${pageContext.request.contextPath}/images/ny2013/fb-img.jpg" /></a>
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

