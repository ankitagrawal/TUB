<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Great Online Shopping Festival">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/gosf.css" rel="stylesheet" type="text/css"/>


</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'>
		<s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt;
		<span class="crumb last" style="font-size: 12px;">Cyber Monday</span>

		<h1 class="title">
			Great Online Shopping Festival
		</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Google Cyber Monday</s:layout-component>
<s:layout-component name="metaKeywords">Google Cyber Monday</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

<div id="pageContainer">
<img src="${pageContext.request.contextPath}/images/GOSF/main-banner.jpg" alt="gosf banner"/>

<div class="cl"></div>

<jsp:include page="../includes/_menuGosf.jsp"/>

<div class="cl"></div>


<div class="heading1">NUTRITION</div>
<div class="prodBoxes">
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT056'
                 productDesc='Get the best carb free, lactose free whey protein known to man. Suitable for bodybuilding & athletic training alike. '/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1000'
                 productDesc='Reduce cardio vascular risk & tissue damage with Vitamin E & Selenium. Free from artificial colors, preservatives & flavors. '/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1001'
                 productDesc='Boost liver function, cardio vascular health & reduce cholesterol with Vitamin Shoppe Lecithin Granules. '/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1005'
                 productDesc='Detoxifies, compliments your diet, and boosts energy levels '/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1008'
                 productDesc='The definitive source of Vitmain B5, it reduces stress and fatigue issues and also cures acne and blemishes.'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1010'
                 productDesc='Dietary supplement which is essential for healthier red blood cell metabolism, efficient nervous and immune system.'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1011'
                 productDesc='Excellent for maintaining nitrogen balance in body.'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1012'
                 productDesc='Enhances digestion of proteins and functioning of joints '/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1015'
                 productDesc='Natural sugar destroyer, helps balance blood sugar and supports glucose metabolism.'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1017'
                 productDesc='Improves bone-strength, bone metabolism, blood coagulation and vascular biology'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1018'
                 productDesc='Strengthens bones,regulates blood pressure and protects your thyroid.'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1019'
                 productDesc='Meant for general wellness, it\'s a powerful shot of Vitamin A, Vitamin D and Omega-3 fatty acids that enhance heart health.'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1022'
                 productDesc='Herbal tonic for people with rheumatoid arthritis, digestive issues, and acute back pain.'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1023'
                 productDesc='Chinese herbal remedy for several ailments and for general well being. '/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1024'
                 productDesc='Organic vegetarian medicine to add GLA content without any chemical solvent. '/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1025'
                 productDesc='Herbal tonic which increases  nutrition absorption by the body, strengthening the skeletal system.'/>



</div>

<div class="cl"></div>


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

