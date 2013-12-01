<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
	boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Happy New Year 2013">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/ny2013.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Cyber
		Monday</span>

		<h1 class="title">Happy New Year 2013</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Happy New Year 2013</s:layout-component>
<s:layout-component name="metaKeywords">Happy New Year 2013</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->
<jsp:include page="../includes/_menuNY2013.jsp"/>

<div id="wrapper">



<div class="logo">
<a href="http://www.healthkart.com"><img src="${pageContext.request.contextPath}/images/ny2013/logo.jpg" />  </a>
</div>





<img src="${pageContext.request.contextPath}/images/ny2013/grooming01.jpg" />

<div class="cl"></div>
		<div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">3</a>
            <span class="pages_link">4</span>
         </div>
<div class="cl"></div>



<!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/reduce-chapped-and-broken-skin.png"/>
    <h2 class="txt01">Reduced Chapped Skin</h2>
<p>For a skin that deserves all the pampering, nothing works better than the wholesome luxury of handcrafted recreational skin butters and bath foams. From lining your lips for a seriously head-turning pout to scrubbing off dead scales of skin, here is a class of skin elixirs you cannot say no to.</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR34' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY300' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY305' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY309' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY311' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT12' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KHADI29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL39' />    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA15' />    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PF010' />    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT22' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VLCCFRT69' />    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNTPR100' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG06' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BBP28' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PNOG29' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MYBLN37' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEAT005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FABIN65' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE35' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR31' />       
</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/light-underarms.png"/>
    <h2 class="txt01">Remove Dark Undearms</h2>
<p>Being a Sith Lord has it's own travails. Especially, when everything starts to turn dark. We hate dark underarms as much as you do and here's how you can get rid of dark underarms without becoming a silly Jedi!</p>
</div>

<div class="cl"></div>
    
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY302' />
<s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY310' />
    

</div>
<!--container01 close-->


<!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/dump-spectacles.png"/>
    <h2 class="txt01">Say No To Specs</h2>
<p>Specs are a thing of the past. These days, people can talk to each other, face to face, without really being there. Unless you have cool steampunk specs or better still - a gold rim monocle, it would be wise to switch to contacts.</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE052' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE054' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE056' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE271' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE272' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE340' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE341' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE342' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE034' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE035' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE039' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE763' />
</div>
<!--container01 close-->

    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/shave.png"/>
    <h2 class="txt01">Become Aerodynamic</h2>
<p>In the winters, the wind is the super hero's true enemy. It turns a whisker or any other bit of facial hair into full blown menace, hampers speed and often prevents one from breaking the sound barrier. If only one could remove facial hair and become smooth and pierce through the air, leaving behind a whiff of musk. Not anymore…<p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY256' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GAT17' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GILI15' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GLD16' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MSH005' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MSH008' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NIVEA10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OSSG02' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG015' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG017' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG018' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG021' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PG028' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI05' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEDIC39' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET10' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET4' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEET5' />


</div>
<!--container01 close-->


<div class="cl"></div>
		<div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming02.jsp">2</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-grooming03.jsp">3</a>
            <span class="pages_link">4</span>
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

