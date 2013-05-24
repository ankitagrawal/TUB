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


<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM26' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM3' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM5' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM6' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TVM7' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI18' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI25' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI29' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI3' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI4' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT30' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT38' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT41' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB098' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF052' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1306' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT093' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1430' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT030' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT438' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT631' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT760' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1432' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2282' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1691' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1043' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1047' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1053' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1075' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1083' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1117' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1674' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1705' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1119' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1309' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT656' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1094' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1019' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1782' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1783' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT993' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1571' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1585' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1586' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1587' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1847' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2030' />


    

</div>
<!--container01 close-->

  <div class="cl"></div>
       <div class="pages">
           <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-05.jsp">Previous</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan-40.jsp">1</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-02.jsp">2</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-03.jsp">3</a>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-04.jsp">4</a>

           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-05.jsp">5</a>
           <span class="pages_link">6</span>
           <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-07.jsp">7</a>
           <a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-07.jsp">Next</a>
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

