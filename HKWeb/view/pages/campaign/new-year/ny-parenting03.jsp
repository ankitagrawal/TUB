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





<img src="${pageContext.request.contextPath}/images/ny2013/parenting01.jpg" />

    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">2</a>
            <span class="pages_link">3</span>
        </div>
<div class="cl"></div>

<!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/potty-train.png"/>
    <h2 class="txt01">Potty Train Your Baby</h2>
<p>Ease the transition to potty training with quality diapers, nappies, gentle wipes and creams. PS: Studies reveal that babies with successful potty training are more likely to become Engineers or Doctors or Scientists! This writer now suddenly a great excuse to counter his parents’ laments.</p> 
</div>

<div class="cl"></div>

    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2067' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1961' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2010' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB276' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB808' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2222' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/protect-child.png"/>
    <h2 class="txt01">Protect your child from illness</h2>
<p>The only thing worse than your baby getting sick is when she gets sick all of a sudden. Here’s how you offset such moments of panic; you arm yourself with digital thermometers, herbal teas for toddlers, nebulizers for kids that dislike medicines and more.</p>
</div>

<div class="cl"></div>

    
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1908' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HT004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HT006' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HT007' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OPT001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRN1' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB141' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED004' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB142' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN001' />
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUTCMB97' />


</div>
<!--container01 close-->


    <!--container01 start-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/send-child-to-school.png"/>
    <h2 class="txt01">Send Your Child to School. Happily. Everyday.</h2>
<p>Make going to school a happy & joyful affair for your little devil, with spiffy lunch boxes, classy bags, impressive water bottles, with healthy and tasty breakfast options like cereals and multi grain shakes.</p>
</div>

<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1128'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1519'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1512'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT505'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1284'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT684'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1315'/>    
</div>
<!--container01 close-->

<div class="container01">


<div class="contant01">
<img src="${pageContext.request.contextPath}/images/ny2013/play-and-learn.png"/>
    <h2 class="txt01">Make Your Child Learn by Playing!</h2>
<p>Research has proved that playing makes your child smarter and less shy. The earlier they start, the better. We have these nice numbers that we think will make your little devil get curious and develop hand eye co-ordination faster.</p>
</div>
<div class="cl"></div>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2184'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1268'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1275'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1284'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1312'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1313'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1903'/>
    <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1902'/>        
</div>
<!--container01 close-->
    <div class="cl"></div>
		<div class="pages">
	         <a class="next"  href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">← Previous</a>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting.jsp">1</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/ny-parenting02.jsp">2</a>
            <span class="pages_link">3</span>
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

