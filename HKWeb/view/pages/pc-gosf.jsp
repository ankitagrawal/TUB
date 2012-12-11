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
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP002' productDesc="Protect yourself with this powerful pepper formula spray."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL002' productDesc="Figure out the best two days you will be most fertile. Grab this I-Sure Ovulation Strip to get pregnant for sure. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP001' productDesc="Protect yourself with this powerful pepper formula spray."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KNOCK001' productDesc="Protect yourself with this powerful pepper formula spray."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NIC001' productDesc="Nicorette reins in your constant urge to smoke. Quit smoking for sure."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH101' productDesc=""/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS041' productDesc='Stroll your little one in this buggy inspired Sunbaby Stroller, having cushioned seat and a hood protecting him from sun.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOS001' productDesc="This Super King Size net protects your little one against dengue, malaria and other diseases caused by insects"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL004' productDesc="keep your hands germ free with this anti-bacterial hand sanitizer "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH006' productDesc="Get a  cleaner tongue with this carefully tested cleaner blister"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS052' productDesc='Calm down your crying baby with this musical toy having soft and soothing lights, and which activates to the sound of cry. '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP091' productDesc="Switch to Roots Hair Builder for thick and shiny hair instantly"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS020' productDesc="Sensational pleasure for your partner with long lasting dotted condoms "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='COP004' productDesc="This personal safety alert device warns you when someone tries to tamper with things that belong to you"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='COP002' productDesc="Ensure more security to yourself with this advanced Multi-functional Alarm cum Pepper Spray."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FER010' productDesc='Help your baby sleep in the melodious music and soothing lights of Chicco Sweetheart Bear.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OPT001' productDesc="Ideal for oral and underarm use"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HV001' productDesc='Ensure your babys health with the Nuby Microwave Steam Sterilizer by keeping his food and water germ free'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH044' productDesc="This handy and foldable toothbrush with diamond rounded filaments cleans even the space between your teeth"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOR002' productDesc="Infused with carbamide peroxide, it cleans your teeth and makes them more white and shiny."/>        	            
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

