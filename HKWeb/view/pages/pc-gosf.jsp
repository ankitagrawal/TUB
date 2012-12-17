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

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">Next →</a>

         </div>

        <div class="cl"></div>


        <div class="prodBoxes">
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PW006' productDesc="Test your pregnancy in just 2 minutes with this Instant Ovulation Kit"/>
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH082' productDesc="Freshen up your house with floral fragrances."/>
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DET002' productDesc="Automatically senses hands and dispenses just the right amount of liquid soap."/>
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH063' productDesc="A battery operated toothbrush that cleans your toddlers teeth gently and effectively."/>			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL002' productDesc="Figure out the best two days you will be most fertile. Grab this I-Sure Ovulation Strip to get pregnant for sure. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP001' productDesc="You can neutralize up to 10 people (not kidding). Comes with step lock mechanism for safe & easy carrying."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KNOCK001' productDesc="A Pepper Spray that fits into a keychain! Small, portable & extremely effective!"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NIC001' productDesc="Nicorette reins in your constant urge to smoke. Quit smoking for sure."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH101' productDesc="Toothbrush heads for your favorite electric tooth brush! Get the best head for that winning smile!"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS041' productDesc="Because being fore armed is better than being fore warned. Take control today!"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOS001' productDesc="This Super King Size net protects your little one against dengue, malaria and other diseases caused by insects"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH006' productDesc="Get a  cleaner tongue with this carefully tested cleaner blister"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS052' productDesc='Passion taken to new heights. Honeymoon pack has accessories that takes care of everything from the foreplay to the morning after'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP091' productDesc="Switch to Roots Hair Builder for thick and shiny hair instantly"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS020' productDesc="Sensational pleasure for your partner with long lasting dotted condoms "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='COP004' productDesc="This personal safety alert device warns you when someone tries to tamper with things that belong to you"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='COP002' productDesc="Ensure more security to yourself with this advanced Multi-functional Alarm cum Pepper Spray."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FER010' productDesc="Put an end to Vaginal Dryness with Pre Seed - a sperm friendly lubricant."/>			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HV001' productDesc="Use HealthViva adult diapers are easy to use, have secure fit and are odor free."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH044' productDesc="This handy and foldable toothbrush with diamond rounded filaments cleans even the space between your teeth"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FER006' productDesc="100% natural dietary supplement for both men and women, detoxifies, improves reproductive health and enhances fertility."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH004' productDesc="Pre-loaded dental floss holder for easy and comfortable cleaning between the teeth."/>        	            
        </div>
        <div class="cl"></div>

        <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">Next →</a>

         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

