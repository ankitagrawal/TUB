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


        <div class="heading1">PERSONAL CARE</div>

        <div class="pages">


            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

         </div>

        <div class="cl"></div>


        <div class="prodBoxes">
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP002' productDesc="With this pepper spray, you get a range of 7 feet. Knock them all out!"/>        
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='JS003' productDesc="Brush better with JSB Electronic Toothbrush,"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT001' productDesc="Neat Feat 3B Action Cream is a perfect pick for curing sweat rash and chafing"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT003' productDesc="Rehydrate and repair the damaged and dried heels of your feet with this foot and heel balm"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH037' productDesc="Ensure healthy and shining white teeth with this premium quality Trisa Clinical white Toothpaste."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH094' productDesc="Care for your gums and teeth and keep your dental health intact with this plaque cleaner "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH095' productDesc="Get an effective cleaning of your teeth with this rechargeable toothbrush which reaches interdental areas of your mouth."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AIR005' productDesc="Freshen up your room with this air freshener that comes in great fragrances"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT005' productDesc="This foot and leg moisturizer, with a rich moisturizing formula for dry skin and cracked heels"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT006' productDesc="Neat Feat roll on Deodoriser aids in preventing foot odour"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH097' productDesc="With soft bristles for effective cleaning of plaque and bacteria"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE005' productDesc="Disk Roller Body Massager is the answer to your muscle pain and extra body fat"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MIDAS005' productDesc="Refresh your home and office with the natural and rich fragrance of Lovin Air Freshener "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PF044' productDesc="For maximum foot protection, use this Neat Feat Diabetic Self Moulding Sole "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE001' productDesc="This face and body massager delivers minute acoustic vibrations, curing muscle and joint pain "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE002' productDesc="FaceMate's Dual Wave Ultrasonic Face Massager revitalizes your skin using safe ultrasonic technology "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE003' productDesc="Relax your muscle and energize your body with this multi-purpose warm massager"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE004' productDesc="Get a wrinkle free, healthy and  a firmer skin and no side effects with this muscle stimulator"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH069' productDesc="Keeps your teeth clean and shiny."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH033' productDesc="Ensure the best possible protection for your teeth with this sonic toothbrush "/>			        
        </div>
        <div class="cl"></div>

         <div class="pages">


            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

