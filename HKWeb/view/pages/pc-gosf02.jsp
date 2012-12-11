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
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH067' productDesc="Cleans your teeth and gums, reduces chances of plaque and maintains your oral health."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH078' productDesc="Refreshes your room with fragrance of sandal and jasmine kill all bad odors in your living room"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VAC001-01' productDesc='Let your child move with care and comfort in this stylish and versatile Chicco Juvenile Stroller.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TP001-01' productDesc='Made of cotton and rayon, Perfect for those with high mentrual flow and hate changing napkins constantly.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH069' productDesc="Keeps your teeth clean and shiny."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH033' productDesc="Ensure the best possible protection for your teeth with this sonic toothbrush "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH063' productDesc="Ensure your kid's dental health with easier and deep cleansing vibrations. Only with Trisa Sonic Junior"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP070' productDesc="For a thorough and gentle cleansing of your skin, use Bel Premium Cotton Balls, with Aloe Vera and Provitamin B5"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS050' productDesc="Climax for men makes sure the partner is left screaming"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AT002' productDesc="Designed with high absorbent mechanism, it is easy,comfortable and hygienic to wear."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH068' productDesc="Maintains your oral hygiene and makes your smile more beautiful."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LIF003' productDesc="Easy to use refill pack for your hand wash dispenser"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH092' productDesc="Wipe out all the plaque problems with this 2-in-1 toothbrush, that cleans and massages your teeth and gums"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH052' productDesc="A fun toothbrush for you little one, Trisa Junior Toothbrush"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PF049' productDesc="Gel heel shields that provide comfort and pain relief for those who constantly have to wear high heels"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOB002' productDesc="Gift your elders, their old age companion in form of Arpan phone, having enhanced sound and large and talking keypad."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH047' productDesc="For a gentle and efficient cleansing of teeth, get this Trisa toothbrush with flexible heads that absorb excess pressure"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH054' productDesc="Refill your Trisa Sonic Power Toothbrush with these heads, having vibrating filaments as smooth as silk."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AT003' productDesc="Attends Slip Regular, with an in-built odour system checks unpleasant smell,ensures comfort, hygiene, and security"/>			
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

