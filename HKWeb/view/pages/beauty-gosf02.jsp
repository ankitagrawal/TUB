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
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <span class="pages_link">2</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">Next →</a>

          </div>

        <div class="cl"></div>


         <div class="prodBoxes">
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP18' productDesc="Exfoliating scrub with a mystical blend of Licorice, Neem & Tulsi "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY111' productDesc="Light restorative cream to banish dark circles  "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OLY018' productDesc="Moisturize before bed and get firm skin and fight 7 signs of aging."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC07' productDesc="Before- bed cream with Saffron and Kumkumadi to refine, sooth and hydrate skin.   "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GILI07' productDesc='Fine blade edges and lubricating strip that ensures you have a smooth shave with least amount of irritation'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY120' productDesc="Fight blemished skin with an arsenal of LHA and Bio-Assimilated Ceramide "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DIVO12' productDesc="Perfect hairbrush cleaning accessory, ensures no fluff is left behind on your paddle brush"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PLMCL06' productDesc="Infused with Tea Tree, Lavender, Peppermint & Chamomile. Revitalizes, adds vitality, deep cleans and softens tresses."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC2' productDesc="Lush moisturizer to rehydrate and sooth the thirstiest of skins"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='STVKC04' productDesc="Effective herbal treatment for acne-prone skin"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC38' productDesc="Enriched with Lactic Acid, get rid of tanned skin, enhance fairness and achieve even skin tone"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI07' productDesc="Caviar extracts for skin so soft, smooth and radiant, it glows from within."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SHNZ06' productDesc="Extracts of Vetiver and Dates with Diamond and Tankana, purifies skin and improves cell regeneration"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC15' productDesc="Saffron and Sandal essence work together to keep skin hydrated, refreshed and free from spots.  "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VICHY4' productDesc="Effective weapon to fight against UV rays. Non sticky, ultra-protective emulsion with micro-sized filtering particles."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY764' productDesc="Ground Walnut and Apricot exfoliates & rehydrates skin. Soothing Almond, Rosemary and Lemon oil detoxifies & rejuvenates dull skin.   "/>
			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='STVKC05' productDesc="Comprehensive solution for problematic skin, reduces excess oil, pacifies sebum formation and regulates hormonal imbalance."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='STVKC18' productDesc="Anti-frizz conditioner that gently smoothes cuticles, reduces friction and helps fortify hair."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PNOG14' productDesc="Discover youthful radiance in 5 easy steps. Essential oils and herbal extract, balances and replenishes dull, dehydrated skin."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PNOG15' productDesc="Skin-beneficial nutrients offer dollops of moisture, nourishes, balances and restores sun-damaged skin."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PNOG34' productDesc="Brown sugar gently scrubs rough surface cells while nourishing Cocoa and Shea butter conditions deeply."/>			                
			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MYBLN31' productDesc="Smudge-proof, creamy gel comes in two distinct shades that can be used separately for either an intense line or dramatic shine."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='REV19' productDesc="A buttery balm that easily glides on your lips. Hydrates, nourishes, tints to give you luscious, healthy glowing lips."/>
        </div>
        <div class="cl"></div>

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <span class="pages_link">2</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">Next →</a>

          </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

