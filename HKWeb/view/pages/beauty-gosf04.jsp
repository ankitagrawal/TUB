<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Cyber Monday Great Online Shopping Festival">

<s:layout-component name="htmlHead">
<link href="${pageContext.request.contextPath}/css/gosf.css" rel="stylesheet" type="text/css" />


</s:layout-component>

<s:layout-component name="breadcrumbs">
    <div class='crumb_outer'>
        <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
        &gt;
        <span class="crumb last" style="font-size: 12px;">Cyber Monday</span>

        <h1 class="title">
            Cyber Monday Great Online Shopping Festival
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


        <div class="heading1">BEAUTY</div>

         <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <span class="pages_link">4</span>


          </div>

        <div class="cl"></div>


         <div class="prodBoxes">


<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC21' productDesc="Antiseptic and gentle wash to diminish pigmentation, acne, open pores & oily skin. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC25' productDesc="Moisturizing gel with anti-oxidant nutrients, nourishes and maintains oil balance of the skin"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC38' productDesc="Enriched with Lactic Acid, get rid of tanned skin, enhance fairness and achieve even skin tone"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC4' productDesc="Dissolves when in contact with warm skin, Lavender oil decreases melanin & clears stubborn hyper pigmentation spots and acne marks. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC46' productDesc="Goodness of Bhringraj, Glycerin and Soy oil, conditions and hydrates dead locks. Absolute nourishment for chemically treated hair."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC64' productDesc="Makeup and skincare in one, it easily blends, protects skin from UV rays and leaves an undetectable finish."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VICHY4' productDesc="Effective weapon to fight against UV rays. Non sticky, ultra-protective emulsion with micro-sized filtering particles."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT22' productDesc="Incredibly soothing body lotion for dry &troubled skin"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP09' productDesc="For faces seeking balance, nourishment and deep cleansing"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP18' productDesc="Exfoliating scrub with a mystical blend of Licorice, Neem & Tulsi "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP31' productDesc="Infused with Sweet Almond and Avocado oil,  relieves stress, improves blood circulation and reduces muscle pains."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP42' productDesc=" Rich skin nutrients that fades wrinkles, rejuvenates and provides volume and fullness to skin"/>
                

         </div>
        <div class="cl"></div>
                                       
        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <span class="pages_link">4</span>


          </div>

    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

