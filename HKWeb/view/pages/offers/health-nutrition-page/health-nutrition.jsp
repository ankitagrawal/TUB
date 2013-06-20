<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="personal-care Offer">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/health-nutrition.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">health-nutrition Offers</span>

		<h1 class="title">health-nutrition Offers</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">health-nutrition Offers</s:layout-component>
<s:layout-component name="metaKeywords">health-nutrition Offers</s:layout-component>

<s:layout-component name="content">
    <div class="container">
    	<div class="nRow">
        	<div class="cquery-ALL" >
    		<ul>
    		  <li><a href="http://www.healthkart.com/health-nutrition/weight-management"><img src="images/banner1.jpg" alt="Weight Management - If you don't dare to begin, you don't stand a chance of getting there ~ Anonymous" /></a></li>
    		  <li><a href="http://www.healthkart.com/health-nutrition/shop-by-need/sexual-wellness"><img src="images/banner2.jpg" alt=" The reason people sweat is that they won't catch fire making love ~ Don Rose" /></a></li>
    		  <li><a href="http://www.healthkart.com/health-nutrition/shop-by-need/prenatal-care"><img src="images/banner3.jpg" alt="I feel blessed to be having a really easy pregnancy" /></a></li>
    		</ul>
    	</div>
        </div>

        <div class="nRow">&nbsp;</div>
      <div class="firstThreeBanners">
        &nbsp;
        <div class="btn1"><a href="http://www.healthkart.com/health-nutrition/shop-by-need/hair-skin-nails"><img src="images/btn1.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>
        <div class="btn2"><a href="http://www.healthkart.com/health-nutrition/shop-by-concern/heart-support"><img src="images/btn2.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>
        <div class="btn3"><a href="http://www.healthkart.com/health-nutrition/shop-by-concern/fertility-support"><img src="images/btn3.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>

      </div>
        <div class="clr"></div>
        <div class="secondThreeBanners">&nbsp;
        <div class="btn4"><a href="http://www.healthkart.com/health-nutrition/shop-by-need/children"><img src="images/btn4.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>
        <div class="btn5"><a href="http://www.healthkart.com/health-nutrition/shop-by-need/post-pregnancy"><img src="images/btn5.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>
        <div class="btn6"><a href="http://www.healthkart.com/health-nutrition/shop-by-concern/arthritis-support"><img src="images/btn6.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>
        </div>
        <div class="clr"></div>
        <div class="lastThreeBanners">&nbsp;
        <div class="btn7"><a href="http://www.healthkart.com/health-nutrition/weight-management"><img src="images/btn7.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>
        <div class="btn8"><a href="http://www.healthkart.com/health-nutrition/shop-by-need/sexual-wellness"><img src="images/btn8.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>
        <div class="btn9"><a href="http://www.healthkart.com/health-nutrition/shop-by-need/prenatal-care"><img src="images/btn9.gif" alt="GET IT NOW!" title="GET IT NOW!" /></a></div>
        </div>
    </div>

        <script src="js/jquery.js" type="text/javascript" ></script>
        <script src="js/jquery.easing.1.3.js" type="text/javascript" ></script>
        <script src="js/cquery_all_slide.js" type="text/javascript"></script>


    <script>
    	$.fn.cquery_allslide({
    		tabIn:0,
    		cssID:5,
    		width:'930',
    		sideButtons:false,
    		controllButtons:true,
    		direction:'left',
    		autoplay:true
    	});
    	</script>

    <c:if test="${not isSecure }">
    	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
    	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
    </c:if>

    </s:layout-component>
    </s:layout-render>
