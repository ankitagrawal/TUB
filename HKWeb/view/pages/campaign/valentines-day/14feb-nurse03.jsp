<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
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
    
    <img class="main-banner" src="${pageContext.request.contextPath}/images/14feb/Hot-nurse.jpg"/>
<div class="cl"></div>

   <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB010' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB011' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB012' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB013' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB014' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB015' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB016' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB017' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB018' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB019' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB020' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB021' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RB022' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SENERA6' />



         <div class="cl"></div>
		<div class="pages">
            <a class="pages_link"  href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-nurse02.jsp">Previous</a>

			<a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-nurse.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/campaign/valentines-day/14feb-nurse02.jsp">2</a>
            <span class="pages_link">3</span>

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

