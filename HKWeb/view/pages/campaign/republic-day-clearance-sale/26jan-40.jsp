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


<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPTER2' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPTER3' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT885' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT886' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1213' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB002' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI001' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB035' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK009' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI002' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD26' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD34' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MKRI11' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MKRI14' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEOV21' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT717' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT053' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT104' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1143' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT761' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT891' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT900' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT797' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1484' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1711' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT692' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1477' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1482' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1472' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT893' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1492' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1493' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1494' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1103' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT799' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1135' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1497' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1700' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1712' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1722' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1724' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1725' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK008' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK010' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RK015' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RZ002' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GO003' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1455' />


    

</div>
<!--container01 close-->

      <div class="cl"></div>
		<div class="pages">
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-03.jsp">3</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-04.jsp">4</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-05.jsp">5</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-06.jsp">6</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/26jan40-07.jsp">7</a>
			<a class="pages_link"  href="${pageContext.request.contextPath}/pages/26jan40-02.jsp">Next</a>
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

