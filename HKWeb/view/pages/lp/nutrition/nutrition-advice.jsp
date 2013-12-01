<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>How to Lose Weight? How to Gain Weight Fast? Nutrition Advice for Every One!</title>
		<link href="default.css" rel="stylesheet" type="text/css" />
		<%@include file="/includes/_taglibInclude.jsp" %>
	</head>

	<body>
		<div id="wraper">
			<div id="header">
				<a href="${pageContext.request.contextPath}/"><img class="logo" src="images/logo_hkart.png" /></a>
				<p class="slogen"> the one stop shop for health, fitness and beauty</p>

				<p class="customer"> Customer Care: +91 124 4616444</p>
				<div class="cl"></div>
				<!-- header close -->
			</div>

			<div class="main_banner">
				<div class="advice_box">
					<img src="images/form_banner.png" />
					<div class="advice_form">
					<s:form beanclass="com.hk.web.action.core.user.RequestCallbackAction">
							<label for="name">Name</label>
							<s:text name="name"/>

							<label for="mobile">Mobile</label>
							<s:text name="mobile" maxlength="12"/>

							<label for="email">Email</label>
							<s:text name="email"/>
          						<s:hidden name="srcUrl" value="${pageContext.request.requestURL}"/>
          <s:hidden name="topLevelCategory" value="nutrition"/>
				<s:submit value="Request Consultation" name="requestConsultation" class="submit" />
				</s:form>
					</div>

				</div>



				<!-- main_banner close -->
			</div>




			<img src="images/Strip.jpg" />

			<div class="contant01">
				<img src="images/image_01.jpg" />
				<div class="txt_cont">
					<div class="txt_big">GAIN LEAN MUSCLE </div>
					<p>You have always wanted that chiselled look. You have tested and tried different exercises, supplements and different gyms too. However, your dream is still far away. Our expert health consultants are just a click away. Just give your age, weight and height specifications and you will resume your journey. </p>
					<div class="point_box">
						<h2>What you can expect: </h2>
						<ul>
							<li>Tips on gaining lean muscle</li>
							<li>Personalized approach to building and maintaining muscle mass</li>
							<li>Customized diet charts (while giving out any diet plan, medical conditions if any is also kept in consideration)</li>
						</ul>
					</div>
					<!--txt_cont close -->
				</div>
				<div class="cl"></div>
				<div class="divider"></div>
				<!--contant01 close -->
			</div>
			<div class="cl"></div>



			<div class="contant01">
				<img src="images/image_02.jpg" />
				<div class="txt_cont">
					<div class="txt_big">LOSE STUBBORN FAT </div>
					<p>Having trouble getting in your old jeans? Get help from Healthkart which is just a click away. Our weight loss consultants will give you the right solution for all your weighty issues. Now you do not have to worry about what to eat and what to leave. You will get the right meal plan charted to suit your age, weight, height & taste! Not just that, if you have any medical history, that would be taken into consideration as well. </p>
					<div class="point_box">
						<h2>We help you with the following:</h2>
						<ul>
							<li>Tips on weight loss</li>
							<li>What exercise to do</li>
							<li>Special diet chart depending on your medical condition</li>
						</ul>
					</div>
					<!--txt_cont close -->
				</div>
				<div class="cl"></div>
				<div class="divider"></div>
				<!--contant01 close -->
			</div>
			<div class="cl"></div>


			<div class="contant01">
				<img src="images/image_03.jpg" />
				<div class="txt_cont">
					<div class="txt_big">MANAGE YOUR WEIGHT </div>
					<p>Your weight is something you just cannot ignore. However in today's extremely stressful lifestyle, weight management is a daunting task. Now at Healthkart, you have the privilege of getting all your weight related queries answered. Our diet consultants would be happy to assist you in every possible way by charting out meal plans for you based on your height, weight, age, health conditions. Feel free to call at our customer care number and avail benefits to get back in shape. If you are aiming for size zero or want to become hit 10% body fat, we have the perfect solution for you. </p>
					<div class="point_box">
						<h2>We help you with the following: </h2>
						<ul>
							<li>Focus on your overall health</li>
							<li>Guidance about what food to take</li>
							<li>Things to avoid eating</li>
						</ul>
					</div>
					<!--txt_cont close -->
				</div>
				<div class="cl"></div>
				<div class="divider"></div>
				<!--contant01 close -->
			</div>
			<div class="cl"></div>

			<p class="footer"> © 2013 healthkart.com </p>




			<!-- wrapper close -->
		</div>
	</body>
</html>
