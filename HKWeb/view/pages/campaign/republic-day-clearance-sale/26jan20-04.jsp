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

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT583' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT608' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1317' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1250' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1251' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1249' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1569' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1470' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1471' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1584' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1252' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1516' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1189' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1343' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1534' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1253' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP072' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MORPN006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LIT003' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP065' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK023' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK025' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYAH4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB254' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH055' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1762' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1752' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1900' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1901' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1891' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1915' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1944' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1782' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB241' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO23' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO24' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO44' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIVO7' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB072' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FC045' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP019' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZUSKA012' />
   
    




</div>
<!--container01 close-->

     <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-03.jsp">Previous</a>
	        <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-20.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-03.jsp">3</a>
            <span class="pages_link">4</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-07.jsp">7</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan20-09.jsp">9</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan20-05.jsp">Next</a>
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

