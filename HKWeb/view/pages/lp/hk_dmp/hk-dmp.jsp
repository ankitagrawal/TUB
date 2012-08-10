<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="HealthKart Diabetes Management Programme">

	<s:layout-component name="htmlHead">
		<style type="text/css">
			.cl {
				clear: both;
			}

			#feedingContainer {
				margin: 15px auto;
				width: 960px;
				overflow: auto;
				clear: both;
				padding-bottom: 250px;
			}

			.leftCol {
				width: 465px;
				float: left;
				margin-top: 35px;
			}

			.rightCol {
				width: 465px;
				float: right;
				margin-top: 35px;
			}

			.productThumb {
				margin-bottom: 25px;
				padding: 20px;
				float: left;
				border: 1px solid #efefef;
				width: 423px;
				overflow: auto;
				background: #fff;
			}

			.productThumb p {
				margin: 0;
				padding: 0;
			}

			.productThumb .tThumb {
				float: left;
				margin-right: 23px;
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
		</style>

	</s:layout-component>

	<s:layout-component name="breadcrumbs">
		<div class='crumb_outer'>
			<s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
			&gt;
			<span class="crumb last" style="font-size: 12px;">HealthKart Diabetes Management Programme</span>

			<h1 class="title">
				HealthKart Diabetes Management Programme
			</h1>
		</div>

	</s:layout-component>

	<s:layout-component name="metaDescription">HealthKart Diabetes Management Programme</s:layout-component>
	<s:layout-component name="metaKeywords"></s:layout-component>

	<s:layout-component name="content">
		<div id="feedingContainer">
			<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/banner.gif" width="960" height="220" alt=""/>

			<div class="cl"></div>
			<!-- Left Column Starts-->
			<div class="leftCol">
				<div class="productThumb">
					<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/pix.gif" width="170" height="170" class="tThumb"/>

					<h3><a href="http://staging01.healthkart.com/product/healthkart-dmp-card-silver-quarterly-plan/HK-DMP-CARD-SILVER">HealthKart DMP Card -
					                                                                            Silver / Quarterly
					                                                                            Plan</a></h3>


					<p class="tPrice">
						<span>Rs 5,000</span>
						Rs 3,000
					</p>

					<p class="tSave">You Save 40%</p>

					<p class="tBuy"><a
							href="http://staging01.healthkart.com/product/healthkart-dmp-card-silver-quarterly-plan/HK-DMP-CARD-SILVER"><img
							src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26" alt="Buy Now"/></a></p>
				</div>


			</div>
			<!-- Left Column Ends-->

			<!-- Right Column Starts-->
			<div class="rightCol">


				<div class="productThumb">
					<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/pix.gif" width="170" height="170" class="tThumb"/>

					<h3><a href="http://staging01.healthkart.com/product/healthkart-dmp-card-silver-quarterly-plan/HK-DMP-CARD-SILVER">HealthKart DMP Card -
					                                                                            Gold / Quarterly
					                                                                            Plan</a></h3>


					<p class="tPrice">
						<span>Rs 5,500</span>
						Rs 3,500
					</p>

					<p class="tSave">You Save 36%</p>

					<p class="tBuy"><a
							href="http://staging01.healthkart.com/product/healthkart-dmp-card-silver-quarterly-plan/HK-DMP-CARD-SILVER"><img
							src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26" alt="Buy Now"/></a></p>
				</div>
			</div>
			<div class="cl"></div>
			<!-- Right Column Ends-->

			<div class="cl"></div>
			<!-- Left Column Starts-->
			<div class="leftCol">
				<div class="productThumb">
					<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/pix.gif" width="170" height="170" class="tThumb"/>

					<h3><a href="http://staging01.healthkart.com/product/healthkart-dmp-card-silver-quarterly-plan/HK-DMP-CARD-SILVER">HealthKart DMP Card -
					                                                                            Silver / Half Yearly
					                                                                            Plan</a></h3>


					<p class="tPrice">
						<span>Rs 6,500</span>
						Rs 4,500
					</p>

					<p class="tSave">You Save 31%</p>

					<p class="tBuy"><a
							href="http://staging01.healthkart.com/product/healthkart-dmp-card-silver-quarterly-plan/HK-DMP-CARD-SILVER"><img
							src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26" alt="Buy Now"/></a></p>
				</div>


			</div>
			<!-- Left Column Ends-->

			<!-- Right Column Starts-->
			<div class="rightCol">


				<div class="productThumb">
					<img src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/pix.gif" width="170" height="170" class="tThumb"/>

					<h3><a href="http://staging01.healthkart.com/product/healthkart-dmp-card-silver-quarterly-plan/HK-DMP-CARD-SILVER">HealthKart DMP Card -
					                                                                            Gold / Half Yearly
					                                                                            Plan</a></h3>


					<p class="tPrice">
						<span>Rs 7,500</span>
						Rs 5,000
					</p>

					<p class="tSave">You Save 33%</p>

					<p class="tBuy"><a
							href="http://staging01.healthkart.com/product/healthkart-dmp-card-silver-quarterly-plan/HK-DMP-CARD-SILVER"><img
							src="${pageContext.request.contextPath}/pages/lp/hk_dmp/images/buynow.gif" width="89" height="26" alt="Buy Now"/></a></p>
				</div>
			</div>
			<div class="cl"></div>
			<!-- Right Column Ends-->
		</div>

	</s:layout-component>


</s:layout-render>

