<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Special Offers on World Diabetes Day">

<s:layout-component name="htmlHead">
  <style type="text/css">
		.cl { clear:both; }

@font-face {
				font-family: 'Esta';
				src: url('${pageContext.request.contextPath}/images/wdd/estaregular.eot');
				src: url('${pageContext.request.contextPath}/images/wdd/estaregular.eot?#iefix') format('embedded-opentype'),
						 url('${pageContext.request.contextPath}/images/wdd/estaregular.woff') format('woff'),
						 url('${pageContext.request.contextPath}/images/wdd/estaregular.ttf') format('truetype'),
						 url('${pageContext.request.contextPath}/images/wdd/estaregular.svg#estaregular') format('svg');
				font-weight: normal;
				font-style: normal;
		}

  .down	{ width:100%; clear:both;}
.clr	{ clear:both; height:1px; line-height:1px; font-size:1px;}

#outer	{ width:960px; clear:both; text-align:left; overflow:auto; padding-bottom:240px; margin:0 auto;}

.bgDiabetesHeader	{ background:url('${pageContext.request.contextPath}/images/wdd/bg-header.jpg') top left repeat-y; padding:30px 0px 30px 0px;}

.h1		{ font-size:70px; line-height:85px; color:#231f20; font-family:'Esta'; font-weight:normal; text-align:center; margin:0px; padding:0px;}
h3		{ font-size:30px; line-height:35px; color:#231f20; font-family:'Esta'; font-weight:normal; text-align:center; margin:0px; padding:0px;}

.dL		{ float:left; width:auto; padding:70px 0px 0px 200px; font-size:30px; line-height:35px; color:#231f20; font-family:'Esta'; font-weight:normal; text-align:center; }
.dM		{ float:left; width:auto; font-size:170px; padding:0px 7px 0px 5px; line-height:150px; color:#007dc5; font-weight:bold;}
.dR		{ float:left; width:auto;  padding:70px 10px 0px 0px; float:left;}

.line1	{ width:600px; clear:both; margin:0 auto; height:1px; line-height:1px; background:#949698;}


.ht1	{ height:20px; clear:both;}
.ht2	{ height:50px; clear:both;}

.des	{ font-family:'Esta'; font-size:19px; color:#231f20; line-height:24px;}
.pad1	{ padding:20px 100px 20px 100px;}
.pad2	{ padding:20px 200px 20px 200px;}
        </style>


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">World Diabetes Day</span>

    <h1 class="title">
     World Diabetes Day
    </h1>
  </div>
  
</s:layout-component>

<s:layout-component name="metaDescription">Special Offers on World Diabetes Day from HealthKart.com. Buy Now!</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>

<s:layout-component name="content">
<div id="outer">
	<div class="down">&nbsp;</div>
    <div class="down bgDiabetesHeader">
    	<div class="dL"><h3>World Diabetes Day</h3></div>
        <div class="dM">o</div>
        <div class="dR"><h3>Nov. 14th</h3></div>
        <div class="ht1">&nbsp;</div>
    	<h2 class="h1">JOIN the BLUE circle</h2>
        <h3>HealthKart Celebrates World Diabetes Day</h3>
      <div class="ht2">&nbsp;</div>
    </div>
    <div class="pad1 des" align="center">Diabetes in your loved ones causing deeper concerns? Bring back the balance in their life by cutting down glucose levels. Find yourself amid a breath of relief with exclusive diabetic combos which aid easy monitoring of blood sugar levels.
    </div>
    <div class="down">&nbsp;</div>
    <div class="line1">&nbsp;</div>
    <div class="ht2">&nbsp;</div>
    <div class="bgDiabetesHeader" align="center"><a href="http://www.healthkart.com/product/diacel:-keep-diabetic-complications-away-in-just-99-days/cmb-dia27" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/onetouch.jpg" border="0" /></a></div>
  <div class="pad2 des" align="center">With measured and contained blood sugar, diabetics can enjoy their meal consumption without having to compromise on a balanced diet, while keeping their condition under a vigil.</div>
    <div class="bgDiabetesHeader" align="center"><a href="http://www.healthkart.com/product/diacel:-keep-diabetic-complications-away-in-just-99-days/cmb-dia28" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/splenda.jpg" border="0" /></a></div>
  <div class="pad2 des" align="center">Entertain no compromises with your taste buds this World Diabetics Day.
Celebrate the joy of being diabetic and happy!</div>
    <div class="down">&nbsp;</div>
</div>

 <div class="cl"></div>

    <!-- Container Ends-->
  
</s:layout-component>


</s:layout-render>

