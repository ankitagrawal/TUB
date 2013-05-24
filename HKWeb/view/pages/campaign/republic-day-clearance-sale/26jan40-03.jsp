<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = SslUtil.isSecure();
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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT106' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT401' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT069' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT087' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT055' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT095' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT399' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT094' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT086' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1411' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1412' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1410' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT755' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT758' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT305' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT437' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT632' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT418' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AS010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HR036' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP062' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW012' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VKR002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU073' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM030' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH047' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COMODE002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HR001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SCARE007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP066' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP064' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA024' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JRDNA12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JRDNA14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JRDNA5' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JRDNA6' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JRDNA7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JRDNA9' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP063' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIAFC001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIAFC002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RD002' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RD004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TP001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR50' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB17' />

    

</div>
<!--container01 close-->

      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-02.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-40.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-02.jsp">2</a>
            <span class="pages_link">3</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-07.jsp">7</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-04.jsp">Next</a>
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

