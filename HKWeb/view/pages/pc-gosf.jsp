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
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AIR005' productDesc="Freshen up your room with this air freshener that comes in great fragrances"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AT002' productDesc="Designed with high absorbent mechanism, it is easy,comfortable and hygienic to wear."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AT003' productDesc="Attends Slip Regular, with an in-built odour system checks unpleasant smell,ensures comfort, hygiene, and security"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='COP002' productDesc="text due"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='COP004' productDesc="This personal safety alert device warns you when someone tries to tamper with things that belong to you"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE001' productDesc="This face and body massager delivers minute acoustic vibrations, curing muscle and joint pain "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE002' productDesc="FaceMate's Dual Wave Ultrasonic Face Massager revitalizes your skin using safe ultrasonic technology "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE003' productDesc="Relax your muscle and energize your body with this multi-purpose warm massager"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE004' productDesc="Get a wrinkle free, healthy and  a firmer skin and no side effects with this muscle stimulator "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE005' productDesc="Disk Roller Body Massager is the answer to your muscle pain and extra body fat"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='JS003' productDesc="Brush better with JSB Electronic Toothbrush,"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KNOCK001' productDesc="Protect yourself with this powerful pepper formula spray."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LIF003' productDesc="Easy to use refill pack for your hand wash dispenser"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MIDAS005' productDesc="Refresh your home and office with the natural and rich fragrance of Lovin Air Freshener "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOB002' productDesc="text due"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOR002' productDesc="Infused with carbamide peroxide, it cleans your teeth and makes them more white and shiny."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOS001' productDesc="This Super King Size net protects your little one against dengue, malaria and other diseases caused by insects"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT001' productDesc="Neat Feat 3B Action Cream is a perfect pick for curing sweat rash and chafing "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT003' productDesc="Rehydrate and repair the damaged and dried heels of your feet with this foot and heel balm "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT005' productDesc="This foot and leg moisturizer, with a rich moisturizing formula for dry skin and cracked heels"/>

            
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

