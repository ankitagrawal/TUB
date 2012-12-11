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
            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">1</a>

            <span class="pages_link">2</span>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf03.jsp">Next →</a>

         </div>

        <div class="cl"></div>


        <div class="prodBoxes">

            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT006' productDesc="Neat Feat roll on Deodoriser aids in preventing foot odour"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT010' productDesc="Give comfort and softness to your tired and worn out feet with Neat Feat Foot Soap Scrub "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NIC001' productDesc="Nicorette reins in your constant urge to smoke. Quit smoking for sure."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH006' productDesc="Get a  cleaner tongue with this carefully tested cleaner blister"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH033' productDesc="Ensure the best possible protection for your teeth with this sonic toothbrush "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH037' productDesc="text due"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH044' productDesc="This handy and foldable toothbrush with diamond rounded filaments cleans even the space between your teeth"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH047' productDesc="For a gentle and efficient cleansing of teeth, get this Trisa toothbrush with flexible heads that absorb excess pressure"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH052' productDesc="A fun toothbrush for you little one, Trisa Junior Toothbrush"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH054' productDesc="text due"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH063' productDesc="Ensure your kid's dental health with easier and deep cleansing vibrations. Only with Trisa Sonic Junior"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH067' productDesc="Cleans your teeth and gums, reduces chances of plaque and maintains your oral health."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH068' productDesc="Maintains your oral hygiene and makes your smile more beautiful."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH069' productDesc="Keeps your teeth clean and shiny."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH078' productDesc="Refreshes your room with fragrance of sandal and jasmine kill all bad odors in your living room"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH092' productDesc="Wipe out all the plaque problems with this 2-in-1 toothbrush, that cleans and massages your teeth and gums"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH094' productDesc="Care for your gums and teeth and keep your dental health intact with this plaque cleaner "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH095' productDesc="text due"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH097' productDesc="With soft bristles for effective cleaning of plaque and bacteria"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH101' productDesc="text due"/>


        </div>
        <div class="cl"></div>

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">1</a>

            <span class="pages_link">2</span>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf03.jsp">Next →</a>

         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

