<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="HealthKart Diabetes Management Programme">

<s:layout-component name="htmlHead">
	<style type="text/css">
		.cl {
			clear: both;
		}

		#managementContainer {
			margin: 15px auto;
			width: 960px;
			overflow: auto;
			clear: both;
			padding-bottom: 200px;
		}

		.productThumb {
			width: 250px;
			margin-bottom: 25px;
			margin-top: 15px;
			margin-left: 10px;
			margin-right: 10px;
			padding: 20px;
			float: left;
			border: 1px solid #efefef;
			overflow: auto;
			background: #fff;
		}

		.productThumb p {
			margin: 0;
			padding: 0;
		}

		.productThumb .tThumb {
			display: table-cell;
			text-align: center;
			width: 250px;
			margin-right: 23px;
			clear: both;
			padding-bottom: 10px;
			height: 170px;
		}

		.productThumb .tThumb img {
			text-align: center;
		}

		.productThumb .tNum {
			background: url(images/number.gif) no-repeat;
			float: left;
			width: 27px;
			height: 27px;
			font-size: 16px;
			color: #785500;
			margin-right: 15px;
			text-align: center;
			line-height: 27px;
		}

		.productThumb h3 {
			font-size: 14px;
			margin-bottom: 10px;
			font-weight: normal;
			height: 40px;
		}

		.productThumb h3 a {
			font-size: 14px;
			color: #505050;
			border: none;
		}

		.productThumb h3 a:hover {
			color: #000;
		}

		.productThumb .tDesc {
			font-size: 13px;
			background: #f0f0f0;
			border: 1px solid #d9d9d9;
			width: 214px;
			height: 85px;
			float: right;
			padding: 5px;
			margin-bottom: 35px;
			line-height: 14px;
		}

		.productThumb .tPrice {
			font-size: 16px;
			clear: right;
			color: #2484c6;
			margin-bottom: 10px;
		}

		.productThumb .tPrice span {
			font-size: 12px;
			color: #a1a1a1;
			text-decoration: line-through;
			margin-right: 15px;
		}

		.productThumb .tSave {
			font-size: 16px;
			color: #e73721;
			font-weight: bold;
			height: 30px;
		}

		.productThumb .tBuy {
			margin-top: 10px;
		}

		.productThumb a {
			border: none;
		}

		.dbm {
			position: relative;
			background: #efefef;
			font-size: 15px;
			padding-left: 10px;
			margin-top: 20px;
			line-height: 28px;
		}

		.dbm span {
			position: absolute;
			right: 10px;
			font-size: 12px;
		}

		.dbm span a {
			color: #2484c6;
		}
	</style>

</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'>
		<s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt;
		<span class="crumb last" style="font-size: 12px;">Diabetes Management Programme</span>

		<h1 class="title">
			HealthKart Diabetes Management Programme
		</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">HealthKart Diabetes Management Programme</s:layout-component>
<s:layout-component name="metaKeywords">HealthKart Diabetes Management Programme</s:layout-component>

<s:layout-component name="content">
<div style="padding-bottom:20px"><img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/banner.jpg"/></div>
<div class="main_container" style="background-color:#FFFFFF;">

<div id="managementContainer">

<div style="padding:10px;">
<h2>Who is this Program for?</h2>

<p>The HealthKart Diabetes Management Program is targeted at people who are on medication for diabetes, heart problems, high cholesterol or thyroid disorders. These people are prone to complications related to kidney disease, nerve disorders, foot problems, paralytic strokes if their diabetes or cholesterol are not controlled or monitored adequately. The Program helps them manage their condition in a cost effective manner.</p>

<h2>What is this Program?</h2>

<p>Based on your prescription profile, you can choose one of the HealthKart Packages. HealthEssential, HealthVital or HealthUltima. Each package will provide you the required medication, diagnostics and nutritional support required for your condition for 3 months plus a host of other benefits.</p>

<h2>What does the Package include?</h2>

<p>HealthEssential, HeathVital or HealthUltima. Each Package will provide you the required medication and diagnostics required for your condition for 3 months plus a host of other benefits. These have been thoughtfully included to incorporate allied services you might require over the period of three months.</p>

<h2>What does this not include?</h2>

<p>This does not include cost of insulins, DPP IV or injectable cardiovascular medicines</p>

<h2>What is the first time bonus offer?</h2>

<h5><br/>A best in class Glucose monitoring Meter offered for those joining the first time! Is there a cost bebefit?</h5>
<p><strong>Definitely!</strong> On an average people spend Rs. 1200-1500 per month (Rs. 40/day on an average) on their medication. The cost per day in this package will be Rs. 40/ Rs.50/ Rs. 60 per day. You get a lot more at approximately the same price!</p>
<ul>
  <li>- A Blood Glucose meter with strips alone would cost you > Rs. 2500</li>
  <li>- 20% discounts on doctor consultations at Apollo Clinics</li>
  <li>- Personalized Diet and Counseling calls</li>
</ul>
<p>If your therapy is changed by the doctor, you are saved against the fluctuations in cost.</p>

<h2>Yes! i am interested, What is necessary to join in?</h2>

<ul>
  <li>1. Prescription of the Doctor</li>
  <li>2. Registration and Signing of the Consent Form</li>
  <li>3. Payment</li>
</ul>
</div>

<div class="cl"></div>
<h2 class="dbm">HealthKart Added Value Packages<span><a
		href="${pageContext.request.contextPath}/home-devices/blood-pressure/bp-monitor?brand=Omron"></a></span></h2>

<div class="productThumb">

	<h3><a href="${pageContext.request.contextPath}/product/health-essential/HK-DMP-001">HealthKart DMP - HealthEssential</a></h3>

	<p>4 Therapy Products: One Thyrocare BioT Test, 25 Monitoring Strips, 2 Counseling Call, Blood Glucose Meter
	   free</p>

	<p class="tPrice">
		Rs 3,500
	</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/health-essential/HK-DMP-001"><img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif"
	                                 width="89" height="26" alt=""/></a></p>
</div>
<div class="productThumb">

	<h3><a href="${pageContext.request.contextPath}/product/health-vital/HK-DMP-002">HealthKart DMP - HeathVital</a></h3>

	<p>5 Therapy Products+ 1 Nutritional Supplement: One Thyrocare BioT Test, 25 Monitoring Strips, 2 Counseling Call,
	   Blood Glucose Meter free</p>

	<p class="tPrice">

		Rs 4,500
	</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/health-vital/HK-DMP-002"><img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif"
	                                 width="89" height="26" alt=""/></a></p>
</div>

<div class="productThumb">

	<h3><a href="${pageContext.request.contextPath}/product/health-ultima/HK-DMP-003">HealthKart DMP - HealthUltima</a></h3>

	<p>7 Therapy Products+ 2 Nutritional Supplement: One Thyrocare BioT Test, 25 Monitoring strips, 2 Counseling Call,
	   Blood Glucose Meter free</p>

	<p class="tPrice">
		Rs 5,500
	</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/health-ultima/HK-DMP-003"><img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif"
	                                 width="89" height="26" alt=""/></a></p>
</div>

<div class="cl"></div>
<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/strip4.jpg" width="960" height="80" alt="offer1"/>

<div class="cl"></div>
<h2 class="dbm">Special Offers for Abbott Employees<span><a
		href="${pageContext.request.contextPath}/home-devices/blood-pressure/bp-monitor?brand=Omron"></a></span></h2>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/DM033.jpg" width="208" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/freestyle-optium-glucometer/DM033">FreeStyle Optium
	                                                                                           Glucometer</a></h3>


	<p class="tPrice">
		Rs 2,199
	</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/freestyle-optium-glucometer/DM033"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>


<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/DS038.jpg" width="201" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/freestyle-optium-strips/DS038">FreeStyle Optium Strips </a>
	</h3>


	<p class="tPrice">

		Rs 350
	</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/freestyle-optium-strips/DS038"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>
<div class="cl"></div>
<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/strip3.jpg" width="960" height="80" alt="offer2"/>

<div class="cl"></div>
<h2 class="dbm">Omron BP Monitor<span><a
		href="${pageContext.request.contextPath}/home-devices/blood-pressure/bp-monitor?brand=Omron">See More</a></span>
</h2>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/hem-7111.jpg" width="218" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/omron-bp-monitor-upper-arm-hem-7111/HB004">Omron BP Monitor
	                                                                                                   Upper Arm
	                                                                                                   (HEM-7111)</a>
	</h3>


	<p class="tPrice">
		<span>Rs 2,580</span>
		Rs 1,169
	</p>

	<p class="tSave">You Save 55%</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/omron-bp-monitor-upper-arm-hem-7111/HB004"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>


<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/hem-7203.jpg" width="221" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/omron-bp-monitor-upper-arm-hem-7203/HB005">Omron BP Monitor
	                                                                                                   Upper Arm
	                                                                                                   (HEM-7203)</a>
	</h3>


	<p class="tPrice">
		<span>Rs 3,280</span>
		Rs 1,930
	</p>

	<p class="tSave">You Save 41%</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/omron-bp-monitor-upper-arm-hem-7203/HB005"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/hem-7101.jpg" width="227" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/omron-bp-monitor-upper-arm-hem-7101/HB041">Omron BP Monitor
	                                                                                                   Upper Arm
	                                                                                                   (HEM-7101)</a>
	</h3>


	<p class="tPrice">
		<span>Rs 2,700</span>
		Rs 1,449
	</p>

	<p class="tSave">You Save 46%</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/omron-bp-monitor-upper-arm-hem-7101/HB041"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="cl"></div>


<h2 class="dbm">Splenda</h2>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/nut-103.jpg" width="170" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/splenda-tablets/NUT103">Splenda Tablets</a></h3>


	<p class="tPrice">

		Rs 70
	</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/splenda-tablets/NUT103"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>


<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/swt-001.jpg" width="170" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/splenda-granulated/SWT001">Splenda Granulated </a></h3>


	<p class="tPrice">Rs 125</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/splenda-granulated/SWT001"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/swt-002.jpg" width="170" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/splenda-packets/SWT002">Splenda Packets </a></h3>


	<p class="tPrice">

		Rs 75
	</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/splenda-packets/SWT002"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="cl"></div>


<div class="cl"></div>
<h2 class="dbm">Diabetic Care<span><a
		href="${pageContext.request.contextPath}/nutrition/condition-specific-supplements/blood-sugar">See
                                                                                                       More</a></span>
</h2>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/nut-1107.jpg" width="121" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/vitamin-shoppe-advanced-sugar-support/NUT1107">Vitamin
	                                                                                                       Shoppe
	                                                                                                       Advanced
	                                                                                                       Sugar
	                                                                                                       Support </a>
	</h3>


	<p class="tPrice">
		<span>Rs 2,299 </span>
		Rs 2,069
	</p>

	<p class="tSave">You Save 10%</p>

	<p class="tBuy"><a
			href="${pageContext.request.contextPath}/product/vitamin-shoppe-advanced-sugar-support/NUT1107"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>


<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/nut-1079.jpg" width="170" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/vitamin-shoppe-fenugreek-extract/NUT1079">Vitamin Shoppe
	                                                                                                  Fenugreek
	                                                                                                  Extract </a></h3>


	<p class="tPrice">
		<span>Rs 499</span>
		Rs 449</p>

	<p class="tSave">You Save 10%</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/vitamin-shoppe-fenugreek-extract/NUT1079"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/nut-689.jpg" width="198" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/glucerna-sr/NUT689">Glucerna SR</a></h3>


	<p class="tPrice">
		<span>Rs 550</span>
		Rs 522
	</p>

	<p class="tSave">You Save 5%</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/glucerna-sr/NUT689"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="cl"></div>
<h2 class="dbm">Diabetic Food<span><a
		href="${pageContext.request.contextPath}/nutrition/specialty-nutrition/diabetic-food">See More</a></span></h2>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/nut-109.jpg" width="169" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/dana-diabetic-jam-danish/NUT109">Dana Diabetic Jam
	                                                                                         (Danish)</a></h3>


	<p class="tPrice">Rs 210 </p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/dana-diabetic-jam-danish/NUT109"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>


<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/nut-107.jpg" width="170" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/sugarless-bliss-sugar-free-cookies/NUT107">Sugarless Bliss
	                                                                                                   Sugar Free
	                                                                                                   Cookies </a></h3>


	<p class="tPrice">

		Rs 91</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/sugarless-bliss-sugar-free-cookies/NUT107"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/swt-005.jpg" width="170" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/golightly-sugarfree-chocolate-mint-candy/SWT005">Golightly
	                                                                                                         Sugarfree
	                                                                                                         Chocolate
	                                                                                                         Mint
	                                                                                                         Candy</a>
	</h3>


	<p class="tPrice">

		Rs 140</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a
			href="${pageContext.request.contextPath}/product/golightly-sugarfree-chocolate-mint-candy/SWT005"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>


<div class="cl"></div>
<h2 class="dbm">Diabetic Foot Care<span><a href="${pageContext.request.contextPath}/diabetes/foot-care">See
                                                                                                        More</a></span>
</h2>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/FCDIA006.jpg" width="113" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/tynor-full-silicon-insole-k01/FCDIA006">Tynor Full Silicon
	                                                                                                Insole(K01)</a></h3>


	<p class="tPrice">

		Rs 715
	</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/tynor-full-silicon-insole-k01/FCDIA006"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>


<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/DIASOC001.jpg" width="171" height="170"/>
	</p>

	<h3><a href="${pageContext.request.contextPath}/product/amron-feather-diabetic-socks/DIASOC001">Amron Feather
	                                                                                                Diabetic Socks</a>
	</h3>


	<p class="tPrice">

		Rs 1,100</p>

	<p class="tSave">&nbsp;</p>

	<p class="tBuy"><a href="${pageContext.request.contextPath}/product/amron-feather-diabetic-socks/DIASOC001"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="productThumb">
	<p class="tThumb">
		<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/FC044.jpg" width="170" height="170"/></p>

	<h3><a href="${pageContext.request.contextPath}/product/zori-orthotic-slippers-unisex-red-white/FC044">Zori Orthotic
	                                                                                                       Slippers
	                                                                                                       Unisex (Red
	                                                                                                       &amp;
	                                                                                                       White) </a>
	</h3>


	<p class="tPrice">
		<span>Rs 2,750 </span>
		Rs 2,612
	</p>

	<p class="tSave">You Save 5%</p>

	<p class="tBuy"><a
			href="${pageContext.request.contextPath}/product/zori-orthotic-slippers-unisex-red-white/FC044"><img
			src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26"
			alt=""/></a></p>
</div>

<div class="cl"></div>

<!-- Management Container Ends-->
</div>


</div>

</s:layout-component>


</s:layout-render>

