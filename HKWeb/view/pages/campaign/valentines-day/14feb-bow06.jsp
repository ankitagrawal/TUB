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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/clooney-set.jpg"/>
<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG027' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG029' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI04' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI26' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI27' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC13' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC30' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC31' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNSNC32' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRM1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRM2' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRM3' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRVLY03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK01' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK03' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK07' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK08' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK09' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK14' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RBK18' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT11' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RMGT12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKOF42' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKOF43' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKOF45' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKOF46' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKOF47' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SKOF53' />
   
    
   




      <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow05.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-bow05.jsp">5</a>
            <span class="pages_link">6</span>

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

