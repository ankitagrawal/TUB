<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = WebContext.isSecure();
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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1131' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1118' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1319' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT605' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT612' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT606' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT599' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1135' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1128' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT617' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1138' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT609' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT597' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT602' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT627' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1312' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT598' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT581' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1110' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1113' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT620' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT607' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT616' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT577' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1130' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT306' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT604' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1116' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1120' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT613' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1129' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT576' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT628' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1114' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1315' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT600' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT260' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT575' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1122' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1111' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT618' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1134' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1112' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1126' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1137' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT614' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT594' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1115' />
   
   
   




</div>
<!--container01 close-->

     <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-02.jsp">Previous</a>
	        <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-20.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-02.jsp">2</a>
            <span class="pages_link">3</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-09.jsp">9</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-04.jsp">Next</a>
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

