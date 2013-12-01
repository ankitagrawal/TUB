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


<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RZ001' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RT013' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RW017' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU032' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED011' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEC005' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP069' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB028' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA010' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN002' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNRL02' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX10' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX18' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX2' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX20' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX21' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX22' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX23' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX24' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX26' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX27' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX28' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX29' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX3' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX31' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX32' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX33' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX34' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX35' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX36' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX37' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX41' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX42' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX43' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX47' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX48' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX49' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX5' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX51' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX6' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX8' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VIVI02' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VIVI04' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VIVI05' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VIVI09' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB096' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT115' />


    

</div>
<!--container01 close-->

      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan-40.jsp">Previous</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-40.jsp">1</a>
            <span class="pages_link">2</span>            
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-07.jsp">7</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-03.jsp">Next</a>
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

