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


        <div class="heading1">BEAUTY</div>

         <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">Next →</a>

          </div>

        <div class="cl"></div>


         <div class="prodBoxes">


<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI07' productDesc="Caviar extracts for skin so soft, smooth and radiant, it glows from within."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI10' productDesc="Exfoliating soap that makes skin soft and smooth. The ground Apricot and Mulberry keeps dry skin at bay. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OLY018' productDesc="Moisturize before bed and get firm skin and fight 7 signs of aging."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OLY019' productDesc="Anti-ageing cream that minimizes wrinkles & eye bags,moisturizes delicate under eye skin and diminishes spots"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ORIY18' productDesc="Essence of Saw Palmetto, Pumpkin Seed Oil, Stinging Nettle and Ginseng extract, prevents pattern baldness and diffused hair loss."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ORIY19' productDesc="Essence of Grape Seed, Resveratrol, Pine Bark, Saffron & Green Tea, protects and lightens skin."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PLMCL06' productDesc="Infused with Tea Tree, Lavender, Peppermint & Chamomile. Revitalizes, adds vitality, deep cleans and softens tresses."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PUCL3' productDesc="Long-lasting fusion of fruity and floral notes.Handbag-friendly size."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SENRA4' productDesc="Cover cleavage effectively, an easy snap on for your bra. It is comfortable and washable."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SHNZ06' productDesc="Extracts of Vetiver and Dates with Diamond and Tankana, purifies skin and improves cell regeneration"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='STVKC04' productDesc="Effective herbal treatment for acne-prone skin"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TPIC01' productDesc="Natural Keratin fibers intertwine seamlessly for thicker and fuller hair, endures rain, wind & perspiration. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VAADI36' productDesc="Prevents premature ageing, wrinkling of skin, relaxes muscles, makes skin smooth and gives deeper sleep."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC12' productDesc="Saffron extracts evens the skin color, hydrates, offers suppleness and protects against UV rays."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC2' productDesc="Lush moisturizer to rehydrate and sooth the thirstiest of skins"/>


                

         </div>
        <div class="cl"></div>
        
         <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">Next →</a>

          </div>

    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

