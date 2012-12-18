<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Great Online Shopping Festival">

<s:layout-component name="htmlHead">
<link href="${pageContext.request.contextPath}/css/gosf.css" rel="stylesheet" type="text/css" />


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
<s:layout-component name="metaKeywords"></s:layout-component>


<s:layout-component name="content">


    <!---- paste all content from here--->

    <div id="pageContainer">
        <img src="${pageContext.request.contextPath}/images/GOSF/main-banner.jpg"/>

        <div class="cl"></div>

        <jsp:include page="../includes/_menuGosf.jsp"/>

        <div class="cl"></div>


        <div class="heading1">PROTEINS & MASS GAINERS</div>

		<div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-protein-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-protein-gosf02.jsp">Next →</a>

         </div>

        <div class="cl"></div>


        <div class="prodBoxes">
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT130' productDesc='Provides 11g of Leucine and 13g of additional BCAAs per serving. Ideal for rapid muscle gain and recovery.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1368' productDesc='Designed for people who want to gain mass. Packed with 52gm of high quality protein blend and 9gm of added fiber.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1367' productDesc='Tailored for fitness enthusiasts engaged in heavy workouts, it has 25gm of 100% Whey Protein blend and EAAs per serving.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT905' productDesc='For bodybuilders who want to gain mass. Infused with 1000 calories, 70g proteins and 10g Branched-Chain Amino Acids.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT131' productDesc='Get the purest whey protein money can buy. All this with no carbs whatsoever. '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT590' productDesc='Packed with low fat, high protein and the best taste in one package – MuscleTech Premium Whey Protein. Suitable for all gym goers.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT056' productDesc='Get the best carb free, lactose free whey protein known to man. Suitable for bodybuilding & athletic training alike. '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT468' productDesc='Increased muscle mass and strength along with improved bones and ligaments'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT911' productDesc='This products not just saturates aching muscles with right kind of amino acids, it also works as a nutritious health supplement'/>
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT912' productDesc='Packed with carbohydrates, vitamins and proteins that ensure quick results. It also minimizes muscular degradation.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1162' productDesc='Built with Vitamins A and E and Zinc and Selenium, it offers maximum nutrient utilization and prevents muscle loss '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1227' productDesc='A pre-workout supplement that acts as a Nitric Oxide Pump Amplifier and contains Pycnogenol as its key ingredient.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT912' productDesc='It helps one gain weight and get increased muscle mass by reducing the degradation of muscles.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT350' productDesc='Packed with amino acids, it is a pill-based formulation that helps in muscle growth, repair and maintenance.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1172' productDesc='A unique blend for healthy metabolism and muscle gain. Excellent for hard training'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT891' productDesc='An award winning muscle building formula. The 7 stage approach repairs muscles  and optimizes post workout growth.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT574' productDesc='High performance whey protein blend.  A superior mix, you absorb upto 80% protein instantly.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT486' productDesc='A unique blend of pure crystalline Ergogenic amino acids which acts as anabolic agent and promotes muscle protein synthesis'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT132' productDesc='Get the best Creatine available to mankind and get unbelievable power and muscle gains. Go with CellTech hardcore to beat the pros.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT472' productDesc='Ideal for assisting your muscles post workout, it is a whey protein isolate that is 100% instantised and hydrolysed. '/>			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1339' productDesc='Packed with whey protein that increases stamina for an improved workout and leaner muscles.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT825' productDesc='This weight gainer is a perfect blend of all the vital proteins and vitamins. Get ready for that chiseled physique.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1332' productDesc='A high calorie weight gainer with amino acids that helps increase muscle size and strength.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT125' productDesc='Quickly build well-toned muscles.This supplement supports muscle growth and peaks performance.'/>															
         </div>


        <div class="cl"></div>
 

        <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-protein-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-protein-gosf02.jsp">Next →</a>

         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

