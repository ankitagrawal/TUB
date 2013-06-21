<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Beauty Offers">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/bty-offers.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Beauty Offers</span>

		<h1 class="title">Beauty Offers</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Beauty Offers</s:layout-component>
<s:layout-component name="metaKeywords">Beauty Offers</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->
 <div id="wrapper">
     <a class="banners" href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris"><img src="${pageContext.request.contextPath}/images/bty-offers/banner01.jpg"/></a>
     <a class="banners" href="http://www.healthkart.com/brand/beauty/maybelline"><img src="${pageContext.request.contextPath}/images/bty-offers/banner02.jpg"/></a>
     <a class="banners04" href="http://www.healthkart.com/brand/beauty/NYX"><img src="${pageContext.request.contextPath}/images/bty-offers/banner3.jpg"/></a>

     <div class="cl"></div>
     <div class="offers">



           <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE Manicure Kit With Purchase Of L'oreal Paris Skin Care and Make Up Worth Rs 2200/-</a></p>
         <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE L'oreal Paris Glamshine 6h Lipgloss Worth Rs 875/- With Purchase Of L’oreal Paris Skin Care and Make Up Worth
             </br> Rs 3000/-</a></p>
         <p><a href="http://www.healthkart.com/brand/beauty/L%27Oreal+Paris">FREE Trousseau Box With Purchase Of L’oreal Paris Skin and Make Up Worth Rs 2500/-</a></p>

         <p><a href="http://www.healthkart.com/brand/beauty/NYX">FREE NYX Auto Eye Pencil Worth  Rs 225/- With Purchase of NYX Products Worth Rs 1000/-</a></p>

         <p><a href="http://www.healthkart.com/brand/beauty/VLCC">FREE VEGA Set of 7 Make-up Brushes Worth Rs 450/- With Purchase  of VLCC Products Worth Rs 1500/-</a></p>
         <p><a href="http://www.healthkart.com/brand/beauty/Lotus+Herbals">FREE Lotus Herbals Jojobawash Active Milli Capsules Nourishing Face Wash Worth Rs 75/- With Purchase  of Lotus Herbals Products Worth Rs 500/-</a></p>
         <p><a href="http://www.healthkart.com/search?query=Schwarzkopf&search.x=-815&search.y=-54">FREE Vega Premium Collection  Straightening Hair Brush Worth Rs 450/- With Purchase  of Schwarzkopf Products Worth
             </br> Rs 1500/-</a></p>


         

       <p><a href="http://www.healthkart.com/beauty">FREE Faces Emery Boards Small Worth Rs.65/- With Purchase of Beauty products Worth Rs 500/- (Prepaid Order) </a></p>

       <p><a href="https://www.healthkart.com/brand/beauty/shahnaz+husain">FREE  Revlon Touch & Glow Advanced Fairness Daily Moisturising Cream SPF 15 Worth Rs 180/- With Purchase of  Shahnaz Hussain Products Worth Rs 1000/-</a></p>

       <p><a href="https://www.healthkart.com/brand/beauty/kaya+skin+clinic">FREE Basicare 24 Round Make-Up Sponges Value Pack Worth Rs 225/- With Purchase of Kaya Products Worth Rs 1000/- </a></p>

         <div class="cl"></div>

         <span>*Offers valid till stocks last.</span>





     </div>

     
    

 







</div>
<!--wrapper close-->


<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

</s:layout-render>

